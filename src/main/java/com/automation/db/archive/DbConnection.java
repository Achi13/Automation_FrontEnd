package com.automation.db.archive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.automation.db.entity.Record;

public class DbConnection {
	
	public String dbUrl =  "jdbc:mysql://localhost/automation_db";
	public String username = "root";
	public String password = "aaaaaa";
	
	private Record testcase_record;
	
	public void dataBaseController(Record testcase_record){
		//set up values
		setQueryParameterValues(testcase_record);
		//set up database
		loadDriver();
		executeQuery(statementToDeleteTestcaseRecord(createConnection()));
		
	}
	
	public void setQueryParameterValues(Record testcase_record) {
		
		this.testcase_record = testcase_record;

	}
	public void loadDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");	
		}catch(Exception e) {/*nothing to do here*/e.printStackTrace();}
	}
	
	public Connection createConnection() {
		//Create Connection to DB	
		Connection con = null;
		
    	try {
			con = DriverManager.getConnection(dbUrl,username,password);
		} catch (SQLException e) {/*nothing to do here*/e.printStackTrace();}
    	
    	return con;
	}

	public List<PreparedStatement> statementToDeleteTestcaseRecord(Connection con) {
		
		List<PreparedStatement>preparedStatement = new ArrayList<PreparedStatement>();
		
		String query = "DELETE FROM testcase_record WHERE testcase_number = ?";
		
		PreparedStatement stmt = null;
		
		try {
			stmt = con.prepareStatement(query);
			
			//set parameters
 			stmt.setString(1, testcase_record.getTestcase());
 			
 			preparedStatement.add(stmt);  
 			
		} catch (SQLException e) {/*nothing to do here*/e.printStackTrace();}
		
		return preparedStatement;
	}
	public void executeQuery(List<PreparedStatement>data) {
		
 		try {
 			
 			for(PreparedStatement stmt: data) {
 				stmt.executeUpdate();
 				stmt.close();
 			}
			//System.out.println(testCaseNumber+"'s record updated successfully");
			} catch (SQLException e) {/*nothing to do here*/e.printStackTrace();}
 			
	}
	

}
