package com.terrasport.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.google.gson.Gson;
import com.terrasport.R;
import com.terrasport.event.AllParticipationEvent;
import com.terrasport.fragment.DemandeParticipationFragment;
import com.terrasport.fragment.EvenementFragment;
import com.terrasport.fragment.EvenementUtilisateurFragment;
import com.terrasport.fragment.ParticipationAVenirFragment;
import com.terrasport.fragment.TerrainFragment;
import com.terrasport.model.DemandeParticipation;
import com.terrasport.model.Evenement;
import com.terrasport.model.Participation;
import com.terrasport.model.Utilisateur;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ParticipationAVenirFragment.OnListFragmentInteractionListener, EvenementFragment.OnListFragmentInteractionListener, DemandeParticipationFragment.OnListFragmentInteractionListener, EvenementUtilisateurFragment.OnListFragmentInteractionListener, TerrainFragment.OnFragmentInteractionListener {

        private Toolbar toolbar;
        private NavigationView navigationView;
        private Fragment fragment;
        private AllParticipationEvent participationsEvent;

        private Utilisateur utilisateur;

        private Dialog dialog;

        public void replaceFragment(Fragment otherFragment) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_menu, otherFragment).commit();
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            DrawerLayout drawerLayout;
            super.onCreate(savedInstanceState);

            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            Gson gson = new Gson();
            utilisateur = gson.fromJson(getIntent().getStringExtra("utilisateur"), Utilisateur.class);

            setContentView(R.layout.activity_main);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.app_name);
            // toolbar.setLogo(R.drawable.logo_appbar);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

            };
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            String jsonObject = null;
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                jsonObject = extras.getString("dashboard");
            }

            participationsEvent = new Gson().fromJson(jsonObject, AllParticipationEvent.class);

            try {
                fragment = ParticipationAVenirFragment.newInstance(participationsEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_menu, fragment).commit();
        }

        @Override
        public void onBackPressed() {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Fragment fragment = null;
            Class fragmentClass = null;

            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if(id == R.id.nav_logout) {
                backToLoginActivity();
            } else {
                if (id == R.id.nav_evenement) {
                    fragmentClass = EvenementFragment.class;
                } else if (id == R.id.nav_mes_evenements) {
                    fragmentClass = EvenementUtilisateurFragment.class;
                } else if (id == R.id.nav_participation) {
                    fragmentClass = ParticipationAVenirFragment.class;
                } else if (id == R.id.nav_demandes_participation) {
                    fragmentClass = DemandeParticipationFragment.class;
                } else if (id == R.id.nav_map_terrains) {
                    fragmentClass = TerrainFragment.class;
                }


                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    String utilisateurJson = gson.toJson(utilisateur);
                    bundle.putString("utilisateur", utilisateurJson);
                    fragment.setArguments(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_menu, fragment).commit();

                item.setChecked(true);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
            return true;
        }

    public void backToLoginActivity() {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Participation item) {

    }

    @Override
    public void onListFragmentInteraction(Evenement item) {

    }

    @Override
    public void onListFragmentInteraction(DemandeParticipation item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 100:
                dialog=new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.view_dialbox_add_event);
                /*
                Button restart=(Button)dialog.findViewById(R.id.restart);
                restart.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        //whatever code you want to execute on restart
                    }
                });
                */
                break;
            default: break;
        }
        return dialog;
    }
}