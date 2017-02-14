package game;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Text;

import data.Card;
import data.Dungeon;
import data.Link;
import data.Persona;
import data.Room;

public class Game {

	private Display display;
	private Shell shell;
	private Dungeon dungeon;
	private Persona currentPersona;
	private List<Card> currentDeck;
	private Room currentRoom;
	
	private Composite mainComposite, topComposite, midComposite, botComposite, descCurrentRoom, picRoom, stateEvent;
	GridData midLeftData, midRightData, midCenterData;
	private Table nextRooms;
	private Text descChar, roomName, roomDesc;
	
	public Game() {
		
	}
	
	public Game(Display display) {
		this.display = display;
		this.shell = new Shell(display);
		this.dungeon = Dungeon.getInstance();
		this.currentPersona = dungeon.getPersonas().get(0);
		this.currentRoom = dungeon.getRoomById(0);
		this.currentDeck = new ArrayList<>();
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
		GridData topData = new GridData(SWT.FILL, SWT.NONE, true, false);
		topComposite.setLayoutData(topData);
		GridLayout gl = new GridLayout(1, false);
		topComposite.setLayout(gl);
		
		descChar = new Text(topComposite, SWT.NONE);
		descChar.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
		descChar.setEditable(false);
		roomName = new Text(topComposite, SWT.NONE);
		roomName.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
		roomName.setEditable(false);
	}
	
	private void buildMidUI() {
		midComposite = new Composite(mainComposite, SWT.NONE);
		GridData midData = new GridData(SWT.FILL, SWT.FILL, true, true);
		midComposite.setLayoutData(midData);
		GridLayout gl = new GridLayout(3, false);
		midComposite.setLayout(gl);
		
		midLeftData = new GridData(SWT.FILL, SWT.FILL, true, true);
		midRightData = new GridData(SWT.FILL, SWT.FILL, true, true);
		midCenterData = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		descCurrentRoom = new Composite(midComposite, SWT.BORDER);
		descCurrentRoom.setLayoutData(midLeftData);
		GridLayout descGridLayout = new GridLayout();
		descCurrentRoom.setLayout(descGridLayout);
		
		roomDesc = new Text(descCurrentRoom, SWT.NONE);
		roomDesc.setLayoutData(midData);
		roomDesc.setEditable(false);
		
		picRoom = new Composite(midComposite, SWT.BORDER);
		picRoom.setLayoutData(midCenterData);
		GridLayout picLayout = new GridLayout();
		picRoom.setLayout(picLayout);
		
		stateEvent = new Composite(midComposite, SWT.BORDER);
		stateEvent.setLayoutData(midRightData);
		GridLayout stateLayout = new GridLayout();
		stateEvent.setLayout(stateLayout);
		
		Label l2 = new Label(stateEvent, SWT.WRAP);
		final GridData data = new GridData(SWT.HORIZONTAL, SWT.TOP, true, false, 1, 1);
		l2.setLayoutData(data);
		l2.setText(currentRoom.getEvent().getInitialDescription());
		
	}
	
	private void buildBotUI() {
		botComposite = new Composite(mainComposite, SWT.BORDER);
		GridData botData = new GridData(SWT.FILL, SWT.NONE, true, false);
		botComposite.setLayoutData(botData);
		GridLayout gl = new GridLayout(5, false);
		botComposite.setLayout(gl);
		
		nextRooms = new Table(botComposite, SWT.BORDER);
		
		Composite card1 = new Composite(botComposite, SWT.BORDER);
		card1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout glc = new GridLayout();
		card1.setLayout(glc);
		
		Composite card2 = new Composite(botComposite, SWT.BORDER);
		card2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		card2.setLayout(glc);
		
		Composite card3 = new Composite(botComposite, SWT.BORDER);
		card3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		card3.setLayout(glc);
		
		Composite card4 = new Composite(botComposite, SWT.BORDER);
		card4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		card4.setLayout(glc);
	}
	
	private void fillNextRooms() {
		nextRooms.removeAll();
		for(Link l : currentRoom.getLinks()) {
			TableItem item = new TableItem(nextRooms, SWT.NONE);
			item.setData(l);
			item.setText(l.getNextRoom().getName());
		}
	}
	
	private void initializeValues() {
		this.refreshCurrentPersonaData();
		this.refreshCurrentRoomData();
	}
	
	private void refreshCurrentPersonaData() {
		descChar.setText("Votre personnage : " + currentPersona.getName() + " - " + currentPersona.getHp() + " HP");
	}
	
	private void refreshCurrentRoomData() {
		roomName.setText("Votre salle actuelle : " + currentRoom.getName());
		roomDesc.setText(currentRoom.getDescription());
		this.fillNextRooms();
	}
	
	private void addListeners() {
		nextRooms.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int index = nextRooms.getSelectionIndex();
				Link l = (Link) nextRooms.getItem(index).getData();
				currentRoom = l.getNextRoom();
				refreshCurrentRoomData();
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
	            //rightData.widthHint = size.x - leftData.widthHint;
	        }
	    });
	}
	
}
