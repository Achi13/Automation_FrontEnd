package com.automation.campaign;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class testcase implements Runnable {

	public String dbUrl =  "jdbc:mysql://localhost/automation_db"; 
	public String username = "root";
	public String password = "aaaaaa";
	/////////////////////////////////
	public Thread t;
	private String threadName;
	private String testcase_number;
	private String filePathBridgeFolder;
	private String filePathTestCaseHolder;
	
	public testcase(String threadName, String testcase_number,  String filePathBridgeFolder, String filePathTestCaseHolder) {
	  this.threadName = threadName;
	  this.testcase_number = testcase_number;
	  this.filePathBridgeFolder = filePathBridgeFolder;
	  this.filePathTestCaseHolder = filePathTestCaseHolder;
	}
	   
	public void start () {
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
	}
	
	public void run() {
		try {
			System.out.println(threadName + " is processing..." );
			beginTestCaseProcess();
			System.out.println(threadName + " exiting.");
		}catch(Exception e){e.printStackTrace();}
		  
	}
	
	public void beginTestCaseProcess() throws SQLException{
		
		PreparedStatement stmt;
		
		//initiate mysql driver
		loadDriver();
		//initiate driver connection
		Connection con = createConnection();
		//transfer testcase to todo and delete from csvHolder
		transferTestCaseNumberFile();
		//generate statement for querying testcase_record
		stmt = statementToQueryTestCaseNumberStatus(con);
		//repeatedly monitor testcase_number status in db
		String status = monitorTestCaseNumberStatus(stmt);
		//generate statement to update testCaseNumber Status
		stmt = statementToUpdateTestCaseNumberStatus(con, status);
		//execute statement
		executeQuery(2, stmt);
		
		con.close();
		stmt.close();
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
	
	public void transferTestCaseNumberFile() {
		
		//String for fullFilePath
		String oldFullFilePath = filePathTestCaseHolder+"//"+testcase_number+".csv";
		String newFullFilePath = filePathBridgeFolder+"//"+testcase_number+".csv";
		
		//transfile file from csvHolder folder to toDo folder
		File oldFile = new File(oldFullFilePath); 
		File newFile = new File(newFullFilePath); 
        
        // renaming the file and moving it to a new location 
        if(oldFile.renameTo(newFile)) 
        { 
            //if file copied successfully then delete the original file 
            oldFile.delete(); 
            System.out.println("File moved successfully"); 
        } 
        else
        { 
            System.out.println("Failed to move the file"); 
        } 
  
	}

	public PreparedStatement statementToQueryTestCaseNumberStatus(Connection con) {
		
		String query = "SELECT * FROM testcase_record WHERE `testcase_number` = ?";
		PreparedStatement stmt = null;
		
		try {
			stmt = con.prepareStatement(query);
			//set parameters
 			stmt.setString(1, testcase_number);
		} catch (SQLException e) {e.printStackTrace();}
		
		return stmt;
	}

	public PreparedStatement statementToUpdateTestCaseNumberStatus(Connection con, String status) {
		
		String query = "UPDATE dependent_testcase_record_execution SET `status` = ? WHERE `dependent_testcase_number` = ?";
		PreparedStatement stmt = null;
		
		try {
			stmt = con.prepareStatement(query);
			//set parameters
 			stmt.setString(1, status);
 			stmt.setString(2, testcase_number);
		} catch (SQLException e) {e.printStackTrace();}
		
		return stmt;
	}
	
	public String monitorTestCaseNumberStatus(PreparedStatement stmt) {
		
		boolean isStatusUpdated = false;
		String updatedStatus = "";
		ResultSet rs;
		int ctr = 0;
		
		try {
			
			while(isStatusUpdated==false) {
				
				sleep(10);
				System.out.println("monitoring "+ctr + ": " + threadName);
				//get resulting set from executing statement
				rs = executeQuery(1,stmt);
				
				//check status from output set
				while(rs.next()) {
					
					String status = rs.getString("status");
					
					if(!status.equalsIgnoreCase("Pending") && !status.equalsIgnoreCase("Executing")) {
						updatedStatus = status;
						isStatusUpdated = true;
					}	
				}
			}
	
		}catch(Exception e) {e.printStackTrace();}
		
		return updatedStatus;
	}
	
	public void sleep(int seconds) {
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}
