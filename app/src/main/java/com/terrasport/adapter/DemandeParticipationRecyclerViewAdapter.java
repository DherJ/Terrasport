package com.terrasport.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.terrasport.R;
import com.terrasport.fragment.DemandeParticipationFragment;
import com.terrasport.model.DemandeParticipation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DemandeParticipation} and makes a call to the
 * specified {@link DemandeParticipationFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DemandeParticipationRecyclerViewAdapter extends RecyclerView.Adapter<DemandeParticipationRecyclerViewAdapter.ViewHolder> {

    private final List<DemandeParticipation> mValues;
    private final DemandeParticipationFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;

    public DemandeParticipationRecyclerViewAdapter(List<DemandeParticipation> items, DemandeParticipationFragment.OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_demande_participation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(mContext, Locale.getDefault());
        StringBuilder adresseBuilder = new StringBuilder();
        SimpleDateFormat formater = null;
        formater = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        try {
            addresses = geocoder.getFromLocation(Double.valueOf(mValues.get(position).getEvenement().getTerrain().getLatitude()), Double.valueOf(mValues.get(position).getEvenement().getTerrain().getLongitude()), 1);
            if (addresses != null && addresses.size() > 0) {
                if(addresses.get(0).getAddressLine(0) != null) {
                    adresseBuilder.append(addresses.get(0).getAddressLine(0) + " ");
                }
                if(addresses.get(0).getAddressLine(1) != null) {
                    adresseBuilder.append(addresses.get(0).getAddressLine(1) + " ");
                }
                if(addresses.get(0).getAddressLine(2) != null) {
                    adresseBuilder.append(addresses.get(0).getAddressLine(2) + " ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.mItem = mValues.get(position);

        holder.textViewDateEvenement.setText(formater.format(mValues.get(position).getDateDemande()));
        holder.textViewUtilisateurProprietaire.setText("Crée par " + mValues.get(position).getEvenement().getUtilisateurCreateur().getNom());
        holder.textViewNiveauRecherche.setText("Niveau recherché: " + mValues.get(position).getEvenement().getNiveauCible().getLibelle().toString());
        holder.textViewAdresseEvenement.setText(adresseBuilder.toString());
        holder.textViewNbParticipants.setText(mValues.get(position).getEvenement().getNbParticipants() + " participants/" + mValues.get(position).getEvenement().getNbPlaces());

        holder.progressBarParticipants.setMax(mValues.get(position).getEvenement().getNbPlaces());
        holder.progressBarParticipants.setProgress(mValues.get(position).getEvenement().getNbParticipants());

        switch(mValues.get(position).getEtat().getId()) {
            case 1:
                // validee
                holder.imageEtatDemande.setImageResource(R.drawable.ic_validee);
                break;
            case 2:
                // attente
                holder.imageEtatDemande.setImageResource(R.drawable.ic_attente);
                break;
            case 3:
                // refusee
                holder.imageEtatDemande.setImageResource(R.drawable.ic_refusee);
                break;
        }

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

        public final ImageView imageEtatDemande;
        public final TextView textViewDateEvenement;
        public final TextView textViewUtilisateurProprietaire;
        public final TextView textViewNiveauRecherche;
        public final TextView textViewAdresseEvenement;
        public final TextView textViewNbParticipants;
        public final AppCompatSeekBar progressBarParticipants;

        public DemandeParticipation mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageEtatDemande = (ImageView) view.findViewById(R.id.image_etat_demande);
            textViewDateEvenement = (TextView) view.findViewById(R.id.text_date_evenement);
            textViewUtilisateurProprietaire = (TextView) view.findViewById(R.id.text_utilisateur_proprietaire);
            textViewNiveauRecherche = (TextView) view.findViewById(R.id.text_niveau_recherche);
            textViewAdresseEvenement = (TextView) view.findViewById(R.id.text_adresse_evenement);
            textViewNbParticipants = (TextView) view.findViewById(R.id.text_nb_participants);
            progressBarParticipants = (AppCompatSeekBar) view.findViewById(R.id.progreesbar_nb_participants);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewDateEvenement.getText() + "'";
        }
    }
}
