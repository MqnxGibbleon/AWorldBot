package net.gib.util;



import net.gib.storage.cloud.mySql.MySQL;
import org.json.JSONObject;

public record ProjectData(String ip, String username, String database, String password, String token, int port) {

    public static ProjectData get(String path, String name) throws Exception {
        JSONObject json = ResourceDirectory.getJson(path,name);
        if (json.has("ip") && json.has("username") && json.has("database")
                && json.has("password") && json.has("token") && json.has("port")) {
            var ip = json.getString("ip");
            var username = json.getString("username");
            var database = json.getString("database");
            var password = json.getString("password");
            var token = json.getString("token");
            var port = json.getInt("port");
            return new ProjectData(ip,username,database,password,token,port);
        }else throw new Exception("Project Data needs ip, username,database,password,token,port");
    }

    public MySQL getMySql(MySQL.DataBaseType type) {
        return new MySQL(ip, String.valueOf(port),username,password,database,type);
    }

}
