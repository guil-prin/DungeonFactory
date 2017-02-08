package data;

public class Link {

	private Room nextRoom;
	private boolean isAccessible;
	
	public Link() {
		
	}
	
	public Link(Room nextRoom, boolean isAccessible) {
		super();
		this.nextRoom = nextRoom;
		this.isAccessible = isAccessible;
	}
	public Room getNextRoom() {
		return nextRoom;
	}
	public void setNextRoom(Room nextRoom) {
		this.nextRoom = nextRoom;
	}
	public boolean isAccessible() {
		return isAccessible;
	}
	public void setAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}
	
}
