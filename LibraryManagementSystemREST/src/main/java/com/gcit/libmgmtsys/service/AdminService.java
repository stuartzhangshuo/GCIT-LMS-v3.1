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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.libmgmtsys.dao.*;
import com.gcit.libmgmtsys.entity.*;

@RestController
@RequestMapping(value = "/admin")
public class AdminService {
	// =================================================================================================================
	// SPRING DAOs
	// =================================================================================================================

	@Autowired
	AuthorDAO authorDao;

	@Autowired
	BookDAO bookDao;

	@Autowired
	BorrowerDAO borrowerDao;

	@Autowired
	PublisherDAO publisherDao;

	@Autowired
	BookLoansDAO bookLoansDao;

	@Autowired
	LibraryBranchDAO libraryBranchDao;

	@Autowired
	GenreDAO genreDao;

	@Autowired
	BookCopiesDAO bookCopiesDao;

	@Autowired
	BookAuthorDAO bookAuthorDao;

	@Autowired
	BookGenreDAO bookGenreDao;
	

	// =================================================================================================================
	// BOOK SERVICES
	// =================================================================================================================

	/*
	 * add a book into the database, and associate publisher, authors, and genres
	 * with it. If the book id is null, update the book instead of add it.
	 */
	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/books/newBook",  consumes = "application/json")
	public void addBook(@RequestBody Book book) throws SQLException {
		Integer id = bookDao.addBookWithID(book);
		book.setBookId(id);
		if (book.getPublisher() != null) {
			bookDao.addPublisher(book);
		}
		if (book.getAuthors() != null && book.getAuthors().size() != 0) {
			bookDao.addBookAuthor(book);
		}
		if (book.getGenres() != null && book.getGenres().size() != 0) {
			bookDao.addBookGenre(book);
		}
	}

	/*
	 * update a book's info
	 */
	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/books/bookRef",  consumes = "application/json")
	public void updateBook(@RequestBody Book book) throws SQLException {
		bookDao.updateTitle(book);// change name
		if (book.getAuthors() == null || book.getAuthors().size() == 0) {
			bookDao.deleteBookAuthor(book);
		} else {
			bookDao.deleteBookAuthor(book);
			bookDao.addAuthors(book);
		}
		if (book.getGenres() == null || book.getGenres().size() == 0) {
			bookDao.deleteBookGenre(book);
		} else {
			bookDao.deleteBookGenre(book);
			bookDao.addGenres(book);
		}
		if (book.getPublisher() != null) {
			bookDao.updateBookPublisher(book);
		}
	}

	/*
	 * delete a book from tbl_book
	 */
	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/books/bookId", consumes = "application/json")
	public void deleteBook(@RequestBody Book book) throws SQLException {
		bookDao.deleteBook(book);
	}

	/*
	 * read all books from the database
	 */
	@RequestMapping(value = "/books", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public List<Book> readBooks(@RequestParam(value = "bookTitle", required = false) String searchString, 
							    @RequestParam(value = "pageNo") Integer pageNo) throws SQLException {
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
	 * read one book from the database given bookId
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/books/bookId/{bookId}", produces = {"application/json", "application/xml"})
	public Book readOneBook(@PathVariable Integer bookId) throws SQLException {
		Book book = bookDao.readOneBook(bookId);
		book.setAuthors(authorDao.readAuthorsByBook(book));
		book.setGenres(genreDao.readGenresByBook(book));
		book.setBorrowers(borrowerDao.readBorrowersByBook(book));
		book.setBranchCopies(bookCopiesDao.readBookCopiesByBook(book));
		return book;
	}
	
	/*
	 * check if a book's title already exists in the database
	 */
	@RequestMapping(value = "/books/bookTitle/{bookTitle}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Boolean checkBookName(@PathVariable String bookTitle) throws SQLException {
		return bookDao.checkBookByName(bookTitle) != null;
	}

	/*
	 * return total number of books in tbl_book.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/books/pageCount/bookId/{bookId}", produces = {"application/json", "application/xml"})
	public Integer getBooksCount() throws SQLException {
		return bookDao.getBooksCount();
	}

	// =================================================================================================================
	// AUTHOR SERVICES
	// =================================================================================================================
	
	/*
	 * add an author into the databbase and associate this author with books in its book list
	 */
	@Transactional
	@RequestMapping(value = "/author/newAuthor", method = RequestMethod.POST, consumes = "application/json")
	public void addAuthor(@RequestBody Author author) throws SQLException {
		if (author.getAuthorId() == null) {
			Integer id = authorDao.addAuthorWithID(author);
			if (author.getBooks() != null && author.getBooks().size() != 0) {
				author.setAuthorId(id);
				authorDao.addAuthor(author);
			}
		} else {
			authorDao.updateAuthorName(author);
		}
	}
	
	/*
	 * add a new author into tbl_author
	 */
	@Transactional
	@RequestMapping(value = "/authors/newAuthor/id", method = RequestMethod.POST, consumes = "application/json")
	public Integer addAuthorWithId(Author author) throws SQLException {
		return authorDao.addAuthorWithID(author);
	}
	
	/*
	 * read all authors from tbl_author
	 */
	@RequestMapping(value = "/authors", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public List<Author> readAuthors(@RequestParam (value = "authorName", required = false) String searchString, 
									@RequestParam (value = "pageNo") Integer pageNo) throws SQLException {
		List<Author> authors = authorDao.readAuthors(searchString, pageNo);
		for (Author author : authors) {
			author.setBooks(bookDao.readBookByAuthor(author));
		}
		return authors;
	}
	
	/*
	 * read one author information given author id
	 */
	@RequestMapping(value = "/authors/authorId/{authorId}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Author readOneAuthor(@PathVariable Integer authorId) throws SQLException {
		Author author = authorDao.readOneAuthor(authorId);
		author.setBooks(bookDao.readBookByAuthor(author));
		return author;
	}
	
	/*
	 * delete an author given author id
	 */
	@Transactional
	@RequestMapping(value = "/authors/authorId/authorId", method = RequestMethod.POST, consumes = "application/json")
	public void deleteAuthor(@RequestBody Author author) throws SQLException {
		authorDao.deleteAuthor(author);
	}
	
	/*
	 * Check if an author name already exist in the database
	 */
	@RequestMapping(value = "/authors/authorName/{authorName}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Boolean checkAuthorName(@PathVariable String authorName) throws SQLException {
		return authorDao.checkAuthorByName(authorName) != null;
	}
	
	/*
	 * count how many authors are there in tbl_author
	 */
	@RequestMapping(value = "/authors/authorCount", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Integer getAuthorsCount() throws SQLException {
		return authorDao.getAuthorsCount();
	}
	
	
	// =================================================================================================================
	// GENRE SERVICES
	// =================================================================================================================
	
	/*
	 * add a new genre to tbl_genre
	 */
	@Transactional
	@RequestMapping(value = "/genres/newGenre", method = RequestMethod.POST, consumes = "application/json")
	public void addGenre(@RequestBody Genre genre) throws SQLException {
		if (genre.getGenreId() == null) {
			Integer id = genreDao.addGenreWithID(genre);
			if (genre.getBooks() != null && genre.getBooks().size() != 0) {
				genre.setGenreId(id);
				genreDao.addBookGenre(genre);
			}
		} else {
			genreDao.updateGenreName(genre);
		}
	}
	
	/*
	 * add a new genre to tbl_genre and return the generated key
	 */
	@Transactional
	@RequestMapping(value = "/genres/newGenre/id", method = RequestMethod.POST, consumes = "application/json")
	public Integer addGenreWithId(@RequestBody Genre genre) throws SQLException {
		return genreDao.addGenreWithID(genre);
	}
	
	/*
	 * delete a genre from tbl_genre given genre_id
	 */
	@Transactional
	@RequestMapping(value = "/genres/genreId/", method = RequestMethod.POST, consumes = "application/json")
	public void deleteGenre(@RequestBody Genre genre) throws SQLException {
		genreDao.deleteGenre(genre);
	}
	
	/*
	 * read one genre given genre id
	 */
	@RequestMapping(value = "/genres/genreId/{genreId}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Genre readOneGenre(Integer genreId) throws SQLException {
		Genre genre = genreDao.readOneGenre(genreId);
		genre.setBooks(bookDao.readBookByGenre(genre));
		return genre;
	}
	
	/*
	 * read all genres from tbl_genre or filter result by using keywords and page number
	 */
	@RequestMapping(value = "/genres/keywords={searchString}/pageNo={pageNo}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public List<Genre> readGenres(@RequestParam (value = "genreName", required = false) String searchString,
					   			  @RequestParam (value = "pageNo") Integer pageNo) throws SQLException {
		List<Genre> genres = genreDao.readGenres(searchString, pageNo);
		for (Genre genre :genres) {
			genre.setBooks(bookDao.readBookByGenre(genre));
		}
		return genres;
	}
	
	/*
	 * return total number of genres in tbl_genre
	 */
	@RequestMapping(value = "/genres/genreCount", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Integer getGenresCount() throws SQLException {
		return genreDao.getGenresCount();
	}
	
	/*
	 * check if given genre name is already exists in tbl_genre
	 */
	@RequestMapping(value = "/genres/genreName/{genreName}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Boolean checkGenreName(@PathVariable String genreName) throws SQLException {
		return genreDao.checkGenreByName(genreName) != null;
	}
	

	// =================================================================================================================
	// PUBLISHER SERVICES
	// =================================================================================================================
	
	/*
	 * add a new publisher to tbl_publisher
	 */
	@Transactional
	@RequestMapping(value = "/publishers/newPublisher", method = RequestMethod.POST, consumes = "application/json")
	public void addPublisher(@RequestBody Publisher publisher) throws SQLException {
		if (publisher.getPublisherId() == null) {
			Integer id = publisherDao.addPublisherWithId(publisher);
			if (publisher.getBooks() != null && publisher.getBooks().size() != 0) {
				publisher.setPublisherId(id);
				publisherDao.updateBookPublisher(publisher);
			}
		} else {
			publisherDao.updatePublisherInfo(publisher);
		}
	}
	
	/*
	 * delete a publisher from tbl_publisher given pubId
	 */
	@Transactional
	@RequestMapping(value = "/publishers/pubId", method = RequestMethod.POST, consumes = "application/json")
	public void deletePublisher(@RequestBody Publisher publisher) throws SQLException {
		publisherDao.deletePublisher(publisher);
	}
	
	/*
	 * update a publisher information in tbl_publisher
	 */
	@Transactional
	@RequestMapping(value = "/publishers/publisherRef", method = RequestMethod.POST, consumes = "application/json")
	public void updatePublisher(@RequestBody Publisher publisher) throws SQLException {
		publisherDao.updatePublisherInfo(publisher);
		if (publisher.getBooks() == null || publisher.getBooks().size() == 0) {
			publisherDao.deleteBookPublisher(publisher);
		} else {
			publisherDao.deleteBookPublisher(publisher);
			publisherDao.updateBookPublisher(publisher);
		}
	}
	
	/*
	 * read one publihser information given pubId
	 */
	@RequestMapping(value = "/publishers/pubId/{publisherId}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Publisher readOnePublisher(@PathVariable Integer publisherId) throws SQLException {
		Publisher publisher = publisherDao.readOnePublisher(publisherId);
		publisher.setBooks(bookDao.readBookByPublisher(publisher));
		return publisher;
	}
	
	/*
	 * read all publishers or filter results by publisherName and pageNo
	 */
	@RequestMapping(value = "/publishers", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public List<Publisher> readPublishers(@RequestParam (value = "publisherName", required = false) String searchString,
										  @RequestParam (value = "pageNo") Integer pageNo) throws SQLException {
		List<Publisher> publishers = publisherDao.readPublishers(searchString, pageNo);
		for (Publisher publisher : publishers) {
			publisher.setBooks(bookDao.readBookByPublisher(publisher));
		}
		return publishers;
	}
	
	/*
	 * return how many publishers are there in tbl_publisher
	 */
	@RequestMapping(value = "/publishers/publisherCount", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Integer getPublishersCount() throws SQLException {
		return publisherDao.getPublishersCount();
	}
	
	/*
	 * check if a publisher name already exists in tbl_publisher
	 */
	@RequestMapping(value = "/publishers/pulisherName/{publisherName}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Boolean checkPublisherName(@PathVariable String publisherName) throws SQLException {
		return publisherDao.checkPublisherByName(publisherName) != null;
	}
	

	// =================================================================================================================
	// BOOK-AUTHOR ASSOCIATION SERVICES
	// =================================================================================================================
	
	/*
	 * INSERT a book-author associations into tbl_book_authors
	 */
	@Transactional
	@RequestMapping(value = "/bookAuthors/bookId/{bookId}/authorId/{authorId}", method = RequestMethod.POST, produces = {"application/json", "application/xml"})
	public void addBookAuthor(@PathVariable Integer bookId, @PathVariable Integer authorId) throws SQLException {
		bookAuthorDao.addBookAuthor(bookId, authorId);
	}
	

	// =================================================================================================================
	// BOOK-GENRE ASSOCIATION SERVICES
	// =================================================================================================================
	
	/*
	 * INSERT a book-genre associations into tbl_book_genres
	 */
	@Transactional
	@RequestMapping(value = "/bookGenres/bookId/{bookId}/genreId/{genreId}", method = RequestMethod.POST, produces = {"application/json", "application/xml"})
	public void addBookGenres(@PathVariable Integer bookId, @PathVariable Integer genreId) throws SQLException {
		bookGenreDao.addBookGenre(bookId, genreId);
	}
	
	
	// =================================================================================================================
	// LIBRARY BRANCH SERVICES
	// =================================================================================================================
	
	/*
	 * CREATE a new library branch into tbl_library_branch, and associate books with it.
	 */
	@Transactional
	@RequestMapping(value = "/libraryBranches/newBranch", method = RequestMethod.POST, consumes = "application/json")
	public void addLibraryBranch(@RequestBody LibraryBranch branch) throws SQLException {
		if (branch.getBranchId() == null) {
			Integer branchId = libraryBranchDao.addLibraryBranchWithID(branch);
			if (branch.getBookCopies() != null && branch.getBookCopies().size() > 0) {
				for (BookCopies bookCopy : branch.getBookCopies()) {
					bookCopy.getLibraryBranch().setBranchId(branchId);
					bookCopiesDao.addBookCopies(bookCopy);
				}
			}
		} else {
			libraryBranchDao.updateLibraryBranch(branch);
		}
	}
	
	/*
	 * UPDATE the information of a library branch given branch id
	 */
	@Transactional
	@RequestMapping(value = "/libraryBranches/branchRef", method = RequestMethod.POST, consumes = "application/json")
	public void updateLibraryBranch(@RequestBody LibraryBranch branch) throws SQLException {
		libraryBranchDao.updateLibraryBranch(branch);
	}
	
	/*
	 * DELETE a library branch given branch id
	 */
	@Transactional
	@RequestMapping(value = "/libraryBranches/branchId", method = RequestMethod.POST, consumes = "application/json")
	public void deleteBranch(@RequestBody LibraryBranch branch) throws SQLException {
		libraryBranchDao.deleteLibraryBranch(branch);
	}
	
	/*
	 * READ ONE library branch information given branch id
	 */
	@RequestMapping(value = "/libraryBranches/branchId/{branchId}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public LibraryBranch readOneBranch(@PathVariable Integer branchId) throws SQLException {
		LibraryBranch branch = libraryBranchDao.readOneBranch(branchId);
		branch.setBookCopies(bookCopiesDao.readBookCopiesByBranch(branch));
		branch.setBookLoans(bookLoansDao.readBookLoanByBranch(branch));
		return branch;
	}
	
	/*
	 * READ total number of library branches in tbl_library_branch
	 */
	@RequestMapping(value = "/libraryBranches/branchCount", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Integer getLibraryBranchCount() throws SQLException {
		return libraryBranchDao.getLibraryBranchesCount();
	}
	
	/*
	 * READ all library branches' information from tbl_library_branch
	 */
	@RequestMapping(value = "/libraryBranches/keywords={searchString}/pageNo={pageNo}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
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
	 * @Return TRUE OR FALSE;
	 */
	@RequestMapping(value = "/libraryBranches/branchName/{branchName}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Boolean checkBranchName(String branchName) throws SQLException {
		return libraryBranchDao.checkBranchByName(branchName) != null;
	}
	
	
	// =================================================================================================================
	// BORROWER SERVICES
	// =================================================================================================================
	
	/*
	 * CREATE a new borrower in tbl_borrower
	 */
	@Transactional
	@RequestMapping(value = "/borrowers/newBorrower", method = RequestMethod.POST, consumes = "application/json")
	public void addBorrower(@RequestBody Borrower borrower) throws SQLException {
		if (borrower.getCardNo() == null) {
			borrowerDao.addBorrower(borrower);
		} else {
			borrowerDao.updateBorrower(borrower);
		}
	}
	
	/*
	 * DELETE a borrower from tbl_borrower given cardNo
	 */
	@Transactional
	@RequestMapping(value = "/borrowers/borrowerId", method = RequestMethod.POST, consumes = "application/json")
	public void deleteBorrower(@RequestBody Borrower borrower) throws SQLException {
		borrowerDao.deleteBorrower(borrower);
	}
	
	/*
	 * UPDATE a borrower's information given cardNo
	 */
	@Transactional
	@RequestMapping(value = "/borrowers/borrowerRef", method = RequestMethod.POST, consumes = "application/json")
	public void updateBorrower(@RequestBody Borrower borrower) throws SQLException {
		borrowerDao.updateBorrower(borrower);
	}
	
	/*
	 * READ ONE borrower's information from tbl_borrower given cardNo
	 */
	@RequestMapping(value = "/borrowers/cardNo/{cardNo}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Borrower readOneBorrower(Integer cardNo) throws SQLException {
		Borrower borrower = borrowerDao.readOneBorrower(cardNo);
		borrower.setBookLoans(bookLoansDao.readBookLoanByBorrower(borrower));
		return borrower;
	}
	
	/*
	 * READ ALL borrowers' information from tbl_borrower
	 */
	@RequestMapping(value = "/borrowers/keywords={searchString}/pageNo={pageNo}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public List<Borrower> readBorrowers(@PathVariable String searchString, @PathVariable Integer pageNo) throws SQLException {
		List<Borrower> borrowers = borrowerDao.readBorrowers(searchString, pageNo);
		for (Borrower borrower : borrowers) {
			borrower.setBookLoans(bookLoansDao.readBookLoanByBorrower(borrower));
		}
		return borrowers;
	}
	
	/*
	 * READ a borrower's name from tbl_borrower
	 * @Return TRUE OR FALSE
	 */
	@RequestMapping(value = "/libraryBranches/borrowerName", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Boolean checkBorrowerName(@PathVariable String borrowerName) throws SQLException {
		return borrowerDao.checkBorrowerByName(borrowerName) != null;
	}
	

	// =================================================================================================================
	// BOOK LOAN SERVICES
	// =================================================================================================================
	
	/*
	 * DELETE a book loan from tbl_book_loans given bookId, branchId, cardNo and dateOut
	 */
	@Transactional
	@RequestMapping(value = "/bookLoans/cardNo", method = RequestMethod.POST, consumes = "application/json")
	public void deleteBookLoan(@RequestBody BookLoans bookLoan) throws SQLException {
		bookLoansDao.deleteBookLoan(bookLoan);
	}
	
	/*
	 * UPDATE a book loan's dueDate given bookId, branchId, cardNo and dateOut
	 */
	@Transactional
	@RequestMapping(value = "/bookLoans/bookLoanRef", method = RequestMethod.POST, consumes = "application/json")
	public void overrideBookLoan(@RequestBody BookLoans bookLoan) throws SQLException {
		bookLoansDao.overrideBookLoan(bookLoan);
	}
	
	/*
	 * READ ALL book loans' information from tbl_book_loans
	 */
	@RequestMapping(value = "/bookLoans/keywords={searchString}/pageNo={pageNo}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public List<BookLoans> readBookLoans(@PathVariable String searchString, @PathVariable Integer pageNo) throws SQLException {
		return bookLoansDao.readBookLoans(searchString, pageNo);
	}
	
	/*
	 * READ ONE book loan's information given bookId, branchId, cardNo and dateOut
	 */
	@RequestMapping(value = "/bookLoans/cardNo/{cardNo}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public BookLoans readOneBookLoan(@PathVariable BookLoans bookLoan) throws SQLException {
		return bookLoansDao.readOneBookLoan(bookLoan);
	}
	
	/*
	 * READ total number of book loans in tbl_book_loans
	 */
	@RequestMapping(value = "/bookLoans/loanCount", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
	public Integer getBookLoansCount() throws SQLException {
		return bookLoansDao.getBookLoansCount();
	}

}
