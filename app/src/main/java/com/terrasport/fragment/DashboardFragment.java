package com.terrasport.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.terrasport.R;
import com.terrasport.event.AllParticipationEvent;
import com.terrasport.fragment.dummy.DummyContent;
import com.terrasport.model.Participation;
import com.terrasport.model.Utilisateur;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DashboardFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ALL_PARTICIPATIONS_EVENT = "all-participations-event";

    // TODO: Customize parameters
    private int mColumnCount = 0;
    private OnListFragmentInteractionListener mListener;
    private final String uriHomeJerome = "http://192.168.1.24:8080/dashboard/participations/3";
    private List<Participation> participations;
    private View view;
    private RecyclerView.Adapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DashboardFragment() {
    }

    public static DashboardFragment newInstance(AllParticipationEvent allParticipationsEvent) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putSerializable(ALL_PARTICIPATIONS_EVENT, allParticipationsEvent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate  (savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            AllParticipationEvent test = (AllParticipationEvent) getArguments().get(ALL_PARTICIPATIONS_EVENT);
            participations = test.getParticipations();
        }

       // RestTemplate restTemplate = new RestTemplate();
        //restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
       // participations = (List<Participation>) restTemplate.getForObject( uriHomeJerome, ArrayList<Participation.class>);

        //Participation[] forNow = restTemplate.getForObject(uriHomeJerome, Participation[].class);
       // participations =  Arrays.asList(forNow);
       // bindRecyclerView();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if(participations == null) {
                participations = new ArrayList<Participation>();
            }

            adapter = new DashboardRecyclerViewAdapter(this.participations, mListener);

            recyclerView.setAdapter(adapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Participation item);
    }

    public void bindRecyclerView() {
        new MyAsyncTask(getActivity(), (RecyclerView) view, adapter).execute("");
    }

    class MyAsyncTask extends AsyncTask<String, String, List<Participation>> {
        RecyclerView mRecyclerView;
        Activity mContex;
        RecyclerView.Adapter adapter;

        public MyAsyncTask(Activity context, RecyclerView rview, RecyclerView.Adapter mAdapter) {
            this.mRecyclerView = rview;
            this.mContex = context;
            adapter = mAdapter;
        }

        protected List<Participation> doInBackground(String... params) {

            // url avec IP de lille1
            //final String uriHomeJerome = "http://192.168.1.24:8080/dashboard/participations/3";
             String uriFacJerome = new String("http://172.19.137.107:8080/dashboard/participations/3");

            // String uriHomeJulien = new String("http://192.168.1.24:8080/dashboard/participations/3");
            // String uriFacJulien = new String("http://172.19.137.107:8080/dashboard/participations/3");
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            //ResponseEntity<Participation[]> responseEntity = restTemplate.getForEntity(uriHomeJerome, Participation[].class);

            /*
            Collection<Participation> readValues;

            try {
                readValues = new ObjectMapper().readValue(new Gson().toJson(restTemplate.getForEntity(uriFacJerome, Participation[].class)), new TypeReference<Collection<Participation>>() { });
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<Participation> participations = new ArrayList<>();
            //List<Participation> participations = Arrays.asList(responseEntity.getBody());
            for(int i = 0; i < participations.size(); i++) {
                mListener.onListFragmentInteraction(participations.get(i));
            }
            */
            AllParticipationEvent allParticipationEvent = restTemplate.getForObject( uriFacJerome, AllParticipationEvent.class);
            return allParticipationEvent.getParticipations();
        }

        @Override
        protected void onPostExecute(List<Participation> mParticipations) {
            // super.onPostExecute(mParticipations);
            // participations = mParticipations;
            // adapter.notifyDataSetChanged();
        }
    }
}