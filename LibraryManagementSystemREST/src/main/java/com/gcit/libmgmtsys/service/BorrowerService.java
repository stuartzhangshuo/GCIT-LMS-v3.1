/**
 * @Author Shuo Zhang <shuo_zhang@gcitsolutions.com>
 * @Date Sep 28, 2017
 */
package com.gcit.libmgmtsys.service;

import java.sql.SQLException;
import java.util.List;

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
@RequestMapping(value = "/Borrower")
public class BorrowerService {
	// =================================================================================================================
	// SPRING DAOs
	// =================================================================================================================

	@Autowired
	BookDAO bookDao;

	@Autowired
	BookCopiesDAO bookCopiesDao;

	@Autowired
	BookLoansDAO bookLoansDao;

	@Autowired
	LibraryBranchDAO libraryBranchDao;

	@Autowired
	BorrowerDAO borrowerDao;
	
	
	// =================================================================================================================
	// POST
	// =================================================================================================================
	
	//insert or update (check-out or check-in) a book loan based on if dateOut attribute is NULL
	@Transactional
	@RequestMapping(value = "/updateBookLoan_borrower", method = RequestMethod.POST, consumes = "application/json")
	public void updateBookLoan(@RequestBody BookLoans bookLoan) throws SQLException {
		Book 	   book 	   = bookDao.readOneBook(bookLoan.getBook().getBookId());
		BookCopies bookCopy    = new BookCopies();
		Integer    noOfCopies  = book.getBranchCopies().get(bookLoan.getLibraryBranch().getBranchId());
		bookCopy.setBook(book);
		bookCopy.setLibraryBranch(bookLoan.getLibraryBranch());
		if (bookLoan.getDateOut() == null) {
			bookCopy.setNoOfCopies(noOfCopies - 1);
			bookLoansDao.addBookLoan(bookLoan);
			bookCopiesDao.updateBookCopies(bookCopy);
		} else {
			bookCopy.setNoOfCopies(noOfCopies + 1);
			bookLoansDao.updateBookLoan(bookLoan);
			bookCopiesDao.updateBookCopies(bookCopy);
		}
	}
	
	
	// =================================================================================================================
	// GET
	// =================================================================================================================
	
	//read ALL books information
	@RequestMapping(value = "/readBooks_borrower/{searchString}/{pageNo}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public List<Book> readBooks(@PathVariable String searchString, @PathVariable Integer pageNo) throws SQLException {
		return bookDao.readBooks(searchString, pageNo);
	}

	//read ALL library branch information
	@RequestMapping(value = "/readLibraryBranches_borrower/{searchString}/{pageNo}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public List<LibraryBranch> readLibraryBranches(@PathVariable String searchString, @PathVariable Integer pageNo) throws SQLException {
		return libraryBranchDao.readLibraryBranches(searchString, pageNo);
	}
	
	//read ONE borrower information given cardNo
	@RequestMapping(value = "/readOneBorrower_borrower/{cardNo}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Borrower readOneBorrower(@PathVariable Integer cardNo) throws SQLException {
		return borrowerDao.readOneBorrower(cardNo);
	}
	
	//read ONE book loan information given cardNo and branchId
	@RequestMapping(value = "/readOneBookLoan_borrower/{cardNo}/{branchId}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public List<BookLoans> readOneBookLoan(@PathVariable Integer cardNo, @PathVariable Integer branchId) throws SQLException {
		return bookLoansDao.readOneBookLoan(cardNo, branchId);
	}
	
}
