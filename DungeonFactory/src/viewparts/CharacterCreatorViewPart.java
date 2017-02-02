 
package viewparts;

import javax.inject.Inject;


import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import data.Dungeon;
import data.Persona;

public class CharacterCreatorViewPart {
	
	private Composite parent;
	private Dungeon dungeon;
	
	private Composite compositeLeft, compositeRight;
	private GridData leftData, rightData;
	private Table tablePersonas, tableCards;
	private Text textName;
	private Spinner textHP;
	private Button deleteCharacter;
	
	@Inject
	public CharacterCreatorViewPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		this.parent = parent; 
		dungeon = Dungeon.getInstance();
		buildUI();
	}
	
	/**
	 * Builds the UI
	 */
	private void buildUI() {
		parent.setLayout(new GridLayout(2, false));
		this.createLeftColumn();
		this.createRightColumn();
		
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
	}
	
	/**
	 * Builds the left part of the UI : Character creation and display.
	 */
	private void createLeftColumn() {
		compositeLeft = new Composite(parent, SWT.BORDER);
        leftData = new GridData(SWT.FILL, SWT.FILL, true, true);
        compositeLeft.setLayoutData(leftData);
        
        // Block New character
        GridLayout gl = new GridLayout(3, false);
        compositeLeft.setLayout(gl);
        Label namePersonaLabel = new Label(compositeLeft, SWT.NONE);
        namePersonaLabel.setText("Nouveau perso :");
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        Text namePersonaField = new Text(compositeLeft, SWT.BORDER | SWT.SINGLE);
        namePersonaField.setLayoutData(gd);
        Button addPersonaButton = new Button(compositeLeft, SWT.BORDER);
        addPersonaButton.setText("+");
        // End of block New Character
        
        GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        tablePersonas = new Table(compositeLeft, SWT.BORDER | SWT.V_SCROLL);
        tablePersonas.setLayoutData(gd_table);
        tablePersonas.setLinesVisible(false);
        tablePersonas.setHeaderVisible(false);
        
        
        for (Persona p : dungeon.getPersonas()) {
            TableItem item = new TableItem(tablePersonas, SWT.NULL);
            item.setData(p);
            item.setText(p.getName());
        }
        
        
        // Block listeners
    	addPersonaButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Persona persona = new Persona(namePersonaField.getText());
				dungeon.addPersona(persona);
				System.out.println(persona.getName() + " - " + persona.getHp() + " hp");
				System.out.println(dungeon.sizeOfPersonas() + " characters");
				TableItem item = new TableItem(tablePersonas, SWT.NULL);
	            item.setData(persona);
				item.setText(persona.getName());
	            namePersonaField.setText("");
			}
		});
    	tablePersonas.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				textName.setText(((Persona) e.item.getData()).getName());
				textHP.setSelection(((Persona) e.item.getData()).getHp());
				textName.setEnabled(true);
				textHP.setEnabled(true);
				deleteCharacter.setEnabled(true);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
    	// End of block listeners
    	
        compositeLeft.pack();
	}
	
	/**
	 * Creates the right part of the UI : Modify selected character stats and cards.
	 */
	private void createRightColumn() {
		compositeRight = new Composite(parent, SWT.BORDER);
        rightData = new GridData(SWT.FILL, SWT.FILL, true, true);
        compositeRight.setLayoutData(rightData);
        
        // Character edit block
        GridLayout gl = new GridLayout(6, false);
        compositeRight.setLayout(gl);
        Label labelName = new Label(compositeRight, SWT.NONE);
        labelName.setText("Nom : ");
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 3;
        textName = new Text(compositeRight, SWT.BORDER | SWT.SEARCH);
        textName.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        textName.setEnabled(false);
        textName.setLayoutData(gd);
        Label labelHP = new Label(compositeRight, SWT.NONE);
        labelHP.setText("HP : ");
        textHP = new Spinner(compositeRight, SWT.BORDER | SWT.SEARCH);
        textHP.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        textHP.setEnabled(false);
        // End of character edit block
        
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
        Label sep = new Label(compositeRight, SWT.HORIZONTAL | SWT.SEPARATOR);
        GridData gdSep = new GridData(GridData.FILL_HORIZONTAL);
        sep.setLayoutData(gdSep);
        
        Text textCardName = new Text(compositeRight, SWT.BORDER | SWT.SEARCH);
        textCardName.setEnabled(false);
        Text textCardTag = new Text(compositeRight, SWT.BORDER | SWT.SEARCH);
        textCardTag.setEnabled(false);
        Text textCardDesc = new Text(compositeRight, SWT.BORDER | SWT.SEARCH);
        textCardDesc.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        textCardDesc.setEnabled(false);
        Text textCardPower = new Text(compositeRight, SWT.BORDER | SWT.SEARCH);
        textCardPower.setEnabled(false);
        Text textCardQty = new Text(compositeRight, SWT.BORDER | SWT.SEARCH);
        textCardQty.setEnabled(false);
        Button addCard = new Button(compositeRight, SWT.NONE);
        GridData buttonGd = new GridData(GridData.FILL_HORIZONTAL);
        addCard.setLayoutData(buttonGd);
        addCard.setText("+");
        // End of card edit block
        
        GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1);
        tableCards = new Table(compositeRight, SWT.BORDER | SWT.V_SCROLL);
        tableCards.setLayoutData(gd_table);
        tableCards.setLinesVisible(false);
        tableCards.setHeaderVisible(false);
        
        
        deleteCharacter = new Button(compositeRight, SWT.NONE);
        deleteCharacter.setText("Supprimer");
        deleteCharacter.setEnabled(false);
        
        deleteCharacter.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int indexOfPersona = tablePersonas.getSelectionIndex();
				Persona personaToDelete = (Persona) tablePersonas.getItem(indexOfPersona).getData();
				tablePersonas.remove(indexOfPersona);
				dungeon.removePersona(personaToDelete);
				textName.setText("");
				textHP.setSelection(0);
				textName.setEnabled(false);
				textHP.setEnabled(false);
				deleteCharacter.setEnabled(false);
			}
		});
        
	}
	
}