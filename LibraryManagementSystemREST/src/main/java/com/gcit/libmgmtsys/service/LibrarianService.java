/**
 * @Author Shuo Zhang <shuo_zhang@gcitsolutions.com>
 * @Date Sep 28, 2017
 */
package com.gcit.libmgmtsys.service;

import java.sql.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.libmgmtsys.dao.*;
import com.gcit.libmgmtsys.entity.*;

@RestController
@RequestMapping(value = "/librarian")
public class LibrarianService {
	// =================================================================================================================
	// SPRING DAOs
	// =================================================================================================================
	
	@Autowired
	LibraryBranchDAO libraryBranchDao;

	@Autowired
	BookDAO bookDao;

	@Autowired
	BookCopiesDAO bookCopiesDao;
	
	
	// =================================================================================================================
	// POST
	// =================================================================================================================
	
	//update one library branch's information
	@Transactional
	@RequestMapping(value = "/updateBranchInfo", method = RequestMethod.POST, consumes = "application/json")
	public void updateBranchInfo(@RequestBody LibraryBranch libraryBranch) throws SQLException {
		libraryBranchDao.updateLibraryBranch(libraryBranch);
	}
	
	//insert a book copy record into database
	@Transactional
	@RequestMapping(value = "/insertBookCopies", method = RequestMethod.POST, consumes = "application/json")
	public void insertBookCopies(@RequestBody BookCopies bookCopies) throws SQLException {
		bookCopiesDao.addBookCopies(bookCopies);
	}
	
	//update an existing book copy's record
	@Transactional
	@RequestMapping(value = "/updateBookCopies", method = RequestMethod.POST, consumes = "application/json")
	public void updateBookCopies(@RequestBody BookCopies bookCopies) throws SQLException {
		bookCopiesDao.updateBookCopies(bookCopies);
	}
	

	// =================================================================================================================
	// GET
	// =================================================================================================================
	
	//read one book information given book id
	@RequestMapping(method = RequestMethod.GET, value = "/readOneBook/{bookId}", produces = {"application/json", "application/xml"})
	public Book readOneBook(@PathVariable Integer bookId) throws SQLException {
		Book book = bookDao.readOneBook(bookId);
		return bookDao.readOneBook(bookId);
	}
	
	//read all books by page number
	@RequestMapping(value = "/readBooks/{searchString}/{pageNo}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public List<Book> readBooks(@PathVariable String searchString, @PathVariable Integer pageNo) throws SQLException {
		return bookDao.readBooks(searchString, pageNo);
	}
	
	//read one library branch information given branch id
	@RequestMapping(value = "/readOneBranch/{branchId}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public LibraryBranch readOneBranch(@PathVariable Integer branchId) throws SQLException {
		return libraryBranchDao.readOneBranch(branchId);
	}
	
	//read all library branches by page number
	@RequestMapping(value = "/readLibraryBranches/{searchString}/{pageNo}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
		public List<LibraryBranch> readLibraryBranches(@PathVariable String searchString, @PathVariable Integer pageNo) throws SQLException {
		return libraryBranchDao.readLibraryBranches(searchString, pageNo);
	}
	
	//check if a branch name already exists in the database
	@RequestMapping(value = "/checkBranchName/{branchName}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Boolean checkBranchName(@PathVariable String branchName) throws SQLException {
		return libraryBranchDao.checkBranchByName(branchName) != null;
	}
	
	//check if a book copy pair (bookId, branchId) exist in the databse
	@RequestMapping(value = "/checkBookCopy", method = RequestMethod.GET, consumes = "application/json", produces = {"application/json", "application/xml"})
	public Boolean checkBookCopy(@RequestBody BookCopies bookCopy) throws SQLException {
		List<BookCopies> bookCopies = bookCopiesDao.checkBookCopies(bookCopy);
		if (bookCopies == null || bookCopies.size() == 0) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
}
