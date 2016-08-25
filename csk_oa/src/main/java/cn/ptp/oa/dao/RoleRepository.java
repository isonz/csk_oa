package cn.ptp.oa.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.ptp.oa.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	public Role findByCode(String code);
}
