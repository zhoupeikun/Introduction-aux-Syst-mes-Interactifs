

import java.util.EventObject;

import fr.lri.swingstates.canvas.CShape;


public class ShapeCreatedEvent extends EventObject {

	CShape shape;

	public ShapeCreatedEvent(Object source, CShape s) {
		super(source);
		shape = s;
	}

	public CShape getShape() {
		return shape;
	}

}
