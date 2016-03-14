package com.weebly.docrosby.listtaker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class StartNewProjectFragment extends Fragment
{
    Button addProjectButton;
    Button cancelButton;
    private EditText mNewTitleField;
    private EditText mNewDescriptionField;
    private myDBHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        db = new myDBHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.new_project_fragment, parent, false);
        addProjectButton = (Button)v.findViewById(R.id.add_new_project_button);
        cancelButton = (Button)v.findViewById(R.id.cancel__new_project_button);
        mNewTitleField = (EditText)v.findViewById(R.id.new_project_title);
        mNewDescriptionField = (EditText)v.findViewById(R.id.new_project_description);

        addProjectButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(mNewTitleField.getText().toString().length() == 0)
                {
                    //Do nothing unless the Project Title field is populated
                }
                else
                {
                    Project p = new Project();
                    p.setTitle(mNewTitleField.getText().toString());
                    p.setDescription(mNewDescriptionField.getText().toString());
                    ProjectLab.get().mProjects.add(p);

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    Fragment fragment = ProjectFragment.newInstance(p.getId());
                    fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerTop)).commit();
                    fm.beginTransaction().add(R.id.fragmentContainerTop, fragment).commit();

                    Fragment fragment2 = ItemListFragment.newInstance(p.getId());
                    fm.beginTransaction().add(R.id.fragmentContainerBottom, fragment2).commit();

                    db.addProject(p);
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerTop)).commit();
                fm.beginTransaction().add(R.id.fragmentContainerTop, new HomePageFragment()).commit();
            }
        });
        return v;
    }

}
