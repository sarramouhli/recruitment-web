package fr.d2factory.libraryapp.exception;

public class BookNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2875957834845417678L;
	
	public BookNotFoundException(String message) {
		super(message);
	}

}
