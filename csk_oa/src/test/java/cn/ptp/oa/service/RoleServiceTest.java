package cn.ptp.oa.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.ptp.oa.domain.Role;
import cn.ptp.oa.domain.User;

public class RoleServiceTest {
	private ApplicationContext context=null;

	@Before
	public void setUp() throws Exception {
		context =
			    new ClassPathXmlApplicationContext(new String[] {
			    		"applicationContext_ds.xml", 
			    		"applicationContext_jpa.xml", 
			    		"applicationContext_main.xml", 
			    		"applicationContext_tx.xml",
			    		"applicationContext_jobs.xml",
			    		"applicationContext_activiti_context.xml"
			    		});
	}
	
	@Test
	public void test() {
		RoleService service = (RoleService)context.getBean(RoleService.class);
		Role entity = new Role();
		entity.setCode(Role.CHAIRMAN_CODE);
		entity.setName("董事长");
		service.save(entity);
	}
	
	@Test
	public void test1() {
//		UserService userService = (UserService)context.getBean(UserService.class);
//		RoleService service = (RoleService)context.getBean(RoleService.class);
//		User user = userService.findByCompanyLead();
//		List<Role> list = new ArrayList<Role>();
//		Role role= service.findByCode(Role.CHAIRMAN_CODE);
//		list.add(role);
//		user.setRoles(list);
//		userService.save(user);
	}
}
