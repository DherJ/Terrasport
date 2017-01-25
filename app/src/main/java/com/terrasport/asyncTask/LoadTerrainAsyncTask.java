package com.terrasport.asyncTask;

import android.os.AsyncTask;

import com.terrasport.event.AllTerrainEvent;
import com.terrasport.fragment.TerrainFragment;
import com.terrasport.model.Terrain;
import com.terrasport.utils.Globals;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by jdher on 21/01/2017.
 */
public class LoadTerrainAsyncTask extends AsyncTask <String, Void, List<Terrain>> {

    private final String URI = Globals.getInstance().getBaseUrl() + "terrain/all";
    private TerrainFragment fragment;

    public LoadTerrainAsyncTask(TerrainFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected List<Terrain> doInBackground(String... params) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        AllTerrainEvent allTerrainEvent = restTemplate.getForObject( URI, AllTerrainEvent.class);
        return allTerrainEvent.getTerrains();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Terrain> result) {
        super.onPostExecute(result);
        if(fragment != null && fragment.getActivity() != null) {
            fragment.updateList(result);
            this.fragment = null;
        }
    }
}