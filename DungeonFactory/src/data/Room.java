package data;

import java.util.ArrayList;
import java.util.List;

public class Room {

	private Integer id;
	private String name;
	private String description;
	private boolean start;
	private boolean finish;
	private List<Link> links;
	private Event event;
	
	public Room() {
		links = new ArrayList<>();
		event = new Event();
	}

	public Room(Integer id, String name, String description, boolean start, boolean finish) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.start = start;
		this.finish = finish;
		links = new ArrayList<>();
		event = new Event();
	}
	
	public Room(Integer id, String name, String description, boolean finish) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.start = false;
		this.finish = finish;
		links = new ArrayList<>();
		event = new Event();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	public void addLink(Link l) {
		links.add(l);
	}
	
	public void removeLink(Link l) {
		links.remove(l);
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
}
