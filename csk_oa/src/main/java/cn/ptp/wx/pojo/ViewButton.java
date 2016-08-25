package cn.ptp.wx.pojo;

public class ViewButton extends Button {
	private final String type="view";  
    private String url;  
  
    public String getType() {  
        return type;  
    }  
  
    public String getUrl() {  
        return url;  
    }  
  
    public void setUrl(String url) {  
        this.url = url;  
    }  
}	
