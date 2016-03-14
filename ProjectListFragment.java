package com.weebly.docrosby.listtaker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProjectListFragment extends ListFragment
{
    private ArrayList<Project> mProjects;
    private myDBHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        db = new myDBHandler(getActivity());
        mProjects = db.getProjects();
        ProjectAdapter adapter = new ProjectAdapter(mProjects);
        setListAdapter(adapter);
    }

    @Override public void onResume()
    {
        super.onResume();
        ((ProjectAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerBottom)).commit();

        Project p = ((ProjectAdapter)getListAdapter()).getItem(position);
        Fragment fragment = ProjectFragment.newInstance(p.getId());
        fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerTop)).commit();
        fm.beginTransaction().add(R.id.fragmentContainerTop, fragment).commit();

        Fragment fragment2 = ItemListFragment.newInstance(p.getId());
        fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerBottom)).commit();
        fm.beginTransaction().add(R.id.fragmentContainerBottom, fragment2).commit();
    }

    protected class ProjectAdapter extends ArrayAdapter<Project>
    {
        public ProjectAdapter(ArrayList<Project> projects)
        {
            super(getActivity(), 0, projects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.project_listview, null);
            }
            Project c = getItem(position);
            TextView titleTextView = (TextView)convertView.findViewById(R.id.project_list_item_titleTextView);
            titleTextView.setTextSize(getResources().getDimension(R.dimen.textsize));
            titleTextView.setText(c.getTitle());
            return convertView;
        }

    }
}
