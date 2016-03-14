package com.weebly.docrosby.listtaker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.UUID;

public class ItemListFragment extends ListFragment {
    public static final String EXTRA_PROJECT_ID = "com.weebly.docrosby.listtaker.project_id";
    private ArrayList<Item> mItems;
    private myDBHandler db;
    private Project mProject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID projectId = (UUID)getArguments().getSerializable(EXTRA_PROJECT_ID);
        mProject = ProjectLab.get().getProject(projectId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new myDBHandler(getActivity());
        mItems = db.getItems(mProject);
        ItemAdapter adapter = new ItemAdapter(mItems);
        setListAdapter(adapter);
    }

    @Override public void onResume() {
        super.onResume();
        ((ItemAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Item mItem = ((ItemAdapter)getListAdapter()).getItem(position);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerTop)).commit();
        fm.beginTransaction().remove(fm.findFragmentById(R.id.fragmentContainerBottom)).commit();
        Fragment fragment = EditItemFragment.newInstance(mProject.getId(), mItem.getId());
        fm.beginTransaction().add(R.id.fragmentContainerTop, fragment).commit();
    }

    protected class ItemAdapter extends ArrayAdapter<Item> {
        public ItemAdapter(ArrayList<Item> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_listview, null);
            }
            Item c = getItem(position);
            TextView titleTextView = (TextView)convertView.findViewById(R.id.item_list_item_titleTextView);
            TextView quantityTextView = (TextView)convertView.findViewById(R.id.item_list_item_quantityTextView);
            TextView priceTextView = (TextView)convertView.findViewById(R.id.item_list_item_priceTextView);
            TextView subtotalTextView = (TextView)convertView.findViewById(R.id.item_list_item_subtotalTextView);
            String name = c.getName();
            String quantity = String.valueOf(c.getNumOfItems());
            NumberFormat formatter = new DecimalFormat("#0.00");
            String price = ("$" + String.valueOf(formatter.format(c.getCost())));
            String subtotal = ("$" + String.valueOf(formatter.format(c.getTotalCost())));

            titleTextView.setText(name);
            titleTextView.setGravity(Gravity.LEFT);
            quantityTextView.setText(quantity);
            quantityTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            priceTextView.setText(price);
            priceTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            subtotalTextView.setText(subtotal);
            subtotalTextView.setGravity(Gravity.RIGHT);
            return convertView;
        }
    }

    public static ItemListFragment newInstance(UUID projectId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PROJECT_ID, projectId);
        ItemListFragment fragment = new ItemListFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

