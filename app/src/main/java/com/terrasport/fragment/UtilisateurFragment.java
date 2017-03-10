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
import com.terrasport.adapter.UtilisateurRecyclerViewAdapter;
import com.terrasport.asyncTask.LoadAllUtilisateurAsyncTask;
import com.terrasport.model.DemandeParticipation;
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
public class UtilisateurFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private LoadAllUtilisateurAsyncTask mTask;
    private List<Utilisateur> utilisateurs;
    public RecyclerView.Adapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UtilisateurFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UtilisateurFragment newInstance(int columnCount) {
        UtilisateurFragment fragment = new UtilisateurFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public static UtilisateurFragment newInstance(Utilisateur utilisateur) {
        UtilisateurFragment fragment = new UtilisateurFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utilisateurs = new ArrayList<>();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        mTask = new LoadAllUtilisateurAsyncTask(this);
        mTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evenement_utilisateur_list, container, false);
        View myRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_list_evenement_utilisateur);
        if (myRecyclerView instanceof RecyclerView) {
            Context context = view.getContext();
            adapter = new UtilisateurRecyclerViewAdapter(this.utilisateurs, mListener, getContext());
            recyclerView = (RecyclerView) myRecyclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
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

    public void updateListView(List<Utilisateur> result) {
        utilisateurs = result;
        adapter = new UtilisateurRecyclerViewAdapter(utilisateurs, mListener, getContext());
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
        void onListFragmentInteraction(Utilisateur item);

    }
}
