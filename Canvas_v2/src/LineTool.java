

import PathTool.ColorTool;
import PathTool.PenTool;
import PathTool.SizeTool;
import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.CSegment;
import fr.lri.swingstates.canvas.transitions.DragOnTag;
import fr.lri.swingstates.canvas.transitions.EnterOnTag;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * @author Nicolas Roussel (roussel@lri.fr)
 *
 */
public class LineTool extends CStateMachine {

	private CSegment seg;
	private Point2D p1;
	final int iconsize = 50;

	private int size_pen = 1;
	private String color;

	private ArrayList<PenTool> linetools;
	private boolean menuCreated = false;
	private CShape palette1;
	private CShape palette2;

	class PenTag extends CExtensionalTag {
		private String type;
		private PenTool pentool;

		PenTag(PenTool pt){
			this.type = pt.getType();
			this.pentool = pt;
		}
		String getType() {
			return this.type;
		}
		PenTool getPenTool(){
			return this.pentool;
		}

	}


	public State start, draw, crossing;

	public LineTool() {
		this(BUTTON1, NOMODIFIER);
		linetools = new ArrayList<PenTool>();
		linetools.add(new ColorTool("red"));
		linetools.add(new ColorTool("green"));
		linetools.add(new ColorTool("blue"));
		linetools.add(new SizeTool(1));
		linetools.add(new SizeTool(4));
		linetools.add(new SizeTool(10));
	}

	public LineTool(final int button, final int modifier) {
		start = new State() {
			Transition press = new Press(button, modifier, ">> draw") {
				public void action() {
					Canvas canvas = (Canvas) getEvent().getSource();
					p1 = getPoint();
					seg = canvas.newSegment(p1, p1);
					seg.setStroke(new BasicStroke(getPenSize()));
					seg.setOutlinePaint(getPenColor());
				}
			};

			Transition drag = new Drag(button,"=>crossing") {
				public void action() {
					Canvas canvas = (Canvas) getEvent().getSource();
					if(!menuCreated){
						palette1 = canvas.newRectangle(120, 70, iconsize*3, 50);
						palette2 = canvas.newRectangle(120, 120, iconsize*3, 50);
						for (int i = 0; i < linetools.size(); i++) {
							PenTool tool = linetools.get(i);
							CShape s;
							if(i<3){
								s = canvas.newImage(120 + i * iconsize, 70, "resources/"
										+ tool.getClass().getName() + i + ".png");
							}
							else{
								s = canvas.newImage(120 + (i-3)* iconsize, 120, "resources/"
										+ tool.getClass().getName() + i + ".png");
							}
							if(i<=2)
								s.setParent(palette1);
							else
								s.setParent(palette2);
							s.addTag(new PenTag(tool));
						}
						menuCreated = true;
					}
				}
			};
		};
		crossing = new State(){
			Transition enter = new DragOnTag(PenTag.class, button){
				public void action(){
					Canvas canvas = (Canvas) getEvent().getSource();
					PenTag tt = (PenTag) getTag();
					/*canvas.getTag(ToolTag.class).setStroke(
							new BasicStroke(1));*/
					LinkedList<CShape> groupColor = palette1.getChildren();
					LinkedList<CShape> groupSize = palette2.getChildren();

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
						for(CShape c: groupColor){
							c.setStroke(new BasicStroke(1));
						}
					}
					tt.setStroke(new BasicStroke(4));
				}
			};

			Transition release = new Release(button, "=>start"){
				public void action(){
					Canvas canvas = (Canvas) getEvent().getSource();
					LinkedList l1 = palette1.getChildren();
					LinkedList l2 = palette2.getChildren();
					int n = palette1.getChildrenCount();
					while(n>0){
						canvas.removeShape((CShape)l1.get(n-1));
						canvas.removeShape((CShape)l2.get(n-1));
						n--;
					}
					canvas.removeShape(palette1);
					canvas.removeShape(palette2);
					menuCreated = false;
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
}
