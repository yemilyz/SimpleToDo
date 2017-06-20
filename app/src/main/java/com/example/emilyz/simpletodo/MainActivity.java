package com.example.emilyz.simpletodo;

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
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) { //only will be executed in the event of a long click
                Log.i("MainActivity", "Item removed from list: " + position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged(); //long click was consumed
                writeItems();

                return true;
            }
        });
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
