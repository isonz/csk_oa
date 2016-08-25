package cn.ptp.oa.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.ptp.oa.domain.User;
import cn.ptp.oa.exception.CustomSessionException;

public class OAWebUtils {
		public static final String SESSION_USER_KEY="user";
		public static final String SESSION_WX_FLAG="weixinflag";
		public static final String SESSION_WX_FLAG_VALUE="Y";
//		public static final String SESSION_SYSTEM_FLAG="systemflag";
//		public static final String SESSION_SYSTEM_FLAG_VALUE="Y";
		
		/**
		 * 判断请求是否来自于微信浏览器
		 * @param req
		 * @return
		 */
		public static boolean requestComefromWeixin(HttpServletRequest req){
			//user-agent = Mozilla/5.0 (Linux; U; Android 5.0; zh-cn; SM-N900 Build/LRX21V) 
			//AppleWebKit/533.1 (KHTML, like Gecko)Version/4.0 MQQBrowser/5.4 TBS/025489 Mobile Safari/533.1 
			//MicroMessenger/6.3.16.49_r03ae324.780 NetType/WIFI Language/zh_CN
			String userAgent = req.getHeader("user-agent");
			System.out.println(userAgent);
			if(userAgent==null){
				return false;
			}
			if(userAgent.indexOf("MicroMessenger")>0){
				//来自微信浏览器
				return true;
			}
			return false;
		}
		/**
		 * 获取当前session用户id
		 * @param request
		 * @return
		 */
		public static String getCurrentUserid(HttpServletRequest request){
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute(SESSION_USER_KEY);
			if(user!=null) return user.getUserid();
			throw new CustomSessionException();
		}
		
		/**
		 * 获取当前session用户name
		 * @param request
		 * @return
		 */
		public static String getCurrentUserName(HttpServletRequest request){
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute(SESSION_USER_KEY);
			if(user!=null) return user.getUserName();
			throw new CustomSessionException();
		}
}
