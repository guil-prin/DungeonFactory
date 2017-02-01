package data;

import java.util.ArrayList;
import java.util.List;

public class Dungeon {

	private static Dungeon dungeon = null;
	private List<Persona> personas;
	
	private Dungeon() {
		personas = new ArrayList<>();
	}
	
	public static final Dungeon getInstance() {
		if(Dungeon.dungeon == null) {
			dungeon = new Dungeon();
		}
		return dungeon;
	}
	
	public void addPersona(Persona p) {
		personas.add(p);
	}
	
	public Integer sizeOfPersonas() {
		return personas.size();
	}

	public List<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}
	
	
	
}
