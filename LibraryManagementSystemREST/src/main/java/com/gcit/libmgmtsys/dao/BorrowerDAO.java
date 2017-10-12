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

import com.gcit.libmgmtsys.entity.Book;
import com.gcit.libmgmtsys.entity.BookLoans;
import com.gcit.libmgmtsys.entity.Borrower;

@SuppressWarnings("rawtypes")
public class BorrowerDAO extends BaseDAO implements ResultSetExtractor<List<Borrower>>{
	//insert a new borrower
	public void addBorrower(Borrower borrower) throws SQLException {
		template.update("INSERT INTO tbl_borrower (name, address, phone) VALUES(?, ?, ?)",
				new Object[] {borrower.getName(), borrower.getAddress(), borrower.getPhone()});
	}
	
	//insert a new borrower and return generated key
	public Integer addBorrowerWithID(Borrower borrower) throws SQLException{
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_borrower (name, address, phone) VALUES(?, ?, ?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, borrower.getName());
				ps.setString(2, borrower.getAddress());
				ps.setString(3, borrower.getPhone());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}
	
	//update borrower information
	public void updateBorrower(Borrower borrower) throws SQLException {
		template.update("UPDATE tbl_borrower SET name = ?, address = ?, phone = ? WHERE cardNo = ?", 
				new Object[] {borrower.getName(), borrower.getAddress(), borrower.getPhone(), borrower.getCardNo()});
	}
	
	//delete a borrower
	public void deleteBorrower(Borrower borrower) throws SQLException {
		template.update("DELETE FROM tbl_borrower WHERE cardNo = ?",
				new Object[] {borrower.getCardNo()});
	}
	
	//read all borrowers' information from tbl_borrower
	public List<Borrower> readBorrowers(String borrowerName, Integer pageNo) throws SQLException {
		setPageNo(pageNo);
		if (borrowerName != null && !borrowerName.isEmpty()) {
			borrowerName = "%" + borrowerName + "%";
			return template.query("SELECT * FROM tbl_author WHERE authorName LIKE ?",
					new Object[] {borrowerName}, this);
		} else {
			return template.query("SELECT * FROM tbl_borrower", this);
		}
	}
	
	//read all borrower's information who have checked out the given book.
	public List<Borrower> readBorrowersByBook(Book book) throws SQLException {
		return template.query("SELECT * FROM tbl_borrower WHERE cardNo IN (SELECT cardNo FROM tbl_book_loans WHERE bookId = ?)",
				new Object[] {book.getBookId()}, this);
	}
	
	//read one borrower's information from tbl_borrower given cardNo
	public Borrower readOneBorrower(Integer cardNo) throws SQLException {
		List<Borrower> borrowers = template.query("SELECT * FROM tbl_borrower WHERE cardNo = ?", 
				new Object[] {cardNo}, this);
		if (borrowers != null) {
			return borrowers.get(0);
		}
		return null;
	}
	
	//check if a borrower's name already exists in the database
	public List<Borrower> checkBorrowerByName(String borrowerName) throws SQLException {
		List<Borrower> borrowers = template.query("SELECT * FROM tbl_borrower WHERE name = ?", 
				new Object[] {borrowerName}, this);
		if (borrowers.size() > 0) {
			return borrowers;
		}
		return null;
	}

	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException {
		List<Borrower> borrowers = new ArrayList<>();
		while (rs.next()) {
			Borrower borrower = new Borrower();
			borrower.setCardNo(rs.getInt("cardNo"));
			borrower.setName(rs.getString("name"));
			borrower.setAddress(rs.getString("address"));
			borrower.setPhone(rs.getString("phone"));
			List<BookLoans> bookLoans = new ArrayList<>();
			borrower.setBookLoans(bookLoans);
			borrowers.add(borrower);
		}
		return borrowers;
	}

	

	

	
}
