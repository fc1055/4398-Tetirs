import java.util.Random;



public abstract class Tetris
{
	//four blocks 
	protected int[][] cells;
	protected int[][][] states;
	protected int color;
	
	public int[][] getShape(){
		return cells;
	}
	public int[][] getState(int index){
		return states[index];
	}
	public int getColor(){
		return color;
	}


//static for four blocks       
public static Tetris randomOne() 
{            
	// blocks created         
	Random random = new Random();           
	int type = random.nextInt(7);           
	switch(type) 
	{            
		case 0 : return new T();
		case 1 : return new I();   
		case 2 : return new J();   
		case 3 : return new L();   
		case 4 : return new S();   
		case 5 : return new Z();   
		case 6 : return new O();
	}           
	
	return null;      
}
public int getBottomInset(int rotation) {
	// TODO Auto-generated method stub
	return 0;
}
public int getDimension() {
	// TODO Auto-generated method stub
	return 0;
}
public int getLeftInset(int rotation) {
	// TODO Auto-generated method stub
	return 0;
}
public int getRightInset(int rotation) {
	// TODO Auto-generated method stub
	return 0;
}
public int getTopInset(int rotation) {
	// TODO Auto-generated method stub
	return 0;
}
public boolean isTile(int col, int row, int rotation) {
	// TODO Auto-generated method stub
	return false;
}

}



class T extends Tetris 
{      
	public T() 
	{            
		color = 72;
		cells = new int [][] {{0,1,0},
				      {1,1,1}};
		states = new int[][][]{{{1,0},
					 {1,1},
					 {1,0}},          
						{{1,1,1},
		              		 	 {0,1,0}},          
			              			{{0,1},
							 {1,1},
							 {0,1}},          
							     {{0,1,0},
				              		      {1,1,1}}};  
	}	
}  

class I extends Tetris
{      
	public I() 
	{        
		color = 0;
		cells = new int [][] {{1,1,1,1}};      
		states = new int[][][]{{{1},{1},{1},{1}},
						{{1,1,1,1}}};
	}
	
}

class J extends Tetris 
{      
	public J() 
	{          
		color = 24;
		cells = new int [][] {{0,1},
				      {0,1},
				      {1,1}};
		states = new int[][][]{{{1,0,0},
					{1,1,1}},          
						{{1,1},
					  	 {1,0},
					  	 {1,0}},          
					      		{{1,1,1},
							 {0,0,1}},          
								 {{0,1},
								  {0,1},
								  {1,1}}};    
	} 

}

class L extends Tetris 
{      
	public L() 
	{            
		color = 120;
		cells = new int [][] {{1,0},
				      {1,0},
				      {1,1}};           
		states = new int[][][]{{{1,1,1},
					{1,0,0}},          
					       {{1,1},
						{0,1},
					  	{0,1}},          
					      	     {{0,0,1},
						      {1,1,1}},          
							     {{1,0},
							      {1,0},
							      {1,1}}};       
	} 
	
}  

class S extends Tetris 
{      
	public S() 
	{           
		color = 48;
		cells = new int [][] {{0,1,1},
				      {1,1,0}};
		states = new int[][][]{{{1,0},
					{1,1},
					{0,1}},                    
					      {{0,1,1},
					       {1,1,0}}};    
	}
	
}


class Z extends Tetris 
{      
	public Z() 
	{  
		color = 96;
		cells = new int [][] {{1,1,0},
				     {0,1,1}};
		states = new int[][][]{{{0,1},
					{1,1},
					{1,0}},                    
					     {{1,1,0},
					      {0,1,1}}};  
	} 
	
}


class O extends Tetris 
{ 
		public O() 
		{ 
			color = 144;
			cells = new int [][] {{1,1},
					      {1,1}};
			states = new int[][][]{{{1,1},
						{1,1}}};      
			
		}

}
