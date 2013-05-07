/* 

   Bellman-Ford shortest path algorithm.
   
   Solves the shortest  pathS problem [sic] for  weighted, directed graph G =  (V, E) with
   non-negative or  negative edge  weights. That is,  the algorithm computes  the shortest
   path from a single source to all the other vertices in G.
   
   The algorithm  returns (False, NULL) in the  case that there is  a negative-weight cyle
   that  is  reachable  from the  source.  If  not,  it  returns (TRUE,  {shortest  paths,
   weights}).

   Author: Sonny Rajagopalan
   Date: 05/05/2013.

*/
import java.util.*;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.lang.UnsupportedOperationException;
import java.lang.ClassCastException;
import java.lang.NullPointerException;

class BellmanFord
{
    private static final int HORRENDOUSLY_LARGE_NUMBER = 999;

    public static void readInGraph (String graphFile, 
				    Map <String, HashMap<String, Integer>> E, 
				    Map <String, Integer> shortestPathEstimate)
    {
	try
	    {
		String graphLine;
		BufferedReader buf = new BufferedReader (new FileReader (graphFile));
		int lineNumber = 0;
		
		while ((graphLine = buf.readLine ()) != null)
		    {
			lineNumber ++;
			if (lineNumber == 1) // Line containing the vertices
			    {
				String[] tokens = graphLine.split (",");
				System.out.println ("Vertices : " + graphLine);
				for (int index = 0; index < tokens.length; index++)
				    {
					if (index == 0)
					    {
						System.out.println ("First vertex (" + tokens [index] + ") is assumed to be the source (without loss of generality)");
						shortestPathEstimate.put (tokens [index], 0);
						System.out.println ("SPE (" + tokens [index] + ") = " + shortestPathEstimate.get (tokens [index]));
					    }
					else
					    {
						shortestPathEstimate.put (tokens [index], HORRENDOUSLY_LARGE_NUMBER);
					    }
					
					HashMap <String, Integer> adjacentNodes = new HashMap <String, Integer> ();
					E.put (tokens [index], adjacentNodes);
				    }
			    }
			else
			    {
				String[] tokens = graphLine.split (",");
				HashMap <String, Integer> adjForVertex1 = E.get (tokens [0]);
				// HashMap <String, Integer> adjForVertex2 = E.get (tokens [1]);

				adjForVertex1.put (tokens [1], Integer.parseInt (tokens [2]));
				// adjForVertex2.put (tokens [0], Integer.parseInt (tokens [2]));

				System.out.println ("Edge: " + graphLine + "; weight = " + tokens [2] + "; |Q| = " + E.size ());
			    }
		    }
	    }
	catch (IOException e)
	    {
		System.out.println ("IOException in readInGraph ()...");
	    }

    } // End of readInGraph ()

    public static void walkTheAdjacencyMatrix (Map <String, HashMap <String, Integer>> E)
    {
	for (Map.Entry <String, HashMap <String, Integer>> row : E.entrySet ())
	    {
		System.out.println ("For vertex [" + row.getKey () + "], the adjacent vertices, and the weights are...");
		for (Map.Entry <String, Integer> column : row.getValue ().entrySet ())
		    {
			System.out.println ("\\---Vertex " + column.getKey () + ", weight = " + column.getValue ());
		    }
	    }
    } // End of walkTheAdjacencyMatrix ()

    public static void main (String [] argv)
    {
	Map<String, HashMap<String, Integer>> AdjacencyMatrix = new HashMap<String, HashMap<String, Integer>> ();
	Map <String, Integer> shortestPathEstimate = new HashMap <String, Integer> ();
	Map <String, String> parent = new HashMap <String, String> ();

	readInGraph (argv[0], AdjacencyMatrix, shortestPathEstimate);
	//walkTheAdjacencyMatrix (AdjacencyMatrix);
	//walkThePriorityQueue (Q);
	
	// First relax all edges 1 to |V| times
	for (int i = 1; i < AdjacencyMatrix.size () - 1; i++)
	    {
		for (Map.Entry <String, HashMap <String, Integer>> adjRow : AdjacencyMatrix.entrySet ())
		    {
			String u = adjRow.getKey ();

			for (Map.Entry <String, Integer> adjRowEntry : adjRow.getValue ().entrySet ())
			    {
				String v = adjRowEntry.getKey ();
				int w    = adjRowEntry.getValue ();
				
				System.out.print ("SPE (" + u + ")  = " + shortestPathEstimate.get (u));
				System.out.print ("; SPE (" + v + ")  = " + shortestPathEstimate.get (v));
				System.out.println ("; w  = " + w);

				if ((shortestPathEstimate.get (u) + w) < shortestPathEstimate.get (v))
				    {
					shortestPathEstimate.put (v, (w + shortestPathEstimate.get (u)));
					parent.put (v, u);
					System.out.println ("Relaxed SPE (" + v + ") == " + shortestPathEstimate.get (v) + ", set parent = " + u);
				    }
			    }
		    }
		
	    }// End of for loop for relaxing edges repeatedly

	// Second: decide if there are negative-weight cycles:
	for (Map.Entry <String, HashMap <String, Integer>> adjRow : AdjacencyMatrix.entrySet ())
	    {
		String u = adjRow.getKey ();
		
		for (Map.Entry <String, Integer> adjRowEntry : adjRow.getValue ().entrySet ())
		    {
			String v = adjRowEntry.getKey ();
			int w    = adjRowEntry.getValue ();
			
			System.out.print ("SPE (" + u + ")  = " + shortestPathEstimate.get (u));
			System.out.print ("; SPE (" + v + ")  = " + shortestPathEstimate.get (v));
			System.out.println ("; w  = " + w);
			
			if ((shortestPathEstimate.get (u) + w) < shortestPathEstimate.get (v))
			    {
				System.out.println ("Graph contains negative-weight edges!");
				System.exit (0);
			    }
		    }
	    }

	System.out.println ("Relaxed shortest paths from source are:");

	for (Map.Entry <String, Integer> node : shortestPathEstimate.entrySet ())
	    {
		System.out.println ("Vertex " + node.getKey () + ", SPE = " + node.getValue ());
	    }


    } // End of main ()
}