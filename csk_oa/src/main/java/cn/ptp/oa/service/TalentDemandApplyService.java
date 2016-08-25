package cn.ptp.oa.service;

import java.util.List;

import cn.ptp.oa.domain.TalentDemandApply;

public interface TalentDemandApplyService extends BaseService<TalentDemandApply, Integer>, WorkFlowForBusinessService {
	public void feedback(Integer id,String feedback);
	
	public List<TalentDemandApply> findByTache(Integer tache);
}
