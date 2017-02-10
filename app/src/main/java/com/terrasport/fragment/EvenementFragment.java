package com.terrasport.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.terrasport.R;
import com.terrasport.adapter.EvenementRecyclerViewAdapter;
import com.terrasport.asyncTask.LoadEvenementAsyncTask;
import com.terrasport.model.Evenement;
import com.terrasport.model.Utilisateur;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EvenementFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private LoadEvenementAsyncTask mTask;
    private List<Evenement> evenements;
    public RecyclerView.Adapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EvenementFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EvenementFragment newInstance(int columnCount) {
        EvenementFragment fragment = new EvenementFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public static EvenementFragment newInstance(Utilisateur utilisateur) {
        EvenementFragment fragment = new EvenementFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        this.evenements = new ArrayList<Evenement>();
        mTask = new LoadEvenementAsyncTask(this);
        mTask.execute();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Sync UI state to current fragment and task state
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_evenement_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;

            adapter = new EvenementRecyclerViewAdapter(this.evenements, mListener);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).color(Color.WHITE).build());
        }
        return view;
    }

    public void updateListView(List<Evenement> newData) {
        evenements = newData;
        adapter.notifyDataSetChanged();
        adapter = new EvenementRecyclerViewAdapter(evenements, mListener);
        recyclerView.setAdapter(adapter);
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
        void onListFragmentInteraction(Evenement item);
    }

}
