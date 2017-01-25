package com.terrasport.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.terrasport.R;
import com.terrasport.adapter.ParticipationAVenirRecyclerViewAdapter;
import com.terrasport.asyncTask.LoadParticipationAVenirAsyncTask;
import com.terrasport.event.AllParticipationEvent;
import com.terrasport.model.Participation;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ParticipationAVenirFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ALL_PARTICIPATIONS_EVENT = "all-participations-event";

    // TODO: Customize parameters
    private int mColumnCount = 0;
    private OnListFragmentInteractionListener mListener;
    private List<Participation> participations;
    // private View view;
    private RecyclerView.Adapter adapter;

    private RecyclerView recyclerView;
    private LoadParticipationAVenirAsyncTask mTask;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ParticipationAVenirFragment() {
    }

    public static ParticipationAVenirFragment newInstance(AllParticipationEvent allParticipationsEvent) {
        ParticipationAVenirFragment fragment = new ParticipationAVenirFragment();
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
            AllParticipationEvent allParticipationsEvent = (AllParticipationEvent) getArguments().get(ALL_PARTICIPATIONS_EVENT);
            this.participations = allParticipationsEvent.getParticipations();
            mTask = new LoadParticipationAVenirAsyncTask(this);
            mTask.execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_participation_a_venir_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if(participations == null) {
                participations = new ArrayList<Participation>();
            }

            adapter = new ParticipationAVenirRecyclerViewAdapter(this.participations, mListener);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).color(Color.WHITE).build());
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

    public void updateListView(List<Participation> result) {
        participations = result;
        adapter = new ParticipationAVenirRecyclerViewAdapter(participations, mListener);
        recyclerView.setAdapter(adapter);
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
}