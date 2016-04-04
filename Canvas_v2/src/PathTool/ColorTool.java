package PathTool;



public class ColorTool extends PenTool {
	
	private String color;
	
	ColorTool(String color){
		super("color");
		this.color = color;
	}
	
	String getColor(){
		return this.color;
	}
}
