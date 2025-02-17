package com.lab3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class AddEditActivity extends AppCompatActivity {

    EditText Name, Job_title, Degree, Rank, Photo;
    TextView  Title_name, Title_job_title, Title_degree, Title_rank, Title_photo;
    Button saveBtn, delBtn, cancelBtn;
    String id, name, jobTitle, degree, rank, photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        Name = findViewById(R.id.name);
        Job_title = findViewById(R.id.jobTitle);
        Degree = findViewById(R.id.degree);
        Rank = findViewById(R.id.rank);
        Photo = findViewById(R.id.photo);

        saveBtn = findViewById(R.id.saveBtn);
        delBtn = findViewById(R.id.delBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        id = getIntent().getStringExtra(getString(R.string.c_id));
        Title_name = findViewById(R.id.title_name);
        Title_job_title = findViewById(R.id.title_job_title);
        Title_degree = findViewById(R.id.title_degree);
        Title_rank = findViewById(R.id.title_rank);
        Title_photo = findViewById(R.id.title_photo);

        ActionBar ab = getSupportActionBar();

        if (getIntent().hasExtra(getString(R.string.c_id))) {
            Objects.requireNonNull(ab).setTitle(R.string.editing);
            delBtn.setVisibility(View.VISIBLE);
            getAndSetIntentData();
        } else {
            Objects.requireNonNull(ab).setTitle(R.string.adding);
            delBtn.setVisibility(View.INVISIBLE);
        }

        saveBtn.setOnClickListener(view -> {
            DBHelper dbh = new DBHelper(AddEditActivity.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(AddEditActivity.this);
            id = getIntent().getStringExtra(getString(R.string.c_id));
            if (getIntent().hasExtra(getString(R.string.c_id))) {
                id = getIntent().getStringExtra(getString(R.string.c_id));
                if (
                        Name.getText().toString().isEmpty() ||
                        Job_title.getText().toString().isEmpty() ||
                        Degree.getText().toString().isEmpty() ||
                        Rank.getText().toString().isEmpty()
                ){
                    builder.setTitle(R.string.error)
                            .setMessage(R.string.errormessage1)
                            .setPositiveButton(R.string.ok, null)
                            .create();
                    builder.show();
                }
                else {
                    dbh.editData(id,
                        Name.getText().toString().trim(),
                        Job_title.getText().toString().trim(),
                        Degree.getText().toString().trim(),
                        Rank.getText().toString().trim(),
                        Photo.getText().toString().trim()
                    );
                    finish();
                }
            }
            else {
                if (
                        Name.getText().toString().isEmpty() ||
                        Job_title.getText().toString().isEmpty() ||
                        Degree.getText().toString().isEmpty() ||
                        Rank.getText().toString().isEmpty()
                ){
                    builder.setTitle(R.string.error)
                            .setMessage(R.string.errormessage1)
                            .setPositiveButton(R.string.ok, null)
                            .create();
                    builder.show();
                }
                else {
                    dbh.addTeacher(
                            Name.getText().toString().trim(),
                            Job_title.getText().toString().trim(),
                            Degree.getText().toString().trim(),
                            Rank.getText().toString().trim(),
                            Photo.getText().toString()
                    );
                    finish();
                }
            }
            setResult(1, null);
        });

        delBtn.setOnClickListener(view -> {
            if (getIntent().hasExtra(getString(R.string.c_id))) {
                String nid = id;
                AlertDialog.Builder builder = new AlertDialog.Builder(AddEditActivity.this);
                builder.setTitle(R.string.deleting)
                        .setMessage(R.string.delete_teacher)
                        .setCancelable(true)
                        .setNegativeButton(R.string.cancel,
                                (dialog, id) -> dialog.cancel())
                        .setPositiveButton(R.string.ok,
                                (dialog, id) -> {
                                    DBHelper dbh = new DBHelper(AddEditActivity.this);
                                    dbh.delOneTeacher(nid);
                                    dialog.cancel();
                                    setResult(1, null);
                                    finish();
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        cancelBtn.setOnClickListener(v -> finish());
    }
    void getAndSetIntentData(){
        if(getIntent().hasExtra(getString(R.string.c_id))){
            id = getIntent().getStringExtra(getString(R.string.c_id));
            name = getIntent().getStringExtra(getString(R.string.c_name));
            jobTitle = getIntent().getStringExtra(getString(R.string.c_job_title));
            degree = getIntent().getStringExtra(getString(R.string.c_degree));
            rank = getIntent().getStringExtra(getString(R.string.c_rank));
            photo = getIntent().getStringExtra(getString(R.string.c_photo));

            Name.setText(name);
            Job_title.setText(jobTitle);
            Degree.setText(degree);
            Rank.setText(rank);
            Photo.setText(photo);
        }
        else Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
    }
}