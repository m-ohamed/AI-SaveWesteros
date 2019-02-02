import java.util.Queue;


public abstract class SearchTreeNode implements Comparable<SearchTreeNode>
{
	char operator;
	int depth;
	int pathCost;
	
	public SearchTreeNode(char operator, int depth, int pathCost)
	{
		this.operator = operator;
		this.depth = depth;
		this.pathCost = pathCost;
	}
	
	public abstract Queue<SearchTreeNode> expands(SearchTreeNode oriNode, char[] operators);
	
	public abstract int compareTo(SearchTreeNode other);
	
	public abstract void heuristicOne();
	
	public abstract void heuristicTwo();
}

