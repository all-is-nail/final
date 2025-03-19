package org.example.stream;

import org.example.model.stream.Person;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamOperationTest {

    /**
     * Tests the map operation.
     */
    @Test
    public void testMap() {
        List<Person> people = Arrays.asList(
                new Person("Alice", Arrays.asList("Reading", "Swimming")),
                new Person("Bob", Arrays.asList("Cycling", "Coding")),
                new Person("Charlie", Arrays.asList("Dancing", "Cooking", "Reading"))
        );

        List<String> names = people.stream()
                // Map each person to their name
                // one-to-one relationship
                .map(Person::getName)
                .collect(Collectors.toList());

        System.out.println(names);
    }

    /**
     * Tests the flatMap operation.
     */
    @Test
    public void testFlatMap() {
        List<Person> people = Arrays.asList(
                new Person("Alice", Arrays.asList("Reading", "Swimming")),
                new Person("Bob", Arrays.asList("Cycling", "Coding")),
                new Person("Charlie", Arrays.asList("Dancing", "Cooking", "Reading"))
        );

        List<String> allHobbies = people.stream()
                // Flat map each person's hobbies to a stream of hobbies
                // one-to-many relationship
                .flatMap(person -> person.getHobbies().stream())
                .collect(Collectors.toList());

        System.out.println(allHobbies);
    }

    /**
     * Tests the filter operation.
     */
    @Test
    public void testFilter() {
        List<Person> people = Arrays.asList(
                new Person("Alice", Arrays.asList("Reading", "Swimming")),
                new Person("Bob", Arrays.asList("Cycling", "Coding")),
                new Person("Charlie", Arrays.asList("Dancing", "Cooking", "Reading"))
        );

        List<Person> peopleWithReadingHobby = people.stream()
                // Filter people who have "Reading" as a hobby
                .filter(person -> person.getHobbies().contains("Reading"))
                .collect(Collectors.toList());

        System.out.println(peopleWithReadingHobby);
    }

    /**
     * Tests the reduce operation.
     */
    @Test
    public void testReduce() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        int sum = numbers.stream()
                // Reduce the stream to a single value by summing all elements
                .reduce(0, Integer::sum);

        System.out.println(sum);
    }

    /**
     * Tests the collect to List operation.
     */
    @Test
    public void testCollect2List() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        List<Integer> evenNumbers = numbers.stream()
                // Filter even numbers
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());

        System.out.println(evenNumbers);
    }

    /**
     * Tests the collect to Set operation.
     */
    @Test
    public void testCollect2Set() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        Set<Integer> evenNumbers = numbers.stream()
                // Filter even numbers
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toSet());

        System.out.println(evenNumbers);
    }

    /**
     * Tests the collect to Map operation.
     */
    @Test
    public void testCollect2Map() {
        List<Person> people = Arrays.asList(
                new Person("Alice", Arrays.asList("Reading", "Swimming")),
                new Person("Bob", Arrays.asList("Cycling", "Coding")),
                new Person("Charlie", Arrays.asList("Dancing", "Cooking", "Reading"))
        );

        Map<String, List<String>> hobbiesByPerson = people.stream()
                .collect(Collectors.toMap(
                        Person::getName,
                        Person::getHobbies
                ));

        System.out.println(hobbiesByPerson);
    }

    /**
     * Tests the collect to Map operation with merge function.
     */
    @Test
    public void testCollect2MapWithMergeFunction() {
        List<Person> people = Arrays.asList(
                new Person("Alice", Arrays.asList("Reading", "Swimming")),
                new Person("Bob", Arrays.asList("Cycling", "Coding")),
                new Person("Charlie", Arrays.asList("Dancing", "Cooking", "Reading"))
        );

        Map<String, List<String>> hobbiesByPerson = people.stream()
                .collect(Collectors.toMap(
                        Person::getName,
                        Person::getHobbies,
                        (hobbies1, hobbies2) -> {
                            hobbies1.addAll(hobbies2);
                            return hobbies1;
                        }
                ));

        System.out.println(hobbiesByPerson);
    }

}