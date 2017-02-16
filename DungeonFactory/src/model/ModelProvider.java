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
		Persona p = new Persona("Élias de Kelliwic'h");
		p.setHp(30);
		Card c1 = new Card("Boule de feu", "b1", "Une boule de feu de force 2", 2);
		for(int i = 0 ; i < 5 ; i++) {
			p.addCardInDeck(c1);
		}
		Card c2 = new Card("Vague de froid", "f1", "Une vague de froid destructrice de force 3", 3);
		for(int i = 0 ; i < 3 ; i++) {
			p.addCardInDeck(c2);
		}
		Card c3 = new Card("Eboulement", "e1", "Une apparition de gros rochers au dessus de la cible", 7);
		p.addCardInDeck(c3);
		dungeon.addPersona(p);
		
		Room r0 = new Room(0, "Entrée", "Une salle d'entrée. Les murs sont en pierres qui semblent tomber les unes après les autres au fil des âges. De mauvaises herbes sortent des fissures des murs.", false);
		r0.setStart(true);
		Room r1 = new Room(1, "Couloir gauche", "Un long couloir sombre où la nature a repris ses droits. De nombreuses plantes jonchent le sol et les murs.", false);
		Room r2 = new Room(2, "Grande salle", "Une grande salle. La porte menant au couloir gauche ne s'ouvre que de l'autre côté. Vous sentez l'odeur du sang.", false);
		Room r3 = new Room(3, "Sortie", "Un couloir qui remonte vers la surface. Il semblerait que ça soit la sortie !", true);
		r0.addLink(new Link(r1, true));
		r0.addLink(new Link(r2, true));
		r1.addLink(new Link(r0, true));
		r1.addLink(new Link(r2, false));
		r2.addLink(new Link(r0, true));
		r2.addLink(new Link(r3, false));
		Event e0 = new Event();
		e0.setInitialDescription("L'entrée de ce donjon s'enfonce dans les ténèbres. Deux chemins s'offrent à vous : un à gauche, et un à droite.");
		e0.setNeedsValidation(false);
		r0.setEvent(e0);
		Event e1 = new Event();
		e1.setInitialDescription("Un mur de ronces vous bloque la route vers la suite du donjon. Il faut trouver un moyen de les détruire.");
		e1.setNeedsValidation(true);
		e1.setFinalDescription("Vous avez détruit avec brio le mur de ronces et rien ne vous empêche de passer.");
		e1.setOpponent(new Opponent("Mur de ronces", 1, 0));
		e1.addAction(c1, "Votre boule de feu réduit en centres ces ronces !");
		r1.setEvent(e1);
		Event e2 = new Event();
		e2.setInitialDescription("Un gobelin vous nargue et vous empêche de passer par cette porte au fond de la salle. Il semblerait cependant que vous pouvez retourner vers la salle d'entrée au besoin.");
		e2.setNeedsValidation(true);
		e2.setFinalDescription("Le gobelin n'est plus qu'un mauvais souvenir. Par contre, vous avez renforcé l'odeur du sang.");
		e2.setOpponent(new Opponent("Gobelin", 10, 2));
		e2.addAction(c1, "Vous infligez des brûlures à votre ennemi.");
		e2.addAction(c2, "Vous provoquez des engelures à votre ennemi.");
		e2.addAction(c3, "Votre ennemi se prend un rocher sur la tête.");
		r2.setEvent(e2);
		Event e3 = new Event();
		e3.setInitialDescription("Félicitations, vous avez gagné ! Vous pouvez être fier de vous !");
		r3.setEvent(e3);
		dungeon.addRoom(r0);
		dungeon.addRoom(r1);
		dungeon.addRoom(r2);
		dungeon.addRoom(r3);
	}
	
	public Dungeon getDungeon() {
		return dungeon;
	}
}
