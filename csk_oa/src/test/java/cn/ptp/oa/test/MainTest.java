package cn.ptp.oa.test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

import org.junit.Test;


public class MainTest {
	@Test
	public void test1(){
		URI uri = null;
		try {
			uri = MainTest.class.getClassLoader().getResource("diagrams").toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		File file = new File(uri);
		System.out.println(file.getPath());
		String files[] = file.list();
		for(String f:files){
			String splits[] = f.split("\\.");
			System.out.println(splits[0]);
			System.out.println("--");
			System.out.println(splits[1]);
		}
		File fs[] = file.listFiles();
		for(File f:fs){
			System.out.println(f.getName());
		}
	}
	
	@Test
	public void test2() throws Exception{
		System.out.println(UUID.fromString(UUID.randomUUID().toString()));
		System.out.println(UUID.randomUUID().toString());
		String s="%E7%94%B5%E5%95%86%E9%83%A8";
		String enc="utf-8";
		enc="iso-8859-1";
		System.out.println(URLDecoder.decode(s, enc));
		
		String ms = "çµåé¨";
		String m = URLEncoder.encode(ms, enc);
		System.out.println(m);
	}
}
