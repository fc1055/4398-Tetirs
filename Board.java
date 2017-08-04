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
	public static final int RIGHT_BORDER = 340;
	public static final int MIDDLE = 220;
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
	private boolean firstHold;
	private boolean printHold;
	private boolean collision;
	
	private Queue<Tetris> blockQueue = new LinkedList<Tetris>();

	
	
	// constructor
	public Board() 
	{ 
		numRotations = 0;
		board = new int[20][10];
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
		// get the rotation pattern
		rotation = t.getState(numRotations%t.states.length);
		numRotations++;
		
		// if after rotating, the block goes past the right border, move the block left
		if(col + rotation[0].length * BLOCK_SIZE > RIGHT_BORDER){
			col = col - BLOCK_SIZE;
		}
		// if after rotating, the block goes past the bottom border move it up
		// the I shaped block has an error still with this logic, it can go past
		// the bottom border, if you rotate after it hits. Not sure how to fix that.
		if(row + rotation.length * BLOCK_SIZE > BOTTOM){
			row = row - BLOCK_SIZE;
		}
	}
	
	public void speedDown(){
		MainActivity.ns = 1000000000.0/1000.0;
		
	}
	public void stopSpeed(){
		MainActivity.ns = 1000000000.0/60.0;
	}
	
	public void drop(){
		row = BOTTOM - rotation.length * BLOCK_SIZE;
	}
	
	public void moveRight(){
		// check to make sure the block doesn't go past the right border
		if(!(col + 1 + rotation[0].length * BLOCK_SIZE > RIGHT_BORDER))
			col = col + 24;
		
	}
	public void moveLeft() {
		// check to make sure the block doesn't go past the left border
		if(!(col - 12 < LEFT_BORDER))
				col = col - 24;
	}
	
	public void hold(){
		// if you've never called hold before put current block
		// into hold
		if(firstHold){
			firstHold = false;
			holdBlock = t;
		}
		// else if you have called hold before, swap current block
		// with hold block
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
		
		if(collision){
			// takes the location of the block relative to 10X20 board
			// and prints it to the board[][] array
			for(int y = 0; y < rotation.length; y++)
				for(int x = 0; x < rotation[y].length; x++)
					if(rotation[y][x] != 0)
						board[(row - TOP)/BLOCK_SIZE + y][(col - LEFT_BORDER)/BLOCK_SIZE + x] = 1;
					

			newShape();
		}
		
		//Checks if the block has hit the bottom -- would like to instead use
		//if(isValidAndEmpty(col, row) to check if it his the bottom
		// and also if it hits a printed block
		if(!(row + rotation.length * BLOCK_SIZE > BOTTOM)){
			row++;
		}
		// uncomment to test  isValidAndEmpty()
		//if(isValidAndEmpty(col, row)){
		//	row++;
		//}
		else{
			collision = true;
		}

	}
	
	
	// gets a random block shape, color, and coordinates
	public void newShape(){
		
		while(blockQueue.size() < 3){
			blockQueue.add(Tetris.randomOne());
		}
		
		row = TOP;
		col = MIDDLE;
		t = blockQueue.remove();
		rotation = t.getShape();
		numRotations = 0;
		collision = false;
	}
	
	// Printing methods
	// paints the board and renders the block shapes at the same time
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		
		printShape(col, row, g, t.getColor(), rotation);
		printShape(UP_NEXT_X, UP_NEXT_Y, g, blockQueue.peek().getColor(), blockQueue.peek().getShape());
		paintBoard(g);
		paintDroppedBlocks(g, t.getColor());
			
		if(printHold){
			printShape(HOLD_X, HOLD_Y, g, holdBlock.getColor(), holdBlock.getShape());
		}
		
	}
	
	// runs through a 10X20 2D array, everywhere there is a 1, a tile is printed
	public void paintDroppedBlocks(Graphics g, int color){
		for(int y = 0; y < board.length; y++)
			for(int x = 0; x < board[y].length; x++)
				if(isOccupied(y,x)){
					g.drawImage(tiles.getSubimage(color,0,BLOCK_SIZE,BLOCK_SIZE), x * BLOCK_SIZE + LEFT_BORDER,
						    y * BLOCK_SIZE + TOP, null);
				}

	}
	
	//paints the simple lines of the gameplay board
	private void paintBoard(Graphics g){
		// prints the board lines
		g.drawRect(100, 30, 240, 480);
		
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

	//The following method finds the collision
	public boolean isValidAndEmpty(int x, int y)
	{

	      // //Ensure the piece is in a valid column.
	      // if(x < -type.getLeftInset(rotation) ||
	      //     x + type.getDimension() - type.getRightInset(rotation) >= COL_COUNT)
	      // {
	      //     return false;
	      // }
        
	       //Ensure the piece is in a valid row.
	       if(!(row + rotation.length * BLOCK_SIZE > BOTTOM))
	       {
	           return true;
	       }


	         /*
	         * Loop through every tile in the piece and see if it conflicts
	         * with an existing tile.
	         * Note: It's fine to do this even though it allows for wrapping * because we've already
	         * checked to make sure the piece is in a valid location.*/
	        for(int col = 0; col < rotation.length; col++){
	            for(int row = 0; row < rotation[col].length; row++){
	            	if((rotation[col][row] != 0) && isOccupied(x + col, y + row)){
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
	
