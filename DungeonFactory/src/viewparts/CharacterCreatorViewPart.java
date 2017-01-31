 
package viewparts;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import data.Dungeon;
import data.Persona;

public class CharacterCreatorViewPart {
	
	private Composite parent;
	private Dungeon dungeon;
	
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
		this.createLeftColumn();
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
        add.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Persona persona = new Persona(text.getText());
				dungeon.addPersona(persona);
				System.out.println(persona.getName() + " - " + persona.getHp() + " hp");
				System.out.println(dungeon.sizeOfPersonas() + " characters");
			}
		});
	}
	
	
}