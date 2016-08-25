package cn.ptp.oa.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.ptp.wx.pojo.Menu;
import net.sf.json.JSONObject;

public class WXMenuServiceTest {
	private ApplicationContext context=null;

	@Before
	public void setUp() throws Exception {
		context =
			    new ClassPathXmlApplicationContext(new String[] {
			    		"applicationContext_ds.xml", 
			    		"applicationContext_jpa.xml", 
			    		"applicationContext_main.xml", 
			    		"applicationContext_tx.xml"
			    		});
	}

	@Test
	public void test() {
		WXMenuService service = (WXMenuService)context.getBean(WXMenuService.class);
		Menu m = service.createMenu();
		String jsonMenu = JSONObject.fromObject(m).toString();
		System.out.println(jsonMenu);
	}

}
