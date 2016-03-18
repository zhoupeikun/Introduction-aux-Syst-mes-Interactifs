package Projet.code;

import java.awt.Color;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.Canvas;

public class Widget_Barre extends CRectangle {
	
	private JButton pinceau, pot, gomme, forme;
	private JPanel widget = new JPanel();
	private CRectangle rectangle;
	private Canvas canvas;

	
	public Widget_Barre(Canvas c) {
		canvas = c;
		rectangle = canvas.newRectangle(100, 100, 100, 15);		
		
		widget.setLayout(new BoxLayout(widget, BoxLayout.Y_AXIS));
		Border border, margin;
		
		margin = new EmptyBorder(10,10,10,10);
		border = BorderFactory.createLineBorder(Color.BLACK, 2);
		
		pinceau = new JButton(new ImageIcon("src/"));
		widget.add(pinceau);
		
		pot = new JButton(new ImageIcon("src/"));
		widget.add(pot);
		
		gomme = new JButton(new ImageIcon("src/"));
		widget.add(gomme);
		
		forme = new JButton(new ImageIcon("src/"));
		widget.add(forme);
		
		//this.setBackground(Color.WHITE);
		//this.setBorder(new CompoundBorder(border, margin));
		
		//this.add(widget);
		
		this.setParent(rectangle);		
		
	}

}
