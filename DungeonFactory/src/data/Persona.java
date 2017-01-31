package data;

import java.util.ArrayList;
import java.util.List;

public class Persona {

	private String name;
	private Integer hp;
	private List<Card> deck;
	
	public Persona() {
		
	}
	
	public Persona(String name) {
		this.name = name;
		this.hp = 20;
		deck = new ArrayList<>();
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
	public List<Card> getDeck() {
		return deck;
	}
	public void setDeck(List<Card> deck) {
		this.deck = deck;
	}
	
	public Integer sizeOfDeck() {
		return deck.size();
	}
	
}
