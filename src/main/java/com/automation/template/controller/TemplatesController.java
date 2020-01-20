package com.automation.template.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.template.service.TemplatesService;

@Controller
@RequestMapping("/templates")
public class TemplatesController {
	
	@Autowired
	TemplatesService templatesService;

	@RequestMapping("/processcreatetemplate")
	public ModelAndView processCreateTemplate(@RequestParam("clientName")String clientName,
			@RequestParam("templateName") String templateName,
			@RequestParam("isPublic")String isPublic,
			@RequestParam("label")List<String>label,
			@RequestParam("webElementNature")List<String>webElementNature,
			@RequestParam("natureOfAction")List<String>natureOfAction,
			@RequestParam("triggerEnterValue")List<String>isTriggerEnter,
			@RequestParam("screenCaptureValue")List<String>isScreenCapture,
			@RequestParam("webElementName")List<String>webElementName,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		
		mv = templatesService.processCreateTemplate(label, natureOfAction, webElementName, webElementNature, isTriggerEnter, isScreenCapture, clientName, templateName, isPublic, session, ra);
		
		return mv;
	}
	
	@RequestMapping("/createtemplatepage")
	public ModelAndView createTemplatePage(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
		mv = templatesService.setCreateTemplatePage(session);
		
		return mv;
	}
	
	@RequestMapping("/modifytemplatepage")
	public ModelAndView modifyTemplatePage(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = templatesService.setModifyTemplatePage(session);
		return mv;
	}
	
	@RequestMapping("/processmodifytemplate")
	public ModelAndView processModelAndView(@RequestParam(value="label", required=false)List<String>label,
			@RequestParam(value="webElementNature", required=false)List<String>webElementNature,
			@RequestParam(value="natureOfAction", required=false)List<String>natureOfAction,
			@RequestParam(value="triggerEnterValue", required=false)List<Boolean>isTriggerEnter,
			@RequestParam(value="screenCaptureValue", required=false)List<Boolean>isScreenCapture,
			@RequestParam(value="webElementName", required=false)List<String>webElementName,
			@RequestParam(value="templateDataId", required=false)List<Long>templateDataId,
			@RequestParam("templateId")String templateId,
			@RequestParam(value="isPublic", required=false)Boolean isPublic,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = templatesService.processModifyTemplate(label, natureOfAction, webElementName, webElementNature, isTriggerEnter, isScreenCapture, templateDataId, templateId, isPublic, session, ra);
		return mv;
	}
	
}
