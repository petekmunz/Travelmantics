package com.petermunyao.travelmantics.dialofFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.petermunyao.travelmantics.R;

import java.util.Objects;

public class UploadImageAlert extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context = getActivity();
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull(context));
        builder.setTitle(getResources().getString(R.string.alert_title))
                .setMessage(getResources().getString(R.string.alert_message))
                .setPositiveButton("OK",null);
        return builder.create();
    }
}
