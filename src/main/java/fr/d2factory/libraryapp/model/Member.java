package fr.d2factory.libraryapp.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */
public abstract class Member {
    /**
     * An initial sum of money the member has
     */
    private float wallet;
    
    /**
     * the member id
     */
    private long id;
    
    /**
     * map represents the borrowed books
     */
    Map<Long, LocalDate> borrowdBooks;


    public float getWallet() {
        return wallet;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the borrowdBooks
	 */
	public Map<Long, LocalDate> getBorrowdBooks() {
		if (this.borrowdBooks == null) {
			return new HashMap<>();
		}
		return borrowdBooks;
	}

	/**
	 * @param borrowdBooks the borrowdBooks to set
	 */
	public void setBorrowdBooks(Map<Long, LocalDate> borrowdBooks) {
		this.borrowdBooks = borrowdBooks;
	}

}
