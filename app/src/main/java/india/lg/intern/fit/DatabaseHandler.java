package india.lg.intern.fit;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "FIT_DB";


    // Footprints table name
    private static final String TABLE_CONTACTS = "FOOTPRINT_TABLE";

    // Footprints Table Columns names
    private static final String KEY_FOOTPRINT = "footprint";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FOOTPRINTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_FOOTPRINT + " INTEGER PRIMARY KEY" + ")";
        db.execSQL(CREATE_FOOTPRINTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new fp
    void addFootprint(Footprint fp) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_POSLIST, fp.getPosList()); // Footprint Name
        values.put(KEY_SPOTLIST, fp.getPhoneNumber()); // Footprint Phone

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single fp
    Footprint getFootprint(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {KEY_DATE,
                        KEY_POSLIST, KEY_SPOTLIST}, KEY_DATE + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Footprint fp = new Footprint(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return fp
        return fp;
    }

    // Getting All Footprints
    public List<Footprint> getAllFootprints() {
        List<Footprint> fpList = new ArrayList<Footprint>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Footprint fp = new Footprint();
                fp.setID(Integer.parseInt(cursor.getString(0)));
                fp.setName(cursor.getString(1));
                fp.setPhoneNumber(cursor.getString(2));
                // Adding fp to list
                fpList.add(fp);
            } while (cursor.moveToNext());
        }

        // return fp list
        return fpList;
    }

    // Updating single fp
    public int updateFootprint(Footprint fp) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_POSLIST, fp.getName());
        values.put(KEY_SPOTLIST, fp.getPhoneNumber());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_DATE + " = ?",
                new String[] { String.valueOf(fp.getID()) });
    }

    // Deleting single fp
    public void deleteFootprint(Footprint fp) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_DATE + " = ?",
                new String[] { String.valueOf(fp.getID()) });
        db.close();
    }


    // Getting fps Count
    public int getFootprintsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}