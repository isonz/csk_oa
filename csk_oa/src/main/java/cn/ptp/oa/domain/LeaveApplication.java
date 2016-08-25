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

import cn.ptp.oa.util.DateUtil;

@Entity
@Table( name = "t_oa_leave_application" )
public class LeaveApplication implements Serializable{
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
	@Column(name="applyDate")
	private Date applyDate = DateUtil.getDate(new Date());
	
	@Column(name = "ftype",length=10)
	private String type;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="startDate")
	private Date startDate;
	
	transient private String part_start_date;
	transient private String part_start_time;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="endDate")
	private Date endDate;
	
	transient private String part_end_date;
	transient private String part_end_time;
	
	@Column(name = "reason")
	private String reason;

	@Column(name = "days")
	private float days=0;
	
	@Column(name="tache")
	private Integer tache=0;//0:未提交  1：审核中  2：已完成  3:不批准
	
	

	public String getPart_start_date() {
		if(part_start_date==null || part_start_date.equals("")){
			if(startDate!=null){
				part_start_date = DateUtil.getDate(startDate, "yyyy-MM-dd");
			}
		}
		return part_start_date;
	}

	public void setPart_start_date(String part_start_date) {
		this.part_start_date = part_start_date;
	}

	public String getPart_start_time() {
		if(part_start_time==null || part_start_time.equals("")){
			if(startDate!=null){
				part_start_time = DateUtil.getDate(startDate, "HH:mm");
			}
		}
		return part_start_time;
	}

	public void setPart_start_time(String part_start_time) {
		this.part_start_time = part_start_time;
	}

	public String getPart_end_date() {
		if(part_end_date==null || part_end_date.equals("")){
			if(endDate!=null){
				part_end_date = DateUtil.getDate(endDate, "yyyy-MM-dd");
			}
		}
		return part_end_date;
	}

	public void setPart_end_date(String part_end_date) {
		this.part_end_date = part_end_date;
	}

	public String getPart_end_time() {
		if(part_end_time==null || part_end_time.equals("")){
			if(endDate!=null){
				part_end_time = DateUtil.getDate(endDate, "HH:mm");
			}
		}
		return part_end_time;
	}

	public void setPart_end_time(String part_end_time) {
		this.part_end_time = part_end_time;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public float getDays() {
		return days;
	}

	public void setDays(float days) {
		this.days = days;
	}

	public Integer getTache() {
		return tache;
	}

	public void setTache(Integer tache) {
		this.tache = tache;
	}
	
	
}
