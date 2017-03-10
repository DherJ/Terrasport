package com.terrasport.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.terrasport.R;
import com.terrasport.fragment.DemandeParticipationFragment;
import com.terrasport.fragment.EvenementUtilisateurFragment;
import com.terrasport.fragment.UtilisateurFragment;
import com.terrasport.model.DemandeParticipation;
import com.terrasport.model.Etat;
import com.terrasport.model.Utilisateur;
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
public class UtilisateurRecyclerViewAdapter extends RecyclerView.Adapter<UtilisateurRecyclerViewAdapter.ViewHolder> {

    private static final String URI_SIGNALER_UTILISATEUR = Globals.getInstance().getBaseUrl() + "utilisateur/signaler";

    private final List<Utilisateur> mValues;
    private final UtilisateurFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;

    public UtilisateurRecyclerViewAdapter(List<Utilisateur> items, UtilisateurFragment.OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_utilisateur, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);

        if(Boolean.FALSE.equals(mValues.get(position).getIsSignale())) {
            holder.imageViewIsSignale.setVisibility(View.GONE);
            holder.signalerButton.setVisibility(View.VISIBLE);
        } else {
            holder.signalerButton.setVisibility(View.GONE);
        }

        holder.textViewNomUtilisateur.setText(mValues.get(position).getNom());
        holder.textViewPrenomUtilisateur.setText(mValues.get(position).getPrenom());

        holder.signalerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mItem.setIsSignale(true);
                signalerUtilisateur(holder.mItem);
                Toast.makeText(mContext, "Utilisateur signalé", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
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

    public void signalerUtilisateur(Utilisateur utilisateur) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.postForEntity(URI_SIGNALER_UTILISATEUR, utilisateur, ResponseEntity.class);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final ImageView imageViewIsSignale;
        public final TextView textViewNomUtilisateur;
        public final TextView textViewPrenomUtilisateur;
        public final Button signalerButton;

        public Utilisateur mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageViewIsSignale = (ImageView) view.findViewById(R.id.image_utilisateur_signale);
            textViewNomUtilisateur = (TextView) view.findViewById(R.id.text_utilisateur_nom);
            textViewPrenomUtilisateur = (TextView) view.findViewById(R.id.text_utilisateur_prenom);
            signalerButton = (Button) view.findViewById(R.id.button_signaler);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewNomUtilisateur.getText() + "'";
        }
    }
}