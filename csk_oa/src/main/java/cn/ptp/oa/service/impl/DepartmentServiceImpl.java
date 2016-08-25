package cn.ptp.oa.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ptp.oa.dao.DepartmentRepository;
import cn.ptp.oa.domain.Department;
import cn.ptp.oa.service.DepartmentService;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
	
	@Resource DepartmentRepository dao;
	@Override
	public List<Department> findAll() {
		return dao.findAll();
	}
	@Override
	public void save(Department entity) {
		dao.save(entity);
	}
	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}
	@Override
	public Department findById(Integer id) {
		return dao.findOne(id);
	}

	
}
