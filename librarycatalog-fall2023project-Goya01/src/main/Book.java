
/*
* This class represents a book in the library. It contains the following information:
* The id of the book
* The title of the book
* The author of the book
* The genre of the book
* The date the book was last checked out
* A boolean that represents if the book is checked out or not
* Implemented getters and setters for each of these fields.
*/

package main;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Book {
	private int id;
	private String title;
	private String author;
	private String genre;
	private LocalDate lastCheckOut;
	private boolean checkedOut;

	public Book(int id, String title, String author, String genre, LocalDate lastCheckOut, boolean checkedOut) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.lastCheckOut = lastCheckOut;
		this.checkedOut = checkedOut;
	}

	public Book() {

	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public LocalDate getLastCheckOut() {
		return lastCheckOut;
	}
	public void setLastCheckOut(LocalDate lastCheckOut) {
		this.lastCheckOut = lastCheckOut;
	}
	public boolean isCheckedOut() {
		return checkedOut;
	}
	public void setCheckedOut(boolean checkedOut) {
		this.checkedOut = checkedOut;
	}
	

	/**
	* @return a string that contains the title and author of the book
	*/

	@Override
	public String toString() {
		return this.getTitle().toUpperCase() + " BY " + this.getAuthor().toUpperCase();
	}

	/**
	* Calculates the fees for a book based on the number of days it is overdue.
	* @return the fees for the book
	*/

	public float calculateFees() {
		LocalDate today = LocalDate.of(2023, 9, 15); // September 15, 2023
		long daysOverdue = ChronoUnit.DAYS.between(lastCheckOut, today);

		if (daysOverdue >= 31) {
			float baseFee = 10.0f;
			float additionalFee = (daysOverdue - 31) * 1.5f;
			return baseFee + additionalFee;
		} else {
			return 0.0f;
		}
	}
}
