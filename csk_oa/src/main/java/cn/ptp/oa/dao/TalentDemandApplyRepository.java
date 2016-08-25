package cn.ptp.oa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.ptp.oa.domain.TalentDemandApply;

public interface TalentDemandApplyRepository extends JpaRepository<TalentDemandApply, Integer> {
	public List<TalentDemandApply> findByTache(Integer tache);
}
