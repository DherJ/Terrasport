package com.terrasport.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.terrasport.R;
import com.terrasport.fragment.EvenementFragment;
import com.terrasport.model.DemandeParticipation;
import com.terrasport.model.Evenement;
import com.terrasport.model.Utilisateur;
import com.terrasport.utils.Globals;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Evenement} and makes a call to the
 * specified {@link EvenementFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class EvenementRecyclerViewAdapter extends RecyclerView.Adapter<EvenementRecyclerViewAdapter.ViewHolder> {

    private static final String URI_SAUVEGARDER_DEMANDE_PARTICIPATION = Globals.getInstance().getBaseUrl() + "demande-participation/sauvegarder";

    private List<Evenement> mValues;
    private EvenementFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;
    private Evenement evenementSelected;
    private Utilisateur mUtilisateur;

    public EvenementRecyclerViewAdapter(List<Evenement> items, EvenementFragment.OnListFragmentInteractionListener listener, Context context, Utilisateur utilisateur) {
        mValues = items;
        mListener = listener;
        mContext = context;
        mUtilisateur = utilisateur;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_evenement, parent, false);
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
            addresses = geocoder.getFromLocation(Double.valueOf(mValues.get(position).getTerrain().getLatitude()), Double.valueOf(mValues.get(position).getTerrain().getLongitude()), 1);
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

        holder.textViewDateEvenement.setText(formater.format(mValues.get(position).getDate()));
        holder.textViewUtilisateurProprietaire.setText("Crée par " + mValues.get(position).getUtilisateurCreateur().getNom());
        holder.textViewNiveauRecherche.setText("Niveau recherché: " + mValues.get(position).getNiveauCible().getLibelle().toString());
        holder.textViewAdresseEvenement.setText(adresseBuilder.toString());
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
                evenementSelected = holder.mItem;

                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.view_dialbox_confirmation_envoyer_demande);
                Button acceptButton = (Button) dialog.findViewById(R.id.cadbtnOk);
                Button declineButton = (Button) dialog.findViewById(R.id.cadbtnCancel);

                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "No" , Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        //fragmentManager.beginTransaction().remove(f).commit();
                        //finish();

                        Toast.makeText(mContext, "Yes" , Toast.LENGTH_SHORT).show();
                        addDemandeParticipation(evenementSelected);

                        dialog.dismiss();
                    }
                });
                dialog.show();
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

    public void addDemandeParticipation(Evenement evenement) {

        DemandeParticipation demandeParticipation = new DemandeParticipation();
        demandeParticipation.setUtilisateur(mUtilisateur);
        demandeParticipation.setEvenement(evenement);

        RestTemplate rt = new RestTemplate();
        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        rt.getMessageConverters().add(new StringHttpMessageConverter());
        rt.postForEntity(URI_SAUVEGARDER_DEMANDE_PARTICIPATION, demandeParticipation, ResponseEntity.class);
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
