 
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
import org.eclipse.swt.graphics.Font;
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
	private Text textName, textCardName, textCardDesc, namePersonaField;
	private Spinner textHP, textCardPower, textCardQty;
	private Button addPersonaButton, deleteCharacter, addCard, addOneCard, removeOneCard, deleteAllCards;
	private Font font;
	
	private static final String NEWCHAR_FIELD = "Nouveau perso :";
	private static final String ADD = "+";
	private static final String DELETE = "Supprimer";
	private static final String NAME_FIELD = "Nom :";
	private static final String HP_FIELD = "HP :";
	private static final String YOURCHAR = "Votre personnage";
	private static final String CARDMANAGER = "Gestion des cartes du personnage";
	private static final String CARDNAME_FIELD = "Nom de la carte";
	private static final String CARDDESC_FIELD = "Description de la carte";
	private static final String CARDSTR_FIELD = "Force";
	private static final String CARDQTY_FIELD = "Quantité";
	private static final String CARDADD = "Ajouter";
	private static final String NAMECOLUMN = "Nom";
	private static final String DESCCOLUMN = "Description";
	private static final String ADDONECARD = "+1 à la sélection";
	private static final String REMOVEONECARD = "-1 à la sélection";
	private static final String DELETESELECTION = "Supprimer la sélection";
	private static final String ERROR = "Erreur";
	private static final String CHARALREADYEXISTS = "Ce personnage existe déjà.";
	private static final String CHARCANCEL = "Retour à son nom précédent.";
	private static final String CARDALREADYEXISTS = "Cette carte existe déjà pour ce personnage.";
	
	@Inject
	public CharacterCreatorViewPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		this.parent = parent; 
		dungeon = ModelProvider.INSTANCE.getDungeon(); //Dungeon.getInstance();
		parent.getShell().getDisplay().loadFont("/font/BLKCHCRY.TTF");
		font = new Font(parent.getShell().getDisplay(), "BlackChancery", 12, SWT.NONE);
		buildUI();
		
	}
	
	/**
	 * Builds the UI
	 */
	private void buildUI() {
		parent.setLayout(new GridLayout(2, false));
		this.createLeftColumn();
		this.createRightColumn();
		this.switchStates(false);
		
		this.addListeners();
	}
	
	/**
	 * Builds the left part of the UI : Character creation and display.
	 */
	private void createLeftColumn() {
		compositeLeft = new Composite(parent, SWT.BORDER);
        leftData = new GridData(SWT.FILL, SWT.FILL, true, true);
        compositeLeft.setLayoutData(leftData);
        GridLayout gl = new GridLayout(3, false);
        compositeLeft.setLayout(gl);
        
        // Block New character
        Label namePersonaLabel = new Label(compositeLeft, SWT.NONE);
        namePersonaLabel.setText(NEWCHAR_FIELD);
        namePersonaLabel.setFont(font);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        namePersonaField = new Text(compositeLeft, SWT.BORDER | SWT.SINGLE);
        namePersonaField.setLayoutData(gd);
        addPersonaButton = new Button(compositeLeft, SWT.NONE);
        addPersonaButton.setText(ADD);
        // End of block New Character
        
        GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        tablePersonas = new Table(compositeLeft, SWT.BORDER | SWT.V_SCROLL);
        tablePersonas.setLayoutData(gd_table);
        tablePersonas.setLinesVisible(false);
        tablePersonas.setHeaderVisible(false);
        
        deleteCharacter = new Button(compositeLeft, SWT.NONE);
        deleteCharacter.setText(DELETE);
        
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
        GridLayout gl = new GridLayout(5, false);
        compositeRight.setLayout(gl);
        
        // Character edit block
        Composite charEdit = new Composite(compositeRight, SWT.NONE);
        charEdit.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 5, 1));
        charEdit.setLayout(new GridLayout(4, false));
        
        Label charHeader = new Label(charEdit, SWT.NONE);
        GridData gdCharHeader = new GridData(SWT.CENTER, SWT.NONE, true, false);
        gdCharHeader.horizontalSpan = 4;
        charHeader.setLayoutData(gdCharHeader);
        charHeader.setText(YOURCHAR);
        charHeader.setFont(font);
        
        Label labelName = new Label(charEdit, SWT.NONE);
        labelName.setText(NAME_FIELD);
        textName = new Text(charEdit, SWT.BORDER | SWT.SEARCH);
        textName.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_HORIZONTAL));
        Label labelHP = new Label(charEdit, SWT.NONE);
        labelHP.setText(HP_FIELD);
        textHP = new Spinner(charEdit, SWT.BORDER | SWT.SEARCH);
        // End of character edit block
        
        Label separator = new Label(compositeRight, SWT.HORIZONTAL | SWT.SEPARATOR);
        GridData gdSeparator = new GridData(GridData.FILL_HORIZONTAL);
        gdSeparator.horizontalSpan = 5;
        separator.setLayoutData(gdSeparator);
        
        // Card edit block
        Composite cardEdit = new Composite(compositeRight, SWT.NONE);
        cardEdit.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
        cardEdit.setLayout(new GridLayout(5, false));
        
        Label headerCard = new Label(cardEdit, SWT.NONE);
        GridData gdCardHeader = new GridData(SWT.CENTER, SWT.NONE, true, false);
        gdCardHeader.horizontalSpan = 5;
        headerCard.setLayoutData(gdCardHeader);
        headerCard.setText(CARDMANAGER);
        headerCard.setFont(font);
        Label labelCardName = new Label(cardEdit, SWT.NONE);
        labelCardName.setText(CARDNAME_FIELD);
        Label labelCarteDesc = new Label(cardEdit, SWT.NONE);
        labelCarteDesc.setText(CARDDESC_FIELD);
        Label labelCardPower = new Label(cardEdit, SWT.NONE);
        labelCardPower.setText(CARDSTR_FIELD);
        Label labelQty = new Label(cardEdit, SWT.NONE);
        labelQty.setText(CARDQTY_FIELD);
        Label labelAdd = new Label(cardEdit, SWT.NONE);
        labelAdd.setText(CARDADD);
        
        textCardName = new Text(cardEdit, SWT.BORDER);
        textCardDesc = new Text(cardEdit, SWT.BORDER);
        textCardDesc.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_HORIZONTAL));
        textCardPower = new Spinner(cardEdit, SWT.BORDER | SWT.SEARCH);
        textCardQty = new Spinner(cardEdit, SWT.BORDER | SWT.SEARCH);
        textCardQty.setMinimum(1);
        addCard = new Button(cardEdit, SWT.NONE);
        addCard.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        addCard.setText(ADD);
        // End of card edit block
        
        // Card display block
        GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1);
        tableCards = new Table(cardEdit, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
        tableCards.setLayoutData(gd_table);
        String[] titles = { NAMECOLUMN, DESCCOLUMN, CARDSTR_FIELD, CARDQTY_FIELD };
        tableCards.setLinesVisible(true);
        tableCards.setHeaderVisible(true);
        for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
            TableColumn column = new TableColumn(tableCards, SWT.NULL);
            column.setText(titles[loopIndex]);
        }
        
        for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
        	tableCards.getColumn(loopIndex).pack();
        }
        
        Composite subComposite = new Composite(compositeRight, SWT.NONE);
        subComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 5, 1));
        subComposite.setLayout(new GridLayout(3, false));
        
        addOneCard = new Button(subComposite, SWT.NONE);
        addOneCard.setText(ADDONECARD);
        removeOneCard = new Button(subComposite, SWT.NONE);
        removeOneCard.setText(REMOVEONECARD);
        deleteAllCards = new Button(subComposite, SWT.NONE);
        deleteAllCards.setText(DELETESELECTION);
        // End of card display block
        
	}
	
	private void switchStates(boolean editable) {
		textName.setEnabled(editable);
		textHP.setEnabled(editable);
		deleteCharacter.setEnabled(editable);
		textCardName.setEnabled(editable);
		textCardDesc.setEnabled(editable);
		textCardPower.setEnabled(editable);
		textCardQty.setEnabled(editable);
		addCard.setEnabled(editable);
		addOneCard.setEnabled(editable);
		removeOneCard.setEnabled(editable);
		deleteAllCards.setEnabled(editable);
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
				item.setText(1, c.getDescriptor());
				item.setText(2, String.valueOf(c.getPower()));
				item.setText(3, String.valueOf(nb));
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
				if(!name.equals("")) {
					if(!dungeon.isPersonaExists(name)) {
						Persona persona = new Persona(name);
						dungeon.addPersona(persona);
						TableItem item = new TableItem(tablePersonas, SWT.NULL);
			            item.setData(persona);
						item.setText(persona.getName());
			            namePersonaField.setText("");
					}
					else {
						MessageDialog.openWarning(parent.getShell(), ERROR, CHARALREADYEXISTS);
					}
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
				Persona p = getSelectedPersona();
				tablePersonas.remove(indexOfPersona);
				dungeon.removePersona(p);
				textName.setText("");
				textHP.setSelection(0);
				switchStates(false);
			}
		});
    	
    	addCard.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Persona p = getSelectedPersona();
				Card c = new Card(textCardName.getText(), textCardDesc.getText(), Integer.parseInt(textCardPower.getText()));
				if(!p.isThisCardInDeck(c)) {
					for(int i = 0 ; i < Integer.parseInt(textCardQty.getText()) ; i++) {
						p.addCardInDeck(c);
					}
					TableItem item = new TableItem(tableCards, SWT.NONE);
					item.setData(c);
					item.setText(0, textCardName.getText());
					item.setText(1, textCardDesc.getText());
					item.setText(2, textCardPower.getText());
					item.setText(3, textCardQty.getText());
				}
				else {
					MessageDialog.openWarning(parent.getShell(), ERROR, CARDALREADYEXISTS);
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
	            columns[0].setWidth((int)(size.x*0.2));
	            columns[1].setWidth((int)(size.x*0.6));
	            columns[2].setWidth((int)(size.x*0.1));
	            columns[3].setWidth((int)(size.x*0.1));
	        }
	    });
    	
    	textName.addFocusListener(new FocusListener() {
    		
    		String oldName = "";
			
			@Override
			public void focusLost(FocusEvent e) {
				String name = textName.getText();
				int indexOfPersona = tablePersonas.getSelectionIndex();
				Persona p = getSelectedPersona();
				if(!dungeon.isPersonaExists(p, name)) {
					tablePersonas.getItem(indexOfPersona).setText(name);
					p.setName(name);
				}
				else {
					MessageDialog.openWarning(parent.getShell(), ERROR, CHARALREADYEXISTS + " " + CHARCANCEL);
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
				Persona p = getSelectedPersona();
				p.setHp(hp);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
    	
    	addOneCard.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Integer indexOfCard = tableCards.getSelectionIndex();
				if(indexOfCard != -1) {
					Persona p = getSelectedPersona();
					TableItem item = tableCards.getItem(indexOfCard);
					Card c = (Card) item.getData();
					p.addCardInDeck(c);
					item.setText(3, String.valueOf(Integer.parseInt(item.getText(3))+1));
				}
			}
		});
    	
    	removeOneCard.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Integer indexOfCard = tableCards.getSelectionIndex();
				if(indexOfCard != -1) {
					Persona p = getSelectedPersona();
					TableItem item = tableCards.getItem(indexOfCard);
					Integer numberOfCards = Integer.parseInt(item.getText(3));
					Card c = (Card) item.getData();
					if(numberOfCards > 1) {
						item.setText(3, String.valueOf(Integer.parseInt(item.getText(3))-1));
					}
					else {
						tableCards.remove(indexOfCard);
					}
					p.removeCardInDeck(c);
				}
			}
		});
    	
    	deleteAllCards.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Integer indexOfCard = tableCards.getSelectionIndex();
				if(indexOfCard != -1) {
					Persona p = getSelectedPersona();
					TableItem item = tableCards.getItem(indexOfCard);
					Integer numberOfCards = Integer.parseInt(item.getText(3));
					Card c = (Card) item.getData();
					tableCards.remove(indexOfCard);
					for(int i = 0 ; i < numberOfCards ; i++) {
						p.removeCardInDeck(c);
					}
				}
			}
		});
	}
	
	/**
	 * Methods for listeners : redundant calls.
	 */
	private Persona getSelectedPersona() {
		int indexOfPersona = tablePersonas.getSelectionIndex();
		Persona p = (Persona) tablePersonas.getItem(indexOfPersona).getData();
		return p;
	}
	
}