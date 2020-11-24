package Server.properties;


import Server.Database;

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

    public static void loadProperties() {
        Properties properties = PropertiesHandler.getProperties();
        Database.setNumberOfRounds(Integer.parseInt(properties.getProperty("NUMBER_OF_ROUNDS", "3")));
        Database.setNumberOfQuestions(Integer.parseInt(properties.getProperty("NUMBER_OF_QUESTIONS", "4")));
    }
}
