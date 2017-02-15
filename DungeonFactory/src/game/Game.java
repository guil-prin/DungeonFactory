package game;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import data.Card;
import data.Dungeon;
import data.Link;
import data.Opponent;
import data.Persona;
import data.Room;

public class Game {

	private Display display;
	private Shell shell;
	private Dungeon dungeon;
	private Persona currentPersona;
	private List<Card> currentDeck;
	private Room currentRoom;
	List<Integer> picks;
	
	private Composite 	mainComposite, topComposite, midComposite, botComposite, descCurrentRoom, picRoom,
						stateEvent, centerRoom, fightRoom;
	private Composite[] cards;
	private GridData midLeftData, midRightData, midCenterData, topData, midData, botData;
	private Table nextRooms;
	private Label descChar, roomName, roomDesc, eventInfo;
	private Label[] cardNames, fightInfos;
	
	public Game() {
		
	}
	
	public Game(Display display) {
		this.display = display;
		this.shell = new Shell(display);
		this.dungeon = Dungeon.getInstance();
		this.currentPersona = dungeon.getPersonas().get(0);
		this.currentRoom = dungeon.getRoomById(0);
		this.currentDeck = new ArrayList<>();
		this.picks = new ArrayList<>();
		this.setDeck();
		shell.setText("Dungeon Factory : The Game");
		shell.setMinimumSize(800, 600);
		
		this.buildUI();
		
		shell.pack();
		shell.open();
		
		while (!shell.isDisposed()) {
            if (!this.display.readAndDispatch ()) this.display.sleep ();
        }
        this.display.dispose ();
	}
	
	private void buildUI() {
		shell.setLayout(new GridLayout());
		mainComposite = new Composite(shell, SWT.BORDER);
		GridData mainData = new GridData(SWT.FILL, SWT.FILL, true, true);
		mainComposite.setLayoutData(mainData);
		GridLayout gl = new GridLayout();
		mainComposite.setLayout(gl);
		this.buildTopUI();
		this.buildMidUI();
		this.buildBotUI();
		
		this.initializeValues();
		
		this.addListeners();
	}
	
	private void buildTopUI() {
		topComposite = new Composite(mainComposite, SWT.BORDER);
		topData = new GridData(SWT.FILL, SWT.NONE, true, false);
		topComposite.setLayoutData(topData);
		GridLayout gl = new GridLayout(1, false);
		topComposite.setLayout(gl);
		
		descChar = new Label(topComposite, SWT.NONE);
		descChar.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
		
		roomName = new Label(topComposite, SWT.NONE);
		roomName.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
	}
	
	private void buildMidUI() {
		midComposite = new Composite(mainComposite, SWT.NONE);
		midData = new GridData(SWT.FILL, SWT.FILL, true, true);
		midComposite.setLayoutData(midData);
		GridLayout gl = new GridLayout(3, false);
		midComposite.setLayout(gl);
		
		Point size = midComposite.getSize();
		
		midLeftData = new GridData(SWT.FILL, SWT.FILL, true, true);
        midLeftData.widthHint = (int) (size.x * 0.25);
		midRightData = new GridData(SWT.FILL, SWT.FILL, true, true);
        midRightData.widthHint = (int) (size.x * 0.25);
		midCenterData = new GridData(SWT.FILL, SWT.FILL, true, true);
        midCenterData.widthHint = (int) (size.x * 0.5);
		GridData textData = new GridData(SWT.HORIZONTAL, SWT.TOP, true, false, 1, 1);
		
		descCurrentRoom = new Composite(midComposite, SWT.BORDER);
		descCurrentRoom.setLayoutData(midLeftData);
		GridLayout descGridLayout = new GridLayout();
		descCurrentRoom.setLayout(descGridLayout);
		
		roomDesc = new Label(descCurrentRoom, SWT.WRAP);
		roomDesc.setLayoutData(textData);
		
		centerRoom = new Composite(midComposite, SWT.BORDER);
		centerRoom.setLayoutData(midCenterData);
		GridLayout picLayout = new GridLayout();
		centerRoom.setLayout(picLayout);
		
		picRoom = new Composite(centerRoom, SWT.BORDER);
		picRoom.setLayoutData(midCenterData);
		picRoom.setLayout(picLayout);
		this.setImage();
		
		fightRoom = new Composite(centerRoom, SWT.BORDER);
		fightRoom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		fightRoom.setLayout(picLayout);
		fightInfos = new Label[4];
		fightInfos[0] = new Label(fightRoom, SWT.WRAP);
		fightInfos[1] = new Label(fightRoom, SWT.WRAP);
		fightInfos[2] = new Label(fightRoom, SWT.WRAP);
		fightInfos[0].setLayoutData(textData);
		fightInfos[1].setLayoutData(textData);
		fightInfos[2].setLayoutData(textData);
		
		stateEvent = new Composite(midComposite, SWT.BORDER);
		stateEvent.setLayoutData(midRightData);
		GridLayout stateLayout = new GridLayout();
		stateEvent.setLayout(stateLayout);
		
		this.fillEventComposite(stateEvent, textData);
		
	}
	
	private void fillEventComposite(Composite c, GridData textData) {
		eventInfo = new Label(stateEvent, SWT.WRAP);
		eventInfo.setLayoutData(textData);
	}
	
	private void setImage() {
		InputStream is = getClass().getClassLoader().getResourceAsStream("img/crypt.jpg");
		Point size = picRoom.getSize();
		ImageData imageData = new ImageData(is);
		//imageData = imageData.scaledTo(size.x, size.y);
		Image image = new Image(display, imageData);
		picRoom.setBackgroundImage(image);
	}
		
	private void buildBotUI() {
		botComposite = new Composite(mainComposite, SWT.BORDER);
		botData = new GridData(SWT.FILL, SWT.NONE, true, false);
		botComposite.setLayoutData(botData);
		GridLayout gl = new GridLayout(5, false);
		botComposite.setLayout(gl);
		
		nextRooms = new Table(botComposite, SWT.BORDER);
		nextRooms.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		cards = new Composite[4];
		cardNames = new Label[4];

		GridData cardData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridLayout glc = new GridLayout();
		
		for(int i = 0 ; i < 4 ; i++) {
			cards[i] = new Composite(botComposite, SWT.BORDER);
			cards[i].setLayoutData(cardData);
			cards[i].setLayout(glc);
			cardNames[i] = new Label(cards[i], SWT.NONE);
			this.makeVisualCard(cards[i], i);
		}
	}
	
	private void fillNextRooms() {
		nextRooms.removeAll();
		for(Link l : currentRoom.getLinks()) {
			TableItem item = new TableItem(nextRooms, SWT.NONE);
			item.setData(l);
			String txt = "";
			if(!l.isAccessible()) {
				txt = " - route bloquée";
			}
			item.setText(l.getNextRoom().getName() + txt);
		}
	}
	
	private void makeVisualCard(Composite c, int indexOfCard) {
		c.setData("index", indexOfCard);
		c.setData("card", currentDeck.get(indexOfCard));
		cardNames[indexOfCard].setText(currentDeck.get(indexOfCard).getName());
	}
	
	private void initializeValues() {
		this.refreshCurrentPersonaData();
		this.refreshCurrentRoomData();
		this.refreshCurrentEventData();
	}
	
	private void refreshCurrentPersonaData() {
		descChar.setText("Votre personnage : " + currentPersona.getName() + " - " + currentPersona.getHp() + " HP");
	}
	
	private void refreshCurrentRoomData() {
		roomName.setText("Votre salle actuelle : " + currentRoom.getName());
		roomDesc.setText(currentRoom.getDescription());
		this.fillNextRooms();
	}
	
	private void refreshCurrentEventData() {
		if(currentRoom.getEvent().isNeedsValidation()) {
			if(currentRoom.isRoomOpen()) {
				eventInfo.setText(currentRoom.getEvent().getFinalDescription());
			}
			else {
				eventInfo.setText(currentRoom.getEvent().getInitialDescription());
			}
		}
		else {
			eventInfo.setText(currentRoom.getEvent().getInitialDescription());
		}
	}
	
	private void refreshFightData(Card c) {
		String l1 = "Vous utilisez " + c.getName();
		String l2 = "";
		String l3 = "";
		Opponent o = currentRoom.getEvent().getOpponent();
		if(currentRoom.getEvent().getActions().containsKey(c)) {
			if(c.getPower() > 0) {
				l2 = "Vous infligez " + c.getPower() + " points de dégats à votre obstacle.";
			}
			else {
				l2 = "Vous déjouez le piège de la salle.";
			}
		}
		else {
			l2 = "Votre action ne vous mène nulle part.";
		}
		if(o.getHp() > 0) {
			if(o.getStr() > 0) {
				l3 = "Votre ennemi riposte ! Il vous inflige " + o.getStr() + " points de dégats.";
			}
			else {
				l3 = "L'obstacle se dresse toujours devant vous...";
			}
		}
		else {
			l3 = "Vous avez détruit ce qui vous barrait la route !";
		}
		fightInfos[0].setText(l1);
		fightInfos[1].setText(l2);
		fightInfos[2].setText(l3);
	}
	
	private void refreshFightData() {
		fightInfos[0].setText("");
		fightInfos[1].setText("");
		fightInfos[2].setText("");
	}
	
	private void setDeck() {
		List<Card> allCards = currentPersona.getDeck();
		for(int i = 0 ; i < allCards.size(); i++) {
			picks.add(i);
		}
		Collections.shuffle(picks);
		for(int i = 0 ; i < 4 ; i++) {
			currentDeck.add(allCards.get(picks.get(i)));
		}
	}
	
	private void manageEvent(Card c) {
		Opponent o = currentRoom.getEvent().getOpponent();
		if(currentRoom.getEvent().getActions().containsKey(c)) {
			o.setHp(o.getHp() - c.getPower());
		}
		if(o.getHp() < 1) {
			for(Link l : currentRoom.getLinks()) {
				l.setAccessible(true);
			}
			this.fillNextRooms();
			this.refreshCurrentEventData();
		}
		else {
			System.out.println("Riposte ennemie !");
			currentPersona.setHp(currentPersona.getHp() - o.getStr());
			this.refreshCurrentPersonaData();
		}
		this.refreshFightData(c);
	}
	
	private void replaceCard(Integer index) {
		Composite cardComposite = cards[index];
		Integer newPick = (int) (Math.random() * (picks.size()-4));
		
		Collections.swap(picks, index, newPick + 4);

		Card newCard = currentPersona.getDeck().get(picks.get(index));
		currentDeck.set(index, newCard);
		makeVisualCard(cardComposite, index); 
	}

	private void addListeners() {
		nextRooms.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int index = nextRooms.getSelectionIndex();
				Link l = (Link) nextRooms.getItem(index).getData();
				if(l.isAccessible()) {
					currentRoom = l.getNextRoom();
					refreshCurrentRoomData();
					refreshCurrentEventData();
					refreshFightData();
				}
			}
		});
		
		midComposite.addListener(SWT.Resize, new Listener()
	    {
	        @Override
	        public void handleEvent(Event arg0)
	        {
	            Point size = midComposite.getSize();

	            midLeftData.widthHint = (int) (size.x * 0.25);
	            midRightData.widthHint = (int) (size.x * 0.25);
	            midCenterData.widthHint = (int) (size.x * 0.5);
	        }
	    });
		
		mainComposite.addListener(SWT.Resize, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Point size = mainComposite.getSize();
				
				botData.heightHint = (int) (size.y * 0.2);
			}
		});
		
		for(int i = 0 ; i < 4 ; i++) {
			cards[i].addMouseTrackListener(new MouseTrackListener() {
				
				PopupDialog popup;
				
				@Override
				public void mouseHover(MouseEvent e) {
				}
				
				@Override
				public void mouseExit(MouseEvent e) {
					popup.close();
				}
				
				@Override
				public void mouseEnter(MouseEvent e) {
					popup = new PopupDialog(((Composite) e.getSource()).getShell(), SWT.BORDER, false, false, false, false, false, "Description de la carte", ((Card)((Composite) e.getSource()).getData("card")).getDescriptor());
					popup.create();
					popup.open();
				}
			});

			cards[i].addMouseListener(new MouseListener() {
				
				@Override
				public void mouseUp(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseDown(MouseEvent e) {
					if(!currentRoom.isRoomOpen()) {
						Integer index = (Integer) ((Composite) e.getSource()).getData("index");
						Card usedCard = (Card) ((Composite) e.getSource()).getData("card");
						manageEvent(usedCard);
						replaceCard(index);
					}
					else {
						fightInfos[0].setText("Impossible de réaliser cette action.");
					}
				}
				
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
	}
	
}
