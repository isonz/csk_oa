package cn.ptp.oa.util.wxutil;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeixinConfigUtils {
	private static Logger log = LoggerFactory.getLogger(WeixinConfigUtils.class);
	private static Properties p  = new Properties();
	
	static{
	    try {
			p.load(WeixinConfigUtils.class.getResourceAsStream("/wxqyh.properties"));
		} catch (IOException e) {
			log.info("load wxqyh.properties failed",e);
		}
	}
	
	public static String getConfigValue(String key){
		return p.getProperty(key);
	}
	
	public static String sCorpID = WeixinConfigUtils.getConfigValue("corpid");
	public static String corpsecret = WeixinConfigUtils.getConfigValue("corpsecret");
	
	public static void main(String[] args) {
		System.out.println(sCorpID);
	}
}
