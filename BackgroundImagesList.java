package com.weebly.docrosby.listtaker;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class BackgroundImagesList extends ListFragment {
    private ArrayList<BackgroundImage> mImages;
    private myDBHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new myDBHandler(getActivity());
        mImages = db.getImages();
        ImageAdapter adapter = new ImageAdapter(mImages);
        setListAdapter(adapter);
    }

    @Override public void onResume() {
        super.onResume();
        ((ImageAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        BackgroundImage bgi = ((ImageAdapter)getListAdapter()).getItem(position);
        String name = bgi.getPng();
        int id1 = getResources().getIdentifier(name, "drawable", getActivity().getApplication().getApplicationContext().getPackageName());
        Drawable drawable = getResources().getDrawable(id1);
        View v1 = getActivity().findViewById(android.R.id.content).getRootView();

        FrameLayout frameLayout = (FrameLayout)v1.findViewById(R.id.fragmentContainerMain);
        frameLayout.setBackground(drawable);
        db.setSavedImage(bgi);
    }

    protected class ImageAdapter extends ArrayAdapter<BackgroundImage> {
        public ImageAdapter(ArrayList<BackgroundImage> images)
        {
            super(getActivity(), 0, images);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.image_listview, null);
            }
            BackgroundImage bgi = getItem(position);
            TextView titleTextView = (TextView)convertView.findViewById(R.id.image_list_item_titleTextView);
            titleTextView.setTextSize(getResources().getDimension(R.dimen.textsize));
            titleTextView.setText(bgi.getName());
            return convertView;
        }

    }
}
