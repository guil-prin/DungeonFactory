 
package viewparts;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import data.Dungeon;
import data.Link;
import data.Room;

public class DungeonCreatorViewPart {
	
	private Integer id;
	
	private Composite parent;
	private Dungeon dungeon;
	
	private Composite compositeLeft, compositeRight;
	private GridData leftData, rightData;
	private Button newRoomButton, checkFinal, linkButton, checkAccessible;
	private Table tableRooms, tableLinks;
	private Text roomName, roomDesc;
	private CTabFolder folder;
	private CTabItem eventTab, linkTab;
	private Combo roomList;
	
	@Inject
	public DungeonCreatorViewPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		this.parent = parent;
		dungeon = Dungeon.getInstance();
		this.id = dungeon.sizeOfRooms() + 1;
		buildUI();
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
        newRoomButton.setText("Nouvelle salle");
        newRoomButton.setLayoutData(gd);
        // End of button new room
        
        GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        tableRooms = new Table(compositeLeft, SWT.BORDER | SWT.V_SCROLL);
        tableRooms.setLayoutData(gd_table);
        tableRooms.setLinesVisible(false);
        tableRooms.setHeaderVisible(false);
        
        for (Room r : dungeon.getRooms()) {
            TableItem item = new TableItem(tableRooms, SWT.NULL);
            item.setData(r);
            item.setText(r.getName());
        }
        
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
        labelName.setText("Nom : ");
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        roomName = new Text(compositeRight, SWT.BORDER | SWT.SEARCH);
        roomName.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        roomName.setEnabled(false);
        roomName.setLayoutData(gd);
        Label labelDesc = new Label(compositeRight, SWT.NONE);
        labelDesc.setText("Description : ");
        roomDesc = new Text(compositeRight, SWT.BORDER | SWT.SEARCH);
        roomDesc.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        roomDesc.setEnabled(false);
        roomDesc.setLayoutData(gd);
        Label labelFinish = new Label(compositeRight, SWT.NONE);
        labelFinish.setText("Salle finale ?");
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
		
	}
	
	private void manageLinks() {
		Composite c = new Composite(folder, SWT.NONE);
		c.setLayout(new GridLayout(3, false));
		roomList = new Combo(c, SWT.READ_ONLY);
		roomList.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		checkAccessible = new Button(c, SWT.CHECK);
		checkAccessible.setText("Salle accessible nativement ?");
		linkButton = new Button(c, SWT.BORDER);
		linkButton.setText("Lier à cette salle");
		linkButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		String[] titles = { "Salle", "Accessible" };
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
				Room r = new Room(id, "Room " + id, "A new room", false);
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
				checkFinal.setEnabled(true);
				folder.setEnabled(true);
				
				this.fillTheLinks();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
			
			private void fillTheLinks() {
				tableLinks.removeAll();
				roomList.removeAll();
				int indexOfRoom = tableRooms.getSelectionIndex();
				Room selectedRoom = (Room) tableRooms.getItem(indexOfRoom).getData();
				List<Room> dRooms = new ArrayList<>();
				dRooms.add(selectedRoom);
				List<Link> links = selectedRoom.getLinks();
				for(Link l : links) {
					TableItem item = new TableItem(tableLinks, SWT.NONE);
					item.setData(l);
					item.setText(0, l.getNextRoom().getId() + " - " + l.getNextRoom().getName());
					item.setText(1, String.valueOf(l.isAccessible()));
					dRooms.add(l.getNextRoom());
				}
				for(Room r : dungeon.getRooms()) {
					if(!dRooms.contains(r))
						roomList.add(r.getId() + " - " + r.getName());
				}
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
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}