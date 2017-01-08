package com.terrasport.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.terrasport.R;
import com.terrasport.model.Utilisateur;

public class MainActivity extends AppCompatActivity {

    private TextView nomTextView;
    private TextView prenomTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        String jsonMyObject = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("utilisateur");
        }
        Utilisateur utilisateur = new Gson().fromJson(jsonMyObject, Utilisateur.class);

        nomTextView = (TextView) findViewById(R.id.nomtextView);
        prenomTextView = (TextView) findViewById(R.id.prenomtextView);

        nomTextView.setText(utilisateur.getNom());
        prenomTextView.setText(utilisateur.getPrenom());
    }
}
