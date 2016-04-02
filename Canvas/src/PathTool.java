import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.debug.StateMachineVisualization;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.StateMachine;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JFrame;

public class PathTool extends CStateMachine {

	private CPolyLine line;
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
	
	public PathTool() {
		this(BUTTON1, NOMODIFIER);
	}

	public PathTool(final int button, final int modifier) {
		start = new State() {
			Transition press = new Press(button, modifier,"=>draw") {
				public void action() {
					Canvas canvas = (Canvas) getEvent().getSource();
					line = canvas.newPolyLine(getPoint());
					line.setFilled(false);
					
				}
			};
			Transition release = new Release(button, NOMODIFIER){
				public void action(){
					line.lineTo(getPoint());
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
		showStateMachine(this);
	}
	public static void showStateMachine(StateMachine sm) {
		JFrame viz = new JFrame();
		viz.getContentPane().add(new StateMachineVisualization(sm));
		viz.pack();
		viz.setVisible(true);
	}

}
