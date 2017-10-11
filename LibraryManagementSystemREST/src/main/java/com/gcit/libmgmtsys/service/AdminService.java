/**
 * @Author Shuo Zhang <shuo_zhang@gcitsolutions.com>
 * @Date Sep 28, 2017
 */
package com.gcit.libmgmtsys.service;

import java.sql.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.libmgmtsys.dao.*;
import com.gcit.libmgmtsys.entity.*;

@RestController
public class AdminService {
	// private Utilities util = new Utilities();

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

	@Transactional
	@RequestMapping(value = "/addAuthor", method = RequestMethod.POST, consumes = "application/json")
	//add an author into the databbase and associate this author with books in its book list
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

	@Transactional
	public void addBook(Book book) throws SQLException {
		if (book.getBookId() == null) {
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
		} else {
			bookDao.updateBook(book);
		}
	}

	@Transactional
	public Integer addAuthorWithId(Author author) throws SQLException {
		return authorDao.addAuthorWithID(author);
	}

	@Transactional
	public void deleteAuthor(Author author) throws SQLException {
		authorDao.deleteAuthor(author);
	}
	
	@RequestMapping(value = "/readOneAuthor", method = RequestMethod.GET, produces = "application/json")
	public Author readOneAuthor(Integer authorId) throws SQLException {
		return authorDao.readOneAuthor(authorId);
	}

	public Borrower readOneBorrower(Integer cardNo) throws SQLException {
		return borrowerDao.readOneBorrower(cardNo);
	}

	public Book readOneBook(Integer bookId) throws SQLException {
		return bookDao.readOneBook(bookId);
	}

	public Publisher readOnePublisher(Integer publisherId) throws SQLException {
		return publisherDao.readOnePublisher(publisherId);
	}

	public BookLoans readOneBookLoan(BookLoans bookLoan) throws SQLException {
		return bookLoansDao.readOneBookLoan(bookLoan);
	}

	public LibraryBranch readOneBranch(Integer branchId) throws SQLException {
		return libraryBranchDao.readOneBranch(branchId);
	}
	
	public Genre readOneGenre(Integer genreId) throws SQLException {
		return genreDao.readOneGenre(genreId);
	}

	// Check if an author name already exist in the database
	public Boolean checkAuthorName(String authorName) throws SQLException {
		return authorDao.checkAuthorByName(authorName) != null;
	}

	public Boolean checkBookName(String bookName) throws SQLException {
		return bookDao.checkBookByName(bookName) != null;
	}
	
	public List<Author> readAuthors(String searchString, Integer pageNo) throws SQLException {
		return authorDao.readAuthors(searchString, pageNo);
	}
	
	@RequestMapping(value = "/readAuthors", method = RequestMethod.GET)
	public List<Author> readAuthors() throws SQLException {
		List<Author> authors = authorDao.readAuthors(null, null);
		
		return authors;
	}

	public List<BookLoans> readBookLoans(String searchString, Integer pageNo) throws SQLException {
		return bookLoansDao.readBookLoans(searchString, pageNo);
	}

	// public List<Genre> readGenres(String searchString) throws SQLException {
	// Connection conn = null;
	// try {
	// conn = util.getConnection();
	// GenreDAO genreDao = new GenreDAO(conn);
	// return genreDao.getGenres(searchString);
	// } catch (InstantiationException | IllegalAccessException |
	// ClassNotFoundException | SQLException e) {
	// e.printStackTrace();
	// } finally {
	// if (conn != null) {
	// conn.close();
	// }
	// }
	// return null;
	// }

	public List<Genre> readGenres(String searchString, Integer pageNo) throws SQLException {
		return genreDao.readGenres(searchString, pageNo);
	}

	public Integer getAuthorsCount() throws SQLException {
		return authorDao.getAuthorsCount();
	}

	public Integer getBookLoansCount() throws SQLException {
		return bookLoansDao.getBookLoansCount();
	}

	public Integer getGenresCount() throws SQLException {
		return genreDao.getGenresCount();
	}

	public Integer getPublishersCount() throws SQLException {
		return publisherDao.getPublishersCount();
	}

	public Integer getLibraryBranchCount() throws SQLException {
		return libraryBranchDao.getLibraryBranchesCount();
	}

	public Integer getBooksCount() throws SQLException {
		return bookDao.getBooksCount();
	}

	public List<Publisher> readPublishers(String searchString, Integer pageNo) throws SQLException {
		return publisherDao.readPublishers(searchString, pageNo);
	}

	public List<LibraryBranch> readLibraryBranches(String searchString, Integer pageNo) throws SQLException {
		return libraryBranchDao.readLibraryBranches(searchString, pageNo);
	}

	// public List<BookLoans> readBookLoans(String cardNo, String branchId) throws
	// SQLException {
	// Connection conn = null;
	// try {
	// conn = util.getConnection();
	// BookLoansDAO bookLoansDao = new BookLoansDAO(conn);
	// return bookLoansDao.readBookLoans(cardNo, branchId);
	// } catch (InstantiationException | IllegalAccessException |
	// ClassNotFoundException | SQLException e) {
	// e.printStackTrace();
	// } finally {
	// if (conn != null) {
	// conn.close();
	// }
	// }
	// return null;
	// }

	public List<Borrower> readBorrowers(String searchString, Integer pageNo) throws SQLException {
		return borrowerDao.readBorrowers(searchString, pageNo);
	}

	public List<Book> readBooks(String searchString, Integer pageNo) throws SQLException {
		return bookDao.readBooks(searchString, pageNo);
	}

	@Transactional
	public void addGenre(Genre genre) throws SQLException {
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

	@Transactional
	public void addPublisher(Publisher publisher) throws SQLException {
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

	@Transactional
	public void addLibraryBranch(LibraryBranch branch, Boolean addBooks) throws SQLException {
		if (branch.getBranchId() == null) {
			Integer branchId = libraryBranchDao.addLibraryBranchWithID(branch);
			if (addBooks) {
				for (BookCopies bookCopy : branch.getBookCopies()) {
					bookCopy.getLibraryBranch().setBranchId(branchId);
					bookCopiesDao.addBookCopies(bookCopy);
				}
			}
		} else {
			libraryBranchDao.updateLibraryBranch(branch);
		}
	}

	@Transactional
	public void addBookAuthor(int bookId, Integer authorId) throws SQLException {
		bookAuthorDao.addBookAuthor(bookId, authorId);
	}

	@Transactional
	public void deleteGenre(Genre genre) throws SQLException {
		genreDao.deleteGenre(genre);
	}

	@Transactional
	public void deletePublisher(Publisher publisher) throws SQLException {
		publisherDao.deletePublisher(publisher);
	}

	@Transactional
	public Integer addGenreWithId(Genre genre) throws SQLException {
		return genreDao.addGenreWithID(genre);
	}

	@Transactional
	public void addBookGenres(Integer bookId, Integer genreId) throws SQLException {
		bookGenreDao.addBookGenre(bookId, genreId);
	}

	@Transactional
	public void updateLibraryBranch(LibraryBranch branch) throws SQLException {
		libraryBranchDao.updateLibraryBranch(branch);
	}

	// public Integer addPublisherWithId(Publisher publisher) throws SQLException {
	// Connection conn = null;
	// try {
	// conn = util.getConnection();
	// PublisherDAO publisherDao = new PublisherDAO(conn);
	// Integer id = publisherDao.addPublisherWithID(publisher);
	// conn.commit();
	// return id;
	// } catch (InstantiationException | IllegalAccessException |
	// ClassNotFoundException | SQLException e) {
	// e.printStackTrace();
	// conn.rollback();
	// } finally {
	// if (conn != null) {
	// conn.close();
	// }
	// }
	// return null;
	// }

	// public void addBookPublisher(Integer bookId, Integer publisherId) throws
	// SQLException {
	// Connection conn = null;
	// try {
	// conn = util.getConnection();
	// BookDAO bookDao = new BookDAO(conn);
	// bookDao.updateBookPublisherWithPubId(bookId, publisherId);
	// conn.commit();
	// } catch (InstantiationException | IllegalAccessException |
	// ClassNotFoundException | SQLException e) {
	// e.printStackTrace();
	// conn.rollback();
	// } finally {
	// if (conn != null) {
	// conn.close();
	// }
	// }
	// }

	// public void updateAuthor(Author author) throws SQLException {
	// Connection conn = null;
	// try {
	// conn = util.getConnection();
	// AuthorDAO authorDao = new AuthorDAO(conn);
	// authorDao.updateAuthorName(author);
	// if (author.getBooks() == null || author.getBooks().size() == 0) {
	// authorDao.deleteBookAuthor(author);
	// } else {
	// authorDao.deleteBookAuthor(author);
	// authorDao.updateBookAuthor(author);
	// }
	// conn.commit();
	// } catch (InstantiationException | IllegalAccessException |
	// ClassNotFoundException | SQLException e) {
	// e.printStackTrace();
	// conn.rollback();
	// } finally {
	// if (conn != null) {
	// conn.close();
	// }
	// }
	// }

	@Transactional
	public void updateBook(Book book) throws SQLException {
		bookDao.updateBook(book);// change name
		if (book.getAuthors() == null || book.getAuthors().size() == 0) {
			bookDao.deleteBookAuthor(book);
		} else {
			bookDao.deleteBookAuthor(book);
			bookDao.updateBookAuthor(book);
		}
		if (book.getGenres() == null || book.getGenres().size() == 0) {
			bookDao.deleteBookGenre(book);
		} else {
			bookDao.deleteBookGenre(book);
			bookDao.updateBookGenre(book);
		}
		if (book.getPublisher() != null) {
			bookDao.updateBookPublisher(book);
		}
	}

	public Boolean checkGenreName(String genreName) throws SQLException {
		return genreDao.checkGenreByName(genreName) != null;
	}

	public Boolean checkPublisherName(String publisherName) throws SQLException {
		return publisherDao.checkPublisherByName(publisherName) != null;
	}

	// public void updateGenre(Genre genre) throws SQLException {
	// Connection conn = null;
	// try {
	// conn = util.getConnection();
	// GenreDAO genreDao = new GenreDAO(conn);
	// genreDao.updateGenreName(genre);
	// if (genre.getBooks() == null || genre.getBooks().size() == 0) {
	// genreDao.deleteBookGenre(genre);
	// } else {
	// genreDao.deleteBookGenre(genre);
	// genreDao.updateBookGenre(genre);
	// }
	// conn.commit();
	// } catch (InstantiationException | IllegalAccessException |
	// ClassNotFoundException | SQLException e) {
	// e.printStackTrace();
	// conn.rollback();
	// } finally {
	// if (conn != null) {
	// conn.close();
	// }
	// }
	// }

	@Transactional
	public void updatePublisher(Publisher publisher) throws SQLException {
		publisherDao.updatePublisherInfo(publisher);
		if (publisher.getBooks() == null || publisher.getBooks().size() == 0) {
			publisherDao.deleteBookPublisher(publisher);
		} else {
			publisherDao.deleteBookPublisher(publisher);
			publisherDao.updateBookPublisher(publisher);
		}
	}

	@Transactional
	public void deleteBook(Book book) throws SQLException {
		bookDao.deleteBook(book);
	}

	@Transactional
	public void deleteBranch(LibraryBranch branch) throws SQLException {
		libraryBranchDao.deleteLibraryBranch(branch);
	}

	public boolean checkBranchName(String branchName) throws SQLException {
		return libraryBranchDao.checkBranchByName(branchName) != null;
	}

	public Boolean checkBorrowerName(String borrowerName) throws SQLException {
		return borrowerDao.checkBorrowerByName(borrowerName) != null;
	}

	@Transactional
	public void addBorrower(Borrower borrower) throws SQLException {
		if (borrower.getCardNo() == null) {
			borrowerDao.addBorrower(borrower);
		} else {
			borrowerDao.updateBorrower(borrower);
		}
	}

	@Transactional
	public void deleteBorrower(Borrower borrower) throws SQLException {
		borrowerDao.deleteBorrower(borrower);
	}

	@Transactional
	public void updateBorrower(Borrower borrower) throws SQLException {
		borrowerDao.updateBorrower(borrower);
	}

	@Transactional
	public void deleteBookLoan(BookLoans bookLoan) throws SQLException {
		bookLoansDao.deleteBookLoan(bookLoan);
	}

	@Transactional
	public void overrideBookLoan(BookLoans bookLoan) throws SQLException {
		bookLoansDao.overrideBookLoan(bookLoan);
	}
}
