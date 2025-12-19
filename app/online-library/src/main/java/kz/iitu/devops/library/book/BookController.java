package kz.iitu.devops.library.book;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) { this.service = service; }

    @GetMapping
    public List<Book> list() { return service.findAll(); }

    @GetMapping("/{id}")
    public Book get(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Book> create(@Valid @RequestBody Book book, UriComponentsBuilder ucb) {
        Book saved = service.create(book);
        URI location = ucb.path("/api/books/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @Valid @RequestBody Book book) {
        return service.update(id, book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
