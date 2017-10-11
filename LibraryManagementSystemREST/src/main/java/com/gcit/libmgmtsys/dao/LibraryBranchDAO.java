package com.gcit.libmgmtsys.dao;

import java.sql.*;
import java.util.*;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.libmgmtsys.entity.Author;
import com.gcit.libmgmtsys.entity.Borrower;
import com.gcit.libmgmtsys.entity.LibraryBranch;
import com.gcit.libmgmtsys.entity.Publisher;

@SuppressWarnings("rawtypes")
public class LibraryBranchDAO extends BaseDAO implements ResultSetExtractor<List<LibraryBranch>>{
	
	//insert a new library branch
	public void addLibraryBranch(LibraryBranch libraryBranch) throws SQLException {
		template.update("INSERT INTO tbl_library_branch (branchName, branchAddress) VALUES(?, ?)",
				new Object[] {libraryBranch.getBranchName(), libraryBranch.getBranchAddress()});
	}
	
	//insert a new library branch and return generated ID
	public Integer addLibraryBranchWithID(LibraryBranch libraryBranch) throws SQLException{
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_library_branch (branchName, branchAddress) VALUES(?, ?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, libraryBranch.getBranchName());
				ps.setString(2, libraryBranch.getBranchAddress());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}
	
//	public Integer addLibraryBranchWithID(LibraryBranch libraryBranch) throws SQLException {
//		return executeUpdateWithID("INSERT INTO tbl_library_branch (branchName, branchAddress) VALUES(?, ?)",
//				new Object[] {libraryBranch.getBranchName(), libraryBranch.getBranchAddress()});
//	}
	
	
	
	//update the name of a library branch
	public void updateLibraryBranch(LibraryBranch libraryBranch) throws SQLException {
		template.update("UPDATE tbl_library_branch SET branchName = ?, branchAddress = ? WHERE branchId = ?",
				new Object[] {libraryBranch.getBranchName(), libraryBranch.getBranchAddress(), libraryBranch.getBranchId()});
	}
	
	//delete an author from author table
	public void deleteLibraryBranch(LibraryBranch libraryBranch) throws SQLException {
		template.update("DELETE FROM tbl_library_branch WHERE branchId = ?",
				new Object[] {libraryBranch.getBranchId()});
	}
	
	//execute query need to be tested
	public List<LibraryBranch> readLibraryBranches(String branchName, Integer pageNo) throws SQLException {
		setPageNo(pageNo);
		if (branchName != null && !branchName.isEmpty()) {
			branchName = "%" + branchName + "%";
			return template.query("SELECT * FROM tbl_library_branch WHERE branchName LIKE ?",
					new Object[] {branchName}, this);
		} else {
			return template.query("SELECT * FROM tbl_library_branch", this);
		}
	}
	
	//Retrieve library info given library id
	public LibraryBranch readOneBranch(Integer branchId) throws SQLException {
		List<LibraryBranch> lb = template.query("SELECT * FROM tbl_library_branch WHERE branchId = ?",
				new Object[] {branchId}, this);
		if (lb != null && !lb.isEmpty()) {
			return lb.get(0);
		}
		return null;
	}

//	@Override
//	protected List<LibraryBranch> parseFirstLevelData(ResultSet rs) throws SQLException {
//		List<LibraryBranch> libraryBranches = new ArrayList<>();
//		while (rs.next()) {
//			LibraryBranch libraryBranch = new LibraryBranch();
//			libraryBranch.setBranchId(rs.getInt("branchId"));
//			libraryBranch.setBranchName(rs.getString("branchName"));
//			libraryBranch.setBranchAddress(rs.getString("branchAddress"));
//			libraryBranches.add(libraryBranch);
//		}
//		return libraryBranches;
//	}
	
	//null pointer exception, Need to be tested
	@Override
	public List<LibraryBranch> extractData(ResultSet rs) throws SQLException {
//		String sql_loans  = "SELECT * FROM tbl_book_loans WHERE branchId = ? AND dateIn IS NULL";
//		String sql_copies = "SELECT * FROM tbl_book_copies WHERE branchId = ?";
		List<LibraryBranch> libraryBranches = new ArrayList<>();
//		BookLoansDAO 		bookLoans  		= new BookLoansDAO(conn);
//		BookCopiesDAO   	bookCopies 		= new BookCopiesDAO(conn);
		while (rs.next()) {
			LibraryBranch libraryBranch = new LibraryBranch();
			libraryBranch.setBranchId(rs.getInt("branchId"));
			libraryBranch.setBranchName(rs.getString("branchName"));
			libraryBranch.setBranchAddress(rs.getString("branchAddress"));
//			libraryBranch.setBookLoans(bookLoans.executeFirstLevelQuery(sql_loans, new Object[] {libraryBranch.getBranchId()}));
//			libraryBranch.setBookCopies(bookCopies.executeFirstLevelQuery(sql_copies, new Object[] {libraryBranch.getBranchId()}));
			libraryBranches.add(libraryBranch);
		}
		return libraryBranches;
	}

	public LibraryBranch readOneLibrayBranchFirstLevel(Integer branchId) throws SQLException {
		List<LibraryBranch> libraryBranches = template.query("SELECT * FROM tbl_library_branch WHERE branchId = ?", 
				new Object[] {branchId}, this);
		if (libraryBranches != null) {
			return libraryBranches.get(0);
		}
		return null;
	}

	public Integer getLibraryBranchesCount() throws SQLException {
		return template.queryForObject("SELECT COUNT(*) as COUNT FROM tbl_library_branch", Integer.class);
	}

	public List<LibraryBranch> checkBranchByName(String branchName) throws SQLException {
		List<LibraryBranch> libraryBranches = template.query("SELECT * FROM tbl_library_branch WHERE branchName = ?", 
				new Object[] {branchName}, this);
		if (libraryBranches.size() > 0) {
			return libraryBranches;
		}
		return null;
	}
}
