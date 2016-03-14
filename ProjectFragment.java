package com.weebly.docrosby.listtaker;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

public class ProjectFragment extends Fragment
{
    public static final String EXTRA_PROJECT_ID = "com.weebly.docrosby.listtaker.project_id";
    private Project mProject;
    private EditText mTitleField;
    private EditText mDescriptionField;
    private Button homeButton;
    private  Button uploadButton;
    private  Button deleteButton;
    private myDBHandler db;
    private EditText newItemName;
    private EditText newItemQuantity;
    private EditText newItemPrice;
    private Button addButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UUID projectId = (UUID)getArguments().getSerializable(EXTRA_PROJECT_ID);
        mProject = ProjectLab.get().getProject(projectId);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        db = new myDBHandler(getActivity());
        mProject.setItems(db.getItems(mProject));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, final Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.project_fragment, parent, false);
        mTitleField = (EditText)v.findViewById(R.id.project_title);
        mTitleField.setText(mProject.getTitle());
        mDescriptionField = (EditText)v.findViewById(R.id.project_description);
        mDescriptionField.setText(mProject.getDescription());
        homeButton = (Button)v.findViewById(R.id.home_button);
        uploadButton = (Button)v.findViewById(R.id.upload_project_button);
        deleteButton = (Button)v.findViewById(R.id.delete_project_button);
        newItemName = (EditText)v.findViewById(R.id.new_item_name);
        newItemQuantity = (EditText)v.findViewById(R.id.new_item_quantity);
        newItemPrice = (EditText)v.findViewById(R.id.new_item_price);
        addButton = (Button)v.findViewById(R.id.add_button);

        homeButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerTop)).commit();
                    fm.beginTransaction().add(R.id.fragmentContainerTop, new HomePageFragment()).commit();
                }
            });
        uploadButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent i = new Intent(getActivity(), myUploaderActivity.class);
                i.putExtra(EXTRA_PROJECT_ID, mProject.getId());
                startActivity(i);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());
                adb.setTitle("Delete Project");
                adb.setMessage("Are you sure?");
                adb.setCancelable(true);
                adb.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerTop)).commit();
                        fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerBottom)).commit();
                        fm.beginTransaction().add(R.id.fragmentContainerTop, new HomePageFragment()).commit();
                        db.deleteProject(mProject);
                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*Just return if cancelled, do not delete the project*/
                    }
                });
                AlertDialog alert = adb.create();
                alert.show();
            }
        });
        mTitleField.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence c, int start, int before, int count)
            {
            }
            public void beforeTextChanged(CharSequence c, int start, int count, int after)
            {
            }
            public void afterTextChanged(Editable c)
            {
                mProject.setTitle(c.toString());
                db.editProject(mProject);
            }
        });
        mDescriptionField.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence c, int start, int before, int count)
            {
            }
            public void beforeTextChanged(CharSequence c, int start, int count, int after)
            {
            }
            public void afterTextChanged(Editable c)
            {
                mProject.setDescription(c.toString());
                db.editProject(mProject);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(newItemName.getText().toString().length() != 0)
                {
                    Item item = new Item(newItemName.getText().toString());
                    if(newItemQuantity.getText().toString().length() != 0)
                    {
                        int quantity = Integer.parseInt(newItemQuantity.getText().toString());
                        item.setNumOfItems(quantity);
                    }
                    if(newItemPrice.getText().toString().length() != 0)
                    {
                        double cost = Double.parseDouble(newItemPrice.getText().toString());
                        item.setCost(cost);
                    }
                    mProject.addItem(item);
                    if(db.addItem(mProject, item))
                    {
                        Toast.makeText(getActivity().getApplicationContext(), (item.getName() + " added to " + mProject.getTitle()), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity().getApplicationContext(), (item.getName() + " already exists in " + mProject.getTitle()), Toast.LENGTH_LONG).show();
                    }
                    newItemName.setText("");
                    newItemQuantity.setText("");
                    newItemPrice.setText("");
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    Fragment fragment = ItemListFragment.newInstance(mProject.getId());
                    if (fm.findFragmentById(R.id.fragmentContainerBottom) != null)
                    {
                        fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerBottom)).commit();
                    }
                    fm.beginTransaction().add(R.id.fragmentContainerBottom, fragment).commit();
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Please provide a name for this item you wish to add.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
}

    public static ProjectFragment newInstance(UUID projectId)
    {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PROJECT_ID, projectId);
        ProjectFragment fragment = new ProjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != Activity.RESULT_OK)
        {
            return;
        }
    }

}
