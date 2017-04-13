package org.ilw.meintagebuch.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.ilw.meintagebuch.dto.SubjectMod;
import org.ilw.meintagebuch.helper.SQLHelper;

import java.util.Iterator;
import java.util.Map;

import ilw.org.meintagebuch.R;

public class SettingsActivity extends AppCompatActivity {

    SQLHelper db;
    Map<Integer, SubjectMod> subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setings);
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

            CheckBox checkBox = new CheckBox(this);
            layoutParams.gravity = Gravity.CENTER;
            checkBox.setId(((Integer)pair.getKey()) + 10000);
            checkBox.setScaleX(2);
            checkBox.setScaleY(2);
            //((SubjectMod)pair.getValue()).
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

}
