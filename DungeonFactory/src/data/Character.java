package data;

import java.util.ArrayList;
import java.util.List;

public class Character {

	private String name;
	private Integer hp;
	private List<Card> deck;
	
	public Character() {
		
	}
	
	public Character(String name, Integer hp) {
		this.name = name;
		this.hp = hp;
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
