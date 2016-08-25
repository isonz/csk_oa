/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 * 
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package com.qq.weixin.mp.aes;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * XMLParse class
 *
 * 提供提取消息格式中的密文及生成回复消息格式的接口.
 */
public class XMLParse {

	/**
	 * 提取出xml数据包中的加密消息
	 * @param xmltext 待提取的xml字符串
	 * @return 提取出的加密消息字符串
	 * @throws AesException 
	 */
	public static Object[] extract(String xmltext) throws AesException     {
		Object[] result = new Object[3];
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader sr = new StringReader(xmltext);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);

			Element root = document.getDocumentElement();
			NodeList nodelist1 = root.getElementsByTagName("Encrypt");
			NodeList nodelist2 = root.getElementsByTagName("ToUserName");
			result[0] = 0;
//			result[1] = nodelist1.item(0).getTextContent();
//			result[2] = nodelist2.item(0).getTextContent();
			result[1] = nodelist1.item(0).getNodeValue();
			result[2] = nodelist2.item(0).getNodeValue();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.ParseXmlError);
		}
	}

	/**
	 * 生成xml消息
	 * @param encrypt 加密后的消息密文
	 * @param signature 安全签名
	 * @param timestamp 时间戳
	 * @param nonce 随机字符串
	 * @return 生成的xml字符串
	 */
	public static String generate(String encrypt, String signature, String timestamp, String nonce) {

		String format = "<xml>\n" + "<Encrypt><![CDATA[%1$s]]></Encrypt>\n"
				+ "<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n"
				+ "<TimeStamp>%3$s</TimeStamp>\n" + "<Nonce><![CDATA[%4$s]]></Nonce>\n" + "</xml>";
		return String.format(format, encrypt, signature, timestamp, nonce);

	}
	
	/**
     * 将xml字符串转化成map
     * @param xmlDoc
     * @return
     */
    public static Map<String,String> parseXMLString(String xmlDoc){
    	Map<String, String> map = new HashMap<String, String>();
    	//System.out.println("parseXMLString:"+xmlDoc);
        try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader sr = new StringReader(xmlDoc);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);

			Element root = document.getDocumentElement();
			NodeList child = root.getChildNodes();
			for(int i=0;i<child.getLength();i++){
				Node node = child.item(i);
				String name = node.getNodeName();
				Node first = node.getFirstChild();
				if(first!=null){
					short type = first.getNodeType() ;
					if(type==Node.CDATA_SECTION_NODE){
						org.w3c.dom.CharacterData cd = (org.w3c.dom.CharacterData)first;
						String value = cd.getData();
						map.put(name, value);
					}
					else{
						org.w3c.dom.Text cd = (org.w3c.dom.Text)first;
						String value = cd.getData();
						map.put(name, value);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        //System.out.println("parseXMLString result:"+map);
        return map;
    }
    
    public static void main(String args[]){
    	String xmlDoc="<xml><ToUserName><![CDATA[wxbba85a81f8a9ab3b]]></ToUserName><FromUserName><![CDATA[ke.chen]]></FromUserName><CreateTime>1457417268</CreateTime><MsgType><![CDATA[event]]></MsgType><AgentID>10</AgentID><Event><![CDATA[click]]></Event><EventKey><![CDATA[RS]]></EventKey></xml>";
    	System.out.println(parseXMLString(xmlDoc));
    }
}
