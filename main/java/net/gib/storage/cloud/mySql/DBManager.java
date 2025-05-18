package net.gib.storage.cloud.mySql;



import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DBManager<E extends CloudStorage>{

    private E storable;
    private final MySQL mySQL;

    public DBManager(E storable, MySQL mySQL) {
        this.storable = storable;
        this.mySQL = mySQL;
    }

    public E get() {
        return storable;
    }

    public void put(E storable) {
        this.storable = storable;
    }

    public MySQL getmySQL() {
        return mySQL;
    }

    public List<DBObject> getStorageValue() {
        var objects = new ArrayList<DBObject>();
        for (Field field : storable.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(SaveAs.class)) continue;
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(storable);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (value == null) continue;
            var safeAs = field.getAnnotation(SaveAs.class).mySql();
            if (!value.getClass().isPrimitive()) {
                objects.add(new DBObject(safeAs, value, field.getName()));
                continue;
            }
            objects.add(new DBObject(safeAs, value.toString(), field.getName()));
        }
        return objects;
    }

    public void createTable() {
        List<DBObject> objs = getStorageValue();
        if (objs.isEmpty()) return;
        var builder = new StringBuilder();
        builder.append(objs.get(0).serializeStatement());
        objs.remove(0);
        for (DBObject obj : objs) {
            builder.append(",");
            builder.append(obj.serializeStatement());
        }
        var stm = "CREATE TABLE IF NOT EXISTS " + storable.getTable() + " (" + builder + ")";

        try (var ps = mySQL.getConnection().prepareStatement(stm)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert() {
        List<DBObject> objs = getStorageValue();
        if (objs.isEmpty()) return;
        var qBuilder = new StringBuilder();
        for (int i = 0; i < objs.size(); i++) {
            if (i == 0) {
                qBuilder.append("?");
                continue;
            }
            qBuilder.append(",?");
        }
        var builder = new StringBuilder();
        builder.append(objs.get(0).name());
        objs.remove(0);
        for (DBObject obj : objs) {
            builder.append(",");
            builder.append(obj.name());
        }

        var stm = "INSERT INTO " + storable.getTable() + " (" + builder + ") VALUES " + "(" + qBuilder + ")";

        if (isAlreadyStored()) {
            update();
            return;
        }
        try (var ps = mySQL.getConnection().prepareStatement(stm)) {
            for (int i = 0; i < getStorageValue().size(); i++) {
                ps.setObject(i + 1, getStorageValue().get(i).value());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        List<DBObject> objs = getStorageValue();
        if (objs.isEmpty()) return;
        if (objs.size() < 2) return;
        DBObject key = objs.get(0);
        var builder = new StringBuilder();
        objs.remove(0);
        builder.append(objs.get(0).name());
        builder.append(" = ?");
        objs.remove(0);
        for (DBObject obj : objs) {
            builder.append(",");
            builder.append(obj.name());
            builder.append(" = ?");
        }


        var stm = "UPDATE " + storable.getTable() + " SET " + builder + " WHERE " + key.name() + " = " + key.value();
        try (var ps = mySQL.getConnection().prepareStatement(stm)) {
            for (int i = 0; i < getStorageValue().size() - 1; i++) {
                ps.setObject(i + 1, getStorageValue().get(i + 1).value());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void load() {
        var fields = getFields();
        if (fields.size() < 2) return;
        var builder = new StringBuilder();
        var key = fields.get(0).getName();
        fields.remove(0);
        var f1 = fields.get(0);
        builder.append(f1.getName());
        fields.remove(0);
        for (Field field : fields) {
            builder.append(",");
            builder.append(field.getName());
        }
        fields.add(0, f1);
        load(builder.toString(), key,fields.toArray(new Field[0]));
    }

    public void loadSingle(String id) {
        var fields = getFields();
        if (fields.size() < 2) return;
        var key = fields.get(0).getName();
        var field = fields.stream().filter(field1 -> field1.getName().equals(id)).toList();
        if (field.isEmpty()) return;
        load(id,key,field.get(0));
    }

    private List<Field> getFields() {
        List<Field> fields = new ArrayList<>();
        for (Field declaredField : storable.getClass().getDeclaredFields()) {
            if (!declaredField.isAnnotationPresent(SaveAs.class)) continue;
            fields.add(declaredField);
        }
        return fields;
    }

    private void load(String id, String key, Field... fields) {
        var stm = "SELECT " + id + " FROM " + storable.getTable() + " WHERE " + key + " = " + getStorageValue().get(0).value();
        try (var ps = mySQL.getConnection().prepareStatement(stm)) {
            var result = ps.executeQuery();
            if (!result.next()) return;
            for (Field field : fields) {
                var f = storable.getClass().getDeclaredField(field.getName());
                f.setAccessible(true);
                f.set(storable, result.getObject(field.getName()));
            }
            result.close();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException se) {
            throw new RuntimeException(se);
        }
    }

    public boolean isAlreadyStored() {
        var key = getStorageValue().get(0);
        try (var ps = mySQL.getConnection().prepareStatement("SELECT COUNT(*) as c FROM " + storable.getTable() + " WHERE " + key.name() + " = " + key.value())) {
            var result = ps.executeQuery();
            result.next();
            return 0 < result.getInt("c");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshTable() {
        try (var ps = mySQL.getConnection().prepareStatement("DROP TABLE IF EXISTS " + storable.getTable())) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        createTable();
    }

    public void drop() {
        var key = getStorageValue().get(0);
        try (var ps = mySQL.getConnection().prepareStatement("DELETE FROM " + storable.getTable() + " WHERE " + key.name() + " = " + key.value())) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateSingle(String name) {
        var key = getStorageValue().get(0);
        var fields = getClass().getDeclaredFields();
        Field nField = null;
        for (Field field : fields) {
            if (!field.getName().equals(name)) continue;
            nField = field;
        }
        if (nField == null) return;
        nField.setAccessible(true);
        try (var ps = mySQL.getConnection().prepareStatement("UPDATE " + storable.getTable() + " SET " + name + "= ? WHERE " + key.name() + " = " + key.value())) {
            ps.setObject(1, nField.get(storable));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public void loadAll(Consumer<Object> consumer) {
        var key = getStorageValue().get(0);
        try (var ps = mySQL.getConnection().prepareStatement("SELECT " + key.name() + " FROM " + storable.getTable())){
            var result = ps.executeQuery();

            while (result.next()) {
                consumer.accept(result.getObject(key.name()));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTable() {
        var s = "ALTER TABLE `players`\n" +
                "\tADD COLUMN `trophies` INT(10) NULL DEFAULT '0' AFTER `knowledge`;";
    }
}
