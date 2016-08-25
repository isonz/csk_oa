package cn.ptp.oa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import cn.ptp.oa.domain.WXMenu;

@org.springframework.stereotype.Repository
public interface WXMenuRepository extends Repository<WXMenu, Integer> {
	@Query("from WXMenu u order by orderNo")
	public List<WXMenu> findOrderByOrderNo();
}
