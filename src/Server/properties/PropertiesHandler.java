package Server.properties;


import Server.Database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHandler {

    // read and return properties from properties file
    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream in = new FileInputStream("properties/server.properties")) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    // sets numberOfRounds and numberOfQuestions with a key-value pair,
    // uses the defaultValue if key is not found in the file
    public static void applyProperties() {
        Properties properties = PropertiesHandler.loadProperties();
        Database.setNumberOfRounds(Integer.parseInt(properties.getProperty("NUMBER_OF_ROUNDS", "3")));
        Database.setNumberOfQuestions(Integer.parseInt(properties.getProperty("NUMBER_OF_QUESTIONS", "4")));
    }
}
