package com.weebly.docrosby.listtaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class myDBHandler extends SQLiteOpenHelper {
    private static myDBHandler sInstance;
    private static final String DATABASE_NAME = "listtaker.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PROJECTS = "Projects";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_NAME = "name";

    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ITEM_NAME = "i_name";
    public static final String COLUMN_ITEM_QUANTITY = "i_quantity";
    public static final String COLUMN_ITEM_PRICE = "i_price";
    public static final String COLUMN_ITEM_ID = "i_id";

    public static final String TABLE_BACKGROUND_PICTURES = "background_pictures";

    private static SQLiteDatabase db;


    public myDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PROJECTS + " ("
                + COLUMN_ID + " TEXT , "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT );";
        db.execSQL(query);
        query = "CREATE TABLE " + TABLE_ITEMS + " ("
                + COLUMN_ID + " TEXT , "
                + COLUMN_ITEM_ID + " TEXT , "
                + COLUMN_ITEM_NAME + " TEXT , "
                + COLUMN_ITEM_QUANTITY + " INTEGER, "
                + COLUMN_ITEM_PRICE + " DOUBLE );";
        db.execSQL(query);
        query = "CREATE TABLE " + TABLE_BACKGROUND_PICTURES + " ("
                + COLUMN_ID + " TEXT , "
                + COLUMN_NAME + " TEXT );";
        db.execSQL(query);
        addPictures(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS + ";");
        onCreate(db);
    }

    public static synchronized myDBHandler getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new myDBHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    public void addProject(Project project) {

        String projectId = project.getId().toString();
        String projectName = project.getTitle();
        String projectDescription = project.getDescription();
        String query = "SELECT * FROM " + TABLE_PROJECTS
                + " WHERE " + COLUMN_ID + "=\"" + projectId + "\";";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if(c.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, projectId);
            values.put(COLUMN_NAME, projectName);
            values.put(COLUMN_DESCRIPTION, projectDescription);

            db.insert(TABLE_PROJECTS, null, values);
        }
        c.close();
        //db.close();
    }

    public boolean deleteProject(Project project) {
        db = getWritableDatabase();
        String projectId = project.getId().toString();
        try {
            db.execSQL("DELETE FROM " + TABLE_PROJECTS
                    + " WHERE " + COLUMN_ID + "=\"" + projectId + "\";");
            db.execSQL("DELETE FROM " + TABLE_ITEMS
                    + " WHERE " + COLUMN_ID + "=\"" + projectId + "\";");
            //db.close();
            return true;
        }
        catch(Exception e) {
            //db.close();
            return false;
        }
    }

    public void editProject(Project project) {
        db = getWritableDatabase();
        String projectId = project.getId().toString();
        db.execSQL("UPDATE " + TABLE_PROJECTS
                + " SET " + COLUMN_NAME + "=\"" + project.getTitle() + "\", " + COLUMN_DESCRIPTION + "=\"" + project.getDescription() + "\""
                + " WHERE " + COLUMN_ID + "=\"" + projectId + "\";");
        //db.close();
    }

    public ArrayList<Project> getProjects() {
        ArrayList<Project> mProjects = new ArrayList<>();
        db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PROJECTS +";";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("_id")) != null) {
                UUID uid = UUID.fromString(c.getString(c.getColumnIndex("_id")));
                String title = c.getString(c.getColumnIndex("name"));
                String description = c.getString(c.getColumnIndex("description"));
                Project project = new Project(uid, title, description);
                mProjects.add(project);
                ProjectLab.get().getProjects().add(project);
            }
            c.moveToNext();
        }
        c.close();
        //db.close();
        return mProjects;
    }

    public boolean addItem(Project project, Item item) {
        db = getWritableDatabase();

        String projectId = project.getId().toString();
        String itemId = item.getId().toString();
        String itemName = item.getName();
        int itemQuantity = item.getNumOfItems();
        double itemPrice = item.getCost();
        String query = "SELECT * FROM " + TABLE_ITEMS
                + " WHERE " + COLUMN_ID + "=\"" + projectId + "\" AND " + COLUMN_ITEM_NAME + "=\"" + itemName + "\";";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if(c.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, projectId);
            values.put(COLUMN_ITEM_ID, itemId);
            values.put(COLUMN_ITEM_NAME, itemName);
            values.put(COLUMN_ITEM_QUANTITY, itemQuantity);
            values.put(COLUMN_ITEM_PRICE, itemPrice);

            try {
                db.insert(TABLE_ITEMS, null, values);
            }
            catch(Exception e) {
                c.close();
                //db.close();
                return false;
            }
        }
        else {
            c.close();
            //db.close();
            return false;
        }
        c.close();
        //db.close();
        return true;
    }

    public boolean deleteItem(Project project, Item item) {
        db = getWritableDatabase();
        String projectId = project.getId().toString();
        String itemId = item.getId().toString();
        try {
            db.execSQL("DELETE FROM " + TABLE_ITEMS
                    + " WHERE " + COLUMN_ID + "=\"" + projectId + "\" AND " + COLUMN_ITEM_ID + "=\"" + itemId + "\";");
            //db.close();
            return true;
        }
        catch(Exception e) {
            //db.close();
            return false;
        }
    }

    public void editItem(Project project, Item item) {
        db = getWritableDatabase();
        String projectId = project.getId().toString();
        String itemId = item.getId().toString();
        db.execSQL("UPDATE " + TABLE_ITEMS
                + " SET " + COLUMN_ITEM_NAME + "=\"" + item.getName() + "\", " + COLUMN_ITEM_QUANTITY + "=" + item.getNumOfItems() + " , " + COLUMN_ITEM_PRICE + "=" + item.getCost()
                + " WHERE " + COLUMN_ITEM_ID + "=\"" + itemId + "\" AND " + COLUMN_ID + "=\"" + projectId + "\";");
    }

    public ArrayList<Item> getItems(Project project) {
        ArrayList<Item> mItems = new ArrayList<>();
        db = getWritableDatabase();
        String projectId = project.getId().toString();
        String query = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ID + "=\"" + projectId + "\";";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("_id")) != null) {
                UUID itemId = UUID.fromString(c.getString(c.getColumnIndex("i_id")));
                String itemName = c.getString(c.getColumnIndex("i_name"));
                int itemQuantity = c.getInt(c.getColumnIndex("i_quantity"));
                double itemPrice = c.getDouble(c.getColumnIndex("i_price"));
                Item item = new Item(itemId, itemName, itemQuantity, itemPrice);
                mItems.add(item);
            }
            c.moveToNext();
        }
        c.close();
        //db.close();
        return mItems;
    }

    public String generateFileString(Project mProject) {
        ArrayList<Item> mItems = getItems(mProject);
        String s = "Item Name,Quantity,Price Per Item,Subtotal,Unique Identifier\n";
        double totalCost = 0;
        for(Item i: mItems)
        {
            s+= (i.getName() + "," + i.getNumOfItems() + "," + i.getCost() + "," + i.getTotalCost() + "," + i.getId() + "," + "\n");
            totalCost += i.getTotalCost();
        }
        s += ("-,-,Total Cost:," + totalCost + ",\n");
        return s;
    }

    public boolean addPictures(SQLiteDatabase db) {
        List<String> s = new ArrayList<>();
        s.add("arches");
        s.add("batthern");
        s.add("brick_background");
        s.add("crisp_paper_ruffles");
        s.add("diagonal_striped_brick");
        s.add("escheresque");
        s.add("gplaypattern");
        s.add("graphy");
        s.add("kuji");
        s.add("paven");
        s.add("purty_wood");
        s.add("retina_wood");
        s.add("straws");
        s.add("tileable_wood_texture");
        s.add("vaio_hard_edge");
        s.add("wall4");
        s.add("weave");
        s.add("white_leather");
        s.add("white_tiles");
        for(String ss : s) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, ss);
            values.put(COLUMN_NAME, ss);
            try {
                db.insert(TABLE_BACKGROUND_PICTURES, null, values);
            }
            catch(Exception e) {
                //db.close();
                return false;
            }
        }
        //db.close();
        return true;
    }

    public ArrayList<BackgroundImage> getImages() {
        ArrayList<BackgroundImage> mImages = new ArrayList<>();
        db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_BACKGROUND_PICTURES + ";";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("_id")) != null) {
                String imageId = c.getString(c.getColumnIndex("_id"));
                String imageName = c.getString(c.getColumnIndex("name"));
                BackgroundImage bgi = new BackgroundImage(imageId, imageName);
                mImages.add(bgi);
            }
            c.moveToNext();
        }
        c.close();
        //db.close();
        return mImages;
    }

    public BackgroundImage getSavedImage() {
        SQLiteDatabase db = getWritableDatabase();
        BackgroundImage bgi = null;
        String query = "SELECT * FROM " + TABLE_BACKGROUND_PICTURES + " LIMIT 1;";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if(cursor.getString(cursor.getColumnIndex("_id")) != null) {
            String imageId = cursor.getString(cursor.getColumnIndex("_id"));
            String imageName = cursor.getString(cursor.getColumnIndex("name"));
            bgi = new BackgroundImage(imageId, imageName);
        }
        cursor.close();
        //db.close();
        return bgi;
    }

    public void setSavedImage(BackgroundImage newImage) {
        BackgroundImage oldImage = getSavedImage();
        db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BACKGROUND_PICTURES
                + " WHERE " + COLUMN_ID + "=\"" + newImage.getPng() + "\";");
        db.execSQL("UPDATE " + TABLE_BACKGROUND_PICTURES
                + " SET " + COLUMN_ID + "=\"" + newImage.getPng() + "\", " + COLUMN_NAME + "=\"" + newImage.getName()
                + "\" WHERE " + COLUMN_ID + "=\"" + oldImage.getPng() + "\";");
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, oldImage.getPng());
        values.put(COLUMN_NAME, oldImage.getName());
        db.insert(TABLE_BACKGROUND_PICTURES, null, values);
        //db.close();
    }
}
