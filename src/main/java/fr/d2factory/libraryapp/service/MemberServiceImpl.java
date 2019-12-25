/**
 * 
 */
package fr.d2factory.libraryapp.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.logging.Logger;

import fr.d2factory.libraryapp.exception.NotMemberExeption;
import fr.d2factory.libraryapp.model.Member;
import fr.d2factory.libraryapp.model.Resident;
import fr.d2factory.libraryapp.model.Student;
import fr.d2factory.libraryapp.utils.Constants;

/**
 * @author sarra
 *
 */
public class MemberServiceImpl implements MemberService {
	
	private static final Logger LOGGER = Logger.getLogger(MemberService.class.getName());

	/**
	 * Check if the member should is late
	 *
	 * @param member the Member
	 */

	public boolean isLate(Member member) {

		for (Map.Entry<Long, LocalDate> borrowdBook : member.getBorrowdBooks().entrySet()) {

			try {
				if (ChronoUnit.DAYS.between(borrowdBook.getValue(), LocalDate.now()) > getAllowedPeriod(member)) {
					return true;
				}
			} catch (NotMemberExeption e) {
				LOGGER.warning(e.getMessage());
			}
		}
		return false;
	}

	/**
	 * The member should pay their books when they are returned to the library
	 *
	 * @param numberOfDays the number of days they kept the book
	 */
	public void payBook(LocalDate borrowedDate, Member member) {
		long numberOfDays = ChronoUnit.DAYS.between(borrowedDate, LocalDate.now());
		float amountToPay = 0;
		
		if (member instanceof Resident) {
			if (numberOfDays > Constants.RESIDENT_ALLOWED_PERIOD) {
				long outOfAllowedPeriod = numberOfDays - Constants.RESIDENT_ALLOWED_PERIOD;
				amountToPay = outOfAllowedPeriod * Constants.EXTRA_TARIF
						+ Constants.RESIDENT_ALLOWED_PERIOD * Constants.NORMAL_TARIF;
			} else {
				amountToPay = numberOfDays * Constants.NORMAL_TARIF;
			}
		}
		if (member instanceof Student) {
			
			// if student is out of allowed period
			if (numberOfDays > Constants.STUDENT_ALLOWED_PERIOD) {
				numberOfDays = Constants.STUDENT_ALLOWED_PERIOD;
			}
			
			// if student is in first year
			if (((Student) member).isFirstYear()) {
				numberOfDays -= Constants.STUDENT_FREE_PERIOD;
				if (numberOfDays < 0) {
					numberOfDays = 0;
				}
			}
			
			amountToPay = numberOfDays * Constants.NORMAL_TARIF;
		}
		
		// get the amount from member's wallet
		// we suppose that member has always the amount
		member.setWallet(member.getWallet() - amountToPay);

	}

	/**
	 * The allowed period of keeping a book
	 *
	 * @param member the Member
	 */
	private long getAllowedPeriod(Member member) throws NotMemberExeption {
		if (member instanceof Resident) {
			return Constants.RESIDENT_ALLOWED_PERIOD;
		}

		if (member instanceof Student) {
			return Constants.STUDENT_ALLOWED_PERIOD;
		}

		throw new NotMemberExeption("Member should be a Student or a Resident.");

	}

}
