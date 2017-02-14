 
package viewparts;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import data.Card;
import data.Dungeon;
import data.Persona;
import model.ModelProvider;

public class CharacterCreatorViewPart {
	
	private Composite parent;
	private Dungeon dungeon;
	
	private Composite compositeLeft, compositeRight;
	private GridData leftData, rightData;
	private Table tablePersonas, tableCards;
	private Text textName, textCardName, textCardTag, textCardDesc, namePersonaField;
	private Spinner textHP, textCardPower, textCardQty;
	private Button addPersonaButton, deleteCharacter, addCard;
	
	@Inject
	public CharacterCreatorViewPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		this.parent = parent; 
		dungeon = ModelProvider.INSTANCE.getDungeon(); //Dungeon.getInstance();
		buildUI();
		
	}
	
	/**
	 * Builds the UI
	 */
	private void buildUI() {
		parent.setLayout(new GridLayout(2, false));
		this.createLeftColumn();
		this.createRightColumn();
		
		this.addListeners();
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
        namePersonaField = new Text(compositeLeft, SWT.BORDER | SWT.SINGLE);
        namePersonaField.setLayoutData(gd);
        addPersonaButton = new Button(compositeLeft, SWT.BORDER);
        addPersonaButton.setText("+");
        // End of block New Character
        
        GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        tablePersonas = new Table(compositeLeft, SWT.BORDER | SWT.V_SCROLL);
        tablePersonas.setLayoutData(gd_table);
        tablePersonas.setLinesVisible(false);
        tablePersonas.setHeaderVisible(false);
        
        deleteCharacter = new Button(compositeLeft, SWT.NONE);
        deleteCharacter.setText("Supprimer");
        deleteCharacter.setEnabled(false);
        
        for (Persona p : dungeon.getPersonas()) {
            TableItem item = new TableItem(tablePersonas, SWT.NULL);
            item.setData(p);
            item.setText(p.getName());
        }
        
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
        
	}
	
	private void switchStates(boolean editable) {
		textName.setEnabled(editable);
		textHP.setEnabled(editable);
		deleteCharacter.setEnabled(editable);
		textCardName.setEnabled(editable);
		textCardTag.setEnabled(editable);
		textCardDesc.setEnabled(editable);
		textCardPower.setEnabled(editable);
		textCardQty.setEnabled(editable);
		addCard.setEnabled(editable);
	}
	
	private void fillWithCards(Persona p) {
		List<Card> checked = new ArrayList<>();
		tableCards.removeAll();
		List<Card> cards = p.getDeck();
		for(Card c : cards) {
			if(!checked.contains(c)) {
				Integer nb = p.numberOfCard(c);
				TableItem item = new TableItem(tableCards, SWT.NONE);
				item.setData(c);
				item.setText(0, c.getName());
				item.setText(1, c.getTag());
				item.setText(2, c.getDescriptor());
				item.setText(3, String.valueOf(c.getPower()));
				item.setText(4, String.valueOf(nb));
				checked.add(c);
			}
		}
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
		
		addPersonaButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				String name = namePersonaField.getText();
				if(!dungeon.isPersonaExists(name)) {
					Persona persona = new Persona(name);
					dungeon.addPersona(persona);
					TableItem item = new TableItem(tablePersonas, SWT.NULL);
		            item.setData(persona);
					item.setText(persona.getName());
		            namePersonaField.setText("");
				}
				else {
					MessageDialog.openWarning(parent.getShell(), "Erreur", "Ce personnage existe déjà.");
				}
			}
		});
		
    	tablePersonas.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Persona p = (Persona) e.item.getData();
				textName.setText(p.getName());
				textHP.setSelection(p.getHp());
				switchStates(true);
				fillWithCards(p);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
    	
    	deleteCharacter.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int indexOfPersona = tablePersonas.getSelectionIndex();
				Persona personaToDelete = (Persona) tablePersonas.getItem(indexOfPersona).getData();
				tablePersonas.remove(indexOfPersona);
				dungeon.removePersona(personaToDelete);
				textName.setText("");
				textHP.setSelection(0);
				switchStates(false);
			}
		});
    	
    	addCard.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int indexOfPersona = tablePersonas.getSelectionIndex();
				Persona p = (Persona) tablePersonas.getItem(indexOfPersona).getData();
				Card c = new Card(textCardName.getText(), textCardTag.getText(), textCardDesc.getText(), Integer.parseInt(textCardPower.getText()));
				if(!p.isThisCardInDeck(c)) {
					for(int i = 0 ; i < Integer.parseInt(textCardQty.getText()) ; i++) {
						p.addCardInDeck(c);
					}
					TableItem item = new TableItem(tableCards, SWT.NONE);
					item.setData(c);
					item.setText(0, textCardName.getText());
					item.setText(1, textCardTag.getText());
					item.setText(2, textCardDesc.getText());
					item.setText(3, textCardPower.getText());
					item.setText(4, textCardQty.getText());
				}
				else {
					MessageDialog.openWarning(parent.getShell(), "Erreur", "Une carte avec ce tag existe déjà.");
				}
			}
		});
    	
    	tableCards.addListener(SWT.Resize, new Listener()
	    {
	        @Override
	        public void handleEvent(Event arg0)
	        {
	            Point size = tableCards.getSize();
	            
	            TableColumn[] columns = tableCards.getColumns();
	            columns[0].setWidth((int)(size.x*0.1));
	            columns[1].setWidth((int)(size.x*0.1));
	            columns[2].setWidth((int)(size.x*0.6));
	            columns[3].setWidth((int)(size.x*0.1));
	            columns[4].setWidth((int)(size.x*0.1));
	        }
	    });
    	
    	textName.addFocusListener(new FocusListener() {
    		
    		String oldName = "";
			
			@Override
			public void focusLost(FocusEvent e) {
				String name = textName.getText();
				int indexOfPersona = tablePersonas.getSelectionIndex();
				Persona p = (Persona) tablePersonas.getItem(indexOfPersona).getData();
				if(!dungeon.isPersonaExists(p, name)) {
					tablePersonas.getItem(indexOfPersona).setText(name);
					p.setName(name);
				}
				else {
					MessageDialog.openWarning(parent.getShell(), "Erreur", "Ce nom de personnage existe déjà, retour à son nom précédent.");
					textName.setText(oldName);
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				oldName = textName.getText();
			}
		});
    	
    	textHP.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				Integer hp = Integer.parseInt(textHP.getText());
				int indexOfPersona = tablePersonas.getSelectionIndex();
				Persona p = (Persona) tablePersonas.getItem(indexOfPersona).getData();
				p.setHp(hp);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
	}
	
}