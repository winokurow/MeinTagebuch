package org.ilw.meintagebuch.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.ilw.meintagebuch.dto.SubjectMod;
import org.ilw.meintagebuch.helper.SQLHelper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import ilw.org.meintagebuch.R;

public class StatisticActivity extends AppCompatActivity {

    SQLHelper db;
    Map<Integer, SubjectMod> subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"1 Tag", "2 Tage", "3 Tage", "4 Tage", "5 Tage", "6 Tage", "7 Tage", "1 Woche", "2 Wochen", "3 Wochen", "4 Wochen", "1 Monat", "2 Monate", "3 Monate", "6 Monate", "1 Jahr", "2 Jahre"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        db = new SQLHelper(getApplicationContext());
        subjects = db.getSubjects();

        LinearLayout parent = (LinearLayout) findViewById(R.id.dynamic);
        Iterator it = subjects.entrySet().iterator();
        while (it.hasNext())
        {
            final Map.Entry pair = (Map.Entry)it.next();

            int value20 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20, getResources().getDisplayMetrics());

            LinearLayout layout2 = new LinearLayout(this);
            layout2.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(value20, 0, value20, 0);
            layoutParams.gravity = Gravity.CENTER;
            layout2.setLayoutParams(layoutParams);


            TextView editText = new TextView(this);
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(value20, 0, 0, 0);
            editText.setLayoutParams(layoutParams);
            editText.setText(((SubjectMod)pair.getValue()).getDescription());
            editText.setTextColor(Color.parseColor("#ff0099cc"));
            editText.setTextSize(22);
            editText.setMaxLines(6000);

            layout2.addView(editText);

            parent.addView(layout2);
        }

        Button abbrechenButton = (Button) findViewById(R.id.btnOK);

        abbrechenButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        Button exportButton = (Button) findViewById(R.id.btnExport);
        exportButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                try {
                    db.backupDatabase();
                } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            }
        });

        Button importButton = (Button) findViewById(R.id.btnImport);
        importButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                try {

                    db.importDB();

                    Intent returnIntent = new Intent();
                    setResult(12, returnIntent);

                    View vg = findViewById (R.id.mainLayout);
                    vg.invalidate();
                    finish();


                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
    }
}
