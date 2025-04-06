package org.example.essearchdemo.controller;

import org.example.essearchdemo.model.Book;
import org.example.essearchdemo.repository.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private BookRepository repository;

    public BookController(BookRepository bookRepository) {
        this.repository = bookRepository;
    }

    @PostMapping
    public Book create(@RequestBody Book book) {
        return repository.save(book);
    }

    @GetMapping("/{id}")
    public Optional<Book> findById(@PathVariable long id) {
        return repository.findById(id);
    }

    @GetMapping
    public Iterable<Book> findAll() {
        return repository.findAll();
    }

    @PutMapping("/{id}")
    public Book update(@PathVariable long id, @RequestBody Book book) {
        book.setId(id);
        return repository.save(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        repository.deleteById(id);
    }
}
