/**
 * 
 */
package com.gcit.libmgmtsys.dao;

import java.sql.SQLException;

@SuppressWarnings("rawtypes")
public class BookAuthorDAO extends BaseDAO{
	
	//associate one book and one author and insert into database
	public void addBookAuthor(Integer bookId, Integer authorId) throws SQLException {
		template.update("INSERT INTO tbl_book_authors VALUES(?, ?)",
				new Object[] {bookId, authorId});
	}

//	@Override
//	protected List parseFirstLevelData(ResultSet rs) throws SQLException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	protected List parseData(ResultSet rs) throws SQLException {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
