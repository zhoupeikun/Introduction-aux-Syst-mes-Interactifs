package e2b;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;

/**
 * A PercentagePieChart acts as a MVC View of a Percentage It maintains a
 * reference to its model in order to repaint itself.
 **/
public class PercentagePieChart extends JComponent implements Percentage_View {

	private static final long serialVersionUID = 1L;
	/**
	 * Hold a reference to the model
	 */
	private final Percentage_Controller myController;
	private boolean isPressedOnPin;

	public PercentagePieChart(Percentage_Controller controller) {
		super();
		myController = controller;
		isPressedOnPin = false;

		// For "controller" behaviour
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
				if (inPin(e)) {
					isPressedOnPin = true;
				}
			}

			public void mouseReleased(MouseEvent e) {
				isPressedOnPin = false;
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

		});
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				if (isPressedOnPin) {
					myController.setValue(pointToPercentage(e));
				}
			}

			public void mouseMoved(MouseEvent e) {
			}

		});
	}

	// "View" behaviour : when the percentage changes, the piechart must be
	// repainted
	public void update() {
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int centerX = this.getWidth() / 2;
		int centerY = this.getHeight() / 2;
		int radius = Math.min(getWidth() - 4, getHeight() - 4) / 2;
		double angle = myController.getValue() * 2 * Math.PI;

		g.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
		g.setColor(Color.yellow);
		g.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2,
				0, (int) Math.toDegrees(angle));

		int pinX = centerX + (int) (Math.cos(angle) * radius);
		int pinY = centerY - (int) (Math.sin(angle) * radius);
		g.setColor(Color.gray.brighter());
		g.fill3DRect(pinX - 4, pinY - 4, 8, 8, true);
	}

	// for "controller" behaviour
	/**
	 * Test if a mouse event is inside the "Pin" that allows to change the
	 * percentage
	 */
	private boolean inPin(MouseEvent ev) {
		int mouseX = ev.getX();
		int mouseY = ev.getY();
		int centerX = this.getWidth() / 2;
		int centerY = this.getHeight() / 2;
		int radius = Math.min(getWidth() - 4, getHeight() - 4) / 2;
		double angle = myController.getValue() * 2 * Math.PI;
		int pinX = centerX + (int) (Math.cos(angle) * radius);
		int pinY = centerY - (int) (Math.sin(angle) * radius);

		Rectangle r = new Rectangle();
		r.setBounds(pinX - 4, pinY - 4, 8, 8);
		return r.contains(mouseX, mouseY);
	}

	// Converts a mouse position to a Percentage value
	private float pointToPercentage(MouseEvent e) {
		int centerX = this.getWidth() / 2;
		int centerY = this.getHeight() / 2;
		int mouseX = e.getX() - centerX;
		int mouseY = e.getY() - centerY;
		double l = Math.sqrt(mouseX * mouseX + mouseY * mouseY);
		double lx = mouseX / l;
		double ly = mouseY / l;
		double theta;
		if (lx > 0) {
			theta = Math.atan(ly / lx);
		} else if (lx < 0) {
			theta = -1 * Math.atan(ly / lx);
		} else {
			theta = 0;
		}

		if ((mouseX > 0) && (mouseY < 0)) {
			theta = -1 * theta;
		} else if (mouseX < 0) {
			theta += Math.PI;
		} else {
			theta = 2 * Math.PI - theta;
		}

		return (float) (theta / (2 * Math.PI));
	}
}
