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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ptp.oa.common.AuditHistoryVO;
import cn.ptp.oa.common.OAWebUtils;
import cn.ptp.oa.domain.LeaveApplication;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.exception.CustomException;
import cn.ptp.oa.exception.CustomRuntimeException;
import cn.ptp.oa.service.LeaveApplicationService;
import cn.ptp.oa.service.UserService;
import cn.ptp.oa.service.WorkFlowService;
import cn.ptp.oa.util.DateUtil;

@Controller
@RequestMapping("/leaveapplication")
public class LeaveApplicationController {
	@Resource private UserService userService;
	@Resource private WorkFlowService workFlowService;
	@Resource private LeaveApplicationService service;
	
	@ModelAttribute("types")
	public Map<String,String> ini(){
		Map<String,String> types = new HashMap<String,String>();
		types.put("病假", "病假");
		types.put("年假", "年假");
		types.put("事假", "事假");
		types.put("补假", "补假");
		types.put("其它", "其它");
		return types;
	}
	
	@RequestMapping(value="/tolist",method=RequestMethod.GET)
	public String toList(Date startDate,Integer tache,HttpServletRequest request,HttpServletResponse response,Model model){
		String userid = OAWebUtils.getCurrentUserid(request);
//		String stache = request.getParameter("tache");
//		int tache=-1;
//		if(stache!=null){
//			tache = Integer.parseInt(stache);
//		}
		if(tache==null){
			tache=LeaveApplicationService.TACHE_ALL;
		}
		if(startDate==null){
//			默认查询最近一个月的记录
			startDate = DateUtil.getScrollMonth(new Date(), -1);
//			startDate = DateUtil.getDate(new Date());
		}
		List<LeaveApplication> list = service.findAfterDateAndTacheAndUserid(startDate, tache, userid);
		
		model.addAttribute("list", list);
		model.addAttribute("userid", userid);
		model.addAttribute("tache", tache);
		model.addAttribute("startDate", startDate);
		return "leaveapplication/list";
	}
	
	@RequestMapping(value="/toadd",method=RequestMethod.GET)
	public String toAddOrEdit(Integer id,HttpServletRequest request,
			HttpServletResponse response,Model model) throws Exception{
		String userid = OAWebUtils.getCurrentUserid(request);
		LeaveApplication leaveapplication=null;
		User user = userService.findByUserid(userid);
		
		if(id==null){
			leaveapplication = new LeaveApplication();
			leaveapplication.setUserid(userid);
			leaveapplication.setUsername(user.getUserName());
			leaveapplication.setDept(user.getDepartment().getName());
			leaveapplication.setType("事假");
		}
		else{
			leaveapplication = service.findById(id);
			if(leaveapplication.getTache()>0){
				throw new CustomException("已提交的记录不允许修改！");
			}
		}
		model.addAttribute(leaveapplication);
//		model.addAttribute("timesUnitMap", timesUnitMap);
		return "leaveapplication/add";
	}
	
	@RequestMapping(value="/doadd")
	public String doAddOrEdit(LeaveApplication leaveApplication){
		String format="yyyy-MM-dd HH:mm";
		leaveApplication.setStartDate(DateUtil.parseDate(leaveApplication.getPart_start_date()+" "+leaveApplication.getPart_start_time(), format));
		leaveApplication.setEndDate(DateUtil.parseDate(leaveApplication.getPart_end_date()+" "+leaveApplication.getPart_end_time(), format));
		service.save(leaveApplication);
		return "redirect:/leaveapplication/tolist";
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
		return "redirect:/leaveapplication/tolist";
	}
	
	@RequestMapping(value="/toaudit")
	public String toAudit(String taskid,Integer id,HttpServletRequest request,
			HttpServletResponse response,Model model){
//		String userid=request.getParameter("userid");
		LeaveApplication apply = service.findById(id);
		List<AuditHistoryVO> list = service.findAuditHistory(id);
		List<String> buttons = workFlowService.findOutcomesByTaskId(taskid);
		model.addAttribute("list", list);
		model.addAttribute("taskid", taskid);
		model.addAttribute("buttons", buttons);
//		model.addAttribute("userid", userid);
		model.addAttribute(apply);
		return "leaveapplication/audit";
	}
	
	@RequestMapping(value="/doaudit",method=RequestMethod.POST)
	public String doAudit(Integer id,String taskid,String auditstatus,String message,HttpServletRequest request){
//		String userid=request.getParameter("userid");
		service.audit(id, taskid, message, auditstatus);
//		ExtraWorkApply apply = service.findById(id);
		return "redirect:/workflow/viewtasklist";
	}
	
	@RequestMapping(value="/viewauditlist")
	public String viewAuditList(Integer id,HttpServletRequest request,
			HttpServletResponse response,Model model){
		LeaveApplication apply = service.findById(id);
		List<AuditHistoryVO> list = service.findAuditHistory(id);
		model.addAttribute("list", list);
		model.addAttribute("apply", apply);
		model.addAttribute(apply);
//		model.addAttribute("timesUnitMap", timesUnitMap);
		return "leaveapplication/viewaudit";
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
