package cn.ptp.oa.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "t_oa_user" )
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	
	@Column(name = "OPENID",length=32)
	private String openid;
	
	@Column(name = "USERID",length=20)
	private String userid;
	
	@Column(name = "USERNAME",length=20)
	private String userName;
	
//	@Column(name = "DEPTNAME",length=20)
//	private String deptName;
	@ManyToMany
	private List<Role> roles = new ArrayList<Role>();
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="DEPT_ID")
	private Department department;
	
//	@Column(name = "DEPT_LEAD")
//	private boolean deptLead=false;//是否部门领导
//	
//	@Column(name = "COMPANY_LEAD")
//	private boolean companyLead=false;//是否公司领导
	
	@Column(name = "VALID")
	private boolean valid=true;
	
	

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

//	public boolean isDeptLead() {
//		return deptLead;
//	}
//
//	public void setDeptLead(boolean deptLead) {
//		this.deptLead = deptLead;
//	}
//
//	public boolean isCompanyLead() {
//		return companyLead;
//	}
//
//	public void setCompanyLead(boolean companyLead) {
//		this.companyLead = companyLead;
//	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public int hashCode() {
		
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        User u = (User)o;
		return u.getId().equals(this.getId());
	}
	
	

}
