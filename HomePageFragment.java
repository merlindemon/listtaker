package com.weebly.docrosby.listtaker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomePageFragment extends Fragment {
    Button start_newButton;
    Button quitButton;
    Button settingsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_page_fragment, parent, false);

        start_newButton = (Button)v.findViewById(R.id.start_new_button);
        quitButton = (Button)v.findViewById(R.id.quit_button);
        settingsButton = (Button)v.findViewById(R.id.settings_button);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (fm.findFragmentById(R.id.fragmentContainerBottom) != null) {
            fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerBottom)).commit();
        }
        Fragment fragment = new ProjectListFragment();
        fm.beginTransaction().add(R.id.fragmentContainerBottom, fragment).commit();

        start_newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerTop)).commit();
                if (fm.findFragmentById(R.id.fragmentContainerBottom) != null) {
                    fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerBottom)).commit();
                }
                Fragment fragment = new StartNewProjectFragment();
                fm.beginTransaction().add(R.id.fragmentContainerTop, fragment).commit();
            }
        });
        quitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().finish();
                System.exit(0);
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.findFragmentById(R.id.fragmentContainerBottom) != null) {
                    fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerBottom)).commit();
                }
                Fragment fragment = new SettingsFragment();
                fm.beginTransaction().add(R.id.fragmentContainerBottom, fragment).commit();
            }
        });
        return v;
    }

}