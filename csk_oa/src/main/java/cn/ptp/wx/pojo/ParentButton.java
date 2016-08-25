package cn.ptp.wx.pojo;

public class ParentButton extends Button {
	private Button[] sub_button;  
	  
    public Button[] getSub_button() {  
        return sub_button;  
    }  
  
    public void setSub_button(Button[] sub_button) {  
        this.sub_button = sub_button;  
    }  
    
    public void addSubButton(Button button){
    	Button[] sub_button_old = new Button[this.getSub_button()==null?1:this.getSub_button().length+1];
    	int i=0;
    	for(;i<(this.getSub_button()==null?0:this.getSub_button().length);i++){
    		sub_button_old[i] = this.getSub_button()[i];
    	}
    	sub_button_old[i] = button;
    	this.setSub_button(sub_button_old);
    }

}
