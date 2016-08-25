package cn.ptp.oa.common;

public class OAUtils {
	public static final String AUDIT_OPINION_AGREE="批准";
	public static final String AUDIT_OPINION_DEAGREE="不批准";
	public static String builderWorkFlowBusinessKey(Integer id,Class clazz){
		String businessKey=clazz.getSimpleName()+"."+id;
		return businessKey;
	}
	
	public static String builderWorkFlowBusinessKey(String id,Class clazz){
		String businessKey=clazz.getSimpleName()+"."+id;
		return businessKey;
	}
	
	public static String builderWorkFlowBusinessKey(String id,String type){
		String businessKey=type+"."+id;
		return businessKey;
	}
}
