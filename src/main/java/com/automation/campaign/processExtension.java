package com.automation.campaign;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class processExtension {
	
	//DATABASE CREDENTIALS
	public String dbUrl =  "jdbc:mysql://localhost/automation_db"; //kulang ng port number to
	public String username = "root";
	public String password = "aaaaaa";
	
	//global variables
	String[] newTestCaseNumberTracker;
	
	public void processController(String[] testCaseNumber, String storyId[], String csvTemplateHolderFilePath, String csvHolderFilePath, String userId, String clientName, String[] description, String executionSchedule, String[] ignoreSeverity, String[] serverImport  ) throws SQLException {
		
		//VARIABLES
		
		//instantiate newTestCaseNumberTracker using testCaseNumber size
		newTestCaseNumberTracker = new String[testCaseNumber.length];
		//storage of datafiles
		List<List<String[]>> listOfDataFiles = new ArrayList<List<String[]>>();
		
		//PROCESS
		
		//retrieve template
		listOfDataFiles = retrieveTemplate(testCaseNumber, csvTemplateHolderFilePath);
		//modify csv file testCase number
		listOfDataFiles = modifyTestCaseNumberAndOtherData(listOfDataFiles , ignoreSeverity, serverImport);
		//update testCaseNumber in table
		loadDriver();
		Connection con = createConnection();
		List<PreparedStatement> updateStatements = statementToUpdateTestCaseNumber(con, storyId, testCaseNumber);
		executeMultipleRelatedQuery(2, updateStatements);
		//transfer modifiedDataFile into csvHolder folder
		writeCsvIntoNewFolder(listOfDataFiles, csvHolderFilePath);
		//insert testCaseNumber in testcase_record
		List<PreparedStatement> insertStatements = statementToInsertTestCaseNumber(con, userId, clientName, description, executionSchedule);
		executeMultipleRelatedQuery(2, insertStatements);
		
		con.close();
		
	}
	
	public List<List<String[]>>  retrieveTemplate(String[] testCaseNumber, String csvTemplateHolderFilePath) {
		
		List<List<String[]>> listOfDataFiles = new ArrayList<List<String[]>>();
		
		for(int i=0; i<testCaseNumber.length;i++) {
			//retrieve csv file from folder
			String fullfilePath = csvTemplateHolderFilePath + "\\" + testCaseNumber[i] + ".csv";
			List<String[]> dataFile = readFile(fullfilePath);
			listOfDataFiles.add(dataFile);
		}
		
		return listOfDataFiles;
	}
	
	public List<String[]> readFile(String fullFilePath) {
		
		//reads files from current CSV file and returns List<String[]> containing data
		List<String[]> dataFile = new ArrayList<>();
		String[] tempA;
		
		try {
			FileReader fileReader = new FileReader(fullFilePath);
			CSVReader csvReader = new CSVReader(fileReader); 
			while((tempA = csvReader.readNext())!=null) {
				dataFile.add(tempA);
			}
			csvReader.close();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return dataFile;
	}

	public List<List<String[]>> modifyTestCaseNumberAndOtherData(List<List<String[]>> listOfDataFiles, String[] ignoreSeverity, String[] serverImport){
		
		List<List<String[]>> modifiedListOfDataFiles = new ArrayList<List<String[]>>();
		
		for(int x=0; x<listOfDataFiles.size();x++) {
			for(int i = 0; i<listOfDataFiles.get(x).size();i++) {
				if(listOfDataFiles.get(x).get(i)[0].equalsIgnoreCase("TestCaseNumber")) {
					//generate testCaseNumber
					String newTestCaseNumber = testCaseNumber();
					//store testCaseNumber in tracker
					newTestCaseNumberTracker[x] = newTestCaseNumber;
					//modify testCaseNumber in currentdataFile
					listOfDataFiles.get(x).get(i)[1] = newTestCaseNumber;
					//add modified dataFile into list
					modifiedListOfDataFiles.add(listOfDataFiles.get(x));
					System.out.println(newTestCaseNumber);
				}
				else if(listOfDataFiles.get(x).get(i)[0].equalsIgnoreCase("IgnoreSeverity")) {
					//modify ignoreSeverity in currentdataFile
					listOfDataFiles.get(x).get(i)[1] = ignoreSeverity[x];
					System.out.println(ignoreSeverity[x]);
				}
				else if(listOfDataFiles.get(x).get(i)[0].equalsIgnoreCase("ServerImport")) {
					//modify ignoreSeverity in currentdataFile
					listOfDataFiles.get(x).get(i)[1] = serverImport[x];
					System.out.println(serverImport[x]);
				}
			}
		
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return modifiedListOfDataFiles;
	}
	
	public String testCaseNumber(){
		
		Date date = new Date();
		String dateFileName;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		String tempA = dateFormat.format(date).toString();
		dateFileName = tempA;
		return dateFileName;
		
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

	public List<PreparedStatement> statementToUpdateTestCaseNumber(Connection con, String[] storyId, String[] testCaseNumber) {
		
		List<PreparedStatement> updateStatements = new ArrayList<PreparedStatement>();
		
		for(int i=0; i<newTestCaseNumberTracker.length;i++) {
			
			String query = "UPDATE `dependent_testcase_record_execution` SET `dependent_testcase_number` = ? WHERE `story_execution_number` = ? AND `dependent_testcase_number` = ?";
			PreparedStatement stmt = null;
			
			try {
				stmt = con.prepareStatement(query);
				//set parameters
	 			stmt.setString(1, newTestCaseNumberTracker[i]);
	 			stmt.setString(2, storyId[i]);
	 			stmt.setString(3, testCaseNumber[i]);
	 			
	 			updateStatements.add(stmt);
	 			
			} catch (SQLException e) {e.printStackTrace();}
			
		}
		
		return updateStatements;
	}

	public List<PreparedStatement> statementToInsertTestCaseNumber(Connection con, String userId, String clientName, String[] description, String executionSchedule ){
		
		List<PreparedStatement> insertStatements = new ArrayList<PreparedStatement>();
		
		for(int i=0; i<newTestCaseNumberTracker.length;i++) {
			
			String query = "INSERT INTO testcase_record(`testcase_number`, `user_id`, `client_name`, `status`, `description`, `execution_schedule`) VALUES (?,?,?,?,?,?)";
			PreparedStatement stmt = null;
			
			try {
				stmt = con.prepareStatement(query);
				//set parameters
	 			stmt.setString(1, newTestCaseNumberTracker[i]);
	 			stmt.setString(2, userId);
	 			stmt.setString(3, clientName);
	 			stmt.setString(4, "Pending");
	 			stmt.setString(5, description[i]);
	 			stmt.setString(6, executionSchedule);
			} catch (SQLException e) {e.printStackTrace();}
			
			insertStatements.add(stmt);
			
		}
		
		return insertStatements;
	}
	
	public List<ResultSet> executeMultipleRelatedQuery(int service, List<PreparedStatement> statements) {
		
		List<ResultSet> rsOutputs = new ArrayList<ResultSet>();
		
		for(int i=0; i<statements.size();i++) {
			rsOutputs.add(executeQuery(service, statements.get(i)));
		}
		
		return rsOutputs;
	}
	
	public ResultSet executeQuery(int service, PreparedStatement stmt) {
		
		ResultSet rs = null;
		
		try {
			
			if(service==1) {
				rs = stmt.executeQuery();
			}else if(service==2) {
				stmt.executeUpdate();
			}
			
		}catch(Exception e) {e.printStackTrace();}
		
		return rs;

}
	
	public void writeCsvIntoNewFolder(List<List<String[]>> listOfDataFiles, String csvHolderFilePath) {
		
		for(int i=0; i<listOfDataFiles.size();i++) {
		
			List<String[]> tempList = listOfDataFiles.get(i);
			    
		    File newFile = new File(csvHolderFilePath+"\\"+newTestCaseNumberTracker[i] + ".csv");
		  
		    try { 
		        FileWriter outputfile = new FileWriter(newFile); 
		        CSVWriter writer = new CSVWriter(outputfile); 
		        writer.writeAll(tempList); 
		        writer.close(); 
		    } 
		    catch (IOException e) { 
		        e.printStackTrace(); 
		    }
			    
		}
		
	}

}