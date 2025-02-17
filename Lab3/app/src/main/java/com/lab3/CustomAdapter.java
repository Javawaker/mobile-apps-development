package com.lab3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{
    private final Activity activity;
    private final Context context;
    private final ArrayList<String> id, name, job_title, degree, rank, photo;
    CustomAdapter(
            Activity activity, Context context, ArrayList<String> id, ArrayList<String> name,
            ArrayList<String> jobTitle, ArrayList<String> degree, ArrayList<String> rank, ArrayList<String> photo){
        this.activity = activity;
        this.context = context;
        this.id = id;
        this.name = name;
        this.job_title = jobTitle;
        this.degree = degree;
        this.rank = rank;
        this.photo = photo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.name.setText(buildSpannableString(R.string.row_name, String.valueOf(name.get(position))));
        holder.jobTitle.setText(buildSpannableString(R.string.row_job_title, String.valueOf(job_title.get(position))));
        holder.degree.setText(buildSpannableString(R.string.row_degree, String.valueOf(degree.get(position))));
        holder.rank.setText(buildSpannableString(R.string.row_rank, String.valueOf(rank.get(position))));

        String photoURL = photo.get(position);
        Glide.with(context)
                .applyDefaultRequestOptions(new RequestOptions())
                .load(photoURL)
                .error(R.drawable.default_photo)
                .into(holder.imageViewPhoto);


        holder.mainLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context,AddEditActivity.class);
            intent.putExtra(activity.getString(R.string.c_id),String.valueOf(id.get(position)));
            intent.putExtra(activity.getString(R.string.c_name),String.valueOf(name.get(position)));
            intent.putExtra(activity.getString(R.string.c_job_title),String.valueOf(job_title.get(position)));
            intent.putExtra(activity.getString(R.string.c_degree),String.valueOf(degree.get(position)));
            intent.putExtra(activity.getString(R.string.c_rank),String.valueOf(rank.get(position)));
            intent.putExtra(activity.getString(R.string.c_photo),String.valueOf(photo.get(position)));
            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name, jobTitle, degree, rank, photo;
        ImageView imageViewPhoto;
        ImageButton btnDelRV;
        LinearLayout mainLayout;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            degree = itemView.findViewById(R.id.degree);
            rank = itemView.findViewById(R.id.rank);
            photo = itemView.findViewById(R.id.photo);
            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            btnDelRV = itemView.findViewById(R.id.delBtnRV);
            btnDelRV.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.deleting)
                    .setMessage(R.string.delete_teacher)
                    .setCancelable(true)
                    .setNegativeButton(R.string.cancel,
                            (dialog, id) -> dialog.cancel())
                    .setPositiveButton(R.string.ok,
                            (dialog, id) -> {
                                DBHelper dbh = new DBHelper(activity);
                                int itemPos = getAdapterPosition();
                                String nid = CustomAdapter.this.id.get(itemPos);
                                dbh.delOneTeacher(nid);
                                activity.recreate();
                                dialog.cancel();
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private SpannableStringBuilder buildSpannableString(@StringRes int stringResId, String value) {
        String rowText = context.getResources().getString(stringResId);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(rowText);
        spannableStringBuilder.append(" ").append(value);
        return spannableStringBuilder;
    }
}