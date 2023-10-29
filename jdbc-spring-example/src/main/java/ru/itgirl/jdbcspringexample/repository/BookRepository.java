package ru.itgirl.jdbcspringexample.repository;

import ru.itgirl.jdbcspringexample.Books;

import java.util.List;

public interface BookRepository {
    List <Books> findAllBooks();

    Books getBookId(Long id);
}
