package net.gib.storage.cloud.mySql;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface CloudStorage {

    String getTable();

//    default List<DBObject> getStorageValue() {
//        var objects = new ArrayList<DBObject>();
//        for (Field field : this.getClass().getDeclaredFields()) {
//            if (!field.isAnnotationPresent(SaveAs.class)) continue;
//            field.setAccessible(true);
//            Object value = null;
//            try {
//                value = field.get(this);
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//            if (value == null) continue;
//            var safeAs = field.getAnnotation(SaveAs.class).mySql();
//            if (!value.getClass().isPrimitive()) {
//                objects.add(new DBObject(safeAs, value, field.getName()));
//                continue;
//            }
//            objects.add(new DBObject(safeAs, value.toString(), field.getName()));
//        }
//        return objects;
//    }
//
//    default void createTable() {
//        List<DBObject> objs = getStorageValue();
//        if (objs.isEmpty()) return;
//        var builder = new StringBuilder();
//        builder.append(objs.get(0).serializeStatement());
//        objs.remove(0);
//        for (DBObject obj : objs) {
//            builder.append(",");
//            builder.append(obj.serializeStatement());
//        }
//        var stm = "CREATE TABLE IF NOT EXISTS " + getTable() + " (" + builder + ")";
//
//        try (var ps = getMySql().getConnection().prepareStatement(stm)) {
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    default void insert() {
//        List<DBObject> objs = getStorageValue();
//        if (objs.isEmpty()) return;
//        var qBuilder = new StringBuilder();
//        for (int i = 0; i < objs.size(); i++) {
//            if (i == 0) {
//                qBuilder.append("?");
//                continue;
//            }
//            qBuilder.append(",?");
//        }
//        var builder = new StringBuilder();
//        builder.append(objs.get(0).name());
//        objs.remove(0);
//        for (DBObject obj : objs) {
//            builder.append(",");
//            builder.append(obj.name());
//        }
//
//        var stm = "INSERT INTO " + getTable() + " (" + builder + ") VALUES " + "(" + qBuilder + ")";
//
//        if (isAlreadyStored()) {
//            update();
//            return;
//        }
//        try (var ps = getMySql().getConnection().prepareStatement(stm)) {
//            for (int i = 0; i < getStorageValue().size(); i++) {
//                ps.setObject(i + 1, getStorageValue().get(i).value());
//            }
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    default void update() {
//        List<DBObject> objs = getStorageValue();
//        if (objs.isEmpty()) return;
//        if (objs.size() < 2) return;
//        DBObject key = objs.get(0);
//        var builder = new StringBuilder();
//        objs.remove(0);
//        builder.append(objs.get(0).name());
//        builder.append(" = ?");
//        objs.remove(0);
//        for (DBObject obj : objs) {
//            builder.append(",");
//            builder.append(obj.name());
//            builder.append(" = ?");
//        }
//
//
//        var stm = "UPDATE " + getTable() + " SET " + builder + " WHERE " + key.name() + " = " + key.value();
//        try (var ps = getMySql().getConnection().prepareStatement(stm)) {
//            for (int i = 0; i < getStorageValue().size() - 1; i++) {
//                ps.setObject(i + 1, getStorageValue().get(i + 1).value());
//            }
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    default void load() {
//        List<Field> fields = new ArrayList<>();
//        for (Field declaredField : this.getClass().getDeclaredFields()) {
//            if (!declaredField.isAnnotationPresent(SaveAs.class)) continue;
//            fields.add(declaredField);
//        }
//        if (fields.size() < 2) return;
//        var builder = new StringBuilder();
//        var key = fields.get(0).getName();
//        fields.remove(0);
//        var f1 = fields.get(0);
//        builder.append(f1.getName());
//        fields.remove(0);
//        for (Field field : fields) {
//            builder.append(",");
//            builder.append(field.getName());
//        }
//        fields.add(0, f1);
//        var stm = "SELECT " + builder + " FROM " + getTable() + " WHERE " + key + " = " + this.getStorageValue().get(0).value();
//        try (var ps = getMySql().getConnection().prepareStatement(stm)) {
//            var result = ps.executeQuery();
//            if (!result.next()) return;
//            for (Field field : fields) {
//                var f = this.getClass().getDeclaredField(field.getName());
//                f.setAccessible(true);
//                f.set(this, result.getObject(field.getName()));
//            }
//        } catch (SQLException | NoSuchFieldException | IllegalAccessException se) {
//            throw new RuntimeException(se);
//        }
//    }
//
//    default boolean isAlreadyStored() {
//        var key = getStorageValue().get(0);
//        try (var ps = getMySql().getConnection().prepareStatement("SELECT COUNT(*) as c FROM " + getTable() + " WHERE " + key.name() + " = " + key.value())) {
//            var result = ps.executeQuery();
//            result.next();
//            return 0 < result.getInt("c");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    default void refreshTable() {
//        try (var ps = getMySql().getConnection().prepareStatement("DROP TABLE IF EXISTS " + getTable())) {
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        createTable();
//    }
//
//    default void drop() {
//        var key = getStorageValue().get(0);
//        try (var ps = getMySql().getConnection().prepareStatement("DELETE FROM " + getTable() + " WHERE " + key.name() + " = " + key.value())) {
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    default void updateSingle(String name) {
//        var key = getStorageValue().get(0);
//        var fields = getClass().getDeclaredFields();
//        Field nField = null;
//        for (Field field : fields) {
//            if (!field.getName().equals(name)) continue;
//            nField = field;
//        }
//        if (nField == null) return;
//        nField.setAccessible(true);
//        try (var ps = getMySql().getConnection().prepareStatement("UPDATE " + getTable() + " SET " + name + "= ? WHERE " + key.name() + " = " + key.value())) {
//            ps.setObject(1, nField.get(this));
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//
//    MySQL getMySql();


}
