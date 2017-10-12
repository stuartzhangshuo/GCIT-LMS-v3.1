/**
 * @Author Shuo Zhang <shuo_zhang@gcitsolutions.com>
 * @Date Sep 27, 2017
 */
package com.gcit.libmgmtsys.dao;

import java.sql.*;
import java.util.*;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.libmgmtsys.entity.Author;
import com.gcit.libmgmtsys.entity.Book;

@SuppressWarnings("rawtypes")
public class AuthorDAO extends BaseDAO implements ResultSetExtractor<List<Author>> {
	// insert a new author into author table
	public void addAuthor(Author author) throws SQLException {
		template.update("INSERT INTO tbl_author (authorName) VALUES(?)", new Object[] { author.getAuthorName() });
	}

	// Insert the given author into the database and return generated ID
	public Integer addAuthorWithID(Author author) throws SQLException {
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_author (authorName) VALUES(?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, author.getAuthorName());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}

	// update the name of an author
	public void updateAuthorName(Author author) throws SQLException {
		template.update("UPDATE tbl_author SET authorName = ? WHERE authorId = ?",
				new Object[] { author.getAuthorName(), author.getAuthorId() });
	}

	// delete an author from author table
	public void deleteAuthor(Author author) throws SQLException {
		template.update("DELETE FROM tbl_author WHERE authorId = ?", new Object[] { author.getAuthorId() });
	}

	// return how many authors in the database
	public Integer getAuthorsCount() throws SQLException {
		return template.queryForObject("SELECT COUNT(*) as COUNT FROM tbl_author", Integer.class);
	}

	// read all authors or all authors have a similar name to input name
	public List<Author> readAuthors(String authorName, Integer pageNo) throws SQLException {
		setPageNo(pageNo);
		if (authorName != null && !authorName.isEmpty()) {
			authorName = "%" + authorName + "%";
			return template.query("SELECT * FROM tbl_author WHERE authorName LIKE ?", new Object[] { authorName },
					this);
		} else {
			return template.query("SELECT * FROM tbl_author", this);
		}
	}

	// return all authors of a book given book id
	public List<Author> readAuthorsByBook(Book book) {
		List<Author> authors = template.query("SELECT * FROM tbl_author WHERE authorId IN (SELECT authorId FROM tbl_book_authors WHERE bookId = ?)",
				new Object[] {book.getBookId()}, this);
		if (authors != null) {
			return authors;
		}
		return null;
	}

	// Read one author by author id
	public Author readOneAuthor(Integer authorId) throws SQLException {
		List<Author> authors = template.query("SELECT * FROM tbl_author WHERE authorId = ?", new Object[] { authorId },
				this);
		if (authors != null) {
			return authors.get(0);
		}
		return null;
	}

	// associate author with all books in his book list
	public void addBookAuthor(Author author) throws SQLException {
		for (Book book : author.getBooks()) {
			template.update("INSERT INTO tbl_book_authors VALUES(?, ?)",
					new Object[] { book.getBookId(), author.getAuthorId() });
		}
	}

	// delete all books' author association with this author.
	public void deleteBookAuthor(Author author) throws SQLException {
		template.update("DELETE FROM tbl_book_authors WHERE authorId = ?", new Object[] { author.getAuthorId() });
	}

	// check if an author's name is in the database already
	public List<Author> checkAuthorByName(String authorName) throws SQLException {
		return template.query("SELECT * FROM tbl_author WHERE authorName = ?", new Object[] { authorName }, this);
	}

	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException {
		List<Author> authors = new ArrayList<>();
		while (rs.next()) {
			Author author = new Author();
			author.setAuthorId(rs.getInt("authorId"));
			author.setAuthorName(rs.getString("authorName"));
			authors.add(author);
		}
		return authors;
	}
}
