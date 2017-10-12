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

import com.gcit.libmgmtsys.entity.BookLoans;

@SuppressWarnings("rawtypes")
public class BookLoansDAO extends BaseDAO implements ResultSetExtractor<List<BookLoans>>{
	//insert a new book loan
	public void addBookLoan(BookLoans bookLoan) throws SQLException {
		template.update("INSERT INTO tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate) "
				+ "VALUES(?, ?, ?, NOW(), NOW() + INTERVAL 7 DAY)",
				new Object[] {bookLoan.getBook().getBookId(), bookLoan.getLibraryBranch().getBranchId(), bookLoan.getBorrower().getCardNo()});
	}
	
	//insert a new book loan and return the generated ID
	public Integer addBookLoansWithID(BookLoans bookLoan) throws SQLException{
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate) "
				+ "VALUES(?, ?, ?, NOW(), NOW() + INTERVAL 7 DAY)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, bookLoan.getBook().getBookId());
				ps.setInt(2, bookLoan.getLibraryBranch().getBranchId());
				ps.setInt(3, bookLoan.getBorrower().getCardNo());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}
	
	//update the check-in
	public void updateBookLoan(BookLoans bookLoan) throws SQLException {
		template.update("UPDATE tbl_book_loans SET dateIn = NOW() "
				+ "WHERE bookId = ? AND branchId = ? AND cardNo = ? AND dateOut = ?", 
				new Object[] {bookLoan.getBook().getBookId(), bookLoan.getLibraryBranch().getBranchId(), bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut()});
	}
	
	//Override the due date of a book loan for 7 days.
	public void overrideBookLoan(BookLoans bookLoan) throws SQLException {
		template.update("UPDATE tbl_book_loans SET dueDate = DATE_ADD(dueDate, INTERVAL 7 DAY) "
				+ "WHERE bookId = ? AND branchId = ? AND cardNo = ? AND dateOut = ?", 
				new Object[] {bookLoan.getBook().getBookId(), bookLoan.getLibraryBranch().getBranchId(), bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut()});
	}
	
	public List<BookLoans> readBookLoans(String cardNo, Integer pageNo) throws NumberFormatException, SQLException {
		setPageNo(pageNo);
		if (cardNo != null && !cardNo.isEmpty()) {
			Integer borrowerId = Integer.parseInt(cardNo);
			return template.query("SELECT * FROM tbl_book_loans WHERE cardNo = ?",
					new Object[] {borrowerId}, this);
		} else {
			return template.query("SELECT * FROM tbl_book_loans", this);
		}
		
//		if (cardNo != null && !cardNo.isEmpty() && branchId != null && !branchId.isEmpty()) {
//			String sql = "SELECT bl.bookId, bl.branchId, bl.cardNo, b.title, lb.branchName, br.name, bl.dateOut, bl.dueDate, bl.dateIn " + 
//					 	 "FROM tbl_book_loans bl, tbl_book b, tbl_library_branch lb, tbl_borrower br " + 
//					 	 "WHERE bl.bookId = b.bookId AND bl.branchId = lb.branchId AND bl.cardNo = br.cardNo " +
//					 	        "AND bl.branchId = ? AND bl.cardNo = ? AND bl.dateIn IS NULL";
//			
//			return executeFirstLevelQuery(sql, new Object[] {Integer.parseInt(branchId), Integer.parseInt(cardNo)});
//		} else {
//			String sql = "SELECT bl.bookId, bl.branchId, bl.cardNo, b.title, lb.branchName, br.name, bl.dateOut, bl.dueDate, bl.dateIn " + 
//						 "FROM tbl_book_loans bl, tbl_book b, tbl_library_branch lb, tbl_borrower br " + 
//						 "WHERE bl.bookId = b.bookId AND bl.branchId = lb.branchId AND bl.cardNo = br.cardNo";
//			return executeFirstLevelQuery(sql, null);
//		}
	}
	
//	public List<BookLoans> getBookLoansByCardNoAndBranchId(String cardNo, String branchId) {
//		String sql = "SELECT bl.bookId, bl.branchId, bl.cardNo, b.title, lb.branchName, br.name, bl.dateOut, bl.dueDate, bl.dateIn " + 
//				 	 "FROM tbl_book_loans bl, tbl_book b, tbl_library_branch lb, tbl_borrower br " + 
//				 	 "WHERE bl.bookId = b.bookId AND bl.branchId = ? AND bl.cardNo = ?";
//		List<BookLoans> bookLoans = new ArrayList<>();
//		return executeFirstLevelQuery();
//		
//	}
	public List<BookLoans> readOneBookLoan(Integer cardNo, Integer branchId) throws SQLException {
		String sql = "SELECT * FROM tbl_book_loans WHERE cardNo = ? AND branchId = ? AND dateIn IS NULL";
		return template.query(sql, new Object[] {cardNo, branchId}, this);
	}
	
	
	//TO-DO: delete a book loan
	
	
//	@Override
//	protected List<BookLoans> parseFirstLevelData(ResultSet rs) throws SQLException {
////		String sql_book 	= "SELECT FROM tbl_book WHERE bookId = ?";
////		String sql_branch   = "SELECT FROM tbl_library_branch WHERE branchId = ?";
////		String sql_borrower = "SELECT FROM tbl_borrower WHERE cardNo = ?";
//		
//		BookDAO bookDao = new BookDAO(conn);
//		BorrowerDAO borrowerDao = new BorrowerDAO(conn);
//		LibraryBranchDAO libraryBranchDao = new LibraryBranchDAO(conn);
//		
//		List<BookLoans> bookLoans = new ArrayList<>();
//		while (rs.next()) {
//			BookLoans bookLoan = new BookLoans();
//			bookLoan.setBook(bookDao.readOneBookFirstLevel(rs.getInt("bookId")));
//			bookLoan.setBorrower(borrowerDao.readOneBorrowerFirstLevel(rs.getInt("cardNo")));
//			bookLoan.setLibraryBranch(libraryBranchDao.readOneLibrayBranchFirstLevel(rs.getInt("branchId")));
//			bookLoan.setDateOut(rs.getString("dateOut"));
//			bookLoan.setDueDate(rs.getString("dueDate"));
//			bookLoan.setDateIn(rs.getString("dateIn"));
//			bookLoans.add(bookLoan);
//		}
//		return bookLoans;
//	}

	@Override
	public List<BookLoans> extractData(ResultSet rs) throws SQLException {
//		String sql_book 	= "SELECT FROM tbl_book WHERE bookId = ?";
//		String sql_branch   = "SELECT FROM tbl_library_branch WHERE branchId = ?";
//		String sql_borrower = "SELECT FROM tbl_borrower WHERE cardNo = ?";
		
//		BookDAO bookDao = new BookDAO(conn);
//		BorrowerDAO borrowerDao = new BorrowerDAO(conn);
//		LibraryBranchDAO libraryBranchDao = new LibraryBranchDAO(conn);
		
		List<BookLoans> bookLoans = new ArrayList<>();
		while (rs.next()) {
			BookLoans bookLoan = new BookLoans();
//			bookLoan.setBook(bookDao.readOneBookFirstLevel(rs.getInt("bookId")));
//			bookLoan.setBorrower(borrowerDao.readOneBorrowerFirstLevel(rs.getInt("cardNo")));
//			bookLoan.setLibraryBranch(libraryBranchDao.readOneLibrayBranchFirstLevel(rs.getInt("branchId")));
			
			bookLoan.setDateOut(rs.getString("dateOut"));
			bookLoan.setDueDate(rs.getString("dueDate"));
			bookLoan.setDateIn(rs.getString("dateIn"));
			bookLoans.add(bookLoan);
		}
		return bookLoans;
	}

	public Integer getBookLoansCount() throws SQLException {
		return template.queryForObject("SELECT COUNT(*) as COUNT FROM tbl_book_loans", Integer.class);
	}

	public void deleteBookLoan(BookLoans bookLoan) throws SQLException {
		template.update("DELETE FROM tbl_book_loans WHERE bookId = ? AND branchId = ? AND cardNo = ? AND dateOut = ?",
				new Object[] {bookLoan.getBook().getBookId(), bookLoan.getLibraryBranch().getBranchId(),
							  bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut()});
	}

	public BookLoans readOneBookLoan(BookLoans bookLoan) throws SQLException {
		List<BookLoans> bookLoans = template.query("SELECT * FROM tbl_book_loans WHERE bookId = ? AND branchId = ? AND cardNo = ? AND dateOut = ?", 
				new Object[] {bookLoan.getBook().getBookId(), bookLoan.getLibraryBranch().getBranchId(),
						  bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut()}, this);
		if (bookLoans != null) {
			return bookLoans.get(0);
		}
		return null;
	}

	
}
