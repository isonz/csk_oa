package cn.ptp.oa.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ptp.oa.common.AuditHistoryVO;
import cn.ptp.oa.common.OAUtils;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.service.UserService;
import cn.ptp.oa.service.WorkFlowService;
import cn.ptp.oa.util.ConfigUtils;
import cn.ptp.oa.util.wxutil.WeixinUtils;

@Service
@Transactional
public class WorkFlowServiceImpl implements WorkFlowService {
	private static Logger log = LoggerFactory.getLogger(WorkFlowServiceImpl.class);
	@Resource protected TaskExecutor taskExecutor;	//注入Spring封装的异步执行器
	
	@Resource private RepositoryService repositoryService;
	@Resource private RuntimeService runtimeService;
	@Resource private TaskService taskService;
	@Resource private FormService formService;
	@Resource private HistoryService historyService;
	@Resource private IdentityService identityService;
	
	@Resource private UserService userService;
	
	public List<Deployment> findDeploy(){
		List<Deployment> list = repositoryService.createDeploymentQuery().orderByDeploymenTime().desc().list();
		return list;
	}
	
	public void deploy(String name,String fileName){
		repositoryService.createDeployment().name(name)
		.addInputStream(fileName+".bpmn", this.getClass().getResourceAsStream("/diagrams/"+fileName+".bpmn"))
		.addInputStream(fileName+".png", this.getClass().getResourceAsStream("/diagrams/"+fileName+".png"))
		.deploy();
	log.info("WorkFlowServiceImpl.deploy("+fileName+")");
	}

	@Override
	public void deploy(String fileName) {
		repositoryService.createDeployment().name(fileName)
			.addInputStream(fileName+".bpmn", this.getClass().getResourceAsStream("/diagrams/"+fileName+".bpmn"))
			.addInputStream(fileName+".png", this.getClass().getResourceAsStream("/diagrams/"+fileName+".png"))
			.deploy();
		log.info("WorkFlowServiceImpl.deploy("+fileName+")");
	}

	@Override
	public void startProcessInstance(String processDefinitionKey, String businessKey,Map<String, Object> variables) {
//		runtimeService.startProcessInstanceById(processDefinitionId, businessKey, variables)
		runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
//		String processInstanceID = this.findProcessInstanceIDByBusinessKey(businessKey);
		//获取正在执行的任务
//		Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
		asyncNextTaskNotice(businessKey);
	}
	
	/**异步通知待办任务*/
	private void asyncNextTaskNotice(final String businessKey){
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				//如果不为true则关闭
				String notice = ConfigUtils.getConfigValue("task_notice_flag");
				if(!"true".equals(notice)){
					return;
				}
//				HistoricTaskInstance historyTask = historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
				//执行发送
				Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
				if(task==null){
					return;
				}
				String username = task.getAssignee();
				String message = "您有一个新的<"+task.getName()+">任务";
				User user = userService.findByUserName(username);
				WeixinUtils.postTextMessageTo(message, user.getUserid());
			}
		});
	}

	@Override
	public List<Task> findTaskListByUser(String userid) {
		return taskService.createTaskQuery().taskAssignee(userid).orderByTaskCreateTime().asc().list();
	}
	
	@Override
	public long findTaskCountByUser(String userid) {
		return taskService.createTaskQuery().taskAssignee(userid).orderByTaskCreateTime().asc().count();
	}
	
	/**根据任务id查询执行对象*/
	private ExecutionEntity findExecutionBy(String taskId){
		HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
//		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String executionId = historicTaskInstance.getExecutionId();
		ExecutionEntity executionEntity = (ExecutionEntity)runtimeService.createExecutionQuery().executionId(executionId).singleResult();
		return executionEntity;
	}
	
	private ProcessDefinitionEntity findProcessDefinitionEntityBy(String taskId){
//		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		String processDefinitionId = historicTaskInstance.getProcessDefinitionId();
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(processDefinitionId);
		return processDefinitionEntity;
	}
	private ProcessDefinition findProcessDefinitionByBusinessKey(String businessKey) {
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceBusinessKey(businessKey)
				.singleResult();
		String processDefinitionId = historicProcessInstance.getProcessDefinitionId();
//		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
//						.processDefinitionId(processDefinitionId)
//						.singleResult();
		ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
		return processDefinition;
	}
	
	/**
	 * 通过任务id查询对应的活动
	 * @param taskid
	 * @return
	 */
	private ActivityImpl findActivityImplBy(String taskid){
		// 1.获取流程定义
		  Task task = this.taskService.createTaskQuery().taskId(taskid).singleResult();
		  ProcessDefinitionEntity pd = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
		  // 2.获取流程实例
		  ProcessInstance pi =runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult(); 
		  // 3.通过流程实例查找当前活动的ID
		  String activitiId = pi.getActivityId();
		  // 4.通过活动的ID在流程定义中找到对应的活动对象
		  ActivityImpl activity = pd.findActivity(activitiId);
		  return activity;
	}
	
	@Override
	public List<String> findOutcomesByTaskId(String taskId) {
		//ExecutionEntity executionEntity = this.findExecutionBy(taskId);
		ActivityImpl activityImpl  = this.findActivityImplBy(taskId);
		List<PvmTransition>  listPvmTransition = activityImpl.getOutgoingTransitions();
		List<String> list = new ArrayList<String>();
		if(listPvmTransition!=null && listPvmTransition.size()>0){
			if(listPvmTransition.size()==1){
				list.add("批准");
				list.add("不批准");
			}
			else{
				for(PvmTransition pvmTransition:listPvmTransition){
					String name = (String)pvmTransition.getProperty("name");
					if("".equals(name)){
						name="默认提交";
					}
					list.add(name);
				}
			}
		}
		return list;
	}
	
	@Override
	public void completeTask(String type,String taskId, String message,Map<String, Object> variables) {
		String businessKey = findBusinessKey(taskId);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String userid = task.getAssignee();
		String processInstanceId = task.getProcessInstanceId();
		Authentication.setAuthenticatedUserId(userid);
		taskService.addComment(taskId, processInstanceId,type,message);
		taskService.complete(taskId, variables);
		
		
		asyncNextTaskNotice(businessKey);
	}
	
	
	
	@Override
	public boolean findProcessInstanceIsCompleted(String taskid) {
		ExecutionEntity executionEntity = this.findExecutionBy(taskid);
		if(executionEntity==null){
			return true;
		}
		return false;
	}
	
	@Override
	public InputStream getResourceAsStream(String deploymentId, String resourceName) {
		return repositoryService.getResourceAsStream(deploymentId, resourceName);
	}
	
	@Override
	public InputStream getResourceAsStreamByDeployID(String deployID) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployID).singleResult();
//		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
//				.deploymentId(deployID)
//				.singleResult();
//		String processDefinitionId = historicProcessInstance.getProcessDefinitionId();
////		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
////						.processDefinitionId(processDefinitionId)
////						.singleResult();
//		ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
		return this.getResourceAsStream(deployID, processDefinition.getDiagramResourceName());
	}
	
	@Override
	public InputStream getResourceAsStreamByBusinessKey(String businessKey) {
		ProcessDefinition processDefinition = this.findProcessDefinitionByBusinessKey(businessKey);
		return this.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());
	}
	
	@Override
	public InputStream getResourceAsStream(String taskid) {
		ProcessDefinitionEntity processDefinition = this.findProcessDefinitionEntityBy(taskid);
		return this.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());
	}
	
	@Override
	public List<AuditHistoryVO> findCommentHistoryByBusinessKey(String businessKey) {
//		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
//				.processInstanceBusinessKey(businessKey)
//				.singleResult();
//		String processInstanceId = historicProcessInstance.getId();
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
				.processInstanceBusinessKey(businessKey)
				.orderByTaskCreateTime().asc()
				.list();
		List<Comment> commentList = new ArrayList<Comment>();
		if(list!=null && list.size()>0){
			for(HistoricTaskInstance hti:list){
				String htaskId = hti.getId();
				List<Comment> taskCommentList = taskService.getTaskComments(htaskId,OAUtils.AUDIT_OPINION_AGREE);
				commentList.addAll(taskCommentList);
				taskCommentList = taskService.getTaskComments(htaskId,OAUtils.AUDIT_OPINION_DEAGREE);
				commentList.addAll(taskCommentList);
			}
		}
		
		return this.changeCommentToVO(commentList);
	}
	
	@Override
	public List<AuditHistoryVO> findCommentHistoryByTaskId(String taskId) {
		List<Comment> commentList = new ArrayList<Comment>();
//		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
//		String processInstanceId = task.getProcessInstanceId();
		String processInstanceId = historicTaskInstance.getProcessInstanceId();
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
					.processInstanceId(processInstanceId)
					.orderByTaskCreateTime().asc()
					.list();
		if(list!=null && list.size()>0){
			for(HistoricTaskInstance hti:list){
				String htaskId = hti.getId();
				List<Comment> taskCommentList = taskService.getTaskComments(htaskId,OAUtils.AUDIT_OPINION_AGREE);
				commentList.addAll(taskCommentList);
				taskCommentList = taskService.getTaskComments(htaskId,OAUtils.AUDIT_OPINION_DEAGREE);
				commentList.addAll(taskCommentList);
			}
		}
		return this.changeCommentToVO(commentList);
	}
	
	private List<AuditHistoryVO> changeCommentToVO(List<Comment> commentList){
		List<AuditHistoryVO> listVo = new ArrayList<AuditHistoryVO>();
		if(commentList!=null && commentList.size()>0){
			for(Comment comment:commentList){
				AuditHistoryVO vo = new AuditHistoryVO();
				vo.setDate(comment.getTime());
				vo.setMessage(comment.getFullMessage());
				vo.setUserName(comment.getUserId());
				vo.setType(comment.getType());
				listVo.add(vo);
			}
		}
		return listVo;
	}
	
	
	
	@Override
	public String findProcessInstanceIDByBusinessKey(String businessKey) {
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)this.findProcessDefinitionByBusinessKey(businessKey);
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
				.processDefinitionId(processDefinitionEntity.getId())
				.processInstanceBusinessKey(businessKey)
//				.processInstanceBusinessKey(processInstanceBusinessKey, processDefinitionKey)
				.singleResult();
		return pi.getId();
	}

	@Override
	public ProcessInstance findProcessInstanceByBusinessKey(String businessKey) {
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)this.findProcessDefinitionByBusinessKey(businessKey);
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
				.processDefinitionId(processDefinitionEntity.getId())
				.processInstanceBusinessKey(businessKey)
//				.processInstanceBusinessKey(processInstanceBusinessKey, processDefinitionKey)
				.singleResult();
		return pi;
	}

	@Override
	public Map<String,Object> findCoordinate(String businessKey){
		Map<String,Object> map = new HashMap<String, Object>();
		coordinateIni(map);
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)this.findProcessDefinitionByBusinessKey(businessKey);
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
				.processDefinitionId(processDefinitionEntity.getId())
				.processInstanceBusinessKey(businessKey)
//				.processInstanceBusinessKey(processInstanceBusinessKey, processDefinitionKey)
				.singleResult();
		//获取正在执行的任务
		Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
		if(task!=null && pi!=null){
			ActivityImpl activityImpl  = processDefinitionEntity.findActivity(pi.getActivityId());
			if(activityImpl!=null){
				map.put("x", activityImpl.getX());
				map.put("y", activityImpl.getY());
				map.put("width", activityImpl.getWidth());
				map.put("height", activityImpl.getHeight());
			}
		}
		return map;
	}
	
	@Override
	public Map<String, Object> findCoordinateByTaskid(String taskid) {
		Map<String,Object> map = new HashMap<String, Object>();
		coordinateIni(map);
		
//		ProcessDefinitionEntity processDefinitionEntity = this.findProcessDefinitionEntityBy(taskid);
//		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinitionEntity.getId()).singleResult();
		
		ActivityImpl activityImpl = findActivityImplBy(taskid);

		
//		if(pi!=null){
//			ActivityImpl activityImpl  = processDefinitionEntity.findActivity(pi.getActivityId());
//			if(activityImpl!=null){
				map.put("x", activityImpl.getX());
				map.put("y", activityImpl.getY());
				map.put("width", activityImpl.getWidth());
				map.put("height", activityImpl.getHeight());
//			}
//		}
		return map;
	}

	private void coordinateIni(Map<String, Object> map) {
		map.put("x", 0);
		map.put("y", 0);
		map.put("width", 0);
		map.put("height", 0);
	}
	
	@Override
	public String findBusinessKey(String taskid) {
		ExecutionEntity executionEntity = this.findExecutionBy(taskid);
		String businessKey = executionEntity.getBusinessKey();
		return businessKey;
	}
}
