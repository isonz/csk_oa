package cn.ptp.oa.service;

import java.util.List;

import cn.ptp.oa.domain.Department;

public interface DepartmentService {
	public List<Department> findAll();
	
	public void save(Department entity);
	
	public void delete(Integer id);
	
	public Department findById(Integer id);
}
