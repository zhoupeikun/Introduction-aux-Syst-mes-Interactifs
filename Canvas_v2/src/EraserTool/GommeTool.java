package EraserTool;

import fr.lri.swingstates.canvas.CShape;

/**
 * Created by peikun on 4/4/16.
 */
public class GommeTool extends CShape {

    private String type;

    GommeTool(String type){
        this.type =  type;
    }

    String getType(){
        return this.type;
    }
}
