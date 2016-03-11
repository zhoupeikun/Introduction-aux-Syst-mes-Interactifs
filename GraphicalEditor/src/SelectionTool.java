
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CElement;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.canvas.transitions.*;
import fr.lri.swingstates.sm.transitions.*;

import java.awt.geom.Point2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.KeyEvent;

import java.util.Hashtable;


public class SelectionTool extends CStateMachine {

	class SelectionTag extends CExtensionalTag {
		private Hashtable<CShape, CShape> marks;
		private CExtensionalTag marksTag;
		private Boolean oldMode = false;

		public SelectionTag() {
			super();
			marks = new Hashtable<CShape, CShape>();
			marksTag = new CExtensionalTag() {
			};
		}

		public void added(CShape s) {
			// System.out.format("Adding SelectionTag to %s\n",s) ;
			if (oldMode)
				s.setStroke(new BasicStroke(3));
			else {
				CRectangle mark = s.getBoundingBox();
				s.getCanvas().addShape(mark);
				mark.setDiagonal(mark.getMinX() - 3, mark.getMinY() - 3,
						mark.getMaxX() + 3, mark.getMaxY() + 3);
				float dash[] = { 5.0f };
				mark.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
				mark.setFilled(false).setOutlinePaint(Color.red).aboveAll();
				marks.put(s, mark);
				mark.addTag(marksTag);
			}
		}

		public void removed(CShape s) {
			// System.out.format("Removing SelectionTag from %s\n",s) ;
			if (oldMode)
				s.setStroke(new BasicStroke(1));
			else {
				CShape mark = marks.get(s);
				mark.setOutlinePaint(Color.blue);
				mark.getCanvas().removeShape(mark);
				marks.remove(s);
			}
		}

		public CElement translateBy(double tx, double ty) {
			reset();
			while (hasNext())
				(nextShape()).translateBy(tx, ty);
			marksTag.translateBy(tx, ty);
			return this;
		}
	}

	private CExtensionalTag baseTag, selectionTag;
	public State idle, move;
	private Point2D p;

	public SelectionTool() {
		this(BUTTON1, NOMODIFIER);
	}

	public SelectionTool(final int button, final int modifier) {
		baseTag = new CExtensionalTag() {
		};
		selectionTag = new SelectionTag();

		idle = new State() {
			Transition moveSelection = new PressOnTag(selectionTag, button,
					NOMODIFIER, ">> move") {
				public void action() {
					p = getPoint();
					consumes(true);
				}
			};
			Transition deselectOne = new ReleaseOnTag(selectionTag, button,
					CONTROL) {
				public void action() {
					getShape().removeTag(selectionTag);
					consumes(true);
				}
			};
			Transition select = new PressOnTag(baseTag, button, NOMODIFIER,
					">> move") {
				public void action() {
					((Canvas) getEvent().getSource()).removeTag(selectionTag);
					getShape().addTag(selectionTag);
					p = getPoint();
					consumes(true);
				}
			};
			Transition selectOneMore = new ReleaseOnTag(baseTag, button,
					CONTROL) {
				public void action() {
					getShape().addTag(selectionTag);
					consumes(true);
				}
			};
			Transition deselectAll = new Press(button, NOMODIFIER) {
				public void action() {
					((Canvas) getEvent().getSource()).removeTag(selectionTag);
					consumes(true);
				}
			};
			Transition delete = new KeyRelease(KeyEvent.VK_BACK_SPACE) {
				public void action() {
					Canvas canvas = (Canvas) getEvent().getSource();
					canvas.removeShapes(selectionTag);
					consumes(true);
				}
			};
			Transition duplicate = new KeyRelease(KeyEvent.VK_ENTER) {
				public void action() {
					Object[] shapes = selectionTag.getCollection().toArray();
					for (Object o : shapes) {
						CShape shape = (CShape) o;
						shape.removeTag(selectionTag);
						CShape dup = shape.duplicate();
						dup.aboveAll().translateBy(5, 5);
						dup.addTag(baseTag).addTag(selectionTag);
					}
					consumes(true);
				}
			};
		};

		move = new State() {
			Transition drag = new Drag(button, modifier) {
				public void action() {
					Point2D q = getPoint();
					selectionTag.translateBy(q.getX() - p.getX(),
							q.getY() - p.getY());
					p = q;
				}
			};
			Transition stop = new Release(button, modifier, ">> idle") {
				public void action() {
					Point2D q = getPoint();
					selectionTag.translateBy(q.getX() - p.getX(),
							q.getY() - p.getY());
				}
			};
		};

	}

	public CExtensionalTag getBaseTag() {
		return baseTag;
	}

	public CExtensionalTag getSelectionTag() {
		return selectionTag;
	}

}
