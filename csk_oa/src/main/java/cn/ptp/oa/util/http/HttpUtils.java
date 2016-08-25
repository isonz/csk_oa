package cn.ptp.oa.util.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public class HttpUtils {
	private static Logger log = LoggerFactory.getLogger(HttpUtils.class);
	
	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String outputStr) {
		String method=requestMethod.toUpperCase();
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new X509TrustManagerImpl() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(method);

			if ("GET".equalsIgnoreCase(method))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return jsonObject;
	}
	
	public static String getPostData(HttpServletRequest request){
		 StringBuffer buffer = new StringBuffer();
		try{
			 // 从request中取得输入流
	        InputStream inputStream = request.getInputStream();
	        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
	       
	        String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			//inputStream.close();
			inputStream = null;
		}catch(Exception e){
			log.error("http request getPostData error:{}", e);
		}
		return buffer.toString();
	}
	
	private static int connectTimeOut = 5000;
	private static int readTimeOut = 10000;

	public static String doGet(String requrl) {
		HttpURLConnection url_con = null;
		String responseContent = null;
		try {

			URL url = new URL(requrl);
			url_con = (HttpURLConnection) url.openConnection();
			url_con.setRequestMethod("GET");
			System.setProperty("连接超时：", String.valueOf(connectTimeOut));
			System.setProperty("访问超时：", String.valueOf(readTimeOut));
			url_con.setDoOutput(true);//
			InputStream in = url_con.getInputStream();
			byte[] echo = new byte[10 * 1024];
			int len = in.read(echo);
			responseContent = (new String(echo, 0, len).trim());
			int code = url_con.getResponseCode();
			if (code != 200) {
				responseContent = "ERROR" + code;
			}
		} catch (Exception e) {
			System.out.println("网络故障:" + e.toString());
		} finally {
			if (url_con != null) {
				url_con.disconnect();
			}
		}
		return responseContent;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String doPost(String url, String param) {
		System.out.println("post:"+url+"参数："+param);
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// // 设置通用的请求属性
			// conn.setRequestProperty("accept", "*/*");
			// conn.setRequestProperty("connection", "Keep-Alive");
			// conn.setRequestProperty("user-agent",
			// "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	
	
//	public static String httpsRequestFile(String requestUrl,String requestMethod, String outputStr) throws Exception{
//		String method=requestMethod.toUpperCase();
//		String path = SystemUtils.getRootPath()+File.separator
//				+ConstantUtil.TEMP_FILE_FOLDER+File.separator;
//		File filePath = new File(path);
//		if(!filePath.exists()){
//			filePath.mkdir();
//		}
//		String picPath = path+UUID.randomUUID();
//		try {
//			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
//			TrustManager[] tm = { new X509TrustManagerImpl() };
//			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
//			sslContext.init(null, tm, new java.security.SecureRandom());
//			// 从上述SSLContext对象中得到SSLSocketFactory对象
//			SSLSocketFactory ssf = sslContext.getSocketFactory();
//
//			URL url = new URL(requestUrl);
//			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
//					.openConnection();
//			httpUrlConn.setSSLSocketFactory(ssf);
//
//			httpUrlConn.setDoOutput(true);
//			httpUrlConn.setDoInput(true);
//			httpUrlConn.setUseCaches(false);
//			// 设置请求方式（GET/POST）
//			httpUrlConn.setRequestMethod(method);
//
//			if ("GET".equalsIgnoreCase(method))
//				httpUrlConn.connect();
//
//			// 当有数据需要提交时
//			if (null != outputStr) {
//				OutputStream outputStream = httpUrlConn.getOutputStream();
//				// 注意编码格式，防止中文乱码
//				outputStream.write(outputStr.getBytes("UTF-8"));
//				outputStream.close();
//			}
//
//			// 将返回的输入流转换成字符串
//			InputStream inputStream = httpUrlConn.getInputStream();
//			
//			FileOutputStream fileOutputStream = new FileOutputStream(picPath);
//			byte[] data = getByteData(inputStream);
//			
//			fileOutputStream.write(data);
//			fileOutputStream.close();
//			
//			// 释放资源
//			inputStream.close();
//			inputStream = null;
//			httpUrlConn.disconnect();
//		} catch (ConnectException ce) {
//			ce.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return picPath;
//	}
	
	/**
	 * 发起https请求并获取结果
	 * @param requestUrl 请求地址
	 * @param requestMethod  请求方式（GET、POST）
	 * @param outputStr  提交的数据
	 * @return
	 */
	public static String httpsRequest(String requestUrl,String requestMethod, String outputStr) {
		String method=requestMethod.toUpperCase();
		String str = null;
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new X509TrustManagerImpl() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(method);

			if ("GET".equalsIgnoreCase(method))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			str = getStringData(inputStream);
			
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static byte[] getByteData(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1) {
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流
//		inStream.close();
		// 把outStream里的数据写入内存
		return outStream.toByteArray();
	}
	
	/**
	 * 从输入流中获取数据，输入流不关闭
	 * @param inputStream
	 * @return
	 */
	public static String getStringData(InputStream inputStream){
		 StringBuffer buffer = new StringBuffer();
		try{
			 // 从request中取得输入流
//	        InputStream inputStream = request.getInputStream();
	        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	       
	        String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			//inputStream.close();
//			inputStream = null;
		}catch(Exception e){
			e.printStackTrace();
		}
		return buffer.toString();
	}
	
//	/**
//	 * 上传文件
//	 * @param url
//	 * @param parms 
//	 * @param files
//	 * @return
//	 */
//	public static String uploadFile(String url,Map<String,String> parms,File... files){
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		HttpPost httppost = new HttpPost(url);
//		
//		MultipartEntityBuilder meb = MultipartEntityBuilder.create();
//		for(File file:files){
//			int i=0;
//			FileBody bin = new FileBody(file);
//			meb = meb.addPart("file"+i++, bin);
////					.addTextBody("openid", openid)
////					.addTextBody("type", type)
//					
//		}
//		Set<String> keySet = parms.keySet();
//		for(String key:keySet){
//			meb = meb.addTextBody(key, parms.get(key));
//		}
//		HttpEntity reqEntity = meb.build();
//		httppost.setEntity(reqEntity);
//		System.out.println("executing request " + httppost.getRequestLine());
//		String rtn = null;
//        try {
//			CloseableHttpResponse response = httpclient.execute(httppost);
//			
//			try{
//				System.out.println("打印响应状态 ---------------------------------------");
//	            // 打印响应状态  
//				System.out.println(response.getStatusLine());
//				// 获取响应对象  
//	            HttpEntity resEntity = response.getEntity();
//	            
//	            if (resEntity != null) {
//	                System.out.println("Response content length: " + resEntity.getContentLength());
//	                // 打印响应内容  
//	                rtn = EntityUtils.toString(resEntity,Charset.forName("UTF-8"));
//	                System.out.println(rtn);  
//	            }
//	            // 销毁  
//	            EntityUtils.consume(resEntity);
//			}finally {
//	            response.close();
//	        }
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//                httpclient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//		return rtn;
//	}
	
	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String doPost(String url, Map<String,String> params) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
        HttpPost post = new HttpPost(url);          //这里用上本机的某个工程做测试
        //创建参数列表
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        Set<String> keys = params.keySet();
        for(String key:keys){
        	list.add(new BasicNameValuePair(key, params.get(key)));
        }
        String rtn = null;
        try {
	        //url格式编码
	        UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list,"UTF-8");
	        post.setEntity(uefEntity);
	        System.out.println("POST 请求...." + post.getURI());
       
			CloseableHttpResponse response = httpclient.execute(post);
			
			try{
				System.out.println("打印响应状态 ---------------------------------------");
	            // 打印响应状态  
				System.out.println(response.getStatusLine());
				// 获取响应对象  
	            HttpEntity resEntity = response.getEntity();
	            
	            if (resEntity != null) {
	                System.out.println("Response content length: " + resEntity.getContentLength());
	                // 打印响应内容  
	                rtn = EntityUtils.toString(resEntity,Charset.forName("UTF-8"));
	                System.out.println(rtn);  
	            }
	            // 销毁  
	            EntityUtils.consume(resEntity);
			}finally {
	            response.close();
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }   
        return rtn;
        
	}
}
