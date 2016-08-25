package cn.ptp.oa.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DateUtil {
	/** 年月日时分秒(无下划线) yyyyMMddHHmmss */
    public static final String dtLong                  = "yyyyMMddHHmmss";
    
    /** 完整时间 yyyy-MM-dd HH:mm:ss */
    public static final String simple                  = "yyyy-MM-dd HH:mm:ss";
    
    /** 年月日(无下划线) yyyyMMdd */
    public static final String dtShort                 = "yyyyMMdd";
	
    
    /**
     * 返回系统当前时间(精确到毫秒)
     * @return
     *      以yyyyMMddHHmmss为格式的当前系统时间
     */
	public  static String getDateTime(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(dtLong);
		return df.format(date);
	}
	
	/**
	 * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public  static String getDateFormatter(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(simple);
		return df.format(date);
	}
	
	
	
	/**
	 * 获取系统当期年月日(精确到天)，格式：yyyyMMdd
	 * @return
	 */
	public static String getDate(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(dtShort);
		return df.format(date);
	}
	
	/**
	 * 获取指定日期(精确到天)
	 * @param date
	 * @return
	 */
	public static Date getDate(Date date){
		String sdate = getDate(date,"yyyy-MM-dd");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date rdate=null;
		try {
			rdate = format.parse(sdate);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
		return rdate;
	}
	
	/**
	 * 格式化日期
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDate(Date date,String format){
		DateFormat df=new SimpleDateFormat(format);
		return df.format(date);
	}
	
	/**
	 * 产生随机的三位数
	 * @return
	 */
	public static String getThree(){
		Random rad=new Random();
		return rad.nextInt(1000)+"";
	}
	
	/**
	 * 比较日期大小，忽略时间，只是比较年月日
	 * @param date1
	 * @param date2
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int compareIgnoreCaseTime(Date date1,Date date2){
		int yearBtween = date1.getYear()-date2.getYear();
		int monthBtween = date1.getMonth() - date2.getMonth();
		int dateBtween = date1.getDate() - date2.getDate();

		if(yearBtween<0){
			return -1;
		}
		else if(yearBtween==0){
			if(monthBtween<0){
				return -1;
			}
			else if(monthBtween==0){
				if(dateBtween<0){
					return -1;
				}
				else if(dateBtween==0){
					return 0;
				}
				else{
					return 1;
				}
			}
			else{
				return 1;
			}
		}
		else{
			return 1;
		}
	}
	
	/**
	 * 获取指定日期的间隔scrollDays的日期，如scrollDays为-1则为前一天，为1则为后一天
	 * @param date
	 * @param scrollDays
	 * @return
	 */
	public static Date getScrollDay(Date date,int scrollDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, scrollDays);
		date = calendar.getTime();
		return date;
	}
	
	/**
	 * 获取指定日期的当月第一天
	 * @param date
	 * @return
	 */
	public static Date getMonthFirst(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date rtn = calendar.getTime();
		
		return getDate(rtn);
	}
	
	/**
	 * 获取当月最后一天
	 * @return
	 */
	public static Date getCurrentMonthEnd(){
		return getMonthEnd(new Date());
	}
	
	/**
	 * 获取指定日期当月最后一天
	 * @param date
	 * @return
	 */
	public static Date getMonthEnd(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);  
		calendar.set(Calendar.DAY_OF_MONTH, 0);  
		Date rtn = calendar.getTime();
		
		return getDate(rtn);

	}
	
	/**
	 * 获取当月第一天
	 * @return
	 */
	public static Date getCurrentMonthFirst(){
		return getMonthFirst(new Date());
	}
	
	/**
	 * 格式化字符串日期
	 * @param date  日期格式字符串
	 * @param format yyyy-MM-dd
	 * @return
	 */
	public static Date parseDate(String date,String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date rdate = null;
		try {
			rdate = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rdate;
	}
	
	/**
	 * 获取指定日期的间隔scrollMonth的日期，如scrollDays为-1则为前一月，为1则为后一月,如date为2014-3-30，scrollMonth为-1，则返回2014-2-28
	 * @param date
	 * @param scrollMonth
	 * @return
	 */
	public static Date getScrollMonth(Date date,int scrollMonth){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, scrollMonth); 
		date = cal.getTime();
		return date;
	}
	/**
	 * 获取指定日期的间隔scrollMonth的日期，如scrollDays为-1则为前一月，为1则为后一月,如date为2014-3-30，scrollMonth为-1，则返回2014-2-28
	 * @param date  如：2014-04-22
	 * @param format 如：yyyy-MM-dd
	 * @param scrollMonth
	 * @return
	 */
	public static Date getScrollMonth(String date,String format,int scrollMonth){
		Calendar cal=Calendar.getInstance();
		cal.setTime(parseDate(date,format));
		cal.add(Calendar.MONTH, scrollMonth); 
		Date rdate = cal.getTime();
		return rdate;
	}
	
	/**
	 * 获取指定日期前或者后scrollSeconds秒
	 * @param date
	 * @param scrollSeconds
	 * @return
	 */
	public static Date getScrollSeconds(Date date,long scrollSeconds){
		if(scrollSeconds>28*24*60*60){
			throw new RuntimeException("滚动日期不能超过28天");
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		
		int sceonds = (int)scrollSeconds % 60;
		
		long minutesScroll = scrollSeconds/60;
		int minutes = (int)minutesScroll % 60;
		
		long hourScroll = minutesScroll/60;
		int hour = (int)hourScroll%24;
		
		long dayScroll = hourScroll/24;
		int day = (int)dayScroll%24;
		
		
		cal.add(Calendar.SECOND, sceonds);
		cal.add(Calendar.MINUTE, minutes);
		cal.add(Calendar.HOUR, hour);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}

	public static void main(String args[]) throws Exception{
		long scrollSeconds=20+5*60+13*60*60;
		long hour = scrollSeconds/60;
//		System.out.println(scrollSeconds%60);
//		System.out.println(hour/60);
//		getScrollSeconds(new Date(),scrollSeconds);
//		Date date = new Date();
//		System.out.println(getDate(date,"yyyyMMdd"));
//		System.out.println(getDate(getScrollDay(date,-17),"yyyyMMdd"));
//		getFirstDayOnMonth(new Date());
//		System.out.println(getDate(getMonthEnd(new Date()),"yyyy-MM-dd"));
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Calendar cal=Calendar.getInstance();
//		cal.setTime(parseDate("2014-3-31","yyyy-MM-dd"));
//		        //cal.add(Calendar.DATE, -1);    //得到前一天
//		        cal.add(Calendar.MONTH, -1);    //得到前一个月
//		        long l_date = cal.getTimeInMillis();
//		        Date date=getScrollMonth(new Date(),-1);
//		        String s_date = format.format(date);
//		        System.out.println(s_date);
		        
//		Date d =getScrollSeconds(new Date(),scrollSeconds*(-1)); 
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
//		System.out.println(dateFormat.format(d));        
//		
//		String time_end="20150605171647";
//		String time = time_end.substring(0, 4)+"-"+time_end.substring(4, 6)+"-"+time_end.substring(6, 8)
//				+" "+time_end.substring(8, 10)+":"+time_end.substring(10, 12);
//		System.out.println(time);
//		
//		String tt = "黄蓉";
//		System.out.println(tt.matches("[\u4e00-\u9fa5]+"));
		
//		Date d = new Date();
//		System.out.println(DateUtil.getDate(d, "HH:mm"));
//		
//		System.out.println(DateUtil.parseDate("2016-02-02"+" "+"13:14", "yyyy-MM-dd HH:mm"));
		
		 int capacity = 1;
	     capacity <<= 2;
	     System.out.println(capacity);
	}
}
