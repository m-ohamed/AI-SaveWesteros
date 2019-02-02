import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;

public abstract class GenericSearchProblem
{
	static Deque<WesterosNode> queue = new LinkedList<WesterosNode>();
	static PriorityQueue<WesterosNode> pQueue = new PriorityQueue<WesterosNode>();

	public String[] search(char[][] grid, String strategy, boolean visualize)
	{
		String[] res = new String[3];
		SaveWesteros newSaveWesteros = new SaveWesteros();

		switch(strategy.substring(0, 2))
		{
		case "BF": res = genericSearch(newSaveWesteros, strategy); break;
		case "DF": res = genericSearch(newSaveWesteros, strategy); break;
		case "ID": res = genericSearch(newSaveWesteros, strategy); break;
		case "UC": res = genericSearch(newSaveWesteros, strategy); break;
		case "GR": res = genericSearch(newSaveWesteros, strategy); break;
		case "AS": res = genericSearch(newSaveWesteros, strategy); break;
		default: break;
		}

		//Visualize
		if(visualize)
		{
			String[] vis = res[0].split(" ");
			int x = 3, y = 3;

			SaveWesteros.printGrid(grid);
			grid[grid.length - 1][grid[0].length - 1] = ' ';

			for(int i = 0; i < vis.length; i++)
			{
				System.out.println(" - - - - - - ");
				switch(vis[i])
				{
				case "L": 
					grid[x][y] = ' ';
					y -= 1; 
					grid[x][y] = 'J';
					break;
				
				case "R": 
					grid[x][y] = ' ';
					y += 1; 
					grid[x][y] = 'J'; 
					break;
				
				case "U": 
					grid[x][y] = ' '; 
					x -= 1; 
					grid[x][y] = 'J'; 
					break;
				
				case "D": 
					grid[x][y] = ' '; 
					x += 1; 
					grid[x][y] = 'J'; 
					break;
				
				case "K": 
					grid[x][y] = 'J';
				
					if(x > 0)
						if(grid[x - 1][y] != 'O')
							grid[x - 1][y] = ' ';

					if(x < grid.length - 1)
						if(grid[x + 1][y] != 'O')
							grid[x + 1][y] = ' ';

					if(y > 0)
						if(grid[x][y - 1] != 'O')
							grid[x][y - 1] = ' ';
					
					if(y < grid[0].length - 1)
						if(grid[x][y + 1] != 'O')
							grid[x][y + 1] = ' ';
						
					break;
				}
				SaveWesteros.printGrid(grid);
			}
		}


		return res;
	}

	public String[] genericSearch(SaveWesteros problem, String QingFun)
	{
		String[] res = new String[3];
		char[] operators = {'L','R','U','D'};
		SearchTreeNode curr = null;
		boolean check = true;
		int counter = 0;
		boolean found = false;
		problem.root.QingFun = QingFun;
		int numOfNodes = 0;
		
		//This outer loop is used for the iterative deepening search, and its counter is set to 40 to avoid it looping forever.
		//Feel free to change the counter to whatever depth you want.
		while (!found && counter < 40)
		{
			numOfNodes = 0;
			counter++;
			curr = null;
			
			//This if statement is used to check which queue we'll use, whether it being the priority queue, or a normal double
			//ended queue.
			if(QingFun.substring(0,2).equals("UC") || QingFun.substring(0,2).equals("GR") || QingFun.substring(0,2).equals("AS"))
			{	
				//This if statement is used if the queuing function is greedy or A* and then calls the heuristic needed to update
				//its heuristic value.
				if((QingFun.substring(0,2).equals("GR") || QingFun.substring(0,2).equals("AS")) && QingFun.substring(2,3) == "1")
					problem.root.heuristicOne();
				else
					if((QingFun.substring(0,2).equals("GR") || QingFun.substring(0,2).equals("AS")) && QingFun.substring(2,3) == "2")
						problem.root.heuristicTwo();

				pQueue.add(problem.root);
			}
			else
			{
				queue.addLast(problem.root);
			}

			//This loops on the tree itself, while there are nodes being created and returned from the expands method
			//those nodes are added to the corresponding queue (according to the if statement explained earlier).
			//It will keep looping until there are no more nodes returning from the expands method, and the 
			//corresponding queue is empty, meaning no more nodes are available to test.
			check = true;
			while(check)
			{
				numOfNodes++;
				
				//This if statement dequeues the node according to the queuing function. 
				if(QingFun.substring(0,2).equals("UC") || QingFun.substring(0,2).equals("GR") || QingFun.substring(0,2).equals("AS"))
					curr = pQueue.remove();
				else
					curr = queue.removeFirst();

				if(((WesterosState)((WesterosNode)curr).state).isGoal) {
					found = true;
					break;
				}

				Queue<SearchTreeNode> list = curr.expands(curr,operators);
				
				//This basically loops over the result of the expands function, and adds each
				//node to the corresponding queue.
				while(!list.isEmpty())
				{
					switch(QingFun.substring(0, 2))
					{
					case "BF": queue.addLast((WesterosNode)list.remove()); break;
					case "DF": queue.addFirst((WesterosNode)list.remove()); break;
					case "ID": 
						WesterosNode currNode = (WesterosNode) list.remove();

						if(currNode.depth <= counter)
							queue.addFirst(currNode);

						break;
					case "UC": pQueue.add((WesterosNode)list.remove()); break;
					case "GR":
						WesterosNode currNode2 = (WesterosNode) list.remove();
						currNode2.QingFun = QingFun;

						if(QingFun.substring(2,3).equals("1"))
							currNode2.heuristicOne();
						else
							currNode2.heuristicTwo();

						pQueue.add(currNode2);


						break;
					case "AS":

						WesterosNode currNode3 = (WesterosNode) list.remove();
						currNode3.QingFun = QingFun;

						if(QingFun.substring(2,3).equals("1"))
							currNode3.heuristicOne();
						else
							currNode3.heuristicTwo();

						pQueue.add(currNode3);

						break;
					}
				}		
				
				//This if statement updates the flag according to the queuing function used.
				if(QingFun.substring(0,2).equals("UC") || QingFun.substring(0,2).equals("GR") || QingFun.substring(0,2).equals("AS"))
				{
					check = !pQueue.isEmpty();
				}
				else
				{
					check = !queue.isEmpty();
				}
			}

			if(!QingFun.equals("ID"))
			{
				break;
			}

		}

		res[2] = ""+numOfNodes;
		res[1] = ""+curr.pathCost;
		res[0] = "No solution";


		if(curr != null)
		{
			if(((WesterosState)((WesterosNode)curr).state).isGoal)
			{
				res[0] = "";
				while(curr != null)
				{
					if(((WesterosNode)curr).operator != ' ')
						res[0] = ((WesterosNode)curr).operator + " " + res[0];

					curr = ((WesterosNode)curr).parent;
				}
			}
		}

		return res;
	}
}