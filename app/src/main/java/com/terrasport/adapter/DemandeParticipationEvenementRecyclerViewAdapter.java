package com.terrasport.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.terrasport.R;
import com.terrasport.fragment.DemandeParticipationFragment;
import com.terrasport.fragment.EvenementUtilisateurFragment;
import com.terrasport.model.DemandeParticipation;
import com.terrasport.model.Etat;
import com.terrasport.utils.Globals;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DemandeParticipation} and makes a call to the
 * specified {@link DemandeParticipationFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DemandeParticipationEvenementRecyclerViewAdapter extends RecyclerView.Adapter<DemandeParticipationEvenementRecyclerViewAdapter.ViewHolder> {

    private final String URI_UPDATE_ETAT_DEMANDE = Globals.getInstance().getBaseUrl() + "demande-participation/miseAjourEtatDemande/";

    private final List<DemandeParticipation> mValues;
    private final EvenementUtilisateurFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;

    public DemandeParticipationEvenementRecyclerViewAdapter(List<DemandeParticipation> items, EvenementUtilisateurFragment.OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_demande_participation_evenement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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
        holder.textViewUtilisateur.setText("Demandeur : " + mValues.get(position).getUtilisateur().getNom());

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Etat etat = new Etat();
                etat.setId(2);
                etat.setLibelle("Validée");
                holder.mItem.setEtat(etat);
                updateEtatDemandeParticipation(holder.mItem);
                Toast.makeText(mContext, "Demande validée", Toast.LENGTH_SHORT).show();
                mValues.remove(holder.mItem);
                notifyItemRemoved(position);
            }
        });

        holder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Etat etat = new Etat();
                etat.setId(3);
                etat.setLibelle("Refusée");
                holder.mItem.setEtat(etat);
                updateEtatDemandeParticipation(holder.mItem);
                Toast.makeText(mContext, "Demande refusée", Toast.LENGTH_SHORT).show();
                mValues.remove(holder.mItem);
                notifyItemRemoved(position);
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

    public void updateEtatDemandeParticipation(DemandeParticipation demandeParticipation) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.postForEntity(URI_UPDATE_ETAT_DEMANDE, demandeParticipation, ResponseEntity.class);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView textViewDateEvenement;
        public final TextView textViewUtilisateur;
        public final TextView textViewAdresseEvenement;
        public final Button acceptButton;
        public final Button declineButton;

        public DemandeParticipation mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewDateEvenement = (TextView) view.findViewById(R.id.text_date_evenement);
            textViewUtilisateur = (TextView) view.findViewById(R.id.text_utilisateur);
            textViewAdresseEvenement = (TextView) view.findViewById(R.id.text_adresse_evenement);
            acceptButton = (Button) view.findViewById(R.id.accept_demande_button);
            declineButton = (Button) view.findViewById(R.id.decline_demande_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewDateEvenement.getText() + "'";
        }
    }
}