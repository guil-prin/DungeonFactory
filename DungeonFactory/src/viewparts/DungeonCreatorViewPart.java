 
package viewparts;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import data.Dungeon;
import data.Persona;
import data.Room;

public class DungeonCreatorViewPart {
	
	private Integer id;
	
	private Composite parent;
	private Dungeon dungeon;
	
	private Composite compositeLeft, compositeRight;
	private GridData leftData, rightData;
	private Button newRoomButton, checkFinal;
	private Table tableRooms;
	private Text roomName, roomDesc;
	
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
        
        
        
        /*
        Label separator = new Label(compositeRight, SWT.HORIZONTAL | SWT.SEPARATOR);
        GridData gdSeparator = new GridData(GridData.FILL_HORIZONTAL);
        gdSeparator.horizontalSpan = 6;
        separator.setLayoutData(gdSeparator);
        
        // Card edit block
        Label labelCardName = new Label(compositeRight, SWT.NONE);
        labelCardName.setText("Nom de la carte");
        Label labelCardTag = new Label(compositeRight, SWT.NONE);
        labelCardTag.setText("Tag de la carte");
        Label labelCarteDesc = new Label(compositeRight, SWT.NONE);
        labelCarteDesc.setText("Description de la carte");
        Label labelCardPower = new Label(compositeRight, SWT.NONE);
        labelCardPower.setText("Force");
        Label labelQty = new Label(compositeRight, SWT.NONE);
        labelQty.setText("Quantité");
        Label labelAdd = new Label(compositeRight, SWT.NONE);
        labelAdd.setText("Ajouter");
        
        textCardName = new Text(compositeRight, SWT.BORDER | SWT.SEARCH);
        textCardName.setEnabled(false);
        textCardTag = new Text(compositeRight, SWT.BORDER | SWT.SEARCH);
        textCardTag.setEnabled(false);
        textCardDesc = new Text(compositeRight, SWT.BORDER);
        textCardDesc.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        textCardDesc.setEnabled(false);
        textCardPower = new Spinner(compositeRight, SWT.BORDER | SWT.SEARCH);
        textCardPower.setEnabled(false);
        textCardQty = new Spinner(compositeRight, SWT.BORDER | SWT.SEARCH);
        textCardQty.setEnabled(false);
        textCardQty.setMinimum(1);
        addCard = new Button(compositeRight, SWT.NONE);
        GridData buttonGd = new GridData(GridData.FILL_HORIZONTAL);
        addCard.setLayoutData(buttonGd);
        addCard.setText("+");
        addCard.setEnabled(false);
        // End of card edit block
        
        // Card display block
        GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1);
        tableCards = new Table(compositeRight, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
        tableCards.setLayoutData(gd_table);
        String[] titles = { "Nom", "Tag", "Description", "Force", "Quantité" };
        tableCards.setLinesVisible(true);
        tableCards.setHeaderVisible(true);
        for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
            TableColumn column = new TableColumn(tableCards, SWT.NULL);
            column.setText(titles[loopIndex]);
        }
        
        for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
        	tableCards.getColumn(loopIndex).pack();
        }
        // End of card display block
        */
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
	}
	
}