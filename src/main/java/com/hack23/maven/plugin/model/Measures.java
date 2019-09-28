package com.hack23.maven.plugin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Measures {
	  private String component;

	    private String metric;

	    private String value;

	    public String getComponent ()
	    {
	        return component;
	    }

	    public void setComponent (String component)
	    {
	        this.component = component;
	    }

	    public String getMetric ()
	    {
	        return metric;
	    }

	    public void setMetric (String metric)
	    {
	        this.metric = metric;
	    }

	    public String getValue ()
	    {
	        return value;
	    }

	    public void setValue (String value)
	    {
	        this.value = value;
	    }

	    @Override
	    public String toString()
	    {
	        return "[component = "+component+", metric = "+metric+", value = "+value+"]";
	    }
}
