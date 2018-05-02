package com.example.michael.projectphase3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateContact extends AppCompatActivity {


    int ID;
    EditText editFirst,editMiddle,editLast,editPhone,editBirth, editDate, editAddress, editCity, editState, editZip;
    Button btnAddData;
    Database myDb;
    ArrayList<String> names=new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,names);


        myDb = new Database(this);

        editFirst = (EditText) findViewById(R.id.editText_First);
        editLast = (EditText) findViewById(R.id.editText_Last);
        editMiddle = (EditText) findViewById(R.id.editText_Middle);
        editPhone = (EditText) findViewById(R.id.editText_Phone);
        editBirth = (EditText) findViewById(R.id.editText_Birth);
        editDate = (EditText) findViewById(R.id.editText_Date);
        editAddress = (EditText) findViewById(R.id.editText_Address);
        editCity = (EditText) findViewById(R.id.editText_City);
        editState = (EditText) findViewById(R.id.editText_State);
        editZip = (EditText) findViewById(R.id.editText_Zip);
        // This button pushes the information into the database
        btnAddData = (Button) findViewById(R.id.button_save);


        AddData();

    }

    public  void AddData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(Integer.toString(ID),editFirst.getText().toString(),
                                editLast.getText().toString(),
                                editMiddle.getText().toString(),editPhone.getText().toString(),
                                editBirth.getText().toString(),editDate.getText().toString(), editAddress.getText().toString(),
                                editCity.getText().toString(), editState.getText().toString(), editZip.getText().toString() );
                        if(isInserted == true) {
                            Toast.makeText(UpdateContact.this, "Data Inserted", Toast.LENGTH_LONG).show();
                            add();
                            Intent intent = new Intent(UpdateContact.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(UpdateContact.this,"Data not Inserted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    // Add
    private void add()
    {
        String name=editFirst.getText().toString();
        if (!name.isEmpty() && name.length()>0)
            //ADD
            adapter.add(name);

        //REFRESH
        adapter.notifyDataSetChanged();
    }

}
