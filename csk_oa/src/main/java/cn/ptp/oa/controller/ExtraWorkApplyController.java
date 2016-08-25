package cn.ptp.oa.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ptp.oa.common.AuditHistoryVO;
import cn.ptp.oa.common.OAWebUtils;
import cn.ptp.oa.domain.ExtraWorkApply;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.exception.CustomException;
import cn.ptp.oa.exception.CustomRuntimeException;
import cn.ptp.oa.service.ExtraWorkApplyService;
import cn.ptp.oa.service.UserService;
import cn.ptp.oa.service.WorkFlowService;

@Controller
@RequestMapping("/extraworkapply")
public class ExtraWorkApplyController {
//	private final  Map<String,String> timesUnitMap = new HashMap<String,String>();
	@Resource private ExtraWorkApplyService service;
	@Resource private UserService userService;
	@Resource private WorkFlowService workFlowService;
	
	
	
	@ModelAttribute("timesUnitMap")
	public Map<String,String> ini(){
		Map<String,String> timesUnitMap = new HashMap<String,String>();
		timesUnitMap.put("小时", "小时");
		timesUnitMap.put("天", "天");
		return timesUnitMap;
	}
	
	@RequestMapping(value="/tolist",method=RequestMethod.GET)
	public String toList(HttpServletRequest request,HttpServletResponse response,Model model){
		String userid = OAWebUtils.getCurrentUserid(request);
		String spage = request.getParameter("page");
		String ssize = request.getParameter("size");
		String stache = request.getParameter("tache");
		int size=5;
		int page=1;
		int tache=1;
		if(ssize!=null){
			size = Integer.parseInt(ssize);
		}
		if(spage!=null){
			page = Integer.parseInt(spage);
		}
		if(stache!=null){
			tache = Integer.parseInt(stache);
		}
		
		Page<ExtraWorkApply> list = null;
		if(tache==2){
			list = service.findCompletedSortByDate(userid, page-1, size);
		}
		else{
			list = service.findUnCompleteSortByDate(userid,page-1,size);
		}
		
		model.addAttribute("list", list);
		model.addAttribute("userid", userid);
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("tache", tache);
		return "extraworkapply/list";
	}
	
	@RequestMapping(value="/toadd",method=RequestMethod.GET)
	public String toAddOrEdit(Integer id,HttpServletRequest request,
			HttpServletResponse response,Model model) throws Exception{
		String userid = OAWebUtils.getCurrentUserid(request);
		ExtraWorkApply apply=null;
		User user = userService.findByUserid(userid);
		
		if(id==null){
			apply = new ExtraWorkApply();
			apply.setUserid(userid);
			apply.setUsername(user.getUserName());
			apply.setDept(user.getDepartment().getName());
		}
		else{
			apply = service.findById(id);
			if(apply.getTache()>0){
				throw new CustomException("已提交的记录不允许修改！");
			}
		}
		model.addAttribute(apply);
//		model.addAttribute("timesUnitMap", timesUnitMap);
		return "extraworkapply/add";
	}
	
	@RequestMapping(value="/doadd")
	public String doAddOrEdit(ExtraWorkApply extraWorkApply){
		service.save(extraWorkApply);
		return "redirect:/extraworkapply/tolist";
	}
	
	@ResponseBody
	@RequestMapping(value="/delete")
	public Object delete(Integer id,Model model){
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
		return "redirect:/extraworkapply/tolist";
	}
	
	@RequestMapping(value="/toaudit")
	public String toAudit(String taskid,Integer id,HttpServletRequest request,
			HttpServletResponse response,Model model){
//		String userid=request.getParameter("userid");
		ExtraWorkApply apply = service.findById(id);
		List<AuditHistoryVO> list = service.findAuditHistory(id);
		List<String> buttons = workFlowService.findOutcomesByTaskId(taskid);
		model.addAttribute("list", list);
		model.addAttribute("taskid", taskid);
		model.addAttribute("buttons", buttons);
//		model.addAttribute("userid", userid);
		model.addAttribute(apply);
		return "extraworkapply/audit";
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
		ExtraWorkApply apply = service.findById(id);
		List<AuditHistoryVO> list = service.findAuditHistory(id);
		model.addAttribute("list", list);
		model.addAttribute("apply", apply);
		model.addAttribute(apply);
//		model.addAttribute("timesUnitMap", timesUnitMap);
		return "extraworkapply/viewaudit";
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
