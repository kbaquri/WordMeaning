package com.appsxhone.wordmeaning;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * Created by Sameer on 19-Feb-16.
 */
public class DatabaseOperations extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.appsxhone.wordmeaning/databases/";

    private static String DB_NAME = "word_meaning_database.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    String tableName, tempTableName;
    String begginer[] = {"ThreeCharacter", "FourCharacter"},
            intermediate[] = {"FiveCharacter", "SixCharacter"},
            hard[] = {"SevenCharacter", "EightCharacter"},
            expert[] = {"NineCharacter", "TenCharacter", "ElevenCharacter", "TwelveCharacter"};

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DatabaseOperations(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;

    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

/*        Cursor c = myDataBase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                Log.d("Table Name=> ", c.getString(1));
                c.moveToNext();
            }
        }*/

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Random Method
    private int getRandom(int max) {

        Random rand = new Random();

        int min = 0;

        int randomNum = rand.nextInt(max - min) + min;

        return randomNum;

    }

    String getTableName(int level) {
        switch (level) {
            case 1:
                tableName = begginer[getRandom(2)];
                break;
            case 2:
                tableName = intermediate[getRandom(2)];
                break;
            case 3:
                tableName = hard[getRandom(2)];
                break;
            case 4:
                tableName = expert[getRandom(4)];
                break;
        }
        return tableName;
    }

    public Cursor getInformation(DatabaseOperations dop, int level) {
        SQLiteDatabase sqLiteDatabase = dop.getReadableDatabase();
        String[] columns = {"Meaning", "Word"};
        String selection = "Completed LIKE ? ";
        String args[] = {"FALSE"};
        String ordr = "Meaning";
        tempTableName = getTableName(level);
        Cursor cursor = sqLiteDatabase.query(tempTableName, columns, selection, args, null, null,ordr, "1");
        return cursor;
    }

    public void updateInformation(DatabaseOperations dop, String word) {
        SQLiteDatabase sqLiteDatabase = dop.getWritableDatabase();
        String selection = "Word LIKE ?";
        String args[] = {word};
        ContentValues values = new ContentValues();
        values.put("Completed", "TRUE");
        sqLiteDatabase.update(tempTableName, values, selection, args);
    }


    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.
}