package cn.ptp.oa.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import cn.ptp.oa.common.AuditHistoryVO;
import cn.ptp.oa.domain.ExtraWorkApply;

public interface ExtraWorkApplyService extends WorkFlowForBusinessService{
	
	
	public void save(ExtraWorkApply entity);
	
	public void delete(Integer id);
	
	public ExtraWorkApply findById(Integer id);
	
	/**
	 * 查找未审核完成的记录，并按日期排序
	 * @return
	 */
	public Page<ExtraWorkApply> findUnCompleteSortByDate(String userid, int page, int size);
	
	/**
	 * 查找已审核完成的记录
	 * @param size 页大小
	 * @param page  第几页
	 * @return
	 */
	public Page<ExtraWorkApply> findCompletedSortByDate(String userid,int page, int size);
	
//	/**
//	 * 提交申请
//	 * @param id
//	 */
//	public void commit(Integer id);
//	
//	/**
//	 * 审核
//	 * @param id
//	 * @param taskid
//	 */
//	public void audit(Integer id,String taskid,String message,String auditstatus);
//	
//	/**
//	 * 根据业务id查询流程图片输入流
//	 * @param id
//	 * @return
//	 */
//	public InputStream getResourceAsStream(Integer id);
//	
//	/**
//	 * 根据业务id查询审核记录
//	 * @param id
//	 * @return
//	 */
//	public List<AuditHistoryVO> findAuditHistory(Integer id);
	
//	/**
//	 * 查看审核记录
//	 * @param id
//	 * @return
//	 */
//	public Map<String,Object> viewAuditHistory(Integer id);
}
