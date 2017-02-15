package data;

import java.util.HashMap;

public class Event {
	
	private String initialDescription;
	private String finalDescription;
	private boolean needsValidation;
	private Opponent opponent;
	private HashMap<Card, String> actions;
	
	public Event() {
		this.initialDescription = "";
		this.finalDescription = "";
		this.needsValidation = false;
		this.opponent = new Opponent();
		this.actions = new HashMap<>();
	}

	public Event(String initialDescription, String finalDescription, boolean isValidated, Opponent opponent,
			HashMap<Card, String> actions) {
		super();
		this.initialDescription = initialDescription;
		this.finalDescription = finalDescription;
		this.needsValidation = isValidated;
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

	public boolean isNeedsValidation() {
		return needsValidation;
	}

	public void setNeedsValidation(boolean isValidated) {
		this.needsValidation = isValidated;
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
	
	public void addAction(Card c, String s) {
		actions.put(c, s);
	}
	
	
}
