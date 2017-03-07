package com.terrasport.asyncTask;

import android.os.AsyncTask;

import com.terrasport.event.AllNiveauEvent;
import com.terrasport.fragment.TerrainFragment;
import com.terrasport.model.Niveau;
import com.terrasport.utils.Globals;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by jdher on 21/01/2017.
 */
public class LoadNiveauAsyncTask extends AsyncTask <String, Void, List<Niveau>> {

    private final String URI = Globals.getInstance().getBaseUrl() + "niveau/all";
    private TerrainFragment fragment;

    public LoadNiveauAsyncTask(TerrainFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected List<Niveau> doInBackground(String... params) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        AllNiveauEvent allNiveauEvent = restTemplate.getForObject( URI, AllNiveauEvent.class);
        return allNiveauEvent.getNiveaux();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Niveau> result) {
        super.onPostExecute(result);
        if(fragment != null && fragment.getActivity() != null) {
            fragment.updateListNiveaux(result);
            this.fragment = null;
        }
    }
}