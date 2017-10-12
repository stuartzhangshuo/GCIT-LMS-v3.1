/**
 * @Genre Shuo Zhang <shuo_zhang@gcitsolutions.com>
 * @Date Sep 28, 2017
 */
package com.gcit.libmgmtsys.dao;

import java.sql.*;
import java.util.*;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.libmgmtsys.entity.Genre;
import com.gcit.libmgmtsys.entity.Book;

@SuppressWarnings("rawtypes")
public class GenreDAO extends BaseDAO implements ResultSetExtractor<List<Genre>>{
	//insert a new genre into genre table
	public void addGenre(Genre genre) throws SQLException {
		template.update("INSERT INTO tbl_genre (genre_name) VALUES(?)",
				new Object[] {genre.getGenreName()});
	}
	
	//insert a new genre into genre table and return generated ID
	public Integer addGenreWithID(Genre genre) throws SQLException{
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_genre (genre_name) VALUES(?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, genre.getGenreName());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}
	
	//update the name of an genre
	public void updateGenreName(Genre genre) throws SQLException {
		template.update("UPDATE tbl_genre SET genre_name = ? WHERE genre_id = ?",
				new Object[] {genre.getGenreName(), genre.getGenreId()});
	}
	
	//delete an genre from genre table
	public void deleteGenre(Genre genre) throws SQLException {
		template.update("DELETE FROM tbl_genre WHERE genre_id = ?",
				new Object[] {genre.getGenreId()});
	}
	
	//count how many genres in genre table
	public Integer getGenresCount() throws SQLException {
		return template.queryForObject("SELECT COUNT(*) as COUNT FROM tbl_genre", Integer.class);
	}
	
	//read one genre by genre id
		public Genre readOneGenre(Integer genreId) throws SQLException {
			List<Genre> genres = template.query("SELECT * FROM tbl_genre WHERE genre_id = ?", 
					new Object[] {genreId}, this);
			if (genres != null) {
				return genres.get(0);
			}
			return null;
		}
	
	//get all genres or all genres have a similar name to input name
	public List<Genre> readGenres(String genreName, Integer pageNo) throws SQLException {
		setPageNo(pageNo);
		if (genreName != null && !genreName.isEmpty()) {
			genreName = "%" + genreName + "%";
			return template.query("SELECT * FROM tbl_genre WHERE genre_name LIKE ?",
					new Object[] {genreName}, this);
		} else {
			return template.query("SELECT * FROM tbl_genre", this);
		}
	}
	
	public List<Genre> readGenresByBook(Book book) {
		return template.query("SELECT * FROM tbl_genre WHERE genre_id IN (SELECT genre_id FROM tbl_book_genres WHERE bookId = ?)",
				new Object[] {book.getBookId()}, this);
	}
	
	//remove all genre book associations in tbl_book_genres of given genre
	public void deleteBookGenre(Genre genre) throws SQLException {
		template.update("DELETE FROM tbl_book_genres WHERE genre_id = ?", new Object[] {genre.getGenreId()});
	}
	
	//associate given genre with all books in its book list.
	public void addBookGenre(Genre genre) throws SQLException {
		for (Book book : genre.getBooks()) {
			template.update("INSERT INTO tbl_book_genres VALUES(?, ?)", 
					new Object[] {genre.getGenreId(), book.getBookId()});
		}
	}
	
	//check if given genre name exist in genre table
	public List<Genre> checkGenreByName(String genreName) throws SQLException {
		List<Genre> genres = template.query("SELECT * FROM tbl_genre WHERE genre_name = ?", 
				new Object[] {genreName}, this);
		if (genres.size() > 0) {
			return genres;
		}
		return null;
	}
	
	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException {
//		BookDAO bookDao = new BookDAO(conn);
//		String  sql     = "SELECT * FROM tbl_book WHERE bookId IN " +
//					     "(SELECT bookId FROM tbl_book_genres WHERE genre_id = ?)";
		List<Genre> genres = new ArrayList<>();
		while (rs.next()) {
			Genre genre = new Genre();
			genre.setGenreId(rs.getInt("genre_id"));
			genre.setGenreName(rs.getString("genre_name"));
//			genre.setBooks(bookDao.executeFirstLevelQuery(sql, new Object[] {genre.getGenreId()}));
			genres.add(genre);
		}
		return genres;
	}
	
//	//get all genres or all genres have a similar name to input name
//	public List<Genre> getGenres(String genreName) throws SQLException {
//		if (genreName != null && !genreName.isEmpty()) {
//			genreName = "%" + genreName + "%";
//			return template.query("SELECT * FROM tbl_genre WHERE genre_name LIKE ?",
//					new Object[] {genreName}, this);
//		} else {
//			return template.query("SELECT * FROM tbl_genre", this);
//		}
//	}
	
//	//associate given genre with all books in its book list.
//	public void updateBookGenre(Genre genre) throws SQLException {
//		for (Book book : genre.getBooks()) {
//			template.update("INSERT INTO tbl_book_genres VALUES(?, ?)", 
//					new Object[] {genre.getGenreId(), book.getBookId()});
//		}
//	}
	
//	public Integer addGenreWithID(Genre genre) throws SQLException {
//	return executeUpdateWithID("INSERT INTO tbl_genre (genre_name) VALUES(?)",
//			new Object[] {genre.getGenreName()});
//}
	
//	@Override
//	protected List<Genre> parseFirstLevelData(ResultSet rs) throws SQLException {
//		List<Genre> genres = new ArrayList<>();
//		while (rs.next()) {
//			Genre genre = new Genre();
//			genre.setGenreId(rs.getInt("genre_id"));
//			genre.setGenreName(rs.getString("genre_name"));
//			genres.add(genre);
//		}
//		return genres;
//	}
}
