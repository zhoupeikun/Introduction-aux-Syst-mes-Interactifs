import java.awt.geom.Point2D;

import fr.lri.swingstates.canvas.*;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Drag;
import fr.lri.swingstates.sm.transitions.Press;
import fr.lri.swingstates.sm.transitions.Release;

public class EraserTool extends CStateMachine{
	
	private CRectangle rect;
	private Point2D p1;

	public State start, draw;

	class ToolTag extends CExtensionalTag {
		private CStateMachine tool;

		ToolTag(CStateMachine csm) {
			tool = csm;
		}

		CStateMachine getTool() {
			return tool;
		}
	}

	public EraserTool() {
		this(BUTTON1, NOMODIFIER);
	}

	public EraserTool(final int button, final int modifier) {
		start = new State() {
			Transition press = new Press(button, modifier, ">> draw") {
				public void action() {
					Canvas canvas = (Canvas) getEvent().getSource();
					p1 = getPoint();
					rect = canvas.newRectangle(p1, 1, 1);
				}
			};
			Transition release = new Release(button, NOMODIFIER){
				public void action(){
				}
			};
		};
		draw = new State() {
			Transition draw = new Drag(button, modifier) {
				public void action() {
					rect.setDiagonal(p1, getPoint());
				}
			};
			Transition stop = new Release(button, modifier, ">> start") {
				public void action() {
					rect.setDiagonal(p1, getPoint());
					fireEvent(new ShapeCreatedEvent(EraserTool.this, rect));
				}
			};
		};
	}

}
