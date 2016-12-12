package in.yellowsoft.LetUKnow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    Context context;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "downloads";

    // Contacts table name
    private static final String TABLE_PLAYLIST = "playlist";

    // Contacts Table Columns names

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_NAME_AR = "name_ar";
    private static final String KEY_NAME_FR = "name_fr";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_PID = "pid";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
                String CREATE_PLAYLIST_TABLE = "CREATE TABLE " + TABLE_PLAYLIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME +  " TEXT" + ")";
        db.execSQL(CREATE_PLAYLIST_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST);

        // Create tables again
        onCreate(db);
    }

    void addPlaylist(String id,String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id); // Contact Phone
        values.put(KEY_NAME, name); // Contact Name
         // Contact Phone
        // Inserting Row
        db.insert(TABLE_PLAYLIST, null, values);
        db.close(); // Closing database connection
    }

        // Getting single contact

    boolean is_following(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PLAYLIST, new String[]{KEY_ID,
                        KEY_NAME}, KEY_ID + "=?",
                new String[]{id}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if(cursor.getCount()>0)
        return true;
        else
            return false;
    }

    public void deletePlaylist(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYLIST, KEY_ID + " = ?",
                new String[] { id });
        db.close();
    }

    String  selected_channels(String pid){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PLAYLIST, new String[]{KEY_ID,
                        KEY_NAME}, KEY_NAME + "=?",
                new String[]{pid}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            if (cursor.getCount() > 0) {
                String append="";
                for (int i = 0; i <cursor.getCount();i++) {
                    if(i==0)
                        append=cursor.getString(cursor.getColumnIndex(KEY_ID));
                    else
                    append=append+","+cursor.getString(cursor.getColumnIndex(KEY_ID));

                    cursor.moveToNext();
                }
                return append;
            }
        }
        return "0";
    }

    String  all_selected_channels(String pid){
        SQLiteDatabase db = this.getReadableDatabase();

        /*Cursor cursor = db.query(TABLE_PLAYLIST, new String[]{KEY_ID,
                        KEY_NAME}, KEY_NAME + "=?",
                new String[]{pid}, null, null, null, null);
        */

        Cursor  cursor = db.rawQuery("select * from "+TABLE_PLAYLIST,null);

        if (cursor != null) {
            cursor.moveToFirst();

            if (cursor.getCount() > 0) {
                String append="";
                for (int i = 0; i <cursor.getCount();i++) {

                    if( !cursor.getString(cursor.getColumnIndex(KEY_NAME)).equals( MainActivity.sports_id) && !cursor.getString(cursor.getColumnIndex(KEY_NAME)).equals(MainActivity.economy_id) )
                    if(i==0)
                        append=cursor.getString(cursor.getColumnIndex(KEY_ID));
                    else
                        append=append+","+cursor.getString(cursor.getColumnIndex(KEY_ID));

                    cursor.moveToNext();
                }
                if(append.equals(""))
                    return  "0";

                return append;
            }
        }
        return "0";
    }

    String  all_selected_channels_new(String pid){
        SQLiteDatabase db = this.getReadableDatabase();

        /*Cursor cursor = db.query(TABLE_PLAYLIST, new String[]{KEY_ID,
                        KEY_NAME}, KEY_NAME + "=?",
                new String[]{pid}, null, null, null, null);
        */

        Cursor  cursor = db.rawQuery("select * from "+TABLE_PLAYLIST,null);

        if (cursor != null) {
            cursor.moveToFirst();

            if (cursor.getCount() > 0) {
                String append="";
                for (int i = 0; i <cursor.getCount();i++) {

                        if(i==0)
                            append=cursor.getString(cursor.getColumnIndex(KEY_ID));
                        else
                            append=append+","+cursor.getString(cursor.getColumnIndex(KEY_ID));

                    cursor.moveToNext();
                }
                return append;
            }
        }
        return "0";
    }


}
