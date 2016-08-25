package cn.ptp.oa.service;

import java.util.Date;
import java.util.List;

import cn.ptp.oa.domain.ShoppingApply;

public interface ShoppingApplyService extends BaseService<ShoppingApply,Integer>,WorkFlowForBusinessService{
	/**
	 * 查询某个日期后userid的所有申请信息
	 * @param userid
	 * @param applyDate
	 * @return
	 */
	public List<ShoppingApply> findByUseridAndApplyDateGreaterThanEqual(String userid,Date applyDate,Integer tache);
	
	/**
	 * 查询某个日期后的所有申请信息
	 * @param applyDate
	 * @return
	 */
	public List<ShoppingApply> findByApplyDateGreaterThanEqual(Date applyDate,Integer tache);
}
