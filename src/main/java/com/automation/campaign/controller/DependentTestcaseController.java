package com.automation.campaign.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.campaign.service.DependentTestcaseService;

@Controller
@RequestMapping("/dependenttestcase")
public class DependentTestcaseController {
	
	@Autowired
	DependentTestcaseService dependentTestcaseService;
	
	@RequestMapping("/dependenttestcasepage/{storyId}")
	public ModelAndView dependentTestcasePage(@PathVariable("storyId")long storyId,
			HttpSession session, Model model) {
		ModelAndView mv = new ModelAndView();
		mv = dependentTestcaseService.setDependentTestcasePage(storyId, session, model);
		return mv;
	}
	
	@RequestMapping("/modifydependenttestcasepage/{testcaseNumber}/{storyId}")
	public ModelAndView modifyDependentTestcase(@PathVariable("testcaseNumber")String testcaseNumber,
			@PathVariable("storyId")Long storyId,
			HttpSession session, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = dependentTestcaseService.setModifyDependentTestcasePage(testcaseNumber, storyId, session, ra);
		return mv;
	}
	
	@RequestMapping("/processdownload")
	public void processDownload(@RequestParam("file")List<String>fileList,
			HttpSession session) {
		dependentTestcaseService.processDownload(fileList, session);
	}
	
	@RequestMapping("/addstoredvalues")
	public ModelAndView addStoredValues(@RequestParam("input")List<String>scriptVariableList,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = dependentTestcaseService.addStoredValues(scriptVariableList, session, ra);
		return mv;
	}
	
	@RequestMapping("/deleteembedscript/{rowCount}/{testcaseNumber}")
	public ModelAndView deleteEmbedScript(@PathVariable("rowCount")int rowCount,
			@PathVariable("testcaseNumber")String testcaseNumber,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = dependentTestcaseService.deleteEmbedScript(rowCount, testcaseNumber, session);
		return mv;
	}
	
	@RequestMapping("/addscripttotestcasepage/{testcaseNumber}")
	public ModelAndView addScriptToTestcasePage(@PathVariable("testcaseNumber")String testcaseNumber,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = dependentTestcaseService.setAddScriptToTestcasePage(testcaseNumber, session);
		return mv;
	}
	
	@RequestMapping("/addscripttotestcase")
	public ModelAndView addScriptToTestcase(@RequestParam("scriptId")String scriptId,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = dependentTestcaseService.addScriptToTestcase(scriptId, session, ra);
		return mv;
	}
	
	@RequestMapping("/setreportlogpage")
	public ModelAndView setReportLogPage(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = dependentTestcaseService.setOneLogPage(session);
		return mv;
	}
	
	@RequestMapping("/archivedependenttestcase/{testcaseNumber}")
	public ModelAndView archiveStory(@PathVariable("testcaseNumber")String testcaseNumber,
			RedirectAttributes ra,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = dependentTestcaseService.archiveDependentTestcase(testcaseNumber, ra, session);
		return mv;
	}
	
	@RequestMapping("/writecsv")
	public ModelAndView writeCsv(@RequestParam(value="inputOutputValue", required=false)List<String> inputOutputValue,
			@RequestParam(value="templateDataId", required=false)List<Long> templateDataId,
			@RequestParam("clientName")String clientName,
			@RequestParam("url")String url,
			@RequestParam("assignedAccount")String assignedAccount,
			@RequestParam("template")String templateId,
			@RequestParam("storyId")Long storyId,
			@RequestParam("description")String description,
			@RequestParam(value="tapImportStatus",required=false)String tapImportStatus,
			HttpSession session,
			RedirectAttributes ra) {
		
		ModelAndView mv = new ModelAndView();
		mv = dependentTestcaseService.writeCsv(session, inputOutputValue, templateDataId, clientName, url, 
				assignedAccount, templateId, storyId, description, tapImportStatus, ra);
		return mv;
		
	}
	
	@RequestMapping("/modifytestcase")
	public ModelAndView modifyTestcase(@RequestParam("templateDataId")List<Long>templateDataId,
			@RequestParam("inputOutputValue")List<String>inputOutputValue,
			@RequestParam("testcaseNumber")String testcaseNumber,
			@RequestParam("storyId")long storyId,
			@RequestParam(value="tapImportStatus", required=false)String tapImportStatus,
			@RequestParam("description")String description,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = dependentTestcaseService.modifyTestcase(templateDataId, inputOutputValue, testcaseNumber, storyId, session, tapImportStatus, description, ra);
		return mv;
	}
	
	@RequestMapping("/retrievearchiveddependenttestcase")
	public ModelAndView retrieveArchivedDependentTestcase(
			@RequestParam("testcaseNumber")List<String>testcaseNumber,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = dependentTestcaseService.retrieveArchivedDependentTestcase(testcaseNumber, session);
		return mv;
	}
	
}
