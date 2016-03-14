package com.weebly.docrosby.listtaker;

import java.util.ArrayList;
import java.util.UUID;

public class ProjectLab
{
    protected ArrayList<Project> mProjects;
    private static ProjectLab sProjectLab;

    private ProjectLab()
    {
        mProjects = new ArrayList<>();
    }

    public static ProjectLab get()
    {
        if(sProjectLab == null)
        {
            sProjectLab = new ProjectLab();
        }
        return sProjectLab;
    }

    public ArrayList<Project> getProjects()
    {
        return mProjects;
    }

    public Project getProject(UUID id)
    {
        for(Project p : mProjects)
        {
            if(p.getId().equals(id))
                return p;
        }
        return null;
    }

}
