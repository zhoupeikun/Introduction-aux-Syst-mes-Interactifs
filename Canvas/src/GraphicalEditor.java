
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.LinkedList;

import javax.swing.JColorChooser;
import javax.swing.JFrame;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CHierarchyTag;
import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.transitions.DragOnTag;
import fr.lri.swingstates.canvas.transitions.PressOnTag;
import fr.lri.swingstates.debug.StateMachineVisualization;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.StateMachine;
import fr.lri.swingstates.sm.StateMachineListener;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Release;
import fr.lri.swingstates.canvas.transitions.ReleaseOnTag;;



public class GraphicalEditor extends JFrame {
	
	private Canvas canvas;
	private CShape palette;
	private CShape pathpalette1;
	private CShape pathpalette2;
	private CShape eraserpalette1, eraserpalette2;
	int iconsize = 50;
	private ArrayList<CStateMachine> tools;
	private ArrayList<CStateMachine> pathtools;
	private ArrayList<CStateMachine> erasertools;
	
	private boolean menuCreated = false;

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

		//add eraser tool
		tools.add(new EraserTool());
		
		//create the tool bar for pathtool
		pathtools = new ArrayList<CStateMachine>();
		pathtools.add(new SelectionTool());
		pathtools.add(new LineTool());
		pathtools.add(new LineTool());
		pathtools.add(new LineTool());
		pathtools.add(new LineTool());
		pathtools.add(new LineTool());

		//create the tool bar for erasertool
		erasertools = new ArrayList<CStateMachine>();
		erasertools.add(new EraserTool());
		erasertools.add(new EraserTool());
		erasertools.add(new EraserTool());
		erasertools.add(new EraserTool());
		erasertools.add(new EraserTool());
		erasertools.add(new EraserTool());

		//palette = canvas.newRectangle(300, 20, 20, iconsize*3);
		palette = canvas.newRectangle(20, 5, tools.size() * iconsize, 15);

		CText ptitle = canvas.newText(0, 0, "TOOLS", new Font("verdana",
				Font.PLAIN, 12));
		ptitle.setParent(palette);
		ptitle.setReferencePoint(0.5, 0.5).translateTo(palette.getCenterX(),
				palette.getCenterY());
		new CStateMachine() {
			State start = new State() {
				Transition changeTool = new PressOnTag(ToolTag.class, BUTTON1,
						"=> select") {
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
			};
			State select = new State(){
				Transition drag = new DragOnTag(ToolTag.class,BUTTON1){
					public void action(){
						if(!menuCreated){
							ToolTag tt = (ToolTag) getTag();
							if(tt.getTool().getClass().getName() == "PathTool"){
								pathpalette1 = canvas.newRectangle(20, 70, iconsize*3, 50);
								pathpalette2 = canvas.newRectangle(20, 120, iconsize*3, 50);
								for (int i = 0; i < pathtools.size(); i++) {
									CStateMachine tool = pathtools.get(i);
									tool.addStateMachineListener(smlistener);
									tool.attachTo(canvas);
									tool.setActive(false);
									CShape s;
	                                if(i<3){
	                                	 s = canvas.newImage(20 + i * iconsize, 70, "resources/"
											+ tool.getClass().getName() + ".png");
	                                }
	                                else{
	                                	 s = canvas.newImage(20 + (i-3)* iconsize, 120, "resources/"
												+ tool.getClass().getName() + ".png");
	                                }
									if(i<=2)
										s.setParent(pathpalette1);
									else
										s.setParent(pathpalette2);
									s.addTag(new ToolTag(tool));
								}
								CPolyLine line = canvas.newPolyLine(getPoint());
								line.setFilled(false);
							}
							if (tt.getTool().getClass().getName() == "EraserTool") {
								eraserpalette1 = canvas.newRectangle(20, 70, iconsize*3, 50);
								eraserpalette2 = canvas.newRectangle(20, 120, iconsize*3, 50);
								for (int i = 0; i < erasertools.size(); i++) {
									CStateMachine tool = erasertools.get(i);
									tool.addStateMachineListener(smlistener);
									tool.attachTo(canvas);
									tool.setActive(false);
									CShape s;
									if(i<3){
										s = canvas.newImage(20 + i * iconsize, 70, "resources/"
												+ tool.getClass().getName() + ".png");
									}
									else{
										s = canvas.newImage(20 + (i-3)* iconsize, 120, "resources/"
												+ tool.getClass().getName() + ".png");
									}
									if(i<=2)
										s.setParent(eraserpalette1);
									else
										s.setParent(eraserpalette2);
									s.addTag(new ToolTag(tool));
								}
							}
							menuCreated = true;
						}
					}
				};

				// TODO: 4/2/16 release to remove shape.
				Transition release = new Release("=>start"){
					public void action(){
						
						LinkedList l1 =pathpalette1.getChildren();
						LinkedList l2 =pathpalette2.getChildren();

						int n1 = pathpalette1.getChildrenCount();
						int n2 = pathpalette2.getChildrenCount();

						while(n1>0){
							canvas.removeShape((CShape)l1.get(n1-1));
							canvas.removeShape((CShape)l2.get(n1-1));
							n1--;
						}
						canvas.removeShape(pathpalette1);
						canvas.removeShape(pathpalette2);
						canvas.removeShape(eraserpalette1);
						canvas.removeShape(eraserpalette2);

						menuCreated = false;
						//canvas.removeAllShapes();
					}
					
				};
			};
			
		}.attachTo(canvas);
		
		for (int i = 0; i < tools.size(); i++) {
			CStateMachine tool = tools.get(i);
			//tool.addStateMachineListener(smlistener);
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
