package com.terrasport.asyncTask;

import android.os.AsyncTask;

import com.terrasport.event.AllEvenementEvent;
import com.terrasport.event.AllParticipationEvent;
import com.terrasport.fragment.EvenementFragment;
import com.terrasport.fragment.ParticipationAVenirFragment;
import com.terrasport.model.Evenement;
import com.terrasport.model.Participation;
import com.terrasport.model.Utilisateur;
import com.terrasport.utils.Globals;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by jdher on 21/01/2017.
 */
public class LoadParticipationAVenirAsyncTask extends AsyncTask <String, Void, List<Participation>> {

    private final String URI = Globals.getInstance().getBaseUrl() + "participation/all-a-venir/utilisateur/";
    private ParticipationAVenirFragment fragment;
    private Utilisateur utilisateur;

    public LoadParticipationAVenirAsyncTask(ParticipationAVenirFragment fragment, Utilisateur pUtilisateur) {
        this.fragment = fragment;
        this.utilisateur = pUtilisateur;
    }

    @Override
    protected List<Participation> doInBackground(String... params) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        AllParticipationEvent allParticipationEvent = restTemplate.getForObject( URI + this.utilisateur.getId(), AllParticipationEvent.class);
        return allParticipationEvent.getParticipations();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Participation> result) {
        super.onPostExecute(result);
        if(fragment != null && fragment.getActivity() != null) {
            fragment.updateListView(result);
            this.fragment = null;
        }
    }
}