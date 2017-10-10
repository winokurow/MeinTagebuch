package org.ilw.meintagebuch.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import org.ilw.meintagebuch.dto.SubjectMod;
import org.ilw.meintagebuch.helper.SQLHelper;
import org.ilw.meintagebuch.org.ilw.meintagebuch.data.Period;

import java.io.IOException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ilw.org.meintagebuch.R;

public class StatisticActivity extends AppCompatActivity {

    SQLHelper db;
    Map<Integer, SubjectMod> subjects;
    private XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        Spinner dropdown = (Spinner)findViewById(R.id.period);
        List<Period> periodList = Arrays.asList(Period.values());

        List<String> textList = new ArrayList<>();
        for(Period period:periodList)
        {
            textList.add(period.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, textList);
        dropdown.setAdapter(adapter);

        db = new SQLHelper(getApplicationContext());
        subjects = db.getSubjects();
        List<SubjectMod> subList = new ArrayList<SubjectMod>(subjects.values());
        textList = new ArrayList<>();
        textList.add("   ");
        for(SubjectMod mod:subList)
        {
            textList.add(mod.getName());
        }
        dropdown = (Spinner)findViewById(R.id.subject1);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, textList);
        dropdown.setAdapter(adapter);

        dropdown = (Spinner)findViewById(R.id.subject2);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, textList);
        dropdown.setAdapter(adapter);

        dropdown = (Spinner)findViewById(R.id.subject3);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, textList);
        dropdown.setAdapter(adapter);

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

        drawGraph();
    }

    protected void drawGraph() {
        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.plot);

        // create a couple arrays of y-values to plot:
        final Number[] domainLabels = {1, 2, 3, 6, 7, 8, 9, 10};
        List<Number> plotList = new ArrayList<Number>();
        plotList.add(12);
        plotList.add(15);

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        XYSeries series1 = new SimpleXYSeries(plotList, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Rating");


        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        //LineAndPointFormatter series1Format =
        //        new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels);
        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.RED, Color.GREEN, Color.BLUE, null);
        LineAndPointFormatter series2Format = new LineAndPointFormatter(Color.RED, Color.GREEN, Color.BLUE, null);

        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        //series1Format.setInterpolationParams(
        //        new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
    }
}
