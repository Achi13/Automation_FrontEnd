package com.automation.thread;

import java.io.File;
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

public class scheduleThread implements Runnable {

	public String dbUrl =  "jdbc:mysql://localhost/automation_db"; //kulang ng port number to
	public String username = "root";
	public String password = "aaaaaa";
	/////////////////////////////////
	public Thread t;
	private String threadName;
	String filePathBridgeFile;
	String filePathToDoFile;
	
	public scheduleThread(String threadName, String filePathBridgeFile, String filePathToDoFile) {
	  this.threadName = threadName;
	  this.filePathBridgeFile = filePathBridgeFile;
	  this.filePathToDoFile = filePathToDoFile;
	}
	   
	public void start () {
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
	}
	
	public void run() {
		try {
			System.out.println(threadName + " is now accepting requests" );
			beginLookerProcess();
			System.out.println(threadName + " has stopped accepting requests.");
		}catch(Exception e){e.printStackTrace();}
		  
	}
	
	public void beginLookerProcess() throws SQLException {
		
		boolean cont = true;
		//load driver
		loadDriver();
		//create connection
		Connection con = createConnection();
		
		while(cont == true) {

			//generate query statement
			PreparedStatement stmt = statementToQueryTestCaseNumber(con, "Pending");
			//query database
			ResultSet rs = executeQuery(1,stmt);
			
			//process acquired data
			List<String> executedTestCaseNumber = processData(rs);
			//update testCaseStatus in List
			updateStatus(con, executedTestCaseNumber, "Executing");
			
			try {
				stmt.close();
			}catch(Exception e) {e.printStackTrace();}
			
			sleep(5);
		}
		
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
	
	public PreparedStatement statementToQueryTestCaseNumber(Connection con, String condition) {
		
		//String query = "UPDATE ? SET ? = ? WHERE ? = ?";
		String query = "SELECT * FROM testcase_record WHERE status = ?";
		PreparedStatement stmt = null;
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, condition);
		} catch (SQLException e) {e.printStackTrace();}
		
		return stmt;
	}
	
	public PreparedStatement statementToUpdateTestCaseNumberStatus(Connection con, String status, String testCaseNumber) {

		String query = "UPDATE testcase_record SET status = ? WHERE testcase_number = ?";
		PreparedStatement stmt = null;
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, status);
			stmt.setString(2, testCaseNumber);
		} catch (SQLException e) {e.printStackTrace();}
		
		return stmt;
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

	public List<String> processData(ResultSet rs) throws SQLException {
		
		List<String> executedTestCaseNumber = new ArrayList<String>();	
			
			while(rs.next()) {
				
				try {
					//convert current time and execution schedule into integers
					System.out.println(rs.getString("user_id"));
					long testSchedule = Long.parseLong(rs.getString("execution_schedule").replace(".", ""));
					System.out.println("testSchedule "+ testSchedule);
					long currentDateTime = Long.parseLong(getCurrentDateAndTime().replace(".", ""));
					System.out.println("currentDateTime "+ currentDateTime);
					//check if execution schedule is greater the current time
					boolean isExecute = determineIfExecute(testSchedule, currentDateTime);
					//transfer corresponding csv file if isExecute is TRUE
					String testCaseNumber = rs.getString("testcase_number");
					boolean isTransferred = transferToToDoFile(isExecute, testCaseNumber );
					//add to list if isExecute is true
					if(isTransferred) {
						executedTestCaseNumber.add(testCaseNumber);
					}
				}catch(Exception e) {e.printStackTrace();}
				
	
			}
				

		
		return executedTestCaseNumber;
	}

	public String getCurrentDateAndTime() {
		
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		String tempA = dateFormat.format(date).toString();
		
		return tempA;
	}
	
	public boolean determineIfExecute(long testSchedule, long currentDateTime) {
		
		boolean tempA;
		
		if(testSchedule<=currentDateTime) {
			tempA = true;
		}else {
			tempA = false;
		}
		
		return tempA;
	}

	public boolean transferToToDoFile(boolean isExecute, String testCaseNumber ) {
		 
		boolean tempA = false;
		 
		if(isExecute) {
			//String for fullFilePath
			String oldFullFilePath = filePathBridgeFile+"//"+testCaseNumber+".csv";
			String newFullFilePath = filePathToDoFile+"//"+testCaseNumber+".csv";
			
			//transfile file from csvHolder folder to toDo folder
			File oldFile = new File(oldFullFilePath); 
			File newFile = new File(newFullFilePath); 
	        
	        // renaming the file and moving it to a new location 
	        if(oldFile.renameTo(newFile)) 
	        { 
	            //if file copied successfully then delete the original file 
	        	tempA = true;
	            oldFile.delete(); 
	            System.out.println("File moved successfully"); 
	        } 
	        else
	        { 
	            System.out.println("Failed to move the file"); 
	        }
	        
		}
		
		return tempA;
	}

	public void updateStatus(Connection con, List<String> executedTestCaseNumber, String status) {
		
		for(int i=0; i<executedTestCaseNumber.size();i++) {
			String testCaseNumber = executedTestCaseNumber.get(i);
			PreparedStatement stmt = statementToUpdateTestCaseNumberStatus(con, status, testCaseNumber);
			executeQuery(2, stmt);
			System.out.println("Looker has transferred and updated " + testCaseNumber);
		}
	}
	
	public void sleep(int seconds) {
		
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
