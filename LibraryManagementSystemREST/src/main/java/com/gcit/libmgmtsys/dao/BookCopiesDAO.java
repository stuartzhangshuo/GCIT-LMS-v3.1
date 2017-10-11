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

@SuppressWarnings("rawtypes")
public class BookCopiesDAO extends BaseDAO implements ResultSetExtractor<List<BookCopies>>{
	//insert a book copies association
	public void addBookCopies(BookCopies bookCopies) throws SQLException {
		template.update("INSERT INTO tbl_book_copies (bookId, branchId, noOfCopies) VALUES(?, ?, ?)",
				new Object[] {bookCopies.getBook().getBookId(), bookCopies.getLibraryBranch().getBranchId(), bookCopies.getNoOfCopies()});
	}
	
	//update a book copies association
	public void updateBookCopies(BookCopies bookCopies) throws SQLException {
		template.update("UPDATE tbl_book_copies SET noOfCopies = ? WHERE bookId = ? AND branchId = ?",
				new Object[] {bookCopies.getNoOfCopies(), bookCopies.getBook().getBookId(), bookCopies.getLibraryBranch().getBranchId()});
	}
	
	//return total number of copies for a book in all libraries.
	public List<BookCopies> getNoOfCopies(Integer bookId) throws SQLException {
		return template.query("SELECT bookId, branchId, sum(noOfCopies) as noOfCopies FROM tbl_book_copies WHERE bookId = ?", 
				new Object[] {bookId}, this);
	}
	
	public List<BookCopies> checkBookCopies(BookCopies bookCopy) throws SQLException {
		String sql = "SELECT * FROM tbl_book_copies WHERE bookId = ? AND branchId = ?";
		return template.query(sql, new Object[] {bookCopy.getBook().getBookId(), bookCopy.getLibraryBranch().getBranchId()}, this);
	}
	
//	//return total number of book copies in a library branch.
//	public Integer getNoOfCopiesInBranch(Integer boodId, Integer branchId) {
//		
//	}

//	@Override
//	protected List<BookCopies> parseFirstLevelData(ResultSet rs) throws SQLException {
//		List<BookCopies> bookCopies = new ArrayList<>();
//		while (rs.next() && rs.getInt("bookId") != 0) {
//			BookCopies bookCopy = new BookCopies();
//			Book 		  	 book 		   	  = new Book();
//			LibraryBranch 	 libraryBranch 	  = new LibraryBranch();
//			BookDAO 		 bookDao 	      = new BookDAO(conn);
//			LibraryBranchDAO libraryBranchDao = new LibraryBranchDAO(conn);
//			
//			book 		  = bookDao.readOneBookFirstLevel(rs.getInt("bookId"));
//			libraryBranch = libraryBranchDao.readOneLibrayBranchFirstLevel(rs.getInt("branchId"));
//			
//			bookCopy.setBook(book);
//			bookCopy.setLibraryBranch(libraryBranch);
//			bookCopy.setNoOfCopies(rs.getInt("noOfCopies"));
//			bookCopies.add(bookCopy);
//		}
//		return bookCopies;
//	}

	@Override
	public List extractData(ResultSet rs) throws SQLException {
		List<BookCopies> bookCopies = new ArrayList<>();
		while (rs.next() && rs.getInt("bookId") != 0) {
			BookCopies bookCopy = new BookCopies();
			Book 		  	 book 		   	  = new Book();
			LibraryBranch 	 libraryBranch 	  = new LibraryBranch();
			book.setBookId(rs.getInt("bookId"));
			libraryBranch.setBranchId(rs.getInt("branchId"));
//			BookDAO 		 bookDao 	      = new BookDAO(conn);
//			LibraryBranchDAO libraryBranchDao = new LibraryBranchDAO(conn);
			
//			book 		  = bookDao.readOneBookFirstLevel(rs.getInt("bookId"));
//			libraryBranch = libraryBranchDao.readOneLibrayBranchFirstLevel(rs.getInt("branchId"));
			
			bookCopy.setBook(book);
			bookCopy.setLibraryBranch(libraryBranch);
			bookCopy.setNoOfCopies(rs.getInt("noOfCopies"));
			bookCopies.add(bookCopy);
		}
		return bookCopies;
	}

}
