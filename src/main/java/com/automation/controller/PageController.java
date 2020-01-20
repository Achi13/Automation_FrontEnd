package com.automation.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.campaign.campaign;
import com.automation.db.campaign.Campaign;
import com.automation.db.campaign.repository.CampaignRepository;
import com.automation.db.repository.UserRepository;
import com.automation.domain.FileEntity;
import com.automation.domain.StoragePath;
import com.automation.domain.ViewElements;
import com.automation.service.AutomationService;
import com.automation.user.domain.User;
import com.automation.user.session.UserSession;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;


@Controller
public class PageController {
	
	private final String SERVER_TIMEOUT = "5000";
	private final String LOCATION = StoragePath.TEMPLATE_HOLDER_LOCATION;
	private final String USER_FOLDER_LOCATION = StoragePath.USER_FOLDER_LOCATION;
	
	
	@Autowired
	private AutomationService automationService;
	
	@Autowired
	private CampaignRepository campaignRepo;
	
	@Autowired
	UserRepository userRepo;
	
	
	
	@RequestMapping("/trythis")
	public String tryThis(Model model) {
		
		Campaign campaign = campaignRepo.findCampaignByCampaignId(1);
		
		campaign campaignJr = new campaign(Integer.toString(campaign.getCampaignId()), StoragePath.TODO_FOLDER_LOCATION, StoragePath.CSVHOLDER_FOLDER);
		campaignJr.start();
		
		model.addAttribute("Info", "Thread Running...");
		return "home";
	}
	
	@RequestMapping("/calendar")
	public String calendarPage(Model model, HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		
		model.addAttribute("user", user.getUsername());
		return "calendar";
	}
	
	@RequestMapping("/index")
	public String pageIndex(Model model, HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		
		if(session.getAttributeNames().hasMoreElements()) {
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
			model.addAttribute("Info", model.asMap().get("Info") != null ? model.asMap().get("Info") : null);
			model.addAttribute("fileinfo", csvHolder);
			model.addAttribute("user", user.getUsername());
			return "index";
		}else {
			model.addAttribute("Error", "Authorization breach!");
			return "login";
		}
	
	}
	
	@RequestMapping("/officerpage")
	public String officerPage(Model model, HttpSession session) {
		model.addAttribute("users", automationService.getUsers());
		model.addAttribute("user", session.getAttribute("uname"));
		return "securityofficer";
	}
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String mainPage(HttpSession session, Model model, RedirectAttributes ra) {
		Logger log = Logger.getLogger(getClass());
		if(session.isNew() || UserSession.getInstance().getUserList().size() == 0) {
			return "redirect:/login";
		}else if(!session.getAttributeNames().hasMoreElements() || UserSession.getInstance().getUserList().size() == 0){
			ra.addFlashAttribute("Error", "Server refreshed. Please Login again");
			return "redirect:/login";
		}else if(UserSession.getInstance().getUserList().size() == 0) {
			ra.addFlashAttribute("Error", "Server refreshed. Please Login again");
			return "redirect:/login";
		}else {
			return "redirect:/home";
		}
	}
	
	@RequestMapping("/universe")
	public String universePage() {
		return "universe";
	}
	
	@RequestMapping("/login")
	public String loginPage(Model model, HttpSession session) {
		
		if((session.getAttributeNames().hasMoreElements() && Objects.nonNull(session.getAttribute("universe"))) && UserSession.getInstance().getUserList().size() > 0) {
			return "redirect:/home";
		}else {
			model.addAttribute("Error", model.asMap().get("Error"));
			return "login1";
		}
		
	}
	
	@RequestMapping("/accountsettings")
	public String accountsettingsPage() {
		return "accountsettings";
	}
	
	@RequestMapping("/register")
	public String pageRegister(Model model) {
		return "registerpage";
	}
	
	@RequestMapping("/registration")
	public String registrationPage(Model model) {
		return "registration";
	}
	
	
	@RequestMapping("/client")
	public String clientPage() {
		return "client";
	}
	
	@RequestMapping("/webaddress")
	public String webaddressPage() {
		return "webaddress";
	}
	
	@RequestMapping("/tap")
	public String tapsPage() {
		return "tap";
	}
	
	@RequestMapping("/summary")
	public String summaryPage() {
		return "summary";
	}
	
	@RequestMapping("/extemplate")
	public String templatePage() {
		return "extemplate";
	}
	
	@RequestMapping("/testcase")
	public String testcasePage() {
		return "testcase";
	}
	
	@RequestMapping("/logincredential")
	public String logincredentialPage() {
		return "logincredential";
	}
	
	@HystrixCommand(fallbackMethod="serverNotResponding", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", 
					value = SERVER_TIMEOUT)})
	@RequestMapping("/return")
	public String pageReturn(Model model) {
		Logger log = Logger.getLogger(getClass());
		return "redirect:/index";
	}
	
	
	@RequestMapping("/home")
	public String pageHome(Model model, HttpSession session, RedirectAttributes ra) {
		User user = (User) session.getAttribute("user");
		Logger log = Logger.getLogger(getClass());
		if(session.getAttributeNames().hasMoreElements() && Objects.nonNull(session.getAttribute("universe")) && !(UserSession.getInstance().getUserList().size() == 0)) {
			if(user.getUserType().toString().equals("superuser") || user.getUserType().toString().equals("user")) {
				log.info(UserSession.getInstance().getUserList().size());
				model.addAttribute("user", user.getUsername());
				model.addAttribute("access", user.getUserType());
				ra.addFlashAttribute("Error", model.asMap().get("Error"));
				return "home";
			}else {
				ra.addFlashAttribute("Error", "Please contact security officer for this matter.");
				return "redirect:/login";
			}
			
		}else if(UserSession.getInstance().getUserList().size() == 0) {
			ra.addFlashAttribute("Error", "Server refreshed. Please Login again");
			return "redirect:/login";
		}else {
			ra.addAttribute("Error", "Authorization breach!");
			return "redirect:/login";
		}
	}
	
	
	@RequestMapping("/viewfiles")
	public String userRequest(HttpSession session, Model model) throws JSONException, IOException {
		String jsonUrl = String.format("%s.json", session.getAttribute("uname"));
		List<ViewElements>elementList = new ArrayList<ViewElements>();
		Logger log = Logger.getLogger(getClass());
		if(session.getAttributeNames().hasMoreElements()) {
			if(new File(jsonUrl).exists()) {
				FileReader fr = new FileReader(new File(jsonUrl));
				BufferedReader br = new BufferedReader(fr);
				JSONObject jsonObject = new JSONObject(br.readLine());
				br.close();
				fr.close();
				if(getUserFiles(USER_FOLDER_LOCATION + session.getAttribute("uname").toString()).size() > 0) {
					for(String file : getUserFiles(USER_FOLDER_LOCATION + session.getAttribute("uname").toString())) {
						
						JSONArray jsonHolder = (JSONArray) jsonObject.get(file);
						ViewElements elements = new ViewElements(jsonHolder);
						elementList.add(elements);
					}
				}
			}else {
				model.addAttribute("elementList", elementList);
				model.addAttribute("user", session.getAttribute("uname"));
				model.addAttribute("Info", model.asMap().get("Info"));
				return "viewfile";
			}
			model.addAttribute("elementList", elementList);
			model.addAttribute("user", session.getAttribute("uname"));
			model.addAttribute("Info", model.asMap().get("Info"));
			return "viewfile";
		}else {
			model.addAttribute("Error", "Authorization breach!");
			return "redirect:/login";
		}
		
		
	}
	
	@HystrixCommand(fallbackMethod="serverNotResponding", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", 
					value = SERVER_TIMEOUT)})
	@RequestMapping("/newtemplate")
	public String pageNewTemplate(Model model, HttpSession session) {
		
		if(session.getAttributeNames().hasMoreElements()) {
			model.addAttribute("Info", model.asMap().get("Info"));
			model.addAttribute("user", model.asMap().get("user"));
			model.addAttribute("user", session.getAttribute("uname"));
			return "newtemplate";
		}else {
			model.addAttribute("Error", "Authorization breach!");
			return "login1";
		}
	}
	
	//PRIVATE FUNCTIONS FOR SERVICE FUNCTIONS
	
	//FOR LIST COUNTING
		private int getFileEntityListCount(String url) {
			File location = new File(url);
			
			List<FileEntity> csvHolder = new ArrayList<>();
			FileEntity data;
			if(location.listFiles().length == 0) {
				return 0;
			}else {
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
				return csvHolder.size();
			}
			
		}
		
		//FOR LIST COUNTING
		private List<String> getUserFiles(String url) {
			File location = new File(url);
			
			List<String>userFiles = new ArrayList<String>();
			
			for (File file:location.listFiles()) {
				String[] fileHolder = file.getName().split("\\.");
				userFiles.add(fileHolder[0]);
			}
			return userFiles;
		}
		
		private int getUserFileList(String url) {
			
			File location = new File(url);
			if(location.exists()) {
				List<File> csvHolder = new ArrayList<>();
				
				for(File file: location.listFiles()) {
					csvHolder.add(file);
				}
				
				return csvHolder.size();
			}else {
				return 0;
			}
		}
		
	
	//HYSTRIX FALLBACK METHODS
	
	//HYSTRIX FALLBACK (Model model)
	public String serverNotResponding(Model model, HttpSession session) {
		model.addAttribute("Error", "Server not responding...");
		return "redirect:/";
	}
	
	//HYSTRIX FALLBACK (Model model)
		public String serverNotResponding(Model model) {
			model.addAttribute("Error", "Server not responding...");
			return "redirect:/";
		}
	
	//HYSTRIX FALLBACK()
	public String serverNotResponding() {
		ModelMap model = new ModelMap();
		model.addAttribute("Error", "Server not responding...");
		return "login1";
	}
	
	
	
}
