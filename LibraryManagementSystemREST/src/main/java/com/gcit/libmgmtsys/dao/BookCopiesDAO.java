/**
 * @Author Shuo Zhang <shuo_zhang@gcitsolutions.com>
 * @Date Sep 28, 2017
 */
package com.gcit.libmgmtsys.dao;

import java.sql.*;
import java.util.*;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.libmgmtsys.entity.Book;
import com.gcit.libmgmtsys.entity.BookCopies;
import com.gcit.libmgmtsys.entity.LibraryBranch;
import com.gcit.libmgmtsys.entity.Publisher;

@SuppressWarnings("rawtypes")
public class BookCopiesDAO extends BaseDAO implements ResultSetExtractor<List<BookCopies>>{
	//insert a book copies association
	public void addBookCopies(BookCopies bookCopies) throws SQLException {
		Integer noOfCopies = bookCopies.getNoOfCopies();
		Integer bookId     = bookCopies.getBook().getBookId();
		Integer branchId   = bookCopies.getLibraryBranch().getBranchId();
		template.update("INSERT INTO tbl_book_copies (bookId, branchId, noOfCopies) VALUES(?, ?, ?)",
				new Object[] {bookId, branchId, noOfCopies});
	}
	
	//update a book copies association
	public void updateBookCopies(BookCopies bookCopies) throws SQLException {
		Integer noOfCopies = bookCopies.getNoOfCopies();
		Integer bookId     = bookCopies.getBook().getBookId();
		Integer branchId   = bookCopies.getLibraryBranch().getBranchId();
		template.update("UPDATE tbl_book_copies SET noOfCopies = ? WHERE bookId = ? AND branchId = ?",
				new Object[] {noOfCopies, bookId, branchId});
	}
	
	//return total number of copies for a book in all libraries.
	public List<BookCopies> getNoOfCopies(Integer bookId) throws SQLException {
		return template.query("SELECT sum(noOfCopies) as noOfCopies FROM tbl_book_copies WHERE bookId = ?", 
				new Object[] {bookId}, this);
	}
	
	//check if a book record exist in a library branch in tbl_book_copies
	public List<BookCopies> checkBookCopies(BookCopies bookCopy) throws SQLException {
		return template.query("SELECT * FROM tbl_book_copies WHERE bookId = ? AND branchId = ?", 
				new Object[] {bookCopy.getBook().getBookId(), bookCopy.getLibraryBranch().getBranchId()}, this);
	}
	
	public List<BookCopies> readBookCopiesByBook(Book book) {
		return template.query("SELECT * FROM tbl_book_copies WHERE bookId = ?",
				new Object[] {book.getBookId()}, this);
	}
	
	// return all book copies of a library branch given branch id
	public List<BookCopies> readBookCopiesByBranch(LibraryBranch branch) throws SQLException {
		List<BookCopies> bookCopies = template.query("SELECT * FROM tbl_book_copies WHERE branchId = ?)",
				new Object[] {branch.getBranchId()}, this);
		if (bookCopies != null) {
			return bookCopies;
		}
		return null;
	}
	
	@Override
	public List extractData(ResultSet rs) throws SQLException {
		List<BookCopies> bookCopies = new ArrayList<>();
		while (rs.next() && rs.getInt("bookId") != 0) {
			BookCopies 		 bookCopy 	   = new BookCopies();
			Book 		  	 book 		   = new Book();
			LibraryBranch 	 libraryBranch = new LibraryBranch();
			
			book.setBookId(rs.getInt("bookId"));
			libraryBranch.setBranchId(rs.getInt("branchId"));
			bookCopy.setBook(book);
			bookCopy.setLibraryBranch(libraryBranch);
			bookCopy.setNoOfCopies(rs.getInt("noOfCopies"));
			bookCopies.add(bookCopy);
		}
		return bookCopies;
	}
}
