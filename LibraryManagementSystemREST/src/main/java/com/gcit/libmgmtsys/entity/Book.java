/**
 * @Author Shuo Zhang <shuo_zhang@gcitsolutions.com>
 * @Date Sep 27, 2017
 */
package com.gcit.libmgmtsys.entity;

import java.io.Serializable;
import java.util.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = {"bookId", "title", "publisher"})
public class Book implements Serializable {

	private static final long serialVersionUID = 8947870066099291812L;

	private Integer   bookId;
	private String    title;
	private Publisher publisher;
	// 2nd level
	private List<Author>   authors;
	private List<Genre>    genres;
	private List<Borrower> borrowers;
	// Customized
	private HashMap<Integer, Integer> branchCopies; // branchId, noOfCopies
	private Integer totalNumOfCopies;

	public HashMap<Integer, Integer> getBranchCopies() {
		return branchCopies;
	}
	
	@XmlElement
	public void setBranchCopies(List<BookCopies> bookCopies) {
		branchCopies = new HashMap<>();
		Integer count = 0;
		for (BookCopies bookCopy : bookCopies) {
			count += bookCopy.getNoOfCopies();
			branchCopies.put(bookCopy.getLibraryBranch().getBranchId(), bookCopy.getNoOfCopies());
		}
		totalNumOfCopies = count;
	}
	
	@XmlElement
	public void setTotalNumOfCopies(Integer totalNumOfCopies) {
		this.totalNumOfCopies = totalNumOfCopies;
	}

	public Integer getTotalNumOfCopies() {
		return totalNumOfCopies;
	}

	public Publisher getPublisher() {
		return publisher;
	}
	
	@XmlElement
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public Integer getBookId() {
		return bookId;
	}
	
	@XmlElement
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}
	
	@XmlElement
	public void setTitle(String title) {
		this.title = title;
	}

	public List<Author> getAuthors() {
		return authors;
	}
	
	@XmlElement
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public List<Genre> getGenres() {
		return genres;
	}
	
	@XmlElement
	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public List<Borrower> getBorrowers() {
		return borrowers;
	}
	
	@XmlElement
	public void setBorrowers(List<Borrower> borrowers) {
		this.borrowers = borrowers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookId == null) ? 0 : bookId.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Book other = (Book) obj;
		if (bookId == null) {
			if (other.bookId != null)
				return false;
		} else if (!bookId.equals(other.bookId))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
