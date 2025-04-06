package org.example.essearchdemo.model;

import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Document(indexName = "books")
public class Book {
    private long id;
    private String name;
    private String author;
    private long price;
    private Date publishDate;
    private String description;
    private String category;
    private String publisher;

    public Book() {
    }

    public Book(long id, String name, String author, long price, Date publishDate, String description, String category, String publisher) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
        this.publishDate = publishDate;
        this.description = description;
        this.category = category;
        this.publisher = publisher;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}

