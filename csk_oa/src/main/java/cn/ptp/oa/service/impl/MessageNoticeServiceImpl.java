package cn.ptp.oa.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import cn.ptp.oa.domain.Role;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.service.MessageNoticeService;
import cn.ptp.oa.service.RoleService;
import cn.ptp.oa.service.UserService;
import cn.ptp.oa.service.WorkFlowService;
import cn.ptp.oa.util.wxutil.WeixinConfigUtils;
import cn.ptp.oa.util.wxutil.WeixinUtils;

@Service("messageNoticeService")
public class MessageNoticeServiceImpl implements MessageNoticeService {
	@Resource private WorkFlowService wokFlowService;
	@Resource private UserService userService;
	@Resource private RoleService roleService;
	private final static String message = "您有%d条待办事项需要处理!";
	@Override
	public void notice() {
		Role chairmanRole = roleService.findByCode(Role.CHAIRMAN_CODE);
		Role deptManaagerRole = roleService.findByCode(Role.DEPT_MANAGER_CODE);
		Role generalManagerRole = roleService.findByCode(Role.GENERALMANAGER_CODE);
		List<User> listUser = new ArrayList<User>();
		listUser.addAll(chairmanRole.getUsers());
		listUser.addAll(deptManaagerRole.getUsers());
		listUser.addAll(generalManagerRole.getUsers());
		if(listUser!=null && listUser.size()>0){
			for(User user:listUser){
				send(user);
			}
		}
	}

	@Override
	public void notice(String userid) {
		User user = userService.findByUserid(userid);
		send(user);
	}

	private void send(User user){
		long count = wokFlowService.findTaskCountByUser(user.getUserName());
		WeixinUtils.postTextMessageTo(String.format(message, count), user.getUserid(), 
				WeixinConfigUtils.getConfigValue("wx_qyh_agentid"));
	}
	
	public static void main(String[] args) {
		System.out.println(String.format(message, 12));
	}
	
	public static void send(String userid,String message){
		
	}
}
