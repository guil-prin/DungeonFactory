 
package viewparts;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import game.Game;

public class GameViewPart {
	@Inject
	public GameViewPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		Button b = new Button(parent, SWT.BORDER);	
		b.setText("Lancer le jeu");
		b.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				parent.getShell().dispose();
				new Game(Display.getCurrent());
			}
		});
	}
	
}