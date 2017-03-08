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
		//rooms.add(new Room(0, "Entrée", "My first room", true, false));
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
	
	public Persona getPersonaByName(String name) {
		for(Persona p : personas) {
			if(p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}
	
	public List<Room> getRooms() {
		return rooms;
	}
	
	public void addRoom(Room r) {
		rooms.add(r);
	}
	
	public void removeRoom(Room r) {
		rooms.remove(r);
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
	
	public List<Room> getEntrancesOfRoom(Room r) {
		List<Room> rooms = new ArrayList<>();
		if(!r.isStart()) {
			for(Room room : dungeon.getRooms()) {
				for(Link link : room.getLinks()) {
					if(link.getNextRoom().equals(r)) {
						rooms.add(room);
					}
				}
			}
		}
		return rooms;
	}
	
	public List<Room> getExitsOfRoom(Room r) {
		List<Room> rooms = new ArrayList<>();
		if(!r.isFinish()) {
			for(Link link : r.getLinks()) {
				rooms.add(link.getNextRoom());
			}
		}
		return rooms;
	}
	
	public boolean hasOneFinalRoom() {
		int numberOfFinalRooms = 0;
		for(Room r : this.getRooms()) {
			if(r.isFinish()) {
				numberOfFinalRooms++;
			}
		}
		if(numberOfFinalRooms == 1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/**
	 * Checks if the dungeon is valid to play.
	 * @return boolean if the dungeon is valid or not.
	 */
	public boolean isValid() {
		boolean valid = true;
		int sizeOfRooms = this.sizeOfRooms();
		int sizeOfPersonas = this.sizeOfPersonas();
		int curRoomIndex = 0;
		int curPersonaIndex = 0;
		
		valid = valid && (sizeOfRooms > 0);
		valid = valid && (sizeOfPersonas > 0);
		valid = valid && hasOneFinalRoom();
		
		while(valid && curPersonaIndex < sizeOfPersonas) {
			Persona p = this.personas.get(curPersonaIndex);
			valid = valid && (p.sizeOfDeck() > 0);
			curPersonaIndex++;
		}
		
		while(valid && curRoomIndex < sizeOfRooms) {
			Room r = this.rooms.get(curRoomIndex);
			valid = valid && (getEntrancesOfRoom(r).size() > 0 || r.isStart()) && (getExitsOfRoom(r).size() > 0 || r.isFinish());
			curRoomIndex++;
		}
		
		return valid;
	}
}
