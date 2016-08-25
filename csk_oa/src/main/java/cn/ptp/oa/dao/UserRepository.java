package cn.ptp.oa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.ptp.oa.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByUserid(String userid);
	
//	@Query("select u from User u where u.department.name=?1 and u.deptLead=?2 and u.valid=?3")
//	public List<User> findByDeptNameAndDeptLeadAndValid(String deptName,boolean deptLead,boolean valid);
//	
//	public List<User> findByCompanyLeadAndValid(boolean companyLead,boolean valid);
	
//	@Query("select u from User u where u.department.id=?1 and u.deptLead=1 and u.valid='1'")
//	public User findHrLead(Integer deptid);
	
	
	public List<User> findByUserName(String userName);
}
