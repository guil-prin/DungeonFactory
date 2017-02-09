package data;

import java.util.ArrayList;
import java.util.List;

public class Dungeon {

	private static Dungeon dungeon = null;
	private List<Persona> personas;
	private List<Room> rooms;
	
	private Dungeon() {
		personas = new ArrayList<>();
		rooms = new ArrayList<>();
		rooms.add(new Room(0, "Entrée", "My first room", true, false));
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
	
	public void removePersona(Persona p) {
		personas.remove(p);
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
	
	public boolean isPersonaExists(String name) {
		boolean found = false;
		for(Persona pers : personas) {
			if(pers.getName().equals(name)) {
				found = true;
			}
		}
		return found;
	}
	
	public boolean isPersonaExists(Persona p, String name) {
		boolean found = false;
		for(Persona pers : personas) {
			if(pers.getName().equals(name) && !(pers.equals(p))) {
				found = true;
			}
		}
		return found;
	}
	
	public List<Room> getRooms() {
		return rooms;
	}
	
	public void addRoom(Room r) {
		rooms.add(r);
	}
	
	public Integer sizeOfRooms() {
		return rooms.size();
	}
	
	public Room getRoomById(Integer id) {
		for(Room r : rooms) {
			if(r.getId() == id)
				return r;
		}
		return null;
	}
	
}
