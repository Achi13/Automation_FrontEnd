package com.automation.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.automation.campaign.domain.DependentTestcase;
import com.automation.configuration.beans.UrlBeans;
import com.automation.db.entity.DataFooter;
import com.automation.db.entity.DataRaw;
import com.automation.db.entity.Record;
import com.automation.db.repository.DataFooterRepository;
import com.automation.db.repository.DataRawRepository;
import com.automation.db.repository.RecordRepository;
import com.automation.domain.Entity;
import com.automation.domain.StoragePath;
import com.automation.template.domain.TemplateData;
import com.automation.testcase.domain.TestcaseActualData;
import com.automation.testcase.domain.TestcaseFooterData;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Controller
public class RESTController {
	
	private final String LOCATION = StoragePath.TEMPLATE_HOLDER_LOCATION;
	
	@Autowired
	RecordRepository recordRepo;
	
	@Autowired
	DataRawRepository rawRepo;
	
	@Autowired
	DataFooterRepository footerRepo;
	
	@GetMapping(value="/gettemplate/{file}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Entity>> getTemplate(@PathVariable("file")String file) throws IOException{
		Logger log = Logger.getLogger(getClass());
		log.info("Accessed");
		List<Entity> elements = new ArrayList<>();
		
		File location = new File(LOCATION);
		for (File files:location.listFiles()) {
			if(files.toString().contains(file)) {
				File csvFile = new File(files.toString());
				FileReader fileReader = new FileReader(csvFile);
				BufferedReader bufferReader = new BufferedReader(fileReader);
				String line;
				int counter=0;
				while((line = bufferReader.readLine()) != null) {
					String[] lineArr = line.split(",");
					if(counter == 0) {
						counter++;
					}else {
						Entity elementEntity = new Entity(lineArr[0], lineArr[1], lineArr[2], lineArr[3].toLowerCase(), lineArr[4], lineArr[5].toLowerCase(), lineArr[6]);
						elements.add(elementEntity);
					}
				}
				bufferReader.close();
			}
		}
		return ResponseEntity.ok(elements);
	}
	
	@GetMapping(value = "/getrecords", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DependentTestcase>>getRecords(){
		
		Logger log = Logger.getLogger(getClass());
		
		
		String url = String.format("%s/%s", UrlBeans.DEPENDENT_TESTCASE_SERVICE, "getalldependenttestcase");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		JSONArray jsonArray = jsonResponse.getJSONArray("dependentTestcaseData");
		
		List<DependentTestcase>records = new ArrayList<DependentTestcase>();
		
		for(int i=0; i<jsonArray.length(); i++) {
			
			JSONObject dependentTestcaseData = jsonArray.getJSONObject(i);
			DependentTestcase dTestcase = new DependentTestcase();
			dTestcase.setTestcaseNumber(dependentTestcaseData.getString("testcaseNumber"));
			dTestcase.setStatus(dependentTestcaseData.getString("status"));
			
			records.add(dTestcase);
			
		}
		
		return ResponseEntity.ok(records);
		
	}
	
	@GetMapping(value = "/gettestcasedata/{testcase}")
	public ResponseEntity<List<Object>>getTestcaseData(@PathVariable("testcase")String testcase){
		float failed = 0;
		float passed = 0;
		float pending = 0;
		String[] passedCollection = new String[2];
		String[] failedCollection = new String[2];
		String[] pendingCollection = new String[2];
		Logger log = Logger.getLogger(getClass());
		
		
		
		List<Object>data = new ArrayList<Object>();
		
		
		String url = String.format("%s/%s?testcaseNumber=%s", UrlBeans.DEPENDENT_TESTCASE_SERVICE, "getsolodependenttestcase", testcase);
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		JSONObject dependentTestcaseJson = jsonResponse.getJSONObject("dependentTestcase");
		DependentTestcase dependentTestcase = new DependentTestcase();
		dependentTestcase.setClientName(dependentTestcaseJson.getString("clientName"));
		dependentTestcase.setDescription(dependentTestcaseJson.getString("description"));
		dependentTestcase.setTestcaseNumber(dependentTestcaseJson.getString("testcaseNumber"));
		dependentTestcase.setSender(dependentTestcaseJson.getString("username"));
		
		JSONObject footerDataJson = jsonResponse.getJSONObject("footerData");
		TestcaseFooterData footerData = new TestcaseFooterData();
		footerData.setSender(footerDataJson.getString("sender"));
		
		
		JSONArray actualData = jsonResponse.getJSONArray("actualData");
		List<TestcaseActualData> raw_data = new ArrayList<TestcaseActualData>();
		for(int i=0; i<actualData.length(); i++) {
			
			JSONObject templateDataJson = actualData.getJSONObject(i);
			
			TestcaseActualData templateData = new TestcaseActualData();
			templateData.setTimestamp(templateDataJson.getString("timestamp"));
			templateData.setLogfield(templateDataJson.getString("logField"));
			templateData.setLabel(templateDataJson.getString("label"));
			templateData.setRemarks(templateDataJson.getString("remarks"));
			templateData.setScreenshotPath(templateDataJson.getString("screenshotPath"));
			raw_data.add(templateData);
		}
		
		for(int i=0; i<raw_data.size(); i++) {
			if(raw_data.get(i).getScreenshotPath().contains("png")) {
				TestcaseActualData rawData = raw_data.get(i);
				String scPath = rawData.getScreenshotPath();
				scPath = scPath.replaceAll("\\\\", "/");
				rawData.setScreenshotPath(scPath);
				raw_data.remove(i);
				raw_data.add(i, rawData);
			}
		}
		
		for(TestcaseActualData rawData:raw_data) {
			if(rawData.getRemarks().equalsIgnoreCase("passed")) {
				passed++;
			}else if(rawData.getRemarks().equalsIgnoreCase("failed")) {
				failed++;
			}else if(rawData.getRemarks().equalsIgnoreCase("pending")) {
				pending++;
			}
		}
		
		HashMap<String, String[]> mathVals = new HashMap<String, String[]>();
		
		// SET VALUES
		//passed
		passedCollection[0] = Integer.toString((Math.round((passed))));
		passedCollection[1] = Integer.toString(Math.round((passed/raw_data.size())*100));
		//failed
		failedCollection[0] = Integer.toString((Math.round((failed))));
		failedCollection[1] = Integer.toString(Math.round((failed/raw_data.size())*100));
		//ignored
		pendingCollection[0] = Integer.toString((Math.round((pending))));
		pendingCollection[1] = Integer.toString(Math.round((pending/raw_data.size())*100));
		
		//CREATE HASH MAP
		mathVals.put("passed", passedCollection);
		mathVals.put("failed", failedCollection);
		mathVals.put("pending", pendingCollection);
		
		//FILL LIST
		//[0]: record [1]:footer [2]:raw_data [3]:data values [4]:total size of raw data [5]: passed,failed,ignored percentage
		data.add(dependentTestcase);
		data.add(footerData);
		data.add(raw_data);
		data.add(raw_data.size()); 
		data.add(mathVals);
		
		return ResponseEntity.ok(data);
		
	}
	
	@GetMapping(value = "/showpicture")
	public ResponseEntity<List<String>>showPicture(@QueryParam("file")String file){
		List<String> encodedString = new ArrayList<String>();
		
		File img = new File(file);
		encodedString.add(img.getName().toString());
		/*
		
		Logger log = Logger.getLogger(getClass());
		log.info(file);
		String encodedFile = Base64.encodeBase64String(file.getBytes());
		log.info(encodedFile);
		encodedString.add(encodedFile);
		*/
		return ResponseEntity.ok(encodedString);
	}
	
	@GetMapping("/updateschedule/{jsondata}/{counter}")
	public ResponseEntity<String> updateSchedule(@PathVariable("jsondata")String json,
			@PathVariable("counter")String counter) throws InterruptedException {
		Thread.sleep(1500);
		int count = Integer.parseInt(counter);
		Logger log = Logger.getLogger(getClass());
		log.info(counter);
		JSONObject jsonData = new JSONObject(json);
		for(int i=0; i<count; i++) {
			JSONObject jsonRow = new JSONObject(jsonData.get(Integer.toString(i)).toString());
			
			if(jsonRow.get("date").toString().isEmpty()) {
				Record record = recordRepo.findRecordByTestcase(jsonRow.getString("testcase").toString());
				record.setExecutionSchedule(getCurrentDateAndTime());
				recordRepo.save(record);
			}else {
				String date = jsonRow.get("date").toString();
				date = date.replaceAll("-", ".").replaceAll("T", ".").replaceAll(":", ".");
				Record record = recordRepo.findRecordByTestcase(jsonRow.getString("testcase").toString());
				record.setExecutionSchedule(date);
				recordRepo.save(record);
			}
		}
		
		
		
		return ResponseEntity.status(200).build();
	}
	@GetMapping("/getserverignore")
	public ResponseEntity<String>getServerIgnore(@DefaultValue("on")@QueryParam(value = "ignoreseverity")String ignoreSeverity,
			@DefaultValue("on")@QueryParam("serverimport")String serverImport){
		//UserSession.getInstance().setValStatuses(key, value);
		
		
		return ResponseEntity.ok("success");
	}
	
	
	private String getCurrentDateAndTime() {
		
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		String tempA = dateFormat.format(date).toString();
		
		return tempA;
	}
	
}
