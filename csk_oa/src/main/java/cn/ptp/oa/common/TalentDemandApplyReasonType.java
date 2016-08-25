package cn.ptp.oa.common;

public enum TalentDemandApplyReasonType {
	A(1,"A、增设职位"),B(2,"B、原职位增加人力"),C(3,"C、储备人力"),
	D(4,"D、原职位离职"),E(5,"E、原职位调动"),F(6,"F、其他");
	
	private int value = 1;
	private String name;
	
	private TalentDemandApplyReasonType(int value,String name){
		this.value = value;
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public static void main(String[] args) {
		System.out.println(TalentDemandApplyReasonType.class.getSimpleName());
	}
}
