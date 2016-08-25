package cn.ptp.oa.service.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ptp.oa.common.AuditHistoryVO;
import cn.ptp.oa.common.OAUtils;
import cn.ptp.oa.dao.LeaveApplicationRepository;
import cn.ptp.oa.domain.LeaveApplication;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.exception.CustomRuntimeException;
import cn.ptp.oa.service.LeaveApplicationService;
import cn.ptp.oa.service.RoleService;
import cn.ptp.oa.service.WorkFlowService;

@Service
@Transactional
public class LeaveApplicationServiceImpl implements LeaveApplicationService {
	@Resource private WorkFlowService workFlowService;
	@Resource private LeaveApplicationRepository dao;
	@Resource private RoleService roleService;

	@Override
	public void save(LeaveApplication entity) {
		dao.save(entity);
	}

	@Override
	public LeaveApplication findById(Integer id) {
		return dao.findOne(id);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Override
	public List<LeaveApplication> findAfterDateAndTacheAndUserid(Date startDate, Integer tache,String userid) {
		if(tache<0){
			return dao.findByUseridAndStartDateGreaterThanEqual(userid, startDate);
		}
		return dao.findByUseridAndStartDateGreaterThanEqualAndTache(userid, startDate, tache);
	}

	@Override
	public void commit(Integer id) {
		LeaveApplication entity = this.findById(id);
		int tache = entity.getTache();
		if(tache>0){
			throw new CustomRuntimeException("只能提交状态为未提交的记录");
		}
		entity.setTache(1);
		dao.save(entity);
		String deptName= entity.getDept();
		User deptLeadUser = roleService.findByDepartmentLead(deptName);
		User companyLeadUser = roleService.findChairman();
		User hrLeadUser = roleService.findHrLead();
		String processDefinitionKey=LeaveApplication.class.getSimpleName();
		String businessKey = OAUtils.builderWorkFlowBusinessKey(id,LeaveApplication.class);
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("deptAuditUserID", deptLeadUser.getUserName());
		variables.put("ceoAuditUserID", companyLeadUser.getUserName());
		variables.put("hrAuditUserID", hrLeadUser.getUserName());
		variables.put("days", entity.getDays());
		
		workFlowService.startProcessInstance(processDefinitionKey, businessKey, variables );
	}

	@Override
	public void audit(Integer id, String taskid,String message,String auditstatus) {
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("status", auditstatus);
		workFlowService.completeTask(auditstatus,taskid,message, variables);
		boolean completed = workFlowService.findProcessInstanceIsCompleted(taskid);
		
		if(completed){
			LeaveApplication entity = this.findById(id);
			if(OAUtils.AUDIT_OPINION_DEAGREE.equals(auditstatus)){
				entity.setTache(3);
			}
			else if(OAUtils.AUDIT_OPINION_AGREE.equals(auditstatus)){
				entity.setTache(2);
			}
			else{
				throw new RuntimeException("LeaveApplicationServiceImpl.audit()参数auditstatus异常");
			}
			dao.save(entity);
		}
	}
	
	@Override
	public InputStream getResourceAsStream(Integer id) {
		String businessKey = OAUtils.builderWorkFlowBusinessKey(id,LeaveApplication.class);
		return workFlowService.getResourceAsStreamByBusinessKey(businessKey);
	}
	
	
	@Override
	public List<AuditHistoryVO> findAuditHistory(Integer id) {
		String businessKey = OAUtils.builderWorkFlowBusinessKey(id,LeaveApplication.class);
		List<AuditHistoryVO> list = workFlowService.findCommentHistoryByBusinessKey(businessKey);
		return list;
	}

}
