package model;

import data.Card;
import data.Dungeon;
import data.Event;
import data.Link;
import data.Opponent;
import data.Persona;
import data.Room;

public enum ModelProvider {

	INSTANCE ;
	private Dungeon dungeon;
	
	private ModelProvider() {
		dungeon = Dungeon.getInstance();
		Persona p = new Persona("Gandalf");
		p.setHp(30);
		Card c1 = new Card("Boule de feu", "b1", "Une boule de feu", 2);
		for(int i = 0 ; i < 5 ; i++) {
			p.addCardInDeck(c1);
		}
		Card c2 = new Card("Vague de froid", "f1", "Une vague de froid destructrice", 3);
		for(int i = 0 ; i < 3 ; i++) {
			p.addCardInDeck(c2);
		}
		dungeon.addPersona(p);
		
		Room r0 = new Room(0, "Entrée", "Une salle d'entrée", false);
		r0.setStart(true);
		Room r1 = new Room(1, "Couloir piégé", "Un couloir piégé par des ronces", false);
		Room r2 = new Room(2, "Sortie", "La sortie de ce donjon très court", true);
		r0.addLink(new Link(r1, true));
		r1.addLink(new Link(r0, true));
		r1.addLink(new Link(r2, false));
		Event e0 = new Event();
		e0.setInitialDescription("L'entrée de ce donjon s'enfonce dans les ténèbres. Rien n'est cependant à signaler.");
		e0.setValidated(false);
		r0.setEvent(e0);
		Event e1 = new Event();
		e1.setInitialDescription("Un grand couloir sombre. Un mur de ronces vous bloque la route vers la sortie.");
		e1.setValidated(true);
		e1.setFinalDescription("Vous avez détruit avec brio le mur de ronces et rien ne vous empêche de passer.");
		e1.setOpponent(new Opponent("Mur de ronces", 1, 0));
		e1.addAction(c1, "Vous détruisez ces ronces !");
		r1.setEvent(e1);
		Event e2 = new Event();
		e2.setInitialDescription("La sortie de ce donjon de test. Profitez de l'air pur !");
		r2.setEvent(e2);
		dungeon.addRoom(r0);
		dungeon.addRoom(r1);
		dungeon.addRoom(r2);
	}
	
	public Dungeon getDungeon() {
		return dungeon;
	}
}
