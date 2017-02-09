package data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class Persona {

	private String name;
	private Integer hp;
	private List<Card> deck;
	
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
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
	
	public void addCardInDeck(Card c) {
		deck.add(c);
	}
	
	public void removeCardInDeck(Card c) {
		deck.remove(c);
	}
	
	public Integer numberOfCard(Card c) {
		Integer nb = 0;
		for(Card check : deck) {
			if(c.equals(check)) {
				nb++;
			}
		}
		return nb;
	}
	
	public boolean isThisCardInDeck(Card c) {
		boolean found = false;
		int i = 0;
		int size = this.sizeOfDeck();
		while(found == false && i < size) {
			Card check = deck.get(i);
			if((check.getTag()).equals(c.getTag())) {
				found = true;
			}
			i++;
		}
		return found;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}
	
}
