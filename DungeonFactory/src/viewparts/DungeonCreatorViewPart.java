 
package viewparts;

import javax.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import data.Card;
import data.Dungeon;
import data.Link;
import data.Persona;
import data.Room;
import model.ModelProvider;

public class DungeonCreatorViewPart {
	
	private Integer id;
	private Font font;
	
	private Composite parent;
	private Dungeon dungeon;
	
	private Composite compositeLeft, compositeRight;
	private GridData leftData, rightData;
	private Button newRoomButton, checkFinal, linkButton, checkAccessible, removeRoom, removeLink, checkOpponent, addCardAction;
	private Table tableRooms, tableLinks, tableCardsEvent;
	private Text roomName, roomDesc, initDescEvent, finalDescEvent, actionCard, nameOpponent;
	private CTabFolder folder;
	private CTabItem eventTab, linkTab;
	private Combo roomList, cardList;
	private Spinner hpOpponent, strengthOpponent;
	
	private static final String NEWROOM = "Nouvelle salle";
	private static final String DELETEROOM = "Supprimer la salle";
	private static final String ROOMNAME = "Nom";
	private static final String ROOMDESC = "Description";
	private static final String INITEVENT = "Evenement initial";
	private static final String NEEDSVALIDATION = "Nécessite une validation ?";
	private static final String VALIDEVENT = "Evènement validé";
	private static final String OPPONENT = "Elément perturbateur";
	private static final String HP = "Points de vie";
	private static final String STRENGTH = "Force";
	private static final String CARD = "Carte";
	private static final String CONSEQUENCES = "Conséquence de l'action";
	private static final String ADDWORD = "Ajouter";
	private static final String ADD = "+";
	private static final String NATIVEACCESS = "Salle accessible nativement ?";
	private static final String LINK = "Lier à cette salle";
	private static final String ROOM = "Salle";
	private static final String ACCESS = "Accessible";
	private static final String REMOVELINK = "Retirer le lien";
	private static final String ISFINALROOM = "Salle finale ?";
	private static final String ANEWROOM = "A new room";
	private static final String BLANKSPACE = " ";
	private static final String FONTNAME = "BLKCHCRY.TTF";
	
	@Inject
	public DungeonCreatorViewPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		this.parent = parent;
		dungeon = ModelProvider.INSTANCE.getDungeon(); //Dungeon.getInstance();
		this.id = dungeon.sizeOfRooms();
		this.loadFont(FONTNAME);
		buildUI();
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
	
	private void buildUI() {
		parent.setLayout(new GridLayout(2, false));
		this.createLeftColumn();
		this.createRightColumn();
		
		this.addListeners();
	}
	
	/**
	 * Builds the left part of the UI : Room creation
	 */
	private void createLeftColumn() {
		compositeLeft = new Composite(parent, SWT.BORDER);
        leftData = new GridData(SWT.FILL, SWT.FILL, true, true);
        compositeLeft.setLayoutData(leftData);
        
        // Button new room
        GridLayout gl = new GridLayout(1, false);
        compositeLeft.setLayout(gl);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        newRoomButton = new Button(compositeLeft, SWT.BORDER);
        newRoomButton.setText(NEWROOM);
        newRoomButton.setLayoutData(gd);
        // End of button new room
        
        GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        tableRooms = new Table(compositeLeft, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
        tableRooms.setLayoutData(gd_table);
        tableRooms.setLinesVisible(false);
        tableRooms.setHeaderVisible(false);
        
        for (Room r : dungeon.getRooms()) {
            TableItem item = new TableItem(tableRooms, SWT.NULL);
            item.setData(r);
            item.setText(r.getName());
        }
        
        removeRoom = new Button(compositeLeft, SWT.BORDER);
        removeRoom.setText(DELETEROOM);
        
        compositeLeft.pack();
	}
	
	/**
	 * Creates the right part of the UI : Modify the room itself
	 */
	private void createRightColumn() { 
		compositeRight = new Composite(parent, SWT.BORDER);
        rightData = new GridData(SWT.FILL, SWT.FILL, true, true);
        compositeRight.setLayoutData(rightData);
        
        // Room edit block
        GridLayout gl = new GridLayout(2, false);
        compositeRight.setLayout(gl);
        Label labelName = new Label(compositeRight, SWT.NONE);
        labelName.setText(ROOMNAME);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        roomName = new Text(compositeRight, SWT.BORDER | SWT.SEARCH);
        roomName.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        roomName.setEnabled(false);
        roomName.setLayoutData(gd);
        Label labelDesc = new Label(compositeRight, SWT.NONE);
        labelDesc.setText(ROOMDESC);
        roomDesc = new Text(compositeRight, SWT.BORDER | SWT.SEARCH);
        roomDesc.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        roomDesc.setEnabled(false);
        roomDesc.setLayoutData(gd);
        Label labelFinish = new Label(compositeRight, SWT.NONE);
        labelFinish.setText(ISFINALROOM);
        checkFinal = new Button(compositeRight, SWT.CHECK);
        checkFinal.setLayoutData(gd);
        checkFinal.setEnabled(false);
        // End of room edit block
        
        // Event and Links edit block
        folder = new CTabFolder(compositeRight, SWT.TOP);
        GridData data = new GridData(SWT.FILL,
                SWT.FILL, true, true,
                2, 1);
        folder.setLayoutData(data);
        eventTab = new CTabItem(folder, SWT.BORDER);
        eventTab.setText("Gestion des évènements");
        this.manageEvents();
        linkTab = new CTabItem(folder, SWT.BORDER);
        linkTab.setText("Gestion des liens");
        this.manageLinks();
        folder.setEnabled(false);
        // End of event and links edit block
	}
	
	private void manageEvents() {
		Composite c = new Composite(folder, SWT.NONE);
		c.setLayout(new GridLayout(3, false));
		GridData dataEvent = new GridData(SWT.FILL, SWT.NONE, true, false);
		dataEvent.horizontalSpan = 2;
		Label initDescLabel = new Label(c, SWT.NONE);
		initDescLabel.setText(INITEVENT);
		initDescEvent = new Text(c, SWT.BORDER);
		initDescEvent.setLayoutData(dataEvent);
		Label checkOpponentLabel = new Label(c, SWT.NONE);
		checkOpponentLabel.setText(NEEDSVALIDATION);
		checkOpponent = new Button(c, SWT.CHECK);
		checkOpponent.setLayoutData(dataEvent);
		Label finalDescLabel = new Label(c, SWT.NONE);
		finalDescLabel.setText(VALIDEVENT);
		finalDescEvent = new Text(c, SWT.BORDER);
		finalDescEvent.setLayoutData(dataEvent);
		finalDescEvent.setEnabled(false);
		Label separator = new Label(c, SWT.HORIZONTAL | SWT.SEPARATOR);
        GridData gdSeparator = new GridData(GridData.FILL_HORIZONTAL);
        gdSeparator.horizontalSpan = 3;
        separator.setLayoutData(gdSeparator);
        
        Label labelOpponent = new Label(c, SWT.NONE);
        labelOpponent.setText(OPPONENT);
        nameOpponent = new Text(c, SWT.BORDER);
        nameOpponent.setLayoutData(dataEvent);
        nameOpponent.setEnabled(false);
        Label labelHP = new Label(c, SWT.NONE);
        labelHP.setText(HP);
        hpOpponent = new Spinner(c, SWT.BORDER | SWT.SEARCH);
        hpOpponent.setEnabled(false);
        hpOpponent.setLayoutData(dataEvent);
        Label labelStr = new Label(c, SWT.NONE);
        labelStr.setText(STRENGTH);
        strengthOpponent = new Spinner(c, SWT.BORDER | SWT.SEARCH);
        strengthOpponent.setEnabled(false);
        strengthOpponent.setLayoutData(dataEvent);
        
        separator = new Label(c, SWT.HORIZONTAL | SWT.SEPARATOR);
        separator.setLayoutData(gdSeparator);
        Label cardLabel = new Label(c, SWT.NONE);
        cardLabel.setText(CARD);
        Label actionLabel = new Label(c, SWT.NONE);
        actionLabel.setText(CONSEQUENCES);
        Label addLabel = new Label(c, SWT.NONE);
        addLabel.setText(ADDWORD);
		cardList = new Combo(c, SWT.READ_ONLY);
		cardList.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		cardList.setEnabled(false);
		actionCard = new Text(c, SWT.BORDER);
		actionCard.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		actionCard.setEnabled(false);
		addCardAction = new Button(c, SWT.BORDER);
		addCardAction.setText(ADD);
		addCardAction.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		
		String[] titles = { CARD, CONSEQUENCES };
		tableCardsEvent = new Table(c, SWT.BORDER | SWT.V_SCROLL);
		tableCardsEvent.setLinesVisible(true);
		tableCardsEvent.setHeaderVisible(true);
		for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
            TableColumn column = new TableColumn(tableCardsEvent, SWT.NULL);
            column.setText(titles[loopIndex]);
        }
        for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
        	tableCardsEvent.getColumn(loopIndex).pack();
        }
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.horizontalSpan = 3;
        tableCardsEvent.setLayoutData(gd);
        tableCardsEvent.setEnabled(false);
		
        eventTab.setControl(c);
	}
	
	private void manageLinks() {
		Composite c = new Composite(folder, SWT.NONE);
		c.setLayout(new GridLayout(3, false));
		roomList = new Combo(c, SWT.READ_ONLY);
		roomList.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		checkAccessible = new Button(c, SWT.CHECK);
		checkAccessible.setText(NATIVEACCESS);
		linkButton = new Button(c, SWT.BORDER);
		linkButton.setText(LINK);
		linkButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		String[] titles = { ROOM, ACCESS };
        tableLinks = new Table(c, SWT.BORDER | SWT.V_SCROLL);
        tableLinks.setLinesVisible(true);
        tableLinks.setHeaderVisible(true);
        for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
            TableColumn column = new TableColumn(tableLinks, SWT.NULL);
            column.setText(titles[loopIndex]);
        }
        for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
        	tableLinks.getColumn(loopIndex).pack();
        }
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.horizontalSpan = 3;
        tableLinks.setLayoutData(gd);
        removeLink = new Button(c, SWT.BORDER);
        removeLink.setText(REMOVELINK);
        linkTab.setControl(c);
	}
	
	private void addListeners() {
		parent.addListener(SWT.Resize, new Listener()
	    {
	        @Override
	        public void handleEvent(Event arg0)
	        {
	            Point size = parent.getSize();

	            leftData.widthHint = (int) (size.x * 0.3);
	            rightData.widthHint = size.x - leftData.widthHint;
	        }
	    });
		
		newRoomButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Room r = new Room(id, ROOM + BLANKSPACE + id, ANEWROOM, false);
				dungeon.addRoom(r);
				TableItem item = new TableItem(tableRooms, SWT.NONE);
				item.setData(r);
				item.setText(r.getName());
				id++;
			}
		});
		
		tableRooms.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Room r = (Room) e.item.getData();
				roomName.setText(r.getName());
				roomDesc.setText(r.getDescription());
				checkFinal.setSelection(r.isFinish());
				roomName.setEnabled(true);
				roomDesc.setEnabled(true);
				if(!checkFinal.getSelection()) {
					checkFinal.setEnabled(!dungeon.hasOneFinalRoom());
				}
				else {
					checkFinal.setEnabled(true);
				}
				folder.setEnabled(true);
				this.fillTheEvent(r);
				this.fillTheLinks(r);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
			
			private void fillTheEvent(Room r) {
				tableCardsEvent.removeAll();
				initDescEvent.setText(r.getEvent().getInitialDescription());
				boolean haveAnOpponent = r.getEvent().isNeedsValidation();
				checkOpponent.setSelection(haveAnOpponent);
				finalDescEvent.setEnabled(haveAnOpponent);
		        nameOpponent.setEnabled(haveAnOpponent);
		        hpOpponent.setEnabled(haveAnOpponent);
		        strengthOpponent.setEnabled(haveAnOpponent);
				cardList.setEnabled(haveAnOpponent);
				actionCard.setEnabled(haveAnOpponent);
		        tableCardsEvent.setEnabled(haveAnOpponent);
				finalDescEvent.setText(r.getEvent().getFinalDescription());
				nameOpponent.setText(r.getEvent().getOpponent().getName());
		        hpOpponent.setSelection(r.getEvent().getOpponent().getHp());
		        strengthOpponent.setSelection(r.getEvent().getOpponent().getStr());
		        Iterator<Entry<Card, String>> it = r.getEvent().getActions().entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<Card, String> pair = (Map.Entry<Card, String>)it.next();
		            TableItem item = new TableItem(tableCardsEvent, SWT.NONE);
		            Card c = (Card)pair.getKey();
		            item.setData(c);
		            item.setText(0, c.getName() + " - Force : " + c.getPower());
		            item.setText(1, (String) pair.getValue()); 
		            //it.remove(); // avoids a ConcurrentModificationException
		        }
		        this.fillTheCards();
			}
			
			private void fillTheLinks(Room r) {
				tableLinks.removeAll();
				roomList.removeAll();
				List<Room> dRooms = new ArrayList<>();
				dRooms.add(r);
				List<Link> links = r.getLinks();
				for(Link l : links) {
					TableItem item = new TableItem(tableLinks, SWT.NONE);
					item.setData(l);
					item.setText(0, l.getNextRoom().getName());
					item.setText(1, String.valueOf(l.isAccessible()));
					dRooms.add(l.getNextRoom());
				}
				for(Room allRooms : dungeon.getRooms()) {
					if(!dRooms.contains(allRooms))
						roomList.add(allRooms.getId() + " - " + allRooms.getName());
				}
			}
			
			private void fillTheCards() {
				cardList.removeAll();
				List<Card> dCards = new ArrayList<>();
				TableItem[] items = tableCardsEvent.getItems();
				for(TableItem item : items) {
					dCards.add((Card) item.getData());
				}
				List<Card> allCards = new ArrayList<>();
				for(Persona p : dungeon.getPersonas()) {
					for(Card c : p.getDeck()) {
						if(!allCards.contains(c)) {
							allCards.add(c);
						}
					}
				}
				for(Card c : allCards) {
					if(!dCards.contains(c)) {
						cardList.add(c.getName() + " - Force : " + c.getPower());
						cardList.setData(c.getName() + " - Force : " + c.getPower(), c);
					}
				}
			}
		});
		
		tableRooms.addMouseTrackListener(new MouseTrackListener() {
			
			PopupDialog popup;
			
			@Override
			public void mouseHover(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				if(popup != null) {
					popup.close();
				}
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				Point pt = new Point(e.x, e.y);
				TableItem item = tableRooms.getItem(pt);
				Room currentRoom = null;
				if(item != null) {
					currentRoom = (Room) item.getData();
					//tableRooms.setToolTipText(currentRoom.getDescription());
					
					popup = new PopupDialog(((Composite) e.getSource()).getShell(), SWT.BORDER, false, false, false, false, false, "Entrées et sorties de la salle " + currentRoom.getName(), getRoomEntrancesAndExits(currentRoom));
					popup.create();
					popup.open();
				}
			}
			
			private String getRoomEntrancesAndExits(Room currentRoom) {
				String formattedString = "";
				List<Room> entrances = dungeon.getEntrancesOfRoom(currentRoom);
				if(entrances.size() > 0) {
					formattedString = "Entrées par : \n";
					for(Room r : entrances) {
						formattedString += r.getName() + "\n";
					}
				}
				List<Room> exits = dungeon.getExitsOfRoom(currentRoom);
				if(exits.size() > 0) {
					formattedString += "Sorties par : \n";
					for(Room r : exits) {
						formattedString += r.getName() + "\n";
					}
				}
				return formattedString;
			}
		});
		
		roomName.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				int indexOfRoom = tableRooms.getSelectionIndex();
				Room r = (Room) tableRooms.getItem(indexOfRoom).getData();
				r.setName(roomName.getText());
				tableRooms.getItem(indexOfRoom).setText(roomName.getText());
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		roomDesc.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				int indexOfRoom = tableRooms.getSelectionIndex();
				Room r = (Room) tableRooms.getItem(indexOfRoom).getData();
				r.setDescription(roomDesc.getText());	
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		checkFinal.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				int indexOfRoom = tableRooms.getSelectionIndex();
				Room r = (Room) tableRooms.getItem(indexOfRoom).getData();
				r.setFinish(!r.isFinish());	
				if(r.isFinish()) {
					tableLinks.removeAll();
					r.getLinks().clear();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		linkButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int index = roomList.getSelectionIndex();
				if(index != -1) {
					String roomId =  roomList.getItem(index).split(" - ")[0];
					Room r = dungeon.getRoomById(Integer.parseInt(roomId));
					boolean checked = checkAccessible.getSelection();
					Link l = new Link(r, checked);
					int indexOfRoom = tableRooms.getSelectionIndex();
					Room workingRoom = (Room) tableRooms.getItem(indexOfRoom).getData();
					workingRoom.addLink(l);
					TableItem item = new TableItem(tableLinks, SWT.NONE);
					item.setText(0, r.getName());
					item.setText(1, String.valueOf(checked));
					item.setData(l);
					roomList.remove(index);
				}
			}
		});
		
		tableLinks.addListener(SWT.Resize, new Listener()
	    {
	        @Override
	        public void handleEvent(Event arg0)
	        {
	            Point size = tableLinks.getSize();
	            
	            TableColumn[] columns = tableLinks.getColumns();
	            columns[0].setWidth((int)(size.x*0.8));
	            columns[1].setWidth((int)(size.x*0.2));
	        }
	    });
		
		removeLink.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int indexLink = tableLinks.getSelectionIndex();
				if(indexLink != -1) {
					Link l = (Link) tableLinks.getItem(indexLink).getData();
					int indexOfRoom = tableRooms.getSelectionIndex();
					Room workingRoom = (Room) tableRooms.getItem(indexOfRoom).getData();
					workingRoom.removeLink(l);
					tableLinks.remove(indexLink);
					roomList.add(l.getNextRoom().getId() + " - " + l.getNextRoom().getName());
				}
			}
		});
		
		removeRoom.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				boolean error = false;
				int indexOfRoom = tableRooms.getSelectionIndex();
				if(indexOfRoom != -1) {
					Room checkRoom = (Room) tableRooms.getItem(indexOfRoom).getData();
					if(!checkRoom.isStart()) {
						for(Room r : dungeon.getRooms()) {
							List<Link> links = r.getLinks();
							for(Link l : links) {
								if(l.getNextRoom().getId().equals(checkRoom.getId())) {
									error = true;
								}
							}
						}
						if(error) {
							MessageDialog.openWarning(parent.getShell(), "Erreur", "Des salles sont liées à celle-ci. Veuillez retirer les liens vers cette salle.");
						}
						else {
							tableRooms.remove(indexOfRoom);
							dungeon.removeRoom(checkRoom);
						}
					}
					else {
						MessageDialog.openWarning(parent.getShell(), "Erreur", "Impossible de supprimer la première salle.");
					}
				}
			}
		});
		
		checkOpponent.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean haveAnOpponent = ((Button) e.getSource()).getSelection();
				finalDescEvent.setEnabled(haveAnOpponent);
		        nameOpponent.setEnabled(haveAnOpponent);
		        hpOpponent.setEnabled(haveAnOpponent);
		        strengthOpponent.setEnabled(haveAnOpponent);
				cardList.setEnabled(haveAnOpponent);
				actionCard.setEnabled(haveAnOpponent);
		        tableCardsEvent.setEnabled(haveAnOpponent);
				int indexOfRoom = tableRooms.getSelectionIndex();
				Room r = (Room) tableRooms.getItem(indexOfRoom).getData();
				r.getEvent().setNeedsValidation(haveAnOpponent);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		initDescEvent.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				int indexOfRoom = tableRooms.getSelectionIndex();
				Room r = (Room) tableRooms.getItem(indexOfRoom).getData();
				r.getEvent().setInitialDescription(initDescEvent.getText());
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		finalDescEvent.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				int indexOfRoom = tableRooms.getSelectionIndex();
				Room r = (Room) tableRooms.getItem(indexOfRoom).getData();
				r.getEvent().setFinalDescription(finalDescEvent.getText());
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		nameOpponent.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				int indexOfRoom = tableRooms.getSelectionIndex();
				Room r = (Room) tableRooms.getItem(indexOfRoom).getData();
				r.getEvent().getOpponent().setName(nameOpponent.getText());;
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
        hpOpponent.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				int indexOfRoom = tableRooms.getSelectionIndex();
				Room r = (Room) tableRooms.getItem(indexOfRoom).getData();
				r.getEvent().getOpponent().setHp(Integer.parseInt(hpOpponent.getText()));;
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        
        strengthOpponent.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				int indexOfRoom = tableRooms.getSelectionIndex();
				Room r = (Room) tableRooms.getItem(indexOfRoom).getData();
				r.getEvent().getOpponent().setStr(Integer.parseInt(strengthOpponent.getText()));;
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        
        tableCardsEvent.addListener(SWT.Resize, new Listener()
	    {
	        @Override
	        public void handleEvent(Event arg0)
	        {
	            Point size = tableCardsEvent.getSize();
	            
	            TableColumn[] columns = tableCardsEvent.getColumns();
	            columns[0].setWidth((int)(size.x*0.3));
	            columns[1].setWidth((int)(size.x*0.7));
	        }
	    });
        
        addCardAction.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int roomId = tableRooms.getSelectionIndex();
				int indexOfCard = cardList.getSelectionIndex();
				if(indexOfCard != -1) {
					String action = actionCard.getText();
					Card c = (Card) cardList.getData(cardList.getItem(indexOfCard));
					TableItem item = new TableItem(tableCardsEvent, SWT.NONE);
					item.setData(c);
					item.setText(0, c.getName() + " - Force : " + c.getPower()); 
					item.setText(1, action);
					dungeon.getRoomById(roomId).getEvent().addAction(c, action);
				}
			}
		});
	}
	
}