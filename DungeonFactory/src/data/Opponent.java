package data;

public class Opponent {

	private String name;
	private Integer hp;
	private Integer str;
	
	public Opponent() {
		super();
		this.name = "";
		this.hp = 0;
		this.str = 0;
	}

	public Opponent(String name, Integer hp, Integer str) {
		super();
		this.name = name;
		this.hp = hp;
		this.str = str;
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

	public Integer getStr() {
		return str;
	}

	public void setStr(Integer str) {
		this.str = str;
	}
	
}
