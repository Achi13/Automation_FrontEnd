package com.automation.db.archive;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.automation.db.entity.DataFooter;
import com.automation.db.entity.DataRaw;
import com.automation.db.entity.Record;

public class dbConnect {
	
	public String dbUrl =  "jdbc:mysql://localhost/archive_db"; //kulang ng port number to
	public String username = "root";
	public String password = "aaaaaa";
	
	//query's parameters
	private List<DataRaw>raw_data;
	private DataFooter footer_data;
	private Record testcase_record;
	
	public void dataBaseController(List<DataRaw>raw_data, DataFooter footer_data, Record testcase_record){
		//set up values
		setQueryParameterValues(raw_data, footer_data, testcase_record);
		//set up database
		loadDriver();
		Connection con = createConnection();
		//generate a statement depending on service type
		controller(con);
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void controller(Connection con){
		//insert raw_data
		List<PreparedStatement>rawData = statementToInsertRawData(con);
		executeQuery(rawData);
		//insert footer_data
		List<PreparedStatement>footerData = statementToInsertFooterData(con);
		executeQuery(footerData);
		//insert testcase_record
		List<PreparedStatement>testcaseRecord = statementToInsertTestcaseRecord(con);
		executeQuery(testcaseRecord);
		
	}
	
	public List<PreparedStatement> statementToInsertRawData(Connection con) {
		
		List<PreparedStatement>preparedStatement = new ArrayList<PreparedStatement>();
		
		for(DataRaw rawData: raw_data) {
			String query = "INSERT INTO raw_data (testcase_number, WebElementName, WebElementNature, NatureOfAction, ScreenCapture, TriggerEnter, InputOutputValue, Label, TimeStamp, ScPath, Remarks, LogField) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
			
			PreparedStatement stmt = null;
			
			try {
				stmt = con.prepareStatement(query);
				
				//set parameters
	 			stmt.setString(1, rawData.getTestcaseNumber().getTestcase());
	 			stmt.setString(2, rawData.getWebELementName());
	 			stmt.setString(3, rawData.getWebElementNature());
	 			stmt.setString(4, rawData.getNatureOfAction());
	 			stmt.setString(5, rawData.getScreenCapture());
	 			stmt.setString(6, rawData.getTriggerEnter());
	 			stmt.setString(7, rawData.getInputOutputValue());
	 			stmt.setString(8, rawData.getLabel());
	 			stmt.setString(9, rawData.getTimeStamp());
	 			stmt.setString(10, rawData.getScPath());
	 			stmt.setString(11, rawData.getRemarks());
	 			stmt.setString(12, rawData.getLogField());
	 			
	 			
	 			//Add stmt to list
	 			preparedStatement.add(stmt);
	 			
	 			
			} catch (SQLException e) {/*nothing to do here*/e.printStackTrace();}
		
		}
		return preparedStatement;
	}
	
	public List<PreparedStatement> statementToInsertFooterData(Connection con) {
		
		List<PreparedStatement>preparedStatement = new ArrayList<PreparedStatement>();
		
		String query = "INSERT INTO footer_data (testcase_number, ClientName, IgnoreSeverity, Sender, ServerImport, TestCaseStatus, TransactionType, Website, AssignedAccount) "
				+ "VALUES (?,?,?,?,?,?,?,?,?)";
		
		PreparedStatement stmt = null;
		
		try {
			stmt = con.prepareStatement(query);
			
			//set parameters
 			stmt.setString(1, footer_data.getTestcaseNumber().getTestcase());
 			stmt.setString(2, footer_data.getClientName());
 			stmt.setString(3, footer_data.getIgnoreSeverity());
 			stmt.setString(4, footer_data.getSender());
 			stmt.setString(5, footer_data.getServerImport());
 			stmt.setString(6, footer_data.getTestcaseStatus());
 			stmt.setString(7, footer_data.getTransactionType());
 			stmt.setString(8, footer_data.getWebsite());
 			stmt.setString(9, footer_data.getAssignedAccount());
 			
 			preparedStatement.add(stmt);
 			
		} catch (SQLException e) {/*nothing to do here*/e.printStackTrace();}
		
		return preparedStatement;
	}
	
	public List<PreparedStatement> statementToInsertTestcaseRecord(Connection con) {
		
		List<PreparedStatement>preparedStatement = new ArrayList<PreparedStatement>();
		
		String query = "INSERT INTO testcase_record (testcase_number, client_name, status, description, user_id) "
				+ "VALUES (?,?,?,?,?)";
		
		PreparedStatement stmt = null;
		
		try {
			stmt = con.prepareStatement(query);
			
			//set parameters
 			stmt.setString(1, testcase_record.getTestcase());
 			stmt.setString(2, testcase_record.getClientName());
 			stmt.setString(3, testcase_record.getStatus());
 			stmt.setString(4, testcase_record.getDescription());
 			stmt.setInt(5, testcase_record.getUser().getUserId());
 			
 			preparedStatement.add(stmt);  
 			
		} catch (SQLException e) {/*nothing to do here*/e.printStackTrace();}
		
		return preparedStatement;
	}
	
	
	
	
	
	public void setQueryParameterValues(List<DataRaw>raw_data, DataFooter footer_data, Record testcase_record) {
		
		this.raw_data = raw_data;
		this.footer_data = footer_data;
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
