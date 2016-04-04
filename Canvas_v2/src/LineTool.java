

import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.CSegment;
import fr.lri.swingstates.canvas.transitions.EnterOnTag;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.*;

import java.awt.BasicStroke;
import java.awt.geom.Point2D;


/**
 * @author Nicolas Roussel (roussel@lri.fr)
 * 
 */
public class LineTool extends CStateMachine {

	private CSegment seg;
	private Point2D p1;

	public State start, draw, crossing;

	public LineTool() {
		this(BUTTON1, NOMODIFIER);
	}

	public LineTool(final int button, final int modifier) {
		start = new State() {
			Transition press = new Press(button, modifier, ">> draw") {
				public void action() {
					Canvas canvas = (Canvas) getEvent().getSource();
					p1 = getPoint();
					seg = canvas.newSegment(p1, p1);
				}
			};
		};
		crossing = new State(){
			Transition select = new Enter( BUTTON1){
				public void action(){
					System.out.println("enter line tool");
				}
			};
		};
		draw = new State() {
			Transition draw = new Drag(button, modifier) {
				public void action() {
					seg.setPoints(p1, getPoint());
				}
			};
			Transition stop = new Release(button, modifier, ">> start") {
				public void action() {
					seg.setPoints(p1, getPoint());
					fireEvent(new ShapeCreatedEvent(LineTool.this, seg));
				}
			};
		};
	}

}
