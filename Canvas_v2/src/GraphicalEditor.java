
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JFrame;

import PathTool.*;
import EraserTool.*;
import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CHierarchyTag;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.transitions.EnterOnTag;
import fr.lri.swingstates.canvas.transitions.PressOnTag;
import fr.lri.swingstates.debug.StateMachineVisualization;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.StateMachine;
import fr.lri.swingstates.sm.StateMachineListener;
import fr.lri.swingstates.sm.Transition;
;



public class GraphicalEditor extends JFrame {
	
	private Canvas canvas;
	private CShape palette;

	int iconsize = 48;
	private ArrayList<CStateMachine> tools;
	

	private SelectionTool selector;
	
	class ToolTag extends CExtensionalTag {
		private CStateMachine tool;

		ToolTag(CStateMachine csm) {
			tool = csm;
		}

		CStateMachine getTool() {
			return tool;
		}
	}
	
	private StateMachineListener smlistener = new StateMachineListener() {
		public void eventOccured(EventObject e) {
			ShapeCreatedEvent csce = (ShapeCreatedEvent) e;
			csce.getShape().addTag(selector.getBaseTag())
					.setFillPaint(Color.white);
			new CHierarchyTag(palette).aboveAll();
		}
	};
	
	public GraphicalEditor(String title, int width, int height){
		super(title);
		//create a canvas 
		canvas = new Canvas(width, height);
		//get the container of the frame
		Container pane = getContentPane();
		//add some elements in the frame
        pane.add(canvas);
        
		tools = new ArrayList<CStateMachine>();
		selector = new SelectionTool();
		tools.add(selector);
		tools.add(new PathTool());
		tools.add(new LineTool());
		tools.add(new EllipseTool());
		tools.add(new EraserTool());


		//palette = canvas.newRectangle(300, 20, 20, iconsize*3);
		palette = canvas.newRectangle(20, 5, tools.size() * iconsize, 15);

		CText ptitle = canvas.newText(0, 0, "TOOLS", new Font("verdana",
				Font.PLAIN, 12));
		ptitle.setParent(palette);
		ptitle.setReferencePoint(0.5, 0.5).translateTo(palette.getCenterX(),
				palette.getCenterY());
		CStateMachine cm = new CStateMachine() {
			State start = new State() {
				Transition changeTool = new PressOnTag(ToolTag.class, BUTTON1,
						NOMODIFIER) {
					public void action() {
						ToolTag tt = (ToolTag) getTag();
						canvas.getTag(ToolTag.class).setStroke(
								new BasicStroke(1));
						tt.setStroke(new BasicStroke(4));
						tt.aboveAll();
						
						CStateMachine theTool = tt.getTool();
						for (CStateMachine aTool : tools){
							aTool.setActive(aTool == theTool);
						}
						consumes(true);
					}
				};
				Transition enterTag = new EnterOnTag(ToolTag.class, BUTTON1){
					public void action(){
						ToolTag tt = (ToolTag) getTag();
						canvas.getTag(ToolTag.class).setStroke(
								new BasicStroke(1));
						tt.setStroke(new BasicStroke(4));
						tt.aboveAll();
					}
				};
			};
		};
		cm.attachTo(canvas);
		showStateMachine(cm);

		
		for (int i = 0; i < tools.size(); i++) {
			CStateMachine tool = tools.get(i);
			tool.addStateMachineListener(smlistener);
			tool.attachTo(canvas);
			if (tool != selector)
				tool.setActive(false);

			CShape s = canvas.newImage(20 + i * iconsize, 20, "resources/"
					+ tool.getClass().getName() + ".png");
			s.setParent(palette);
			s.addTag(new ToolTag(tool));
		}
        //set the frame visible
		pack();
		setVisible(true);
	}
	
	public static void showStateMachine(StateMachine sm) {
		JFrame viz = new JFrame();
		viz.getContentPane().add(new StateMachineVisualization(sm));
		viz.pack();
		viz.setVisible(true);
	}
	
	public static void main(String[] args) {
		GraphicalEditor editor = new GraphicalEditor("Canvas", 400,
				600);
		// editor.populate() ;
		editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
