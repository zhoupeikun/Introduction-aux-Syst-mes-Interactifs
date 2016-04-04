package PathTool;



public class ColorTool extends PenTool {
	
	private String color;
	
	public ColorTool(String color){
		super("color");
		this.color = color;
	}
	
	public String getColor(){
		return this.color;
	}
}
