package cn.ptp.oa.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.qq.weixin.mp.aes.WXBizMsgCrypt;

import cn.ptp.oa.common.OAWebUtils;
import cn.ptp.oa.domain.User;
import cn.ptp.oa.service.UserService;
import cn.ptp.oa.service.WXMenuService;
import cn.ptp.oa.util.wxutil.WeixinConfigUtils;
import cn.ptp.oa.util.wxutil.WeixinUtils;
import cn.ptp.wx.pojo.Menu;
import cn.ptp.wx.pojo.global.AccessToken;

/**
 * 后台管理入口
 * @author Administrator
 *
 */
@Controller
public class AdminController {
	private static final Log log = LogFactory.getLog(AdminController.class);
	@Resource private WXMenuService wXMenuService;
	@Resource private UserService userService;
	
	/**
	 * 微信菜单中标志为“待办任务”菜单
	 */
	private static final String TASK_LIST_KEY="tasklist";
	/**
	 * 微信菜单中标志为“人事”菜单
	 */
	private static final String RS_KEY="rs";
	/**
	 * 微信菜单中标志为“物流”菜单
	 */
	private static final String WL_KEY="wl";
	
	
	//@Value("${wx_qyh_agentid}")
	private String agentid = WeixinConfigUtils.getConfigValue("wx_qyh_agentid");
	
	//@Value("${sToken}")
	private String sToken = WeixinConfigUtils.getConfigValue("sToken");
	
	//@Value("${sEncodingAESKey}")
	private String sEncodingAESKey = WeixinConfigUtils.getConfigValue("sEncodingAESKey");
	
	//@Value("${corpid}")
	private String sCorpID = WeixinConfigUtils.getConfigValue("corpid");
	
	private String corpsecret = WeixinConfigUtils.getConfigValue("corpsecret");
	
	/**
	 * 创建企业号应用菜单
	 * @param request
	 * @param response 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/admin/createwxmenu",method = RequestMethod.GET)
	public @ResponseBody Object createWXMenu(HttpServletRequest request,HttpServletResponse response,Model model){
		Map<String,String> map = new HashMap<String,String>();
		map.put("flag", "0");
		String accessToken=null;
		try {
			Menu menu = wXMenuService.createMenu();
			Map<String,Object> rtn_map = WeixinUtils.createMenu(menu, accessToken, agentid);
			int code = (Integer) rtn_map.get("errcode");
			String msg = (String) rtn_map.get("errmsg");
			if(code==0){
				map.put("flag", "1");
			}
			else{
				map.put("msg", msg);
			}
		} catch (Exception e) {
			log.info(e.getMessage());
			map.put("msg", e.getMessage());
		}
		
		return map;
	}
	
	@RequestMapping(value="/wxcallback",method = RequestMethod.GET)
	public void wxcallbackGet(HttpServletRequest request,HttpServletResponse response)throws Exception{
		
		WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
		
		String sVerifyMsgSig = request.getParameter("msg_signature");// HttpUtils.ParseUrl("msg_signature");
		String sVerifyTimeStamp = request.getParameter("timestamp");// HttpUtils.ParseUrl("timestamp");
		String sVerifyNonce = request.getParameter("nonce");// HttpUtils.ParseUrl("nonce");
		String sVerifyEchoStr = request.getParameter("echostr");//HttpUtils.ParseUrl("echostr");
		String sEchoStr=null; //需要返回的明文
		try {
			sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp,
					sVerifyNonce, sVerifyEchoStr);
			log.info("verifyurl echostr: " + sEchoStr);
			// 验证URL成功，将sEchoStr返回
			// HttpUtils.SetResponse(sEchoStr);
		} catch (Exception e) {
			//验证URL失败，错误原因请查看异常
			e.printStackTrace();
		}
		response.getOutputStream().write(sEchoStr.getBytes());
	}
	
	@RequestMapping(value="/wxcallback",method = RequestMethod.POST)
	public void wxcallbackPost(HttpServletRequest request,HttpServletResponse response)throws Exception{
		log.info("sToken:"+sToken);
		log.info("sEncodingAESKey:"+sEncodingAESKey);
		log.info("sCorpID:"+sCorpID);
		WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
//		String sReqData = HttpUtils.getPostData(request);
		String sReqData = WeixinUtils.parseXml(request).get("Encrypt");
		String sReqMsgSig = request.getParameter("msg_signature");
		String sReqTimeStamp = request.getParameter("timestamp");
		String sReqNonce = request.getParameter("nonce");
		
		try {
//			String sMsg = wxcpt.DecryptMsg(sReqMsgSig, sReqTimeStamp, sReqNonce, sReqData);
			Map<String,String> map = wxcpt.newDecryptXMLMsg(sReqMsgSig, sReqTimeStamp, sReqNonce, sReqData);
					//XMLParse.parseXMLString(sMsg);
			log.info(map);
			String EventKey = map.get("EventKey");
			String FromUserName = map.get("FromUserName");
			String CreateTime = map.get("CreateTime");
			String message="<a href='http://test.dev.ptpfans.com/oa/extraworkapply/tolist?userid="+FromUserName+">人事</a>";
			
			//WeixinUtils.postTextMessageTo(message, FromUserName, agentid);
			response.setStatus(200);//当接收成功后，http头部返回200表示接收ok，其他错误码一律当做失败并发起重试
		} catch (Exception e) { 
			// 解密失败，失败原因请查看异常
			e.printStackTrace();
		} 
	}
	
	/**验证后根据code获取用户id，设置session并跳转到首页*/
	@RequestMapping(value="/oauth2")
	public ModelAndView oauth2(String code,String state,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		System.out.println("oauth2");
		if(!WeixinConfigUtils.getConfigValue("state").equals(state)){
			return null;
		}
		
		String userid = WeixinUtils.getUserId(code, AccessToken.getToken(sCorpID, corpsecret).getAccess_token());
		
		User user = userService.findByUserid(userid);
		HttpSession session = request.getSession();
		session.setAttribute(OAWebUtils.SESSION_USER_KEY, user);
		session.setAttribute(OAWebUtils.SESSION_WX_FLAG,OAWebUtils.SESSION_WX_FLAG_VALUE);
		
		
		Object url = request.getSession().getAttribute("old_url");
		Object method = (Object)request.getSession().getAttribute("old_url_method");
		if(url!=null){
			try {
				System.out.println("redirect----------------"+url.toString());
				if("post".equalsIgnoreCase(method.toString())){
					String key=(String)session.getAttribute("key");
					Map map = (Map)session.getAttribute(key);
					System.out.println("---------map.size:"+map.size());
					session.removeAttribute(key);
					return new ModelAndView("redirect:"+url.toString(),map);
				}
				return new ModelAndView("redirect:"+url.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		request.getRequestDispatcher("/portalindex").forward(request, response);
		return new ModelAndView("redirect:/portalindex");
	}
}
