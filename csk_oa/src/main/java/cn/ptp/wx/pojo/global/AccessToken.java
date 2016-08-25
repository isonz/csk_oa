package cn.ptp.wx.pojo.global;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.ptp.oa.util.http.HttpUtils;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class AccessToken {
	private static Logger log = LoggerFactory.getLogger(AccessToken.class);
	private static final String url="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=CORPID&corpsecret=CORPSECRET";
	private static AccessToken cache = new AccessToken();//全局缓存
	private static final Lock lock = new ReentrantLock();
	
	/**
	 * 从微信服务器获取token并更新缓存token
	 * @return
	 */
	private static AccessToken getTokenFromWeixinServerAndUpdateCache(String corpid,String corpsecret){
		String requestUrl = url.replace("CORPID", corpid).replace("CORPSECRET", corpsecret);
		JSONObject jsonObject = HttpUtils.httpRequest(requestUrl, "GET", null);
//		AccessToken accessToken = null;
		// 如果请求成功
		if (null != jsonObject) {
			try {
				lock.lock();
//				accessToken =new AccessToken();
				long current = System.currentTimeMillis();
				String token = jsonObject.getString("access_token");
				int expires_in = jsonObject.getInt("expires_in");
//				accessToken.setCurrentTimes(current);
//				accessToken.setAccess_token(token);
//				accessToken.setExpires_in(expires_in);
				cache.setAccess_token(token);
				cache.setExpires_in(expires_in);
				cache.setCurrentTimes(current);
			} catch (JSONException e) {
				// 获取token失败
				log.error("获取token失败 errcode:{} errmsg:{}",
						jsonObject.getInt("errcode"),
						jsonObject.getString("errmsg"));
			}finally{ 
				lock.unlock();
			}
		}
		return cache;
	}
	
	/**
	 * 获取全局token
	 * @param corpid
	 * @param corpsecret
	 * @return
	 */
	public static AccessToken getToken(String corpid,String corpsecret){
		//先从缓存中获取，如果不存在或者超时，重新从微信服务器请求
		long currentTimes = System.currentTimeMillis();
		if(cache.getAccess_token()==null){
			//不存在
			return getTokenFromWeixinServerAndUpdateCache(corpid,corpsecret);
		}
		else {
			long endtime = cache.getCurrentTimes() + cache.getExpires_in()*1000;
			if(currentTimes>=endtime){
				//超时
				return getTokenFromWeixinServerAndUpdateCache(corpid,corpsecret);
			}
		}
		
		return cache;
	}
	
	// 获取到的凭证  
    private String access_token;  
    // 凭证有效时间，单位：秒  
    private int expires_in;  
    
    private long currentTimes;//当前时间
  
  
    public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	public long getCurrentTimes() {
		return currentTimes;
	}

	public void setCurrentTimes(long currentTimes) {
		this.currentTimes = currentTimes;
	}  
}
