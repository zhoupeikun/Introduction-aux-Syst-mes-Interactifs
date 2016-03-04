package e6;

import java.util.EventObject ;

import fr.lri.swingstates.canvas.CShape ;

/**
 * @author Nicolas Roussel (roussel@lri.fr)
 *
 */
@SuppressWarnings("serial")
public class ShapeCreatedEvent extends EventObject {
	
    CShape shape ;

    public ShapeCreatedEvent(Object source, CShape s) {
	   super(source) ;
	   shape = s ;
    }
	
    public CShape getShape() {
	   return shape ;
    }

}
