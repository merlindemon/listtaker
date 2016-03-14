package com.weebly.docrosby.listtaker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingsFragment extends Fragment
{
    private Button saveSettingsButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_fragment, parent, false);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().add(R.id.fragmentContainerImageList, new BackgroundImagesList()).commit();
        saveSettingsButton = (Button) v.findViewById(R.id.save_settings_button);

        saveSettingsButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerBottom)).commit();
                fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerTop)).commit();
                fm.beginTransaction().add(R.id.fragmentContainerTop, new HomePageFragment()).commit();
            }
        });
        return v;
    }
}
