package e2b;

import java.util.Set;

/**
 * This class is a MVC "Model" of a percentage (a value such as 0 <= x <= 1)
 **/
public class Percentage_Controller {
	
	private final Percentage_Model myModel;
	private Set<Percentage_View> myViews = new java.util.HashSet<Percentage_View> ();

	/**
	 * Create a Percentage with an initial value
	 * @param initialValue : the initial value
	 * @throws IllegalArgumentException if value is not correct
	 **/
	public Percentage_Controller(Percentage_Model model) {
		myModel = model;
	}

	/**
	 * Change the value of this Percentage.
	 * Update the views
	 * @param newValue : the value
	 * @throws IllegalArgumentException if value is not correct
	 **/
	public void setValue(float newValue) {
		float oldValue = myModel.getValue();
		myModel.setValue(newValue);
		if (myModel.getValue() != oldValue) 
			// Percentage has changed, notify the Views.
			updateViews();
	}
	
	/**
	 * Get the value from the model
	 * @return the percentage value
	 */
	public float getValue() {
		return myModel.getValue(); // TODO return the value of the percentage from the model instead
	} 

	/**
	 * Add a new Listener to this model
	 * @param l     the new listener
	 **/
	public void addView(Percentage_View l) {
		// TODO add the views and don't forgot to update this view !
		myViews.add(l);
		updateViews();
	}

	/**
	 * Remove a Listener from this model
	 * @param l     the  listener to remove
	 **/
	public void removeView(Percentage_View l) {
		// TODO remove the view
		myViews.remove(l);
		updateViews();
	}

	/**
	 * Iterates on all listeners and
	 * send the StateChanged method to each
	 * @param e the event to dispatch
	 */
	protected void updateViews() {
		// TODO update the views
		for (Percentage_View e : myViews) {
			e.update();
		}
	}
}
