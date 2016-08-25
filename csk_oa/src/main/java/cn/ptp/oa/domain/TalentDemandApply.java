package cn.ptp.oa.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import cn.ptp.oa.common.TalentDemandApplyReasonType;

/**
 * 用人需求申请表
 * @author Administrator
 *
 */
@Entity
@Table( name = "t_oa_talent_demand_apply" )
public class TalentDemandApply implements Serializable{
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
	@Column(name="arrivalDate")
	private Date arrivalDate;//期望到岗日期
	
	@Column(name = "postName",length=20)
	private String postName;
	
	@Column(name = "personNum")
	private int personNum;
	
	@Column(name = "havePersonNum")
	private int havePersonNum;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "applyReasonType")
	private TalentDemandApplyReasonType type;
	
	@Column(name = "applyReason")
	private String applyReason;
	
	@Column(name = "duties")
	private String duties;//工作职责
	
	@Column(name = "skill")
	private String skill;//技能
	
	@Column(name = "experience")
	private String experience;//经验
	
	@Column(name = "capacity")
	private String capacity;//能力
	
	
	@Temporal(TemporalType.DATE)
	@Column(name="feedbackDate")
	private Date feedbackDate;
	
	@Column(name = "feedback")
	private String feedback;
	
	@Column(name="tache")
	private Integer tache=0;//0:未提交  1：审核中  2：已完成  3:不批准
	
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

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public int getPersonNum() {
		return personNum;
	}

	public void setPersonNum(int personNum) {
		this.personNum = personNum;
	}

	public int getHavePersonNum() {
		return havePersonNum;
	}

	public void setHavePersonNum(int havePersonNum) {
		this.havePersonNum = havePersonNum;
	}

	public TalentDemandApplyReasonType getType() {
		return type;
	}

	public void setType(TalentDemandApplyReasonType type) {
		this.type = type;
	}

	public String getApplyReason() {
		return applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}

	public String getDuties() {
		return duties;
	}

	public void setDuties(String duties) {
		this.duties = duties;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public Date getFeedbackDate() {
		return feedbackDate;
	}

	public void setFeedbackDate(Date feedbackDate) {
		this.feedbackDate = feedbackDate;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	
	
	
}
