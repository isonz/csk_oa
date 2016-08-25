package cn.ptp.oa.service;

import java.util.Date;
import java.util.List;

import cn.ptp.oa.domain.LeaveApplication;

public interface LeaveApplicationService extends WorkFlowForBusinessService{
	public void save(LeaveApplication entity);
	
	public LeaveApplication findById(Integer id);
	
	public void delete(Integer id);
	
	/**
	 * 查询开始请假日期在startDate以后并且当前环节为tache的记录
	 * @param startDate
	 * @param tache tache<0时，忽略次条件
	 * @return
	 */
	public List<LeaveApplication> findAfterDateAndTacheAndUserid(Date startDate, Integer tache,String userid) ;
	
	
}
