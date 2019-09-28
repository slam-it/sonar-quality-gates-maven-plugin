package com.hack23.maven.plugin.model;

import java.util.ArrayList;

public class MeasuresContainer {
	
	 private ArrayList<Measures> measures;

	    public ArrayList<Measures> getMeasures ()
	    {
	        return measures;
	    }

	    public void setMeasures (ArrayList<Measures> measures)
	    {
	        this.measures = measures;
	    }

	    @Override
	    public String toString()
	    {
	        return "MeasuresContainer [measures = "+measures+"]";
	    }
}
