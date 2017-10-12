package com.gcit.lms;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.gcit.libmgmtsys.dao.*;
import com.gcit.libmgmtsys.service.AdminService;
import com.gcit.libmgmtsys.service.BorrowerService;
import com.gcit.libmgmtsys.service.LibrarianService;

@Configuration
public class LMSConfig {
	private final String DRIVER   = "com.mysql.cj.jdbc.Driver";
	private final String URL      = "jdbc:mysql://localhost/library?useSSL = false";
	private final String USER     = "root";
	private final String PASSWORD = "root";
	
	@Bean
	public BasicDataSource dataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(DRIVER);
		ds.setUrl(URL);
		ds.setUsername(USER);
		ds.setPassword(PASSWORD);
		
		return ds;
	}
	
	@Bean
	public JdbcTemplate template() {
		return new JdbcTemplate(dataSource());
	}
	
	@Bean
	public AuthorDAO authorDao() {
		return new AuthorDAO();
	}
	
	@Bean
	public BookCopiesDAO bookCopiesDao() {
		return new BookCopiesDAO();
	}
	
	@Bean
	public BookDAO bookDao() {
		return new BookDAO();
	}
	
	@Bean
	public BookLoansDAO bookLoansDao() {
		return new BookLoansDAO();
	}
	
	@Bean
	public BorrowerDAO borrowerDao() {
		return new BorrowerDAO();
	}
	
	@Bean
	public GenreDAO genreDao() {
		return new GenreDAO();
	}
	
	@Bean
	public LibraryBranchDAO libraryBranchDao() {
		return new LibraryBranchDAO();
	}
	
	@Bean
	public PublisherDAO publisherDao() {
		return new PublisherDAO();
	}
	
	@Bean
	public AdminService adminService() {
		return new AdminService();
	}
	
	@Bean
	public LibrarianService librarianService() {
		return new LibrarianService();
	}
	
	@Bean
	public BorrowerService borrowerService() {
		return new BorrowerService();
	}
	
	@Bean
	public BookAuthorDAO bookAuthorDao() {
		return new BookAuthorDAO();
	}
	
	@Bean
	public BookGenreDAO bookGenreDao() {
		return new BookGenreDAO();
	}
	
	@Bean
	public PlatformTransactionManager txManager() {
		return new DataSourceTransactionManager(dataSource());
	}
}
