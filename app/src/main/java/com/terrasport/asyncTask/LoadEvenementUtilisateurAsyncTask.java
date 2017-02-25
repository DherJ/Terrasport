package com.terrasport.asyncTask;

import android.os.AsyncTask;

import com.terrasport.event.AllEvenementEvent;
import com.terrasport.fragment.EvenementFragment;
import com.terrasport.fragment.EvenementUtilisateurFragment;
import com.terrasport.model.Evenement;
import com.terrasport.model.Utilisateur;
import com.terrasport.utils.Globals;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by jdher on 21/01/2017.
 */
public class LoadEvenementUtilisateurAsyncTask extends AsyncTask <String, Void, List<Evenement>> {

    private final String URI = Globals.getInstance().getBaseUrl() + "evenement/utilisateur/";
    private EvenementUtilisateurFragment fragment;
    private Utilisateur utilisateur;

    public LoadEvenementUtilisateurAsyncTask(EvenementUtilisateurFragment fragment, Utilisateur pUtilisateur) {
        this.fragment = fragment;
        this.utilisateur = pUtilisateur;
    }

    @Override
    protected List<Evenement> doInBackground(String... params) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        AllEvenementEvent allEvenementEvent = restTemplate.getForObject( URI + this.utilisateur.getId(), AllEvenementEvent.class);
        return allEvenementEvent.getEvenements();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Evenement> result) {
        super.onPostExecute(result);
        if(fragment != null && fragment.getActivity() != null) {
            fragment.updateListView(result);
            this.fragment = null;
        }
    }
}