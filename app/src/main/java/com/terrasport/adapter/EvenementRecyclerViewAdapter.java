package com.terrasport.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.terrasport.R;
import com.terrasport.fragment.EvenementFragment;
import com.terrasport.model.Evenement;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Evenement} and makes a call to the
 * specified {@link EvenementFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class EvenementRecyclerViewAdapter extends RecyclerView.Adapter<EvenementRecyclerViewAdapter.ViewHolder> {

    private List<Evenement> mValues;
    private EvenementFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;

    public EvenementRecyclerViewAdapter(List<Evenement> items, EvenementFragment.OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_evenement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.textViewDateEvenement.setText(mValues.get(position).getDate().toString());
        holder.textViewUtilisateurProprietaire.setText("Crée par " + mValues.get(position).getUtilisateurCreateur().getNom());
        holder.textViewNiveauRecherche.setText("Niveau recherché: " + mValues.get(position).getNiveauCible().getLibelle().toString());
        holder.textViewAdresseEvenement.setText(mValues.get(position).getTerrain().getLongitude());
        holder.textViewNbParticipants.setText(mValues.get(position).getNbParticipants() + " participants/" + mValues.get(position).getNbPlaces());

        holder.progressBarParticipants.setMax(mValues.get(position).getNbPlaces());
        holder.progressBarParticipants.setProgress(mValues.get(position).getNbParticipants());

        switch(mValues.get(position).getTerrain().getSport().getId()) {
            case 1:
                holder.imageSport.setImageResource(R.drawable.map_marker_foot);
                break;
            case 2:
                holder.imageSport.setImageResource(R.drawable.map_marker_basket);
                break;
            case 3:
                holder.imageSport.setImageResource(R.drawable.map_marker_rugby);
                break;
            case 4:
                holder.imageSport.setImageResource(R.drawable.map_marker_foot);
                break;
        }

        holder.buttonEnvoyerDemandeParticipation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Test" , Toast.LENGTH_SHORT).show();
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageSport;
        public final TextView textViewDateEvenement;
        public final TextView textViewUtilisateurProprietaire;
        public final TextView textViewNiveauRecherche;
        public final TextView textViewAdresseEvenement;
        public final TextView textViewNbParticipants;
        public final AppCompatSeekBar progressBarParticipants;
        public final AppCompatButton buttonEnvoyerDemandeParticipation;

        public Evenement mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageSport = (ImageView) view.findViewById(R.id.image_sport_fragment_evenement);
            textViewDateEvenement = (TextView) view.findViewById(R.id.text_date_evenement);
            textViewUtilisateurProprietaire = (TextView) view.findViewById(R.id.text_utilisateur_proprietaire);
            textViewNiveauRecherche = (TextView) view.findViewById(R.id.text_niveau_recherche);
            textViewAdresseEvenement = (TextView) view.findViewById(R.id.text_adresse_evenement);
            textViewNbParticipants = (TextView) view.findViewById(R.id.text_nb_participants);
            progressBarParticipants = (AppCompatSeekBar) view.findViewById(R.id.progreesbar_nb_participants);
            buttonEnvoyerDemandeParticipation = (AppCompatButton) view.findViewById(R.id.button_demander_participer);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewDateEvenement.getText() + "'";
        }
    }
}
