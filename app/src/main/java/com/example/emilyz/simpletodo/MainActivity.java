package com.example.emilyz.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//declare fields here
    //called by Android when activity is created

    //a numeric code to identify any edit activity
    public final static int EDIT_REQUEST_CODE = 20;
    //keys used for passing data between activities
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";


    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) { //called when app is created
        //super class logic first
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items); //initialize adapter with item list
        lvItems = (ListView) findViewById(R.id.lvItems); //reference to the list view to wire adapter to it. Change the list view that already exists.
        lvItems.setAdapter(itemsAdapter); //connect the adapter to the list view


        //mock data
        //items.add("First item");
        //items.add("SeCoNd ItEm");

        setupListViewListener();


    }
    //other methods here
    public void onAddItem(View v){
        //add items by pressing add button
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString(); //need value as a string
        itemsAdapter.add(itemText); //add to adapter
        etNewItem.setText(""); //clear the field
        writeItems();
        Toast.makeText(getApplicationContext(), "Item is added to list.", Toast.LENGTH_SHORT).show();//pop up text box after item is added to list
    }

    private void setupListViewListener(){
        Log.i("Main Activity", "Setting up listener when on list view");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){ //long click
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) { //only will be executed in the event of a long click
                Log.i("MainActivity", "Item removed from list: " + position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged(); //long click was consumed
                writeItems();

                return true;
            }
        });

        //set up item listener for edit (regular click)
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //must perform the following three tasks
                //create new activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                //pass the data being edited
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);
                //display the activity
                startActivityForResult(i,EDIT_REQUEST_CODE);

            }
        });
    }


    //handle results from edit activity


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the edit acitivity completed ok
        if(resultCode==RESULT_OK &&requestCode == EDIT_REQUEST_CODE){
            //extracted updated item text from result intent extras
            String updatedItem=data.getExtras().getString(ITEM_TEXT);
            //extracted original position of edited item
            int position = data.getExtras().getInt(ITEM_POSITION);
            //update model with new item text at the edited position
            items.set(position, updatedItem);
            //notify the adapter that the model has changed
            itemsAdapter.notifyDataSetChanged();
            //persist with changed model
            writeItems();
            //notify the user the operation is complete
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
        }

    }

    //persistence functions
    private File getDataFile(){ //file where data is stored
        return new File (getFilesDir(),"todo.txt");
    }
    private void readItems(){ //read file
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("Main Activity", "Error reading file.", e);
            items = new ArrayList<>();
        }

    }
    private void writeItems(){ //write file
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("Main Activity", "Error writing file.", e);
        }

    }
}
