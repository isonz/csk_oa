package cn.ptp.oa.service;

import java.util.List;

import cn.ptp.oa.domain.Role;
import cn.ptp.oa.domain.User;

public interface RoleService {
	public List<Role> findAll();
	
	public void save(Role entity);
	
	public void delete(Integer id);
	
	public Role findById(Integer id);
	
	public Role findByCode(String code);
	
//	/**
//	 * 查找所有的部门经理
//	 * @return
//	 */
//	public List<User> findByDepartmentLead();
	
	/**
	 * 根据部门名称查找部门经理
	 * @return
	 */
	public User findByDepartmentLead(String deptName);
	
	/**
	 * 查找人事部经理角色用户
	 * @return
	 */
	public User findHrLead();
	
	/**
	 * 查找财务部经理角色用户
	 * @return
	 */
	public User findCwLead();
	
	/**
	 * 查找总经理角色用户
	 * @return
	 */
	public User findGeneralManager();
	
	/**
	 * 查找董事长角色用户
	 * @return
	 */
	public User findChairman();
}
