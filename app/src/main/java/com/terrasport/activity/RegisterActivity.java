package com.terrasport.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.terrasport.R;
import com.terrasport.model.Utilisateur;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserRegisterTask mAuthTask = null;
    private Utilisateur utilisateur;

    // UI references.
    private View mProgressView;
    private View mLoginFormView;

    private EditText nomView;
    private EditText prenomView;
    private EditText mailView;
    private EditText loginView;
    private EditText ageView;
    private EditText passwordView;
    private EditText passwordValidationView;
    private Spinner selectSexe;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        utilisateur = new Utilisateur();
        nomView = (EditText) findViewById(R.id.input_nom);
        prenomView = (EditText) findViewById(R.id.input_prenom);
        mailView = (EditText) findViewById(R.id.input_email);
        ageView = (EditText) findViewById(R.id.input_age);
        loginView = (EditText) findViewById(R.id.input_login);
        passwordView = (EditText) findViewById(R.id.input_password);
        passwordValidationView = (EditText) findViewById(R.id.input_password_repeat);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        selectSexe = (Spinner) findViewById(R.id.utilisateur_select_sexe);
        selectSexe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                utilisateur.setSexe(selectSexe.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        nomView.setError(null);
        prenomView.setError(null);
        mailView.setError(null);
        ageView.setError(null);
        loginView.setError(null);
        passwordView.setError(null);
        passwordValidationView.setError(null);

        // Store values at the time of the login attempt.
        String nom = nomView.getText().toString();
        String prenom = prenomView.getText().toString();
        String mail = mailView.getText().toString();
        String age = ageView.getText().toString();
        String login = loginView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordValidation = passwordValidationView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(nom)) {
            nomView.setError(getString(R.string.error_field_required));
            focusView = nomView;
            cancel = true;
        }
        else if (TextUtils.isEmpty(prenom)) {
            prenomView.setError(getString(R.string.error_field_required));
            focusView = prenomView;
            cancel = true;
        }
        else if (TextUtils.isEmpty(age)) {
            ageView.setError(getString(R.string.error_field_required));
            focusView = ageView;
            cancel = true;
        }
        else if (TextUtils.isEmpty(mail)) {
            mailView.setError(getString(R.string.error_field_required));
            focusView = mailView;
            cancel = true;
        } // Check for a valid email address.
          else  if (!isEmailValid(mail)) {
            mailView.setError(getString(R.string.error_invalid_email));
            focusView = mailView;
            cancel = true;
        }
        else if (TextUtils.isEmpty(login)) {
            loginView.setError(getString(R.string.error_field_required));
            focusView = loginView;
            cancel = true;
        }
        else if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        } else
           // Check for a valid password, if the user entered one.
           if ( !TextUtils.isEmpty(password) && !isPasswordValid(password) ) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }
        else if ( !TextUtils.isEmpty(password) && isPasswordValid(password) && !isPasswordValidationValid(password, passwordValidation) ) {
            passwordValidationView.setError(getString(R.string.error_invalid_password));
            focusView = passwordValidationView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            utilisateur.setNom(nomView.getText().toString());
            utilisateur.setPrenom(prenomView.getText().toString());
            utilisateur.setEmail(mailView.getText().toString());
            utilisateur.setSexe(selectSexe.getSelectedItem().toString());
            utilisateur.setAge(Integer.parseInt(ageView.getText().toString()));
            utilisateur.setLogin(login);
            utilisateur.setPassword(password);

            mAuthTask = new UserRegisterTask(utilisateur);
            mAuthTask.execute((String) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean isPasswordValidationValid(String password, String passwordValidation) {
        return password.equals(passwordValidation);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Register Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Object, Object, ResponseEntity<ResponseEntity>> {

        private Utilisateur utilisateur;

        UserRegisterTask(Utilisateur pUtilisateur) {
            utilisateur = pUtilisateur;
        }

        @Override
        protected ResponseEntity<ResponseEntity> doInBackground(Object... params) {
            // TODO: attempt authentication against a network service.

            try {

                String uriHomeJerome = new String("http://192.168.1.24:8080/utilisateur/sauvegarder");
                // String uriFacJerome = new String("http://172.19.137.107:8080/utilisateur/sauvegarder");

                // String uriHomeJulien = new String("http://192.168.1.24:8080/utilisateur/sauvegarder");
                // String uriFacJulien = new String("http://172.19.137.107:8080/utilisateur/sauvegarder");

                RestTemplate rt = new RestTemplate();

                rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                rt.getMessageConverters().add(new StringHttpMessageConverter());

                Thread.sleep(2000);

                return rt.postForEntity(uriHomeJerome, utilisateur, ResponseEntity.class);
                //return rt.postForEntity(uriFacJerome, utilisateur, ResponseEntity.class);

                //return rt.postForEntity(uriHomeJulien, utilisateur, ResponseEntity.class);
                //return rt.postForEntity(uriFacJulien, utilisateur, ResponseEntity.class);

            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}