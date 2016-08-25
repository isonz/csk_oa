package cn.ptp.oa.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ptp.oa.dao.UserRepository;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.exception.CustomRuntimeException;
import cn.ptp.oa.service.UserService;
import cn.ptp.oa.util.ConfigUtils;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Resource private UserRepository dao;

	
	@Override
	public User findByUserName(String userName) {
		List<User> list = dao.findByUserName(userName);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void save(User entity) {
		String userName = entity.getUserName();
		User exit = this.findByUserName(userName);
		if(entity.getId()==null){
			if(exit!=null){
				throw new CustomRuntimeException("用户名不能重复");
			}
		}
		
		dao.save(entity);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Override
	public User findById(Integer id) {
		return dao.findOne(id);
	}

	@Override
	public User findByUserid(String userid) {
		return dao.findByUserid(userid);
	}

//	@Override
//	public User findByDeptLead(String deptName) {
//		List<User>  list = dao.findByDeptNameAndDeptLeadAndValid(deptName, true, true);
//		return (list!=null && list.size()>0)?list.get(0):null;
//	}
//
//	@Override
//	public User findByCompanyLead() {
//		List<User>  list = dao.findByCompanyLeadAndValid(true, true);
//		return (list!=null && list.size()>0)?list.get(0):null;
//	}

	@Override
	public List<User> findAll() {
		return dao.findAll();
	}

//	@Override
//	public User findHrLead() {
//		String deptid = ConfigUtils.getConfigValue("hr_dept_id");
//		return dao.findHrLead(Integer.parseInt(deptid));
//	}
}
