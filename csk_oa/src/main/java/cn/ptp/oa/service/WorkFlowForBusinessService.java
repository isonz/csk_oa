package cn.ptp.oa.service;

import java.io.InputStream;
import java.util.List;

import cn.ptp.oa.common.AuditHistoryVO;

public interface WorkFlowForBusinessService {
	public final int TACHE_ALL = -1;
	/**
	 * 提交申请
	 * @param id
	 */
	public void commit(Integer id);
	
	/**
	 * 审核
	 * @param id
	 * @param taskid
	 */
	public void audit(Integer id,String taskid,String message,String auditstatus);
	
	/**
	 * 根据业务id查询流程图片输入流
	 * @param id
	 * @return
	 */
	public InputStream getResourceAsStream(Integer id);
	
	/**
	 * 根据业务id查询审核记录
	 * @param id
	 * @return
	 */
	public List<AuditHistoryVO> findAuditHistory(Integer id);
}
