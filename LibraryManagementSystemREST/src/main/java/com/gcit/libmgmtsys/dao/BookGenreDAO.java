/**
 * 
 */
package com.gcit.libmgmtsys.dao;

import java.sql.SQLException;

@SuppressWarnings({"rawtypes"})
public class BookGenreDAO extends BaseDAO{
	
	public void addBookGenre(Integer bookId, Integer genreId) throws SQLException {
		template.update("INSERT INTO tbl_book_genres VALUES(?, ?)",
				new Object[] {genreId, bookId});
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
