package com.terrasport.asyncTask;

import android.os.AsyncTask;

import com.terrasport.event.AllEvenementEvent;
import com.terrasport.fragment.EvenementFragment;
import com.terrasport.model.Evenement;
import com.terrasport.utils.Globals;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by jdher on 21/01/2017.
 */
public class LoadEvenementAsyncTask extends AsyncTask <String, Void, List<Evenement>> {

    private final String URI = Globals.getInstance().getBaseUrl() + "evenement/all-a-venir/utilisateur/3";
    private EvenementFragment fragment;

    public LoadEvenementAsyncTask(EvenementFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected List<Evenement> doInBackground(String... params) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        AllEvenementEvent allEvenementEvent = restTemplate.getForObject( URI, AllEvenementEvent.class);
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