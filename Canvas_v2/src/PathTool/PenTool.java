package PathTool;

import fr.lri.swingstates.canvas.CShape;


public class PenTool extends CShape{
	private String type;
	
	PenTool(String type){
		this.type =  type;
	}
	
	String getType(){
		return this.type;
	}
}
