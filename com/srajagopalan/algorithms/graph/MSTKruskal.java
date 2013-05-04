/*
  An implementation of Kruskals algorithm in Java.
  Use the ordered set (TreeSet) for log (n) operations.

  Author: Sonny Rajagopalan
  Date 04/20/2013.

  ATTRIBUTION:
  ============

  Some parts of code is adapted from:
  http://krishami.com/2011/12/implementation-of-kruskals-algorithm-in-java/

  The code has been rewritten by the author, to use more CLR language, and also read 
  graph input from file.

 */

import java.io.*;
import java.util.StringTokenizer;
import java.util.TreeSet; // Where the input tree edges are going to be stored (ordered set)
import java.util.HashSet; // Where the two working forests are going to be stored
import java.util.Vector;  // Where the "v tree"  and "u tree" are held.

class Edge implements Comparable <Edge>
{
    String u;
    String v;
    int w;
    
    public Edge (String u, String v, int w)
	{
	    this.u = u;
	    this.v = v;
	    this.w = w;
	}

    public String getU ()
    {
	return u;
    }
    
    public String getV ()
    {
	return v;
    }

    public int getWeight ()
    {
	return w;
    }

    public int compareTo (Edge anotherEdge)
    {
	if (this.w < anotherEdge.w)
	    {
		return -1;
	    }
	else
	    {
		return 1;
	    }
    }
}

class MSTKruskal
{
    private static void makeSet (Vector <HashSet <String>> workingForest, String nodeID)
    {
	HashSet <String> nodeV = new HashSet <String> ();
	nodeV.add (nodeID);
	workingForest.add (nodeV);
    }

    private static HashSet <String> findSet (Vector <HashSet <String>> workingForest, String nodeID)
    {
	for (HashSet <String> tree: workingForest)
	    {
		if (tree.contains (nodeID))
		    {
			return tree;
		    }
	    }
	return null;
    }

    private static void readInGraph (String graphFile, Vector <HashSet <String>> workingForest, TreeSet <Edge> orderedEdges)
    {
	try
	    {
		FileReader inputGraph = new FileReader (graphFile);
		BufferedReader buf = new BufferedReader (inputGraph);
		String lineOfInput = buf.readLine ();
		int lineNumber = 0;

		while (lineOfInput != null)
		    {
			lineNumber ++;

			StringTokenizer tok = new StringTokenizer (lineOfInput, ",");
			//System.out.println ("Line = " + lineNumber);
			if (lineNumber == 1) // Line # 1 is an enumeration of the vertices
			    {
				System.out.println (lineOfInput);
				while (tok.hasMoreTokens ())
				    {
					String v = tok.nextToken ();
					// The working forest is initially just the collection of individual nodes for trees
					//System.out.println ("MAKE-SET (\"" + v + "\")");
					makeSet (workingForest, v);
				    }
			    }
			else
			    {
				String u = tok.nextToken ();
				String v = tok.nextToken ();
				String wt = tok.nextToken ();
				int w = Integer.valueOf (wt).intValue ();
				Edge e = new Edge (u, v, w);
				
				orderedEdges.add (e);
			    }
			lineOfInput = buf.readLine ();
		    }
	    }
	catch (IOException e)
	    {
	    }
    }

    public static void main (String[] args)
    {
	TreeSet <Edge> orderedEdges = new TreeSet <Edge> ();
	Vector <HashSet <String> > workingForest = new Vector <HashSet <String> > ();
	TreeSet <Edge> outputMst = new TreeSet <Edge> (); // Output MST M

	/*
	  0. Read in the G = (V, E)
	  1. Create a forest F with each vertex in the graph a separate tree.	  
         	  (Created in the makeSet () operation called by readInGraph ().)
	  2. Sort the set of edges E in increasing order of weight into a set S.
	          (Done in step 0 above: the act of creating the TreeSet orderedEdged)
	*/
	readInGraph (args[0], workingForest, orderedEdges);

	/*
	  3. outputMst = null; // The MST that will be output
	  (Initialized above)
	*/

	/*
	  4. Walk the ordered edges set from min weight to largest weight:
	  5.    If the edge e connects two unconnected trees, then M = M union e
	  6.    Else, next
	*/
	
	for (Edge e : orderedEdges)
	    {
		String u = e.getU ();
		String v = e.getV ();
		int w    = e.getWeight ();
		HashSet <String> treeA = findSet (workingForest, u);
		HashSet <String> treeB = findSet (workingForest, v);

		System.out.print ("Consider edge (" + u + ", " + v + ", " + w +"): ");
		System.out.print ("treeA = " + treeA + ", treeB = " + treeB);

		if (treeA != treeB)
		    {
			/*
			  See CLR definition of UNION (x, y)
			*/
			System.out.println (": Perform UNION (" + u + ", " + v + ")");
			HashSet <String> newTree = new HashSet <String> ();
			newTree.add (u);
			newTree.add (v);
			newTree.addAll (treeA);
			newTree.addAll (treeB);
			workingForest.remove (treeA);
			workingForest.remove (treeB);
			workingForest.add (newTree);
			outputMst.add (e);
		    }
		else
		    {
			System.out.println (": Cannot UNION because treeA, treeB are the same!");
		    }
	    }

	/*
	  7. Output outputMst.
	*/
	int minimumWeight = 0;

	for (Edge e : outputMst)
	    {
		minimumWeight += e.getWeight ();
		System.out.println ("(" + e.getU () + ", " + e.getV () + ") => " + e.getWeight ());
	    }

	System.out.println ("Size of outputMst = " + outputMst.size () + ", and the minimum weight is " + minimumWeight);
    }
}