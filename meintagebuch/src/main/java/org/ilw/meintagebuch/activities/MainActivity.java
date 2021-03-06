package org.ilw.meintagebuch.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ilw.meintagebuch.dto.Day;
import org.ilw.meintagebuch.dto.Subject;
import org.ilw.meintagebuch.dto.SubjectMod;
import org.ilw.meintagebuch.helper.SQLHelper;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ilw.org.meintagebuch.R;
import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    TextView dateEditText;
    ImageButton prevButton;
    ImageButton nextButton;
    EditText commentEditText;

    Date date;
    SQLHelper db;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE d.MMMM.yyyy", Locale.GERMANY);
    private Map<String,Day> records;
    Map<Integer, String> comments;
    Map<Integer, SubjectMod> subjects;
    private int currentColor;
    private String currentBackground = "";
    Day currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new SQLHelper(getApplicationContext());
        comments = new HashMap<Integer,String>();
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
                comments = new HashMap<Integer, String>();
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
                comments = new HashMap<Integer, String>();
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, 1);
                date = c.getTime();

                redraw();
            }
        });

        commentEditText = (EditText) findViewById(R.id.generalComment);

        Button buttonSpeichern = (Button) findViewById(R.id.btnSpeichern);
        buttonSpeichern.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                save();
            }
        });

        ImageButton buttonSettings = (ImageButton) findViewById(R.id.settingsButton);
        buttonSettings.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                save();
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(myIntent, 1);
            }
        });

        ImageButton buttonStatistic = (ImageButton) findViewById(R.id.statisticButton);
        buttonStatistic.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                save();
                Intent myIntent = new Intent(MainActivity.this, StatisticActivity.class);
                startActivityForResult(myIntent, 1);
            }
        });

        Button btnPick = (Button) findViewById(R.id.btnColorPicker);

        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        records = db.getRecords();
        redraw();
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
    }

    public void redraw() {
        db = new SQLHelper(getApplicationContext());
        String dateString = simpleDateFormat.format(date);


        if (records.containsKey(dateString))
        {
            currentDay = records.get(dateString);
            currentColor = currentDay.getColor();
            commentEditText.setText(currentDay.getComment());
        } else {
            currentDay = null;
            currentColor = 16777215;
            commentEditText.setText("");
        }
        List<String> textList = new ArrayList<>();
        Field[] fields=R.drawable.class.getFields();
        for(int count=0; count < fields.length; count++){
            if (fields[count].getName().contains("texture_"))
            {
                textList.add(fields[count].getName().split("_")[1]);
            }
        }
        Spinner dropdown = (Spinner)findViewById(R.id.backgroundType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, textList);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentBackground = "texture_" + parentView.getItemAtPosition(position).toString();
                changeBackground();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        subjects = db.getSubjects("1");

        RelativeLayout centerLayout = (RelativeLayout) findViewById(R.id.centerLayout);
        //centerLayout.setBackgroundColor(currentColor);
        changeBackground();

        LinearLayout parent = (LinearLayout) findViewById(R.id.dynamic);
        parent.removeAllViews();
        Iterator it = subjects.entrySet().iterator();
        while (it.hasNext())
        {
            final Map.Entry pair = (Map.Entry)it.next();

            LinearLayout layout1 = new LinearLayout(this);
            layout1.setOrientation(LinearLayout.VERTICAL);
            int value75 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,75, getResources().getDisplayMetrics());
            int value20 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20, getResources().getDisplayMetrics());
            int value08 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,8, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(value20, 0, value20, 0);
            layoutParams.gravity = Gravity.CENTER;
            layout1.setLayoutParams(layoutParams);
            layout1.setBackgroundResource(R.drawable.border);

            TextView textView = new TextView(this);
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(value08, 0, 0, 0);
            layoutParams.gravity = Gravity.CENTER;
            textView.setLayoutParams(layoutParams);
            textView.setText(((SubjectMod)pair.getValue()).getDescription());
            //textView.setTextColor(Color.parseColor("#ff0099cc"));
            textView.setTextSize(20);
            textView.setMaxLines(6000);

            layout1.addView(textView);

            LinearLayout layout2 = new LinearLayout(this);
            layout2.setOrientation(LinearLayout.HORIZONTAL);
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(value20, 0, value20, 1);
            layoutParams.gravity = Gravity.CENTER;
            layout2.setLayoutParams(layoutParams);

            SeekBar seekBar = new SeekBar(this);
            layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 80);
            layoutParams.setMargins(value08, 0, 0, 0);
            layoutParams.gravity = Gravity.CENTER;
            seekBar.setLayoutParams(layoutParams);
            seekBar.setMax(10);
            seekBar.setId(((Integer)pair.getKey()) + 10000);
            seekBar.setProgress(0);

            layout2.addView(seekBar);

            ImageButton button = new ImageButton(this);
            button.setBackgroundColor(currentColor);
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            button.setLayoutParams(layoutParams);
            button.setImageResource(R.drawable.comment);
            button.setId(((Integer)pair.getKey()) + 20000);
            button.setContentDescription("Kommentar hinzufügen");
            button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    onShowPopup(view, (Integer)pair.getKey());
                }
            });

            layout2.addView(button);

            layout1.addView(layout2);
            parent.addView(layout1);
        }
        dateEditText.setText(dateString);

        if (simpleDateFormat.format(date).equals(simpleDateFormat.format(new Date()))) {
            nextButton.setEnabled(false);
            nextButton.setAlpha(.5f);
            nextButton.setClickable(false);
        } else {
            nextButton.setEnabled(true);
            nextButton.setAlpha(1f);
            nextButton.setClickable(true);
        }

        it = subjects.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry pair = (Map.Entry) it.next();
            int id = ((Integer) pair.getKey());
            SeekBar seekBar = (SeekBar) findViewById(id + 10000);
            if ((currentDay != null) && (records.get(simpleDateFormat.format(date)).getSubjects().containsKey(id))) {
                Day current = records.get(simpleDateFormat.format(date));

                seekBar.setProgress(current.getSubjects().get(id).getValue());

                comments.put(id, current.getSubjects().get(id).getComments());

                commentEditText.setText(current.getComment());

            } else {

                seekBar.setProgress(5);
            }

        }

        it = comments.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry pair = (Map.Entry) it.next();
            int id = ((Integer) pair.getKey());
            ImageButton button = (ImageButton) findViewById(id + 20000);
            if (button != null) {
                button.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        Toast.makeText(v.getContext(), (String) pair.getValue(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
            button.setImageResource(R.drawable.commentset);
        }

    }
    public void save() {
        String comment = "";
        Map<Integer, Subject> subject = new HashMap<>();
        Iterator it = subjects.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry pair = (Map.Entry) it.next();
            int id = ((Integer) pair.getKey());
            SeekBar seekBar = (SeekBar) findViewById(id + 10000);
            Subject subj = new Subject(seekBar.getProgress(), comments.get(id));
            subject.put(id, subj);
        }
        db = new SQLHelper(getApplicationContext());
        if (records.containsKey(simpleDateFormat.format(date)))
        {
            Map<Integer, Subject> subjects2 = records.get(simpleDateFormat.format(date)).getSubjects();
            subjects2.putAll(subject);
            records.put(simpleDateFormat.format(date), new Day(subjects2, commentEditText.getText().toString().trim(), currentColor));
            db.addDay(simpleDateFormat.format(date), records.get(simpleDateFormat.format(date)));

        } else {
            records.put(simpleDateFormat.format(date), new Day(subject, commentEditText.getText().toString().trim(), currentColor));
            db.addDay(simpleDateFormat.format(date), records.get(simpleDateFormat.format(date)));
        }
    }

    // call this method when required to show popup
    public void onShowPopup(View v, final Integer elementName){

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
                                redraw();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if(resultCode == RESULT_OK){

                recreate();
            }
            if (resultCode == RESULT_CANCELED) {
                //Do nothing?
            }
            if (resultCode == 12) {
                db = new SQLHelper(getApplicationContext());
                records = db.getRecords();
                redraw();
            }

        }
    }//onActivityResult

    private void openDialog() {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, currentColor, false, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                currentColor = color;
                save();
                redraw();

            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(getApplicationContext(), "Action canceled!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void changeBackground()
    {
        if (!(currentBackground.isEmpty()))
        {
            int resid = this.getResources().getIdentifier(currentBackground, "drawable", this.getPackageName());
            RelativeLayout centerLayout = (RelativeLayout) findViewById(R.id.centerLayout);
            centerLayout.setBackground(getResources().getDrawable(resid));
        }
    }
}