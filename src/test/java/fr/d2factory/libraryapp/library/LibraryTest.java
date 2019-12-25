package fr.d2factory.libraryapp.library;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.d2factory.libraryapp.LibraryApp;
import fr.d2factory.libraryapp.exception.BookNotFoundException;
import fr.d2factory.libraryapp.exception.HasLateBooksException;
import fr.d2factory.libraryapp.model.Book;
import fr.d2factory.libraryapp.model.Member;
import fr.d2factory.libraryapp.model.Resident;
import fr.d2factory.libraryapp.model.Student;
import fr.d2factory.libraryapp.repository.BookRepository;

/**
 * Do not forget to consult the README.md :)
 */
public class LibraryTest {
	private LibraryApp libraryApp;
	private BookRepository bookRepository;
	private static List<Book> books;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		File booksJson = new File("src/test/resources/books.json");
		books = mapper.readValue(booksJson, new TypeReference<List<Book>>() {
		});
	}

	@BeforeEach
	void setup() throws JsonParseException, JsonMappingException, IOException {
		libraryApp = new LibraryApp();
		// Initialize data
		bookRepository = new BookRepository();
		bookRepository.addBooks(books);

	}

	@Test
	void member_can_borrow_a_book_if_book_is_available() {
		// Get a book isbn for test
		long isbnCode = books.get(1).getIsbn().getIsbnCode();
		// Create a member
		Member member = new Student();
		member.setId(222);
		member.setWallet(1000);
		Book result = libraryApp.borrowBook(isbnCode, member, LocalDate.now());
		assertEquals(books.get(1), result);
	}

	@Test
	void borrowed_book_is_no_longer_available() {
		// Create a member
		Member member = new Student();
		member.setId(222);
		member.setWallet(1000);

		Book bookToBeBorrowed = books.get(1);
		bookRepository.saveBookBorrow(bookToBeBorrowed, LocalDate.now());

		Assertions.assertThrows(BookNotFoundException.class, () -> {
			libraryApp.borrowBook(books.get(1).getIsbn().getIsbnCode(), member, LocalDate.now());
		});
	}

	@Test
	void residents_are_taxed_10cents_for_each_day_they_keep_a_book() {
		Resident resident = new Resident();
		resident.setId(564);
		resident.setWallet(100);

		// prepare borrowed books
		Map<Long, LocalDate> borrowedBooks = new HashMap<>();
		borrowedBooks.put(books.get(1).getIsbn().getIsbnCode(), LocalDate.now().minusDays(50));

		resident.setBorrowdBooks(borrowedBooks);
		bookRepository.saveBookBorrow(books.get(1), LocalDate.now().minusDays(50));
		libraryApp.returnBook(books.get(1), resident);
		assertEquals(95, resident.getWallet());

	}

	@Test
	void students_pay_10_cents_the_first_30days() {
		Student student = new Student();
		student.setId(564);
		student.setWallet(100);
		student.setFirstYear(false);

		// prepare borrowed books
		Map<Long, LocalDate> borrowedBooks = new HashMap<>();
		borrowedBooks.put(books.get(1).getIsbn().getIsbnCode(), LocalDate.now().minusDays(20));

		student.setBorrowdBooks(borrowedBooks);
		bookRepository.saveBookBorrow(books.get(1), LocalDate.now().minusDays(20));
		libraryApp.returnBook(books.get(1), student);
		assertEquals(98f, student.getWallet());
	}

	@Test
	void students_in_1st_year_are_not_taxed_for_the_first_15days() {
		Student student = new Student();
		student.setId(564);
		student.setWallet(100);
		student.setFirstYear(true);

		// prepare borrowed books
		Map<Long, LocalDate> borrowedBooks = new HashMap<>();
		borrowedBooks.put(books.get(1).getIsbn().getIsbnCode(), LocalDate.now().minusDays(20));

		student.setBorrowdBooks(borrowedBooks);
		bookRepository.saveBookBorrow(books.get(1), LocalDate.now().minusDays(20));
		libraryApp.returnBook(books.get(1), student);
		assertEquals(99.5f, student.getWallet());
	}

	@Test
	void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() {
		Resident resident = new Resident();
		resident.setId(564);
		resident.setWallet(100);

		// prepare borrowed books
		Map<Long, LocalDate> borrowedBooks = new HashMap<>();
		borrowedBooks.put(books.get(1).getIsbn().getIsbnCode(), LocalDate.now().minusDays(80));

		resident.setBorrowdBooks(borrowedBooks);
		bookRepository.saveBookBorrow(books.get(1), LocalDate.now().minusDays(80));
		libraryApp.returnBook(books.get(1), resident);
		assertEquals(90, resident.getWallet());
	}

	@Test
	void members_cannot_borrow_book_if_they_have_late_books() {
		Student student = new Student();
		student.setId(564);
		student.setWallet(100);
		student.setFirstYear(false);

		// prepare borrowed books
		Map<Long, LocalDate> borrowedBooks = new HashMap<>();
		borrowedBooks.put(books.get(1).getIsbn().getIsbnCode(), LocalDate.now().minusDays(35));
		borrowedBooks.put(books.get(0).getIsbn().getIsbnCode(), LocalDate.now().minusDays(20));

		student.setBorrowdBooks(borrowedBooks);
		bookRepository.saveBookBorrow(books.get(1), LocalDate.now().minusDays(35));
		bookRepository.saveBookBorrow(books.get(0), LocalDate.now().minusDays(20));

		Assertions.assertThrows(HasLateBooksException.class, () -> {
			libraryApp.borrowBook(books.get(2).getIsbn().getIsbnCode(), student, LocalDate.now());
		});

	}
}
