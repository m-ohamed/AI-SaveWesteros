import java.util.LinkedList;
import java.util.Queue;
import java.lang.Math;

public class WesterosNode extends SearchTreeNode
{
	State state;
	WesterosNode parent;
	int hn;
	String QingFun;

	public WesterosNode(State state, WesterosNode parent, char operator, int depth, int pathCost)
	{
		super(operator, depth, pathCost);
		this.state = state;
		this.parent = parent;
		this.hn = 0;
		this.QingFun = "   ";
	}

	public static void main(String[] args)
	{

	}

	public Queue<SearchTreeNode> expands(SearchTreeNode oriNode, char[] operators)
	{
		Queue<SearchTreeNode> res = new LinkedList<SearchTreeNode>();

		WesterosState currState = (WesterosState)((WesterosNode)oriNode).state;

		boolean goLeft = false;
		boolean goRight = false;
		boolean goUp = false;
		boolean goDown = false;

		for(int i = 0; i < operators.length; i++)
		{
			switch(operators[i])
			{
			case 'L': goLeft = true; break;
			case 'R': goRight = true; break;
			case 'U': goUp = true; break;
			case 'D': goDown = true; break;
			}
		}

		//To go left
		if(goLeft)
		{
			//Check if the cell to the left is available and not out of bounds
			if(currState.j > 0)
			{
				//Create a new grid and copy the old grid in it
				char[][] leftGrid = new char[currState.grid.length][currState.grid[0].length];

				for(int i = 0; i < currState.grid.length; i++)
					for(int j = 0; j < currState.grid[0].length; j++)
						leftGrid[i][j] = currState.grid[i][j];
				
				//Check if the cell that could be moved to (the one to the left) is not an obstacle
				if(currState.grid[currState.i][currState.j - 1] != 'O')
				{
					//Check if the cell is a dragonstone cell, and if so, create a new node and update it with the relevant information
					//(including position, number of dragonglass, depth, and path cost)
					if(currState.grid[currState.i][currState.j - 1] == 'D')
					{
						WesterosNode n= new WesterosNode(new WesterosState(currState.i,currState.j - 1,currState.numOfWhitewalkersRem,
								currState.maxNumOfDragonglass,currState.numOfDragonglassUsed,currState.maxNumOfDragonglass,currState.isGoal,leftGrid),
								((WesterosNode)oriNode),'L',((WesterosNode)oriNode).depth + 1,((WesterosNode)oriNode).pathCost + 2);
						if(!visitedAncestor(n))
							res.add(n);
					}
					else
					{
						//If the cell is not a dragonstone, then it is checked whether it is a whitewalker or not.
						//If it is, and Jon has dragonglass, then the whitewalker is killed, and the remaining cells
						//around Jon are checked incase they also have whitewalkers in them, if they do, those are also
						//killed. A counter is also created to count the number of whitewalkers that Jon has killed.
						//In the end a new node is created and updated with the relevant information
						//(including position, number of dragonglass, depth, and path cost)
						if(currState.grid[currState.i][currState.j - 1] == 'W')
						{
							if(currState.numOfDragonglass > 0)
							{
								leftGrid[currState.i][currState.j - 1] = ' ';

								int numKilled = 1;

								if(currState.j < currState.grid[0].length - 1 && leftGrid[currState.i][currState.j + 1] == 'W')
								{
									leftGrid[currState.i][currState.j + 1] = ' ';
									numKilled++;
								}

								if(currState.i > 0 && leftGrid[currState.i - 1][currState.j] == 'W')
								{
									leftGrid[currState.i - 1][currState.j] = ' ';
									numKilled++;
								}

								if(currState.i < currState.grid.length - 1 && leftGrid[currState.i + 1][currState.j] == 'W')
								{
									leftGrid[currState.i + 1][currState.j] = ' ';
									numKilled++;
								}

								//If the number of the number of whitewalkers remaining minus the number killed is 0
								//then in the new state the goal is set to true.
								if(currState.numOfWhitewalkersRem - numKilled == 0)
								{
									WesterosNode n= new WesterosNode(new WesterosState(currState.i,currState.j,currState.numOfWhitewalkersRem - numKilled,
											currState.numOfDragonglass - 1, currState.numOfDragonglassUsed + 1, currState.maxNumOfDragonglass, true,leftGrid),
											(WesterosNode)oriNode,'K',((WesterosNode)oriNode).depth + 1,((WesterosNode)oriNode).pathCost + 1);
									if(!visitedAncestor(n))
										res.add(n);
								}
								else
								{
									res.add(new WesterosNode(new WesterosState(currState.i,currState.j,currState.numOfWhitewalkersRem - numKilled,
											currState.numOfDragonglass - 1, currState.numOfDragonglassUsed + 1, currState.maxNumOfDragonglass, currState.isGoal,leftGrid),
											(WesterosNode)oriNode,'K',((WesterosNode)oriNode).depth + 1,((WesterosNode)oriNode).pathCost + 1));
								}
							}
						}
						else
						{
							//If it is none of the previous scenarios, then it must simply be an empty cell that Jon can freely move to.
							WesterosNode n= new WesterosNode(new WesterosState(currState.i,currState.j - 1,currState.numOfWhitewalkersRem,
									currState.numOfDragonglass,currState.numOfDragonglassUsed,currState.maxNumOfDragonglass,currState.isGoal,leftGrid),
									(WesterosNode)oriNode,'L',((WesterosNode)oriNode).depth + 1, ((WesterosNode)oriNode).pathCost + 3);
							if(!visitedAncestor(n))
								res.add(n);
						}
					}
				}
			}
		}


		//To go right
		if(goRight)
		{
			//Check if the cell to the right is available and not out of bounds
			if(currState.j < currState.grid[0].length - 1)
			{			
				//Create a new grid and copy the old grid in it
				char[][] rightGrid = new char[currState.grid.length][currState.grid[0].length];

				for(int i = 0; i < currState.grid.length; i++)
					for(int j = 0; j < currState.grid[0].length; j++)
						rightGrid[i][j] = currState.grid[i][j];
				
				//Check if the cell that could be moved to (the one to the right) is not an obstacle
				if(currState.grid[currState.i][currState.j + 1] != 'O')
				{
					//Check if the cell is a dragonstone cell, and if so, create a new node and update it with the relevant information
					//(including position, number of dragonglass, depth, and path cost)
					if(currState.grid[currState.i][currState.j + 1] == 'D')
					{
						WesterosNode n= new WesterosNode(new WesterosState(currState.i,currState.j + 1,currState.numOfWhitewalkersRem,
								currState.maxNumOfDragonglass,currState.numOfDragonglassUsed,currState.maxNumOfDragonglass,currState.isGoal,rightGrid),
								((WesterosNode)oriNode),'R',((WesterosNode)oriNode).depth + 1,((WesterosNode)oriNode).pathCost + 2);
						if(!visitedAncestor(n))
							res.add(n);
					}
					else
					{
						//If the cell is not a dragonstone, then it is checked whether it is a whitewalker or not.
						//If it is, and Jon has dragonglass, then the whitewalker is killed, and the remaining cells
						//around Jon are checked incase they also have whitewalkers in them, if they do, those are also
						//killed. A counter is also created to count the number of whitewalkers that Jon has killed.
						//In the end a new node is created and updated with the relevant information
						//(including position, number of dragonglass, depth, and path cost)
						if(currState.grid[currState.i][currState.j + 1] == 'W')
						{
							if(currState.numOfDragonglass > 0)
							{
								rightGrid[currState.i][currState.j + 1] = ' ';

								int numKilled = 1;

								if(currState.j > 0 && rightGrid[currState.i][currState.j - 1] == 'W')
								{
									rightGrid[currState.i][currState.j - 1] = ' ';
									numKilled++;
								}

								if(currState.i > 0 && rightGrid[currState.i - 1][currState.j] == 'W')
								{
									rightGrid[currState.i - 1][currState.j] = ' ';
									numKilled++;
								}

								if(currState.i < currState.grid.length - 1 && rightGrid[currState.i + 1][currState.j] == 'W')
								{
									rightGrid[currState.i + 1][currState.j] = ' ';
									numKilled++;
								}

								//If the number of the number of whitewalkers remaining minus the number killed is 0
								//then in the new state the goal is set to true.
								if(currState.numOfWhitewalkersRem - numKilled == 0)
								{
									WesterosNode n=new WesterosNode(new WesterosState(currState.i,currState.j,currState.numOfWhitewalkersRem - numKilled,
											currState.numOfDragonglass - 1, currState.numOfDragonglassUsed + 1, currState.maxNumOfDragonglass, true,rightGrid),
											(WesterosNode)oriNode,'K',((WesterosNode)oriNode).depth + 1,((WesterosNode)oriNode).pathCost + 1);
									if(!visitedAncestor(n))
										res.add(n);
								}
								else
								{
									res.add(new WesterosNode(new WesterosState(currState.i,currState.j,currState.numOfWhitewalkersRem - numKilled,
											currState.numOfDragonglass - 1, currState.numOfDragonglassUsed + 1, currState.maxNumOfDragonglass, currState.isGoal,rightGrid),
											(WesterosNode)oriNode,'K',((WesterosNode)oriNode).depth + 1,((WesterosNode)oriNode).pathCost + 1));
								}
							}
						}
						else
						{
							//If it is none of the previous scenarios, then it must simply be an empty cell that Jon can freely move to.
							WesterosNode n=new WesterosNode(new WesterosState(currState.i,currState.j + 1,currState.numOfWhitewalkersRem,
									currState.numOfDragonglass,currState.numOfDragonglassUsed,currState.maxNumOfDragonglass,currState.isGoal,rightGrid),
									(WesterosNode)oriNode,'R',((WesterosNode)oriNode).depth + 1, ((WesterosNode)oriNode).pathCost + 3);
							if(!visitedAncestor(n))
								res.add(n);
						}
					}
				}
			}
		}


		//To go up
		if(goUp)
		{
			//Check if the cell on top is available and not out of bounds
			if(currState.i > 0)
			{			
				//Create a new grid and copy the old grid in it
				char[][] upGrid = new char[currState.grid.length][currState.grid[0].length];

				for(int i = 0; i < currState.grid.length; i++)
					for(int j = 0; j < currState.grid[0].length; j++)
						upGrid[i][j] = currState.grid[i][j];
				
				//Check if the cell that could be moved to (the one on top) is not an obstacle
				if(currState.grid[currState.i - 1][currState.j] != 'O')
				{
					//Check if the cell is a dragonstone cell, and if so, create a new node and update it with the relevant information
					//(including position, number of dragonglass, depth, and path cost)
					if(currState.grid[currState.i - 1][currState.j] == 'D')
					{
						WesterosNode n= new WesterosNode(new WesterosState(currState.i - 1,currState.j,currState.numOfWhitewalkersRem,
								currState.maxNumOfDragonglass,currState.numOfDragonglassUsed,currState.maxNumOfDragonglass,currState.isGoal,upGrid),
								((WesterosNode)oriNode),'U',((WesterosNode)oriNode).depth + 1,((WesterosNode)oriNode).pathCost + 2);

						if(!visitedAncestor(n))
							res.add(n);
					}
					else
					{
						//If the cell is not a dragonstone, then it is checked whether it is a whitewalker or not.
						//If it is, and Jon has dragonglass, then the whitewalker is killed, and the remaining cells
						//around Jon are checked incase they also have whitewalkers in them, if they do, those are also
						//killed. A counter is also created to count the number of whitewalkers that Jon has killed.
						//In the end a new node is created and updated with the relevant information
						//(including position, number of dragonglass, depth, and path cost)
						if(currState.grid[currState.i - 1][currState.j] == 'W')
						{
							if(currState.numOfDragonglass > 0)
							{
								upGrid[currState.i - 1][currState.j] = ' ';

								int numKilled = 1;

								if(currState.j > 0 && upGrid[currState.i][currState.j - 1] == 'W')
								{
									upGrid[currState.i][currState.j - 1] = ' ';
									numKilled++;
								}

								if(currState.j < currState.grid[0].length - 1 && upGrid[currState.i][currState.j + 1] == 'W')
								{
									upGrid[currState.i][currState.j + 1] = ' ';
									numKilled++;
								}

								if(currState.i < currState.grid.length - 1 && upGrid[currState.i + 1][currState.j] == 'W')
								{
									upGrid[currState.i + 1][currState.j] = ' ';
									numKilled++;
								}

								//If the number of the number of whitewalkers remaining minus the number killed is 0
								//then in the new state the goal is set to true.
								if(currState.numOfWhitewalkersRem - numKilled == 0)
								{
									WesterosNode n= new WesterosNode(new WesterosState(currState.i,currState.j,currState.numOfWhitewalkersRem - numKilled,
											currState.numOfDragonglass - 1, currState.numOfDragonglassUsed + 1, currState.maxNumOfDragonglass, true,upGrid),
											(WesterosNode)oriNode,'K',((WesterosNode)oriNode).depth + 1,((WesterosNode)oriNode).pathCost + 1);
									if(!visitedAncestor(n))
										res.add(n);
								}
								else
								{
									res.add(new WesterosNode(new WesterosState(currState.i,currState.j,currState.numOfWhitewalkersRem - numKilled,
											currState.numOfDragonglass - 1, currState.numOfDragonglassUsed + 1, currState.maxNumOfDragonglass, currState.isGoal,upGrid),
											(WesterosNode)oriNode,'K',((WesterosNode)oriNode).depth + 1,((WesterosNode)oriNode).pathCost + 1));
								}
							}
						}
						else
						{
							//If it is none of the previous scenarios, then it must simply be an empty cell that Jon can freely move to.
							WesterosNode n = new WesterosNode(new WesterosState(currState.i - 1,currState.j,currState.numOfWhitewalkersRem,
									currState.numOfDragonglass,currState.numOfDragonglassUsed,currState.maxNumOfDragonglass,currState.isGoal,upGrid),
									(WesterosNode)oriNode,'U',((WesterosNode)oriNode).depth + 1, ((WesterosNode)oriNode).pathCost + 3);
							if(!visitedAncestor(n))
								res.add(n);
						}
					}
				}
			}
		}


		//To go down
		if(goDown)
		{
			//Check if the cell to the bottom is available and not out of bounds
			if(currState.i < currState.grid.length - 1)
			{
				//Create a new grid and copy the old grid in it
				char[][] downGrid = new char[currState.grid.length][currState.grid[0].length];

				for(int i = 0; i < currState.grid.length; i++)
					for(int j = 0; j < currState.grid[0].length; j++)
						downGrid[i][j] = currState.grid[i][j];
				
				//Check if the cell that could be moved to (the one to the bottom) is not an obstacle
				if(currState.grid[currState.i + 1][currState.j] != 'O')
				{
					//Check if the cell is a dragonstone cell, and if so, create a new node and update it with the relevant information
					//(including position, number of dragonglass, depth, and path cost)
					if(currState.grid[currState.i + 1][currState.j] == 'D')
					{
						WesterosNode n =new WesterosNode(new WesterosState(currState.i + 1,currState.j,currState.numOfWhitewalkersRem,
								currState.maxNumOfDragonglass,currState.numOfDragonglassUsed,currState.maxNumOfDragonglass,currState.isGoal,downGrid),
								((WesterosNode)oriNode),'D',((WesterosNode)oriNode).depth + 1,((WesterosNode)oriNode).pathCost + 2);
						if(!visitedAncestor(n))
							res.add(n);
					}
					else
					{
						//If the cell is not a dragonstone, then it is checked whether it is a whitewalker or not.
						//If it is, and Jon has dragonglass, then the whitewalker is killed, and the remaining cells
						//around Jon are checked incase they also have whitewalkers in them, if they do, those are also
						//killed. A counter is also created to count the number of whitewalkers that Jon has killed.
						//In the end a new node is created and updated with the relevant information
						//(including position, number of dragonglass, depth, and path cost)
						if(currState.grid[currState.i + 1][currState.j] == 'W')
						{
							if(currState.numOfDragonglass > 0)
							{	
								downGrid[currState.i + 1][currState.j] = ' ';

								int numKilled = 1;

								if(currState.j > 0 && downGrid[currState.i][currState.j - 1] == 'W')
								{
									downGrid[currState.i][currState.j - 1] = ' ';
									numKilled++;
								}

								if(currState.j < currState.grid[0].length - 1 && downGrid[currState.i][currState.j + 1] == 'W')
								{
									downGrid[currState.i][currState.j + 1] = ' ';
									numKilled++;
								}

								if(currState.i > 0 && downGrid[currState.i - 1][currState.j] == 'W')
								{
									downGrid[currState.i - 1][currState.j] = ' ';
									numKilled++;
								}
								
								//If the number of the number of whitewalkers remaining minus the number killed is 0
								//then in the new state the goal is set to true.
								if(currState.numOfWhitewalkersRem - numKilled == 0)
								{
									WesterosNode n =new WesterosNode(new WesterosState(currState.i,currState.j,currState.numOfWhitewalkersRem - numKilled,
											currState.numOfDragonglass - 1, currState.numOfDragonglassUsed + 1, currState.maxNumOfDragonglass, true,downGrid),
											(WesterosNode)oriNode,'K',((WesterosNode)oriNode).depth + 1,((WesterosNode)oriNode).pathCost + 1);

									if(!visitedAncestor(n))
										res.add(n);
								}
								else
								{
									res.add(new WesterosNode(new WesterosState(currState.i,currState.j,currState.numOfWhitewalkersRem - numKilled,
											currState.numOfDragonglass - 1, currState.numOfDragonglassUsed + 1, currState.maxNumOfDragonglass, currState.isGoal,downGrid),
											(WesterosNode)oriNode,'K',((WesterosNode)oriNode).depth + 1,((WesterosNode)oriNode).pathCost + 1));
								}
							}
						}
						else
						{
							//If it is none of the previous scenarios, then it must simply be an empty cell that Jon can freely move to.
							WesterosNode n = new WesterosNode(new WesterosState(currState.i + 1,currState.j,currState.numOfWhitewalkersRem,
									currState.numOfDragonglass,currState.numOfDragonglassUsed,currState.maxNumOfDragonglass,currState.isGoal,downGrid),
									(WesterosNode)oriNode,'D',((WesterosNode)oriNode).depth + 1, ((WesterosNode)oriNode).pathCost + 3);
							if(!visitedAncestor(n))
								res.add(n);
						}
					}
				}
			}
		}

		return res;
	}

	public static boolean visitedAncestor(WesterosNode n){
		WesterosState currState= (WesterosState) n.state;
		WesterosNode nodeParent = n.parent;
		while(nodeParent != null)
		{
			if(currState.areEqual((WesterosState)nodeParent.state))
			{
				return true;
			}

			nodeParent = nodeParent.parent;
		}
		return false;
	}


	public int compareTo(SearchTreeNode other)
	{
		WesterosNode oW = (WesterosNode) other;

		if(oW.QingFun.substring(0,2).equals("GR"))
			return this.hn - oW.hn;

		return (this.pathCost + this.hn) - (oW.pathCost + oW.hn);
	}


	public void heuristicOne()
	{
		WesterosNode aW = (WesterosNode)this;
		WesterosState aS = (WesterosState)aW.state;
		char[][] currGrid = ((WesterosState)aW.state).grid;
		int distance = 0;

		for(int i = 0; i < currGrid.length; i++)
		{
			int currDist = 0;

			for(int j = 0; j < currGrid[0].length; j++)
			{
				if(currGrid[i][j] == 'W')
				{
					currDist = (aS.i - i) + (aS.j - j);

					if(currDist > distance)
						distance = currDist;
				}
			}
		}

		this.hn = distance;
	}

	public void heuristicTwo()
	{
		WesterosNode aW = (WesterosNode)this;
		WesterosState aS = (WesterosState)aW.state;

		int numOfRemWW = (int)(aS.numOfWhitewalkersRem/3);

		//This line checks whether the amount of dragonstone remaining is enough to kill the remaining whitewalkers in groups of 3, and this
		//is to avoid overestimation, since we are thinking of it as a best case scenario where all remaining whitewalkers are put in sets of 3.
		//So after checking whether or not the remaining amount of dragonstone is enough to kill the number of remaining whitewalkers, 
		//if it is true then the function will return 0, meaning the number of whitewalkers remaining is enough, otherwise, I will at least
		//need to make one more trip to the dragonstone.
		int remainingDG = (aS.numOfDragonglass - numOfRemWW >= 0) ? 0 :  (int)Math.ceil((double)(numOfRemWW / aS.maxNumOfDragonglass));

		this.hn = remainingDG;
	}



}
