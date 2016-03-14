package com.weebly.docrosby.listtaker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.UUID;

public class EditItemFragment extends Fragment {
    public static final String EXTRA_PROJECT_ID = "com.weebly.docrosby.listtaker.project_id";
    public static final String EXTRA_ITEM_ID = "com.weebly.docrosby.listtaker.item_id";
    private Project mProject;
    private Item mItem;
    EditText editName;
    EditText editQuantity;
    EditText editPrice;
    Button returnButton;
    Button deleteItemButton;
    private myDBHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID projectId = (UUID)getArguments().getSerializable(EXTRA_PROJECT_ID);
        mProject = ProjectLab.get().getProject(projectId);
        UUID itemId = (UUID)getArguments().getSerializable(EXTRA_ITEM_ID);
        for(Item item: mProject.getItems()) {
            if(item.getId().equals(itemId)) {
                mItem = item;
            }
        }
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        db = new myDBHandler(getActivity());
        editName.setText(mItem.getName());
        editQuantity.setText(String.valueOf(mItem.getNumOfItems()));
        editPrice.setText(String.valueOf(mItem.getCost()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_item_fragment, parent, false);
        editName = (EditText)v.findViewById(R.id.edit_item_name);
        editQuantity = (EditText)v.findViewById(R.id.edit_item_quantity);
        editPrice = (EditText)v.findViewById(R.id.edit_item_price);
        returnButton = (Button)v.findViewById(R.id.return_button);
        deleteItemButton = (Button)v.findViewById(R.id.delete_item_button);

        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(editName.getText().toString().length() != 0) {
                    mItem.setName(editName.getText().toString());
                    mItem.setNumOfItems(Integer.parseInt(editQuantity.getText().toString()));
                    mItem.setCost(Double.parseDouble(editPrice.getText().toString()));
                    db.editItem(mProject, mItem);
                }

                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment fragment = ProjectFragment.newInstance(mProject.getId());
                fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerTop)).commit();
                fm.beginTransaction().add(R.id.fragmentContainerTop, fragment).commit();

                Fragment fragment2 = ItemListFragment.newInstance(mProject.getId());
                fm.beginTransaction().add(R.id.fragmentContainerBottom, fragment2).commit();
            }
        });
        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String deletedItemName = mItem.getName();
                if(db.deleteItem(mProject, mItem)) {
                    Toast.makeText(getActivity().getApplicationContext(), (deletedItemName + " was successfully removed."), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(),("An error occurred while attempting to delete " + deletedItemName + "."), Toast.LENGTH_LONG).show();
                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment fragment = ProjectFragment.newInstance(mProject.getId());
                fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerTop)).commit();
                fm.beginTransaction().add(R.id.fragmentContainerTop, fragment).commit();

                Fragment fragment2 = ItemListFragment.newInstance(mProject.getId());
                fm.beginTransaction().add(R.id.fragmentContainerBottom, fragment2).commit();
            }
        });
        return v;
    }

    public static EditItemFragment newInstance(UUID projectId, UUID itemId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PROJECT_ID, projectId);
        args.putSerializable(EXTRA_ITEM_ID, itemId);
        EditItemFragment fragment = new EditItemFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
