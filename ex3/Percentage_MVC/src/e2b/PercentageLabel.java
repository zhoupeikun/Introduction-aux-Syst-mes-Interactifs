package e2b;

import javax.sound.midi.ControllerEventListener;
import javax.swing.JLabel;

/**
 * A MVC View of a Percentage as a label.
 * This is not a MVC Controller, so it does not allow to change the Percentage value
 * This class implements PercentageListener, to be informed of changes in the Percentage
 * This class does not need a reference to the model, since all needed information is contained
 * in the PercentageEvent.
 */
public class PercentageLabel extends JLabel implements Percentage_View {
	
	private static final long serialVersionUID = 1L;
	
	private final Percentage_Controller myController;
	
	public PercentageLabel(Percentage_Controller controller) {
		myController = controller;
	}
	
	/**
	 * Called by the Model (Percentage) when a change occurs
	 */
	public void update() {
		// TODO update the JLabel with the value of the percentage
		this.setText(String.valueOf(myController.getValue()));
	}
}
