package cn.ptp.oa.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ptp.oa.common.AuditHistoryVO;
import cn.ptp.oa.common.OAWebUtils;
import cn.ptp.oa.common.TalentDemandApplyReasonType;
import cn.ptp.oa.domain.TalentDemandApply;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.exception.CustomRuntimeException;
import cn.ptp.oa.service.TalentDemandApplyService;
import cn.ptp.oa.service.UserService;
import cn.ptp.oa.service.WorkFlowService;
import cn.ptp.oa.util.DateUtil;

@Controller
@RequestMapping("/talentdemandapply")
public class TalentDemandApplyController {
	@Resource private TalentDemandApplyService service;
	@Resource private UserService userService;
	@Resource private WorkFlowService workFlowService;
	
	@ModelAttribute("iniMap")
	public Map<String,String> ini(){
		Map<String,String> iniMap = new HashMap<String,String>();
		iniMap.put("classEntityName", TalentDemandApply.class.getSimpleName());
		iniMap.put("classRequestMapping", TalentDemandApply.class.getSimpleName().toLowerCase());
		return iniMap;
	}
	
	@ModelAttribute("typeMap")
	public Map<String,String> iniType(){
		Map<String,String> typeMap = new HashMap<String,String>();
		typeMap.put(TalentDemandApplyReasonType.A.toString(), TalentDemandApplyReasonType.A.getName());
		typeMap.put(TalentDemandApplyReasonType.B.toString(), TalentDemandApplyReasonType.B.getName());
		typeMap.put(TalentDemandApplyReasonType.C.toString(), TalentDemandApplyReasonType.C.getName());
		typeMap.put(TalentDemandApplyReasonType.D.toString(), TalentDemandApplyReasonType.D.getName());
		typeMap.put(TalentDemandApplyReasonType.E.toString(), TalentDemandApplyReasonType.E.getName());
		typeMap.put(TalentDemandApplyReasonType.F.toString(), TalentDemandApplyReasonType.F.getName());
		return typeMap;
	}
	
	@RequestMapping(value="/tolist",method=RequestMethod.GET)
	public String toList(HttpServletRequest request,Model model,Integer tache){
//		String userid = OAWebUtils.getCurrentUserid(request);
		List<TalentDemandApply> list = service.findByTache(tache);
		model.addAttribute("list", list);
		if(tache==null){
			tache=TalentDemandApplyService.TACHE_ALL;
		}
		model.addAttribute("tache", tache);
		return "talentdemandapply/list";
	}
	
	@RequestMapping(value="/toadd",method=RequestMethod.GET)
	public String toAddOrEdit(Integer id,HttpServletRequest request,
			HttpServletResponse response,Model model) throws Exception{
		String userid = OAWebUtils.getCurrentUserid(request);
		TalentDemandApply entity=null;
		User user = userService.findByUserid(userid);
		
		if(id==null){
			entity = new TalentDemandApply();
			entity.setUserid(userid);
			entity.setUsername(user.getUserName());
			entity.setDept(user.getDepartment().getName());
			entity.setApplyDate(DateUtil.getDate(new Date()));
		}
		else{
			entity = service.findById(id);
			if(entity.getTache()>0){
				//throw new CustomException("已提交的记录不允许修改！");
			}
		}
		model.addAttribute("entity",entity);
//		model.addAttribute("timesUnitMap", timesUnitMap);
		return "talentdemandapply/add";
	}
	
	@RequestMapping(value="/doadd")
	public String doAddOrEdit(TalentDemandApply entity){
		service.save(entity);
		return "redirect:/talentdemandapply/tolist";
	}
	
	@ResponseBody
	@RequestMapping(value="/delete")
	public Object delete(Integer id){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			service.delete(id);
			map.put("ok", 1);
		}
		catch(CustomRuntimeException e){
			map.put("ok", -1);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	@RequestMapping(value="/commit")
	public String commit(Integer id,HttpServletRequest request,
			HttpServletResponse response,Model model){
		service.commit(id);
		return "redirect:/talentdemandapply/tolist";
	}
	
	@RequestMapping(value="/tofeedback")
	public String toFeedback(Integer id,HttpServletRequest request,
			HttpServletResponse response,Model model){
		TalentDemandApply apply = service.findById(id);
		
		model.addAttribute("entity",apply);
		return "talentdemandapply/feedback";
	}
	
	@RequestMapping(value="/dofeedback")
	public String doFeedback(TalentDemandApply entity){
		service.save(entity);
		return "redirect:/talentdemandapply/tolist";
	}
	
	@RequestMapping(value="/toaudit")
	public String toAudit(String taskid,Integer id,HttpServletRequest request,
			HttpServletResponse response,Model model){
		TalentDemandApply apply = service.findById(id);
		List<AuditHistoryVO> list = service.findAuditHistory(id);
		List<String> buttons = workFlowService.findOutcomesByTaskId(taskid);
		model.addAttribute("list", list);
		model.addAttribute("taskid", taskid);
		model.addAttribute("buttons", buttons);
//		model.addAttribute("userid", userid);
		model.addAttribute("entity",apply);
		return "talentdemandapply/audit";
	}
	
	@RequestMapping(value="/doaudit")
	public String doAudit(Integer id,String taskid,String auditstatus,String message,HttpServletRequest request){
		service.audit(id, taskid, message, auditstatus);
		return "redirect:/workflow/viewtasklist";
	}
	
	@RequestMapping(value="/viewauditlist")
	public String viewAuditList(Integer id,HttpServletRequest request,
			HttpServletResponse response,Model model){
		TalentDemandApply apply = service.findById(id);
		List<AuditHistoryVO> list = service.findAuditHistory(id);
		model.addAttribute("list", list);
		model.addAttribute("apply", apply);
		model.addAttribute("entity",apply);
		return "talentdemandapply/viewaudit";
	}
	
	
	@RequestMapping(value="/viewimage",method=RequestMethod.GET)
	public void viewWorkFlowImage(Integer id,HttpServletRequest request,
			HttpServletResponse response,Model model) throws Exception{
		InputStream is = service.getResourceAsStream(id);
		OutputStream os = response.getOutputStream();
		byte[] b = new byte[1024];
		int read=0;
		while((read = is.read(b))!=-1){
			os.write(b);
		}
		os.flush();
		os.close();
		is.close();
	}
}
