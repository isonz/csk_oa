package cn.ptp.oa.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 调休申请
 * @author Administrator
 *
 */
@Entity
@Table( name = "t_oa_comp_time_apply" )
public class CompTimeApply implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	
	@Column(name = "name",length=20)
	private String username;
	
	@Column(name = "userid",length=20)
	private String userid;
	
	@Column(name = "dept",length=20)
	private String dept;
	

	@Temporal(TemporalType.DATE)
	@Column(name="applydate")
	private Date applyDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="startdate1")
	private Date startDate1;
	
	@Temporal(TemporalType.DATE)
	@Column(name="enddate1")
	private Date endDate1;
	
	@Temporal(TemporalType.DATE)
	@Column(name="startdate2")
	private Date startDate2;
	
	@Temporal(TemporalType.DATE)
	@Column(name="enddate2")
	private Date endDate2;
	
	@Column(name = "reason")
	private String reason;
	
	@Column(name="tache")
	private Integer tache=0;
	
	

	public Integer getTache() {
		return tache;
	}

	public void setTache(Integer tache) {
		this.tache = tache;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public Date getStartDate1() {
		return startDate1;
	}

	public void setStartDate1(Date startDate1) {
		this.startDate1 = startDate1;
	}

	public Date getEndDate1() {
		return endDate1;
	}

	public void setEndDate1(Date endDate1) {
		this.endDate1 = endDate1;
	}

	public Date getStartDate2() {
		return startDate2;
	}

	public void setStartDate2(Date startDate2) {
		this.startDate2 = startDate2;
	}

	public Date getEndDate2() {
		return endDate2;
	}

	public void setEndDate2(Date endDate2) {
		this.endDate2 = endDate2;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	
}
