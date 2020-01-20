package com.automation;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.automation.db.entity.Record;
import com.automation.db.entity.User;
import com.automation.db.repository.RecordRepository;
import com.automation.db.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QaAutomationApplicationTests {
	
	private Logger log;
	private final String SERVER_TIMEOUT = "5000";
	private final String LOCATION = "C:\\Users\\rtsbondoc\\Desktop\\Template";
	private final String USER_FOLDER_LOCATION = "C:\\Users\\rtsbondoc\\Desktop\\";
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RecordRepository recordRepo; 
	
	User user;
	Record record;
	
	@Test
	public void contextLoads() {
	}
	
	
	@Test
	public void insertIntoUser() {
		/*
		Logger log = Logger.getLogger(getClass());
		List<Record>record = new ArrayList<>();
		record = recordRepo.findAll();
		
		
		for(Record records : record) {
			if(records.getUser().getUserId() == userRepo.findUserByUsername("Wonwon").getUserId()) {
				log.info(records.getUser().getUserId());
				log.info(records.getTestcase());
				log.info(records.getStatus());
			}
		}*/
		
	}
	
	/*
	@Test
	public void userRequest() throws JSONException, IOException {
		String jsonUrl = String.format("%s.json", "bbb");
		List<ViewElements>elementList = new ArrayList<ViewElements>();
		HashMap<String, ViewElements> sample = new HashMap<>();
		Logger log = Logger.getLogger(getClass());
			log.info(jsonUrl);
			FileReader fr = new FileReader(new File(jsonUrl));
			
			BufferedReader br = new BufferedReader(fr);
			JSONObject jsonObject = new JSONObject(br.readLine());
			br.close();
			fr.close();
			for(String file : getUserFiles(USER_FOLDER_LOCATION + "bbb")) {
				JSONArray jsonHolder = (JSONArray) jsonObject.get(file);
				log.info(jsonHolder);
				ViewElements elements = new ViewElements(jsonHolder);
				elementList.add(elements);
			}
			
		
		
	}*/
	
	@Test
	public void testData() {
		log = Logger.getLogger(getClass());
		
		
		
		/*
		
		List<Record>records = new ArrayList<Record>();
		records = recordRepo.findAllRecordsByUser(userRepo.findUserByUsername("bbb"));
		assertTrue(records.size() == 3);*/
	}
	
	private List<String> getUserFiles(String url) {
		File location = new File(url);
		
		List<String>userFiles = new ArrayList<String>();
		
		for (File file:location.listFiles()) {
			String[] fileHolder = file.getName().split("\\.");
			userFiles.add(fileHolder[0]);
		}
		return userFiles;
	}
	
}
