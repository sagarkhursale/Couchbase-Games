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

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private final String DATABASE = "couchbase_games";

    private Score[] scores = {
            new Score("john@example.com", "John Adams", 42),
            new Score("paul@example.com", "Paul Stone", 58),
            new Score("jane@example.com", "Jane Smith", 100),
            new Score("sally@example.com", "Sally Brown", 121)
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        createScore();
        //
    }


    private void createScore() {
        Database database = null;

        try {
            // Helper Object
            DatabaseConfiguration configuration = new DatabaseConfiguration(
                    getApplicationContext());

            database = new Database(DATABASE, configuration);
        } catch (CouchbaseLiteException e) {
            Log.i(TAG, "createScore() : " + e.toString());
            return;
        }

        // Create the document
        createHighScores(database);

        // Get and output the contents
        outputContents(database);

        // Update the document and add an attachment
        updateHighScores(database);

        // Get and output the contents
        outputContents(database);

        // end
    }


    /**
     * Creates the high scores
     *
     * @param database The CBL database
     */
    private void createHighScores(Database database) {
        for (Score score : scores) {
            // Create a new document and with the email address as the ID
            MutableDocument scoreDocument = new MutableDocument(score.getEmail());

            // Add the data for the score
            scoreDocument.setString("email", score.getEmail());
            scoreDocument.setString("name", score.getName());
            scoreDocument.setInt("score", score.getScore());

            try {
                // Save the document to the database
                database.save(scoreDocument);
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Error saving", e);
            }
        }
    }


    /**
     * Outputs the contents of the high scores
     *
     * @param database The CBL database
     */
    private void outputContents(Database database) {
        // Create a new array to score the data from the database
        ArrayList<Score> scoresFromDatabase = new ArrayList<Score>();

        for (Score score : scores) {
            // Get the document
            Document getDocument = database.getDocument(score.getEmail());

            if (getDocument != null) {
                // Create a score object from the values
                String email = getDocument.getString("email");
                String name = getDocument.getString("name");
                int scoreVal = getDocument.getInt("score");

                scoresFromDatabase.add(new Score(email, name, scoreVal));
            } else {
                Log.i(TAG, "The Document was null for " + score.getEmail());
            }
        }

        // Output all of the scores
        for (Score scoreFromDatabase : scoresFromDatabase) {
            Log.i(TAG, scoreFromDatabase.toString());
        }
    }

    /**
     * Updates the document
     *
     * @param database The CBL database
     */
    private void updateHighScores(Database database) {
        Random random = new Random();

        for (Score score : scores) {
            Document updateDocument = database.getDocument(score.getEmail());

            if (updateDocument != null) {
                try {
                    // Change to a mutable document
                    MutableDocument mutableDocument = updateDocument.toMutable();

                    // Update the document with more data
                    mutableDocument.setInt("score", random.nextInt(1000000));
                    mutableDocument.setString("date", "2015-05-01");

                    // Save the document to the database
                    database.save(mutableDocument);
                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Error saving", e);
                }
            }
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
