import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


class Board extends JPanel
{ 
	
	public static final int BLOCK_SIZE = 24;
	public static final int BOTTOM = 510;
	public static final int LEFT_BORDER = 100;
	public static final int RIGHT_BORDER = 240;
	public static final int TOP = 30;
	public static final int HOLD_X = 424;
	public static final int HOLD_Y = 64;
	public static final int UP_NEXT_X = 424;
	public static final int UP_NEXT_Y = 224;
	
	private BufferedImage tiles;
	private int[][] rotation;
	private int[][] board;
	private Tetris t;
	private Tetris holdBlock;
	private int row; 
	private int col;  
	private int numRotations;
	private int topOfFallenPieces;
	private boolean firstHold;
	private boolean printHold;
	
	private Queue<Tetris> blockQueue = new LinkedList<Tetris>();
	
	
	// constructor
	public Board() 
	{ 
		col = 190;
		row = 32;
		numRotations = 0;
		board = new int[10][20];
		firstHold = true;
		
		newShape();
		setImage();
	} 
	   
	// setters and getters
	public int getRow() 
	{   
		return row;  
	}    
	
	public void setRow(int row) 
	{   
		this.row = row;  
	}    
	
	public int getCol() 
	{   
		return col;  
	} 
	
	public void setCol(int col) 
	{   
		this.col = col;  
	}       
	
	public void setImage() 
	{   
		tiles = null;
		// reads the image file of the blocks
		try {
			tiles = ImageIO.read(new File("C:\\Users\\Amy\\Documents\\GitHub\\Tetris\\tiles.png"));
		} catch (IOException e) {
			
		} 
	} 

	// motion control methods
	public void rotate(){
		rotation = t.getState(numRotations%t.states.length);
		numRotations++;
	}
	
	public void speedDown(){
		MainActivity.ns = 1000000000.0/1000.0;
		
	}
	public void stopSpeed(){
		MainActivity.ns = 1000000000.0/60.0;
	}
	
	public void drop(){
		if(isValidAndEmpty(t, col, row + topOfFallenPieces, numRotations))
			row = BOTTOM;
	}
	public void moveRight(){
		if(isValidAndEmpty(t, col + 15, row, numRotations))
			col = col + 15;
		
	}
	public void moveLeft() {
		if(isValidAndEmpty(t, col - 15, row, numRotations))
				col = col - 15;
	}
	
	public void hold(){
		if(firstHold){
			firstHold = false;
			holdBlock = t;
		}
		else{
			Tetris tempBlock;
			
			tempBlock = holdBlock;
			holdBlock = t;
			t = tempBlock;
			rotation = t.getShape();
		}
		
		printHold = true;
		repaint();
	}
	
	// resets the y position of the block so it stays on the screen
	public void updatePosition(){
		if(row <= BOTTOM)
			row += 1;
		else{
			row = 32;
			repaint();
			newShape();
		}
	}
	
	
	// gets a random block shape, color, and coordinates
	public void newShape(){
		
		while(blockQueue.size() < 3){
			blockQueue.add(Tetris.randomOne());
		}
		
		t = blockQueue.remove();
		rotation = t.getShape();
		numRotations = 0;
	}
	
	// Printing methods
	// paints the board and renders the block shapes at the same time
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		
		printShape(col, row, g, t.getColor(), rotation);
		printShape(UP_NEXT_X, UP_NEXT_Y, g, blockQueue.peek().getColor(), blockQueue.peek().getShape());
		paintBoard(g);
		
		if(printHold){
			printShape(HOLD_X, HOLD_Y, g, holdBlock.getColor(), holdBlock.getShape());
		}
		
	}
	
	
	//paints the simple lines of the gameplay board
	private void paintBoard(Graphics g){
		// prints the board lines
		g.drawRect(LEFT_BORDER, TOP, RIGHT_BORDER, BOTTOM);
		
		g.drawString("Hold", 420, 40);
		g.drawRect(400, 50, 144, 120);
		
		g.drawString("Up Next", 420, 190);
		g.drawRect(400, 200, 144, 120);
	}
	
	
	// prints the tetris blocks based on 2D array coordinates
	private void printShape(int x, int y, Graphics g, int color, int[][] coords){
		
		//crops the image to a single 24X24 block
		BufferedImage block = tiles.getSubimage(color, 0, BLOCK_SIZE, BLOCK_SIZE);
		
		//prints the block in the shape of the tetromino
		for(int row = 0; row < coords.length; row++)
			for(int col = 0; col < coords[row].length; col++)
				if(coords[row][col] == 1){
					g.drawImage(block, col * BLOCK_SIZE + x, row * BLOCK_SIZE + y, null);
				}
		
	}
	
	
	
	
	// Collision Detection methods
	//You can for example use a 2D array with Boolean or integer values that represent presence of a square on a location: int[][] squares = new int[20][10];

	private static int COL_COUNT = 10;
	private static int ROW_COUNT = 20;

	//The following method finds the collision
	public boolean isValidAndEmpty(Tetris type, int x, int y, int rotation)
	{

	        //Ensure the piece is in a valid column.
	        if(x < -type.getLeftInset(rotation) ||
	            x + type.getDimension() - type.getRightInset(rotation) >= COL_COUNT)
	        {
	            return false;
	        }

	        //Ensure the piece is in a valid row.
	        if(y < -type.getTopInset(rotation) ||y + type.getDimension() -type.getBottomInset(rotation) >= ROW_COUNT)
	        {
	            return false;
	        }


	         /*
	         * Loop through every tile in the piece and see if it conflicts
	         * with an existing tile.
	         * Note: It's fine to do this even though it allows for wrapping * because we've already
	         * checked to make sure the piece is in a valid location.*/
	        for(int col = 0; col < type.getDimension(); col++)
	{
	            for(int row = 0; row < type.getDimension(); row++)
	            {
	            if(type.isTile(col, row, rotation) &&
	                    isOccupied(x + col, y + row))
	            {
	                return false;

	        }
	}

	}
	                return true;

	}

	private boolean isOccupied(int i, int j) {
			if(board[i][j] == 1)
				return true;
			else
				return false;
	}
	

}
