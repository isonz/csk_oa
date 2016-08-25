package cn.ptp.oa.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.ptp.oa.domain.User;

public class UserServiceTest {
	private ApplicationContext context=null;

	@Before
	public void setUp() throws Exception {
		context =
			    new ClassPathXmlApplicationContext(new String[] {
			    		"applicationContext_ds.xml", 
			    		"applicationContext_jpa.xml", 
			    		"applicationContext_main.xml", 
			    		"applicationContext_tx.xml",
			    		"applicationContext_activiti_context.xml"
			    		});
	}

	@Test
	public void test() {
//		UserService service = (UserService)context.getBean(UserService.class);
//		User user = service.findByDeptLead("电商部");
//		User user1 = service.findByCompanyLead();
//		System.out.println(user.getUserName());
//		System.out.println(user1.getUserName());
	}
}
