package cn.ptp.oa.service;

import java.util.List;

import cn.ptp.oa.domain.User;

public interface UserService {
	public void save(User entity);
	
	public void delete(Integer id);
	
	public User findById(Integer id);
	
	public User findByUserid(String userid);
	
//	public User findByDeptLead(String deptName);
	
//	public User findHrLead();
//	
//	public User findByCompanyLead();
	
	public List<User> findAll();
	
	public User findByUserName(String userName);
}
