

import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.CEllipse;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.*;

import java.awt.geom.Point2D;

/**
 * @author Nicolas Roussel (roussel@lri.fr)
 * 
 */
public class EllipseTool extends CStateMachine {

	private CEllipse ell;
	private Point2D p1;

	public State start, draw;

	public EllipseTool() {
		this(BUTTON1, NOMODIFIER);
	}

	public EllipseTool(final int button, final int modifier) {
		start = new State() {
			Transition press = new Press(button, modifier, ">> draw") {
				public void action() {
					Canvas canvas = (Canvas) getEvent().getSource();
					p1 = getPoint();
					ell = canvas.newEllipse(p1, 1, 1);
				}
			};
		};
		draw = new State() {
			Transition draw = new Drag(button, modifier) {
				public void action() {
					ell.setDiagonal(p1, getPoint());
				}
			};
			Transition stop = new Release(button, modifier, ">> start") {
				public void action() {
					ell.setDiagonal(p1, getPoint());
					fireEvent(new ShapeCreatedEvent(EllipseTool.this, ell));
				}
			};
		};
	}

}
