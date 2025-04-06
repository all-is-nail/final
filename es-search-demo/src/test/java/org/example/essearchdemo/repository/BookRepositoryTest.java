package org.example.essearchdemo.repository;

import org.example.essearchdemo.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @Test
    void testById() {
        repository.deleteById(1L);
        
        Book book = new Book(1,
                "Java编程思想",
                "Bruce Eckel",
                100,
                new Date(),
                "计算机语言",
                "计算机",
                "人民邮电出版社");

        Book savedBook = repository.save(book);
        assertNotNull(savedBook);

        Book foundBook = repository.findById(1L).orElse(null);

        assertNotNull(foundBook);
        assertEquals("Java编程思想", foundBook.getName());
        assertEquals("Bruce Eckel", foundBook.getAuthor());
        assertEquals("计算机语言", foundBook.getDescription());
    }
}