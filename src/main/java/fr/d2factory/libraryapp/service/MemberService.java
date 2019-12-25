/**
 * 
 */
package fr.d2factory.libraryapp.service;

import java.time.LocalDate;

import fr.d2factory.libraryapp.model.Member;

/**
 * @author sarra
 *
 */
public interface MemberService {
	public boolean isLate(Member member);

	void payBook(LocalDate borrowedDate, Member member);
}
