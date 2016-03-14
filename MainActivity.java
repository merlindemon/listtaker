package com.weebly.docrosby.listtaker;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class MainActivity extends FragmentActivity {
    public myDBHandler mDBHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the view
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainerTop);
        if (fragment == null) {
            fragment = new HomePageFragment();
            fm.beginTransaction().add(R.id.fragmentContainerTop, fragment).commit();
        }
        //Create the database handler
        //mDBHandler = new myDBHandler(this, null, null, 1);
        mDBHandler = myDBHandler.getInstance(this.getApplicationContext());

        //Set the BackGroundImage to the one last saved by the user
        String imageName = mDBHandler.getSavedImage().getPng();
        int id1 = getResources().getIdentifier(imageName, "drawable", getApplication().getApplicationContext().getPackageName());
        Drawable drawable = getResources().getDrawable(id1);
        findViewById(R.id.fragmentContainerMain).setBackground(drawable);
    }

    @Override
    public void onBackPressed() {
        // do nothing.
    }
}
