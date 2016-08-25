package cn.ptp.oa.controller;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import cn.ptp.oa.common.DeploymentVO;
import cn.ptp.oa.common.OAUtils;
import cn.ptp.oa.common.OAWebUtils;
import cn.ptp.oa.service.UserService;
import cn.ptp.oa.service.WorkFlowService;

@Controller
@RequestMapping("/workflow")
public class WorkFlowController {
	private static Logger log = LoggerFactory.getLogger(WorkFlowController.class);
	@Resource private WorkFlowService workFlowService;
	@Resource private UserService userService;
	
	private List<DeploymentVO> getAllDeploymentFile(){
		List<DeploymentVO> list = new ArrayList<DeploymentVO>();
		URI uri = null;
		try {
			uri = WorkFlowController.class.getClassLoader().getResource("diagrams").toURI();
		} catch (URISyntaxException e) {
			log.info(e.getMessage());
		}
		File file = new File(uri);
		if(!file.exists() || !file.isDirectory()){
			log.info("系统异常,"+file.getPath()+"文件夹不存在");
			return list;
		}
		String fileNames[] = file.list();
		for(String fileName:fileNames){
			String splits[] = fileName.split("\\.");
			if("bpmn".equals(splits[1])){
				DeploymentVO vo = new DeploymentVO();
				vo.setName(splits[0]);
				list.add(vo);
			}
		}
		return list;
	}
	
	@RequestMapping(value="/listdeploy")
	public String listDeploy(Model model){
		List<Deployment> list = workFlowService.findDeploy();
		model.addAttribute("list", list);
		List<DeploymentVO> dlist = getAllDeploymentFile();
		model.addAttribute("dlist", dlist);
		
		return "workflow/deployList";
	}
	
	/**发布流程*/
	@RequestMapping(value="/deploy")
	public String deploy(Model model,String name){
		workFlowService.deploy(name);
		return "redirect:/workflow/listdeploy";
	}
	
	@RequestMapping(value="/viewimagepagebydeploy",method=RequestMethod.GET)
	public String viewImageByDeployID(String id,HttpServletRequest request,HttpServletResponse response,Model model){
		model.addAttribute("id", id);
		return "/workflow/viewimagedeploy";
	}
	
	@RequestMapping(value="/viewimagebydeploy",method=RequestMethod.GET)
	public void viewWorkFlowImageByDeploy(String id,HttpServletRequest request,
			HttpServletResponse response,Model model) throws Exception{
		InputStream is = workFlowService.getResourceAsStreamByDeployID(id);
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
	
	@RequestMapping(value="/uploadanddeploy")
	public String deploy(String name,MultipartFile file,Model model){
		workFlowService.deploy(name,"LeaveApplication");
		return "";
	}
	
	@RequestMapping(value="/viewtasklist")
	public String viewTaskList(HttpServletRequest request,Model model){
		String userName = OAWebUtils.getCurrentUserName(request);
		List<Task> list = workFlowService.findTaskListByUser(userName);
//		long count = workFlowService.findTaskCountByUser(userName);
		model.addAttribute("list", list);
//		model.addAttribute("count", count);
//		model.addAttribute("userid", userid);
		return "/workflow/taskList";
	}
	
	/**
	 * 根据任务id跳转到审核页面，次页面需要查看申请信息以及所有历史审核信息
	 * @param taskId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/toaudit")
	public String toAudit(String taskid,HttpServletRequest request,Model model){
		String businessKey = workFlowService.findBusinessKey(taskid);
		String keys[] = businessKey.split("\\.");
//		if(ExtraWorkApply.class.getSimpleName().equals(keys[0])){
//			return "redirect:/extraworkapply/toaudit?id="+keys[1]+"&taskid="+taskid;
//		}
//		throw new RuntimeException("WorkFlowController：toAudit错误");
		return "redirect:/"+keys[0].toLowerCase()+"/toaudit?id="+keys[1]+"&taskid="+taskid;
	}
	
	
	@RequestMapping(value="/viewimagepage",method=RequestMethod.GET)
	public String viewImage(HttpServletRequest request,HttpServletResponse response,Model model){
		String type = request.getParameter("type");
		String id = request.getParameter("id");
//		String typeName=null;
//		if(ExtraWorkApply.class.getSimpleName().equals(type)){
//			typeName = ExtraWorkApply.class.getSimpleName();
//		}
		
		String businessKey = OAUtils.builderWorkFlowBusinessKey(id,type);
//		String businessKey = type+"."+id;
		model.addAttribute("businessKey", businessKey);
		Map<String,Object> zb = workFlowService.findCoordinate(businessKey);
		model.addAttribute("zb", zb);
		return "/workflow/viewimage";
	}
	
	@RequestMapping(value="/viewimage",method=RequestMethod.GET)
	public void viewWorkFlowImage(String businessKey,HttpServletRequest request,
			HttpServletResponse response,Model model) throws Exception{
		InputStream is = workFlowService.getResourceAsStreamByBusinessKey(businessKey);
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
	
	@RequestMapping(value="/viewimagepagebytaskid",method=RequestMethod.GET)
	public String viewImageByTaskid(String taskid,HttpServletRequest request,HttpServletResponse response,Model model){
		model.addAttribute("taskid", taskid);
		Map<String,Object> zb = workFlowService.findCoordinateByTaskid(taskid);
		model.addAttribute("zb", zb);
		return "/workflow/viewimage";
	}
	
	@RequestMapping(value="/viewimagetaskid",method=RequestMethod.GET)
	public void viewWorkFlowImageByTaskid(String taskid,HttpServletRequest request,
			HttpServletResponse response,Model model) throws Exception{
		InputStream is = workFlowService.getResourceAsStream(taskid);
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
