package org.ilw.meintagebuch.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.ilw.meintagebuch.dto.SubjectMod;
import org.ilw.meintagebuch.helper.SQLHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ilw.org.meintagebuch.R;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "Settings Activity";
    ImageButton addButton;
    SQLHelper db;
    Map<Integer, SubjectMod> subjects;
    Map<Integer, SubjectMod> subjectsInit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setings);

        addButton = (ImageButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                onShowPopup(view);
            }
        });


        db = new SQLHelper(getApplicationContext());
        subjects = db.getSubjects();
        subjectsInit = new HashMap<>();

        for (Map.Entry<Integer, SubjectMod> entry : subjects.entrySet()) {
            try {
                subjectsInit.put(entry.getKey(), (SubjectMod)(entry.getValue()).clone());
            } catch (CloneNotSupportedException e)
            {
                Log.e(TAG, e.getMessage());
            }

        }

        Button abbrechenButton = (Button) findViewById(R.id.btnAbbrechen);

        abbrechenButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();

            }
        });

        Button saveButton = (Button) findViewById(R.id.btnSpeichern);

        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                for (Map.Entry<Integer, SubjectMod> entry : subjects.entrySet()) {
                    if (!(subjectsInit.get(entry.getKey()).equals(subjects.get(entry.getKey()))))
                    {
                        db.updateSubject(((Integer)(entry.getKey())).toString(), subjects.get(entry.getKey()).getName(), subjects.get(entry.getKey()).getStatus(), subjects.get(entry.getKey()).getDescription());
                    }
                }
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();

            }
        });

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

            CheckBox checkBox = new CheckBox(this);
            layoutParams.gravity = Gravity.CENTER;
            checkBox.setId(((Integer)pair.getKey()) + 10000);
            checkBox.setScaleX(2);
            checkBox.setScaleY(2);
            String status = ((SubjectMod)pair.getValue()).getStatus();
            if (status.equals("1")) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        subjects.get(pair.getKey()).setStatus("1");
                    } else {
                        subjects.get(pair.getKey()).setStatus("0");
                    }
                }
            });
            byte a;
            a=1;
            final char test='1';
            switch (a)
            {
                case test:
            }
            layout2.addView(checkBox);
            EditText editText = new EditText(this);
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
    }

    // call this method when required to show popup
    public void onShowPopup(View v){

        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate the custom popup layout
        View inflatedView = layoutInflater.inflate(R.layout.popup_layout, null, false);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(inflatedView);

        final EditText userInput = (EditText) inflatedView
                .findViewById(R.id.editTextDialogUserInput);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                db.addSubject(userInput.getText().toString(), "1", userInput.getText().toString());
                                recreate();
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
}
