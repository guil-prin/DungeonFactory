 
package game;

import javax.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import data.Card;
import data.Dungeon;
import data.Link;
import data.Opponent;
import data.Persona;
import data.Room;

public class GameViewPart {
	
	private Dungeon dungeon;
	private Persona currentPersona;
	private List<Card> currentDeck;
	private Room currentRoom;
	List<Integer> picks;
	
	private Shell shell;
	private Composite 	parent, mainComposite, initComposite, topComposite, midComposite, botComposite, descCurrentRoom, picRoom,
						stateEvent, centerRoom, fightRoom, nextRoomsComposite, deckComposite;
	private Composite[] cards;
	private GridData midLeftData, midRightData, midCenterData, topData, midData, botData, nextGridData;
	private Table nextRooms;
	private Label descChar, roomName, roomDesc, eventInfo, picLabel; 
	private Label[] cardNames, cardImg, fightInfos;
	private Cursor nope, hand;
	private Font font;
	
	private static final String PICKCHAR = "Choisir son personnage : ";
	private static final String CHARPICKED = "Choisir ce personnage";
	private static final String YOURCHAR = "Votre personnage : ";
	private static final String SEPARATOR = " - ";
	private static final String HP = " points de vie";
	private static final String CURRENTROOM = "Vous êtes dans la salle : ";
	private static final String YOURROOM = "Votre salle";
	private static final String YOUREVENT = "Evènement en cours";
	private static final String FIGHTINFO = "Informations de combat";
	private static final String NEXTROOMS = "Déplacement vers";
	private static final String YOURDECK = "Votre main";
	private static final String FORBIDDENACTION = "Impossible de réaliser cette action.";
	private static final String EMPTY = "";
	private static final String FONTNAME = "BLKCHCRY.TTF";
	
	
	
	@Inject
	public GameViewPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		this.parent = parent;
		this.shell = parent.getShell();
		this.dungeon = Dungeon.getInstance();
		//this.currentPersona = dungeon.getPersonas().get(0);
		this.currentRoom = dungeon.getRoomById(0);
		this.currentDeck = new ArrayList<>();
		this.picks = new ArrayList<>();
		nope = shell.getDisplay().getSystemCursor(SWT.CURSOR_NO);
		hand = shell.getDisplay().getSystemCursor(SWT.CURSOR_HAND);
		

		mainComposite = new Composite(parent, SWT.BORDER);
		GridData mainData = new GridData(SWT.FILL, SWT.FILL, true, true);
		mainComposite.setLayoutData(mainData);
		GridLayout gl = new GridLayout();
		mainComposite.setLayout(gl);
		
		this.loadFont(FONTNAME);
		
		this.buildInitUI();
		
	}
	
	private void loadFont(String name) {
		//if(parent.getShell().getDisplay().loadFont("/font/BLKCHCRY.TTF"))
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		String path = "/font/" + name; 
		URL url = FileLocator.find(bundle, new Path(path), null);		
		URL fileUrl = null;
		try {
			fileUrl = FileLocator.toFileURL(url);
		}
		catch (IOException e) {
		// Will happen if the file cannot be read for some reason
			e.printStackTrace();
		}
		File file = new File(fileUrl.getPath());
		Display.getCurrent().loadFont(file.toString());
		font = new Font(parent.getShell().getDisplay(), "BlackChancery", 12, SWT.NONE);
	}
	
	private void buildInitUI() {
		initComposite = new Composite(mainComposite, SWT.NONE);
		GridData initData = new GridData(SWT.FILL, SWT.FILL, true, true);
		initComposite.setLayoutData(initData);
		GridLayout gl = new GridLayout(3, false);
		initComposite.setLayout(gl);
		
		Label chooseChar = new Label(initComposite, SWT.NONE);
		chooseChar.setText(PICKCHAR);
		chooseChar.setFont(font);
		
		Combo charCombo = new Combo(initComposite, SWT.READ_ONLY);
		charCombo.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
		for(Persona p : dungeon.getPersonas()) {
			charCombo.add(p.getName());
		}
		
		Button b = new Button(initComposite, SWT.NONE);
		b.setText(CHARPICKED);
		
		b.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if(charCombo.getSelectionIndex() != -1) {
					currentPersona = dungeon.getPersonaByName(charCombo.getText());
					initData.exclude = !initData.exclude;
					initComposite.setVisible(!initData.exclude);
					initComposite = null;
					
					buildUI();
				}
			}
		});
		
		initComposite.pack();
	}
	
	private void buildUI() {

		this.setDeck();
		
		this.buildTopUI();
		this.buildMidUI();
		this.buildBotUI();

		this.initializeValues();
		
		this.addListeners();
		shell.layout(true, true);
		
		//this.setImage();
	}
	
	private void buildTopUI() {
		topComposite = new Composite(mainComposite, SWT.BORDER);
		topData = new GridData(SWT.FILL, SWT.NONE, true, false);
		topComposite.setLayoutData(topData);
		GridLayout gl = new GridLayout(1, false);
		topComposite.setLayout(gl);
		
		descChar = new Label(topComposite, SWT.NONE);
		descChar.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
		//descChar.setFont(font);
		
		roomName = new Label(topComposite, SWT.NONE);
		roomName.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
		//roomName.setFont(font);
		
		topComposite.pack();
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
		
		descCurrentRoom = new Composite(midComposite, SWT.BORDER);
		descCurrentRoom.setLayoutData(midLeftData);
		GridLayout descGridLayout = new GridLayout();
		descCurrentRoom.setLayout(descGridLayout);
		
		Label topRoomDesc = new Label(descCurrentRoom, SWT.NONE);
		topRoomDesc.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, true, false));
		topRoomDesc.setText(YOURROOM);
		topRoomDesc.setFont(font);
		
		GridData textData = new GridData(SWT.HORIZONTAL, SWT.TOP, true, false, 1, 1);
		
		roomDesc = new Label(descCurrentRoom, SWT.WRAP);
		roomDesc.setLayoutData(textData);
		
		centerRoom = new Composite(midComposite, SWT.BORDER);
		centerRoom.setLayoutData(midCenterData);
		GridLayout picLayout = new GridLayout();
		centerRoom.setLayout(picLayout);
		
		picRoom = new Composite(centerRoom, SWT.BORDER);
		picRoom.setLayoutData(midCenterData);
		picRoom.setLayout(picLayout);
		picLabel = new Label(picRoom, SWT.CENTER);
		picLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		fightRoom = new Composite(centerRoom, SWT.BORDER);
		fightRoom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		fightRoom.setLayout(picLayout);
		fightInfos = new Label[4];
		fightInfos[0] = new Label(fightRoom, SWT.WRAP);
		fightInfos[1] = new Label(fightRoom, SWT.WRAP);
		fightInfos[2] = new Label(fightRoom, SWT.WRAP);
		fightInfos[3] = new Label(fightRoom, SWT.WRAP);
		fightInfos[0].setLayoutData(new GridData(SWT.CENTER, SWT.NONE, true, false));
		fightInfos[1].setLayoutData(textData);
		fightInfos[2].setLayoutData(textData);
		fightInfos[3].setLayoutData(textData);
		fightInfos[0].setText(FIGHTINFO);
		fightInfos[0].setFont(font);
		
		stateEvent = new Composite(midComposite, SWT.BORDER);
		stateEvent.setLayoutData(midRightData);
		GridLayout stateLayout = new GridLayout();
		stateEvent.setLayout(stateLayout);
		
		Label topEventDesc = new Label(stateEvent, SWT.NONE);
		topEventDesc.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, true, false));
		topEventDesc.setText(YOUREVENT);
		topEventDesc.setFont(font);
		eventInfo = new Label(stateEvent, SWT.WRAP);
		eventInfo.setLayoutData(textData);
	}
		
	private void buildBotUI() {
		botComposite = new Composite(mainComposite, SWT.BORDER);
		botData = new GridData(SWT.FILL, SWT.NONE, true, false);
		botData.heightHint = 180;
		botComposite.setLayoutData(botData);
		GridLayout gl = new GridLayout(3, false);
		botComposite.setLayout(gl);		
		
		nextRoomsComposite = new Composite(botComposite, SWT.NONE);
		nextGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		//nextGridData.widthHint = (int) (size.y * 0.2);
		nextRoomsComposite.setLayoutData(nextGridData);
		nextRoomsComposite.setLayout(new GridLayout());
		Label nextRoomsLabel = new Label(nextRoomsComposite, SWT.NONE);
		nextRoomsLabel.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, true, false));
		nextRoomsLabel.setText(NEXTROOMS);
		nextRoomsLabel.setFont(font);
		nextRooms = new Table(nextRoomsComposite, SWT.BORDER | SWT.V_SCROLL);
		nextRooms.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		cards = new Composite[4];
		cardImg = new Label[4];
		cardNames = new Label[4];
		
		Composite logoComposite = new Composite(botComposite, SWT.NONE);
		GridData logoGD = new GridData(SWT.FILL, SWT.FILL, true, true);
		logoComposite.setLayoutData(logoGD);
		GridLayout logoGL = new GridLayout();
		logoGL.marginWidth = 0;
		logoGL.marginHeight = 0;
		logoComposite.setLayout(logoGL);
		Image img = LoadImage("logo.png");
		Label label = new Label(logoComposite,SWT.NONE);
		label.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, true, false));
		label.setImage(img);
	    Canvas canvas = new Canvas(shell,SWT.NO_REDRAW_RESIZE);
	    canvas.addPaintListener(new PaintListener() {
	        public void paintControl(PaintEvent e) {
	         e.gc.drawImage(img,0,0);
	        }
	    });

		//logoComposite.setBackgroundImage(img);
		
		deckComposite = new Composite(botComposite, SWT.BORDER);
		deckComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		deckComposite.setLayout(new GridLayout(4, false));
		
		Label yourDeckLabel = new Label(deckComposite, SWT.NONE);
		GridData yourDeckLabelData = new GridData(SWT.CENTER, SWT.NONE, true, false);
		yourDeckLabelData.horizontalSpan = 4;
		yourDeckLabel.setLayoutData(yourDeckLabelData);
		yourDeckLabel.setText(YOURDECK);
		yourDeckLabel.setFont(font);
		
		GridData cardData = new GridData(SWT.FILL, SWT.FILL, true, true);
		cardData.minimumWidth = 100;
		GridLayout glc = new GridLayout();
		
		for(int i = 0 ; i < 4 ; i++) {
			cards[i] = new Composite(deckComposite, SWT.NONE);
			cards[i].setLayoutData(cardData);
			cards[i].setLayout(glc);
			cardImg[i] = new Label(cards[i], SWT.NONE);
			cardImg[i].setLayoutData(new GridData(SWT.CENTER, SWT.NONE, true, false));
			cardNames[i] = new Label(cards[i], SWT.WRAP | SWT.CENTER);
			cardNames[i].setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
			this.makeVisualCard(i);
		}
	}
	
	private Image LoadImage(String name) {
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		String path = "/img/" + name; // ADD IMG IN BUILD.PROPERTIES
		URL url = FileLocator.find(bundle, new Path(path), null);
		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);
		Image image = imageDesc.createImage();
		return image;
	}
	
	private Image resize(Image image, int width, int height) {
		  Image scaled = new Image(parent.getDisplay(), width, height);
		  GC gc = new GC(scaled);
		  gc.setAntialias(SWT.ON);
		  gc.setInterpolation(SWT.HIGH);
		  gc.drawImage(image, 0, 0,image.getBounds().width, image.getBounds().height, 0, 0, width, height);
		  gc.dispose();
		  image.dispose(); // don't forget about me!
		  return scaled;
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
	
	private void makeVisualCard(int indexOfCard) {
		cardImg[indexOfCard].setData("index", indexOfCard);
		cardImg[indexOfCard].setData("card", currentDeck.get(indexOfCard));
		
		Image img = cardImg[indexOfCard].getImage();
		if(img != null) {
			img.dispose();
		}
		cardImg[indexOfCard].setImage(this.LoadImage("card_sized.png"));
		cardNames[indexOfCard].setText(currentDeck.get(indexOfCard).getName() + " - " + currentDeck.get(indexOfCard).getPower());
		
	}
	
	private void setPicDamage() {
		if(!picLabel.isVisible()) {
			picLabel.setVisible(true);
		}
		Image img = picLabel.getImage();
		if(img != null) {
			img.dispose();
		}
		Image blood = this.LoadImage("bloodstain.png");
		picLabel.setImage(blood);
	    Canvas canvas = new Canvas(shell,SWT.NO_REDRAW_RESIZE);
	    canvas.addPaintListener(new PaintListener() {
	        public void paintControl(PaintEvent e) {
	         e.gc.drawImage(blood,0,0);
	        }
	    });
	}
	
	private void shakeWindow() {
		for(int i = 0 ; i < 100 ; i++) {
			shell.setBounds(shell.getBounds().x+10, shell.getBounds().y, shell.getBounds().width, shell.getBounds().height);
			shell.setBounds(shell.getBounds().x-10, shell.getBounds().y, shell.getBounds().width, shell.getBounds().height);
		}
	}
	
	private void initializeValues() {
		this.refreshCurrentPersonaData();
		this.refreshCurrentRoomData();
		this.refreshCurrentEventData();
	}
	
	private void refreshCurrentPersonaData() {
		descChar.setText(YOURCHAR + currentPersona.getName() + SEPARATOR + currentPersona.getHp() + HP);
	}
	
	private void refreshCurrentRoomData() {
		roomName.setText(CURRENTROOM + currentRoom.getName());
		roomDesc.setText(currentRoom.getDescription());
		this.fillNextRooms();
		descCurrentRoom.layout();
		this.checkVictoryState();
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
		stateEvent.layout();
	}
	
	private void refreshFightData(Card c) {
		String l1 = "Vous utilisez " + c.getName();
		String l2 = EMPTY;
		String l3 = EMPTY;
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
				this.setPicDamage();
				this.shakeWindow();
			}
			else {
				l3 = "L'obstacle se dresse toujours devant vous...";
			}
		}
		else {
			l3 = "Vous avez détruit ce qui vous barrait la route !";
			Image img = picLabel.getImage();
			if(img != null) {
				img.dispose();
			}
			picLabel.setVisible(false);
		}
		fightInfos[1].setText(l1);
		fightInfos[2].setText(l2);
		fightInfos[3].setText(l3);
		
		this.checkVictoryState();
	}
	
	private void clearFightData() {
		fightInfos[1].setText(EMPTY);
		fightInfos[2].setText(EMPTY);
		fightInfos[3].setText(EMPTY);
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
			currentPersona.setHp(currentPersona.getHp() - o.getStr());
			this.refreshCurrentPersonaData();
		}
		this.refreshFightData(c);
	}
	
	private void replaceCard(Integer index) {
		Integer newPick = (int) (Math.random() * (picks.size()-4));
		
		Collections.swap(picks, index, newPick + 4);

		Card newCard = currentPersona.getDeck().get(picks.get(index));
		currentDeck.set(index, newCard);
		makeVisualCard(index); 
	}
	
	private void checkVictoryState() {
		if(currentPersona.getHp() <= 0) {
			MessageDialog.openInformation(shell, "Vous avez été vaincu !", "Votre ennemi vous a terrassé. Recommencer ?");
		}
		if(currentRoom.isFinish()) {
			MessageDialog.openInformation(shell, "Vous êtes sorti du donjon !", "Félicitations, vous avez triomphé du donjon !");
		}
	}

	private void addListeners() {
		nextRooms.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int index = nextRooms.getSelectionIndex();
				Link l = (Link) nextRooms.getItem(index).getData();
				if(l.isAccessible() && currentPersona.isPersonaAlive()) {
					currentRoom = l.getNextRoom();
					refreshCurrentRoomData();
					refreshCurrentEventData();
					clearFightData();
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
		
		for(int i = 0 ; i < 4 ; i++) {
			cardImg[i].addMouseTrackListener(new MouseTrackListener() {
				
				@Override
				public void mouseHover(MouseEvent e) {
				}
				
				@Override
				public void mouseExit(MouseEvent e) {
				}
				
				@Override
				public void mouseEnter(MouseEvent e) {
					Label l = (Label) e.getSource();
					l.setToolTipText(((Card) l.getData("card")).getDescriptor());
					if(currentRoom.isRoomOpen() || currentPersona.getHp() <= 0) {
						l.setCursor(nope);
					}
					else {
						l.setCursor(hand);
					}
				}
			});

			cardImg[i].addMouseListener(new MouseListener() {
				
				@Override
				public void mouseUp(MouseEvent e) {
				}
				
				@Override
				public void mouseDown(MouseEvent e) {
					if(!currentRoom.isRoomOpen() && currentPersona.isPersonaAlive()) {
						Integer index = (Integer) ((Label) e.getSource()).getData("index");
						Card usedCard = (Card) ((Label) e.getSource()).getData("card");
						manageEvent(usedCard);
						replaceCard(index);
					}
				}
				
				@Override
				public void mouseDoubleClick(MouseEvent e) {
				}
			});
		}
		
		/*
		picRoom.addListener(SWT.Resize, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Point size = picRoom.getSize();
				Image image = picRoom.getBackgroundImage();
				if(image == null) {
					Bundle bundle = FrameworkUtil.getBundle(getClass());
					String path = "/img/img.jpg"; // ADD IMG IN BUILD.PROPERTIES
					URL url = FileLocator.find(bundle, new Path(path), null);
					ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);
					image = imageDesc.createImage();
				}
				Image resized = resize(image, size.x, size.y);
				picLabel.setImage(resized);
			}
		});
		*/
	}
	
	
}