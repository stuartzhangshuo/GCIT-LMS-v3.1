/**
 * @Author Shuo Zhang <shuo_zhang@gcitsolutions.com>
 * @Date Sep 27, 2017
 */
package com.gcit.libmgmtsys.dao;

import java.sql.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class BaseDAO<T> {
	@Autowired
	JdbcTemplate template;
	
	protected static Connection conn = null;
	
	private Integer pageNo;
	private Integer pageSize = 10;
	
//	//Initialize DAO with a given connection object from service layer
//	public BaseDAO(Connection conn) {
//		BaseDAO.conn = conn;
//	}
//	
//	//Function for prepare & executing DML and DDL SQL statements
//	protected void executeUpdate(String sql, Object[] vals) throws SQLException{
//		PreparedStatement pstmt = conn.prepareStatement(sql);
//		if (vals != null && vals.length > 0) {
//			int pos = 1;
//			for (Object val : vals) {
//				pstmt.setObject(pos++, val);
//			}
//		}
//		pstmt.executeUpdate();
//	}
//	
//	//Function for prepare & executing DML and DDL SQL statements AND 
//	//@RETURN Generated Keys
//	protected Integer executeUpdateWithID(String sql, Object[] vals) throws SQLException{
//		PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//		if (vals != null && vals.length > 0) {
//			int pos = 1;
//			for (Object val : vals) {
//				pstmt.setObject(pos++, val);
//			}
//		}
//		pstmt.executeUpdate();
//		ResultSet rs = pstmt.getGeneratedKeys();
//		if (rs.next()) {
//			return rs.getInt(1);
//		}
//		return null;
//	}
//	
//	//Given an SQL statement, prepare the SQL with given parameters and
//	//@Renturn a list of target objects with only FIRST LEVEL properties filled
//	//CALL by another instance
//	protected List<T> executeFirstLevelQuery(String sql, Object[] vals) throws SQLException {
//		PreparedStatement pstmt =  conn.prepareStatement(sql);
//		if (vals != null && vals.length > 0) {
//			int pos = 1;
//			for (Object object : vals) {
//				pstmt.setObject(pos++, object);
//			}
//		}
//		ResultSet rs = pstmt.executeQuery();
//		return parseFirstLevelData(rs);
//	}
//	
//	//Definition for parsing a result set for an entity's FIRST LEVEL properties
//	protected abstract List<T> parseFirstLevelData(ResultSet rs) throws SQLException;
//	
//	//Given an SQL statement, prepare the SQL with given parameters and
//	//@Renturn a list of target objects with all properties filled
//	protected List<T> executeQuery(String sql, Object[] vals) throws SQLException {
//		if (getPageNo() != null) {
//			Integer index = (getPageNo() - 1) * getPageSize();
//			sql += " LIMIT " + index + ", " + getPageSize();
//		}
//		PreparedStatement pstmt =  conn.prepareStatement(sql);
//		if (vals != null && vals.length > 0) {
//			int pos = 1;
//			for (Object object : vals) {
//				pstmt.setObject(pos++, object);
//			}
//		}
//		ResultSet rs = pstmt.executeQuery();
//		return parseData(rs);
//	}
//	
//	protected Integer getCount(String sql, Object[] vals) throws SQLException {
//		PreparedStatement pstmt =  conn.prepareStatement(sql);
//		if (vals != null && vals.length > 0) {
//			int pos = 1;
//			for (Object object : vals) {
//				pstmt.setObject(pos++, object);
//			}
//		}
//		ResultSet rs = pstmt.executeQuery();
//		while (rs.next()) {
//			return rs.getInt("COUNT");
//		}
//		return null;
//	}
//	
//	//Definition for parsing a result set for an entity's ALL properties
//	protected abstract List<T> parseData(ResultSet rs) throws SQLException;
	
	
	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
