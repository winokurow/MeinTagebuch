package ilw.org.meintagebuch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ilw.org.meintagebuch.R;
import ilw.org.meintagebuch.dto.Day;
import ilw.org.meintagebuch.dto.Subject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    TextView dateEditText;
    ImageButton prevButton;
    ImageButton nextButton;
    SeekBar seekBar1;
    SeekBar seekBar2;

    EditText commentEditText;
    Date date;
    //SQLHelper db;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE d.MMMM.yyyy", Locale.GERMANY);
    private Map<String,Day> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        //db = new SQLHelper(getApplicationContext());
        //records = db.getRecords();  d
        redraw();
    }

    public void redraw() {
        String dateString = simpleDateFormat.format(date);
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

        if (records.containsKey(simpleDateFormat.format(date)))
        {
            Day current = records.get(simpleDateFormat.format(date));
            seekBar1.setProgress(current.getSubjects().get("sport").getValue());
            seekBar2.setProgress(current.getSubjects().get("hausaufgaben").getValue());
            commentEditText.setText(current.getComment());
        } else
        {
            //seekBar1.setProgress(5);
            seekBar2.setProgress(5);
        }
    }

    public void save() {
        Subject subj1 = new Subject(seekBar1.getProgress(), "");
        Subject subj2 = new Subject(seekBar2.getProgress(), "");
        Map<String, Subject> subjects = new HashMap<>();
        subjects.put("sport", subj1);
        subjects.put("hausaufgaben", subj2);

        if (records.containsKey(simpleDateFormat.format(date)))
        {
            Map<String, Subject> subjects2 = records.get(simpleDateFormat.format(date)).getSubjects();
            subjects2.putAll(subjects);
            records.put(simpleDateFormat.format(date), new Day(subjects2, commentEditText.getText().toString().trim()));
            //db.addDay(simpleDateFormat.format(date), records.get(simpleDateFormat.format(date)));

        } else {
            records.put(simpleDateFormat.format(date), new Day(subjects, commentEditText.getText().toString().trim()));
            //db.addDay(simpleDateFormat.format(date), records.get(simpleDateFormat.format(date)));
        }

    }
}