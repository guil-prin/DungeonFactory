 
package viewparts;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class CharacterCreatorViewPart {
	
	private Composite parent;
	
	@Inject
	public CharacterCreatorViewPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		this.parent = parent; 
		buildUI();
	}
	
	private void buildUI() {
		parent.setLayout(new GridLayout(2, false));
		this.createLeftColumn();
		this.createLeftColumn();
	}
	
	private void createLeftColumn() {
		Composite compositeLeft = new Composite(parent, SWT.NONE);
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
        label.setText("yolo");
	}
	
	
}