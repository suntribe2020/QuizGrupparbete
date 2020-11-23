package Server.properties;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHandler {

    public static Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream in = new FileInputStream("properties/server.properties")) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
