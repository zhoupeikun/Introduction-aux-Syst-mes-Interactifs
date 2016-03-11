package e2b;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A PercentagePieSlider acts boths as a MVC View and Controller of a Percentage
 * It maintains a reference to its model in order to update it.
 */
public class PercentageSlider extends JSlider implements Percentage_View {

	private static final long serialVersionUID = 1L;
	
	private final Percentage_Controller myController;

	public PercentageSlider(Percentage_Controller controller) {
		myController = controller;
		setMinimum(0);
		setMaximum(100);
		this.setMinorTickSpacing(5);
		this.setMajorTickSpacing(10);
		this.setPaintTicks(true);
		this.setPaintLabels(true);
		this.setSnapToTicks(true);

		// "Controller" behaviour : when the value of the slider changes,
		// The model must be updated
		addChangeListener(
			new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					//TODO set the value of percentage with the new value of the slider
					JSlider source = (JSlider) e.getSource();
					myController.setValue((float)source.getValue()/100);
					myController.updateViews();
				}
			});

	}

	// "View" behaviour : when the percentage changes, the slider must be updated
	public void update() {
		// TODO update the value of the slider
		this.setValue((int)(myController.getValue()*100));
	}
}
