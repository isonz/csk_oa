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
import cn.ptp.oa.dao.ShoppingApplyRepository;
import cn.ptp.oa.domain.ShoppingApply;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.exception.CustomRuntimeException;
import cn.ptp.oa.service.RoleService;
import cn.ptp.oa.service.ShoppingApplyService;
import cn.ptp.oa.service.WorkFlowService;

@Service
@Transactional
public class ShoppingApplyServiceImpl implements ShoppingApplyService {
	@Resource ShoppingApplyRepository dao;
	@Resource private RoleService roleService;
	@Resource private WorkFlowService workFlowService;

	@Override
	public List<ShoppingApply> findByUseridAndApplyDateGreaterThanEqual(String userid, Date applyDate,Integer tache) {
		if(tache==TACHE_ALL){
			return dao.findByUseridAndApplyDateGreaterThanEqual(userid, applyDate);
		}
		return dao.findByUseridAndApplyDateGreaterThanEqualAndTache(userid, applyDate, tache);
		
	}

	@Override
	public List<ShoppingApply> findByApplyDateGreaterThanEqual(Date applyDate,Integer tache) {
		if(tache==TACHE_ALL){
			return dao.findByApplyDateGreaterThanEqual(applyDate);
		}
		return dao.findByApplyDateGreaterThanEqualAndTache(applyDate, tache);
	}

	@Override
	public ShoppingApply findById(Integer id) {
		return dao.findOne(id);
	}

	@Override
	public void save(ShoppingApply entity) {
		dao.save(entity);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Override
	public List<ShoppingApply> findAll() {
		return dao.findAll();
	}

	@Override
	public void commit(Integer id) {
		ShoppingApply entity = this.findById(id);
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
		String processDefinitionKey=ShoppingApply.class.getSimpleName();
		String businessKey = OAUtils.builderWorkFlowBusinessKey(id,ShoppingApply.class);
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("deptAuditUserID", deptLeadUser.getUserName());
		variables.put("hrAuditUserID", hrLeadUser.getUserName());
		variables.put("GMAuditUserID", roleService.findGeneralManager().getUserName());
		variables.put("ceoAuditUserID", companyLeadUser.getUserName());
		variables.put("CWAuditUserID", roleService.findCwLead().getUserName());
		
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
			ShoppingApply entity = this.findById(id);
			if(OAUtils.AUDIT_OPINION_DEAGREE.equals(auditstatus)){
				entity.setTache(3);
			}
			else if(OAUtils.AUDIT_OPINION_AGREE.equals(auditstatus)){
				entity.setTache(2);
			}
			else{
				throw new RuntimeException("ShoppingApplyServiceImpl.audit()参数auditstatus异常");
			}
			dao.save(entity);
		}
	}
	
	@Override
	public InputStream getResourceAsStream(Integer id) {
		String businessKey = OAUtils.builderWorkFlowBusinessKey(id,ShoppingApply.class);
		return workFlowService.getResourceAsStreamByBusinessKey(businessKey);
	}
	
//	private String builderWorkFlowBusinessKey(Integer id){
//		String businessKey=ExtraWorkApply.class.getSimpleName()+"."+id;
//		return businessKey;
//	}
	
	@Override
	public List<AuditHistoryVO> findAuditHistory(Integer id) {
		String businessKey = OAUtils.builderWorkFlowBusinessKey(id,ShoppingApply.class);
		List<AuditHistoryVO> list = workFlowService.findCommentHistoryByBusinessKey(businessKey);
		return list;
	}
	
}
