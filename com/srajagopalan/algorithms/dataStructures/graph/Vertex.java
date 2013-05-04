/*
  Author: Sonny Rajagopalan
  Date 04/20/2013.
 */

package com.srajagopalan.algorithms.dataStructures.graph;

public class Vertex implements Comparable<Vertex>
{
    private String id;
    private int    key;
    boolean processed;

    public Vertex (String id, int key, boolean processed)
	{
	    this.id = id;
	    this.key = key;
	    this.processed = processed;
	}

    public Vertex (String id)
	{
	    this.id = id;
	}

    public String getID ()
    {
	return id;
    }

    public int getKey ()
    {
	return this.key;
    }

    public void setKey (int key)
    {
	this.key = key;
    }

    public boolean getProcessed ()
    {
	return this.processed;
    }

    public void setProcessed (boolean processed)
    {
	this.processed = processed;
    }

    @Override
	public int compareTo (Vertex anotherVertex)
	{
	    int compareStatus = this.key - anotherVertex.getKey ();

	    //System.out.println ("           Vertex::compareTo (" + this.key + "," + anotherVertex.getKey () + "): => " + compareStatus);

	    return compareStatus;
	}

    @Override
	public boolean equals (Object o)
	{
	    if (o instanceof Vertex)
		{
		    Vertex another = (Vertex) o;
		    boolean sameOrNot = id.equals (another.id);

		    //System.out.println ("           Vertex::equals (" + id + ", " + another.id + ") => " + sameOrNot);

		    return sameOrNot;
		}
	    return false;
	}
}