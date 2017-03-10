package com.terrasport.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import com.terrasport.R;
import com.terrasport.fragment.ParticipationAVenirFragment;
import com.terrasport.model.Participation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Participation} and makes a call to the
 * specified {@link ParticipationAVenirFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ParticipationAVenirRecyclerViewAdapter extends RecyclerView.Adapter<ParticipationAVenirRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private final List<Participation> mValues;
    private final ParticipationAVenirFragment.OnListFragmentInteractionListener mListener;

    public ParticipationAVenirRecyclerViewAdapter(List<Participation> items, ParticipationAVenirFragment.OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_participation_a_venir, parent, false);
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

        switch(mValues.get(position).getEvenement().getSport().getId()) {
            case 1:
                holder.imageSport.setImageResource(R.drawable.map_marker_foot);
                break;
            case 2:
                holder.imageSport.setImageResource(R.drawable.map_marker_rugby);
                break;
            case 3:
                holder.imageSport.setImageResource(R.drawable.map_marker_basket);
                break;
            case 4:
                holder.imageSport.setImageResource(R.drawable.map_marker_handball);
                break;
            case 5:
                holder.imageSport.setImageResource(R.drawable.map_marker_golf);
                break;
            case 6:
                holder.imageSport.setImageResource(R.drawable.map_marker_baseball);
                break;
            case 7:
                holder.imageSport.setImageResource(R.drawable.map_marker_tennis);
                break;
            case 8:
                holder.imageSport.setImageResource(R.drawable.map_marker_volleyball);
                break;
        }

        holder.textViewDateEvenement.setText(formater.format(mValues.get(position).getEvenement().getDate()));
        holder.textViewUtilisateurProprietaire.setText("Crée par " + mValues.get(position).getEvenement().getUtilisateurCreateur().getNom());
        holder.textViewAdresseEvenement.setText(adresseBuilder.toString());
        holder.textViewNbParticipants.setText(mValues.get(position).getEvenement().getNbParticipants() + " participants/" + mValues.get(position).getEvenement().getNbPlaces());

        holder.progressBarParticipants.setMax(mValues.get(position).getEvenement().getNbPlaces());
        holder.progressBarParticipants.setProgress(mValues.get(position).getEvenement().getNbParticipants());

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
        public final TextView textViewAdresseEvenement;
        public final TextView textViewNbParticipants;
        public final AppCompatSeekBar progressBarParticipants;

        public Participation mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageSport = (ImageView) view.findViewById(R.id.participation_image_sport);
            textViewDateEvenement = (TextView) view.findViewById(R.id.participation_text_date_evenement);
            textViewUtilisateurProprietaire = (TextView) view.findViewById(R.id.participation_text_utilisateur_proprietaire);
            textViewAdresseEvenement = (TextView) view.findViewById(R.id.participation_text_adresse_evenement);
            textViewNbParticipants = (TextView) view.findViewById(R.id.participation_text_nb_participants);
            progressBarParticipants = (AppCompatSeekBar) view.findViewById(R.id.participation_progreesbar_nb_participants);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewUtilisateurProprietaire.getText() + "'";
        }
    }
}
