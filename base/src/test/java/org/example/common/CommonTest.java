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

    /**
     * Test BigDecimal operations with different precision
     */
    @Test
    public void testBigDecimal() {
        // Create BigDecimal instances with different precision and scale
        java.math.BigDecimal bd1 = new java.math.BigDecimal("10.555");
        java.math.BigDecimal bd2 = new java.math.BigDecimal("2.33");
        System.out.println("bd1 precision is " + bd1.precision());
        System.out.println("bd1 scale is " + bd1.scale());
        System.out.println("bd2 precision is " + bd2.precision());
        System.out.println("bd2 scale is " + bd2.scale());

        // Perform arithmetic operations with different precision
        System.out.println("Addition: " + bd1.add(bd2));
        System.out.println("Subtraction: " + bd1.subtract(bd2));
        System.out.println("Multiplication: " + bd1.multiply(bd2));
        // Division with scale and rounding mode
        System.out.println("Division (2 decimal places): " + 
            bd1.divide(bd2, 2, java.math.RoundingMode.HALF_UP));
        System.out.println("Division (3 decimal places): " + 
            bd1.divide(bd2, 3, java.math.RoundingMode.HALF_UP));

        // Compare BigDecimals
        System.out.println("Comparison: " + bd1.compareTo(bd2));
    }
}
