package cn.ptp.oa.domain;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "t_oa_shopping_detail" )
public class ShoppingApplyDetail implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="shoppingapply_id")
	private ShoppingApply shoppingApply;
	
	@Column(name = "name",length=20)
	private String name;
	
	@Column(name = "price")
	private float price;
	
	@Column(name = "number")
	private float number;
	
	@Column(name = "purpose")
	private String purpose;
	
	
	transient private String flag;//前台页面提交的数据标记

	public String getTotal() {
		double total = number * price;
		DecimalFormat df = new DecimalFormat("#.00");
		
		return df.format(total);
	}
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ShoppingApply getShoppingApply() {
		return shoppingApply;
	}

	public void setShoppingApply(ShoppingApply shoppingApply) {
		this.shoppingApply = shoppingApply;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getNumber() {
		return number;
	}

	public void setNumber(float number) {
		this.number = number;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	
}
