package com.terrasport.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.terrasport.R;

/**
 * Created by jdher on 03/02/2017.
 */

public class CustomDialogView extends Dialog implements
        android.view.View.OnClickListener {

    public Dialog d;
    public EditText editNbPlacesRestantes, editNbPlacesDisponibles;
    public Button yes, no;

    public CustomDialogView(Activity a) {
        super(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_dialbox_add_event);
        editNbPlacesRestantes = (EditText) findViewById(R.id.edit_nb_places_restantes);
        editNbPlacesDisponibles = (EditText) findViewById(R.id.edit_nb_places_disponibles);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                //c.finish();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}