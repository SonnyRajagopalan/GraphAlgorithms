/*
  An implementation of Prims algorithm in Java.

  Author: Sonny Rajagopalan
  Date 04/20/2013.

  The implementation tries quite hard to use CLR language, but
  some shortcuts were taken.

  Some inefficiencies are noted. First, a Fibonacci heap-based
  priority heap is better optimized for the use here--
  I instead use the java PriorityQueue. Second, I cannot change keys 
  in=place and heapify to the correct location, so I incur O (n) per
  node added into the VNew.

  There are some notes about the Fibonacci based implementation being very
  cumbersome and having a lot of overhead. I have not investigated it yet.

  The overall cost is thus :
  (1) O (V  lg (V)) for the |V| poll () calls that pops the minimum element from Q;
  (2) O (V *(V + lg (V))) for the |V| times an O (V + lg (V)) activity is incurred (removal (O (V)) + insertion (O (lg V)))
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

import com.srajagopalan.algorithms.dataStructures.graph.*;

		      
class MSTPrim
{
    private static final int HORRENDOUSLY_LARGE_NUMBER = 999;

    public static void readInGraph (String graphFile, 
				    PriorityQueue <Vertex> Q, 
				    Map <String, HashMap<String, Integer>> E, 
				    Map <String, Integer> key, 
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
						Q.add (new Vertex (tokens [index], HORRENDOUSLY_LARGE_NUMBER, false));
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
					key.put (tokens [index], HORRENDOUSLY_LARGE_NUMBER);
					HashMap <String, Integer> adjacentNodes = new HashMap <String, Integer> ();
					E.put (tokens [index], adjacentNodes);
				    }
			    }
			else
			    {
				String[] tokens = graphLine.split (",");
				HashMap <String, Integer> adjForVertex1 = E.get (tokens [0]);
				HashMap <String, Integer> adjForVertex2 = E.get (tokens [1]);

				adjForVertex1.put (tokens [1], Integer.parseInt (tokens [2]));
				adjForVertex2.put (tokens [0], Integer.parseInt (tokens [2]));

				System.out.println ("Edge: " + graphLine + "; weight = " + tokens [2] + "; |Q| = " + E.size ());
			    }
		    }
		// Set the root's key to be zero.
		System.out.println ("Setting root key = 0. |Q| = " + Q.size ());
		Vertex root = Q.poll ();
		root.setKey (0);
		Q.add (root);
		System.out.println ("(After) Setting root key = 0. |Q| = " + Q.size ());
	    }
	catch (IOException e)
	    {
		System.out.println ("IOException in readInGraph ()...");
	    }

    }

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
    }

    public static void walkThePriorityQueue (PriorityQueue <Vertex> Q)
    {
	Vertex u = Q.poll ();
	while (u != null)
	    {
		//System.out.println ("[" + Q.size () + "] Vertex = " + u.getID () + "; key = " + u.getKey ());
		u = Q.poll ();
	    }
    }

    public static void main (String [] argv)
    {
	PriorityQueue<Vertex> Q = new PriorityQueue <Vertex> ();
	Map<String, HashMap<String, Integer>> AdjacencyMatrix = new HashMap<String, HashMap<String, Integer>> ();
	Map <String, Integer> key = new HashMap <String, Integer> ();
	Map <String, String> parent = new HashMap <String, String> ();
	HashSet<String> VNew = new HashSet<String> (); // The tree that will grow to be an MST
	HashMap<String, Integer> ENew = new HashMap<String, Integer> (); // The edges in the MST
	HashSet <String> unvisitedNodes = new HashSet <String> ();

	readInGraph (argv[0], Q, AdjacencyMatrix, key, unvisitedNodes);
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
		System.out.println ("\n\n\n[" + Q.size () + "] Getting adjacency for vertex " + u.getID () + " in heap with key = " + u.getKey ());
		HashMap <String, Integer> adjOfU = AdjacencyMatrix.get (u.getID ());
		System.out.println ("\\-----> adjacency list for vertex " + u.getID () + " (" + adjOfU.size () + " elements)");

		for (Map.Entry<String, Integer> entry: adjOfU.entrySet ())
		    {
			String vID = entry.getKey ();
			Integer wV2U = entry.getValue ();
			Vertex v = new Vertex (vID, 0, false);

			System.out.println ("Trying vertex " + vID);
			boolean status = Q.remove (v);  // Currently too expensive : O (n) worst case!

			if (status)
			    {
				Vertex vv = new Vertex (vID);
				int keyValue = key.get (vID);

				System.out.println ("|Q| == " + Q.size () + "; Q contains " + vID + ": keyValue == " + keyValue + ", and wV2U == " + wV2U);

				if (keyValue > wV2U)
				    {
					vv.setKey (wV2U);
					vv.setProcessed (false);
					key.put (vID, wV2U);
					parent.put (vID, u.getID ());

					try
					    {
						Q.add (vv); // This action re-heapifies at O (lg V) cost.
					    }
					catch (ClassCastException e)
					    {
						System.out.println ("ClassCastException thrown while trying to add new key to PQ");
					    }
					catch (NullPointerException e)
					    {
						System.out.println ("NullPointerException thrown while trying to add new key to PQ");
					    }
					System.out.println ("\\-----> Pushed neighbor " + vID + ", key = " + wV2U + " to Q; |Q| == " + Q.size ());
				    }
				else
				    {
					System.out.println ("\\-----> Discard adjacent vertex " + vID + " (keyValue == " + keyValue + ")");
					vv.setKey (keyValue);
					vv.setProcessed (false);
					Q.add (vv);
				    }
			    }
			else
			    {
				System.out.println ("\\-----> Discard adjacent vertex " + vID + ": already visited: not considered");
			    }
		    }

		VNew.add (u.getID ());
		unvisitedNodes.remove (u.getID ());

		if (VNew.size () == 1)
		    {
			System.out.println ("Added first vertex " + u.getID () + "; |VNew| = 1"  + "; |Q| = " + Q.size ());
		    }
		else
		    {
			path = parent.get (u.getID ()) + u.getID ();
			ENew.put (path, u.getKey ());
			System.out.println ("Added edge " + path + " of w = " + u.getKey () + "; |VNew| = " + VNew.size () + "; |Q| = " + Q.size ());
		    }

		u = Q.poll ();
	    }
    }
}