package com.hack23.maven.plugin.model;

public class Conditions
{
    private String op;

    private String period;

    private String metric;

    private String level;

    private String error;
    
    private String actual;

    public String getActual() {
		return actual;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}

	public String getOp ()
    {
        return op;
    }

    public void setOp (String op)
    {
        this.op = op;
    }

    public String getPeriod ()
    {
        return period;
    }

    public void setPeriod (String period)
    {
        this.period = period;
    }

    public String getMetric ()
    {
        return metric;
    }

    public void setMetric (String metric)
    {
        this.metric = metric;
    }

    public String getLevel ()
    {
        return level;
    }

    public void setLevel (String level)
    {
        this.level = level;
    }

    public String getError ()
    {
        return error;
    }

    public void setError (String error)
    {
        this.error = error;
    }

    @Override
    public String toString()
    {
        return "Condition [op = "+op+", period = "+period+", metric = "+metric+", level = "+level+", error = "+error+"]";
    }
}