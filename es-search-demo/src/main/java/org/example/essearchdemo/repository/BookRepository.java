package org.example.essearchdemo.repository;

import org.example.essearchdemo.model.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book, Long> {
}
