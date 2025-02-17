package com.lab3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    RecyclerView mainList;
    Button AddBtn, DelAllBtn;
    DBHelper DBH;
    ArrayList<String> id, name, job_title, degree, rank, photo;
    CustomAdapter customAdapter;
    ActivityResultLauncher<Intent> addEditActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> mainListUpdate()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainList = findViewById(R.id.MainList);
        AddBtn = findViewById(R.id.AddBtn);
        DelAllBtn = findViewById(R.id.DelAllBtn);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.main_label);
        AddBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            addEditActivityLauncher.launch(intent);
        });

        DelAllBtn.setOnClickListener(view -> {
            if(customAdapter.getItemCount() >0 ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.deleting)
                        .setMessage(R.string.delete_all_teachers)
                        .setCancelable(true)
                        .setNegativeButton(R.string.no,
                                (dialog, id) -> dialog.cancel())
                        .setPositiveButton(R.string.yes,
                                (dialog, id) -> {
                                    DBH = new DBHelper(MainActivity.this);
                                    DBH.delAllTeachers();
                                    mainListUpdate();
                                    dialog.cancel();
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        DBH = new DBHelper(MainActivity.this);
        id = new ArrayList<>();
        name = new ArrayList<>();
        job_title = new ArrayList<>();
        degree = new ArrayList<>();
        rank = new ArrayList<>();
        photo = new ArrayList<>();
        fillArrays();
        customAdapter = new CustomAdapter(MainActivity.this,this, id, name, job_title, degree, rank, photo);
        mainList.setAdapter(customAdapter);
        mainList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        if (requestCode == 1) {
            mainListUpdate();
        }
    }

    void mainListUpdate(){
        fillArrays();
        mainList.setAdapter(customAdapter);
    }

    void fillArrays(){
        Cursor cursor = DBH.loadData();
        id.clear();
        name.clear();
        job_title.clear();
        degree.clear();
        rank.clear();
        photo.clear();
        if(cursor.getCount() == 0){
            Toast.makeText(this, R.string.no_teachers, Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                job_title.add(cursor.getString(2));
                degree.add(cursor.getString(3));
                rank.add(cursor.getString(4));
                photo.add(cursor.getString(5));
            }
        }
    }
}