package cn.ptp.oa.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ptp.oa.dao.RoleRepository;
import cn.ptp.oa.domain.Department;
import cn.ptp.oa.domain.Role;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.exception.CustomRuntimeException;
import cn.ptp.oa.service.DepartmentService;
import cn.ptp.oa.service.RoleService;
import cn.ptp.oa.util.ConfigUtils;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	@Resource private RoleRepository dao;
	@Resource private DepartmentService departmentService;

	@Override
	public void save(Role entity) {
		if(entity.getId()==null){
			Role exit = dao.findByCode(entity.getCode());
			if(exit!=null){
				throw new CustomRuntimeException("角色代码不能重复");
			}
		}
		dao.save(entity);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Override
	public Role findById(Integer id) {
		return dao.findOne(id);
	}

	@Override
	public User findByDepartmentLead(String deptName) {
		User rtn = null;
		Role role = dao.findByCode(Role.DEPT_MANAGER_CODE);
		List<User> list = role.getUsers();
		if(list!=null){
			for(User user:list){
				if(user.isValid()){
					Department dept = user.getDepartment();
					if(deptName.equals(dept.getName())){
						rtn = user;
						break;
					}
				}
			}
		}
		
		return rtn;
	}

	@Override
	public User findHrLead() {
		String deptid = ConfigUtils.getConfigValue("hr_dept_id");
		Department dept = departmentService.findById(Integer.parseInt(deptid));
		String deptName = dept.getName();
		return findByDepartmentLead(deptName);
	}

	@Override
	public User findCwLead() {
		String deptid = ConfigUtils.getConfigValue("cw_dept_id");
		Department dept = departmentService.findById(Integer.parseInt(deptid));
		String deptName = dept.getName();
		return findByDepartmentLead(deptName);
	}

	@Override
	public User findGeneralManager() {
		User rtn = null;
		Role role = dao.findByCode(Role.GENERALMANAGER_CODE);
		List<User> list = role.getUsers();
		if(list!=null){
			for(User user:list){
				if(user.isValid()){
					rtn = user;
					break;
				}
			}
		}
		return rtn;
	}

	@Override
	public User findChairman() {
		User rtn = null;
		Role role = dao.findByCode(Role.CHAIRMAN_CODE);
		List<User> list = role.getUsers();
		if(list!=null){
			for(User user:list){
				if(user.isValid()){
					rtn = user;
					break;
				}
			}
		}
		return rtn;
	}

	@Override
	public Role findByCode(String code) {
		return dao.findByCode(code);
	}

	@Override
	public List<Role> findAll() {
		return dao.findAll();
	}

}
