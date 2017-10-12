/**
 * @Author Shuo Zhang <shuo_zhang@gcitsolutions.com>
 * @Date Sep 27, 2017
 */
package com.gcit.libmgmtsys.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BookCopies implements Serializable{
	
	private static final long serialVersionUID = 7061771541509860716L;
	
	private Book 		  book;
	private LibraryBranch libraryBranch;
	private Integer 	  noOfCopies;
	
	public Book getBook() {
		return book;
	}
	
	@XmlElement
	public void setBook(Book book) {
		this.book = book;
	}
	
	public LibraryBranch getLibraryBranch() {
		return libraryBranch;
	}
	
	@XmlElement
	public void setLibraryBranch(LibraryBranch libraryBranch) {
		this.libraryBranch = libraryBranch;
	}
	
	public Integer getNoOfCopies() {
		return noOfCopies;
	}
	
	@XmlElement
	public void setNoOfCopies(Integer noOfCopies) {
		this.noOfCopies = noOfCopies;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((book == null) ? 0 : book.hashCode());
		result = prime * result + ((libraryBranch == null) ? 0 : libraryBranch.hashCode());
		result = prime * result + ((noOfCopies == null) ? 0 : noOfCopies.hashCode());
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
		BookCopies other = (BookCopies) obj;
		if (book == null) {
			if (other.book != null)
				return false;
		} else if (!book.equals(other.book))
			return false;
		if (libraryBranch == null) {
			if (other.libraryBranch != null)
				return false;
		} else if (!libraryBranch.equals(other.libraryBranch))
			return false;
		if (noOfCopies == null) {
			if (other.noOfCopies != null)
				return false;
		} else if (!noOfCopies.equals(other.noOfCopies))
			return false;
		return true;
	}
	
}
