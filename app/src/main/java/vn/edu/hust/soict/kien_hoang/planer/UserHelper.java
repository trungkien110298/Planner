package vn.edu.hust.soict.kien_hoang.planer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "planner.db";
    private static final int SCHEMA_VERSION = 1;
    public UserHelper(Context context){
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE users (_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // no-op, since will not be called until 2nd schema
        // version exists
    }

    public Cursor getAll() {
        return(getReadableDatabase()
                .rawQuery("SELECT _id, username, password FROM users ORDER BY _id",null));
    }

    public Cursor getByUsername(String username){
        return (getReadableDatabase()
                .rawQuery("SELECT _id, username, password FROM users WHERE username =" + username,null));
    }

    public void insert(String username, String password) {
        ContentValues cv=new ContentValues();
        cv.put("username", username);
        cv.put("password", password);
        getWritableDatabase().insert("users", "username", cv);
    }
    public String getUsername(Cursor c) {
        return(c.getString(1));
    }
    public String getPassword(Cursor c) {
        return(c.getString(2));
    }
}
