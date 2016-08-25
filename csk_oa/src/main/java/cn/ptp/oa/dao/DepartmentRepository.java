package cn.ptp.oa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.ptp.oa.domain.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
	
}
