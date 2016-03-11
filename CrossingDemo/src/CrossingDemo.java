import java.awt.*;
import java.awt.geom.Point2D;

import javax.swing.*;

import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.debug.StateMachineVisualization;
import fr.lri.swingstates.sm.JStateMachine;
import fr.lri.swingstates.sm.State ;
import fr.lri.swingstates.sm.StateMachine;
import fr.lri.swingstates.sm.StateMachineListener;
import fr.lri.swingstates.sm.Transition ;
import fr.lri.swingstates.sm.jtransitions.EnterOnJTag;
import fr.lri.swingstates.sm.jtransitions.LeaveOnJTag;
import fr.lri.swingstates.sm.transitions.Press ;
import fr.lri.swingstates.sm.transitions.Drag ;
import fr.lri.swingstates.sm.transitions.Release ;
import fr.lri.swingstates.canvas.Canvas ;
import fr.lri.swingstates.canvas.CStateMachine ;

// --------------------------------------------------------------------------------------

class CrossingTrace extends CStateMachine {

    // ...
	int x, y, xEnd, yEnd;
	Canvas canvas;
	private Point2D p1, p2;
	CPolyLine polyLine;

    CrossingTrace(Canvas c) {
	   // ...
		canvas = c;
		super.attachTo(c);
    }

    State waiting = new State() {
		  // ...
		Transition t1 = new Press(BUTTON1, "=>draw") {
			public void action() {
				p1 = getPoint();
				polyLine = canvas.newPolyLine(p1);
				polyLine.setFilled(false);
				canvas.setDrawable(false);
				polyLine.setOutlinePaint(Color.MAGENTA);
			}
		  };
	   } ;


	State draw = new State() {
		Transition t1 = new Drag() {
			public void action() {
				p2 = getPoint();
				polyLine.lineTo(p2);
			};
		};

		Transition t2 = new Release("=>waiting") {
			public void action() {
				polyLine.remove();
			}
		};
	};

    // ...
};

class CrossingTool extends JStateMachine {
	public CrossingTool(Container pane) {
		   // ...
			super.attachTo(pane);
	}
	
	State waitting = new State() {
		Transition t1 = new Press(BUTTON1,NOMODIFIER, "=>crossing") {
			public void action() {};
		};
		
	};
	
	State crossing = new State() {
		Transition t1 = new EnterOnJTag(AbstractButton.class.getName(), ">>clicking") {
			public void action() {};
		};
		
		Transition t2 = new Release("=>waitting") {
			public void action() {};
		};

	};
	
	State clicking = new State() {
		
		Transition t1 = new LeaveOnJTag(AbstractButton.class.getName(), "=> crossing") {
			public void action() {
				((AbstractButton)getComponent()).doClick();
			};
		};
	
		Transition t2 = new Release("=>waitting") {
			public void action() {};
		};
	};
}


// --------------------------------------------------------------------------------------


public class CrossingDemo extends JFrame {

    public CrossingDemo() {
	   	super("CrossingDemo") ;

		Container pane = getContentPane() ;
		pane.setSize(new Dimension(400,300)) ;
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

	   	Canvas canvas = new Canvas(getContentPane().getWidth(), getContentPane().getHeight()) ;
	   	new CrossingTrace(canvas) ;
		CStateMachine sm = new CrossingTrace(canvas);
		//MachineListener ml = new MachineListener();
		


		canvas.setOpaque(false);

		this.setGlassPane(canvas);
		this.getGlassPane().setVisible(true);

		//CHECCKBOX
		JCheckBox Java = new JCheckBox("Java");
		JCheckBox Swing = new JCheckBox("Swing");
		JCheckBox SwingStates = new JCheckBox("SwingStates");
		JCheckBox UPS = new JCheckBox("UPS");
		JButton Cool = new JButton("coll");
		JButton useful = new JButton("useful");

		pane.add(Java);
		pane.add(Swing);
		pane.add(SwingStates);
		pane.add(UPS);
		pane.add(Cool);
		pane.add(useful);
	   	//pane.add(canvas) ;

		//showStateMachine(sm);
		CrossingTool crossingTool = new CrossingTool(canvas);
		crossingTool.attachTo(this);
		showStateMachine(crossingTool);
		pack() ;
	   	setVisible(true) ;
    }

	public void showStateMachine(StateMachine sm) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new StateMachineVisualization(sm));
		frame.pack();
		frame.setVisible(true);
	}


    static public void main(String args[]) {
	   CrossingDemo demo = new CrossingDemo() ;
    }

}
