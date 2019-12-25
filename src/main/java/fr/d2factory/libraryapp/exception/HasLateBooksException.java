package fr.d2factory.libraryapp.exception;

/**
 * This exception is thrown when a member who owns late books tries to borrow another book
 */
public class HasLateBooksException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HasLateBooksException(String message) {
		super(message);
	}
}
