package cn.ptp.oa.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.ptp.oa.domain.CompTimeApply;

public interface CompTimeApplyRepository extends JpaRepository<CompTimeApply, Integer>{
	/**
	 * 查询某个日期后userid的所有申请信息
	 * @param userid
	 * @param applyDate
	 * @return
	 */
	public List<CompTimeApply> findByUseridAndApplyDateGreaterThanEqual(String userid,Date applyDate);
	public List<CompTimeApply> findByUseridAndApplyDateGreaterThanEqualAndTache(String userid,Date applyDate,Integer tache);
	
	/**
	 * 查询某个日期后的所有申请信息
	 * @param applyDate
	 * @return
	 */
	public List<CompTimeApply> findByApplyDateGreaterThanEqual(Date applyDate);
	public List<CompTimeApply> findByApplyDateGreaterThanEqualAndTache(Date applyDate,Integer tache);
}
