package com.automation.campaign;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class story implements Runnable{

	public String dbUrl =  "jdbc:mysql://localhost/automation_db"; //kulang ng port number to
	public String username = "root";
	public String password = "aaaaaa";
	/////////////////////////////////
	public Thread t;
	private String threadName;
	private String story_id;
	private String filePathBridgeFolder;
	private String filePathTestCaseHolder;
	
	public story(String threadName, String story_id,  String filePathBridgeFolder, String filePathTestCaseHolder) {
	  this.threadName = threadName;
	  this.story_id = story_id;
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
			beginStoryProcess();
			System.out.println(threadName + " exiting.");
		}catch(Exception e){e.printStackTrace();}
		  
	}
	
	public void beginStoryProcess() throws SQLException{
		
		List<String> testcase_number = new ArrayList<String>();
		PreparedStatement stmt;
		ResultSet rs;
		
		//initiate mysql driver
		loadDriver();
		//initiate driver connection
		Connection con = createConnection();
		//generate prepared statement to query db
		stmt = statementToQueryTestCaseNumber(con);
		//execute prepared statement
		rs = executeQuery(1,stmt);
		//get data from result set
		testcase_number = getDataFromResultSet(rs);
		//initiate theme thread
		initiateTestCaseThread(testcase_number);
		
		//requery db to get status and update table
		
		//generate prepared statement to re-query db for story status
		stmt = statementToQueryTestCaseNumber(con);
		//execute prepared statement
		rs = executeQuery(1,stmt);
		//get data from result set
		String status = getDataFromResultSetForCheckingStatus(rs);
		//generate prepared statement to update db
		stmt = statementToUpdateStoryIdStatus(con, status);
		//execute prepared statement
		executeQuery(2,stmt);
		
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

	public PreparedStatement statementToQueryTestCaseNumber(Connection con) {
		
		String query = "SELECT * FROM dependent_testcase_record_execution WHERE `story_execution_number` = ?";
		PreparedStatement stmt = null;
		
		try {
			stmt = con.prepareStatement(query);
			//set parameters
 			stmt.setString(1, story_id);
		} catch (SQLException e) {e.printStackTrace();}
		
		return stmt;
	}

	public PreparedStatement statementToUpdateStoryIdStatus(Connection con, String status) {
		
		String query = "UPDATE story_execution SET `status` = ? WHERE `story_execution_number` = ?";
		PreparedStatement stmt = null;
		
		try {
			stmt = con.prepareStatement(query);
			//set parameters
 			stmt.setString(1, status);
 			stmt.setString(2, story_id);
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

	public List<String> getDataFromResultSet(ResultSet rs) throws SQLException {
		
		List<String> testcase_number = new ArrayList<String>();
		
		while(rs.next()) {
			testcase_number.add(rs.getString("dependent_testcase_number"));
		}
		
		return testcase_number;
	}
	
	public String getDataFromResultSetForCheckingStatus(ResultSet rs) throws SQLException {
		
		String status = "Passed";
		
		while(rs.next()) {
			if(rs.getString("status").equalsIgnoreCase("Failed") || rs.getString("status").equalsIgnoreCase("Ignored")) {
				status = "Failed";
				return status;
			}
		}
		
		return status;
	}

	public void initiateTestCaseThread(List<String> testcase_number) {
		
		List<Thread> threadList = new ArrayList<Thread>();
		
		for(int i=0;i<testcase_number.size();i++) {
			//create name for new thread
			String testCaseThreadName = story_id+"_testcase_"+i;
			//execute thread
			testcase testcaseJr = new testcase(testCaseThreadName, testcase_number.get(i), filePathBridgeFolder, filePathTestCaseHolder);
			testcaseJr.start();
			threadList.add(testcaseJr.t);
			threadList = monitorThreadList(threadList);
		}
		
	}

	public List<Thread> monitorThreadList(List<Thread> threadList) {
		
		boolean isListEmpty = false;
		
		while(isListEmpty==false) {
			sleep(10);
			for(int i=0; i<threadList.size(); i++) {
				if(!threadList.get(i).isAlive()) {
					threadList.remove(i);
				}
			}
			
			if(threadList.size()==0) {
				isListEmpty = true;
			}
		}
		
		return threadList;
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
