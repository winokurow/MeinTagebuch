package org.ilw.meintagebuch.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.ilw.meintagebuch.dto.Day;
import org.ilw.meintagebuch.dto.Subject;
import org.ilw.meintagebuch.helper.SQLHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ilw.org.meintagebuch.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    TextView dateEditText;
    ImageButton prevButton;
    ImageButton nextButton;
    SeekBar seekBar1;
    SeekBar seekBar2;
    ImageButton commentSubj1Button;
    EditText commentEditText;
    Date date;
    SQLHelper db;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE d.MMMM.yyyy", Locale.GERMANY);
    private Map<String,Day> records;
    Map<String, String> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        comments = new HashMap<String,String>();
        records = new HashMap<String,Day>();
        if (date == null) {
            date = new Date();
        }
        dateEditText = (TextView) findViewById(R.id.date);

        prevButton = (ImageButton) findViewById(R.id.previousDateButton);
        nextButton = (ImageButton) findViewById(R.id.nextDateButton);

        prevButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                save();
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, -1);
                date = c.getTime();

                redraw();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                save();
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, 1);
                date = c.getTime();

                redraw();
            }
        });

        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);

        commentEditText = (EditText) findViewById(R.id.generalComment);

        Button buttonSpeichern = (Button) findViewById(R.id.btnSpeichern);
        buttonSpeichern.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                save();
            }
        });

        commentSubj1Button = (ImageButton) findViewById(R.id.coment1Button);
        commentSubj1Button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                onShowPopup(view, "sport");
            }
        });

        db = new SQLHelper(getApplicationContext());
        records = db.getRecords();
        redraw();
    }

    public void redraw() {
        String dateString = simpleDateFormat.format(date);
        dateEditText.setText(dateString);
        comments = new HashMap<String,String>();
        if (simpleDateFormat.format(date).equals(simpleDateFormat.format(new Date()))) {
            nextButton.setEnabled(false);
            nextButton.setAlpha(.5f);
            nextButton.setClickable(false);
        } else {
            nextButton.setEnabled(true);
            nextButton.setAlpha(1f);
            nextButton.setClickable(true);
        }

        if (records.containsKey(simpleDateFormat.format(date)))
        {
            Day current = records.get(simpleDateFormat.format(date));
            seekBar1.setProgress(current.getSubjects().get("sport").getValue());
            seekBar2.setProgress(current.getSubjects().get("hausaufgaben").getValue());
            comments.put("sport", current.getSubjects().get("sport").getComments());
            comments.put("hausaufgaben", current.getSubjects().get("hausaufgaben").getComments());


            commentEditText.setText(current.getComment());

        } else
        {
            seekBar1.setProgress(5);
            seekBar2.setProgress(5);
        }
    }

    public void save() {
        String comment = "";
            Subject subj1 = new Subject(seekBar1.getProgress(), comments.get("sport"));
        Subject subj2 = new Subject(seekBar2.getProgress(), comments.get("hausaufgaben"));
        Map<String, Subject> subjects = new HashMap<>();
        subjects.put("sport", subj1);
        subjects.put("hausaufgaben", subj2);

        if (records.containsKey(simpleDateFormat.format(date)))
        {
            Map<String, Subject> subjects2 = records.get(simpleDateFormat.format(date)).getSubjects();
            subjects2.putAll(subjects);
            records.put(simpleDateFormat.format(date), new Day(subjects2, commentEditText.getText().toString().trim()));
            db.addDay(simpleDateFormat.format(date), records.get(simpleDateFormat.format(date)));

        } else {
            records.put(simpleDateFormat.format(date), new Day(subjects, commentEditText.getText().toString().trim()));
            db.addDay(simpleDateFormat.format(date), records.get(simpleDateFormat.format(date)));
        }

    }

    // call this method when required to show popup
    public void onShowPopup(View v, final String elementName){

        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate the custom popup layout
        View inflatedView = layoutInflater.inflate(R.layout.popup_layout, null, false);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(inflatedView);

        final EditText userInput = (EditText) inflatedView
                .findViewById(R.id.editTextDialogUserInput);
        userInput.setText(comments.get(elementName));
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                    comments.put(elementName, userInput.getText().toString());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    void setSimpleList(ListView listView){

        ArrayList<String> contactsList = new ArrayList<String>();

        for (int index = 0; index < 10; index++) {
            contactsList.add("I am @ index " + index + " today " + Calendar.getInstance().getTime().toString());
        }

        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                R.layout.popup_layout, android.R.id.text1, contactsList));
    }
}