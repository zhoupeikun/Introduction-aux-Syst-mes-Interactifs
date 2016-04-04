package EraserTool;

/**
 * Created by peikun on 4/4/16.
 */
public class SizeTool extends GommeTool{

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
