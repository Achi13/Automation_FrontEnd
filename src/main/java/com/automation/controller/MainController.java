package com.automation.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.domain.Entity;
import com.automation.domain.StoragePath;
import com.automation.domain.UserDomain;
import com.automation.service.AutomationService;
import com.automation.service.CrudService;
import com.automation.session.UserSession;
import com.automation.user.domain.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Controller
public class MainController {
	
	@Autowired
	AutomationService automationService;
	
	@Autowired
	CrudService crudService;
	
	private final String HTML_LOCATION = "C:\\Users\\rtsbondoc\\Desktop\\html";
	private final String SERVER_TIMEOUT = "5000";
	private final String LOCATION = StoragePath.TEMPLATE_HOLDER_LOCATION;
	
	
	
	//FOR SUBMITTING DATA FROM EXISTING TEMPLATE
	@RequestMapping("/verifydata")
	public ModelAndView verifyData(@RequestParam("webElementName") List<String> webElementName,
			@RequestParam("webElementNature")List<String> webElementNature,
			@RequestParam("natureOfAction")List<String> natureOfActiom,
			@RequestParam("triggerEnterValue")List<String> triggerEnter,
			@RequestParam("inputOutput")List<String> inputOut,
			@RequestParam("notes")List<String> notes,
			@RequestParam("screenCaptureValue") List<String>screenCapture,
			@RequestParam("serverImport")String serverImport,
			@RequestParam("transactiontype")String transactionType,
			@RequestParam("clientName")String clientName,
			@RequestParam("severity")String severity,
			@RequestParam("description")String description,
			HttpSession session) throws IOException, URISyntaxException {
		
		ModelAndView mv = new ModelAndView();
		mv = automationService.writeCSV(webElementName, webElementNature, natureOfActiom, triggerEnter, inputOut, notes, screenCapture, serverImport, 
				session,transactionType, clientName,severity,description);
		Logger log = Logger.getLogger(getClass());
		log.info(mv);
		
		return mv;
	}
	
	@RequestMapping("/verifycampaign")
	public ModelAndView verifyCampaign(@RequestParam("webElementName") List<String> webElementName,
			@RequestParam("webElementNature")List<String> webElementNature,
			@RequestParam("natureOfAction")List<String> natureOfActiom,
			@RequestParam("triggerEnterValue")List<String> triggerEnter,
			@RequestParam("inputOutput")List<String> inputOut,
			@RequestParam("notes")List<String> notes,
			@RequestParam("screenCaptureValue") List<String>screenCapture,
			@RequestParam("serverImport")String serverImport,
			@RequestParam("transactiontype")String transactionType,
			@RequestParam("clientName")String clientName,
			@RequestParam("severity")String severity,
			@RequestParam("description")String description,
			@RequestParam("storyid")int storyId,
			HttpSession session) throws IOException, URISyntaxException {
		
		ModelAndView mv = new ModelAndView();
		mv = automationService.verifyCampaign(webElementName, webElementNature, natureOfActiom, triggerEnter, inputOut, notes, screenCapture, serverImport, 
				session,transactionType, clientName,severity,description, storyId);
		Logger log = Logger.getLogger(getClass());
		log.info(mv);
		
		return mv;
	}
	
	//FOR SUBMITTING DATA FROM NEW TEMPLATE
	@RequestMapping("/verifydatanew")
	public ModelAndView verifyDataNew(@RequestParam("webElementName") List<String> webElementName,
			@RequestParam("webElementNature")List<String> webElementNature,
			@RequestParam("natureOfAction")List<String> natureOfActiom,
			@RequestParam("triggerEnterValue")List<String> triggerEnter,
			@RequestParam("inputOutput")List<String> inputOut,
			@RequestParam("screenCaptureValue")List<String>screenCapture,
			@RequestParam("notes")List<String> notes,
			@RequestParam("clientName")String clientName,
			@RequestParam("orderType")String orderType,
			@RequestParam("natureOfOrder")String natureOfOrder,
			@RequestParam("serverImport")String serverImport,
			@RequestParam("severity")String severity,
			@RequestParam("description")String description,
			HttpSession session,
			RedirectAttributes ra) throws IOException{
		
		ModelAndView mv = new ModelAndView();
		mv = automationService.writeCSVFromNewTemplate(webElementName, webElementNature, natureOfActiom, triggerEnter, inputOut, notes, 
				screenCapture, clientName,orderType,natureOfOrder, serverImport, severity, session, ra, description);
		return mv;
	}
	
	//CHECKING THE CONTENTS OF THE TEMPLATE SELECTED
	@RequestMapping("/checktemplate")
	public ModelAndView pageExTeplate(@RequestParam("file")String file,HttpSession session,
			@RequestParam(value="status", required=false)String status,
			@RequestParam(value="storyid", required=false)Integer storyId) throws IOException {
		
		
		ModelAndView mv = new ModelAndView();
		
		if(storyId == null) {
			mv = automationService.checkTemplate(file, session, status, null);
		}else {
			mv = automationService.checkTemplate(file, session, status, storyId);
		}
		
		
		
		return mv;
	}
	
	
	@RequestMapping("/processregister")
	public ModelAndView processRegister(@RequestParam("useraccess") String userAccess,
			@RequestParam("uname")String uname,
			@RequestParam("pass")String pass,
			@RequestParam("status")String status,
			RedirectAttributes ra) throws IOException {
		ModelAndView mv = new ModelAndView();
		mv = crudService.processRegister(userAccess, uname, pass, status, ra);
		return mv;
	}
	
	//FOR PROCESSING OF LOGIN
	@RequestMapping("/processlogin")
	public ModelAndView processLogin(@RequestParam(value = "uname")String uname, 
			@RequestParam(value = "pass")String pass,
			HttpSession session,
			RedirectAttributes ra) throws IOException {
		ModelAndView mv = new ModelAndView();
		mv = crudService.processLogin(uname, pass,session, ra);
		return mv;
	}
	
	@RequestMapping("/processupdate")
	public ModelAndView processUpdate(@RequestParam("userid")int userId,
			@RequestParam("useraccess")String userAccess,
			@RequestParam("uname")String uname,
			@RequestParam("pass")String pass,
			@RequestParam("status")String status,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = crudService.processUpdate(userId, userAccess, uname, pass, status, ra);
		return mv;
	}
	
	@RequestMapping("/processdelete/{userId}")
	public ModelAndView processDelete(@PathVariable("userId")int userId,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = crudService.processDelete(userId,ra);
		return mv;
	}
	
	//FOR REDIRECTING TO UPDATE USER PAGE
	@RequestMapping("/update/{userId}")
	public ModelAndView updatePage(@PathVariable("userId")int userId,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = crudService.updatePage(userId, session, ra);
		return mv;
	}
	
	@RequestMapping("/editsubmittedrequest/{testcase}")
	public ModelAndView editSubmittedRequest(@PathVariable("testcase")String testcase,
			HttpSession session) throws IOException {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.editSubmittedRequest(testcase, session);
		
		return mv;
	}
	
	//FOR REDIRECTING TO REPORTS
	@HystrixCommand(fallbackMethod="serverNotResponding", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", 
					value = SERVER_TIMEOUT)})
	@RequestMapping("/seereports")
	public String seeReport(HttpSession session, RedirectAttributes ra, Model model) {
		String mv = "";
		if(session.getAttributeNames().hasMoreElements()) {
			for(UserDomain userDomain: UserSession.getInstance().getUserDomain()) {
				if(userDomain.getSessionId().equals(session.getId())) {
					if(userDomain.getUserAccess().equals("superuser")) {
						ra.addFlashAttribute("Info", model.asMap().get("Info"));
						mv = "redirect:/adminpage";
						return mv;
					}else if(userDomain.getUserAccess().equals("user")) {
						ra.addFlashAttribute("Info", model.asMap().get("Info"));
						mv="redirect:/userpage";
						return mv;
					}else {
						ra.addFlashAttribute("Error", "Invalid user access! Please contact the dev. for the problem");
						mv="redirect:/login";
						return mv;
					}
				}
			}
		}else {
			ra.addFlashAttribute("Error", "Authorization breach!");
			mv="redirect:/login";
			return mv;
		}
		return mv;
	}
	
	@RequestMapping("/dashboard")
	public ModelAndView dashboardPage(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.dashboardPage(session);
		
		return mv;
	}
	
	@RequestMapping("/executecampaign/{campaignid}/{execution}")
	public ModelAndView executeCampaign(@PathVariable("campaignid")int campaignId,
			@PathVariable("execution")String execution,
			HttpSession session,
			RedirectAttributes ra) throws SQLException {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.executeCampaign(campaignId, session, execution, ra);
		
		return mv;
	}
	
	@RequestMapping("/campaign")
	public ModelAndView campaign(HttpSession session, RedirectAttributes ra,
			Model model) {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.viewCampaign(session, ra, model);
		
		
		return mv;
	}
	
	@RequestMapping("/createcampaign")
	public ModelAndView createCampaign(@RequestParam("campaignname")String campaignName,
			@RequestParam("description")String description,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.createCampaign(session, campaignName, description);
		
		return mv;
	}
	
	@RequestMapping("/modifycampaign")
	public ModelAndView modifyCampaign(@RequestParam("campaignname")String campaignName,
			@RequestParam("campaignid")int campaignId,
			@RequestParam("description")String description) {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.modifyCampaign(campaignName, campaignId, description);
		
		return mv;
	}
	
	@RequestMapping("/theme/{campaignId}")
	public ModelAndView theme(@PathVariable("campaignId")int campaignId,
			HttpSession session,
			Model model) {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.viewTheme(campaignId, session, model);
		
		return mv;
	}
	
	@RequestMapping("/createtheme")
	public ModelAndView createTheme(@RequestParam("themename")String themeName,
			@RequestParam("description")String description,
			@RequestParam("campaignid")int campaignId,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.createTheme(session, themeName, description, campaignId, ra);
		
		return mv;
	}
	
	@RequestMapping("/modifytheme")
	public ModelAndView modifyTheme(@RequestParam("themename")String themeName,
			@RequestParam("themeid")int themeId,
			@RequestParam("description")String description,
			@RequestParam("campaignid")String campaignId) {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.modifyTheme(themeName, themeId, description, campaignId);
		
		return mv;
	}
	
	@RequestMapping("/story/{themeId}")
	public ModelAndView story(@PathVariable("themeId")int themeId,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.viewStory(themeId, session);
		
		return mv;
	}
	
	@RequestMapping("/createstory")
	public ModelAndView createStory(@RequestParam("storyname")String storyName,
			@RequestParam("description")String description,
			@RequestParam("themeid")int themeId,
			@RequestParam("serverimport")String serverImport,
			@RequestParam("severity")String severity,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.createStory(session, storyName, description, themeId, ra, serverImport, severity);
		
		return mv;
	}
	
	@RequestMapping("/modifystory")
	public ModelAndView modifyStory(@RequestParam("storyid")int storyId,
			@RequestParam("storyname")String storyName,
			@RequestParam("description")String description,
			@RequestParam("themeid")String themeId,
			@RequestParam("serverimport")String serverImport,
			@RequestParam("severity")String ignoreSeverity) {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.modifyStory(storyId, storyName, description, themeId, serverImport, ignoreSeverity);
		
		return mv;
	}
	
	
	@RequestMapping("/dependentrecord/{storyId}")
	public ModelAndView dependentRecord(@PathVariable("storyId")int storyId,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.viewDependentRecord(storyId, session);
		
		return mv;
	}
	
	@RequestMapping("/modifydependent")
	public ModelAndView modifyDependent(@RequestParam("webElementName") List<String> webElementName,
			@RequestParam("webElementNature")List<String> webElementNature,
			@RequestParam("natureOfAction")List<String> natureOfActiom,
			@RequestParam("triggerEnterValue")List<String> triggerEnter,
			@RequestParam("inputOutput")List<String> inputOut,
			@RequestParam("notes")List<String> notes,
			@RequestParam("screenCaptureValue") List<String>screenCapture,
			@RequestParam("serverImport")String serverImport,
			@RequestParam("transactiontype")String transactionType,
			@RequestParam("clientName")String clientName,
			@RequestParam("severity")String severity,
			@RequestParam("description")String description,
			@RequestParam("testcase")String testcase,
			HttpSession session) throws IOException, URISyntaxException {
		
		ModelAndView mv = new ModelAndView();
		mv = automationService.modifyDependent(webElementName, webElementNature, natureOfActiom, triggerEnter, inputOut, notes, screenCapture, serverImport, 
				session,transactionType, clientName,severity,description, testcase);
		Logger log = Logger.getLogger(getClass());
		log.info(mv);
		
		return mv;
	}
	
	//FOR PROCESSING LOGOUT
	@HystrixCommand(fallbackMethod="serverNotResponding", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", 
					value = SERVER_TIMEOUT)})
	@RequestMapping("/logout")
	public String processLogout(HttpSession session, RedirectAttributes ra) throws InterruptedException {
		String mv = "";
		
		User userSession = (User)session.getAttribute("user");
		
		Logger log = Logger.getLogger(getClass());
		log.info(session.getId());
		if(session.getAttributeNames().hasMoreElements() && !(com.automation.user.session.UserSession.getInstance().getUserList().size() == 0)) {
			int counter = 0;
			for (User user : com.automation.user.session.UserSession.getInstance().getUserList()) {
				if(user.getUsername().equals(userSession.getUsername())) {
					com.automation.user.session.UserSession.getInstance().getUserList().remove(counter);
					mv = "redirect:/";
					session.invalidate();
					session = null;
					return mv;
				}else {
					counter++;
				}
			}
		}else if(UserSession.getInstance().getUserDomain().size() == 0){
			//ra.addFlashAttribute("Error", "Server refreshed. Please Login again");
			return "redirect:/login";
		}else {
			return "redirect:/login";
		}
		
		
		return mv;
	}
	
	@RequestMapping("/checkextent")
	public String checkExtent(HttpSession session, RedirectAttributes ra) {
		String mv = "";
		
		Logger log = Logger.getLogger(getClass());
		int counter = 0;
		for (UserDomain userDomain : UserSession.getInstance().getUserDomain()) {
			if(userDomain.getSessionId().equals(session.getId())) {
				
				if(userDomain.getUserAccess().equals("superuser")) {
					mv = "redirect:" + HTML_LOCATION + "\\superuser.html";
					return mv;
				}else if(userDomain.getUserAccess().equals("user")) {
					mv = "redirect:" + HTML_LOCATION + "\\" + session.getAttribute("uname") + ".html";
					return mv;
				}else {
					ra.addFlashAttribute("Error", "Invalid user access! Please contact the dev. for the problem");
					mv = "redirect:/home";
					return mv;
				}
			}else {
				counter++;
			}
		}
		return mv;
	}
	
	@RequestMapping("/submitrequest")
	public ModelAndView SubmitRequest(@RequestParam("schedule")List<String> schedule,
			@RequestParam("testcase")List<String>testcase,
			//sample code
			HttpSession session) throws IOException {
		ModelAndView mv = new ModelAndView();
		mv = automationService.sendBulkRequest(schedule, testcase, session);
		return mv;
	}
	
	@RequestMapping("/getreports")
	public ModelAndView getReportsPage(HttpSession session, Model model) {
		ModelAndView mv = new ModelAndView();
		if(session.getAttributeNames().hasMoreElements()) {
			mv = automationService.setReportPage(session);
			return mv;
		}else {
			mv.addObject("Error", "Authorization Breach!");
			mv.setViewName("redirect:/login");
			return mv;
		}
	}
	
	@RequestMapping("/editrequestpage/{testcase}")
	public ModelAndView editRequestPage(@PathVariable("testcase")String testcase,
			HttpSession session) throws IOException {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.editRequestPage(testcase, session);
		
		return mv;
	}
	
	@RequestMapping("/deleterequest/{testcase}")
	public ModelAndView deleteRequest(@PathVariable("testcase")String testcase,
			HttpSession session,
			RedirectAttributes ra) throws IOException {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.deleteRequest(testcase, session, ra);
		
		return mv;
	}
	
	@RequestMapping("/verifymodify")
	public ModelAndView verifyModify(@RequestParam("webElementName") List<String> webElementName,
			@RequestParam("webElementNature")List<String> webElementNature,
			@RequestParam("natureOfAction")List<String> natureOfActiom,
			@RequestParam("triggerEnterValue")List<String> triggerEnter,
			@RequestParam("inputOutput")List<String> inputOut,
			@RequestParam("notes")List<String> notes,
			@RequestParam("screenCaptureValue") List<String>screenCapture,
			@RequestParam("serverImport")String serverImport,
			@RequestParam("transactiontype")String transactionType,
			@RequestParam("clientName")String clientName,
			@RequestParam("severity")String severity,
			@RequestParam("testcase")String testcase,
			@RequestParam("assignedAccount")String assignedAccount,
			@RequestParam("sender")String sender,
			@RequestParam("description")String description,
			HttpSession session,
			RedirectAttributes ra) throws IOException {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.verifyModify(webElementName, webElementNature, natureOfActiom, triggerEnter, inputOut, notes, screenCapture, serverImport, transactionType, clientName, severity, testcase, assignedAccount, sender, session, ra, description);
		
		return mv;
		
	}
	
	@RequestMapping("/processdownload")
	public void processDownload(@RequestParam("file")List<String> file, HttpSession session,
			RedirectAttributes ra,
			HttpServletResponse response) throws IOException {
		
		automationService.processDownload(file, session, ra, response);
	}
	
	@RequestMapping("/processarchive/{testcase}")
	public ModelAndView processArchive(@PathVariable("testcase")String testcase,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		
		mv = automationService.processArchive(testcase, session, ra);
		
		return mv;
	}
	
	//HYSTRIX FALLBACK(HttpSession session)
	public String serverNotResponding(HttpSession session, RedirectAttributes ra) {
		ra.addFlashAttribute("Error", "Server not responding...");
		return "redirect:/";
	}
	
	
	
	
}
