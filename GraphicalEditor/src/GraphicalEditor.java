

import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.CTag;
import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CHierarchyTag;
import fr.lri.swingstates.sm.StateMachineListener;
import fr.lri.swingstates.debug.StateMachineVisualization;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.canvas.transitions.*;
import fr.lri.swingstates.sm.transitions.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.Container;
import java.awt.Color;
import java.awt.Font;
import java.awt.BasicStroke;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import java.util.ArrayList;


public class GraphicalEditor extends JFrame {

	private Canvas canvas;
	private CShape palette;
	private SelectionTool selector;
	private ArrayList<CStateMachine> tools;
	private JFrame smviz;

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

	public GraphicalEditor(String title, int width, int height) {
		super(title);
		canvas = new Canvas(width, height);
		canvas.setAntialiased(true);
		getContentPane().add(canvas);
		smviz = null;

		tools = new ArrayList<CStateMachine>();
		selector = new SelectionTool();
		tools.add(selector);
		tools.add(new RectangleTool());
		tools.add(new EllipseTool());
		tools.add(new LineTool());
		tools.add(new PathTool());

		int iconsize = 50;
		palette = canvas.newRectangle(20, 5, tools.size() * iconsize, 15);
		palette.addTag(selector.getBaseTag());
		CText ptitle = canvas.newText(0, 0, "TOOLS", new Font("verdana",
				Font.PLAIN, 12));
		ptitle.setParent(palette);
		ptitle.setReferencePoint(0.5, 0.5).translateTo(palette.getCenterX(),
				palette.getCenterY());
		new CStateMachine() {
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
						for (CStateMachine aTool : tools)
							aTool.setActive(aTool == theTool);
						consumes(true);
					}
				};
			};
		}.attachTo(canvas);

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

		CStateMachine vizToggler = new CStateMachine() {
			public State invisible = new State() {
				Transition help = new KeyPress(KeyEvent.VK_F1, ">> visible") {
					public void action() {
						if (smviz == null) {
							smviz = new JFrame("StateMachine Viz");
							Container pane = smviz.getContentPane();
							pane.setLayout(new BoxLayout(pane,
									BoxLayout.PAGE_AXIS));
							for (CStateMachine csm : tools) {
								pane.add(new JLabel(csm.getClass().getName()));
								pane.add(new StateMachineVisualization(csm));
							}
							smviz.pack();
						}
						smviz.setVisible(true);
					}
				};
			};
			public State visible = new State() {
				Transition help = new KeyPress(KeyEvent.VK_F1, ">> invisible") {
					public void action() {
						smviz.setVisible(false);
					}
				};
			};
		};
		vizToggler.attachTo(canvas);

		pack();
		setVisible(true);
		canvas.requestFocusInWindow();
	}

	public void populate() {
		canvas.newRectangle(10, 210, 30, 30).addTag(selector.getBaseTag());
		canvas.newRectangle(50, 300, 30, 30).addTag(selector.getBaseTag());
		canvas.newRectangle(100, 250, 30, 30).addTag(selector.getBaseTag());
	}

	public static void main(String[] args) {
		GraphicalEditor editor = new GraphicalEditor("Graphical Editor", 400,
				600);
		// editor.populate() ;
		editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
