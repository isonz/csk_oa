package cn.ptp.oa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ptp.oa.common.OAWebUtils;
import cn.ptp.oa.domain.Department;
import cn.ptp.oa.domain.Role;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.service.DepartmentService;
import cn.ptp.oa.service.RoleService;
import cn.ptp.oa.service.UserService;

/**
 * 系统管理入口
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/system")
public class SystemController {
	@Resource private UserService userService;
	@Resource private DepartmentService deptService;
	@Resource private RoleService roleService;
	
	@RequestMapping(value="/portal",method=RequestMethod.GET)
	public String sysport(){
		
		return "system/sysportalIndex";
	}
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String systemlogin(HttpServletRequest request){
		System.out.println("sys login");
		return "system/syslogin";
	}
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String systemdologin(HttpServletRequest request){
		String userid = request.getParameter("userid");
		User user = userService.findByUserid(userid);
		request.getSession(true).setAttribute(OAWebUtils.SESSION_USER_KEY, user);
		return "redirect:/system/portal";
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String systemlogout(HttpServletRequest request){
		request.getSession().removeAttribute(OAWebUtils.SESSION_USER_KEY);
		request.getSession().invalidate();
		return "system/syslogin";
	}
	
	@RequestMapping(value="/dept/list",method=RequestMethod.GET)
	public String findDepartmentAll(Model model){
		List<Department> list = deptService.findAll();
		model.addAttribute("list", list);
		return "system/deptlist";
	}
	
	@RequestMapping(value="/dept/toupdate",method=RequestMethod.GET)
	public String toDepartmentUpdate(Integer id,Model model){
		List<Department> deptList = deptService.findAll();
		Map<Integer,String> map = new HashMap<Integer,String>();
		if(deptList!=null && deptList.size()>0){
			for(Department dept:deptList){
				map.put(dept.getId(), dept.getName());
			}
		}
		model.addAttribute("deptmap", map);
		Department department=null;
		if(id==null){
			department = new Department();
		}
		else{
			department = deptService.findById(id);
		}
		model.addAttribute(department);
		return "system/deptToUpdate";
	}
	
	@RequestMapping(value="/dept/doupdate",method=RequestMethod.POST)
	public String doDepartmentUpdate(Department department,Model model){
		if(department.getParent().getId()==null && department.getParent().getName()==null){
			department.setParent(null);
		}
		deptService.save(department);
		return "redirect:/system/dept/list";
	} 
	
	@RequestMapping(value="/dept/delete",method=RequestMethod.GET)
	public String toDepartmentDelete(Integer id,Model model){
		deptService.delete(id);
		return "redirect:/system/dept/list";
	}
	
	//用户管理
	@RequestMapping(value="/user/list",method=RequestMethod.GET)
	public String findUserAll(Model model){
		List<User> list = userService.findAll();
		model.addAttribute("list", list);
		
		return "system/userlist";
	}
	
	@RequestMapping(value="/user/toupdate",method=RequestMethod.GET)
	public String toUserUpdate(Integer id,Model model){
		List<Department> deptList = deptService.findAll();
		Map<Integer,String> map = new HashMap<Integer,String>();
		if(deptList!=null && deptList.size()>0){
			for(Department dept:deptList){
				map.put(dept.getId(), dept.getName());
			}
		}
		model.addAttribute("deptmap", map);
		List<Role> roleList = roleService.findAll();
		model.addAttribute("roles", roleList);
		User user=null;
		if(id==null){
			user = new User();
		}
		else{
			user = userService.findById(id);
			List<Role> roleListHaved = user.getRoles();
			if(roleListHaved!=null && roleListHaved.size()>0){
				String ids="";
				for(Role r:roleListHaved){
					if("".equals(ids)){
						ids = ""+r.getId();
					}
					else{
						ids = ids +","+r.getId();
					}
				}
//				ids = ids+"]";
				model.addAttribute("roleHavedIds", ids);
			}
		}
		model.addAttribute(user);
		return "system/userToUpdate";
	}
	
	@RequestMapping(value="/user/doupdate",method=RequestMethod.POST)
	public String doUserUpdate(User user,Model model){
		//角色不会变化，所以没有设置角色级联用户，为了避免保存时报瞬时对象没有保存的错误，先从数据库查询出来再设置角色
		List<Role> list = user.getRoles();
		List<Role> listRole = new ArrayList<Role>();
		if(list!=null){
			List<Role> all = roleService.findAll();
			Map<Integer,Role> map = new HashMap<Integer,Role>();
			if(all!=null){
				for(Role entity:all){
					map.put(entity.getId(), entity);
				}
			}
			for(Role role:list){
				Role entity = map.get(role.getId());
				listRole.add(entity);
			}
		}
		user.setRoles(listRole);
		userService.save(user);
		return "redirect:/system/user/list";
	}
	
	@RequestMapping(value="/user/delete",method=RequestMethod.GET)
	public String toUserDelete(Integer id,Model model){
		userService.delete(id);
		return "redirect:/system/user/list";
	}
	
	//角色管理
	@RequestMapping(value="/role/list",method=RequestMethod.GET)
	public String findRoleAll(Model model){
		List<Role> list = roleService.findAll();
		model.addAttribute("list", list);
		
		return "system/rolelist";
	}
	
	@RequestMapping(value="/role/toupdate",method=RequestMethod.GET)
	public String toRoleUpdate(Integer id,Model model){
		Role role=null;
		if(id==null){
			role = new Role();
		}
		else{
			role = roleService.findById(id);
		}
		model.addAttribute(role);
		return "system/roleToUpdate";
	}
	
	@RequestMapping(value="/role/doupdate",method=RequestMethod.POST)
	public String doRoleUpdate(Role role,Model model){
		roleService.save(role);
		return "redirect:/system/role/list";
	}
	
	@RequestMapping(value="/role/delete",method=RequestMethod.GET)
	public String toRoleDelete(Integer id,Model model){
		roleService.delete(id);
		return "redirect:/system/role/list";
	}
	
	
	//业务较复杂(manytomany)暂不做
	@ResponseBody
	@RequestMapping(value="/wx/updatedeptuser",method=RequestMethod.POST)
	public Object updateUserFromWeixinQyh(Model model){
		
		return null;
	}
}
