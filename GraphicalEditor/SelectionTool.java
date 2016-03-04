package e6;

import fr.lri.swingstates.canvas.CStateMachine ;
import fr.lri.swingstates.canvas.CExtensionalTag ;
import fr.lri.swingstates.canvas.CElement ;
import fr.lri.swingstates.sm.State ;
import fr.lri.swingstates.sm.Transition ;
import fr.lri.swingstates.canvas.transitions.*;
import fr.lri.swingstates.sm.transitions.*;

import java.awt.geom.Point2D ;

/**
 * @author Nicolas Roussel (roussel@lri.fr)
 *
 */
public class SelectionTool extends CStateMachine {
    
    private CExtensionalTag movableTag ;
    private CElement moved ;
    private Point2D pPrevious ;

    public State start, move ;

    public SelectionTool() {
	   this(BUTTON1,NOMODIFIER) ;
    }
			
    public SelectionTool(final int button, final int modifier) {
	   movableTag = new CExtensionalTag() {} ;

	   start = new State() {
			 Transition move = new PressOnTag(movableTag, button, modifier, ">> move") {
				    public void action() {
					   pPrevious = getPoint() ;
					   moved = getShape() ;
					   consumes(true) ;
				    }
				} ;
		  } ;
    
	   move = new State() {
			 Transition drag = new Drag(button, modifier) {
				    public void action() {
					   moved.translateBy(getPoint().getX() - pPrevious.getX(), getPoint().getY() - pPrevious.getY()) ;
					   pPrevious = getPoint() ;
				    }
				} ;
			 Transition stop = new Release(button, modifier, ">> start") {
				    public void action() {
					   moved.translateBy(getPoint().getX() - pPrevious.getX(), getPoint().getY() - pPrevious.getY()) ;
				    }
				} ;
		  } ;

    }

    public CExtensionalTag getMovableTag() {
	   return movableTag ;
    }

}
