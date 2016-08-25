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

@Entity
@Table( name = "t_oa_extra_work" )
public class ExtraWorkApply  implements Serializable{
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
	@Column(name="date")
	private Date date;
	
	@Column(name = "times")
	private float times=0;
	
	@Column(name = "timesUnit",length=10)
	private String timesUnit="小时";
	
	@Column(name = "reason",length=255)
	private String reason;
	

	@Temporal(TemporalType.DATE)
	@Column(name="validDate")
	private Date validDate;
	
	
	@Column(name="tache")
	private Integer tache=0;//0:未提交  1：审核中  2：已完成  3:不批准
	
	
	
	public Integer getTache() {
		return tache;
	}

	public void setTache(Integer tache) {
		this.tache = tache;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getTimes() {
		return times;
	}

	public void setTimes(float times) {
		this.times = times;
	}

	public String getTimesUnit() {
		return timesUnit;
	}

	public void setTimesUnit(String timesUnit) {
		this.timesUnit = timesUnit;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

}
