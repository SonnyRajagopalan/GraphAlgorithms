/* 

   Dijkstra's shortest path algorithm.
   
   Solves the  shortest pathS problem [sic]  for weighted, directed  graph G = (V,  E) with
   non-negative edge  weights. That  is, the  algorithm computes the  shortest path  from a
   single source to all the other vertices in G.
   
   Author: Sonny Rajagopalan
   Date: 05/03/2013.

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

import com.srajagopalan.algorithms.dataStructures.graph.Vertex;

class ShortestPathDijkstra
{
    private static final int HORRENDOUSLY_LARGE_NUMBER = 999;

    public static void readInGraph (String graphFile, 
				    PriorityQueue <Vertex> Q, 
				    Map <String, HashMap<String, Integer>> E, 
				    Map <String, Integer> shortestPathEstimate, 
				    HashSet <String> unvisitedNodes)
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
					try
					    {
						if (index == 0)
						    {
							System.out.println ("First vertex is assumed to be the source (without loss of generality)");
							Q.add (new Vertex (tokens [index], 0, false));
						    }
						else
						    {
							Q.add (new Vertex (tokens [index], HORRENDOUSLY_LARGE_NUMBER, false));
						    }

						unvisitedNodes.add (tokens [index]);
					    }
					catch (ClassCastException e)
					    {
						System.out.println ("ClassCastException trying to add into priority queue");
					    }
					catch (NullPointerException e)
					    {
						System.out.println ("null pointer exception trying to add into priority queue");
					    }
					shortestPathEstimate.put (tokens [index], HORRENDOUSLY_LARGE_NUMBER);
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

    public static void walkThePriorityQueue (PriorityQueue <Vertex> Q)
    {
	Vertex u = Q.poll ();
	while (u != null)
	    {
		//System.out.println ("[" + Q.size () + "] Vertex = " + u.getID () + "; key = " + u.getKey ());
		u = Q.poll ();
	    }
    } // End of walkThePriorityQueue ()

    public static void main (String [] argv)
    {
	PriorityQueue<Vertex> Q = new PriorityQueue <Vertex> ();
	Map<String, HashMap<String, Integer>> AdjacencyMatrix = new HashMap<String, HashMap<String, Integer>> ();
	Map <String, Integer> shortestPathEstimate = new HashMap <String, Integer> ();
	Map <String, String> parent = new HashMap <String, String> ();
	HashSet<String> VNew = new HashSet<String> (); // The tree that will grow to be an MST
	HashMap<String, Integer> ENew = new HashMap<String, Integer> (); // The edges in the MST
	HashSet <String> unvisitedNodes = new HashSet <String> ();

	readInGraph (argv[0], Q, AdjacencyMatrix, shortestPathEstimate, unvisitedNodes);
	//walkTheAdjacencyMatrix (AdjacencyMatrix);
	//walkThePriorityQueue (Q);

	System.out.println ("Get Q.root...");
	Vertex u = Q.poll ();
	// Set the root's parent:
	parent.put (u.getID (), "");
	System.out.println ("After Q.root...");

	while (u != null)
	    {
		String path="";
		int spe2U = u.getKey ();
		System.out.println ("\n\n\n[" + Q.size () + "] Getting adjacency for vertex " + u.getID () + " in heap with shortestPathEstimate = " + spe2U);
		HashMap <String, Integer> adjOfU = AdjacencyMatrix.get (u.getID ());
		System.out.println ("\\-----> adjacency list for vertex " + u.getID () + " (" + adjOfU.size () + " elements)");


		/*       
			                  |----|
		                     /----| v1 |
		          wU2V----> /     |----|
		                   /
		  |---------------|       |----|
		  |currentNode (u)|-------| v2 |
		  |---------------|       |----|
		                   \
                                    \     |----|
                                     \----| v3 |
                                          |----|

		  spe2U = top.getKey ();
		  foreach v in Adj (u):
                     if (v.getKey () > (spe2U + wU2V))
		         spe2V = spe2U + wU2V;
			 set key (V) = spe2V
		 */        

		for (Map.Entry<String, Integer> entry: adjOfU.entrySet ())
		    {
			String vID = entry.getKey ();
			int wU2V = entry.getValue ();
			Vertex v = new Vertex (vID, 0, false);

			System.out.println ("Trying vertex " + vID);
			boolean status = Q.remove (v);  // Currently too expensive : O (n) worst case!

			if (status)
			    {
				Vertex vv = new Vertex (vID);
				int spe2V = shortestPathEstimate.get (vID);				

				System.out.println ("|Q| == " + Q.size () + "; Q contains " + vID + ": spe2V == " + spe2V + "; spe2U = " + spe2U + " and wU2V == " + wU2V);

				if (spe2V > (wU2V + spe2U))
				    {
					int newSPEForV = wU2V + spe2U;
					vv.setKey (newSPEForV); // update the SPE
					vv.setProcessed (false);
					shortestPathEstimate.put (vID, newSPEForV);
					parent.put (vID, u.getID ());

					try
					    {
						Q.add (vv); // This action re-heapifies at O (lg V) cost.
					    }
					catch (ClassCastException e)
					    {
						System.out.println ("ClassCastException thrown while trying to add new shortestPathEstimate to PQ");
					    }
					catch (NullPointerException e)
					    {
						System.out.println ("NullPointerException thrown while trying to add new shortestPathEstimate to PQ");
					    }
					System.out.print   ("\\-----> Pushed neighbor " + vID + ", shortestPathEstimate = " + wU2V + " to Q (total SPE = ");
					System.out.println (newSPEForV + ") |Q| == " + Q.size ());
				    }
				else
				    {
					System.out.println ("\\-----> Discard adjacent vertex " + vID + " (spe2V == " + spe2V + ")");
					vv.setKey (spe2V);
					vv.setProcessed (false);
					Q.add (vv);
				    }
			    } // If status == true (i.e., v exists in unvisted)
			else
			    {
				System.out.println ("\\-----> Discard adjacent vertex " + vID + ": already visited: not considered");
			    }
		    } // End of for ()

		VNew.add (u.getID ());
		unvisitedNodes.remove (u.getID ());

		if (VNew.size () == 1)
		    {
			System.out.println ("Added first vertex " + u.getID () + " (SPE = 0); |VNew| = 1"  + "; |Q| = " + Q.size ());
		    }
		else
		    {
			path = parent.get (u.getID ()) + u.getID ();
			ENew.put (path, u.getKey ());
			System.out.println ("Added edge " + path + " of SPE = " + u.getKey () + "; |VNew| = " + VNew.size () + "; |Q| = " + Q.size ());
		    }

		u = Q.poll ();
	    } // End of while ()
    } // End of main ()
}