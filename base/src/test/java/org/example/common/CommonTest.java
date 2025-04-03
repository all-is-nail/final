package org.example.common;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;

public class CommonTest {

    @Test
    public void testProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(Files.newInputStream(Paths.get("src/main/resources/example.properties")));
        System.out.println(properties);
    }

    /**
     * use HashMap to read properties file
     *
     * @throws IOException
     */
    @Test
    public void testPropertiesWithHashMap() throws IOException {
        Properties properties = new Properties();
        properties.load(Files.newInputStream(Paths.get("src/main/resources/example.properties")));

        Map<String, String> propertiesMap = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            propertiesMap.put(key, properties.getProperty(key));
        }

        System.out.println(propertiesMap);
    }
}
