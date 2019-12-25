package fr.d2factory.libraryapp.exception;

/**
 * This exception is thrown when we pass an Object that is not a Resident or Student
 */

public class NotMemberExeption extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotMemberExeption(String message) {
		super(message);
	}
}
