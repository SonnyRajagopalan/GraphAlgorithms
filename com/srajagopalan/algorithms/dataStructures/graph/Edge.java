/*
  Author: Sonny Rajagopalan
  Date 04/20/2013.

  ATTRIBUTION:
  ============

  Some parts of code is adapted from:
  http://krishami.com/2011/12/implementation-of-kruskals-algorithm-in-java/
 */
package com.srajagopalan.algorithms.dataStructures.graph;

public class Edge implements Comparable <Edge>
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

    @Override
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