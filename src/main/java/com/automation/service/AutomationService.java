
package com.automation.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditQuery;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.campaign.campaign;
import com.automation.campaign.processExtension;
import com.automation.db.archive.DbConnection;
import com.automation.db.archive.dbConnect;
import com.automation.db.campaign.Campaign;
import com.automation.db.campaign.CampaignExecution;
import com.automation.db.campaign.DependentRecord;
import com.automation.db.campaign.DependentRecordExecution;
import com.automation.db.campaign.Story;
import com.automation.db.campaign.StoryExecution;
import com.automation.db.campaign.Theme;
import com.automation.db.campaign.ThemeExecution;
import com.automation.db.campaign.repository.CampaignExecutionRepository;
import com.automation.db.campaign.repository.CampaignRepository;
import com.automation.db.campaign.repository.DependentRecordExecutionRepository;
import com.automation.db.campaign.repository.DependentRecordRepository;
import com.automation.db.campaign.repository.StoryExecutionRepository;
import com.automation.db.campaign.repository.StoryRepository;
import com.automation.db.campaign.repository.ThemeExecutionRepository;
import com.automation.db.campaign.repository.ThemeRepository;
import com.automation.db.entity.DataFooter;
import com.automation.db.entity.DataRaw;
import com.automation.db.entity.Record;
import com.automation.db.entity.User;
import com.automation.db.repository.DataFooterRepository;
import com.automation.db.repository.DataRawRepository;
import com.automation.db.repository.RecordRepository;
import com.automation.db.repository.UserRepository;
import com.automation.domain.Entity;
import com.automation.domain.FileEntity;
import com.automation.domain.FooterData;
import com.automation.domain.StoragePath;
import com.automation.domain.ViewElements;
import com.automation.session.FileEntitySession;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class AutomationService {
	
	private final String SERVER_TIMEOUT = "5000";
	private final String TODO_LOCATION = StoragePath.TODO_FOLDER_LOCATION;
	private final String LOCATION = StoragePath.TEMPLATE_HOLDER_LOCATION;
	private final String TODO_FOLDER_PASSED = StoragePath.PASSED_FOLDER_LOCATION;
	private final String TODO_FOLDER_FAILED = StoragePath.FAILED_FOLDER_LOCATION;
	private final String TODO_FOLDER_IGNORED = StoragePath.IGNORED_FOLDER_LOCATION;
	private final String DOWNLOAD_FOLDER = StoragePath.DOWNLOAD_FOLDER;
	private final String USER_FOLDER = StoragePath.USER_FOLDER_LOCATION;
	private final String USER_FOLDER_LOCATION = StoragePath.USER_FOLDER_LOCATION;
	private final String BRIDGE_FOLDER = StoragePath.BRIDGE_FOLDER;
	
	@Autowired
	RecordRepository recordRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	DataFooterRepository footerDataRepo;
	
	@Autowired
	DataRawRepository rawDataRepo;
	
	@Autowired
	CampaignRepository campaignRepo;
	
	@Autowired
	ThemeRepository themeRepo;
	
	@Autowired
	StoryRepository storyRepo;
	
	@Autowired
	DependentRecordRepository dependentRecordRepo;
	
	@Autowired
	CampaignExecutionRepository campaignExeRepo;
	
	@Autowired
	StoryExecutionRepository storyExeRepo;
	
	@Autowired
	ThemeExecutionRepository themeExeRepo;
	
	@Autowired
	DependentRecordExecutionRepository dependentRecordExeRepo;
	
	
	
	
	
	
	
	//MAIN CONTROLLER CLASS FUNCTIONS===================================================
	
	@HystrixCommand(fallbackMethod="serverNotResponding", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", 
					value = SERVER_TIMEOUT)})
	public ModelAndView writeCSV(List<String> webElementName,
			List<String> webElementNature,
			List<String> natureOfActiom,
			List<String> triggerEnter,
			List<String> inputOut,
			List<String> notes,
			List<String> screenCapture,
			String serverImport,
			HttpSession session,
			String transactionType,
			String clientName,
			String severity,
			String description) throws IOException, URISyntaxException {
		Logger log = Logger.getLogger(getClass());
		
		log.info(serverImport + ": " + severity);
		ModelAndView mv = new ModelAndView();
		//String url = "C:\\Users\\rtsbondoc\\Desktop\\ToDoFiles" + session.getAttribute("uname") + "\\" + testcaseNumber()+".csv";
		String url = String.format("%s\\%s.csv",StoragePath.CSVHOLDER_FOLDER, testcaseNumber());
		
		//int elementSize = FileEntitySession.getInstance().getFileEntity().getElements().size();
		int elementSize = notes.size();
		
		FileEntity website = new FileEntity();
		
		
		
		FileWriter csvWriter = new FileWriter(url);
		csvWriter.append("WebElementName");
		csvWriter.append(",");
		csvWriter.append("WebElementNature");
		csvWriter.append(",");
		csvWriter.append("NatureOfAction");
		csvWriter.append(",");
		csvWriter.append("ScreenCapture");
		csvWriter.append(",");
		csvWriter.append("TriggerEnter");
		csvWriter.append(",");
		csvWriter.append("InputOutputValue");
		csvWriter.append(",");
		csvWriter.append("Label");
		csvWriter.append(",");
		csvWriter.append("Timestamp");
		csvWriter.append(",");
		csvWriter.append("SCPath");
		csvWriter.append(",");
		csvWriter.append("Remarks");
		csvWriter.append(",");
		csvWriter.append("LogField");
		csvWriter.append("\n");
		
		for(int i=0; i<elementSize; i++) {
			
			if(i>=0 && i<=1) {
				
				if(session.getAttribute("assigned").toString().equals("selenium5")) {
					session.setAttribute("assigned", "selenium1");
				}
				
				String assignedUname = session.getAttribute("assigned").toString();
				
				csvWriter.append(webElementName.get(i));
				csvWriter.append(",");
				csvWriter.append(webElementNature.get(i));
				csvWriter.append(",");
				csvWriter.append(natureOfActiom.get(i));
				csvWriter.append(",");
				csvWriter.append(screenCapture.get(i));
				csvWriter.append(",");
				csvWriter.append(triggerEnter.get(i));
				csvWriter.append(",");
				csvWriter.append(assignedUname);
				csvWriter.append(",");
				csvWriter.append(notes.get(i));
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append("\n");
			}else {
				csvWriter.append(webElementName.get(i));
				csvWriter.append(",");
				csvWriter.append(webElementNature.get(i));
				csvWriter.append(",");
				csvWriter.append(natureOfActiom.get(i));
				csvWriter.append(",");
				csvWriter.append(screenCapture.get(i));
				csvWriter.append(",");
				csvWriter.append(triggerEnter.get(i));
				csvWriter.append(",");
				csvWriter.append(inputOut.get(i));
				csvWriter.append(",");
				csvWriter.append(notes.get(i));
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append("\n");
			}
			
			
			
		}
		
		
		
		
		
		csvWriter.append("ClientName");
		csvWriter.append(",");
		csvWriter.append(clientName);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//=============================
		csvWriter.append("TransactionType");
		csvWriter.append(",");
		csvWriter.append(transactionType);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==============================
		csvWriter.append("Website");
		csvWriter.append(",");
		csvWriter.append(website.getWebsites().get(clientName));
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("ServerImport");
		csvWriter.append(",");
		csvWriter.append(serverImport);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("Sender");
		csvWriter.append(",");
		csvWriter.append(session.getAttribute("uname").toString());
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("AssignedAccount");
		csvWriter.append(",");
		csvWriter.append(session.getAttribute("assigned").toString());
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("IgnoreSeverity");
		csvWriter.append(",");
		csvWriter.append(severity);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");

		//==================================
		csvWriter.append("TestCaseNumber");
		csvWriter.append(",");
		csvWriter.append(testcaseNumber());
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("TestCaseStatus");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		
		
		
		csvWriter.flush();
		csvWriter.close();
		
		
		String assignedUname = session.getAttribute("assigned").toString();
		int counter = Integer.parseInt(assignedUname.substring(assignedUname.length()-1)) + 1;
		session.setAttribute("assigned", "selenium"+counter);
		
		
		
		
		String jsonUrl = String.format("%s.json", (String)session.getAttribute("uname"));
		JSONObject jsonObject;
		if(new File(jsonUrl).exists()) {
			FileReader fr = new FileReader(new File(jsonUrl));
			BufferedReader br = new BufferedReader(fr);
			jsonObject = new JSONObject(br.readLine());
			fr.close();
			br.close();
		}else {
			jsonObject = new JSONObject();
		}
		List<ViewElements> elementHolder = new ArrayList<ViewElements>();
		ViewElements element = new ViewElements(testcaseNumber(), clientName, transactionType, description);
		elementHolder.add(element);
		jsonObject.put(testcaseNumber(), elementHolder);
		
		FileWriter jsonWriter = new FileWriter(jsonUrl);
		jsonWriter.write(jsonObject.toString());
		jsonWriter.flush();
		jsonWriter.close();
		
		String userFolder = String.format("%s%s\\%s.csv", StoragePath.USER_FOLDER_LOCATION, session.getAttribute("uname"), testcaseNumber());
		FileUtils.copyFile(new File(url), new File(userFolder));
		FileUtils.deleteQuietly(new File(url));
		
		
		
		mv.addObject("user", session.getAttribute("uname"));
		mv.addObject("transactionType", transactionType);
		mv.addObject("elements", FileEntitySession.getInstance().getFileEntity().getElements());
		mv.addObject("Info", "Successfully submitted.");
		mv.setViewName("extemplate");
		
		return mv;
	}
	
	
	
	public ModelAndView verifyCampaign(List<String> webElementName,
			List<String> webElementNature,
			List<String> natureOfActiom,
			List<String> triggerEnter,
			List<String> inputOut,
			List<String> notes,
			List<String> screenCapture,
			String serverImport,
			HttpSession session,
			String transactionType,
			String clientName,
			String severity,
			String description,
			int storyId) throws IOException, URISyntaxException {
		ModelAndView mv = new ModelAndView();
		//String url = "C:\\Users\\rtsbondoc\\Desktop\\ToDoFiles" + session.getAttribute("uname") + "\\" + testcaseNumber()+".csv";
		String url = String.format("%s\\%s.csv",StoragePath.DEPENDENT_TESTCASE_TEMPLATE, testcaseNumber());
		
		//int elementSize = FileEntitySession.getInstance().getFileEntity().getElements().size();
		int elementSize = notes.size();
		
		FileEntity website = new FileEntity();
		
		FileWriter csvWriter = new FileWriter(url);
		csvWriter.append("WebElementName");
		csvWriter.append(",");
		csvWriter.append("WebElementNature");
		csvWriter.append(",");
		csvWriter.append("NatureOfAction");
		csvWriter.append(",");
		csvWriter.append("ScreenCapture");
		csvWriter.append(",");
		csvWriter.append("TriggerEnter");
		csvWriter.append(",");
		csvWriter.append("InputOutputValue");
		csvWriter.append(",");
		csvWriter.append("Label");
		csvWriter.append(",");
		csvWriter.append("Timestamp");
		csvWriter.append(",");
		csvWriter.append("SCPath");
		csvWriter.append(",");
		csvWriter.append("Remarks");
		csvWriter.append(",");
		csvWriter.append("LogField");
		csvWriter.append("\n");
		
		for(int i=0; i<elementSize; i++) {
			
			if(i>=0 && i<=1) {
				
				if(session.getAttribute("assigned").toString().equals("selenium5")) {
					session.setAttribute("assigned", "selenium1");
				}
				
				
				String assignedUname = session.getAttribute("assigned").toString();
				
				switch(clientName) {
					case "uob":
						break;
					case "tsb":
						assignedUname = assignedUname.replace("s", "S");
						break;
				}
				
				csvWriter.append(webElementName.get(i));
				csvWriter.append(",");
				csvWriter.append(webElementNature.get(i));
				csvWriter.append(",");
				csvWriter.append(natureOfActiom.get(i));
				csvWriter.append(",");
				csvWriter.append(screenCapture.get(i));
				csvWriter.append(",");
				csvWriter.append(triggerEnter.get(i));
				csvWriter.append(",");
				csvWriter.append(assignedUname);
				csvWriter.append(",");
				csvWriter.append(notes.get(i));
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append("\n");
			}else {
				csvWriter.append(webElementName.get(i));
				csvWriter.append(",");
				csvWriter.append(webElementNature.get(i));
				csvWriter.append(",");
				csvWriter.append(natureOfActiom.get(i));
				csvWriter.append(",");
				csvWriter.append(screenCapture.get(i));
				csvWriter.append(",");
				csvWriter.append(triggerEnter.get(i));
				csvWriter.append(",");
				csvWriter.append(inputOut.get(i));
				csvWriter.append(",");
				csvWriter.append(notes.get(i));
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append("\n");
			}
			
			
			
		}
		
		
		
		
		
		csvWriter.append("ClientName");
		csvWriter.append(",");
		csvWriter.append(clientName);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//=============================
		csvWriter.append("TransactionType");
		csvWriter.append(",");
		csvWriter.append(transactionType);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==============================
		csvWriter.append("Website");
		csvWriter.append(",");
		csvWriter.append(website.getWebsites().get(clientName));
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("ServerImport");
		csvWriter.append(",");
		csvWriter.append(serverImport);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("Sender");
		csvWriter.append(",");
		csvWriter.append(session.getAttribute("uname").toString());
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("AssignedAccount");
		csvWriter.append(",");
		csvWriter.append(session.getAttribute("assigned").toString());
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("IgnoreSeverity");
		csvWriter.append(",");
		csvWriter.append(severity);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");

		//==================================
		csvWriter.append("TestCaseNumber");
		csvWriter.append(",");
		csvWriter.append(testcaseNumber());
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("TestCaseStatus");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		
		
		
		csvWriter.flush();
		csvWriter.close();
		
		
		String assignedUname = session.getAttribute("assigned").toString();
		int counter = Integer.parseInt(assignedUname.substring(assignedUname.length()-1)) + 1;
		session.setAttribute("assigned", "selenium"+counter);
		
		DependentRecord dRecord = new DependentRecord(testcaseNumber(), storyRepo.findStoryByStoryId(storyId), 
				clientName, description, null);
		dependentRecordRepo.save(dRecord);
		
		
		
		mv.addObject("user", session.getAttribute("uname"));
		mv.addObject("transactionType", transactionType);
		mv.addObject("elements", FileEntitySession.getInstance().getFileEntity().getElements());
		mv.addObject("Info", "Success");
		mv.setViewName("redirect:/dependentrecord/" + storyId);
		
		return mv;
	}
	
	@HystrixCommand(fallbackMethod="serverNotResponding", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", 
					value = SERVER_TIMEOUT)})
	public ModelAndView writeCSVFromNewTemplate(List<String> webElementName,
			List<String> webElementNature,
			List<String> natureOfActiom,
			List<String> triggerEnter,
			List<String> inputOut,
			List<String> notes,
			List<String>screenCapture,
			String clientName,
			String orderType,
			String natureOfOrder,
			String serverImport,
			String severity,
			HttpSession session,
			RedirectAttributes ra,
			String description) throws IOException {
		
		FileEntity website = new FileEntity();
		
		ModelAndView mv = new ModelAndView();
		List<Entity> elements = new ArrayList<>();
		String url = String.format("%s\\%s.csv", StoragePath.CSVHOLDER_FOLDER, testcaseNumber());
		FileWriter csvWriter = new FileWriter(url);
		csvWriter.append("WebElementName");
		csvWriter.append(",");
		csvWriter.append("WebElementNature");
		csvWriter.append(",");
		csvWriter.append("NatureOfAction");
		csvWriter.append(",");
		csvWriter.append("ScreenCapture");
		csvWriter.append(",");
		csvWriter.append("TriggerEnter");
		csvWriter.append(",");
		csvWriter.append("InputOutputValue");
		csvWriter.append(",");
		csvWriter.append("Label");
		csvWriter.append(",");
		csvWriter.append("Timestamp");
		csvWriter.append(",");
		csvWriter.append("SCPath");
		csvWriter.append(",");
		csvWriter.append("Remarks");
		csvWriter.append(",");
		csvWriter.append("LogField");
		csvWriter.append("\n");
		
		for(int i=0; i<webElementName.size(); i++) {
			if(i>=0 && i<=1) {
				if(clientName.equals("tsb")) {
					if(i == 0) {
						Entity entity = new Entity(webElementName.get(i),webElementNature.get(i),natureOfActiom.get(i),triggerEnter.get(i),null,screenCapture.get(i),notes.get(i));
						elements.add(entity);
						csvWriter.append("admit");
						csvWriter.append(",");
						csvWriter.append("id");
						csvWriter.append(",");
						csvWriter.append("click");
						csvWriter.append(",");
						csvWriter.append("false");
						csvWriter.append(",");
						csvWriter.append("false");
						csvWriter.append(",");
						csvWriter.append("");
						csvWriter.append(",");
						csvWriter.append("Admit checkbox");
						csvWriter.append(",");
						csvWriter.append("");
						csvWriter.append(",");
						csvWriter.append("");
						csvWriter.append(",");
						csvWriter.append("");
						csvWriter.append(",");
						csvWriter.append("");
						csvWriter.append("\n");
					}
				}
				if(session.getAttribute("assigned").toString().equals("selenium5")) {
					session.setAttribute("assigned", "selenium1");
				}
				
				String assignedUname = session.getAttribute("assigned").toString();
				
				switch(clientName) {
					case "uob":
						break;
					case "tsb":
						assignedUname = assignedUname.replace("s", "S");
						break;
				}
				
				
				Entity entity = new Entity(webElementName.get(i),webElementNature.get(i),natureOfActiom.get(i),triggerEnter.get(i),assignedUname,screenCapture.get(i),notes.get(i));
				elements.add(entity);
				csvWriter.append(webElementName.get(i));
				csvWriter.append(",");
				csvWriter.append(webElementNature.get(i));
				csvWriter.append(",");
				csvWriter.append(natureOfActiom.get(i));
				csvWriter.append(",");
				csvWriter.append(screenCapture.get(i));
				csvWriter.append(",");
				csvWriter.append(triggerEnter.get(i));
				csvWriter.append(",");
				csvWriter.append(assignedUname);
				csvWriter.append(",");
				csvWriter.append(notes.get(i));
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append("\n");
			}else {
				Entity entity = new Entity(webElementName.get(i),webElementNature.get(i),natureOfActiom.get(i),triggerEnter.get(i),inputOut.get(i),screenCapture.get(i),notes.get(i));
				elements.add(entity);
				csvWriter.append(webElementName.get(i));
				csvWriter.append(",");
				csvWriter.append(webElementNature.get(i));
				csvWriter.append(",");
				csvWriter.append(natureOfActiom.get(i));
				csvWriter.append(",");
				csvWriter.append(screenCapture.get(i));
				csvWriter.append(",");
				csvWriter.append(triggerEnter.get(i));
				csvWriter.append(",");
				csvWriter.append(inputOut.get(i));
				csvWriter.append(",");
				csvWriter.append(notes.get(i));
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append("\n");
			}
			
		}
		
		
		
		csvWriter.append("ClientName");
		csvWriter.append(",");
		csvWriter.append(clientName);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//=============================
		csvWriter.append("TransactionType");
		csvWriter.append(",");
		csvWriter.append(natureOfOrder+orderType);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==============================
		csvWriter.append("Website");
		csvWriter.append(",");
		csvWriter.append(website.getWebsites().get(clientName));
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("ServerImport");
		csvWriter.append(",");
		csvWriter.append(serverImport);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("IgnoreSeverity");
		csvWriter.append(",");
		csvWriter.append(severity);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("Sender");
		csvWriter.append(",");
		csvWriter.append(session.getAttribute("uname").toString());
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("AssignedAccount");
		csvWriter.append(",");
		csvWriter.append(session.getAttribute("assigned").toString());
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("TestCaseNumber");
		csvWriter.append(",");
		csvWriter.append(testcaseNumber());
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("TestCaseStatus");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		
		
		csvWriter.flush();
		csvWriter.close();
		
		
		
		
		String assignedUname = session.getAttribute("assigned").toString();
		int counter = Integer.parseInt(assignedUname.substring(assignedUname.length()-1)) + 1;
		session.setAttribute("assigned", "selenium"+counter);
		
		String jsonUrl = String.format("%s.json", (String)session.getAttribute("uname"));
		JSONObject jsonObject;
		if(new File(jsonUrl).exists()) {
			FileReader fr = new FileReader(new File(jsonUrl));
			BufferedReader br = new BufferedReader(fr);
			jsonObject = new JSONObject(br.readLine());
			fr.close();
			br.close();
		}else {
			jsonObject = new JSONObject();
		}
		List<ViewElements> elementHolder = new ArrayList<ViewElements>();
		ViewElements element = new ViewElements(testcaseNumber(), clientName, natureOfOrder+orderType, description);
		elementHolder.add(element);
		jsonObject.put(testcaseNumber(), elementHolder);
		
		FileWriter jsonWriter = new FileWriter(jsonUrl);
		jsonWriter.write(jsonObject.toString());
		jsonWriter.flush();
		jsonWriter.close();
		
		String userFolder = String.format("%s%s\\%s.csv", StoragePath.USER_FOLDER_LOCATION, session.getAttribute("uname"), testcaseNumber());
		FileUtils.copyFile(new File(url), new File(userFolder));
		FileUtils.deleteQuietly(new File(url));
		
		
		
		ra.addFlashAttribute("Info", "Successfully submitted.");
		ra.addFlashAttribute("user", session.getAttribute("uname"));
		ra.addFlashAttribute("elements", FileEntitySession.getInstance().getFileEntity().getElements());
		mv.setViewName("redirect:/newtemplate");
		return mv;
	}
	
	public ModelAndView checkTemplate(String file, HttpSession session, String status, Integer storyId) throws IOException {
		ModelAndView mv = new ModelAndView();
		
		File csvFile = new File(file);
		FileReader fileReader = new FileReader(csvFile);
		BufferedReader bufferReader = new BufferedReader(fileReader);
		String line;
		int counter=0;
		
		
		
		List<Entity> elements = new ArrayList<>();
		
		while((line = bufferReader.readLine()) != null) {
			String[] lineArr = line.split(",");
			if(counter == 0) {
				counter++;
			}else {
				Entity elementEntity = new Entity(lineArr[0], lineArr[1], lineArr[2], lineArr[3].toLowerCase(), lineArr[4], lineArr[5].toLowerCase(), lineArr[6]);
				elements.add(elementEntity);
			}
		}
		
		String[] fileHolder = csvFile.getName().split("_");
		String[] fileName = fileHolder[1].split("\\.");
		
		
		
		FileEntity fileEntity = new FileEntity(elements);
		FileEntitySession.getInstance().setFileEntity(fileEntity);
		bufferReader.close();
		fileReader.close();
		
		if(storyId == null) {
			mv.addObject("clientName", fileHolder[0]);
			mv.addObject("status", status);
			mv.addObject("storyId", null);
			mv.addObject("user", session.getAttribute("uname"));
			mv.addObject("transactionType", fileName[0]);
			mv.addObject("elements", elements);
			mv.setViewName("extemplate");
			return mv;
		}else {
			mv.addObject("clientName", fileHolder[0]);
			mv.addObject("status", status);
			mv.addObject("storyId", storyId);
			mv.addObject("user", session.getAttribute("uname"));
			mv.addObject("transactionType", fileName[0]);
			mv.addObject("elements", elements);
			mv.setViewName("extemplate");
			return mv;
		}
		
		
		/*
		File location = new File(LOCATION);
		
		List<FileEntity> csvHolder = new ArrayList<>();
		FileEntity data;
		for (File files:location.listFiles()) {
			String fileName = files.toString();
			String extension = "";

			int i = fileName.lastIndexOf('.');
			int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

			if (i > p) {
			    extension = fileName.substring(i+1);
			    if(extension.equals("csv")) {
			    	data = new FileEntity(files.toString(), files.getName());
			    	csvHolder.add(data);
			    }
			}
		}
		mv.addObject("fileinfo", csvHolder);
		mv.addObject("user", session.getAttribute("uname"));
		mv.setViewName("extemplate");*/
		
		
	}
	
	public ModelAndView sendBulkRequest(List<String> schedule,
			List<String>testcase,
			HttpSession session) throws IOException {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		log.info(schedule.size());
		
		for(int i=0; i<schedule.size(); i++) {
			
			String holder = schedule.get(i).replaceAll("T", "");
			log.info(schedule.get(i));
			
		}
		
		
		String srcDir = String.format("%s%s", StoragePath.USER_FOLDER_LOCATION, session.getAttribute("uname"));
		String jsonUrl = String.format("%s.json", session.getAttribute("uname"));
		FileReader fr = new FileReader(new File(jsonUrl));
		BufferedReader br = new BufferedReader(fr);
		JSONObject jsonObject = new JSONObject(br.readLine());
		br.close();
		fr.close();
		
		for(String file : getUserFiles(USER_FOLDER_LOCATION + session.getAttribute("uname").toString())) {
			
			JSONArray jsonHolder = (JSONArray) jsonObject.get(file);
			ViewElements elements = new ViewElements(jsonHolder);
			Record record = new Record(userRepo.findUserByUsername(session.getAttribute("uname").toString()), elements.getTestCaseNumber(), 
					elements.getClientName(), "Pending", elements.getDescription());
			recordRepo.save(record);
		}
		
		
		
		if(new File(jsonUrl).exists()) {
			FileUtils.copyDirectory(new File(srcDir), new File(BRIDGE_FOLDER));
			FileUtils.cleanDirectory(new File(srcDir));
			FileUtils.deleteQuietly(new File(jsonUrl));
			mv.addObject("user", session.getAttribute("uname"));
			mv.addObject("Info","Files successfully queued");
			mv.setViewName("viewfile");
			return mv;
		}else {
			mv.addObject("user", session.getAttribute("uname"));
			mv.addObject("Error", "No files to queue!");
			mv.setViewName("viewfile");
			return mv;
		}
	}
	
	public ModelAndView dashboardPage(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
		List<Record> records = recordRepo.findAll();
		
		mv.addObject("data", records);
		mv.addObject("user", session.getAttribute("uname"));
		mv.setViewName("dashboard");
		
		
		return mv;
	}
	
	public ModelAndView editRequestPage(String testcase, HttpSession session) throws IOException {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		log.info("working 1");
		String userFolderUrl = String.format("%s%s", USER_FOLDER, session.getAttribute("uname").toString());
		
		List<Entity> dataList = new ArrayList<Entity>();
		FooterData footer = new FooterData();
		FileReader reader = new FileReader(new File(session.getAttribute("uname").toString() + ".json"));
		BufferedReader buffer = new BufferedReader(reader);
		JSONObject jsonObject = new JSONObject(buffer.readLine());
		buffer.close();
		
		JSONArray testcaseJson =(JSONArray) jsonObject.get(testcase);
		
		JSONObject descJson = new JSONObject(testcaseJson.get(0).toString());
		
		
		String description = descJson.get("description").toString();
		
		
		File userFolder = new File(userFolderUrl);
		log.info("working 2");
		for(File userFile: userFolder.listFiles()) {
			
			if(userFile.getName().contains(testcase)) {
				log.info("working 3");
				
				String hol = "";
				FileReader fr = new FileReader(userFile);
				BufferedReader br = new BufferedReader(fr);
				while((hol = br.readLine()) != null) {
					String[] data = hol.split(",");
					switch(data[0]) {
						case "ClientName":
							footer.setClientName(data[1]);
							break;
						case "TransactionType":
							footer.setTransactionType(data[1]);
							break;
						case "Website":
							footer.setWebsite(data[1]);
							break;
						case "ServerImport":
							footer.setServerImport(data[1]);
							break;
						case "Sender":
							footer.setSender(data[1]);
							break;
						case "AssignedAccount":
							footer.setAssignedAccount(data[1]);
							break;
						case "IgnoreSeverity":
							footer.setIgnoreSeverity(data[1]);
							break;
						case "TestCaseNumber":
							footer.setTestcaseNumber(data[1]);
							break;
						case "TestCaseStatus":
							break;
						default:
							Entity entity = new Entity(data[0], data[1], data[2], data[3], data[5], data[4], data[6]);
							dataList.add(entity);
							break;
					}
				}
				br.close();
			}
			
		}
		dataList.remove(0);
		mv.addObject("description", description);
		mv.addObject("user", session.getAttribute("uname"));
		mv.addObject("dataList", dataList);
		mv.addObject("footerDataList", footer);
		mv.setViewName("modifyrequest");
		
		
		return mv;
	}
	
	public ModelAndView editRequestPageOneLog(String testcase, HttpSession session) throws IOException {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		
		
		
		
		log.info("working 1");
		String userFolderUrl = String.format("%s%s", USER_FOLDER, session.getAttribute("uname").toString());
		
		
		File dependentFolder = new File(StoragePath.DEPENDENT_TESTCASE_TEMPLATE);
		//if()
		
		
		
		List<Entity> dataList = new ArrayList<Entity>();
		FooterData footer = new FooterData();
		
		
		
		
		
		
		
		
		
		File userFolder = new File(userFolderUrl);
		log.info("working 2");
		for(File userFile: userFolder.listFiles()) {
			
			if(userFile.getName().contains(testcase)) {
				log.info("working 3");
				
				String hol = "";
				FileReader fr = new FileReader(userFile);
				BufferedReader br = new BufferedReader(fr);
				while((hol = br.readLine()) != null) {
					String[] data = hol.split(",");
					switch(data[0]) {
						case "ClientName":
							footer.setClientName(data[1]);
							break;
						case "TransactionType":
							footer.setTransactionType(data[1]);
							break;
						case "Website":
							footer.setWebsite(data[1]);
							break;
						case "ServerImport":
							footer.setServerImport(data[1]);
							break;
						case "Sender":
							footer.setSender(data[1]);
							break;
						case "AssignedAccount":
							footer.setAssignedAccount(data[1]);
							break;
						case "IgnoreSeverity":
							footer.setIgnoreSeverity(data[1]);
							break;
						case "TestCaseNumber":
							footer.setTestcaseNumber(data[1]);
							break;
						case "TestCaseStatus":
							break;
						default:
							Entity entity = new Entity(data[0], data[1], data[2], data[3], data[5], data[4], data[6]);
							dataList.add(entity);
							break;
					}
				}
			}
			
		}
		dataList.remove(0);
		mv.addObject("user", session.getAttribute("uname"));
		mv.addObject("dataList", dataList);
		mv.addObject("footerDataList", footer);
		mv.setViewName("modifyrequest");
		
		
		return mv;
	}
	
	
	
	public ModelAndView verifyModify(List<String> webElementName,
			List<String> webElementNature,
			List<String> natureOfActiom,
			List<String> triggerEnter,
			List<String> inputOut,
			List<String> notes,
			List<String> screenCapture,
			String serverImport,
			String transactionType,
			String clientName,
			String severity,
			String testcase,
			String assignedAccount,
			String sender,
			HttpSession session,
			RedirectAttributes ra,
			String description) throws IOException{
		ModelAndView mv = new ModelAndView();
		
		
		String url = String.format("%s\\%s.csv",StoragePath.CSVHOLDER_FOLDER, testcaseNumber());
		
		//int elementSize = FileEntitySession.getInstance().getFileEntity().getElements().size();
		int elementSize = notes.size();
		
		
		FileWriter csvWriter = new FileWriter(url);
		csvWriter.append("WebElementName");
		csvWriter.append(",");
		csvWriter.append("WebElementNature");
		csvWriter.append(",");
		csvWriter.append("NatureOfAction");
		csvWriter.append(",");
		csvWriter.append("ScreenCapture");
		csvWriter.append(",");
		csvWriter.append("TriggerEnter");
		csvWriter.append(",");
		csvWriter.append("InputOutputValue");
		csvWriter.append(",");
		csvWriter.append("Label");
		csvWriter.append(",");
		csvWriter.append("Timestamp");
		csvWriter.append(",");
		csvWriter.append("SCPath");
		csvWriter.append(",");
		csvWriter.append("Remarks");
		csvWriter.append(",");
		csvWriter.append("LogField");
		csvWriter.append("\n");
		
		for(int i=0; i<elementSize; i++) {
			
			if(i>=0 && i<=1) {
				
				
				
				
				
				csvWriter.append(webElementName.get(i));
				csvWriter.append(",");
				csvWriter.append(webElementNature.get(i));
				csvWriter.append(",");
				csvWriter.append(natureOfActiom.get(i));
				csvWriter.append(",");
				csvWriter.append(screenCapture.get(i));
				csvWriter.append(",");
				csvWriter.append(triggerEnter.get(i));
				csvWriter.append(",");
				csvWriter.append(assignedAccount);
				csvWriter.append(",");
				csvWriter.append(notes.get(i));
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append("\n");
			}else {
				csvWriter.append(webElementName.get(i));
				csvWriter.append(",");
				csvWriter.append(webElementNature.get(i));
				csvWriter.append(",");
				csvWriter.append(natureOfActiom.get(i));
				csvWriter.append(",");
				csvWriter.append(screenCapture.get(i));
				csvWriter.append(",");
				csvWriter.append(triggerEnter.get(i));
				csvWriter.append(",");
				csvWriter.append(inputOut.get(i));
				csvWriter.append(",");
				csvWriter.append(notes.get(i));
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append("\n");
			}
			
			
			
		}
		
		
		
		
		
		csvWriter.append("ClientName");
		csvWriter.append(",");
		csvWriter.append(clientName);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//=============================
		csvWriter.append("TransactionType");
		csvWriter.append(",");
		csvWriter.append(transactionType);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==============================
		csvWriter.append("Website");
		csvWriter.append(",");
		csvWriter.append("http://3.1.175.154:15050/wui/activity/index");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("ServerImport");
		csvWriter.append(",");
		csvWriter.append(serverImport);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("Sender");
		csvWriter.append(",");
		csvWriter.append(sender);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("AssignedAccount");
		csvWriter.append(",");
		csvWriter.append(assignedAccount);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("IgnoreSeverity");
		csvWriter.append(",");
		csvWriter.append(severity);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");

		//==================================
		csvWriter.append("TestCaseNumber");
		csvWriter.append(",");
		csvWriter.append(testcase);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("TestCaseStatus");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		
		
		
		csvWriter.flush();
		csvWriter.close();
		
		String jsonUrl = String.format("%s.json", (String)session.getAttribute("uname"));
		JSONObject jsonObject;
		if(new File(jsonUrl).exists()) {
			FileReader fr = new FileReader(new File(jsonUrl));
			BufferedReader br = new BufferedReader(fr);
			jsonObject = new JSONObject(br.readLine());
			jsonObject.remove(testcase);
			fr.close();
			br.close();
		}else {
			jsonObject = new JSONObject();
		}
		List<ViewElements> elementHolder = new ArrayList<ViewElements>();
		ViewElements element = new ViewElements(testcase, clientName, transactionType, description);
		elementHolder.add(element);
		jsonObject.put(testcase, elementHolder);
		
		FileWriter jsonWriter = new FileWriter(jsonUrl);
		jsonWriter.write(jsonObject.toString());
		jsonWriter.flush();
		jsonWriter.close();
		
		
		
		
		String userFolder = String.format("%s%s\\%s.csv", StoragePath.USER_FOLDER_LOCATION, sender, testcase);
		FileUtils.copyFile(new File(url), new File(userFolder));
		FileUtils.deleteQuietly(new File(url));
		
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ra.addFlashAttribute("user", session.getAttribute("uname"));
		ra.addFlashAttribute("Info", "Successully modified.");
		mv.setViewName("redirect:/viewfiles");
		return mv;
	}
	
	public ModelAndView deleteRequest(String testcase,
			HttpSession session,
			RedirectAttributes ra) throws IOException {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		String userFolder = String.format("%s%s\\%s.csv", StoragePath.USER_FOLDER_LOCATION, session.getAttribute("uname").toString(), testcase);
		File jsonFile = new File(session.getAttribute("uname").toString() + ".json");
		FileReader fr = new FileReader(jsonFile);
		BufferedReader br = new BufferedReader(fr);
		JSONObject jsonObject = new JSONObject(br.readLine().toString());
		fr.close();
		br.close();
		
		
		FileWriter jsonWriter = new FileWriter(jsonFile);
		jsonObject.remove(testcase);
		
		jsonWriter.write(jsonObject.toString());
		jsonWriter.flush();
		jsonWriter.close();
		
		
		FileUtils.deleteQuietly(new File(userFolder));
		
		ra.addFlashAttribute("Info", "Successfully Deleted.");
		mv.setViewName("redirect:/viewfiles");
		
		return mv;
	}
	
	public ModelAndView executeCampaign(int campaignId, HttpSession session,
			String execution, RedirectAttributes ra) throws SQLException {
		ModelAndView mv = new ModelAndView();
		
		Logger log = Logger.getLogger(getClass());
		//Campaign
		Campaign campaign = campaignRepo.findCampaignByCampaignId(campaignId);
		log.info(campaign.getCampaignId());
		//Theme
		List<Theme> theme = themeRepo.findAllThemeByCampaignId(campaign);
		for(Theme themes: theme) {
			log.info(themes.getThemeId());
		}
		//Story
		List<Story>story = getStories(theme);
		for(Story stories: story) {
			log.info(stories.getStoryId());
		}
		//Dependent Record
		List<DependentRecord>dependentRecord = getDependentRecord(story);
		
		String[] testCaseNumber = new String[dependentRecord.size()];
		String[] storyExecutionNumber = new String[dependentRecord.size()];
		String[] description = new String[dependentRecord.size()];
		String[] serverImport = new String[dependentRecord.size()];
		String[] ignoreSeverity = new String[dependentRecord.size()];
		
		for(DependentRecord drs : dependentRecord) {
			log.info(drs.getTestcaseNumber());
		}
		
		int counter = 0;
		CampaignExecution campaignExecution = new CampaignExecution(campaign, campaign.getTimestamp(), campaign.getDescription(),
				"Executing", campaign.getCampaignName());
		campaignExeRepo.save(campaignExecution);
		
		for(Theme themes : theme) {
			ThemeExecution themeExe = new ThemeExecution(themeRepo.findThemeByThemeId(themes.getThemeId()), campaignExecution, themes.getTimestamp(), 
					themes.getDescription(), themes.getStatus(), themes.getThemeName());
			themeExeRepo.save(themeExe);
			int themeId = themes.getThemeId();
			for(Story stories:story) {
				if(themeId == stories.getThemeId().getThemeId()) {
					StoryExecution storyExe = new StoryExecution(storyRepo.findStoryByStoryId(stories.getStoryId()), themeExe, stories.getTimestamp(), stories.getDescription(),
							stories.getStatus(), stories.getStoryName());
					storyExe.setServerImport(stories.getServerImport());
					storyExe.setIgnoreSeverity(stories.getIgnoreSeverity());
					storyExeRepo.save(storyExe);
					int storyid = stories.getStoryId();
					for(DependentRecord dRecords : dependentRecord) {
						if(storyid == dRecords.getStoryId().getStoryId()) {
							DependentRecordExecution dRecordExe = new DependentRecordExecution(dRecords.getTestcaseNumber(), storyExe, 
									dRecords.getClientName(), dRecords.getDescription(), "Pending");
							dependentRecordExeRepo.save(dRecordExe);
							
							testCaseNumber[counter] = dRecordExe.getTestcaseNumber();
							storyExecutionNumber[counter] = Integer.toString(dRecordExe.getStoryId().getStoryExecutionNumber());
							description[counter] = dRecordExe.getDescription();
							ignoreSeverity[counter] = dRecordExe.getStoryId().getIgnoreSeverity();
							serverImport[counter] = dRecordExe.getStoryId().getServerImport();
							counter++;
						}
					}
				}
			}
		}
		
		
		
		processExtension pController = new processExtension();
		pController.processController(testCaseNumber, storyExecutionNumber, StoragePath.DEPENDENT_TESTCASE_TEMPLATE, 
				StoragePath.TEMPORARY_HOLDER_FOLDER, Integer.toString(userRepo.findUserByUsername(session.getAttribute("uname").toString()).getUserId()), dependentRecord.size() == 0 ? null:dependentRecord.get(0).getClientName(), 
				description, execution.equals("null") ? getCurrentDateAndTime():execution, ignoreSeverity, serverImport);
		
		campaign execute = new campaign(Integer.toString(campaignExecution.getCampaignExecutionNumber()), 
				StoragePath.BRIDGE_FOLDER, StoragePath.TEMPORARY_HOLDER_FOLDER);
		
		execute.start();
		 
		
		ra.addFlashAttribute("Info", "Campaign Successfully Executed.");
		mv.setViewName("redirect:/campaign");
		
		
		return mv;
	}
	
	private String getCurrentDateAndTime() {
		
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		String tempA = dateFormat.format(date).toString();
		
		return tempA;
	}
	
	private List<Story>getStories(List<Theme>theme){
		List<Story> story = new ArrayList<Story>();
		for(Theme themes:theme) {
			List<Story>storyHolder = storyRepo.findAllStoryByThemeId(themes);
			for(Story stories:storyHolder) {
				story.add(stories);
			}
			
		}
		return story;
	}
	
	private List<DependentRecord>getDependentRecord(List<Story>story){
		List<DependentRecord> dependentRecord = new ArrayList<DependentRecord>();
		for(Story stories:story) {
			List<DependentRecord>drHolder = dependentRecordRepo.findAllDependentRecordByStoryId(stories);
			for(DependentRecord dependentRecords:drHolder) {
				dependentRecord.add(dependentRecords);
			}
			
		}
		return dependentRecord;
	}
	
	public ModelAndView viewCampaign(HttpSession session, RedirectAttributes ra,
			Model model) {
		ModelAndView mv = new ModelAndView();
		
		List<Campaign>campaignList = campaignRepo.findAllCampaignByUserId(userRepo.findUserByUsername(session.getAttribute("uname").toString()));
		List<String>status = new ArrayList<String>();
		
		for(Campaign campaigns:campaignList) {
			int size = campaignExeRepo.findAllCampaignExecutionByCampaignId(campaigns).size();
			if(size > 0) {
				if(campaignExeRepo.findAllCampaignExecutionByCampaignId(campaigns).get(size - 1).getStatus() == null) {
					status.add("");
				}else {
					status.add(campaignExeRepo.findAllCampaignExecutionByCampaignId(campaigns).get(size - 1).getStatus());
				}
			}else {
				status.add("");
			}
		}
		
		
		
		mv.addObject("Info", model.asMap().get("Info"));
		mv.addObject("user", session.getAttribute("uname"));
		mv.addObject("campaignData", campaignList);
		mv.addObject("status" , status);
		mv.setViewName("campaign");
		
		return mv;
		
	}
	
	public ModelAndView createCampaign(HttpSession session, String campaignName,
			String description) {
		ModelAndView mv = new ModelAndView();
		
		Campaign campaign = new Campaign(userRepo.findUserByUsername(session.getAttribute("uname").toString()), timeStamp(), 
				description, null, campaignName);
		campaignRepo.save(campaign);
		
		
		mv.setViewName("redirect:/campaign");
		
		
		return mv;
	}
	
	public ModelAndView modifyCampaign(String campaignName,
			int campaignId, String description) {
		ModelAndView mv = new ModelAndView();
		
		Campaign campaign = campaignRepo.findCampaignByCampaignId(campaignId);
		campaign.setCampaignName(campaignName);
		campaign.setDescription(description);
		campaignRepo.save(campaign);
		
		
		mv.setViewName("redirect:/campaign");
		return mv;
	}
	
	public ModelAndView viewTheme(int campaignId, HttpSession session, Model model) {
		ModelAndView mv = new ModelAndView();
		
		List<Theme>themeList = themeRepo.findAllThemeByCampaignId(campaignRepo.findCampaignByCampaignId(campaignId));
		
		mv.addObject("user", session.getAttribute("uname"));
		mv.addObject("themeData", themeList);
		mv.addObject("campaignId", campaignId);
		mv.setViewName("theme");
		
		return mv;
	}
	
	public ModelAndView createTheme(HttpSession session,
			String themeName, String description,
			int campaignId,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		
		Theme theme = new Theme(campaignRepo.findCampaignByCampaignId(campaignId), timeStamp(), 
				description, null, themeName);
		
		themeRepo.save(theme);
		
		ra.addFlashAttribute("campaignId", campaignId);
		mv.setViewName("redirect:/theme/" + campaignId);
		
		
		return mv;
	}
	
	public ModelAndView modifyTheme(String themeName,
			int themeId,
			String description,
			String campaignId) {
		ModelAndView mv = new ModelAndView();
		
		Theme theme = themeRepo.findThemeByThemeId(themeId);
		theme.setThemeName(themeName);
		theme.setDescription(description);
		themeRepo.save(theme);
		
		mv.setViewName("redirect:/theme/" + campaignId);
		
		return mv;
	}
	
	
	
	public ModelAndView viewStory(int themeId, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
		List<Story>storyList = storyRepo.findAllStoryByThemeId(themeRepo.findThemeByThemeId(themeId));
		
		mv.addObject("user", session.getAttribute("uname"));
		mv.addObject("storyData", storyList);
		mv.addObject("themeId", themeId);
		mv.setViewName("story");
		
		return mv;
	}
	
	public ModelAndView createStory(HttpSession session,
			String storyName, String description,
			int themeId,
			RedirectAttributes ra,
			String serverImport,
			String ignoreSeverity) {
		ModelAndView mv = new ModelAndView();
		
		Story story = new Story(themeRepo.findThemeByThemeId(themeId), timeStamp(), 
				description, null, storyName);
		story.setServerImport(serverImport);
		story.setIgnoreSeverity(ignoreSeverity);
		storyRepo.save(story);
		
		mv.setViewName("redirect:/story/" + themeId);
		
		return mv;
	}
	
	public ModelAndView modifyStory(int storyId, String storyName,
			String description, String themeId, String serverImport,
			String ignoreSeverity) {
		ModelAndView mv = new ModelAndView();
		
		Story story = storyRepo.findStoryByStoryId(storyId);
		story.setStoryName(storyName);
		story.setDescription(description);
		story.setServerImport(serverImport);
		story.setIgnoreSeverity(ignoreSeverity);
		storyRepo.save(story);
		
		mv.setViewName("redirect:/story/"+themeId);
		
		return mv;
	}
	
	public ModelAndView viewDependentRecord(int storyId, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
		File location = new File(LOCATION);
		
		List<FileEntity> csvHolder = new ArrayList<>();
		FileEntity data;
		
		for (File files:location.listFiles()) {
			String fileName = files.toString();
			String extension = "";

			int i = fileName.lastIndexOf('.');
			int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

			if (i > p) {
			    extension = fileName.substring(i+1);
			    if(extension.equals("csv")) {
			    	data = new FileEntity(files.toString(), files.getName());
			    	csvHolder.add(data);
			    }
			}
		}
		
		List<DependentRecord>dependentRecords = dependentRecordRepo.findAllDependentRecordByStoryId(storyRepo.findStoryByStoryId(storyId));
		
		mv.addObject("storyrecord", dependentRecords);
		mv.addObject("user", session.getAttribute("uname"));
		mv.addObject("fileinfo", csvHolder);
		mv.addObject("storyId", storyId);
		mv.setViewName("adminreport");
		
		return mv;
		
	}
	
	public ModelAndView modifyDependent(List<String> webElementName,
			List<String> webElementNature,
			List<String> natureOfActiom,
			List<String> triggerEnter,
			List<String> inputOut,
			List<String> notes,
			List<String> screenCapture,
			String serverImport,
			HttpSession session,
			String transactionType,
			String clientName,
			String severity,
			String description,
			String testcase) throws IOException, URISyntaxException {
		ModelAndView mv = new ModelAndView();
		//String url = "C:\\Users\\rtsbondoc\\Desktop\\ToDoFiles" + session.getAttribute("uname") + "\\" + testcaseNumber()+".csv";
		String url = String.format("%s\\%s.csv",StoragePath.DEPENDENT_TESTCASE_TEMPLATE, testcase);
		
		FileUtils.deleteQuietly(new File(url));
		
		String newFile = String.format("%s\\%s.csv",StoragePath.DEPENDENT_TESTCASE_TEMPLATE, testcase);
		
		//int elementSize = FileEntitySession.getInstance().getFileEntity().getElements().size();
		int elementSize = notes.size();
		
		FileEntity website = new FileEntity();
		
		FileWriter csvWriter = new FileWriter(newFile);
		csvWriter.append("WebElementName");
		csvWriter.append(",");
		csvWriter.append("WebElementNature");
		csvWriter.append(",");
		csvWriter.append("NatureOfAction");
		csvWriter.append(",");
		csvWriter.append("ScreenCapture");
		csvWriter.append(",");
		csvWriter.append("TriggerEnter");
		csvWriter.append(",");
		csvWriter.append("InputOutputValue");
		csvWriter.append(",");
		csvWriter.append("Label");
		csvWriter.append(",");
		csvWriter.append("Timestamp");
		csvWriter.append(",");
		csvWriter.append("SCPath");
		csvWriter.append(",");
		csvWriter.append("Remarks");
		csvWriter.append(",");
		csvWriter.append("LogField");
		csvWriter.append("\n");
		
		for(int i=0; i<elementSize; i++) {
			
			if(i>=0 && i<=1) {
				
				if(session.getAttribute("assigned").toString().equals("selenium5")) {
					session.setAttribute("assigned", "selenium1");
				}
				
				
				String assignedUname = session.getAttribute("assigned").toString();
				
				switch(clientName) {
					case "uob":
						break;
					case "tsb":
						assignedUname = assignedUname.replace("s", "S");
						break;
				}
				
				csvWriter.append(webElementName.get(i));
				csvWriter.append(",");
				csvWriter.append(webElementNature.get(i));
				csvWriter.append(",");
				csvWriter.append(natureOfActiom.get(i));
				csvWriter.append(",");
				csvWriter.append(screenCapture.get(i));
				csvWriter.append(",");
				csvWriter.append(triggerEnter.get(i));
				csvWriter.append(",");
				csvWriter.append(assignedUname);
				csvWriter.append(",");
				csvWriter.append(notes.get(i));
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append("\n");
			}else {
				csvWriter.append(webElementName.get(i));
				csvWriter.append(",");
				csvWriter.append(webElementNature.get(i));
				csvWriter.append(",");
				csvWriter.append(natureOfActiom.get(i));
				csvWriter.append(",");
				csvWriter.append(screenCapture.get(i));
				csvWriter.append(",");
				csvWriter.append(triggerEnter.get(i));
				csvWriter.append(",");
				csvWriter.append(inputOut.get(i));
				csvWriter.append(",");
				csvWriter.append(notes.get(i));
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append("\n");
			}
			
			
			
		}
		
		
		
		
		
		csvWriter.append("ClientName");
		csvWriter.append(",");
		csvWriter.append(clientName);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//=============================
		csvWriter.append("TransactionType");
		csvWriter.append(",");
		csvWriter.append(transactionType);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==============================
		csvWriter.append("Website");
		csvWriter.append(",");
		csvWriter.append(website.getWebsites().get(clientName));
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("ServerImport");
		csvWriter.append(",");
		csvWriter.append(serverImport);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("Sender");
		csvWriter.append(",");
		csvWriter.append(session.getAttribute("uname").toString());
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("AssignedAccount");
		csvWriter.append(",");
		csvWriter.append(session.getAttribute("assigned").toString());
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("IgnoreSeverity");
		csvWriter.append(",");
		csvWriter.append(severity);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");

		//==================================
		csvWriter.append("TestCaseNumber");
		csvWriter.append(",");
		csvWriter.append(testcase);
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append("\n");
		//==================================
		csvWriter.append("TestCaseStatus");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		csvWriter.append(",");
		csvWriter.append("");
		
		
		
		csvWriter.flush();
		csvWriter.close();
		
		
		String assignedUname = session.getAttribute("assigned").toString();
		int counter = Integer.parseInt(assignedUname.substring(assignedUname.length()-1)) + 1;
		session.setAttribute("assigned", "selenium"+counter);
		
		
		mv.addObject("user", session.getAttribute("uname"));
		mv.addObject("Info", "Success");
		mv.setViewName("redirect:/dependentrecord/" + dependentRecordRepo.findDependentRecordByTestcaseNumber(testcase).getStoryId().getStoryId());
		
		return mv;
	}
	
	public ModelAndView processArchive(String testcase,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		DbConnection dbController = new DbConnection();
		
		Logger log = Logger.getLogger(getClass());
		dbConnect db = new dbConnect();
		
		Record testcase_record = recordRepo.findRecordByTestcase(testcase);
		DataFooter footer_data = footerDataRepo.findDataFooterByTestcaseNumber(testcase_record);
		List<DataRaw> raw_data = rawDataRepo.findAllDataRawByTestcaseNumber(testcase_record);
		
		
		db.dataBaseController(raw_data, footer_data, testcase_record);
		
		
		
		footerDataRepo.delete(footer_data);
		rawDataRepo.removeDataRawsByTestcaseNumber(testcase_record);
		dbController.dataBaseController(testcase_record);
		
		
		
		
		ra.addFlashAttribute("Info", "Data successfully archived.");
		mv.setViewName("redirect:/getreports");
		
		return mv;
	}
	
	public ModelAndView editSubmittedRequest(String testcase,
			HttpSession session) throws IOException {
		ModelAndView mv = new ModelAndView();
		
		File tempalateDirectory = new File(StoragePath.DEPENDENT_TESTCASE_TEMPLATE);
		
		
		List<Entity>rawDataList = new ArrayList<Entity>();
		DataFooter footerData = new DataFooter();
		
		
		for(File testcases:tempalateDirectory.listFiles()) {
			if(testcases.getName().contains(testcase)) {
				String hol = "";
				FileReader fr = new FileReader(testcases);
				BufferedReader br = new BufferedReader(fr);
				while((hol = br.readLine()) != null) {
					String[] data = hol.split(",");
					switch(data[0]) {
						case "ClientName":
							footerData.setClientName(data[1]);
							break;
						case "TransactionType":
							footerData.setTransactionType(data[1]);
							break;
						case "Website":
							footerData.setWebsite(data[1]);
							break;
						case "ServerImport":
							footerData.setServerImport(data[1]);
							break;
						case "Sender":
							footerData.setSender(data[1]);
							break;
						case "AssignedAccount":
							footerData.setAssignedAccount(data[1]);
							break;
						case "IgnoreSeverity":
							footerData.setIgnoreSeverity(data[1]);
							break;
						case "TestCaseNumber":
							break;
						case "TestCaseStatus":
							break;
						default:
							Entity entity = new Entity(data[0], data[1], data[2], data[3], data[5], data[4], data[6]);
							rawDataList.add(entity);
							break;
					}
				}
				br.close();
				fr.close();
				rawDataList.remove(0);
				mv.addObject("user", session.getAttribute("uname"));
				mv.addObject("page", "dependent");
				mv.addObject("clientName", footerData.getClientName());
				mv.addObject("user", session.getAttribute("uname"));
				mv.addObject("transactionType", footerData.getTransactionType());
				mv.addObject("elements", rawDataList);
				mv.addObject("testcase", testcase);
				mv.setViewName("extemplate");
				return mv;
			}
		}
		
		
		
		if(recordRepo.findRecordByTestcase(testcase).getStatus().equals("Pending")) {
			
			File todoFolder = new File(TODO_LOCATION);
			for(File testcaseFile:todoFolder.listFiles()) {
				if(testcaseFile.getName().contains(testcase)) {
					int counter = 0;
					String brHolder = "";
					FileReader fr = new FileReader(testcaseFile);
					BufferedReader br = new BufferedReader(fr);
					while((brHolder = br.readLine()) != null) {
						String[]data = brHolder.split(",");
						if(counter == 0) {
							counter++;
						}else {
							switch(data[0]) {
								case "ClientName":
									footerData.setClientName(data[1]);
									break;
								case "TransactionType":
									footerData.setTransactionType(data[1]);
									break;
								case "Website":
									break;
								case "ServerImport":
									break;
								case "Sender":
									break;
								case "AssignedAccount":
									break;
								case "IgnoreSeverity":
									break;
								case "TestCaseNumber":
									break;
								case "TestCaseStatus":
									break;
								default:
									Entity rawData = new Entity(data[0], data[1], data[2], data[4], data[5], data[3], data[6]);
									rawDataList.add(rawData);
							}
						}
					}
					fr.close();
					br.close();
				}
			}
			mv.addObject("clientName", footerData.getClientName());
			mv.addObject("user", session.getAttribute("uname"));
			mv.addObject("transactionType", footerData.getTransactionType());
			mv.addObject("elements", rawDataList);
			mv.setViewName("extemplate");
			return mv;
		}else {
			
			List<DataRaw>dataFromDb = rawDataRepo.findAllDataRawByTestcaseNumber(recordRepo.findRecordByTestcase(testcase));
			footerData = footerDataRepo.findDataFooterByTestcaseNumber(recordRepo.findRecordByTestcase(testcase));
			
			for(DataRaw dataToBePassed: dataFromDb) {
				Entity rawData = new Entity(dataToBePassed.getWebELementName(),dataToBePassed.getWebElementNature(),
						dataToBePassed.getNatureOfAction(), dataToBePassed.getTriggerEnter(),
						dataToBePassed.getInputOutputValue(), dataToBePassed.getScreenCapture(), 
						dataToBePassed.getLabel());
				rawDataList.add(rawData);
			}
			
			mv.addObject("clientName", footerData.getClientName());
			mv.addObject("user", session.getAttribute("uname"));
			mv.addObject("transactionType", footerData.getTransactionType());
			mv.addObject("elements", rawDataList);
			mv.setViewName("extemplate");
			return mv;
			
		}
		
		
	}
	
	public ModelAndView setReportPage(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		List<Record> records = new ArrayList<Record>();
		
		com.automation.user.domain.User user =  (com.automation.user.domain.User) session.getAttribute("user");
		
		
		if(user.getUserType().equals("superuser")) {
			records = recordRepo.findAll();
			mv.addObject("records", records);
			mv.addObject("user", session.getAttribute("uname"));
			mv.setViewName("adminreport");
			return mv;
		}else if(user.getUserType().equals("user")) {
			records = recordRepo.findAllRecordsByUser(userRepo.findUserByUsername(session.getAttribute("uname").toString()));
			mv.addObject("records", records);
			mv.addObject("user", session.getAttribute("uname"));
			mv.setViewName("userreport");
			return mv;
		}else {
			mv.setViewName("redirect:/login");
			return mv;
		}
	}
	
	public void processDownload(List<String> file, HttpSession session,
			RedirectAttributes ra,
			HttpServletResponse response) throws IOException {
		Logger log = Logger.getLogger(getClass());
		String holder = "";
		int rowNum = 0;
		HSSFWorkbook excelFile = new HSSFWorkbook();
		HSSFSheet sheetHolder;
		File location = null;
		
		for(int i=0; i<file.size(); i++) {
			
			String[] testcase = file.get(i).split("_");
			log.info(i);
			if(testcase[1].equalsIgnoreCase("passed")) {
				location = new File(TODO_FOLDER_PASSED);
			}else if(testcase[1].equalsIgnoreCase("failed")) {
				location = new File(TODO_FOLDER_FAILED);
			}else {
				location = new File(TODO_FOLDER_IGNORED);
			}
			log.info(location.toString());
			File[] filesHolder = location.listFiles();
			for(File files: filesHolder) {
				if(files.getName().contains(testcase[0])) {
					log.info(testcase[0]);
					sheetHolder = excelFile.createSheet(file.get(i).split("_")[0]);
					FileReader fr = new FileReader(files);
					BufferedReader br = new BufferedReader(fr);
					while((holder = br.readLine())!= null) {
						String[] row = holder.split(",");
						HSSFRow currRow = sheetHolder.createRow(rowNum);
						for(int j=0; j<row.length; j++) {
							currRow.createCell(j).setCellValue(row[j]);
						}
						rowNum++;
					}
					fr.close();
					br.close();
					break;
				}
				rowNum = 0;
			}
		}
		File excel = new File(DOWNLOAD_FOLDER + session.getAttribute("uname").toString() + ".xls");
		FileOutputStream out = new FileOutputStream(excel);
		InputStream io = new FileInputStream(excel);
		excelFile.write(out);
		excelFile.close();
		out.close();
		response.addHeader("Content-disposition", "attachment;filename=" + session.getAttribute("uname").toString() + ".xls");
	    response.setContentType("application/xls");
		IOUtils.copy(io, response.getOutputStream());
		response.flushBuffer();
		//ra.addFlashAttribute("user", session.getAttribute("uname"));
		//ra.addFlashAttribute("Info", "File will start to download...");
		//mv.setViewName("redirect:/getreports");
		//return mv;
	}
	
	
	
	
	
	//PAGE CONTROLLER CLASS FUNCTIONS===================================================
	public int getReportsCount(HttpSession session) {
		
		
		
		if(userRepo.findUserByUsername(session.getAttribute("uname").toString()).getUserAccess().equals("superuser")) {
			
			return recordRepo.findAll().size();
			
		}else if(userRepo.findUserByUsername(session.getAttribute("uname").toString()).getUserAccess().equals("user")) {
			return recordRepo.findAllRecordsByUser(userRepo.findUserByUsername(session.getAttribute("uname").toString())).size();
		}else {
			return 0;
		}
		
	}
	
	public List<User>getUsers(){
		List<User>users = new ArrayList<User>();
		for(User user: userRepo.findAll()) {
			if(!user.getUserAccess().equals("officer")) {
				users.add(user);
			}
		}
		return users;
	}
	
	
	//PRIVATE FUNCTIONS FOR SERVICE FUNCTIONS
	
	public String testcaseNumber(){
		Date date = new Date();
		String dateFileName;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		String tempA = dateFormat.format(date).toString();
		dateFileName = tempA;
		return dateFileName;
	}
	
	private String timeStamp(){
		Date date = new Date();
		String dateFileName;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		String tempA = dateFormat.format(date).toString();
		dateFileName = tempA;
		return dateFileName;
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
	
	//HYSTRIX FALLBACK METHODS
	
	/*HYSTRIX FALLBACK
	 * List<String> webElementName,
			List<String> webElementNature,
			List<String> natureOfActiom,
			List<String> triggerEnter,
			List<String> inputOut,
			List<String> notes,
			List<String> screenCapture,
			String serverImport
	*/
	public ModelAndView serverNotResponding(List<String> webElementName,
			List<String> webElementNature,
			List<String> natureOfActiom,
			List<String> triggerEnter,
			List<String> inputOut,
			List<String> notes,
			List<String> screenCapture,
			String serverImport,
			HttpSession session,
			String TransactionType,
			String clientName,
			String severity,
			String description) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("user", session.getAttribute("uname"));
		mv.addObject("Error", "Server not responding...");
		mv.setViewName("extemplate");
		return mv;
	}
	
	/*HYSTRIX FALLBACK 
	 * List<String> webElementName,
			List<String> webElementNature,
			List<String> natureOfActiom,
			List<String> triggerEnter,
			List<String> inputOut,
			List<String> notes,
			List<String>screenCapture,
			String orderType,
			String natureOfOrder,
			String serverImport*/
	public ModelAndView serverNotResponding(List<String> webElementName,
			List<String> webElementNature,
			List<String> natureOfActiom,
			List<String> triggerEnter,
			List<String> inputOut,
			List<String> notes,
			List<String>screenCapture,
			String clientName,
			String orderType,
			String natureOfOrder,
			String serverImport,
			String severity,
			HttpSession session,
			RedirectAttributes ra,
			String description) {
		ModelAndView mv = new ModelAndView();
		ra.addFlashAttribute("user", session.getAttribute("uname"));
		ra.addFlashAttribute("Error", "Server not responding...");
		mv.setViewName("redirect:/newtemplate");
		return mv;
	}
	
}
