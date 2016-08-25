package cn.ptp.wx.pojo;

public class LocationSelectButton extends Button{
	private final String type = "location_select";
	private String key;
	public String getType() {
		return type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	
}
