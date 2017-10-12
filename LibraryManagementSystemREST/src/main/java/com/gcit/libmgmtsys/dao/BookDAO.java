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

@SuppressWarnings({ "rawtypes" })
public class BookDAO extends BaseDAO implements ResultSetExtractor<List<Book>> {

	// insert a new book into book table
	public void addBook(Book book) throws SQLException {
		template.update("INSERT INTO tbl_book (bookName) VALUES(?)", new Object[] { book.getTitle() });
	}

	// insert a new book into book table and return generated id
	public Integer addBookWithID(Book book) throws SQLException {
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

	// update the title of a book
	public void updateTitle(Book book) throws SQLException {
		template.update("UPDATE tbl_book SET title = ? WHERE bookId = ?",
				new Object[] { book.getTitle(), book.getBookId() });
	}

	// add the authors of a book into the database
	public void addAuthors(Book book) throws SQLException {
		for (Author author : book.getAuthors()) {
			template.update("INSERT INTO tbl_book_authors VALUES(?, ?)",
					new Object[] { book.getBookId(), author.getAuthorId() });
		}
	}

	// add the genres of a book into the database
	public void addGenres(Book book) throws SQLException {
		for (Genre genre : book.getGenres()) {
			template.update("INSERT INTO tbl_book_genres VALUES(?, ?)",
					new Object[] { genre.getGenreId(), book.getBookId() });
		}
	}

	// update the publisher of a book
	public void updateBookPublisher(Book book) throws SQLException {
		template.update("UPDATE tbl_book SET pubId = ? WHERE bookId = ?",
				new Object[] { book.getPublisher().getPublisherId(), book.getBookId() });
	}

	// delete an Book from book table
	public void deleteBook(Book book) throws SQLException {
		template.update("DELETE FROM tbl_book WHERE bookId = ?", new Object[] { book.getBookId() });
	}

	// delete all author associations of a book given book id
	public void deleteBookAuthor(Book book) throws SQLException {
		template.update("DELETE FROM tbl_book_authors WHERE bookId = ?", new Object[] { book.getBookId() });
	}

	// delete all genre associations of a book given book id
	public void deleteBookGenre(Book book) throws SQLException {
		template.update("DELETE FROM tbl_book_genres WHERE bookId = ?", new Object[] { book.getBookId() });
	}

	// set the publisher for a book
	public void addPublisher(Book book) throws SQLException {
		template.update("UPDATE tbl_book SET pubId = ? WHERE bookId = ?",
				new Object[] { book.getPublisher().getPublisherId(), book.getBookId() });
	}

	// set the publisher association of a book to NULL
	public void deletePublisher(Book book) throws SQLException {
		template.update("UPDATE tbl_book SET pubId = NULL WHERE bookId = ?", new Object[] { book.getBookId() });
	}

	// read one book from tbl_book
	public Book readOneBook(Integer bookId) throws SQLException {
		List<Book> books = template.query("SELECT * FROM tbl_book WHERE bookId = ?", new Object[] { bookId }, this);
		if (books != null & books.size() != 0) {
			return books.get(0);
		}
		return null;
	}

	// read all books or all books have a similar title to the input title
	public List<Book> readBooks(String title, Integer pageNo) throws SQLException {
		setPageNo(pageNo);
		if (title != null && !title.isEmpty()) {
			title = "%" + title + "%";
			return template.query("SELECT * FROM tbl_book WHERE title LIKE ?", new Object[] { title }, this);
		} else {
			return template.query("SELECT * FROM tbl_book", this);
		}
	}

	// count how many books are there in tbl_book
	public Integer getBooksCount() throws SQLException {
		return template.queryForObject("SELECT COUNT(*) as COUNT FROM tbl_book", Integer.class);
	}

	// check if a book title already exist in the database
	public List<Book> checkBookByName(String title) throws SQLException {
		List<Book> books = template.query("SELECT * FROM tbl_book WHERE title = ?", new Object[] { title }, this);
		if (books.size() > 0) {
			return books;
		}
		return null;
	}

	// associate a book with its authors
	public void addBookAuthor(Book book) throws SQLException {
		for (Author author : book.getAuthors()) {
			template.update("INSERT INTO tbl_book_authors VALUES(?, ?)",
					new Object[] { book.getBookId(), author.getAuthorId() });
		}
	}

	// associate a book with its genres
	public void addBookGenre(Book book) throws SQLException {
		for (Genre genre : book.getGenres()) {
			template.update("INSERT INTO tbl_book_genres VALUES(?, ?)",
					new Object[] { genre.getGenreId(), book.getBookId() });
		}
	}

	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<>();
		while (rs.next()) {
			Book book = new Book();
			book.setBookId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));
			Publisher publisher = new Publisher();
			publisher.setPublisherId(rs.getInt("pubId"));
			book.setPublisher(publisher);
			books.add(book);
		}
		return books;
	}
}
