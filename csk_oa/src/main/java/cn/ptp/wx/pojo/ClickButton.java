package cn.ptp.wx.pojo;

public class ClickButton extends Button {
	private final String type="click";
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}
	
}
