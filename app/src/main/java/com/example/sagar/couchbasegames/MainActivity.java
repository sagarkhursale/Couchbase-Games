package com.example.sagar.couchbasegames;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableDocument;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private final String DATABASE = "couchbase_games";

    private Database mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Helper Object
        DatabaseConfiguration configuration = new DatabaseConfiguration(
                getApplicationContext());

        try {
            mDatabase = new Database(DATABASE, configuration);
        } catch (CouchbaseLiteException e) {
            Log.i(TAG, "CB-1 Error : " + e.toString());
        }

        //
    }


    private String createDocument(Database database) {
        // create doc with random ID
        MutableDocument mutableDocument = new MutableDocument();

        String docID = mutableDocument.getId();
        Log.i(TAG, "CB - Generated doc ID is : " + mutableDocument.getId());

        mutableDocument.setString("name", "Sagar K");
        mutableDocument.setString("score", "42");

        try {
            // save the properties to the document
            // the data is now persisted to database
            database.save(mutableDocument);
        } catch (CouchbaseLiteException e) {
            Log.i(TAG, "CB-2 Error : " + e.toString());
        }

        return docID;
    }


    private void getDocument(Database database) {
        // Retrieve doc from database
        Document document = database.getDocument("sagar@example.com");

        String value = document.getString("name");
        String score = document.getString("score");

        // Display the value from retrieved doc
        Log.i(TAG, "Value is : " + value);

    }


    private void outputContents(Database database, String docId) {
        // get the document
        Document getDocument = database.getDocument(docId);

        if (getDocument != null) {
            for (String keyName : getDocument.getKeys()) {

                Log.i(TAG, "Key : " + keyName + "" +
                        "value : " + getDocument.getValue(keyName));

            }
        } else {
            Log.i(TAG, "Doc is null");
        }
    }


    private void updateDocument(Database database, String docId) {
        Document getDocument = database.getDocument(docId);

        // update doc with more data
        MutableDocument updateDoc = getDocument.toMutable();

        if (updateDoc != null) {
            try {

                updateDoc.setString("score", "1337");
                updateDoc.setString("game", "Space Invader");

                database.save(updateDoc);

            } catch (CouchbaseLiteException e) {
                Log.i(TAG, "CB-3 Error : " + e.toString());
            }

        }
    }


    private void deleteDocument(Database database, String docId) {
        Document document = database.getDocument(docId);

        try {
            database.delete(document);
        } catch (CouchbaseLiteException e) {
            Log.i(TAG, "CB-4 Error : " + e.toString());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // END
}
