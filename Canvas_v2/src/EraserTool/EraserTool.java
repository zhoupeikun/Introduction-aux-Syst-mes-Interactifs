package EraserTool;

import fr.lri.swingstates.canvas.*;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.transitions.DragOnTag;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Drag;
import fr.lri.swingstates.sm.transitions.Press;
import fr.lri.swingstates.sm.transitions.Release;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

import static PathTool.PathTool.showStateMachine;

/**
 * Created by Peikun on 4/4/16.
 */
public class EraserTool extends CStateMachine {

    private CPolyLine line;
    public State start, crossing, draw;
    private boolean drawing = false;
    private boolean cross = false;
    private int size_eraser = 1;
    int iconsize = 48;

    private ArrayList<GommeTool> eraserTools;
    private boolean menuCreated = false;
    private CShape eraserPalette;


    class EraserTag extends CExtensionalTag {
        private String type;
        private GommeTool gommeTool;

        EraserTag(GommeTool gt) {
            this.type = gt.getType();
            this.gommeTool = gt;
        }

        String getType() {
            return this.type;
        }

        GommeTool getGommeTool() {
            return this.gommeTool;
        }
    }

    public EraserTool() {
        this(BUTTON1, NOMODIFIER);
        //create the toolbar for eraserTool
        eraserTools = new ArrayList<GommeTool>();

        eraserTools.add(new SizeTool(10));
        eraserTools.add(new SizeTool(20));
        eraserTools.add(new SizeTool(50));
    }

    public EraserTool(final int button, final int modifier) {
        start = new State() {
            Transition drag = new Drag(button, "=>crossing") {
                public boolean guard() {
                    cross = true;
                    return cross;
                }

                public void action() {

                    Canvas canvas = (Canvas) getEvent().getSource();

                    if (!menuCreated) {
                        eraserPalette = canvas.newRectangle(212, 70, iconsize * 3, 48);

                        for (int i = 0; i < 3; i++) {
                            GommeTool tool = eraserTools.get(i);
                            CShape s;
                            s = canvas.newImage(212 + i * iconsize, 70, "resources/"
                                    + "Eraser" + i + ".png");
                            //if(i<=2)
                            s.setParent(eraserPalette);
                            s.addTag(new EraserTag(tool));
                        }
                        menuCreated = true;

                    }
                }
            };
            Transition press = new Press(button, modifier, "=>draw") {

                public void action() {
                    Canvas canvas = (Canvas) getEvent().getSource();
                    line = canvas.newPolyLine(getPoint());
                    line.setFilled(false);
                    line.setStroke(new BasicStroke(getEraserSize()));
                    line.setOutlinePaint(Color.WHITE);
                }
            };

			/*Transition release = new Release(button, modifier){
				public void action(){
					line.lineTo(getPoint());
				}
			}; */
        };

        crossing = new State() {

            Transition enter = new DragOnTag(EraserTag.class, button) {


                public void action() {
                    Canvas canvas = (Canvas) getEvent().getSource();
                    EraserTag tt = (EraserTag) getTag();
					/*canvas.getTag(ToolTag.class).setStroke(
							new BasicStroke(1));*/
                    LinkedList<CShape> groupSize = eraserPalette.getChildren();

                    String type = tt.getType();
                    setEraserSize((SizeTool) tt.getGommeTool());
                    System.out.println("eraserSize=" + getEraserSize());
                    for (CShape c : groupSize) {
                            c.setStroke(new BasicStroke(1));
                        }
                    tt.setStroke(new BasicStroke(4));

                    canvas.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                            new ImageIcon("resources/eraser" + getEraserSize() +".png").getImage(),
                            new Point(canvas.getX(), canvas.getY()),""));
                }
            };


            Transition release = new Release(button, "=>start") {
                public void action() {
                    Canvas canvas = (Canvas) getEvent().getSource();
                    LinkedList l1 = eraserPalette.getChildren();
                    int n = eraserPalette.getChildrenCount();
                    while (n > 0) {
                        canvas.removeShape((CShape) l1.get(n - 1));
                        n--;
                    }
                    canvas.removeShape(eraserPalette);
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

            Transition stop = new Release(button, modifier, "=>start") {
                public void action() {
                    line.lineTo(getPoint());
                    //fireEvent(new ShapeCreatedEvent(EraserTool.this, line));
                }
            };
        };
        showStateMachine(this);
    }

    public void setEraserSize(SizeTool tt) {
        this.size_eraser = tt.getSize();
    }

    public int getEraserSize() {
        return this.size_eraser;
    }

}