 
package viewparts;

import javax.inject.Inject;


import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
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
	
	private Table table;
	
	@Inject
	public CharacterCreatorViewPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		this.parent = parent; 
		dungeon = Dungeon.getInstance();
		buildUI();
	}
	
	private void buildUI() {
		parent.setLayout(new GridLayout(2, false));
		this.createLeftColumn();
		this.createRightColumn();
	}
	
	private void createLeftColumn() {
		Composite compositeLeft = new Composite(parent, SWT.BORDER);
		
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight= 0;
        gridLayout.marginTop = 0;
        gridLayout.marginBottom = 0;
        gridLayout.horizontalSpacing = 0;

        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 0;
        compositeLeft.setLayout(gridLayout);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        compositeLeft.setLayoutData(gridData);

        Label label = new Label(compositeLeft, SWT.NONE);
        label.setText("Nouveau personnage : ");
        
        Text text = new Text(compositeLeft, SWT.BORDER | SWT.SEARCH);
        text.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        
        Button add = new Button(compositeLeft, SWT.BORDER);
        add.setText("+");
        
        
        table = new Table(compositeLeft, SWT.BORDER | SWT.V_SCROLL);
        table.setLinesVisible(false);
        table.setHeaderVisible(false);
        
        for (Persona p : dungeon.getPersonas()) {
            TableItem item = new TableItem(table, SWT.NULL);
            item.setText(p.getName());
        }
        
        
    	add.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Persona persona = new Persona(text.getText());
				dungeon.addPersona(persona);
				System.out.println(persona.getName() + " - " + persona.getHp() + " hp");
				System.out.println(dungeon.sizeOfPersonas() + " characters");
				TableItem item = new TableItem(table, SWT.NULL);
	            item.setText(persona.getName());
	            text.setText("");
			}
		});
    	
        compositeLeft.pack();
	}
	
	private void createRightColumn() {
		Composite compositeLeft = new Composite(parent, SWT.BORDER);
		
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight= 0;
        gridLayout.marginTop = 0;
        gridLayout.marginBottom = 0;
        gridLayout.horizontalSpacing = 0;

        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 0;
        compositeLeft.setLayout(gridLayout);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        compositeLeft.setLayoutData(gridData);
        
        Label labelName = new Label(compositeLeft, SWT.NONE);
        labelName.setText("Nom : ");
        
        Text textName = new Text(compositeLeft, SWT.BORDER | SWT.SEARCH);
        textName.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        textName.setEnabled(false);
        
        Label labelHP = new Label(compositeLeft, SWT.NONE);
        labelHP.setText("HP du personnage : ");
        
        Spinner textHP = new Spinner(compositeLeft, SWT.BORDER | SWT.SEARCH);
        textHP.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        textHP.setEnabled(false);
        
        Button delete = new Button(compositeLeft, SWT.NONE);
        delete.setText("Supprimer le personnage");
        delete.setEnabled(false);
        
	}
	
}