/**
 * 
 */
package fr.d2factory.libraryapp.model;

/**
 * @author sarra
 *
 */
public class Student extends Member {
	
	private boolean firstYear = false;
	

	/**
	 * @return the firstYear
	 */
	public boolean isFirstYear() {
		return firstYear;
	}

	/**
	 * @param firstYear the firstYear to set
	 */
	public void setFirstYear(boolean firstYear) {
		this.firstYear = firstYear;
	}
}
