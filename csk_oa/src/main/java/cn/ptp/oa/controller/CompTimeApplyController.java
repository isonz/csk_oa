package cn.ptp.oa.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ptp.oa.common.AuditHistoryVO;
import cn.ptp.oa.common.OAWebUtils;
import cn.ptp.oa.domain.CompTimeApply;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.exception.CustomException;
import cn.ptp.oa.exception.CustomRuntimeException;
import cn.ptp.oa.service.CompTimeApplyService;
import cn.ptp.oa.service.UserService;
import cn.ptp.oa.service.WorkFlowService;
import cn.ptp.oa.util.DateUtil;

@Controller
@RequestMapping("/comptimeapply")
public class CompTimeApplyController {
	@Resource private CompTimeApplyService service;
	@Resource private UserService userService;
	@Resource private WorkFlowService workFlowService;
	
	@RequestMapping(value="/tolist",method=RequestMethod.GET)
	public String toList(Date applyDate,Integer tache,HttpServletRequest request,Model model){
		String userid = OAWebUtils.getCurrentUserid(request);
		if(applyDate==null){
			//默认查询最近一个月的申请
			applyDate = DateUtil.getScrollMonth(new Date(), -1);
		}
		model.addAttribute("applyDate", applyDate);
		List<CompTimeApply> list = service.findByUseridAndApplyDateGreaterThanEqual(userid, applyDate,tache);
		model.addAttribute("list", list);
		return "comptimeapply/list";
	}
	
	@RequestMapping(value="/toadd",method=RequestMethod.GET)
	public String toAddOrEdit(Integer id,HttpServletRequest request,
			HttpServletResponse response,Model model) throws Exception{
		String userid = OAWebUtils.getCurrentUserid(request);
		CompTimeApply compTimeApply=null;
		User user = userService.findByUserid(userid);
		
		if(id==null){
			compTimeApply = new CompTimeApply();
			compTimeApply.setUserid(userid);
			compTimeApply.setUsername(user.getUserName());
			compTimeApply.setDept(user.getDepartment().getName());
			compTimeApply.setApplyDate(DateUtil.getDate(new Date()));
		}
		else{
			compTimeApply = service.findById(id);
			if(compTimeApply.getTache()>0){
				throw new CustomException("已提交的记录不允许修改！");
			}
		}
		model.addAttribute(compTimeApply);
//		model.addAttribute("timesUnitMap", timesUnitMap);
		return "comptimeapply/add";
	}
	
	@RequestMapping(value="/doadd")
	public String doAddOrEdit(CompTimeApply compTimeApply){
		service.save(compTimeApply);
		return "redirect:/comptimeapply/tolist";
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
//		String userid = request.getParameter("userid");
		service.commit(id);
		return "redirect:/comptimeapply/tolist";
	}
	
	@RequestMapping(value="/toaudit")
	public String toAudit(String taskid,Integer id,HttpServletRequest request,
			HttpServletResponse response,Model model){
//		String userid=request.getParameter("userid");
		CompTimeApply apply = service.findById(id);
		List<AuditHistoryVO> list = service.findAuditHistory(id);
		List<String> buttons = workFlowService.findOutcomesByTaskId(taskid);
		model.addAttribute("list", list);
		model.addAttribute("taskid", taskid);
		model.addAttribute("buttons", buttons);
//		model.addAttribute("userid", userid);
		model.addAttribute(apply);
		return "comptimeapply/audit";
	}
	
	@RequestMapping(value="/doaudit")
	public String doAudit(Integer id,String taskid,String auditstatus,String message,HttpServletRequest request){
//		String userid=request.getParameter("userid");
		service.audit(id, taskid, message, auditstatus);
//		ExtraWorkApply apply = service.findById(id);
		return "redirect:/workflow/viewtasklist";
	}
	
	@RequestMapping(value="/viewauditlist")
	public String viewAuditList(Integer id,HttpServletRequest request,
			HttpServletResponse response,Model model){
		CompTimeApply apply = service.findById(id);
		List<AuditHistoryVO> list = service.findAuditHistory(id);
		model.addAttribute("list", list);
		model.addAttribute("apply", apply);
		model.addAttribute(apply);
//		model.addAttribute("timesUnitMap", timesUnitMap);
		return "comptimeapply/viewaudit";
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
