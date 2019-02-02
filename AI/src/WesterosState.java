public class WesterosState extends State
{
	int i;
	int j;
	int numOfWhitewalkersRem;
	int numOfDragonglass;
	int numOfDragonglassUsed;
	int maxNumOfDragonglass;
	boolean isGoal;
	char[][] grid;
	
	public WesterosState(int i, int j, int numOfWhitewalkersRem, int numOfDragonglass, int numOfDragonglassUsed, int maxNumOfDragonglass, boolean isGoal, char[][] grid)
	{
		this.i = i;
		this.j = j;
		this.numOfWhitewalkersRem = numOfWhitewalkersRem;
		this.numOfDragonglass = numOfDragonglass;
		this.numOfDragonglassUsed = numOfDragonglassUsed;
		this.maxNumOfDragonglass = maxNumOfDragonglass;
		this.isGoal = isGoal;
		this.grid = grid;
	}
	
	public boolean areEqual(State a)
	{
		WesterosState aW = (WesterosState)a;
		
		if(aW.i == this.i 
				&& aW.j == this.j 
				&& aW.numOfWhitewalkersRem == this.numOfWhitewalkersRem 
				&& aW.numOfDragonglass == this.numOfDragonglass 
				&& aW.numOfDragonglassUsed == this.numOfDragonglassUsed)
			return true;
		else
			return false;
	}
	
}
