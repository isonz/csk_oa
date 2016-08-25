package cn.ptp.oa.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.ptp.oa.domain.LeaveApplication;

public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Integer> {
//	public List<LeaveApplication> findByUseridAndDateGreaterThanEqual(String userid, Date date);
	public List<LeaveApplication> findByUseridAndApplyDate(String userid, Date applyDate);
	
	public Page<LeaveApplication> findByUseridAndTacheNot(String userid,Integer tache, Pageable pageable);
	public Page<LeaveApplication> findByUseridAndTacheGreaterThanEqual(String userid,Integer tache, Pageable pageable);
	
	public List<LeaveApplication> findByUseridAndStartDateGreaterThanEqual(String userid,Date startDate);
	
	public List<LeaveApplication> findByUseridAndStartDateGreaterThanEqualAndTache(String userid,Date startDate,Integer tache);
	
}
