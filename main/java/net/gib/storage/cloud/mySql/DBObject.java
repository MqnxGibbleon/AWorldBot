package net.gib.storage.cloud.mySql;

public record DBObject(String safeAs, Object value, String name) {

    public String serializeStatement() {
        return name + " " + safeAs;
    }

}
