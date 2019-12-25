package fr.d2factory.libraryapp.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.d2factory.libraryapp.model.Book;
import fr.d2factory.libraryapp.model.ISBN;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
	private static Map<ISBN, Book> availableBooks = new HashMap<>();
	private static Map<Book, LocalDate> borrowedBooks = new HashMap<>();

	public void addBooks(List<Book> books) {
		books.forEach(book -> availableBooks.put(book.getIsbn(), book));
	}

	public Book findBook(long isbnCode) {
		return availableBooks.entrySet().stream().filter(bookEntry -> bookEntry.getKey().getIsbnCode() == isbnCode)
				.map(Map.Entry::getValue).findAny().orElse(null);
	}

	
	public void saveBookBorrow(Book book, LocalDate borrowedAt) {
		availableBooks.remove(book.getIsbn());
		borrowedBooks.put(book, borrowedAt);
	}

	public LocalDate findBorrowedBookDate(Book book) {
		return borrowedBooks.entrySet().stream().filter(borrowedEntry -> borrowedEntry.getKey().equals(book))
				.map(Map.Entry::getValue).findAny().orElse(null);
	}
	public void returnBookBorrow(Book book) {
		borrowedBooks.remove(book);
		availableBooks.put(book.getIsbn(), book);
	}
	
}
