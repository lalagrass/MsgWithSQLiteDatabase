package com.lalagrass.sqlitesample2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.Date;

/**
 * Created by ASUS on 9/8/2015.
 */
public class AddMsgDialog  extends DialogFragment {

    public interface dialogCallbacks {
        void onDialogOk(MsgData data);
    }

    private dialogCallbacks callbacks;
    private EditText textName;
    private EditText textMsg;
    private RadioGroup radioRole;

    public void setCallbacks(dialogCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.layout_addmsg, null);
        textName = (EditText) rootView.findViewById(R.id.editName);
        textMsg = (EditText) rootView.findViewById(R.id.editMsg);
        radioRole = (RadioGroup) rootView.findViewById(R.id.radioRole);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.string_addmsg);
        builder.setView(rootView);
        builder.setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                boolean isSender = radioRole.getCheckedRadioButtonId() == R.id.radioFrom;
                String name = "";
                String msg = "";
                Editable tname = textName.getText();
                if (tname != null)
                    name = tname.toString();
                Editable tmsg = textMsg.getText();
                if (tmsg != null)
                    msg = tmsg.toString();
                AddData(isSender, name, msg);
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void AddData(boolean sender, String name, String msg) {
        Date now = new Date();
        MsgData data = new MsgData();
        data.Name = name;
        data.Msg = msg;
        data.Date = now.getTime();
        if (sender)
            data.IsSender = 1;
        else
            data.IsSender = 0;
        if (callbacks != null)
            callbacks.onDialogOk(data);
    }
}