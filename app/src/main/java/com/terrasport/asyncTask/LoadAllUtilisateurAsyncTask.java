package com.terrasport.asyncTask;

import android.os.AsyncTask;

import com.terrasport.event.AllUtilisateurEvent;
import com.terrasport.fragment.EvenementUtilisateurFragment;
import com.terrasport.fragment.UtilisateurFragment;
import com.terrasport.model.Utilisateur;
import com.terrasport.utils.Globals;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by jdher on 21/01/2017.
 */
public class LoadAllUtilisateurAsyncTask extends AsyncTask <String, Void, List<Utilisateur>> {

    private final String URI = Globals.getInstance().getBaseUrl() + "utilisateur/all";
    private UtilisateurFragment fragment;
    private Utilisateur utilisateur;

    public LoadAllUtilisateurAsyncTask(UtilisateurFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected List<Utilisateur> doInBackground(String... params) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        AllUtilisateurEvent allUtilisateurEvent = restTemplate.getForObject( URI, AllUtilisateurEvent.class);
        return allUtilisateurEvent.getUtilisateurs();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Utilisateur> result) {
        super.onPostExecute(result);
        if(fragment != null && fragment.getActivity() != null) {
            fragment.updateListView(result);
            this.fragment = null;
        }
    }
}