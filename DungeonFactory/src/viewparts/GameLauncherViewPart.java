 
package viewparts;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class GameLauncherViewPart {
	
	@Inject
	EModelService modelService;
	@Inject
	MApplication app;
	
	public static final String LAUNCHGAME = "CONSTRUIRE VOTRE JEU";
	
	@Inject
	public GameLauncherViewPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		Button b = new Button(parent, SWT.WRAP);	
		b.setText(LAUNCHGAME);
		FontData[] fd = b.getFont().getFontData();
		fd[0].setHeight(72);
		b.setFont(new Font(parent.getDisplay(), fd[0]));
		b.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				parent.getShell().dispose();
				
				MUIElement window = modelService.find("dungeonfactory.trimmedwindow.gamewindow", app);

				window.setToBeRendered(true);
				
				//new Game(Display.getCurrent());
			}
		});
	}
	
}