package e6;

import fr.lri.swingstates.canvas.Canvas ;
import fr.lri.swingstates.canvas.CStateMachine ;
import fr.lri.swingstates.canvas.CShape ;
import fr.lri.swingstates.sm.StateMachineListener ;
import fr.lri.swingstates.debug.StateMachineVisualization ;

import javax.swing.JFrame ;
import javax.swing.JLabel ;
import javax.swing.BoxLayout ;
import javax.swing.BorderFactory ;
import java.awt.Container ;
import java.awt.Color ;
import java.util.EventObject ;

/**
 * @author Nicolas Roussel (roussel@lri.fr)
 *
 */
public class GraphicalEditor extends JFrame implements StateMachineListener {

    private Canvas canvas ;
    private SelectionTool selector ;

    public GraphicalEditor(String title, int width, int height) {
	   super(title) ;

	   canvas = new Canvas(width, height) ;
	   getContentPane().add(canvas) ;

	   selector = new SelectionTool(CStateMachine.BUTTON1, CStateMachine.SHIFT) ;
	   selector.attachTo(canvas) ;

	   CStateMachine tool = new RectangleTool(CStateMachine.BUTTON1, CStateMachine.NOMODIFIER) ;
	   tool.attachTo(canvas) ;
	   tool.addStateMachineListener(this) ;

	   JFrame smviz = new JFrame("StateMachine Viz") ;
	   Container pane = smviz.getContentPane() ;
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
	   pane.add(new JLabel("SelectionTool")) ;
	   pane.add(new StateMachineVisualization(selector)) ;
	   pane.add(new JLabel("RectangleTool")) ;
	   pane.add(new StateMachineVisualization(tool)) ;
	   smviz.pack() ;
	   smviz.setVisible(true) ;

	   pack() ;
	   setVisible(true) ;
    }

    public void eventOccured(EventObject e) {
	   ShapeCreatedEvent csce = (ShapeCreatedEvent)e ;
	   csce.getShape().addTag(selector.getMovableTag()).setFillPaint(Color.white) ;
    }
	
    public static void main(String[] args) {
	   GraphicalEditor editor = new GraphicalEditor("Graphical Editor",400,600) ;
	   editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
    }

}
