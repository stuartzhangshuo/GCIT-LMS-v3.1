/**
 * @Book Shuo Zhang <shuo_zhang@gcitsolutions.com>
 * @Date Sep 28, 2017
 */
package com.gcit.libmgmtsys.dao;

import java.sql.*;
import java.util.*;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.libmgmtsys.entity.*;

@SuppressWarnings({"rawtypes"})
public class BookDAO extends BaseDAO implements ResultSetExtractor<List<Book>>{
	
	//insert a new book into book table
	public void addBook(Book book) throws SQLException {
		template.update("INSERT INTO tbl_book (bookName) VALUES(?)",
				new Object[] {book.getTitle()});
	}
	
	//insert a new book into book table and return generated ID
//	public Integer addBookWithID(Book book) throws SQLException {
//		return executeUpdateWithID("INSERT INTO tbl_book (title) VALUES(?)",
//				new Object[] {book.getTitle()});
//	}
	public Integer addBookWithID(Book book) throws SQLException{
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_book (title) VALUES(?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, book.getTitle());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}
	
	//update the name of a book
	public void updateBook(Book book) throws SQLException {
		template.update("UPDATE tbl_book SET title = ? WHERE bookId = ?",
				new Object[] {book.getTitle(), book.getBookId()});
	}
	
	public void updateBookAuthor(Book book) throws SQLException {
		for (Author author : book.getAuthors()) {
			template.update("INSERT INTO tbl_book_authors VALUES(?, ?)", new Object[] {book.getBookId(), author.getAuthorId()});
		}
	}
	
	public void updateBookGenre(Book book) throws SQLException {
		for (Genre genre : book.getGenres()) {
			template.update("INSERT INTO tbl_book_genres VALUES(?, ?)", new Object[] {genre.getGenreId(), book.getBookId()});
		}
	}
	
	//update the publisher of a book
	public void updateBookPublisher(Book book) throws SQLException {
		template.update("UPDATE tbl_book SET pubId = ? WHERE bookId = ?", 
				new Object[] {book.getPublisher().getPublisherId(), book.getBookId()});
	}
	
//	public void updateBookPublisherWithPubId(Integer bookId, Integer pubId) throws SQLException {
//		executeUpdate("UPDATE tbl_book SET pubId = ? WHERE bookId = ?", new Object[] {pubId, bookId});
//	}
	
	//delete an Book from book table
	public void deleteBook(Book book) throws SQLException {
		template.update("DELETE FROM tbl_book WHERE bookId = ?",
				new Object[] {book.getBookId()});
	}
	
	public void deleteBookAuthor(Book book) throws SQLException {
		template.update("DELETE FROM tbl_book_authors WHERE bookId = ?", new Object[] {book.getBookId()});
	}
	
	public void deleteBookGenre(Book book) throws SQLException {
		template.update("DELETE FROM tbl_book_genres WHERE bookId = ?", new Object[] {book.getBookId()});
	}
	
	public void deletePublisher(Book book) throws SQLException {
		template.update("UPDATE tbl_book SET pubId = NULL WHERE bookId = ?", new Object[] {book.getBookId()});
	}
	
	//get all books or all books have a similar title to the input title
	public List<Book> readBooks(String bookName, Integer pageNo) throws SQLException {
		setPageNo(pageNo);
		if (bookName != null && !bookName.isEmpty()) {
			bookName = "%" + bookName + "%";
			return template.query("SELECT * FROM tbl_book WHERE title LIKE ?",
					new Object[] {bookName}, this);
		} else {
			return template.query("SELECT * FROM tbl_book", this);
		}
	}
	
	public Integer getBooksCount() throws SQLException {
		return template.queryForObject("SELECT COUNT(*) as COUNT FROM tbl_book", Integer.class);
	}
	
	public void addPublisher(Book book) throws SQLException {
		template.update("UPDATE tbl_book SET pubId = ? WHERE bookId = ?",
				new Object[] {book.getPublisher().getPublisherId(), book.getBookId()});
	}
	
//	public List<Book> getBooksWithBranchId(String branchId) throws NumberFormatException, SQLException {
//		AuthorDAO     authorDao     = new AuthorDAO(conn);
//		GenreDAO      genreDao      = new GenreDAO(conn);
//		PublisherDAO  publisherDao  = new PublisherDAO(conn);
//		//BookCopiesDAO bookCopiesDao = new BookCopiesDAO(conn);
//		
//		String sql = "SELECT * FROM tbl_book b, tbl_book_copies bc WHERE b.bookId = bc.bookId AND branchId = ?";
//		
//		String  sql_author = "SELECT * FROM tbl_author WHERE authorId IN " +
//							"(SELECT authorId FROM tbl_book_authors WHERE bookId = ?)";
//		
//		String  sql_publisher = "SELECT * FROM tbl_publisher WHERE publisherId IN " +
//								"(SELECT pubId FROM tbl_book WHERE bookId = ?)";
//		
//		String  sql_genre  = "SELECT * FROM tbl_genre WHERE genre_id IN " +
//							"(SELECT genre_id FROM tbl_book_genres WHERE bookId = ?)";
//		
//		
//		PreparedStatement pstmt = conn.prepareStatement(sql);
//		pstmt.setInt(1, Integer.parseInt(branchId));
//		ResultSet rs = pstmt.executeQuery();
//		List<Book> booksWithBranchId = new ArrayList<>();
//		while (rs.next()) {
//			Book book = new Book();
//			book.setBookId(rs.getInt(1));
//			book.setTitle(rs.getString(2));
//			book.setAuthors(authorDao.executeFirstLevelQuery(sql_author, new Object[] {book.getBookId()}));
//			book.setGenres(genreDao.executeFirstLevelQuery(sql_genre, new Object[] {book.getBookId()}));
//			book.setPublishers(publisherDao.executeFirstLevelQuery(sql_publisher, new Object[] {book.getBookId()}));
//			book.setNumOfCopies(rs.getInt("noOfCopies"));
//			booksWithBranchId.add(book);
//		}
//		return booksWithBranchId;
//	}
	
	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException {
//		AuthorDAO     authorDao     = new AuthorDAO(conn);
//		GenreDAO      genreDao      = new GenreDAO(conn);
//		BorrowerDAO   borrowerDao   = new BorrowerDAO(conn);
//		BookCopiesDAO bookCopiesDao = new BookCopiesDAO(conn);
//		PublisherDAO  publisherDao  = new PublisherDAO(conn);
//		
//		String  sql_author = "SELECT * FROM tbl_author WHERE authorId IN " +
//					        "(SELECT authorId FROM tbl_book_authors WHERE bookId = ?)";
//		
//		String  sql_genre  = "SELECT * FROM tbl_genre WHERE genre_id IN " +
//			                "(SELECT genre_id FROM tbl_book_genres WHERE bookId = ?)";
		
//		String sql_publisher = "SELECT * FROM tbl_publisher WHERE publisherId = ?";
		
//		String sql_borrowers = "SELECT * FROM tbl_borrower WHERE cardNo IN " +
//                			  "(SELECT cardNo FROM tbl_book_loans WHERE bookId = ? AND dateIn IS NULL)";
		
		//String sql_noOfCopies = "SELECT bookId, branchId, sum(noOfCopies) as noOfCopies FROM tbl_book_copies WHERE bookId = ?";
		//String sql_noOfCopies = "SELECT * FROM tbl_book_copies WHERE bookId = ?";
		
		List<Book> books = new ArrayList<>();
		while (rs.next()) {
			Book book = new Book();
			book.setBookId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));
			Publisher publisher = new Publisher();
			publisher.setPublisherId(rs.getInt("pubId"));
			book.setPublisher(publisher);
//			List<Publisher> publisher = publisherDao.executeFirstLevelQuery(sql_publisher, new Object[] {rs.getInt("pubId")});
//			if (publisher == null || publisher.isEmpty()) {
//				book.setPublisher(null);
//			} else {
//				book.setPublisher(publisher.get(0));
//			}
//			book.setAuthors(authorDao.executeFirstLevelQuery(sql_author, new Object[] {book.getBookId()}));
//			book.setGenres(genreDao.executeFirstLevelQuery(sql_genre, new Object[] {book.getBookId()}));
//			book.setBorrowers(borrowerDao.executeFirstLevelQuery(sql_borrowers, new Object[] {book.getBookId()}));

//			List<BookCopies> bookCopies = bookCopiesDao.executeFirstLevelQuery(sql_noOfCopies, new Object[] {book.getBookId()});
//			HashMap<Integer, Integer> map = new HashMap<>();
//			if (bookCopies == null || bookCopies.size() == 0) {
//				book.setTotalNumOfCopies(0);
//				book.setBranchCopies(map);
//			} else {
//				int total = 0;
//				for (BookCopies bookCopy : bookCopies) {
//					total += bookCopy.getNoOfCopies();
//					map.put(bookCopy.getLibraryBranch().getBranchId(), bookCopy.getNoOfCopies());
//				}
//				book.setBranchCopies(map);
//				book.setTotalNumOfCopies(total);	//no of copies in all branch
//			}
			books.add(book);
		}
		return books;
	}
	
//	@Override
//	protected List<Book> parseFirstLevelData(ResultSet rs) throws SQLException {
//		List<Book> books = new ArrayList<>();
//		while (rs.next()) {
//			Book book = new Book();
//			book.setBookId(rs.getInt("bookId"));
//			book.setTitle(rs.getString("title"));
//			books.add(book);
//		}
//		return books;
//	}
	public Book readOneBook(Integer bookId) throws SQLException {
		List<Book> books = template.query("SELECT * FROM tbl_book WHERE bookId = ?", 
				new Object[] {bookId}, this);
		if (books != null) {
			return books.get(0);
		}
		return null;
	}
	
//	public Book readOneBookFirstLevel(Integer bookId) throws SQLException {
//		List<Book> books = executeFirstLevelQuery("SELECT * FROM tbl_book WHERE bookId = ?", 
//				new Object[] {bookId});
//		if (books != null) {
//			return books.get(0);
//		}
//		return null;
//	}
	
	public List<Book> checkBookByName(String title) throws SQLException {
		List<Book> books = template.query("SELECT * FROM tbl_book WHERE title = ?", 
				new Object[] {title}, this);
		if (books.size() > 0) {
			return books;
		}
		return null;
	}
	public void addBookAuthor(Book book) throws SQLException {
		for (Author author : book.getAuthors()) {
			template.update("INSERT INTO tbl_book_authors VALUES(?, ?)", 
					new Object[] {book.getBookId(), author.getAuthorId()});
		}
	}
	public void addBookGenre(Book book) throws SQLException {
		for (Genre genre : book.getGenres()) {
			template.update("INSERT INTO tbl_book_genres VALUES(?, ?)", 
					new Object[] {genre.getGenreId(), book.getBookId()});
		}
	}
	
}
