package com.appsxhone.wordmeaning;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView topScoreTextView, topHintTextView, answerTextView, meaningTextView;
    String word, meaning, shuffled;
    int level, score, hint;
    Context context = this;
    Cursor cursor;
    final DatabaseOperations databaseOperations = new DatabaseOperations(context);
    private UserPreferences userPreferences;
    Intent intent;
    RelativeLayout relativeLayout;
    List<String> letters;

    SoundPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        relativeLayout = (RelativeLayout) findViewById(R.id.rl);

        soundPlayer = new SoundPlayer(this);
        soundPlayer.resumeSound(this);

        intent = getIntent();
        level = intent.getIntExtra("LEVEL", 1);

        databaseCheckandCopy();

        topScoreTextView = (TextView) findViewById(R.id.topScore);
        userPreferences = UserPreferences.getInstance(getApplicationContext());
        score = userPreferences.getScore();
        topScoreTextView.setText(String.valueOf(score));
        topScoreTextView.setTypeface(Typeface.createFromAsset(getAssets(), "ARCENA.ttf"));


        topHintTextView = (TextView) findViewById(R.id.topHint);
        hint = userPreferences.getHint();
        topHintTextView.setText(String.valueOf(hint));
        topHintTextView.setTypeface(Typeface.createFromAsset(getAssets(), "ARCENA.ttf"));

        meaningTextView = (TextView) findViewById(R.id.txtMeaning);
        meaningTextView.setTypeface(Typeface.createFromAsset(getAssets(), "ARCENA.ttf"));
        answerTextView = (TextView) findViewById(R.id.txtWord);
        answerTextView.setTypeface(Typeface.createFromAsset(getAssets(), "ARCENA.ttf"));


        topHintTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hint > 0) {
                    hint--;
                    answerTextView.setText((String) answerTextView.getText() + word.charAt(answerTextView.length()));
                    topHintTextView.setText(String.valueOf(hint));
                    userPreferences.updateHint(hint);
                    if (answerTextView.getText().length() == word.length()) {
                        checkAnswer();
                    }
                } else {
                    //show hint buy dialog
                }
            }
        });


        gameOnce();

        backButton = (ImageButton) findViewById(R.id.topBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }


    void gameOnce() {
        cursor = databaseOperations.getInformation(databaseOperations, level);
        cursor.moveToFirst();
        meaning = cursor.getString(0);
        word = cursor.getString(1);

        letters = Arrays.asList(word.split(""));
        Collections.shuffle(letters);
        shuffled = "";
        for (String letter : letters) {
            shuffled += letter;
        }

//        Toast.makeText(GameActivity.this, word, Toast.LENGTH_SHORT).show();


        meaningTextView.setText(meaning);

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        if (level == 1) {
            gridview.setNumColumns(2);
        } else if (level == 2) {
            gridview.setNumColumns(3);
        } else if (level == 3) {
            gridview.setNumColumns(3);
        } else {
            gridview.setNumColumns(4);
        }
        gridview.setAdapter(new ImageAdapter(this, shuffled, level));

        do {
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    answerTextView.setText((String) answerTextView.getText() + shuffled.charAt(position));
                    checkAnswer();
                }
            });

        } while (answerTextView.getText().equals(meaning));
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundPlayer.resumeSound(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundPlayer.pauseSound(this);
    }

    void databaseCheckandCopy() {
        DatabaseOperations myDbHelper = new DatabaseOperations(this);

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }
    }

    @Override
    public void onBackPressed() {
        Snackbar snackbar = Snackbar
                .make(relativeLayout, "Do you want to exit?", Snackbar.LENGTH_LONG)
                .setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), StartActivity.class));
                        finish();
                    }
                });
        // Changing message text color
        snackbar.setActionTextColor(Color.GREEN);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.CYAN);
        sbView.setBackgroundColor(Color.RED);
        snackbar.show();
    }

    public void successDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_success);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tv = (TextView) dialog.findViewById(R.id.successText);
        tv.setText("Correct Answer!");
        tv.setTypeface(Typeface.createFromAsset(this.getAssets(), "ARCENA.ttf"));

        Button btnPlayAgain = (Button) dialog.findViewById(R.id.playAgain);
        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gameOnce();
                dialog.dismiss();
            }
        });
        Button btnPlayLater = (Button) dialog.findViewById(R.id.playLater);
        btnPlayLater.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StartActivity.class));
                finish();
            }
        });

        dialog.show();
    }

    public void checkAnswer() {
        if (answerTextView.getText().equals(word)) {
            Toast.makeText(GameActivity.this, "Correct Answer", Toast.LENGTH_LONG).show();
            databaseOperations.updateInformation(databaseOperations, word);

            score++;

            if (score > userPreferences.getScore()) {
                userPreferences.updateScore(score);
            }

            userPreferences.updateProgress();

            topScoreTextView.setText(String.valueOf(score));
            answerTextView.setText("");

            successDialog();

        } else if (answerTextView.getText().length() == shuffled.length()) {
            answerTextView.setText("");
            Toast.makeText(GameActivity.this, "Wrong Answer", Toast.LENGTH_LONG).show();
        }
    }
}
