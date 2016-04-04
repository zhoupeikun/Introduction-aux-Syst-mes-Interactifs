package PathTool;


public class SizeTool extends PenTool {
	
	final static int minsize = 1;
	final static int midsize = 20;
	final static int maxsize = 30;
	
	private int size;
	
	public SizeTool(){
		this(minsize);
	}
	public SizeTool(int size){
		super("size");
		this.size = size;
	}
	
	public void setSize(int size){
		this.size = size;
	}
	
	public int getSize(){
		return this.size;
	}
}
