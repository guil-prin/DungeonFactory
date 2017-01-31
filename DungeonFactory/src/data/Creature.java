package data;

public class Creature {

	private String name;
	private Integer hp;
	
	public Creature() {
		
	}
	
	public Creature(String name, Integer hp) {
		this.name = name;
		this.hp = hp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getHp() {
		return hp;
	}

	public void setHp(Integer hp) {
		this.hp = hp;
	}
	
	
}
