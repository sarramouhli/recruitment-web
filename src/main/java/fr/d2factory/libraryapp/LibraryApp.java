/**
 * 
 */
package fr.d2factory.libraryapp;

import java.time.LocalDate;

import fr.d2factory.libraryapp.exception.BookNotFoundException;
import fr.d2factory.libraryapp.exception.HasLateBooksException;
import fr.d2factory.libraryapp.exception.LibraryException;
import fr.d2factory.libraryapp.model.Book;
import fr.d2factory.libraryapp.model.Member;
import fr.d2factory.libraryapp.repository.BookRepository;
import fr.d2factory.libraryapp.service.MemberService;
import fr.d2factory.libraryapp.service.MemberServiceImpl;

/**
 * @author sarra
 *
 *         The library class is in charge of stocking the books and managing the
 *         return delays and members The books are available via the
 *         {@link fr.d2factory.libraryapp.repository.BookRepository}
 */
public class LibraryApp {

	BookRepository bookRepository = new BookRepository();
	MemberService memberService = new MemberServiceImpl();

	/**
	 * A member is borrowing a book from our library.
	 *
	 * @param isbnCode   the isbn code of the book
	 * @param member     the member who is borrowing the book
	 * @param borrowedAt the date when the book was borrowed
	 *
	 * @return the book the member wishes to obtain if found
	 * @throws HasLateBooksException in case the member has books that are late
	 *
	 * @see fr.d2factory.libraryapp.model.ISBN
	 * @see Member
	 */
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) {
		if (memberService.isLate(member)) {
			throw new HasLateBooksException("Member has late book");
		}

		Book borrowedBook = bookRepository.findBook(isbnCode);
		
		if (borrowedBook == null) {
			throw new BookNotFoundException("the requested book is not found");
		}
		
		return borrowedBook;
	}

	/**
	 * A member returns a book to the library. We should calculate the tarif and
	 * probably charge the member
	 *
	 * @param book   the {@link Book} they return
	 * @param member the {@link Member} who is returning the book
	 * @throws Exception
	 *
	 * @see Member#payBook(int)
	 */
	public void returnBook(Book book, Member member) {
		LocalDate borrowedDate = bookRepository.findBorrowedBookDate(book);
		if (borrowedDate == null) {
			throw new LibraryException(
					"a problem has occured, the returned book is not found in list of borrowed books");
		}
		memberService.payBook(borrowedDate, member);
		bookRepository.returnBookBorrow(book);
	}

}
