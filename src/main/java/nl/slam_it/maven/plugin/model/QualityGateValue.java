package nl.slam_it.maven.plugin.model;

import java.util.ArrayList;

public class QualityGateValue
{
    private String ignoredConditions;

    private String level;

    private ArrayList<Conditions> conditions;

    public String getIgnoredConditions ()
    {
        return ignoredConditions;
    }

    public void setIgnoredConditions (String ignoredConditions)
    {
        this.ignoredConditions = ignoredConditions;
    }

    public String getLevel ()
    {
        return level;
    }

    public void setLevel (String level)
    {
        this.level = level;
    }

    public ArrayList<Conditions> getConditions ()
    {
        return conditions;
    }

    public void setConditions (ArrayList<Conditions> conditions)
    {
        this.conditions = conditions;
    }

    @Override
    public String toString()
    {
        return "QualityGateValue [ignoredConditions = "+ignoredConditions+", level = "+level+", conditions = "+conditions+"]";
    }
}