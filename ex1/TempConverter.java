package e1;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TempConverter extends JFrame {
dx
	private JLabel labelC; // Name of the Celsius field
	private JLabel labelF; // Name of the Fahrenheit field
	private JTextField textFieldC; // Celsius field
	private JTextField textFieldF; // Fahrenheit field
	private JButton buttonReset; // Button for reseting the fields
	private JButton buttonClose; // Button for closing the window
	private JFrame mainFrame;

	/*
	 * Listener of the Celsius field: convert the data from Clesius to
	 * Fahrenheit when the "enter" keyboard button is hit (in the Clesius field)
	 */
	private ActionListener textFieldCListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String value = textFieldC.getText();
			try {
				float valC = new Float(value);
				float valF = valC * 1.8f + 32;
				textFieldF.setText(Float.toString(valF));
			} catch (Exception exp) {
				textFieldF.setText("");
				textFieldC.setText("");
			}
		}
	};

	/*
	 * Listener of the Fahrenheit field: Convert the data from Fahrenheit to
	 * Celsius when the "enter" keyboard button is hit (in the Fahrenheit field)
	 */
	// private ActionListener textFieldFListener = new ActionListener() {
	// ...
	// };

	/*
	 * Listener of the Reset button: Reset the value in the text field when the
	 * Reset button is pressed
	 */
	// private ActionListener buttonResetListener = new ActionListener() {
	// ...
	// };

	/*
	 * Listener of the Close button: Close the program window when the Close
	 * button is pressed
	 */
	private ActionListener buttonCloseListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	};

	/**
	 * Constructor of the temperature converter
	 * 
	 * @param title
	 *            title of the window
	 */
	public TempConverter(String title) {
		super(title);
		init();
	}

	/**
	 * Initialize the converter window
	 */
	public void init() {
		// ...

		mainFrame = new JFrame("Temoerature converter");
		mainFrame.setSize(400, 200);

		labelC = new JLabel("Celsius", JLabel.LEFT);
		textFieldC = new JTextField(6);
		JPanel panelC = new JPanel();
		panelC.setLayout(new BoxLayout(panelC, BoxLayout.Y_AXIS));
		panelC.add(labelC);
		panelC.add(textFieldC);


		labelF = new JLabel("Fahrenheit", JLabel.RIGHT);
		textFieldF = new JTextField(6);
		JPanel panelF = new JPanel();
		panelF.setLayout(new BoxLayout(panelF, BoxLayout.Y_AXIS));
		panelF.add(labelF);
		panelF.add(textFieldF);
		textFieldF.addActionListener(textFieldCListener);


		JPanel uppanel = new JPanel();
		uppanel.setLayout(new FlowLayout(1));
		uppanel.add(panelC);
		uppanel.add(panelF);

		buttonReset = new JButton("Reset");
		buttonClose = new JButton("Close");
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.X_AXIS));
		panelButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
		panelButton.add(Box.createHorizontalGlue());
		panelButton.add(buttonReset);
		panelButton.add(Box.createRigidArea(new Dimension(10, 0)));
		panelButton.add(buttonClose);


		// buttonReset.addActionListener(textFieldCListener);
		buttonClose.addActionListener(buttonCloseListener);
		
		Container contain = getContentPane();
		
		contain.add(uppanel, BorderLayout.CENTER);
		contain.add(panelButton, BorderLayout.SOUTH);

		/*
		 * TempConverter tempConverter = new TempConverter(); mainFrame = new
		 * JFrame("Temperature converter"); mainFrame.setSize(400, 200);
		 * mainFrame.setLayout(new GridLayout(3,1));
		 * mainFrame.addWindowListener(new WindowAdapter() { public void
		 * windowCLosing(WindowEvent windowEvent) { System.exit(0);} }); JLabel
		 * Celsius = new JLabel("Celsius", JLabel.RIGHT); JLabel Fahrenheit =
		 * new JLabel("Fahrenheit", JLabel.CENTER); final JTextField celsiusText
		 * = new JTextField(); final JTextField fahrenheit = new JTextField();
		 * 
		 * JButton resetButton = new JButton("Reset"); JButton closeButton = new
		 * JButton("Close");
		 * resetButton.addActionListener(textFieldClistener());
		 * 
		 * 
		 * mainFrame.add(Celsius); mainFrame.a
		 */

		// Resize window and make it visible
		pack();
		setVisible(true);
		// Close the application when the user closes the window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new TempConverter("Temperature converter");
	}

}
