package cn.ptp.oa.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import cn.ptp.oa.common.OAWebUtils;
import cn.ptp.oa.util.wxutil.WeixinConfigUtils;
import cn.ptp.oa.util.wxutil.WeixinUtils;

/**
 * 全局异常处理器
 * @author Administrator
 *
 */
public class CustomHandlerExceptionResolver implements HandlerExceptionResolver {
	private static Logger log = LoggerFactory.getLogger(CustomHandlerExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, 
			Object handler,Exception ex) {
		log.error("异常捕获",ex);
		CustomException customException = null;
		
		if(ex instanceof CustomException){
			customException = ((CustomException)ex);
		}
		else if(ex instanceof CustomRuntimeException){
			if(ex instanceof CustomSessionException){
				String siteUrl=WeixinConfigUtils.getConfigValue("siteUrl");
				StringBuffer url = request.getRequestURL();  
				if(url.toString().indexOf("system")>0){
					//如果是管理员登陆管理页面，则不处理
					ModelAndView modelAndView = new ModelAndView();
					modelAndView.setViewName("system/syslogin");
					return modelAndView;
//					String redirect_url =siteUrl+"/sys/login";
//					try {
//						response.sendRedirect(redirect_url);
//						return null;
//					} catch (IOException e) {
//						log.error("异常捕获",e);
//					}
				}
				else{
					//如果是sesson异常，跳转到登陆页面
					if(OAWebUtils.requestComefromWeixin(request)){
//						String redirect_url = WeixinUtils.getOAuth2Url(WeixinConfigUtils.getConfigValue("corpid"), 
//								siteUrl+"/oauth2", 
//								WeixinConfigUtils.getConfigValue("state"));
//						try {
//							response.sendRedirect(redirect_url);
//							return null;
//						} catch (IOException e) {
//							e.printStackTrace();
//							log.error("异常捕获",e);
//						}
						ModelAndView modelAndView = new ModelAndView();
						modelAndView.setViewName("portalIndex");
						modelAndView.addObject(OAWebUtils.SESSION_WX_FLAG, OAWebUtils.SESSION_WX_FLAG_VALUE);
						return modelAndView;
					}
					else{
						ModelAndView modelAndView = new ModelAndView();
						modelAndView.setViewName("login");
						return modelAndView;
//						try {
//							if(response.isCommitted()){
//								System.out.println("------------------已提交");
//							}
//							else{
//								System.out.println("-------------------未提交");
//							}
//							response.sendRedirect(siteUrl+"/login");
//							return null;
//						} catch (IOException e) {
//							e.printStackTrace();
//							log.error("异常捕获",e);
//						}
					}
				}
//				return null;
			}
			else{
				customException = new  CustomException(ex.getMessage());
			}
		}
		else{
			ex.printStackTrace();
			customException = new  CustomException("未知错误，请联系系统管理员。"+ex.getMessage());
		}
		String message=customException.getMessage();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", message);
		modelAndView.setViewName("error");
		return modelAndView;
	}

}
