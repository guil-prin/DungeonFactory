 
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
	private Table tablePersonas;
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
        
        GridLayout gl = new GridLayout(4, false);
        compositeRight.setLayout(gl);
        Label labelName = new Label(compositeRight, SWT.NONE);
        labelName.setText("Nom : ");
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        textName = new Text(compositeRight, SWT.BORDER | SWT.SEARCH);
        textName.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        textName.setEnabled(false);
        textName.setLayoutData(gd);
        
        Label labelHP = new Label(compositeRight, SWT.NONE);
        labelHP.setText("HP : ");
        
        textHP = new Spinner(compositeRight, SWT.BORDER | SWT.SEARCH);
        textHP.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        textHP.setEnabled(false);
        
        
        deleteCharacter = new Button(compositeRight, SWT.NONE);
        deleteCharacter.setText("Supprimer");
        deleteCharacter.setEnabled(false);
        
	}
	
}