package cn.ptp.oa.controller;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import cn.ptp.oa.common.OAWebUtils;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.service.UserService;

/**
 * oa入口
 * @author Administrator
 *
 */

@Controller
public class PortalController {
	@Resource private UserService userService;
	
	@RequestMapping(value="/portalindex",method = RequestMethod.GET)
	public String portalIndex(HttpServletRequest request,HttpServletResponse response,Model model){
		String userid=request.getParameter("userid");
		model.addAttribute("userid", userid);
		return "portalIndex";
	}
	
	@RequestMapping(value="/login",method = RequestMethod.GET)
	public String tologin(HttpServletRequest request,HttpServletResponse response,Model model){
		return "login";
	}
	
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public ModelAndView dologin(HttpServletRequest request,HttpServletResponse response,Model model){
		String userid = request.getParameter("userid");
		User user = userService.findByUserid(userid);
		HttpSession session = request.getSession();
		session.setAttribute(OAWebUtils.SESSION_USER_KEY, user);
		Object url = (Object)session.getAttribute("old_url");
		Object method = (Object)session.getAttribute("old_url_method");
		if(url!=null){
			try {
				System.out.println("redirect----------------"+url.toString());
				if("post".equalsIgnoreCase(method.toString())){
					String key=(String)session.getAttribute("key");
					Map map = (Map)session.getAttribute(key);
					session.removeAttribute(key);
					return new ModelAndView("redirect:"+url.toString(),map);
				}
				return new ModelAndView("redirect:"+url.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		return "redirect:/portalindex";
		return new ModelAndView("redirect:/portalindex");
	}
	
	@RequestMapping(value="/portal/rs",method = RequestMethod.GET)
	public String viewRSList(){
		return "";
	}
	
	@RequestMapping(value="/portal/wl",method = RequestMethod.GET)
	public String viewWLList(){
		return "";
	}
}
