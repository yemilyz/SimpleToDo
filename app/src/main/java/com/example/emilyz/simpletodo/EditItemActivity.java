package com.example.emilyz.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static com.example.emilyz.simpletodo.MainActivity.ITEM_POSITION;
import static com.example.emilyz.simpletodo.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {
    //track edit text being used
    EditText etItemText;
    //position of edited item in list
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        //resolve edit text from layout
        etItemText = (EditText) findViewById(R.id.etItemText);
        //set edit text value from intent extra
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));
        //update position from intent extra
        position = getIntent().getIntExtra(ITEM_POSITION, 0);
        //update title bar of activity
        getSupportActionBar().setTitle("Edit Item");
    }

    //function for save button
    public void onSaveItem(View v){
        //prepare for new intent
        Intent i = new Intent();
        //pass updated item text as extra
        i.putExtra(ITEM_TEXT, etItemText.getText().toString());
        //pass past item text as extra
        i.putExtra(ITEM_POSITION, position);
        //set intent as result of activity
        setResult(RESULT_OK,i);
        //close activity and redirect to main
        finish();
    }


}
