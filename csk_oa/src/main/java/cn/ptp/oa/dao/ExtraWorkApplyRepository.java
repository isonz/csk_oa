package cn.ptp.oa.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.ptp.oa.domain.ExtraWorkApply;

@Repository
public interface ExtraWorkApplyRepository extends JpaRepository<ExtraWorkApply, Integer>{
	public List<ExtraWorkApply> findByUseridAndDateGreaterThanEqual(String userid, Date date);
	public List<ExtraWorkApply> findByUseridAndDateGreaterThanEqualAndTache(String userid, Date date,Integer tache);
	public List<ExtraWorkApply> findByUseridAndDate(String userid, Date date);
	public List<ExtraWorkApply> findByUseridAndDateAndTache(String userid, Date date,Integer tache);
	
	public Page<ExtraWorkApply> findByUseridAndTacheNot(String userid,Integer tache, Pageable pageable);
	public Page<ExtraWorkApply> findByUseridAndTacheGreaterThanEqual(String userid,Integer tache, Pageable pageable);
}
