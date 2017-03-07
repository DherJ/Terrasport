package com.terrasport.asyncTask;

import android.os.AsyncTask;

import com.terrasport.event.AllSportEvent;
import com.terrasport.fragment.TerrainFragment;
import com.terrasport.model.Sport;
import com.terrasport.utils.Globals;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by jdher on 21/01/2017.
 */
public class LoadSportAsyncTask extends AsyncTask <String, Void, List<Sport>> {

    private final String URI = Globals.getInstance().getBaseUrl() + "sport/all";
    private TerrainFragment fragment;

    public LoadSportAsyncTask(TerrainFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected List<Sport> doInBackground(String... params) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        AllSportEvent allSportEvent = restTemplate.getForObject( URI, AllSportEvent.class);
        return allSportEvent.getSports();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Sport> result) {
        super.onPostExecute(result);
        if(fragment != null && fragment.getActivity() != null) {
            fragment.updateListSport(result);
            this.fragment = null;
        }
    }
}