package com.example.android.scanos2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ExampleDialog extends AppCompatDialogFragment {
    private EditText editFilename;
    private ExampleDialogListener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view)
                .setTitle("Name")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String filename= editFilename.getText().toString();
                        listener.applyTexts(filename);
                    }
                });
        editFilename= view.findViewById(R.id.editFilename);
    return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener{
        void applyTexts(String filename);
    }
}
