package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.*;

/**
 * THIS CLASS IS A VIEW
 * BoardView is a class that draws the 20x10 board on a JPanel.
 * It can also draw a grid. 
 * 
 * @author Philip
 * @version 1.0
 */

public class BoardView extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private BufferedImage tiles, background;
	private int[][] boardCoords;
	private final int HEIGHT,WIDTH, BLOCKSIZE;
	private Shape currentShape;
	private Board board;
	private boolean withGrid;  
	private BufferedImage[] colors;
	
	public BoardView(Board board,int height,int width, int blocksize, boolean withGrid) {
		this.board = board;
		this.HEIGHT = height;
		this.WIDTH = width;
		this.BLOCKSIZE = blocksize;
		this.withGrid = withGrid;
		this.setPreferredSize(new Dimension(width*blocksize,height*blocksize));
		init();
		
	}
	
	
	
	public void init() {
		colors = new BufferedImage[7];
		this.setBackground(Color.cyan); //om man vill ha en enf�rgad bakgrund:
		
		boardCoords = board.getBoard(); //h�mta spelplanen
		
		try {
			tiles = ImageIO.read(Board.class.getResource("/images/tiles.png"));
			background = ImageIO.read(Board.class.getResource("/images/background1.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}

		setColors();
	}
	
	public void setColors() {
		for(int i=0; i<colors.length; i++){
			colors[i] = tiles.getSubimage(i*40,0, 40, 40);
		}
	}
	
	// paintComponent ritar upp bakgrund, Shape som man kan styra(currentShape), en grid(om man vill) och statiska shapes(shapes som har kolliderat)
	// funktionen anropas av systemet
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		g2.drawImage(background,0,0,null); //rita upp bakgrunden
		
		currentShape = GameEngine.getCurrentShape(); 
		
		if(withGrid) {
			
			g2.setColor(Color.BLACK); //set color 
			g2.setStroke(new BasicStroke(2)); //set thickness
					
			//horizontal
			for(int i = 0; i <= HEIGHT; i++) {
				g.drawLine(0, i*BLOCKSIZE, WIDTH*BLOCKSIZE, i*BLOCKSIZE);
				
			}
			
			//vertical
			for(int j = 0; j<= WIDTH; j++) {
				g.drawLine(j*BLOCKSIZE, 0, j*BLOCKSIZE,HEIGHT*BLOCKSIZE );
			}
		}
		
		//rita upp currentShape
		for(int i = 0; i < currentShape.getShape().length; i++) {
			for(int j = 0; j < currentShape.getShape()[i].length; j++) {
				if(currentShape.getShape()[i][j] == 1) {
					g.drawImage(colors[currentShape.getColor()-1],j*40 + currentShape.x*BLOCKSIZE,i*BLOCKSIZE + currentShape.y* BLOCKSIZE,null);
				}
			}
		}
		
		//rita upp shapes som har kolliderat
		for(int i=0; i<boardCoords.length; i++) {
			for(int j=0; j<boardCoords[i].length; j++) {
				if(boardCoords[i][j] != 0) {
					g2.drawImage(tiles.getSubimage((boardCoords[i][j]-1)*BLOCKSIZE, 0, BLOCKSIZE, BLOCKSIZE), j*BLOCKSIZE, i*BLOCKSIZE, null);
				}
				
			}
		}
		
	}
	
}
