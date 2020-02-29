package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Board;
import model.Shape;

public class NextShapesPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private BufferedImage tiles;
	private BufferedImage[] colors;
	private LinkedList<Shape> shapes = new LinkedList<Shape>();
	
	public NextShapesPanel() {
		init();
	}
	
	private void init() {
		loadImages();
		colors = new BufferedImage[8];
		setColors();
		this.setLayout(new GridLayout(4,18,1,1));
		this.setBackground(Color.black);
		this.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,2));
		this.setPreferredSize(new Dimension(240,500));
	}
	
	private void loadImages() {
		try {
			tiles = ImageIO.read(Board.class.getResource("/images/tiles4.png"));
			tiles = ImageResizer.resize(tiles,0.75);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setColors() {
	
		for(int i=0; i<colors.length; i++){
			colors[i] = tiles.getSubimage(i*30,0, 30, 30);	
		}
	}
	
	public void updateNextShape(LinkedList<Shape> shape) {
		shapes = shape;
	}
	
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		if(shapes==null|| shapes.size() == 0) {
			return;
		}
		Graphics2D g2 = (Graphics2D)g;
		int row = 1;
		for(int k = 0; k<3; k++) {
			if(shapes!=null&& shapes.size() != 0) {
				Shape shape = shapes.get(k);
				for(int i = 0; i < shape.getCoords().length; i++) {
					for(int j = 0; j < shape.getCoords()[i].length; j++) {
						if(shape.getCoords()[i][j] == 1) {
							g2.drawImage(colors[shape.getColor()-1],j*30 + 2*30,i*30 + row*35,null);
							
						}
					}
				}
				
			}
			row = row + 4;
		}
		
	}
}
