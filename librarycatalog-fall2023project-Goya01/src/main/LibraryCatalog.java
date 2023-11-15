
/*
* The LibraryCatalog class manages the library catalog, including books and users.
* It provides various methods for adding, removing, checking out, and returning books,
* as well as generating reports.
*
* This class stores catalog and users in a generic List data structure. You are free to
* choose which List implementation to use. Later on, I instantiate both Lists as ArrayLists.
*/

package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import javax.swing.text.html.HTMLDocument.Iterator;

import data_structures.ArrayList;
import data_structures.DoublyLinkedList;
import data_structures.SinglyLinkedList;
import interfaces.FilterFunction;
import interfaces.List;

public class LibraryCatalog {
	private List<Book> catalog;
	private List<User> users;
	
	/**
	* Constructs a LibraryCatalog object by reading the catalog and user csv files.
	*
	* @throws IOException if the file cannot be read
	*/

	public LibraryCatalog() throws IOException{
		catalog = getBooksFromFiles();
		users = getUsersFromFiles();
	}

	/**
	* Reads the catalog csv file and returns a list of books.
	* 
	* @throws IOException if the file cannot be read
	* @return a list of books
	* 
	*/

	private List<Book> getBooksFromFiles() throws IOException {
		List<Book> books = new ArrayList<>();
		String file = "data/catalog.csv";

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			br.readLine();

			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");

				int id = Integer.parseInt(values[0].trim());
				String title = values[1].trim();
				String author = values[2].trim();
				String genre = values[3].trim();
				LocalDate lastCheckOut = LocalDate.parse(values[4].trim());
				boolean checkedOut = Boolean.parseBoolean(values[5].trim());
				
				Book book = new Book(id, title, author, genre, lastCheckOut, checkedOut);
				books.add(book);
			}
		}
		return books;
	}
	
	/**
	* Reads the user csv file and returns a list of users.
	* @return a list of users
	* @throws IOException if the file cannot be read
	*/

	private List<User> getUsersFromFiles() throws IOException {
		List<User> users = new ArrayList<>();
		String file = "data/user.csv";
		String line;

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			br.readLine();

			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				int id = Integer.parseInt(values[0].trim());
				String name = values[1].trim();
				User user = new User(id, name, new ArrayList<>());
				
				if (values.length == 3) {
					List<Book> listOfBooks = new ArrayList<>();
					String checkedOutBookIds = values[2].trim();
					if (!checkedOutBookIds.isEmpty()) {
						String[] bookIdsString = checkedOutBookIds.substring(1, checkedOutBookIds.length() - 1).split(" ");
						for (String bookIds : bookIdsString) {
							int idOfBook = Integer.parseInt(bookIds.trim());
							for (Book b : catalog) {
								if (b.getId() == idOfBook) {
									listOfBooks.add(b);
									break;
								}
							}
						}
					}
					user.setCheckedOutList(listOfBooks);
				}
				users.add(user);
			}
		}
		return users;
	}
	
	/**
	* @return a list of books in the catalog
	*/

	public List<Book> getBookCatalog() {
		return catalog;
	}

	/**
	* @return a list of users in the catalog
	*/

	public List<User> getUsers() {
		return users;
	}

	/**
	* Adds a new book to the catalog.
	* @param title title of the book
	* @param author author of the book
	* @param genre genre of the book
	*/

	public void addBook(String title, String author, String genre) {
		int id = catalog.size() + 1;
		LocalDate todaysDate = LocalDate.of(2023, 9, 15);
		boolean checkedOut = false;

		Book newbook = new Book(id, title, author, genre, todaysDate, checkedOut);
		catalog.add(newbook);
	}

	/**
	* Removes a book from the catalog.
	* @param id id of the book to remove
	*/

	public void removeBook(int id) {
		for (Book b : catalog) {
			if (b.getId() == id) {
				catalog.remove(b);
				break;
			}
		}
	}	
	
	/**
	* Checks out a book from the catalog based on the id.
	* @param id id of the book to check out
	* @return true if the book was successfully checked out, false otherwise
	*/

	public boolean checkOutBook(int id) {
		for (Book b : catalog) {
			if (b.getId() == id) {
				if (!b.isCheckedOut()) {
					b.setLastCheckOut(LocalDate.of(2023, 9, 15));
					b.setCheckedOut(true);
					return true;
				}
				else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	* Returns a book to the catalog based on the id and sets the checkedOut boolean to false.
	* @param id id of the book to return
	* @return true if the book was successfully returned, false otherwise
	*/

	public boolean returnBook(int id) {
		for (Book b : catalog) {
			if (b.getId() == id) {
				if (b.isCheckedOut()) {
					b.setCheckedOut(false);
					return true;
				}
				else {
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	* Checks if the book is available to be checked out.
	* @param id id of the book to check
	* @return !b.isCheckedOut() if the book is available, false otherwise
	*/

	public boolean getBookAvailability(int id) {
		for (Book b : catalog) {
			if (b.getId() == id) {
				return !b.isCheckedOut();
			}
		}
		return false;
	}

	/**
	* This method returns the number of books in the catalog with the given title.
	* @param title title of the book
	* @return count the number of books with the given title
	*/

	public int bookCount(String title) {
		int count = 0;
		for (Book b : catalog) {
			if (b.getTitle().equalsIgnoreCase(title)) {
				count++;
			}
		}
		return count;
	}

	/**
	* This generates a report of the library catalog and writes it to a file. 
	* This method has a variable named output in which it will store all the 
	* information that will be written to the file. The first part of the report
	* is a summary of the books in the catalog. The second part of the report is
	* a list of all the books that are currently checked out. The third part of
	* the report is a list of all the users that owe book fees. The last part of
	* the report is the total amount of fees owed by all the users.
	* @throws IOException if the file cannot be written to or created.
	*/

	public void generateReport() throws IOException {
		String output = "\t\t\t\tREPORT\n\n";
		output += "\t\tSUMMARY OF BOOKS\n";
		output += "GENRE\t\t\t\t\t\tAMOUNT\n";

		int adventure = 0;
		int fiction = 0;
		int classics = 0;
		int mystery = 0;
		int scienceFiction = 0;
		int totalBooks = 0;
		
		for (int i = 0; i < catalog.size(); i++) {
			String genre = catalog.get(i).getGenre();
			switch (genre) {
			case "Adventure":
				adventure++;
				break;
			case "Fiction":
				fiction++;
				break;
			case "Classics":
				classics++;
				break;
			case "Mystery":
				mystery++;
				break;
			case "Science Fiction":
				scienceFiction++;
				break;
			}
			totalBooks = adventure + fiction + classics + mystery + scienceFiction;
		}

		output += "Adventure\t\t\t\t\t" + (adventure) + "\n";
		output += "Fiction\t\t\t\t\t\t" + (fiction) + "\n";
		output += "Classics\t\t\t\t\t" + (classics) + "\n";
		output += "Mystery\t\t\t\t\t\t" + (mystery) + "\n";
		output += "Science Fiction\t\t\t\t\t" + (scienceFiction) + "\n";
		output += "====================================================\n";
		output += "\t\t\tTOTAL AMOUNT OF BOOKS\t" + (totalBooks) + "\n\n";
		
		output += "\t\t\tBOOKS CURRENTLY CHECKED OUT\n\n";

		for (Book b : catalog) {
			if (b.isCheckedOut()) {
				output += b.toString() + "\n";
			}
		}
		int totalBooksCheckedOut = 0;
		for (Book b : catalog) {
			if (b.isCheckedOut()) {
				totalBooksCheckedOut++;
			}
		}
		
		
		output += "====================================================\n";
		output += "\t\t\tTOTAL AMOUNT OF BOOKS\t" + (totalBooksCheckedOut) + "\n\n";
		
		output += "\n\n\t\tUSERS THAT OWE BOOK FEES\n\n";

		for (User u: users) {
			if (u.getCheckedOutList().size() > 0) {
				output += u.getName();
				float userFees = 0;
				for (Book b : u.getCheckedOutList()) {
					if (b.isCheckedOut()) {
						userFees += b.calculateFees();
					}
				}
				output += "\t\t\t\t\t$" + userFees + "\n";
			}
		}

		float totalFees = 0;
		for (User u: users) {
			if (u.getCheckedOutList().size() > 0) {
				for (Book b : u.getCheckedOutList()) {
					if (b.isCheckedOut()) {
						totalFees += b.calculateFees();
					}
				}
			}
		}
		
		output += "====================================================\n";
		output += "\t\t\t\tTOTAL DUE\t$" + (totalFees) + "\n\n\n";
		output += "\n\n";
		System.out.println(output);
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("report/actual_report.txt"))) {
			bw.write(output);
		}
	}
	
	/*
	* BONUS Methods
	* 
	* You are not required to implement these, but they can be useful for
	* other parts of the project.
	*/
	public List<Book> searchForBook(FilterFunction<Book> func) {
		return null;
	}
	
	public List<User> searchForUsers(FilterFunction<User> func) {
		return null;
	}
	
}
