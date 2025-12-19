package kz.iitu.devops.library.book;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository repo;

    public BookService(BookRepository repo) { this.repo = repo; }

    public List<Book> findAll() { return repo.findAll(); }

    public Book findById(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Book not found: id=" + id));
    }

    @Transactional
    public Book create(Book b) {
        try {
            return repo.save(b);
        } catch (DataIntegrityViolationException e) {
            throw e;
        }
    }

    @Transactional
    public Book update(Long id, Book payload) {
        Book existing = findById(id);
        existing.setTitle(payload.getTitle());
        existing.setAuthor(payload.getAuthor());
        existing.setIsbn(payload.getIsbn());
        existing.setPublishedYear(payload.getPublishedYear());
        return repo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Book existing = findById(id);
        repo.delete(existing);
    }
}
