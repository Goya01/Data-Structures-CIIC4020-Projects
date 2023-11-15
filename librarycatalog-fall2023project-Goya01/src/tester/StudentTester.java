package tester;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.time.LocalDate;


import org.junit.Before;
import org.junit.Test;

import interfaces.List;
import main.Book;
import main.LibraryCatalog;
import main.User;

/*
 * DO NOT MODIFY THIS FILE!!!
 * 
 * If your method do not pass the testers FIRST assume your implementation is wrong. 
 * The testers set the expectations for the implementation, not the other way around.
 * 
 * Do not modify unless told otherwise by the professor or TA.
 * 
 * If you need to modify the tester to pass it, what makes you think you will pass the other testers we will use for grading?
 */
public class StudentTester {
	
	LibraryCatalog LC;
	
	@Before
	public void setup() throws IOException {
		LC = new LibraryCatalog();
	}
	@Test
	public void testIfConstructedCorrectly() {
		assertTrue("Failed to create Bokk and User Lists correctly.", LC.getBookCatalog().size() == 50 && LC.getUsers().size() == 30);
	}
	@Test
	public void testAddBook() {
		LC.addBook("My Personal Biography", "G Bonilla", "Classics");
		LC.addBook("Coder's Guide to Failing", "G Bonilla", "Adventure");
		
		Book epicBook1 = findBookById(LC.getBookCatalog(), 51);
		if(epicBook1 == null)
			fail("Didn't add the book {51, MyPersonal Biography, G Bonilla, Classics} to the List");
		Book epicBook2 = findBookById(LC.getBookCatalog(), 52);
		if(epicBook2 == null)
			fail("Didn't add the book {52, Coder's Guide to Failing, G Bonilla, Adventure} to the List");
		assertTrue("Failed to add the information of the books correctly.", 
				compareBooks(epicBook1, 51, "My Personal Biography", "G Bonilla", "Classics", LocalDate.of(2023, 9, 15), false) &&
				compareBooks(epicBook2, 52, "Coder's Guide to Failing", "G Bonilla", "Adventure", LocalDate.of(2023, 9, 15), false));
		
	}
	@Test
	public void removeBook() {
		boolean isThere = this.findBookById(this.LC.getBookCatalog(), 16) != null;
		LC.removeBook(16);
		boolean notThere = this.findBookById(this.LC.getBookCatalog(), 16) == null;
		assertTrue("Didn't remove book {16, 1984, George Orwell, Science Fiction, 2022-06-29, true}. "
				+ "Could also be that it was never there to begin with.", isThere && notThere);
	}
	@Test
	public void testCheckOutBook() {
		boolean success = LC.checkOutBook(19);
		boolean failure = !LC.checkOutBook(20);
		assertTrue("Failed to checkout a book that isn't check out (id = 19) should be true"
				+ "or failed to not checkout a book that is already checked out (id = 20) should be false", success && failure);
	}
	@Test
	public void testReturnBook() {
		boolean success = LC.returnBook(20);
		boolean failure = !LC.returnBook(19);
		assertTrue("Failed to return a book that is check out (id = 20) should be true "
				+ "or failed to not return a book that isn't checked out (id = 19) should be false", success && failure);
	}
	@Test
	public void testBookAvailability() {
		boolean success = LC.getBookAvailability(19);
		boolean failure = !LC.getBookAvailability(20);
		assertTrue("Failed to return that book is available (id = 19) should be true "
				+ "or failed to return that book isn;t available (id=20) should be false", success && failure);
	}
	@Test
	public void testBookCount() {
		int count = LC.bookCount("The Little Prince");
		assertTrue("Failed to return correct count for The Little Prince, expected 2", count == 2);
	}
	@Test
	public void testBookToString() {
		assertTrue("Failed to return correct string for book id = 34, expected THE LITTLE PRINCE BY ANTOINE DE SAINT-EXUPERY.", 
				this.findBookById(this.LC.getBookCatalog(), 34).toString().equals("THE LITTLE PRINCE BY ANTOINE DE SAINT-EXUPERY"));
	}
	@Test
	public void testBookFees() {
		Book book = this.findBookById(LC.getBookCatalog(), 36);
		assertTrue("Failed to calculate expected fees for book id = 36, expected 289.50 in fees.", book.calculateFees() == 389.5);
	}
	/*
	 * BONUS TESTERS
	 */
	@Test
	public void testFunctionalBookMethod() {
	
		List<Book> adL = LC.searchForBook(x -> x.getGenre().equals("Adventure"));
		if(adL == null) {
			fail("Not implemented");
			System.out.println("NO BONUS!");
		}
		if(adL.size() != 12)
			fail("Didn't get the correct book when the filter function was for genre = adventure");
		for(Book b: adL) {
			if(!b.getGenre().equals("Adventure"))
				fail("Didn't get the correct book when the filter function was for genre = adventure");
		}
		adL = LC.searchForBook(x -> x.getId() == 5);
		if(adL.size() != 1 && adL.get(0).getTitle().equals("Pride and Prejudice"))
			fail("Didn't get the correct book when the filter function was for id = 5");

		System.out.println("BONUS +5 pts!!!");
		return;

	}
	@Test
	public void testFunctionalUserMethod() {

		List<User> udL = LC.searchForUsers(x -> x.getCheckedOutList().size() == 1);
		if(udL == null) {
			fail("Not implemented");
			System.out.println("NO BONUS!");
		}
		if(udL.size() != 4)
			fail("Didn't get the correct users when the filter function was for users"
					+ " that have exactly 1 book checked out.");
		for(User u: udL) {
			if(u.getCheckedOutList().size() != 1)
				fail("Didn't get the correct users when the filter function was for users"
						+ " that have exactly 1 book checked out.");
		}
		udL = LC.searchForUsers(x -> x.getId() == 5);
		if(udL.size() != 1 && udL.get(0).getName().equals("William Brown"))
			fail("Didn't get the correct user when the filter function was for id = 5");

		System.out.println("BONUS +5 pts!!!");
		return;

	}
	
	/*
	 * Helper methods
	 */
	private Book findBookById(List<Book> L, int id) {
		for(Book b: L) {
			if(b.getId() == id)
				return b;
		}
		return null;
	}
	
	
	private boolean compareBooks(Book actual, int id, String title, String author, String genre, LocalDate ld, boolean checkedOut) {
		return actual.getId() == id && actual.getTitle().equals(title) && actual.getAuthor().equals(author) &&
				actual.isCheckedOut() == checkedOut && actual.getLastCheckOut().equals(ld);
	}

}
