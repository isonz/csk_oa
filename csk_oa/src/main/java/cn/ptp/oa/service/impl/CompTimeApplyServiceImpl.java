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
import cn.ptp.oa.dao.CompTimeApplyRepository;
import cn.ptp.oa.domain.CompTimeApply;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.exception.CustomRuntimeException;
import cn.ptp.oa.service.CompTimeApplyService;
import cn.ptp.oa.service.RoleService;
import cn.ptp.oa.service.WorkFlowService;

@Service
@Transactional
public class CompTimeApplyServiceImpl implements CompTimeApplyService {
	@Resource private CompTimeApplyRepository dao;
	@Resource private RoleService roleService;
	@Resource private WorkFlowService workFlowService;
	
	@Override
	public CompTimeApply findById(Integer id) {
		return dao.findOne(id);
	}

	@Override
	public void save(CompTimeApply entity) {
		dao.save(entity);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Override
	public List<CompTimeApply> findAll() {
		return dao.findAll();
	}

	@Override
	public List<CompTimeApply> findByUseridAndApplyDateGreaterThanEqual(String userid, Date applyDate,Integer tache) {
		if(tache==null || TACHE_ALL == tache){
			return dao.findByUseridAndApplyDateGreaterThanEqual(userid, applyDate);
		}
		return dao.findByUseridAndApplyDateGreaterThanEqualAndTache(userid, applyDate,tache);
	}

	@Override
	public List<CompTimeApply> findByApplyDateGreaterThanEqual(Date applyDate,Integer tache) {
		if(tache==null || TACHE_ALL == tache){
			return dao.findByApplyDateGreaterThanEqual(applyDate);
		}
		return dao.findByApplyDateGreaterThanEqualAndTache(applyDate,tache);
	}

	@Override
	public void commit(Integer id) {
		CompTimeApply entity = this.findById(id);
		int tache = entity.getTache();
		if(tache>0){
			throw new CustomRuntimeException("只能提交状态为未提交的记录");
		}
		entity.setTache(1);
		dao.save(entity);
		String deptName= entity.getDept();
		
		User deptLeadUser = roleService.findByDepartmentLead(deptName);
//		User companyLeadUser = userService.findByCompanyLead();
//		User hrLeadUser = userService.findHrLead();
		String processDefinitionKey=CompTimeApply.class.getSimpleName();
		String businessKey = OAUtils.builderWorkFlowBusinessKey(id,CompTimeApply.class);
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("deptAuditUserID", deptLeadUser.getUserName());
//		variables.put("ceoAuditUserID", companyLeadUser.getUserName());
//		variables.put("hrAuditUserID", hrLeadUser.getUserName());
//		variables.put("days", entity.getDays());
		
		workFlowService.startProcessInstance(processDefinitionKey, businessKey, variables );
	}

	@Override
	public void audit(Integer id, String taskid,String message,String auditstatus) {
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("status", auditstatus);
		workFlowService.completeTask(auditstatus,taskid,message, variables);
		boolean completed = workFlowService.findProcessInstanceIsCompleted(taskid);
		
		if(completed){
			CompTimeApply entity = this.findById(id);
			if(OAUtils.AUDIT_OPINION_DEAGREE.equals(auditstatus)){
				entity.setTache(3);
			}
			else if(OAUtils.AUDIT_OPINION_AGREE.equals(auditstatus)){
				entity.setTache(2);
			}
			else{
				throw new RuntimeException("CompTimeApplyServiceImpl.audit()参数auditstatus异常");
			}
			dao.save(entity);
		}
	}
	
	@Override
	public InputStream getResourceAsStream(Integer id) {
		String businessKey = OAUtils.builderWorkFlowBusinessKey(id,CompTimeApply.class);
		return workFlowService.getResourceAsStreamByBusinessKey(businessKey);
	}
	
//	private String builderWorkFlowBusinessKey(Integer id){
//		String businessKey=ExtraWorkApply.class.getSimpleName()+"."+id;
//		return businessKey;
//	}
	
	@Override
	public List<AuditHistoryVO> findAuditHistory(Integer id) {
		String businessKey = OAUtils.builderWorkFlowBusinessKey(id,CompTimeApply.class);
		List<AuditHistoryVO> list = workFlowService.findCommentHistoryByBusinessKey(businessKey);
		return list;
	}

	
}
