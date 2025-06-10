package org.example.optional;

import org.junit.Test;

import java.util.Optional;

public class OptionalTest {

    @Test
    public void testOptional() {
        Optional<String> optional = Optional.of("Hello, World!");
        System.out.println(optional.get());
    }

    @Test
    public void testOptional2() {
        Optional<String> optional = Optional.ofNullable(null);
        // if optional is null, return "Default Value"
        System.out.println(optional.orElse("Default Value"));
    }

    @Test
    public void testOptional3() {
        Optional<String> optional = Optional.ofNullable(null);
        // if optional is null, return "Default Value"
        System.out.println(optional.orElseGet(() -> "Default Value"));
    }

    @Test
    public void testOptionalMap() {
        // Create an Optional with a string value
        Optional<String> optional = Optional.of("Hello, World!");
        
        // Use map to transform the value if present
        // use lambda expression to transform the value
        Optional<Integer> lengthOptional = optional.map(str -> str.length());
        
        // Print the transformed value
        System.out.println("Original string: " + optional.get());
        System.out.println("String length: " + lengthOptional.get());
        
        // Example with null value
        Optional<String> nullOptional = Optional.ofNullable(null);
        // use method reference to transform the value
        Optional<Integer> nullLengthOptional = nullOptional.map(String::length);
        
        // Print the result (will be empty Optional)
        System.out.println("Null optional length: " + nullLengthOptional.orElse(-1));
    }
}
