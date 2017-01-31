package data;

public class Card {

	private String name;
	private String tag;
	private String descriptor;
	private Integer power;
	
	public Card() {
		
	}
	
	public Card(String name, String tag, String descriptor, Integer power) {
		this.name = name;
		this.tag = tag;
		this.descriptor = descriptor;
		this.power = power;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getDescriptor() {
		return descriptor;
	}
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}
	public Integer getPower() {
		return power;
	}
	public void setPower(Integer power) {
		this.power = power;
	}
	
}
