package cn.ptp.oa.util.wxutil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.ptp.oa.util.http.HttpUtils;
import cn.ptp.wx.pojo.Menu;
import cn.ptp.wx.pojo.global.AccessToken;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

public class WeixinUtils {
	private static Logger log = LoggerFactory.getLogger(WeixinUtils.class);
	
	/*###################################创建应用菜单#######################################*/
	/**
	 * 创建应用菜单URL
	 */
	private final static String MENU_CREATE_URL="https://qyapi.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN&agentid=AGENTID";
	/**
	 * 创建菜单
	 * 
	 * @param menu
	 *            菜单实例
	 * @param accessToken
	 *            有效的access_token
	 * @param agentid
	 * 			   企业应用的id
	 * @return 
	 * 		map.put("errcode", result);0表示成功，其他值表示失败 
			map.put("errmsg", msg);
	 */
	public static Map<String,Object> createMenu(Menu menu, String accessToken,String agentid) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("errcode", 0);

		// 拼装创建菜单的url
		String url = MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken).replace("AGENTID", agentid);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.fromObject(menu).toString();
		System.out.println(accessToken);
		System.out.println(jsonMenu);
		// 调用接口创建菜单
		JSONObject jsonObject = HttpUtils.httpRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			int result = jsonObject.getInt("errcode");
			String msg = jsonObject.getString("errmsg");
			if (0 != result) {
				log.error("创建菜单失败 errcode:{} errmsg:{}",result,msg);
			}
			map.put("errcode", result);
			map.put("errmsg", msg);
		}

		return map;
	}
	
	/*###################################OAuth2.0验证接口说明#######################################*/
	/**OAuth2.0验证接口说明*/
	private final static String OAUTH2_AUTHORIZE_URL="https://open.weixin.qq.com/connect/oauth2/authorize?appid=CORPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	
	/**
	 * 获取身份验证链接,用户点击后，页面将跳转至 redirect_uri?code=CODE&state=STATE，
	 * 企业可根据code参数获得员工的userid。
	 * @param CorpID
	 * @param redirect_uri
	 * @param state
	 * @return
	 */
	public static String getOAuth2Url(String CorpID,String redirect_uri,String state){
		URLCodec codec = new URLCodec();
		try {
			redirect_uri = codec.encode(redirect_uri);
		} catch (EncoderException e) {
			log.info("WeixinUtils.getOAuth2Url()：codec.encode(redirect_uri)", e);
			throw new RuntimeException(e);
		}
		return OAUTH2_AUTHORIZE_URL.replaceAll("CORPID", CorpID)
				.replaceAll("REDIRECT_URI", redirect_uri)
				.replaceAll("STATE", state)
				.replaceAll("SCOPE", "snsapi_base")
				;
	}
	
	/*###################################根据code获取成员信息#######################################*/
	private final static String GET_USER_INFO_URL="https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE";
	
	/**
	 * 根据oauth2获取的code获取用户id
	 * @param code
	 * @param access_token
	 * @return
	 */
	public static String getUserId(String code,String access_token){
		String url = GET_USER_INFO_URL.replaceAll("ACCESS_TOKEN", access_token).replaceAll("CODE", code);
		JSONObject jsonObject = HttpUtils.httpRequest(url, "get", null);
//		String errcode = (String)jsonObject.get("errcode");
		System.out.println(jsonObject);
		String userid = (String)jsonObject.get("UserId");
		return userid;
	}
	
	/*###################################获取部门列表#######################################*/
	private final static String GET_DEPARTMENT_LIST_URL="https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=ACCESS_TOKEN&id=ID";
	
	/**
	 * 根据企业通讯录部门id获取部门列表
	 * @param access_token
	 * @param id  id为0则查询所有部门
	 * @return
	 */
	public static JSONObject getDepartmentList(String access_token,Integer id){
		String url = GET_DEPARTMENT_LIST_URL.replaceAll("ACCESS_TOKEN", access_token).replaceAll("ID", id+"");
		JSONObject jsonObject = HttpUtils.httpRequest(url, "get", null);
		System.out.println(jsonObject);
		return jsonObject;
	}
	
	
	/*###################################获取部门成员#######################################*/
	private final static String GET_DEPARTMENT_USER_LIST_URL="https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD&status=STATUS";
	
	public static JSONObject getDepartmentUserList(String access_token,Integer id){
		String url = GET_DEPARTMENT_USER_LIST_URL.replaceAll("ACCESS_TOKEN", access_token)
				.replaceAll("DEPARTMENT_ID", id+"")
				.replace("FETCH_CHILD", "1")
				.replace("STATUS", "0")
				;
		JSONObject jsonObject = HttpUtils.httpRequest(url, "get", null);
		System.out.println(jsonObject);
		return jsonObject;
	}
	
	/*###################################获取部门成员(详情)#######################################*/
	private final static String GET_DEPARTMENT_USER_DETAIL_LIST_URL="https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD&status=STATUS";
	
	public static JSONObject getDepartmentUserDetailList(String access_token,Integer id){
		String url = GET_DEPARTMENT_USER_DETAIL_LIST_URL.replaceAll("ACCESS_TOKEN", access_token)
				.replaceAll("DEPARTMENT_ID", id+"")
				.replace("FETCH_CHILD", "1")
				.replace("STATUS", "0")
				;
		JSONObject jsonObject = HttpUtils.httpRequest(url, "get", null);
		System.out.println(jsonObject);
		return jsonObject;
	}
	
	/*###################################发送接口说明#######################################*/
	private final static String POST_MESSAGE_URL="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";
	
	/**发送接口*/
	public static int postTextMessageTo(String access_token,String message,String users,String agentid){
		String url = POST_MESSAGE_URL.replaceAll("ACCESS_TOKEN", access_token);
		Map<String,Object> map = new LinkedHashMap<String, Object>();
		map.put("touser", users);
		map.put("msgtype", "text");
		map.put("agentid", agentid);
		
		Map<String,Object> content = new LinkedHashMap<String, Object>();
		content.put("content", message);
		map.put("text", content);
		
		String outputStr = JSONObject.fromObject(map).toString();
		String rtn = HttpUtils.httpsRequest(url, "POST", outputStr);
		System.out.println(rtn);
		JSONObject jsonObject = JSONObject.fromObject(rtn);
		int ok = -1;
		if(jsonObject!=null){
			ok = jsonObject.getInt("errcode");
		}
		return ok;
	}
	
	/**发送接口*/
	public static int postTextMessageTo(String message,String users,String agentid){
		String access_token = AccessToken.getToken(WeixinConfigUtils.sCorpID, WeixinConfigUtils.corpsecret).getAccess_token();
		return postTextMessageTo(access_token, message, users, agentid);
	}
	
	/**发送接口*/
	public static int postTextMessageTo(String message,String users){
		String agentid = WeixinConfigUtils.getConfigValue("wx_qyh_agentid");
		return  postTextMessageTo(message, users, agentid);
	}
	
	
	public static void main(String args[]){
		postTextMessageTo("xxx提交了加班申请，请审核1","ke.chen","16");
//		String sCorpID= WeixinConfigUtils.getConfigValue("corpid");
//		String corpsecret= WeixinConfigUtils.getConfigValue("corpsecret");
//				
//		JSONObject jObject =getDepartmentUserList(AccessToken.getToken(sCorpID, corpsecret).getAccess_token(),8);
//		
//		String kfid="";
//		JSONArray jarray = jObject.getJSONArray("userlist");
//		@SuppressWarnings("unchecked")
//		Iterator<Map<String,String>> it = jarray.iterator();
//		while(it.hasNext()){
//			Map<String,String> map = it.next();
//			String userid = map.get("userid");
//			if("".equals(kfid)){
//				kfid = userid;
//			}
//			else{
//				kfid = kfid+","+userid;
//			}
//		}
//		System.out.println(kfid);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/** 
     * 解析微信发来的请求（XML） 
     * 
     * @param request
     * @return
     * @throws Exception
     */  
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素 
        Element root = document.getRootElement();
        // 得到根元素的所有子节点 
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList)
            map.put(e.getName(), e.getText());

        // 释放资源  
        inputStream.close();
        inputStream = null;

        return map;
    }
    
    
}
