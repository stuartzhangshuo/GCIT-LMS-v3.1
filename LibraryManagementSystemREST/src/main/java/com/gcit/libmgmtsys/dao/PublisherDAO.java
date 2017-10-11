/**
 * @Author Shuo Zhang <shuo_zhang@gcitsolutions.com>
 * @Date Sep 28, 2017
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
import com.gcit.libmgmtsys.entity.Publisher;

@SuppressWarnings("rawtypes")
public class PublisherDAO extends BaseDAO implements ResultSetExtractor<List<Publisher>>{
	//insert a new publisher into publisher table
	public void addPublisher(Publisher publisher) throws SQLException {
		template.update("INSERT INTO tbl_publisher (publisherName, publisherAddress, publisherPhone) VALUES(?, ?, ?)",
				new Object[] {publisher.getPublisherName(), publisher.getPublisherAddress(), publisher.getPublisherPhone()});
	}
	
	//insert a new publisher into publisher table and return generated ID
	public Integer addPublisherWithId(Publisher publisher) throws SQLException{
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_publisher (publisherName, publisherAddress, publisherPhone) VALUES(?, ?, ?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, publisher.getPublisherName());
				ps.setString(2, publisher.getPublisherAddress());
				ps.setString(3, publisher.getPublisherPhone());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}
	
//	public Integer addPublisherWithId(Publisher publisher) throws SQLException {
//		return executeUpdateWithID("INSERT INTO tbl_publisher (publisherName, publisherAddress, publisherPhone) VALUES(?, ?, ?)",
//				new Object[] {publisher.getPublisherName(), publisher.getPublisherAddress(), publisher.getPublisherPhone()});
//	}
	
	//update the name of a publisher
	public void updatePublisherInfo(Publisher publisher) throws SQLException {
		template.update("UPDATE tbl_publisher SET publisherName = ?, publisherAddress = ?, publisherPhone = ? WHERE publisherId = ?",
				new Object[] {publisher.getPublisherName(), publisher.getPublisherAddress(), publisher.getPublisherPhone(), publisher.getPublisherId()});
	}
	
	//delete a publisher from publisher table
	public void deletePublisher(Publisher publisher) throws SQLException {
		template.update("DELETE FROM tbl_publisher WHERE publisherId = ?",
				new Object[] {publisher.getPublisherId()});
	}
	
	public Publisher readOnePublisher(Integer publisherID) throws SQLException{
		List<Publisher> publishers = template.query("SELECT * FROM tbl_publisher WHERE publisherId = ?", 
				new Object[] {publisherID}, this);
		if (publishers != null) {
			return publishers.get(0);
		}
		return null;
	}
	
	public List<Publisher> readPublishers(String publisherName, Integer pageNo) throws SQLException {
		setPageNo(pageNo);
		if (publisherName != null && !publisherName.isEmpty()) {
			publisherName = "%" + publisherName + "%";
			return template.query("SELECT * FROM tbl_publisher WHERE publisherName LIKE ?",
					new Object[] {publisherName}, this);
		} else {
			return template.query("SELECT * FROM tbl_publisher", this);
		}
	}
	
//	public List<Publisher> getPublishers(String publisherName) throws SQLException {
//		if (publisherName != null && !publisherName.isEmpty()) {
//			publisherName = "%" + publisherName + "%";
//			return executeQuery("SELECT * FROM tbl_publisher WHERE publisherName LIKE ?",
//					new Object[] {publisherName});
//		} else {
//			return executeQuery("SELECT * FROM tbl_publisher", null);
//		}
//	}

//	@Override
//	protected List<Publisher> parseFirstLevelData(ResultSet rs) throws SQLException {
//		List<Publisher> publishers = new ArrayList<>();
//		while (rs.next()) {
//			Publisher publisher = new Publisher();
//			publisher.setPublisherId(rs.getInt("publisherId"));
//			publisher.setPublisherName(rs.getString("publisherName"));
//			publisher.setPublisherAddress(rs.getString("publisherAddress"));
//			publisher.setPublisherPhone(rs.getString("publisherPhone"));
//			publishers.add(publisher);
//		}
//		return publishers;
//	}

	@Override
	public List<Publisher> extractData(ResultSet rs) throws SQLException {
//		BookDAO bookDAO = new BookDAO(conn);
//		String sql = "SELECT * FROM tbl_book WHERE pubId = ?";
		List<Publisher> publishers = new ArrayList<>();
		while (rs.next()) {
			Publisher publisher = new Publisher();
			publisher.setPublisherId(rs.getInt("publisherId"));
			publisher.setPublisherName(rs.getString("publisherName"));
			publisher.setPublisherAddress(rs.getString("publisherAddress"));
			publisher.setPublisherPhone(rs.getString("publisherPhone"));
//			publisher.setBooks(bookDAO.executeFirstLevelQuery(sql, new Object[] {publisher.getPublisherId()}));
			publishers.add(publisher);
		}
		return publishers;
	}

	public Integer getPublishersCount() throws SQLException {
		return template.queryForObject("SELECT COUNT(*) as COUNT FROM tbl_publisher", Integer.class);
	}

	public List<Publisher> checkPublisherByName(String publisherName) throws SQLException {
		List<Publisher> publishers = template.query("SELECT * FROM tbl_publisher WHERE publisherName = ?", 
				new Object[] {publisherName}, this);
		if (publishers.size() > 0) {
			return publishers;
		}
		return null;
	}

	public void updateBookPublisher(Publisher publisher) throws SQLException {
		for (Book book : publisher.getBooks()) {
			template.update("UPDATE tbl_book SET pubId = ? WHERE bookId = ?", 
					new Object[] {publisher.getPublisherId(), book.getBookId()});
		}
	}

	public void deleteBookPublisher(Publisher publisher) throws SQLException {
		template.update("UPDATE tbl_book SET pubId = NULL WHERE pubId = ?", new Object[] {publisher.getPublisherId()});
	}
}
