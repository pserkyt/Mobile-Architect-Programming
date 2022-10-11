package com.noresidue.fitnessjournal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    String userEmail = "";
    WeightHelper helper;
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Get passed through user information:
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("USER");

        // Create the weight database helper:
        helper = new WeightHelper(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Weight Log Database");
        actionBar.setDisplayHomeAsUpEnabled(true);

        init();

        /*int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 12;

        // Maybe add table rows per item?
        ArrayList<Weight> weights = listWeights();
        int rows = weights.size();
        for (int i = 0; i > rows; i++) {
            Weight row = weights.get(i);

            final TextView tv = new TextView(this);
            tv.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.LEFT);
            tv.setPadding(5, 15, 0, 15);

            tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
            tv.setText(String.valueOf(row.getId()));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            final TextView tv2 = new TextView(this);

            tv2.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tv2.setGravity(Gravity.LEFT);
            tv2.setPadding(5, 15, 0, 15);
            if (i == -1) {
                tv2.setText("Date");
                tv2.setBackgroundColor(Color.parseColor("#f7f7f7"));
            } else {
                tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                tv2.setTextColor(Color.parseColor("#000000"));
                tv2.setText(row.getUser());
            }
            final TableRow layCustomer = new TableRow(this);
            layCustomer.setOrientation(TableRow.VERTICAL);
            layCustomer.setPadding(0, 10, 0, 10);
            layCustomer.setBackgroundColor(Color.parseColor("#f8f8f8"));
            final TextView tv3 = new TextView(this);
            if (i == -1) {
                tv3.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv3.setPadding(5, 5, 0, 5);
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            } else {
                tv3.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv3.setPadding(5, 0, 0, 5);
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
            tv3.setGravity(Gravity.TOP);
            if (i == -1) {
                tv3.setText("Customer");
                tv3.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else {
                tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3.setTextColor(Color.parseColor("#000000"));
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv3.setText(row.getWeight());
            }
            layCustomer.addView(tv3);
            if (i > -1) {
                final TextView tv3b = new TextView(this);
                tv3b.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv3b.setGravity(Gravity.RIGHT);
                tv3b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv3b.setPadding(5, 1, 0, 5);
                tv3b.setTextColor(Color.parseColor("#aaaaaa"));
                tv3b.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3b.setText(row.getUser());
                layCustomer.addView(tv3b);
            }
            final TableRow layAmounts = new TableRow(this);
            layAmounts.setOrientation(TableRow.VERTICAL);
            layAmounts.setGravity(Gravity.RIGHT);
            layAmounts.setPadding(0, 10, 0, 10);
            layAmounts.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
        }*/
    }

    public void init() {
        // Header:
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table_main);
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(Color.DKGRAY);

        TextView tv0 = new TextView(this);
        tv0.setText("Weight");
        tv0.setTextColor(Color.WHITE);
        tv0.setTextSize(20);
        tv0.setGravity(Gravity.CENTER_HORIZONTAL);
        tv0.setPadding(0, 2, 10, 2);
        headerRow.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText("Date");
        tv1.setTextColor(Color.WHITE);
        tv1.setTextSize(20);
        tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        tv1.setPadding(10, 2, 10, 2);
        headerRow.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText("Actions");
        tv2.setTextColor(Color.WHITE);
        tv2.setTextSize(20);
        tv2.setGravity(Gravity.CENTER_HORIZONTAL);
        tv2.setPadding(10, 2, 0, 2);
        headerRow.addView(tv2);
        tableLayout.addView(headerRow);

        // Weights:
        ArrayList<Weight> weights = listWeights();
        int rows = weights.size();
        System.out.println("Rows: " + rows);
        for (int i = rows-1; i > -1; i--) { // descending order
            Weight weight = weights.get(i);
            System.out.println("Weight(" + i + "): " + weight.getWeight());

            // Weight:
            TableRow tableRow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText("" + weight.getWeight());
            t1v.setTextColor(Color.BLACK);
            t1v.setTextSize(18);
            t1v.setGravity(Gravity.CENTER_HORIZONTAL);
            t1v.setPadding(0, 25, 0, 0);
            tableRow.addView(t1v);
            System.out.println("Weight(" + i + "): " + weight.getWeight());

            // Date:
            TextView t2v = new TextView(this);
            t2v.setText("" + weight.getDate());
            t2v.setTextColor(Color.BLACK);
            t2v.setTextSize(18);
            t2v.setGravity(Gravity.CENTER_HORIZONTAL);
            t2v.setPadding(0, 25, 0, 0);
            tableRow.addView(t2v);
            System.out.println("Date(" + i + "): " + weight.getDate());

            // Actions:
            ImageButton editButton = new ImageButton(this);
            editButton.setScaleX(0.8f);
            editButton.setScaleY(0.8f);
            //editButton.setMaxWidth(0);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editClicked(weight.getId());
                }
            });
            ((ImageButton) editButton).setImageResource(R.drawable.edit_icon);
            //editButton.setClickable(true);
            tableRow.addView(editButton);

            ImageButton deleteButton = new ImageButton(this);
            deleteButton.setScaleX(0.8f);
            deleteButton.setScaleY(0.8f);
            //deleteButton.setMaxWidth(0);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(helper.delete(weight.getId())) {
                        Toast.makeText(ListActivity.this, "Weight deleted!", Toast.LENGTH_SHORT).show();

                        // Refresh view:
                        Intent intent = new Intent(ListActivity.this, ListActivity.class);
                        intent.putExtra("USER", userEmail); // pass email to list
                        startActivity(intent);
                    } else {
                        Toast.makeText(ListActivity.this, "Failed to delete weight!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ((ImageButton) deleteButton).setImageResource(R.drawable.delete_icon);
            //deleteButton.setClickable(true);
            tableRow.addView(deleteButton);

            // Add the new row:
            tableLayout.addView(tableRow);
        }
    }

    public ArrayList<Weight> listWeights() {
        Cursor cursor = helper.getAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(ListActivity.this, "No weights found!", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            ArrayList<Weight> weights = new ArrayList<Weight>();
            while (cursor.moveToNext()) {
                Weight temp = new Weight();

                temp.setId(Integer.parseInt(cursor.getString(0)));
                temp.setUser(cursor.getString(1));
                temp.setWeight(Integer.parseInt(cursor.getString(2)));
                temp.setDate(cursor.getString(3));

                weights.add(temp);
            }

            return weights;
        }
    }

    public void editClicked(int weightID){
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("USER", userEmail); // pass email to edit
        intent.putExtra("WEIGHT_ID", weightID); // pass weight id to edit
        startActivity(intent);
    }
}