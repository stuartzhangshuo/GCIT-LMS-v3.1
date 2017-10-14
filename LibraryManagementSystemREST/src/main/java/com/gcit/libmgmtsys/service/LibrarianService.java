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
	
	@Autowired
	AuthorDAO authorDao;
	
	@Autowired
	GenreDAO genreDao;
	
	@Autowired
	BorrowerDAO borrowerDao;
	
	@Autowired
	BookLoansDAO bookLoansDao;
	
	
	// =================================================================================================================
	// POST
	// =================================================================================================================
	
	/*
	 * UPDATE one library branch's information
	 */
	@Transactional
	@RequestMapping(value = "/updateBranchInfo", method = RequestMethod.POST, consumes = "application/json")
	public void updateBranchInfo(@RequestBody LibraryBranch libraryBranch) throws SQLException {
		libraryBranchDao.updateLibraryBranch(libraryBranch);
	}
	
	/*
	 * INSERT a book copy record into database
	 */
	@Transactional
	@RequestMapping(value = "/insertBookCopies", method = RequestMethod.POST, consumes = "application/json")
	public void insertBookCopies(@RequestBody BookCopies bookCopies) throws SQLException {
		bookCopiesDao.addBookCopies(bookCopies);
	}
	
	/*
	 * UPDATE an existing book copy's record
	 */
	@Transactional
	@RequestMapping(value = "/updateBookCopies", method = RequestMethod.POST, consumes = "application/json")
	public void updateBookCopies(@RequestBody BookCopies bookCopies) throws SQLException {
		bookCopiesDao.updateBookCopies(bookCopies);
	}
	

	// =================================================================================================================
	// GET
	// =================================================================================================================
	
	/*
	 * READ ONE book's information given book id
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/readOneBook/{bookId}", produces = {"application/json", "application/xml"})
	public Book readOneBook(@PathVariable Integer bookId) throws SQLException {
		Book book = bookDao.readOneBook(bookId);
		book.setAuthors(authorDao.readAuthorsByBook(book));
		book.setGenres(genreDao.readGenresByBook(book));
		book.setBorrowers(borrowerDao.readBorrowersByBook(book));
		book.setBranchCopies(bookCopiesDao.readBookCopiesByBook(book));
		return book;
	}
	
	/*
	 * READ ALL books' information from tbl_book
	 */
	@RequestMapping(value = "/readBooks/{searchString}/{pageNo}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public List<Book> readBooks(@PathVariable String searchString, @PathVariable Integer pageNo) throws SQLException {
		List<Book> books = bookDao.readBooks(null, pageNo);
		for (Book book : books) {
			book.setAuthors(authorDao.readAuthorsByBook(book));
			book.setGenres(genreDao.readGenresByBook(book));
			book.setBorrowers(borrowerDao.readBorrowersByBook(book));
			book.setBranchCopies(bookCopiesDao.readBookCopiesByBook(book));
		}
		return books;
	}
	
	/*
	 * READ ONE library branch's information given branch id
	 */
	@RequestMapping(value = "/readOneBranch/{branchId}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public LibraryBranch readOneBranch(@PathVariable Integer branchId) throws SQLException {
		LibraryBranch branch = libraryBranchDao.readOneBranch(branchId);
		branch.setBookCopies(bookCopiesDao.readBookCopiesByBranch(branch));
		branch.setBookLoans(bookLoansDao.readBookLoanByBranch(branch));
		return branch;
	}
	
	/*
	 * READ ALL library branches's info from tbl_library_branch
	 */
	@RequestMapping(value = "/readLibraryBranches/{searchString}/{pageNo}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
		public List<LibraryBranch> readLibraryBranches(@PathVariable String searchString, @PathVariable Integer pageNo) throws SQLException {
		List<LibraryBranch> libraryBranches = libraryBranchDao.readLibraryBranches(searchString, pageNo);
		for (LibraryBranch branch : libraryBranches) {
			branch.setBookCopies(bookCopiesDao.readBookCopiesByBranch(branch));
			branch.setBookLoans(bookLoansDao.readBookLoanByBranch(branch));
		}
		return libraryBranches;
	}
	
	/*
	 * READ a library branch name from tbl_library_branch
	 * @Return TRUE OR FALSE
	 */
	@RequestMapping(value = "/checkBranchName/{branchName}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Boolean checkBranchName(@PathVariable String branchName) throws SQLException {
		return libraryBranchDao.checkBranchByName(branchName) != null;
	}
	
	/*
	 * READ a book copy pair (bookId, branchId) from tbl_book_copies
	 * @Return TURE OR FALSE
	 */
	@RequestMapping(value = "/checkBookCopy", method = RequestMethod.GET, consumes = "application/json", produces = {"application/json", "application/xml"})
	public Boolean checkBookCopy(@RequestBody BookCopies bookCopy) throws SQLException {
		List<BookCopies> bookCopies = bookCopiesDao.checkBookCopies(bookCopy);
		if (bookCopies == null || bookCopies.size() == 0) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
}
