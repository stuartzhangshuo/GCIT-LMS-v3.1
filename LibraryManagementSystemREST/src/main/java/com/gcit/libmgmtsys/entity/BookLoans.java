/**
 * @Author Shuo Zhang <shuo_zhang@gcitsolutions.com>
 * @Date Sep 27, 2017
 */
package com.gcit.libmgmtsys.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BookLoans implements Serializable{
	
	private static final long serialVersionUID = 1880265167738841075L;
	
	private Borrower 	  borrower;
	private LibraryBranch libraryBranch;
	private Book 		  book;
	
	private String	dateOut;
	private String  dueDate;
	private String  dateIn;
	
	
	public Borrower getBorrower() {
		return borrower;
	}
	
	@XmlElement
	public void setBorrower(Borrower borrower) {
		this.borrower = borrower;
	}
	
	public LibraryBranch getLibraryBranch() {
		return libraryBranch;
	}
	
	@XmlElement
	public void setLibraryBranch(LibraryBranch libraryBranch) {
		this.libraryBranch = libraryBranch;
	}
	
	public Book getBook() {
		return book;
	}
	
	@XmlElement
	public void setBook(Book book) {
		this.book = book;
	}
	
	public String getDateOut() {
		return dateOut;
	}
	
	@XmlElement
	public void setDateOut(String dateOut) {
		this.dateOut = dateOut;
	}
	
	public String getDueDate() {
		return dueDate;
	}
	
	@XmlElement
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	
	public String getDateIn() {
		return dateIn;
	}
	
	@XmlElement
	public void setDateIn(String dateIn) {
		this.dateIn = dateIn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((book == null) ? 0 : book.hashCode());
		result = prime * result + ((borrower == null) ? 0 : borrower.hashCode());
		result = prime * result + ((dateOut == null) ? 0 : dateOut.hashCode());
		result = prime * result + ((libraryBranch == null) ? 0 : libraryBranch.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookLoans other = (BookLoans) obj;
		if (book == null) {
			if (other.book != null)
				return false;
		} else if (!book.equals(other.book))
			return false;
		if (borrower == null) {
			if (other.borrower != null)
				return false;
		} else if (!borrower.equals(other.borrower))
			return false;
		if (dateOut == null) {
			if (other.dateOut != null)
				return false;
		} else if (!dateOut.equals(other.dateOut))
			return false;
		if (libraryBranch == null) {
			if (other.libraryBranch != null)
				return false;
		} else if (!libraryBranch.equals(other.libraryBranch))
			return false;
		return true;
	}
}
