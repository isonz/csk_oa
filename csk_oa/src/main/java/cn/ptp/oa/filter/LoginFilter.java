package cn.ptp.oa.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.ptp.oa.common.OAWebUtils;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.util.wxutil.WeixinConfigUtils;
import cn.ptp.oa.util.wxutil.WeixinUtils;


public class LoginFilter implements Filter {
//	/**post请求时，如果session过期，则先缓存post请求的数据，登陆后继续请求时将参数重新提交*/
//	private final Map<String,Map<String,Object>> postParameterMap = new HashMap<String,Map<String,Object>>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
//		System.out.println("LoginFilter.init");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		StringBuffer url = req.getRequestURL();  
		String path = req.getServletPath();
//		String queryString  = builderQueryString(req);
		System.out.println("-------------loginfileter:"+url);
//		System.out.println(url+"?"+queryString);
		
		if(url.toString().indexOf("wxcallback")>0){
			//回调通知则放过
			chain.doFilter(request, response);
			return;
		}
		//如果是验证用户则放过
		if(url.toString().indexOf("oauth2")>0){
			chain.doFilter(request, response);
			return;
		}
		
		HttpSession session = req.getSession(true);
		User user = (User)session.getAttribute(OAWebUtils.SESSION_USER_KEY);
		if(user!=null){
			//已登录或者授权过则放过
			chain.doFilter(request, response);
			return;
		}
		
		
		String wxflag = (String) session.getAttribute(OAWebUtils.SESSION_WX_FLAG);
		if(wxflag==null){
			wxflag = request.getParameter(OAWebUtils.SESSION_WX_FLAG);
		}
		//来自微信浏览器
		if(wxflag==null){
			if(OAWebUtils.requestComefromWeixin(req)){
				wxflag = OAWebUtils.SESSION_WX_FLAG_VALUE;
			}
		}
		String siteUrl=WeixinConfigUtils.getConfigValue("siteUrl");
		
		if(url.toString().indexOf("system")>0){
			//如果是管理员登陆管理页面，则不处理
			//chain.doFilter(request, response);
			//return;
		}
		
		if(OAWebUtils.SESSION_WX_FLAG_VALUE.equalsIgnoreCase(wxflag)){
			//如果是还没登陆
			saveParameter(req);
			String redirect_url = WeixinUtils.getOAuth2Url(WeixinConfigUtils.getConfigValue("corpid"), 
					siteUrl+"/oauth2", 
					WeixinConfigUtils.getConfigValue("state"));
			System.out.println(redirect_url);
			resp.sendRedirect(redirect_url);
//			session.setAttribute("old_url", url);
			session.setAttribute("old_url", path);
			session.setAttribute("old_url_method", req.getMethod());
			return;
		}
		else{
			if(url.toString().indexOf("login")>0){
				//如果是登录则放过
				chain.doFilter(request, response);
				return;
			}
			//不是微信，跳转到登陆页面
			String redirect_url =siteUrl+"/login";
			if(!url.toString().equals(redirect_url)){
				saveParameter(req);
				resp.sendRedirect(redirect_url);
//				session.setAttribute("old_url", url);
				session.setAttribute("old_url", path);
				session.setAttribute("old_url_method", req.getMethod());
				return;
			}
		}
//		session.setAttribute("old_url", url+"?"+queryString);
//		session.setAttribute("old_url_method", req.getMethod());
		chain.doFilter(request, response);
	}
	
	/**如果需要登陆时，先缓存post请求的参数*/
	private void saveParameter(HttpServletRequest req){
		String method = req.getMethod();
		if("post".equalsIgnoreCase(method)){
			HttpSession session = req.getSession();
			Map<String,Object> map = req.getParameterMap();
			Map<String,Object> mapValue= new HashMap<String,Object>();
			Set<Entry<String, Object>>  set = map.entrySet();
			for(Entry<String,Object> entry:set){
				String k = entry.getKey();
				Object v = entry.getValue();
				mapValue.put(k, v);
			}
			String key=UUID.randomUUID().toString();
			session.setAttribute(key, mapValue);
			session.setAttribute("key", key);
			System.out.println("---------map.size:"+mapValue.size());
		}
	}
	
//	/**登陆后重定向到原请求时，添加原参数*/
//	private void addParameter(HttpServletRequest req){
//		String method = req.getMethod();
//		if("post".equalsIgnoreCase(method)){
//			HttpSession session = req.getSession();
//			req.getParameterMap().putAll(postParameterMap.get(session.getId()));
//		}
//	}

	private String builderQueryString(HttpServletRequest req){
		String method = req.getMethod();
		if("get".equalsIgnoreCase(method)){
			return req.getQueryString();
		}
		String queryString="";
		Map<String,Object> map = req.getParameterMap();
		Set<String> set = map.keySet();
		for(String key:set){
			if(queryString.equals("")){
				queryString = key+"="+buiderValue(map.get(key));
			}
			else{
				queryString = queryString+"&"+key+"="+buiderValue(map.get(key));
			}
		}
		return queryString;
	}
	
	private String buiderValue(Object value){
		String rtn="";
		if (value != null && value.getClass().isArray()) {
			Object arrays[] = (Object[])value;
			
			for(Object array:arrays){
				if("".equals(rtn)){
					rtn=(String)array;
				}
				else{
					rtn=rtn+","+(String)array;
				}
			}
		}
		return rtn;
	}
	@Override
	public void destroy() {
		
	}

}
