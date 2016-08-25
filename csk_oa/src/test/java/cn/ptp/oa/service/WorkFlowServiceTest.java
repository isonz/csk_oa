package cn.ptp.oa.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.ptp.oa.common.AuditHistoryVO;

public class WorkFlowServiceTest {
	private ApplicationContext context=null;
	private WorkFlowService service = null;
	private TaskService taskService = null;

	@Before
	public void setUp() throws Exception {
		context =
			    new ClassPathXmlApplicationContext(new String[] {
			    		"applicationContext_ds.xml", 
			    		"applicationContext_jpa.xml", 
			    		"applicationContext_main.xml", 
			    		"applicationContext_tx.xml",
			    		"applicationContext_activiti_context.xml",
			    		"applicationContext_jobs.xml"
			    		});
		service = (WorkFlowService)context.getBean(WorkFlowService.class);
		taskService = (TaskService)context.getBean(TaskService.class);
	}
	
	@Test
	public void test(){
		Task task = taskService.createTaskQuery().processInstanceBusinessKey("ExtraWorkApply.3").singleResult();
		System.out.println(task.getId());
	}
	
	@Test
	public void deployTest(){
		service.deploy("LeaveApplication");
	}

	@Test
	public void testDeployAndStart() {
		//service.deploy("ExtraWorkApply");
		String processDefinitionKey="ExtraWorkApply";
		String businessKey="ExtraWorkApply"+1;
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("userID", "csk");
		variables.put("deptAuditUserID", "ison");
		variables.put("ceoAuditUserID", "wu");
		
		service.startProcessInstance(processDefinitionKey, businessKey, variables );
	}
	
	@Test
	public void testQueryTask() {
		String userid="wu";
		List<Task> list = service.findTaskListByUser(userid);
		if(list!=null && list.size()>0){
			for(Task task:list){
				System.out.println("流程定义key："+task.getProcessDefinitionId());
				System.out.println("待办人："+task.getAssignee());
				System.out.println("执行对象id："+task.getExecutionId());
				System.out.println("任务名称："+task.getName());
				System.out.println("任务ID："+task.getId());
				System.out.println("------------------------------------------------");
			}
		}
	}
	
	@Test
	public void testCompleteTask(){
		String taskId="30004";
		String message="新部门审批批注";
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("status", "批准");
		service.completeTask("批准",taskId,message, variables);
		System.out.println("---------------------任务完成---------------------------");
	}
	/*

	 * 根据业务key查找批注历史
	 * @param businessKey
	 * @return
	
	public List<Comment> findCommentHistoryByBusinessKey(String businessKey);
	
	
	 * 根据任务id查找批注历史
	 * @param taskId
	 * @return
	
	public List<Comment> findCommentHistoryByTaskId(String taskId);
	*/
	@Test
	public void testFindProcessInstanceIsCompleted(){
		String taskId="30004";
		boolean complete = service.findProcessInstanceIsCompleted(taskId);
		if(complete) System.out.println("---------------------已完成---------------------------");
		else System.out.println("---------------------未完成---------------------------");
	}
	
	@Test
	public void testFindCommentHistoryByBusinessKey(){
		String businessKey="ExtraWorkApply1";
		List<AuditHistoryVO> list = service.findCommentHistoryByBusinessKey(businessKey);
		if(list!=null && list.size()>0){
			for(AuditHistoryVO vo:list){
				System.out.println(vo.getUserName()+"\t"+vo.getDate()+"\t"+vo.getMessage());
				System.out.println("------------------------------------------------");
			}
		}
	}
	
	@Test
	public void testFindCommentHistoryByTaskId(){
		String taskId="30004";
		List<AuditHistoryVO> list = service.findCommentHistoryByTaskId(taskId);
		if(list!=null && list.size()>0){
			for(AuditHistoryVO vo:list){
				System.out.println(vo.getUserName()+"\t"+vo.getDate()+"\t"+vo.getMessage());
				System.out.println("------------------------------------------------");
			}
		}
	}
	
	@Test
	public void testFindCoordinate(){
		String businessKey = "ExtraWorkApply.1";
		Map<String, Object> map = service.findCoordinate(businessKey);
		System.out.println("x"+map.get("x"));
		System.out.println("y"+map.get("y"));
		System.out.println("width"+map.get("width"));
		System.out.println("height"+map.get("height"));
	}
	
	@Test
	public void testFindCoordinateByTaskid(){
		String taskid = "2506";
		Map<String, Object> map = service.findCoordinateByTaskid(taskid);
		System.out.println("x"+map.get("x"));
		System.out.println("y"+map.get("y"));
		System.out.println("width"+map.get("width"));
		System.out.println("height"+map.get("height"));
	}
}
