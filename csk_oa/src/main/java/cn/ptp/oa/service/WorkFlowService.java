package cn.ptp.oa.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import cn.ptp.oa.common.AuditHistoryVO;

public interface WorkFlowService {
	public List<Deployment> findDeploy();
	
	public void deploy(String name,String fileName);
	/**
	 * 部署
	 * @param fileName
	 */
	public void deploy(String fileName);
	
	/**
	 * 启动流程
	 * @param processDefinitionKey 流程定义key，即bpmn文件中的id
	 * @param businessKey 业务key，默认规则为业务domain类名+"."+id
	 * @param variables 流程变量，用于启动实例时传递的各个任务执行人
	 */
	public void startProcessInstance(String processDefinitionKey,String businessKey,Map<String,Object> variables);
	
	/**
	 * 查找用户的待办任务
	 * @param userid
	 * @return
	 */
	public List<Task> findTaskListByUser(String userid);
	
	/**
	 * 查找用户的待办任务记录数
	 * @param userid
	 * @return
	 */
	public long findTaskCountByUser(String userid);
	
	/**
	 * 查找taskid对应的任务流出的流程线,如果没有值赋值为‘默认提交’
	 * @param taskId
	 * @return
	 */
	public List<String> findOutcomesByTaskId(String taskId);
	
	/**
	 * 完成任务
	 * @param type  类型分为批准和不批准
	 * @param taskId
	 * @param variables
	 */
	public void completeTask(String type,String taskId,String message,Map<String,Object> variables);
	
	/**
	 * 根据部署id和资源名称获取资源输入流，一般用来查询流程图片
	 * @param deploymentId
	 * @param resourceName
	 * @return
	 */
	public InputStream getResourceAsStream(String deploymentId, String resourceName);
	
	/**
	 * 根据任务id查询流程图片
	 * @param taskid
	 * @return
	 */
	public InputStream getResourceAsStream(String taskid);
	
	/**
	 * 根据业务key和业务id构成的业务key查询流程图片
	 * @param businessKey
	 * @return
	 */
	public InputStream getResourceAsStreamByBusinessKey(String businessKey);
	
	/**
	 * 根据流程发布id查询流程图片
	 * @param deployID
	 * @return
	 */
	public InputStream getResourceAsStreamByDeployID(String deployID);
	
	
	/**
	 * 根据任务id查找对应的流程实例是否完成
	 * @param taskid
	 * @return true 已完成，false 未完成
	 */
	public boolean findProcessInstanceIsCompleted(String taskid);
	
	/**
	 * 根据业务key查找批注历史
	 * @param businessKey
	 * @return
	 */
	public List<AuditHistoryVO> findCommentHistoryByBusinessKey(String businessKey);
	
	/**
	 * 根据任务id查找批注历史
	 * @param taskId
	 * @return
	 */
	public List<AuditHistoryVO> findCommentHistoryByTaskId(String taskId);
	
	/**
	 * 根据业务key查找流程实例id
	 * @param businessKey
	 * @return
	 */
	public String findProcessInstanceIDByBusinessKey(String businessKey);
	
	/**
	 * 根据业务key查找流程实例
	 * @param businessKey
	 * @return
	 */
	public ProcessInstance findProcessInstanceByBusinessKey(String businessKey);
	
	/**
	 * 根据业务key查询当前活动的坐标
	 * @param businessKey
	 * @return
	 */
	public Map<String,Object> findCoordinate(String businessKey);
	
	/**
	 * 根据任务id查询当前活动的坐标
	 * @param taskid
	 * @return
	 */
	public Map<String,Object> findCoordinateByTaskid(String taskid);
	
	/**
	 * 根据任务id查询业务key
	 * @param taskid
	 * @return
	 */
	public String findBusinessKey(String taskid);
	
	
}
