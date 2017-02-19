package com.terrasport.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.terrasport.R;
import com.terrasport.adapter.EvenementUtilisateurRecyclerViewAdapter;
import com.terrasport.asyncTask.LoadEvenementUtilisateurAsyncTask;
import com.terrasport.model.Evenement;
import com.terrasport.model.Utilisateur;
import com.terrasport.utils.Globals;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EvenementUtilisateurFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    private static final String URI_SAUVEGARDER_EVENEMENT = Globals.getInstance().getBaseUrl() + "evenement/sauvegarder";

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private LoadEvenementUtilisateurAsyncTask mTask;
    private List<Evenement> evenements;
    public RecyclerView.Adapter adapter;
    private FloatingActionButton addEvenementButton;
    private Utilisateur utilisateur;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EvenementUtilisateurFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EvenementUtilisateurFragment newInstance(int columnCount) {
        EvenementUtilisateurFragment fragment = new EvenementUtilisateurFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public static EvenementUtilisateurFragment newInstance(Utilisateur utilisateur) {
        EvenementUtilisateurFragment fragment = new EvenementUtilisateurFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Gson gson = new Gson();
        utilisateur = gson.fromJson(getActivity().getIntent().getStringExtra("utilisateur"), Utilisateur.class);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        this.evenements = new ArrayList<Evenement>();
        mTask = new LoadEvenementUtilisateurAsyncTask(this);
        mTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evenement_utilisateur_list, container, false);
        View myRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_list_evenement_utilisateur);
        addEvenementButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        if (myRecyclerView instanceof RecyclerView) {
            Context context = view.getContext();
            adapter = new EvenementUtilisateurRecyclerViewAdapter(this.evenements, mListener, getContext(), utilisateur);
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

    public void updateListView(List<Evenement> result) {
        evenements = result;
        adapter = new EvenementUtilisateurRecyclerViewAdapter(evenements, mListener, getContext(), utilisateur);
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
        void onListFragmentInteraction(Evenement item);
    }
}
