package com.automation.campaign.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.campaign.domain.DependentTestcase;
import com.automation.configuration.beans.UrlBeans;
import com.automation.template.domain.TemplateData;
import com.automation.testcase.domain.TestcaseRecord;
import com.automation.universe.domain.Script;
import com.automation.universe.domain.ScriptVariable;
import com.automation.universe.domain.Universe;
import com.automation.user.domain.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class DependentTestcaseService {
	
	public ModelAndView setDependentTestcasePage(long storyId, HttpSession session,
			Model model) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		session.setAttribute("storyId", storyId);
		
		User userr = (User)session.getAttribute("user");
		
		String url = String.format("%s/%s?storyId=%d", UrlBeans.DEPENDENT_TESTCASE_SERVICE, "getdependenttestcase", storyId);
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		JSONArray jsonArray = jsonResponse.getJSONArray("dependentTestcaseData");
		
		List<DependentTestcase>dependentTestcaseList = new ArrayList<DependentTestcase>();
		
		for(int i=0; i<jsonArray.length(); i++) {
			
			JSONObject dependentTestcaseData = jsonArray.getJSONObject(i);
			JSONObject universeId = dependentTestcaseData.getJSONObject("universeId");
			JSONObject userId = dependentTestcaseData.getJSONObject("userId");
			
			log.info(dependentTestcaseData.toString());
			
			Universe universe = new Universe(universeId.toString());
			User user = new User(userId.toString());
			
			DependentTestcase dependentTestcase = new DependentTestcase(dependentTestcaseData.toString(),
					universe, user);
			
			dependentTestcaseList.add(dependentTestcase);
		}
		
		JSONArray archiveDependentTestcaseArray = jsonResponse.getJSONArray("dependentTestcaseArchive");
		List<DependentTestcase>archiveTestcase = new ArrayList<DependentTestcase>();
		
		for(int i=0; i<archiveDependentTestcaseArray.length(); i++) {
			
			JSONObject archiveData = archiveDependentTestcaseArray.getJSONObject(i);
			
			//JSONObject clientData = archiveData.getJSONObject("clientId");
			
			DependentTestcase dTestcase = new DependentTestcase();
			//dTestcase.setClientName(archiveData.getString("clientName"));
			dTestcase.setTestcaseNumber(archiveData.getString("testcaseNumber"));
			dTestcase.setDescription(archiveData.getString("description"));
			
			archiveTestcase.add(dTestcase);
		}
		
		mv.addObject("userId", userr.getUserId());
		mv.addObject("storyId", storyId);
		mv.addObject("storyRecord", dependentTestcaseList);
		mv.addObject("archiveData", archiveTestcase);
		mv.addObject("user", userr.getUsername());
		mv.addObject("Info", model.asMap().get("Info") != null ? model.asMap().get("Info"): null);
		mv.setViewName("adminreport");
		return mv;
	}
	
	public ModelAndView archiveDependentTestcase(String testcaseNumber, RedirectAttributes ra,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		String url = String.format("%s/%s", UrlBeans.DEPENDENT_TESTCASE_SERVICE, "archivedependenttestcase");
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("testcaseNumber", testcaseNumber);
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		if(clientResponse.getStatus() == 200) {
			ra.addFlashAttribute("Info", jsonResponse.get("Info"));
			mv.setViewName("redirect:/dependenttestcase/dependenttestcasepage/" + session.getAttribute("storyId"));
			return mv;
		}else {
			ra.addFlashAttribute("Error", jsonResponse.get("Error"));
			mv.setViewName("redirect:/dependenttestcase/dependenttestcasepage/" + session.getAttribute("storyId"));
			return mv;
		}
		
	}
	
	public ModelAndView setModifyDependentTestcasePage(String testcaseNumber,
			Long storyId, HttpSession session, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		User user = (User)session.getAttribute("user");
		
		session.setAttribute("testcaseNumber", testcaseNumber);
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("testcaseNumber", testcaseNumber);
		
		String url = String.format("%s/%s", UrlBeans.DEPENDENT_TESTCASE_SERVICE, "getmodifytestcasedata");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info("Modify: " + response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		JSONArray jsonArray = jsonResponse.getJSONArray("templateDataList");
		
		List<TemplateData>templateDataList = new ArrayList<TemplateData>();
		
		for(int i=0; i<jsonArray.length(); i++) {
			
			JSONObject templateDataJson = jsonArray.getJSONObject(i);
			
			TemplateData templateData = new TemplateData();
			templateData.setTemplateDataId(templateDataJson.getLong("templateDataId"));
			templateData.setLabel(templateDataJson.getString("label"));
			templateData.setNatureOfAction(templateDataJson.getString("natureOfAction"));
			templateData.setScreenCapture(templateDataJson.getBoolean("screenCapture"));
			templateData.setTriggerEnter(templateDataJson.getBoolean("triggerEnter"));
			templateData.setWebElementName(templateDataJson.getString("webElementName"));
			templateData.setWebElementNature(templateDataJson.getString("webElementNature"));
			try {
				templateData.setInputOutputValue(templateDataJson.getString("inputOutputValue"));
			}catch (Exception e){
				//NO ACTION HERE
			}
			
			
			templateDataList.add(templateData);
		}
		
		JSONArray scriptListArray = jsonResponse.getJSONArray("scriptList");
		
		
		
		List<Script>scriptList = new ArrayList<Script>();
		for(int i=0; i<scriptListArray.length(); i++) {
			
			JSONObject scriptData = scriptListArray.getJSONObject(i);
			
			log.info(scriptData.toString());
			
			Script script = new Script();
			script.setDescription(scriptData.getString("description"));
			script.setName(scriptData.getString("name"));
			script.setScriptId(scriptData.getLong("scriptId"));
			
			scriptList.add(script);
		}
		
		
		mv.addObject("testcaseNumber", testcaseNumber);
		mv.addObject("description", jsonResponse.get("description"));
		mv.addObject("storyId", storyId);
		mv.addObject("user", user.getUsername());
		mv.addObject("scriptList", scriptList);
		mv.addObject("templateDataList", templateDataList);
		mv.addObject("isServerImport", jsonResponse.get("isServerImport"));
		mv.addObject("tapImportStatus", jsonResponse.get("tapImportStatus"));
		mv.setViewName("testcase");
		return mv;
	}
	
	public ModelAndView deleteEmbedScript(int rowCount, String testcaseNumber, HttpSession session) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("testcaseNumber", testcaseNumber);
		jsonData.put("rowCount", rowCount);
		
		String url = String.format("%s/%s", UrlBeans.DEPENDENT_TESTCASE_SERVICE, "deleteembedscript");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		log.info(response);
		
		mv.setViewName("redirect:/dependenttestcase/modifydependenttestcasepage/" + session.getAttribute("testcaseNumber") + "/" + session.getAttribute("storyId"));
		return mv;
		
	}
	
	public ModelAndView setAddScriptToTestcasePage(String testcaseNumber,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		User user = (User)session.getAttribute("user");
		
		session.setAttribute("testcaseNumber", testcaseNumber);
		
		Universe universe = (Universe)session.getAttribute("universe");
		
		String url = String.format("%s/%s?universeId=%d", UrlBeans.SCRIPT_SERVICE, "getscriptbyuniverseid", universe.getUniverseId());
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		log.info(response);
		JSONObject jsonResponse = new JSONObject(response);
		
		session.setAttribute("testcaseNumber", testcaseNumber);
		
		JSONArray scriptArray = jsonResponse.getJSONArray("scriptList");
		
		List<Script>scriptList = new ArrayList<Script>();
		for(int i=0; i<scriptArray.length(); i++) {
			JSONObject scriptData = scriptArray.getJSONObject(i);
			
			Script script = new Script();
			script.setScriptId(scriptData.getLong("scriptId"));
			script.setName(scriptData.getString("name"));
			script.setDescription(scriptData.getString("description"));
			
			scriptList.add(script);
		}
		
		mv.addObject("testcaseNumber", testcaseNumber);
		mv.addObject("scriptList", scriptList);
		mv.addObject("user", user.getUsername());
		mv.addObject("checkerTestcase", false);
		mv.setViewName("script");
		return mv;
	}
	
	public ModelAndView addScriptToTestcase(String scriptId, HttpSession session,
			RedirectAttributes ra) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		User user = (User)session.getAttribute("user");
		
		String url = String.format("%s/%s?scriptId=%s", UrlBeans.SCRIPT_SERVICE, "getsizeofscriptvariable", scriptId);
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		if(clientResponse.getStatus() == 200) {
			session.setAttribute("scriptId", scriptId);
			
			JSONObject jsonResponse = new JSONObject(response);
			
			JSONArray scriptVariableArray = jsonResponse.getJSONArray("scriptVariableList");
			
			if(scriptVariableArray.length() > 0) {
				List<ScriptVariable>scriptVariableList = new ArrayList<ScriptVariable>();
				for(int i=0; i<scriptVariableArray.length(); i++) {

					JSONObject scriptVariableData = scriptVariableArray.getJSONObject(i);
					
					ScriptVariable scriptVariable = new ScriptVariable();
					scriptVariable.setName(scriptVariableData.getString("name"));
					scriptVariable.setDescription(scriptVariableData.getString("description"));
					
					scriptVariableList.add(scriptVariable);
					
				}
				
				mv.addObject("scriptVariableList", scriptVariableList);
				mv.addObject("checkerTestcase", true);
				mv.addObject("user", user.getUsername());
				mv.setViewName("script");
				return mv;
			}else {
				
				JSONObject jsonData = new JSONObject();
				jsonData.put("scriptId", scriptId);
				jsonData.put("testcaseNumber", session.getAttribute("testcaseNumber"));
				
				url = String.format("%s/%s", UrlBeans.SCRIPT_SERVICE, "finalizeaddscript");
				
				client = new Client();
				webResource = client.resource(url);
				clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,jsonData.toString());
				
				response = new String(clientResponse.getEntity(String.class));
				
				log.info(response);
				
				jsonResponse = new JSONObject(response);
				
				ra.addFlashAttribute("Info", jsonResponse.get("Info"));
				mv.setViewName("redirect:/dependenttestcase/dependenttestcasepage/" + session.getAttribute("storyId"));
				return mv;
				
			}
		}else {
			
			JSONObject jsonResponse = new JSONObject(response);
			
			ra.addFlashAttribute("Error", jsonResponse.get("Error"));
			mv.setViewName("redirect:/dependenttestcase/dependenttestcasepage/" + session.getAttribute("storyId"));
			return mv;
		}
		
		
		
		
		
	}
	
	public ModelAndView addStoredValues(List<String>scriptVariableList,
			HttpSession session,
			RedirectAttributes ra) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("scriptVariableList", scriptVariableList);
		jsonData.put("scriptId", session.getAttribute("scriptId"));
		jsonData.put("testcaseNumber", session.getAttribute("testcaseNumber"));
		
		
		String url = String.format("%s/%s", UrlBeans.SCRIPT_SERVICE, "finalizeaddscript");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,  jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		
		JSONObject jsonResponse = new JSONObject(response);
		
		ra.addFlashAttribute("Info", jsonResponse.get("Info"));
		mv.setViewName("redirect:/dependenttestcase/dependenttestcasepage/" + session.getAttribute("storyId"));
		return mv;
		
	}
	
	public ModelAndView modifyTestcase(List<Long>templateDataId, List<String>inputOutputValue,
			String testcaseNumber, long storyId, HttpSession session, 
			String tapImportStatus, String description,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		log.info("Access:/Write csv");
		
		JSONObject credentials = new JSONObject();
		
		
		
		credentials.put("testcaseNumber", testcaseNumber);
		credentials.put("inputOutputValue", inputOutputValue);
		credentials.put("templateDataId", templateDataId);
		credentials.put("tapImportStatus", tapImportStatus);
		credentials.put("description", description);
		
		log.info(credentials);
		
		
		
		String urlPath = String.format("%s/%s", UrlBeans.DEPENDENT_TESTCASE_SERVICE, "modifydependenttestcase");
		
		Client client = new Client();
		WebResource webResource = client.resource(urlPath);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, credentials.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		ra.addFlashAttribute("Info", jsonResponse.get("Info"));
		mv.setViewName("redirect:/dependenttestcase/dependenttestcasepage/" + storyId);
		return mv;
	}
	
	public ModelAndView writeCsv(HttpSession session, List<String> inputOutputValue, 
		List<Long>templateDataId, String clientId, String webAddress, String loginAccountId, 
		String templateId, Long storyId, String description, String tapImportStatus,
		RedirectAttributes ra) {
		
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		log.info("Access:/Write csv");
		
		JSONObject credentials = new JSONObject();
		
		
		User user = (User)session.getAttribute("user");
		
		Universe universe = (Universe)session.getAttribute("universe");
		
		credentials.put("userId", user.getUserId());
		credentials.put("clientId", clientId);
		credentials.put("templateId", templateId);
		credentials.put("storyId", storyId);
		credentials.put("universeId", universe.getUniverseId());
		credentials.put("webAddress", webAddress);
		credentials.put("loginAccountId", loginAccountId);
		credentials.put("description", description);
		credentials.put("inputOutputValue", inputOutputValue);
		credentials.put("templateDataId", templateDataId);
		credentials.put("tapImportStatus", tapImportStatus == null ? "-1" : tapImportStatus);
		
		log.info(credentials);
		
		
		
		String urlPath = String.format("%s/%s", UrlBeans.DEPENDENT_TESTCASE_SERVICE, "createdependenttestcase");
		
		Client client = new Client();
		WebResource webResource = client.resource(urlPath);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, credentials.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		if(clientResponse.getStatus() == 200) {
			JSONObject jsonResponse = new JSONObject(response);
			
			ra.addFlashAttribute("Info", jsonResponse.get("Info"));
			mv.setViewName("redirect:/dependenttestcase/dependenttestcasepage/" + storyId);
			return mv;
		}else {
			JSONObject jsonResponse = new JSONObject(response);
			
			ra.addFlashAttribute("Error", jsonResponse.get("Error"));
			mv.setViewName("redirect:/dependenttestcase/dependenttestcasepage/" + storyId);
			return mv;
		}
		
		
		
	}
	
	public ModelAndView retrieveArchivedDependentTestcase(List<String>testcaseNumber,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		JSONArray jsonArray = new JSONArray();
		
		for(String id : testcaseNumber) {
			jsonArray.put(id);
		}
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("testcaseNumberList", jsonArray);
		
		
		String url = String.format("%s/%s", UrlBeans.ARCHIVE_SERVICE, "reverseArchiveDependentTestcase");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		//JSONObject jsonResponse = new JSONObject(response);
		
		mv.setViewName("redirect:/dependenttestcase/dependenttestcasepage/" + session.getAttribute("storyId"));
		return mv;
	}
	
	
	public ModelAndView setDashboardPage(HttpSession session) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		User user = (User)session.getAttribute("user");
		
		
		String url = String.format("%s/%s", UrlBeans.DEPENDENT_TESTCASE_SERVICE, "getdependenttestcase");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		//JSONObject jsonResponse = new JSONObject(response);
		
		//JSONArray jsonArray = jsonResponse.getJSONArray("dependentTestcaseData");
		
		mv.addObject("user", user.getUsername());
		return mv;
	}
	
	public void processDownload(List<String>fileList, HttpSession session) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("recordId", fileList);
		
		String url = String.format("%s/%s", UrlBeans.TESTCASE_RECORD_SERVICE, "downloadtestcaserecord");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
	}
	
	public ModelAndView setOneLogPage(HttpSession session) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		Universe universe = (Universe)session.getAttribute("universe");
		User user = (User)session.getAttribute("user");
		
		String url = String.format("%s/%s?username=%s&universeId=%d", UrlBeans.DEPENDENT_TESTCASE_SERVICE, "gettestcasebyusername", user.getUsername(), universe.getUniverseId());
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonObject = new JSONObject(response);
		JSONArray jsonArrayUsername = jsonObject.getJSONArray("dependentTestcaseUsernameList");
		JSONArray jsonArrayUniverse = jsonObject.getJSONArray("dependentTestcaseUniverseList");
		
		if(user.getUserType().equals("superuser")) {
			List<TestcaseRecord>dependentTestcaseList = new ArrayList<TestcaseRecord>();
			for(int i=0; i<jsonArrayUniverse.length(); i++) {
				
				JSONObject dTestcaseObject = jsonArrayUniverse.getJSONObject(i);
				JSONObject clientName = dTestcaseObject.getJSONObject("clientId");
				JSONObject userName = dTestcaseObject.getJSONObject("userId");
				JSONObject testcaseNumber = dTestcaseObject.getJSONObject("testcaseNumber");
				
				User userCredential = new User();
				userCredential.setUsername(userName.getString("username"));
				
				TestcaseRecord testcaseRecord = new TestcaseRecord();
				testcaseRecord.setRecordId(dTestcaseObject.getLong("recordId"));
				testcaseRecord.setExecutionVersion(dTestcaseObject.getInt("executionVersion"));
				testcaseRecord.setClientName(clientName.getString("clientName"));
				testcaseRecord.setUserId(userCredential);
				testcaseRecord.setDescription(dTestcaseObject.getString("description"));
				testcaseRecord.setTestcaseNumber(testcaseNumber.getString("testcaseNumber"));
				testcaseRecord.setStatus(dTestcaseObject.getString("status"));
				
				DependentTestcase dTestcase = new DependentTestcase();
				dTestcase.setClientName(clientName.getString("clientName"));
				dTestcase.setDescription(dTestcaseObject.getString("description"));
				dTestcase.setExecutionVersionCurrent(dTestcaseObject.getInt("executionVersion"));
				dTestcase.setStatus(dTestcaseObject.getString("status"));
				dTestcase.setTestcaseNumber(testcaseNumber.getString("testcaseNumber"));
				dTestcase.setUserId(userCredential);
				
				dependentTestcaseList.add(testcaseRecord);
			}
			mv.addObject("user", user.getUsername());
			mv.addObject("userType", user.getUserType());
			mv.addObject("dependentTestcaseList", dependentTestcaseList);
		}else {
			
			List<TestcaseRecord>dependentTestcaseList = new ArrayList<TestcaseRecord>();
			for(int i=0; i<jsonArrayUsername.length(); i++) {
				
				JSONObject dTestcaseObject = jsonArrayUniverse.getJSONObject(i);
				JSONObject clientName = dTestcaseObject.getJSONObject("clientId");
				JSONObject userName = dTestcaseObject.getJSONObject("userId");
				JSONObject testcaseNumber = dTestcaseObject.getJSONObject("testcaseNumber");
				
				User userCredential = new User();
				userCredential.setUsername(userName.getString("username"));
				
				TestcaseRecord testcaseRecord = new TestcaseRecord();
				testcaseRecord.setRecordId(dTestcaseObject.getLong("recordId"));
				testcaseRecord.setExecutionVersion(dTestcaseObject.getInt("executionVersion"));
				testcaseRecord.setClientName(clientName.getString("clientName"));
				testcaseRecord.setUserId(userCredential);
				testcaseRecord.setDescription(dTestcaseObject.getString("description"));
				testcaseRecord.setTestcaseNumber(testcaseNumber.getString("testcaseNumber"));
				testcaseRecord.setStatus(dTestcaseObject.getString("status"));
				
		
				
				dependentTestcaseList.add(testcaseRecord);
			}
			mv.addObject("user", user.getUsername());
			mv.addObject("userType", user.getUserType());
			mv.addObject("dependentTestcaseList", dependentTestcaseList);
			
		}
		mv.setViewName("userreport");
		return mv;
	}
	
}
