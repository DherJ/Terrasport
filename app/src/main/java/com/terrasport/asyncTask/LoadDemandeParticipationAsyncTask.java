package com.terrasport.asyncTask;

import android.os.AsyncTask;

import com.terrasport.event.AllDemandeParticipationEvent;
import com.terrasport.fragment.DemandeParticipationFragment;
import com.terrasport.model.DemandeParticipation;
import com.terrasport.model.Utilisateur;
import com.terrasport.utils.Globals;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by jdher on 21/01/2017.
 */
public class LoadDemandeParticipationAsyncTask extends AsyncTask <String, Void, List<DemandeParticipation>> {

    private final String URI = Globals.getInstance().getBaseUrl() + "demande-participation/all/utilisateur/";
    private DemandeParticipationFragment fragment;
    private Utilisateur utilisateur;

    public LoadDemandeParticipationAsyncTask(DemandeParticipationFragment fragment, Utilisateur pUtilisateur) {
        this.fragment = fragment;
        this.utilisateur = pUtilisateur;
    }

    @Override
    protected List<DemandeParticipation> doInBackground(String... params) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        AllDemandeParticipationEvent allParticipationEvent = restTemplate.getForObject( URI + this.utilisateur.getId(), AllDemandeParticipationEvent.class);
        return allParticipationEvent.getDemandeParticipations();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<DemandeParticipation> result) {
        super.onPostExecute(result);
        if(fragment != null && fragment.getActivity() != null) {
            fragment.updateListView(result);
            this.fragment = null;
        }
    }
}