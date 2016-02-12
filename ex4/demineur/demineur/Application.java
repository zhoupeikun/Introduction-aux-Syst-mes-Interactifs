package demineur;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by aprouzeau on 13/01/2016.
 */
public class Application extends JFrame {

	private DemineurModel model;
    

    public Application(int n, int nombreMines){
        this.model = new DemineurModel(n, nombreMines);
        MineAreaPanel pnl = new MineAreaPanel();
        this.setVisible(true);
    }



    public void initUi() {
        Frame main = new Frame();
        MineAreaPanel area = new MineAreaPanel();


    }

    private class MineAreaPanel extends JPanel {
        private int rows = 20;
        private int columns = 20;
        private Block[][] blocks = null;

        public MineAreaPanel() {
            GridLayout gl = new GridLayout(rows, columns);
            this.setLayout(gl);

            Border border = BorderFactory
                    .createBevelBorder(BevelBorder.LOWERED);
            this.setBorder(border);

            imgMineArray = new Icon[10];
            for (int i = 0; i < 9; i++) {
                imgMineArray[i] = new ImageIcon("images/N_" + i + ".png");
            }
            imgMineArray[9] = new ImageIcon("images/0.png");

            blocks = new Block[rows][columns];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    blocks[i][j] = new Block(imgMineArray[9]);
                    blocks[i][j].addMouseListener(mlBlock);
                    this.add(blocks[i][j]);
                }
            }
        }

        private class Block extends JLabel {
            private boolean mine = false;
            private int roundMineCount;
            private boolean display = false;

            public boolean isDisplay() {
                return display;
            }

            public void setDisplay(boolean display) {
                this.display = display;
            }

            public boolean isMine() {
                return mine;
            }

            public void setMine(boolean mine) {
                this.mine = mine;
            }

            public int getRoundMineCount() {
                return roundMineCount;
            }

            public void setRoundMineCount(int roundMineCount) {
                this.roundMineCount = roundMineCount;
            }

            public Block(Icon icon) {
                super(icon);
            }
        }



    public static void main(String[] args) {
        new Application(10, 10);
    }


}
