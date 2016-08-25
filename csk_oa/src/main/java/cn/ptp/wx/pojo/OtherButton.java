package cn.ptp.wx.pojo;

public abstract class OtherButton extends Button{
	private String key;
	
	private Button[] sub_button;  
	
	  
    public Button[] getSub_button() {  
        return sub_button;  
    }  
  
    public void setSub_button(Button[] sub_button) {  
        this.sub_button = sub_button;  
    }

	public abstract String getType();


	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}  
    
    
}
