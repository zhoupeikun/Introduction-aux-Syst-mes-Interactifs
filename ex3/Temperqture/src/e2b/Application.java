package e2b;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Application extends JFrame {

	private static final long serialVersionUID = 1L;

	// The "Model" of a Percentage
	Percentage_Model myModel;
	// The "Controller" of a Percentage
	Percentage_Controller myController;
	
	// A Views
	PercentageLabel myTextView;
	PercentagePieChart myPieViewAndController;
	PercentageSlider mySliderViewAndController;
	// ConsoleView myConsoleView; TODO uncomment this

	
	/** Construct the application */
	public Application() {
		super("Model - View - Controller example");
		
		// Create the model and its controller
		myModel = new Percentage_Model(0.33f);
		myController = new Percentage_Controller(myModel);
		
		// Create the views
		myTextView = new PercentageLabel(myController);
		// TODO create other views
		myPieViewAndController =  new PercentagePieChart(myController);
		mySliderViewAndController = new PercentageSlider(myController);

		// Connect the views to the controller
		myController.addView(myTextView);
		// TODO connect other views
		myController.addView(myPieViewAndController);
		myController.addView(mySliderViewAndController);
		
		// Initialize the GUI
		initUI();
		
		// Resize window and make it visible
		pack();
		setVisible(true);
		// Close the application when the user closes the window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	private void initUI() {
		JLabel jLabel1 = new JLabel("Percentage:");
		
		myTextView.setEnabled(false);
		myPieViewAndController.setPreferredSize(new Dimension(90, 90));
		
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new FlowLayout());
		northPanel.add(jLabel1, null);
		northPanel.add(myTextView, null);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(northPanel, BorderLayout.NORTH);
		// TODO add the others views to the GUI
		mainPanel.add(myPieViewAndController, BorderLayout.CENTER);
		mainPanel.add(mySliderViewAndController, BorderLayout.SOUTH);
		
		getContentPane().add(mainPanel);
	}

	
	/** Main method */
	public static void main(String[] args) {
		new Application();
	}
}
