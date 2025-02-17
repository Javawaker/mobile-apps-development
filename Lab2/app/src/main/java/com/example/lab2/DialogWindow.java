package com.example.lab2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.annotation.NonNull;

public class DialogWindow extends DialogFragment {
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder.setTitle(getString(R.string.info)).setMessage(getString(R.string.about)).setPositiveButton(getString(R.string.ok),null).create();
    }
}