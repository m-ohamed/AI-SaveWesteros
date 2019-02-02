import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class SaveWesteros extends GenericSearchProblem
{
	static int maxNumOfDragonglass;
	static int totalNumberOfWhitewalkers;
	static char[][] grid;
	WesterosNode root;
	
	public SaveWesteros()
	{
		this.root = new WesterosNode((State)new WesterosState(grid.length - 1,grid[0].length - 1,totalNumberOfWhitewalkers,0,0,maxNumOfDragonglass,false,grid),null,' ',0,0);
	}
	
	public static char[][] GenGrid()
	{
		Random rand = new Random();
		
		//Random number formula: rand.nextInt((max - min) + 1) + min;
		
		//Grid could be initialzed to be a random size from 4 to 10, however, we stuck to it being 4x4.
//		int  m = rand.nextInt(7) + 4;
//		int  n = rand.nextInt(7) + 4;
		
		int m = 4, n = 4;
		
		char[][] res = new char[m][n];
		
		for(int i = 0; i < m; i++)
			for(int j = 0; j < n; j++)
				res[i][j] = ' ';
		
		res[m-1][n-1] = 'J';
		
		//Randomizing the position of the dragonstone
		int dm = rand.nextInt(m);
		int dn = rand.nextInt(n);
		
		while(dm == (m - 1) && dn == (n - 1))
		{
			dm = rand.nextInt(m);
			dn = rand.nextInt(n);
		}
		System.out.println("Dragonglass X: " + dm + ", Dragonglass Y: " + dn);
		
		res[dm][dn] = 'D';
		
		//Randomizing the number of whitewalkers spawning to be the size of the grid divided by 2, meaning
		//in a 4x4 grid a maximum of 8 whitewalkers will be available.
		totalNumberOfWhitewalkers = rand.nextInt((((m*n)/2) - 1) + 1) + 1;
		System.out.println("Total number of whitewalkers: " + totalNumberOfWhitewalkers);

		//The whitewalkers positions will then be added, while making sure that this cell is free and not
		//occupied by anything else.
		for(int i = 0; i < totalNumberOfWhitewalkers; i++)
		{
			int wm = rand.nextInt(m);
			int wn = rand.nextInt(n);
			
			while(res[wm][wn] != ' ')
			{
				wm = rand.nextInt(m);
				wn = rand.nextInt(n);
			}
			
			System.out.println("Whitewalker #" + i + " at position x,y: " + wm + ", " + wn);
			
			res[wm][wn] = 'W';
		}
		
		//Randomizing an obstacle and initializing it.
		int om = rand.nextInt(m), on = rand.nextInt(n);
		
		while(res[om][on] != ' ')
		{
			om = rand.nextInt(m);
			on = rand.nextInt(n);
		}
		
		res[om][on] = 'O';
		
		//Randomizing the maximum number of dragonglass that Jon can hold.
		maxNumOfDragonglass = rand.nextInt((totalNumberOfWhitewalkers - 1) + 1) + 1;;
		
		System.out.println("Maximum number of dragonglass: " + maxNumOfDragonglass);
		
		printGrid(res);
		try {
			outputGrid(res);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return res;
		
	}
	
	public static void outputGrid(char[][] res) throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter("Grid.pl", "UTF-8");
		
		for(int i = 0; i < res.length; i++)
		{
			for(int j = 0; j < res[0].length; j++)
			{
				switch(res[i][j])
				{
				case 'J': writer.println("jon(" + i + "," + j + ",s0" + ")."); break;
				case 'W': writer.println("whitewalker(" + i + "," + j + ",s0" + ")."); break;
				case 'O': writer.println("obstacle(" + i + "," + j + ")."); break;
				case 'D': writer.println("dragonstonepos(" + i + "," + j + ")."); break;
				}
			}
		}
		
		writer.println("maxSize(" + res.length + "," + res[0].length + ").");
		writer.println("dragonGlass(0,s0).");
		writer.println("dragonGlassMax(" + maxNumOfDragonglass + ").");
		
		//output += "wadap";
		
		
		
		
		
		writer.close();
		
	}
	
	public static void printGrid(char[][] res)
	{
		for(int i = 0; i < res.length; i++)
		{
			for(int j = 0; j < res[i].length; j++)
			{
				System.out.print("[" + res[i][j] + "]");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args)
	{
		grid = GenGrid();
//		
//		SaveWesteros q = new SaveWesteros();
//	
//		String[] res = q.search(grid,"AS1",true);
//		
//		System.out.println(res[0]);
//		System.out.println(res[1]);
//		System.out.println(res[2]);		
	}
}
