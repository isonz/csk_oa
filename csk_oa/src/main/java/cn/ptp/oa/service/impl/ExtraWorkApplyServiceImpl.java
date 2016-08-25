package cn.ptp.oa.service.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ptp.oa.common.AuditHistoryVO;
import cn.ptp.oa.common.OAUtils;
import cn.ptp.oa.dao.ExtraWorkApplyRepository;
import cn.ptp.oa.domain.ExtraWorkApply;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.exception.CustomRuntimeException;
import cn.ptp.oa.service.ExtraWorkApplyService;
import cn.ptp.oa.service.RoleService;
import cn.ptp.oa.service.WorkFlowService;

@Service
@Transactional
public class ExtraWorkApplyServiceImpl implements ExtraWorkApplyService {
	@Resource private ExtraWorkApplyRepository dao;
	@Resource private WorkFlowService workFlowService;
	@Resource private RoleService roleService;

	@Override
	public void save(ExtraWorkApply entity) {
		dao.save(entity);
	}

	@Override
	public void delete(Integer id) {
		ExtraWorkApply entity = this.findById(id);
		if(entity.getTache()!=0){
			throw new CustomRuntimeException("已提交的数据不允许删除");
		}
		dao.delete(id);
	}

	@Override
	public ExtraWorkApply findById(Integer id) {
		return dao.findOne(id);
	}

	@Override
	public Page<ExtraWorkApply> findUnCompleteSortByDate(String userid, int page, int size) {
		return dao.findByUseridAndTacheNot(userid, 2,new PageRequest(page,size));
	}

	@Override
	public Page<ExtraWorkApply> findCompletedSortByDate(String userid, int page, int size) {
		return dao.findByUseridAndTacheGreaterThanEqual(userid, 2,new PageRequest(page,size));
	}
	
	
	@Override
	public void commit(Integer id) {
		ExtraWorkApply entity = this.findById(id);
		int tache = entity.getTache();
		if(tache>0){
			throw new CustomRuntimeException("只能提交状态为未提交的记录");
		}
		entity.setTache(1);
		dao.save(entity);
		String deptName= entity.getDept();
		User deptLeadUser = roleService.findByDepartmentLead(deptName);
		User companyLeadUser = roleService.findChairman();
		String processDefinitionKey=ExtraWorkApply.class.getSimpleName();
		String businessKey = OAUtils.builderWorkFlowBusinessKey(id,ExtraWorkApply.class);
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("deptAuditUserID", deptLeadUser.getUserName());
		variables.put("ceoAuditUserID", companyLeadUser.getUserName());
		
		workFlowService.startProcessInstance(processDefinitionKey, businessKey, variables );
	}

	@Override
	public void audit(Integer id, String taskid,String message,String auditstatus) {
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("status", auditstatus);
		workFlowService.completeTask(auditstatus,taskid,message, variables);
		boolean completed = workFlowService.findProcessInstanceIsCompleted(taskid);
		
		if(completed){
			ExtraWorkApply entity = this.findById(id);
			if(OAUtils.AUDIT_OPINION_DEAGREE.equals(auditstatus)){
				entity.setTache(3);
			}
			else if(OAUtils.AUDIT_OPINION_AGREE.equals(auditstatus)){
				entity.setTache(2);
			}
			else{
				throw new RuntimeException("ExtraWorkApplyServiceImpl.audit()参数auditstatus异常");
			}
			dao.save(entity);
		}
	}
	
	@Override
	public InputStream getResourceAsStream(Integer id) {
		String businessKey = OAUtils.builderWorkFlowBusinessKey(id,ExtraWorkApply.class);
		return workFlowService.getResourceAsStreamByBusinessKey(businessKey);
	}
	
//	private String builderWorkFlowBusinessKey(Integer id){
//		String businessKey=ExtraWorkApply.class.getSimpleName()+"."+id;
//		return businessKey;
//	}
	
	@Override
	public List<AuditHistoryVO> findAuditHistory(Integer id) {
		String businessKey = OAUtils.builderWorkFlowBusinessKey(id,ExtraWorkApply.class);
		List<AuditHistoryVO> list = workFlowService.findCommentHistoryByBusinessKey(businessKey);
		return list;
	}
	
//	@Override
//	public Map<String, Object> viewAuditHistory(Integer id) {
//		Map<String, Object> map = new HashMap<String,Object>();
//		List<AuditHistoryVO> list = this.findAuditHistory(id);
//		map.put("list", list);
//		
//		return null;
//	}
}
