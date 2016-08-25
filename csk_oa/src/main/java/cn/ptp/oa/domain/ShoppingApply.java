package cn.ptp.oa.domain;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

/**
 * 调休申请
 * @author Administrator
 *
 */
@Entity
@Table( name = "t_oa_shopping" )
public class ShoppingApply implements Serializable{
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
	
	@Column(name = "storekeeper",length=20)
	private String storeKeeper;//仓管员

	@Temporal(TemporalType.DATE)
	@Column(name="applydate")
	private Date applyDate;
	
	@Column(name="tache")
	private Integer tache=0;
	
	
	@OneToMany(mappedBy = "shoppingApply",cascade = CascadeType.ALL,fetch=FetchType.EAGER,orphanRemoval=true)
	@OrderBy(value="id ASC")
	private List<ShoppingApplyDetail> detail = new ArrayList<ShoppingApplyDetail>();
	

	public String getTotal() {
		float all = 0;
		for(ShoppingApplyDetail mx:detail){
			float single = mx.getPrice() * mx.getNumber();
			all = all + single;
		}
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(all);
	}

	

	public List<ShoppingApplyDetail> getDetail() {
		return detail;
	}



	public void setDetail(List<ShoppingApplyDetail> detail) {
		this.detail = detail;
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

	public String getStoreKeeper() {
		return storeKeeper;
	}

	public void setStoreKeeper(String storeKeeper) {
		this.storeKeeper = storeKeeper;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public Integer getTache() {
		return tache;
	}

	public void setTache(Integer tache) {
		this.tache = tache;
	}
	
	
}
