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
import cn.ptp.oa.dao.TalentDemandApplyRepository;
import cn.ptp.oa.domain.TalentDemandApply;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.exception.CustomRuntimeException;
import cn.ptp.oa.service.RoleService;
import cn.ptp.oa.service.TalentDemandApplyService;
import cn.ptp.oa.service.WorkFlowForBusinessService;
import cn.ptp.oa.service.WorkFlowService;

@Service
@Transactional
public class TalentDemandApplyServiceImpl implements TalentDemandApplyService {
	@Resource private TalentDemandApplyRepository dao;
	@Resource private WorkFlowService workFlowService;
	@Resource private RoleService roleService;

	@Override
	public TalentDemandApply findById(Integer id) {
		return dao.findOne(id);
	}

	@Override
	public void save(TalentDemandApply entity) {
		dao.save(entity);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Override
	public List<TalentDemandApply> findAll() {
		return dao.findAll();
	}

	@Override
	public void commit(Integer id) {
		TalentDemandApply entity = this.findById(id);
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
		String processDefinitionKey=TalentDemandApply.class.getSimpleName();
		String businessKey = OAUtils.builderWorkFlowBusinessKey(id,TalentDemandApply.class);
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("deptAuditUserID", deptLeadUser.getUserName());
		variables.put("ceoAuditUserID", companyLeadUser.getUserName());
		variables.put("hrAuditUserID", hrLeadUser.getUserName());
		
		workFlowService.startProcessInstance(processDefinitionKey, businessKey, variables );

	}

	@Override
	public void audit(Integer id, String taskid, String message, String auditstatus) {
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("status", auditstatus);
		workFlowService.completeTask(auditstatus,taskid,message, variables);
		boolean completed = workFlowService.findProcessInstanceIsCompleted(taskid);
		
		if(completed){
			TalentDemandApply entity = this.findById(id);
			if(OAUtils.AUDIT_OPINION_DEAGREE.equals(auditstatus)){
				entity.setTache(3);
			}
			else if(OAUtils.AUDIT_OPINION_AGREE.equals(auditstatus)){
				entity.setTache(2);
			}
			else{
				throw new RuntimeException("TalentDemandApplyServiceImpl.audit()参数auditstatus异常");
			}
			dao.save(entity);
		}

	}

	@Override
	public InputStream getResourceAsStream(Integer id) {
		String businessKey = OAUtils.builderWorkFlowBusinessKey(id,TalentDemandApply.class);
		return workFlowService.getResourceAsStreamByBusinessKey(businessKey);
	}

	@Override
	public List<AuditHistoryVO> findAuditHistory(Integer id) {
		String businessKey = OAUtils.builderWorkFlowBusinessKey(id,TalentDemandApply.class);
		List<AuditHistoryVO> list = workFlowService.findCommentHistoryByBusinessKey(businessKey);
		return list;
	}

	@Override
	public void feedback(Integer id,String feedback) {
		TalentDemandApply entity = dao.findOne(id);
		if(entity.getTache()!=2){
			throw new CustomRuntimeException("没有审核完成的申请不允许填写！");
		}
		entity.setFeedback(feedback);
		entity.setFeedbackDate(new Date());
		dao.save(entity);
	}

	@Override
	public List<TalentDemandApply> findByTache(Integer tache) {
		if(tache==null || TACHE_ALL == tache){
			return this.findAll();
		}
		return dao.findByTache(tache);
	}

}
