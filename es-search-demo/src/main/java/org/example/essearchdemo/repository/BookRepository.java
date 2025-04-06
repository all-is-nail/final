package org.example.essearchdemo.repository;

import org.example.essearchdemo.model.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book, Long> {

    /**
     * search by name
     * 
     * @param name
     * @return
     */
    List<Book> findByNameContainingIgnoreCase(String name);
}
