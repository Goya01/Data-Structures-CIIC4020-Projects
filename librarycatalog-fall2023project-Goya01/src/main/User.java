
/*
* This class represents a user in the library. It contains the following information:
* The id of the user
* The name of the user
* A list of books that the user has checked out
* Implemented getters and setters for each of these fields.
*/

package main;

import interfaces.List;

public class User {
	private int id;
	private String name;
	private List<Book> checkedOutList;

	public User(int id, String name, List<Book> checkedOutList) {
		this.id = id;
		this.name = name;
		this.checkedOutList = checkedOutList;
	}

	public User() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Book> getCheckedOutList() {
		return checkedOutList;
	}

	public void setCheckedOutList(List<Book> checkedOutList) {
		this.checkedOutList = checkedOutList;
	}
	
}
