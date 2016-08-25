package cn.ptp.oa.service;

import java.util.Date;
import java.util.List;

import cn.ptp.oa.domain.CompTimeApply;

public interface CompTimeApplyService extends BaseService<CompTimeApply,Integer>,WorkFlowForBusinessService{
	/**
	 * 查询某个日期后userid的所有申请信息
	 * @param userid
	 * @param applyDate
	 * @return
	 */
	public List<CompTimeApply> findByUseridAndApplyDateGreaterThanEqual(String userid,Date applyDate,Integer tache);
	
	/**
	 * 查询某个日期后的所有申请信息
	 * @param applyDate
	 * @return
	 */
	public List<CompTimeApply> findByApplyDateGreaterThanEqual(Date applyDate,Integer tache);
}
