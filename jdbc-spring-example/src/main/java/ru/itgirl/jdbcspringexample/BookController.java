package ru.itgirl.jdbcspringexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.itgirl.jdbcspringexample.Books;
import ru.itgirl.jdbcspringexample.repository.BookRepository;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookRepository bookRepository;
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    @GetMapping("/books/all")
    public List<Books> getAllBooks() {
        return bookRepository.findAllBooks();
    }
    @GetMapping("/books/{id}")
    public Books getBookId(@PathVariable("id") Long id) {
        return bookRepository.getBookId(id);
    }
}
