

import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.*;

import java.awt.geom.Point2D;

/**
 * @author Nicolas Roussel (roussel@lri.fr)
 * 
 */
public class PathTool extends CStateMachine {

	private CPolyLine line;

	public State start, draw;

	public PathTool() {
		this(BUTTON1, NOMODIFIER);
	}

	public PathTool(final int button, final int modifier) {
		start = new State() {
			Transition press = new Press(button, modifier, ">> draw") {
				public void action() {
					Canvas canvas = (Canvas) getEvent().getSource();
					line = canvas.newPolyLine(getPoint());
					line.setFilled(false);
				}
			};
		};
		draw = new State() {
			Transition draw = new Drag(button, modifier) {
				public void action() {
					line.lineTo(getPoint());
				}
			};
			Transition stop = new Release(button, modifier, ">> start") {
				public void action() {
					line.lineTo(getPoint());
					fireEvent(new ShapeCreatedEvent(PathTool.this, line));
				}
			};
		};
	}

}
