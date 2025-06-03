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
}
