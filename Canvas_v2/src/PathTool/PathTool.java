package PathTool;

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
import fr.lri.swingstates.canvas.transitions.DragOnTag;


import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.EventObject ;

import javax.swing.*;


/**
 * @author Honglin
 * 
 */
public class  PathTool extends CStateMachine {

	private CPolyLine line;
	public State start, crossing, draw;
	private boolean drawing = false;
	private boolean cross = false;
	int iconsize = 48;
	
	private int size_pen = 1;
	private String color;
	
	final static int minsize = 1;
	final static int midsize = 20;
	final static int maxsize = 30;
	
	private ArrayList<PenTool> pathTools;
	private boolean menuCreated = false;
	private CShape pathPalette1;
	private CShape pathPallette2;

	class PenTag extends CExtensionalTag {
		private String type;
		private PenTool penTool;

		PenTag(PenTool pt){
			this.type = pt.getType();
			this.penTool = pt;
		}
		String getType() {
			return this.type;
		}
		PenTool getPenTool(){
			return this.penTool;
		}
	
	}

	
	public PathTool() {
		this(BUTTON1, NOMODIFIER);
		//create the tool bar for pathtool
		pathTools = new ArrayList<PenTool>();
		
		pathTools.add(new ColorTool("red"));
		pathTools.add(new ColorTool("green"));
		pathTools.add(new ColorTool("blue"));
		pathTools.add(new SizeTool(1));
		pathTools.add(new SizeTool(4));
		pathTools.add(new SizeTool(10));
		
	}

	public PathTool(final int button, final int modifier) {
		start = new State() {
			Transition drag = new Drag(button,"=>crossing") {
				public boolean guard(){
					cross = true;
					return cross;
				}
				public void action() {

					Canvas canvas = (Canvas) getEvent().getSource();
				
					if(!menuCreated){
							pathPalette1 = canvas.newRectangle(20, 70, iconsize*3, 50);
							pathPallette2 = canvas.newRectangle(20, 120, iconsize*3, 50);
							for (int i = 0; i < pathTools.size(); i++) {
								PenTool tool = pathTools.get(i);
								CShape s;
                                if(i<3){
                                	 s = canvas.newImage(20 + i * iconsize, 70, "resources/"
										+ tool.getClass().getName() + i + ".png");
                                }
                                else{
                                	 s = canvas.newImage(20 + (i-3)* iconsize, 120, "resources/"
											+ tool.getClass().getName() + i + ".png");
                                }
								if(i<=2)
									s.setParent(pathPalette1);
								else
									s.setParent(pathPallette2);
								s.addTag(new PenTag(tool));
							}
						menuCreated = true;
						
					}
					canvas.setCursor(Cursor.getDefaultCursor());
				}
			};
			Transition press =  new Press(button, modifier,"=>draw"){
				
				public void action(){
					Canvas canvas = (Canvas) getEvent().getSource();
					line = canvas.newPolyLine(getPoint());
					line.setFilled(false);
					line.setStroke(new BasicStroke(getPenSize()));
					line.setOutlinePaint(getPenColor());
				}
			};
			
			/*Transition release = new Release(button, modifier){
				public void action(){
					line.lineTo(getPoint());
				}
			}; */
		};
		crossing = new State(){
			
			Transition enter = new DragOnTag(PenTag.class, button){
				
				public void action(){
					Canvas canvas = (Canvas) getEvent().getSource();
					PenTag tt = (PenTag) getTag();
					/*canvas.getTag(ToolTag.class).setStroke(
							new BasicStroke(1));*/
					LinkedList<CShape> groupColor = pathPalette1.getChildren();
					LinkedList<CShape> groupSize = pathPallette2.getChildren();

					String type = tt.getType();
					if(type =="size"){
						setPenSize((SizeTool)tt.getPenTool());
						System.out.println("pensize="+getPenSize());
						for(CShape c: groupSize){
							c.setStroke(new BasicStroke(1));
						}
					}
					else{
						setPenColor((ColorTool)tt.getPenTool());
						System.out.println("pencolor=" + getPenColor());
						for(CShape c: groupColor){
							c.setStroke(new BasicStroke(1));
						}
					}
					tt.setStroke(new BasicStroke(4));

					canvas.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
							new ImageIcon("resources/Pentool.png").getImage(),
							new Point(canvas.getX()+5, canvas.getY()+45),""));
				}
			};
			
			Transition release = new Release(button, "=>start"){
				public void action(){
					Canvas canvas = (Canvas) getEvent().getSource();
					LinkedList l1 = pathPalette1.getChildren();
					LinkedList l2 = pathPallette2.getChildren();
					int n = pathPalette1.getChildrenCount();
					while(n>0){
						canvas.removeShape((CShape)l1.get(n-1));
						canvas.removeShape((CShape)l2.get(n-1));
						n--;
					}
					canvas.removeShape(pathPalette1);
					canvas.removeShape(pathPallette2);
					menuCreated = false;
				}
			};
		};
		draw = new State() {
			Transition draw = new Drag(button, modifier) {
				public void action() {
					line.lineTo(getPoint());
				}
			};

			Transition stop = new Release(button, modifier,"=>start") {
				public void action() {
					line.lineTo(getPoint());
					//fireEvent(new ShapeCreatedEvent(PathTool.this, line));
				}
			};
		};
		showStateMachine(this);
	}
	
	public void setPenSize(SizeTool tt){
		this.size_pen = tt.getSize();
	}
	public int getPenSize(){
		return this.size_pen;
	}
	public void setPenColor(ColorTool cc){
		this.color = cc.getColor();
	}
	public Color getPenColor(){
		switch(this.color){
		 case "red": return Color.RED;
		 case "green": return Color.GREEN;
		 case "blue": return Color.BLUE;
		 default: 
			 System.out.println("colorrrrrrr");
			 return Color.BLACK;
		}
	}
	
	public static void showStateMachine(StateMachine sm) {
		JFrame viz = new JFrame();
		viz.getContentPane().add(new StateMachineVisualization(sm));
		viz.pack();
		viz.setVisible(true);
	}

}
