 
package viewparts;

import javax.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import data.Dungeon;
import model.ModelProvider;

public class GameLauncherViewPart {
	
	private Font font;
	private Composite parent;
	private Dungeon dungeon;
	
	@Inject
	EModelService modelService;
	@Inject
	MApplication app;
	
	private static final String LAUNCHGAME = "Construire votre jeu";
	private static final String FONTNAME = "BLKCHCRY.TTF";
	
	
	@Inject
	public GameLauncherViewPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		this.parent = parent;
		this.dungeon = ModelProvider.INSTANCE.getDungeon(); //Dungeon.getInstance();
		this.loadFont(FONTNAME);
		
		Button b = new Button(parent, SWT.WRAP);	
		b.setText(LAUNCHGAME);
		b.setFont(font);
		FontData[] fd = b.getFont().getFontData();
		fd[0].setHeight(36);
		b.setFont(new Font(parent.getDisplay(), fd[0]));
		b.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if(dungeon.isValid()) {
					parent.getShell().dispose();
					
					MUIElement window = modelService.find("dungeonfactory.trimmedwindow.gamewindow", app);
	
					window.setToBeRendered(true);
				}
				else {
					MessageDialog.openError(parent.getShell(), "Erreur", "Le donjon n'est pas valide. Vérifiez qu'il y ait au moins 1 personnage avec au moins une carte, qu'il y ait une salle finale, et que chaque salle soit reliée avec une autre.");
				}
				//new Game(Display.getCurrent());
			}
		});
	}
	
	private void loadFont(String name) {
		//if(parent.getShell().getDisplay().loadFont("/font/BLKCHCRY.TTF"))
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		String path = "/font/" + name; 
		URL url = FileLocator.find(bundle, new Path(path), null);		
		URL fileUrl = null;
		try {
			fileUrl = FileLocator.toFileURL(url);
		}
		catch (IOException e) {
		// Will happen if the file cannot be read for some reason
			e.printStackTrace();
		}
		File file = new File(fileUrl.getPath());
		Display.getCurrent().loadFont(file.toString());
		font = new Font(parent.getShell().getDisplay(), "BlackChancery", 12, SWT.NONE);
	}
	
}