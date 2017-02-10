package data;

import java.util.HashMap;

public class Event {
	
	private String initialDescription;
	private String finalDescription;
	private boolean isValidated;
	private Opponent opponent;
	private HashMap<Card, String> actions;
	
	public Event() {
		
	}

	public Event(String initialDescription, String finalDescription, boolean isValidated, Opponent opponent,
			HashMap<Card, String> actions) {
		super();
		this.initialDescription = initialDescription;
		this.finalDescription = finalDescription;
		this.isValidated = isValidated;
		this.opponent = opponent;
		this.actions = actions;
	}

	public String getInitialDescription() {
		return initialDescription;
	}

	public void setInitialDescription(String initialDescription) {
		this.initialDescription = initialDescription;
	}

	public String getFinalDescription() {
		return finalDescription;
	}

	public void setFinalDescription(String finalDescription) {
		this.finalDescription = finalDescription;
	}

	public boolean isValidated() {
		return isValidated;
	}

	public void setValidated(boolean isValidated) {
		this.isValidated = isValidated;
	}

	public Opponent getOpponent() {
		return opponent;
	}

	public void setOpponent(Opponent opponent) {
		this.opponent = opponent;
	}

	public HashMap<Card, String> getActions() {
		return actions;
	}

	public void setActions(HashMap<Card, String> actions) {
		this.actions = actions;
	}
	
	
	
}
