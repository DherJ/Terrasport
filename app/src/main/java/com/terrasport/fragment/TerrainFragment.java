package com.terrasport.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.terrasport.R;
import com.terrasport.asyncTask.LoadNiveauAsyncTask;
import com.terrasport.asyncTask.LoadSportAsyncTask;
import com.terrasport.asyncTask.LoadTerrainAsyncTask;
import com.terrasport.model.Evenement;
import com.terrasport.model.Niveau;
import com.terrasport.model.Sport;
import com.terrasport.model.Terrain;
import com.terrasport.model.Utilisateur;
import com.terrasport.utils.Globals;
import com.terrasport.view.MapSearchView;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TerrainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TerrainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TerrainFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private static final String PRIVE = "Privé";
    private static final String PUBLIC = "Public";
    private static final String OCCUPE = "Occupé";
    private static final String LIBRE = "Libre";

    private final String URI_SAUVEGARDER_EVENEMENT = Globals.getInstance().getBaseUrl() + "evenement/sauvegarder";
    private final String URI_SAUVEGARDER_TERRAIN = Globals.getInstance().getBaseUrl() + "terrain/sauvegarder";

    /**  Elements pour ajouter un évènement **/
    private Calendar calendar;
    private List<Niveau> niveaux;
    private Niveau niveauCible;
    private Integer nbPlacesRestantes;
    private Integer nbPlacesDisponnibles;

    /**  Elements pour ajouter un terrain **/
    private String longitude;
    private String latitude;
    private String nomTerrain;
    private boolean isTerrainPublic;
    private Sport sportSelected;

    private OnFragmentInteractionListener mListener;

    private LoadTerrainAsyncTask mTerrainTask;
    private LoadSportAsyncTask mSportTask;
    private LoadNiveauAsyncTask mNiveauxTask;
    private List<Terrain> terrains;
    private List<Sport> sports;
    private List<Evenement> evenements;
    private Terrain terrain;

    private Utilisateur utilisateur;

    private GoogleMap mGoogleMap;

    private LocationManager locationManager;
    private Location location;

    private HashMap<Marker, Terrain> mMarkersHashMap;

    private MapSearchView searchView;
    public Button searchButton;

    public TerrainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param utilisateur Parameter 1.
     * @return A new instance of fragment TerrainFragment.
     */
    public static TerrainFragment newInstance(Utilisateur utilisateur) {
        TerrainFragment fragment = new TerrainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.terrains = new ArrayList<Terrain>();
        this.sports = new ArrayList<Sport>();
        this.niveaux = new ArrayList<Niveau>();
        this.evenements = new ArrayList<Evenement>();
        mTerrainTask = new LoadTerrainAsyncTask(this);
        mTerrainTask.execute();
        mSportTask = new LoadSportAsyncTask(this);
        mSportTask.execute();
        mNiveauxTask = new LoadNiveauAsyncTask(this);
        mNiveauxTask.execute();
        mMarkersHashMap = new HashMap<>();
        calendar = new GregorianCalendar();

        Gson gson = new Gson();
        utilisateur = gson.fromJson(this.getArguments().getString("utilisateur"), Utilisateur.class);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_terrain, container, false);

        searchButton = (Button) v.findViewById(R.id.button_search);
        searchView = (MapSearchView) v.findViewById(R.id.view_search);
        searchView.setIsVisible(false);
        searchView.setSports(this.sports);
        //searchView.setSearchButton(searchButton);
        searchView.setTerrainFragment(this);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchView.setVisible(searchView.isVisible());
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void addTerrain(LatLng latLng) {

        Terrain terrain = new Terrain();
        terrain.setNom(nomTerrain);
        terrain.setIsOccupe(Boolean.FALSE);
        terrain.setIsPublic(isTerrainPublic);
        terrain.setSport(sportSelected);
        terrain.setLatitude(latitude);
        terrain.setLongitude(longitude);

        RestTemplate rt = new RestTemplate();
        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        rt.getMessageConverters().add(new StringHttpMessageConverter());
        rt.postForEntity(URI_SAUVEGARDER_TERRAIN, terrain, ResponseEntity.class);

        ajouterMarketTerrain(terrain, latLng);
    }

    public void ajouterMarketTerrain(Terrain terrain, LatLng latLng) {
        int drawable = 0;
        switch(terrain.getSport().getId()) {
            case 1:
                drawable = R.drawable.map_marker_foot;
                break;
            case 2:
                drawable = R.drawable.map_marker_rugby;
                break;
            case 3:
                drawable = R.drawable.map_marker_basket;
                break;
            case 4:
                drawable = R.drawable.map_marker_handball;
                break;
            case 5:
                drawable = R.drawable.map_marker_golf;
                break;
            case 6:
                drawable = R.drawable.map_marker_baseball;
                break;
            case 7:
                drawable = R.drawable.map_marker_tennis;
                break;
            case 8:
                drawable = R.drawable.map_marker_volleyball;
                break;
        }
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(drawable, terrain.getIsPublic()));
        Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(terrain.getIsOccupe()+"").snippet("This is my spot!")
                .icon(bitmap));
        mMarkersHashMap.put(marker, terrain);
    }

    public void addEvenement() {
        Timestamp time = new Timestamp(calendar.getTimeInMillis());

        Evenement evenement = new Evenement();
        evenement.setTerrain(terrain);
        evenement.setSport(terrain.getSport());
        evenement.setNbPlaces(nbPlacesDisponnibles);
        evenement.setNbPlacesRestantes(nbPlacesRestantes);
        evenement.setUtilisateurCreateur(utilisateur);
        evenement.setDate(time);
        evenement.setNiveauCible(niveauCible);
        RestTemplate rt = new RestTemplate();

        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        rt.getMessageConverters().add(new StringHttpMessageConverter());
        rt.postForEntity(URI_SAUVEGARDER_EVENEMENT, evenement, ResponseEntity.class);
    }

    public void updateList(List<Terrain> result) {
        this.terrains = result;
    }

    public void updateListSport(List<Sport> result) {
        this.sports = result;
        searchView.setSports(result);
        searchView.draw();
    }

    public void updateListNiveaux(List<Niveau> result) {
        this.niveaux = result;
    }

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId, boolean isPublic) {
        View customMarkerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.marker_image);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);

        return returnedBitmap;
    }

    public void setTerrainsMarkers() {
        if(mGoogleMap != null) {
            int drawable = 0;
            BitmapDescriptor bitmap = null;
            for(int i = 0; i < this.terrains.size(); i++) {
                LatLng latLng = new LatLng(Double.valueOf(this.terrains.get(i).getLatitude()), Double.valueOf(this.terrains.get(i).getLongitude()));
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                switch(this.terrains.get(i).getSport().getId()) {
                    case 1:
                        drawable = R.drawable.map_marker_foot;
                        break;
                    case 2:
                        drawable = R.drawable.map_marker_rugby;
                        break;
                    case 3:
                        drawable = R.drawable.map_marker_basket;
                        break;
                    case 4:
                        drawable = R.drawable.map_marker_handball;
                        break;
                    case 5:
                        drawable = R.drawable.map_marker_golf;
                        break;
                    case 6:
                        drawable = R.drawable.map_marker_baseball;
                        break;
                    case 7:
                        drawable = R.drawable.map_marker_tennis;
                        break;
                    case 8:
                        drawable = R.drawable.map_marker_volleyball;
                        break;
                }
                bitmap = BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(drawable, this.terrains.get(i).getIsPublic()));
                Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(this.terrains.get(i).getIsOccupe()+"").snippet("This is my spot!")
                        .icon(bitmap));
                mMarkersHashMap.put(marker, this.terrains.get(i));
            }
            mGoogleMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnInfoWindowClickListener(this);
        mGoogleMap.setOnMapLongClickListener(this);

        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getContext().getSystemService(context);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }

        location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
           mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            /*
            // pour mettre le stle de nuit
            mGoogleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.map_style_night));
            */
            mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("Ma position").snippet("Je me trouve ici!")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_user)));
            mGoogleMap.setMyLocationEnabled(true);
            // déplacement de la caméra sur notre position
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            // Zoom in the Google Map
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
        setTerrainsMarkers();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        terrain = mMarkersHashMap.get(marker);
        if(terrain != null)  {
            // on affiche le formulaire our créer un évènement

            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.view_dialbox_add_event);

            EditText editNbPlacesDisponnibles = (EditText) dialog.findViewById(R.id.edit_nb_places_disponibles);
            editNbPlacesDisponnibles.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    nbPlacesDisponnibles = Integer.parseInt(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            EditText editNbPlacesRestantes = (EditText) dialog.findViewById(R.id.edit_nb_places_restantes);
            editNbPlacesRestantes.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    nbPlacesRestantes = Integer.parseInt(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            Spinner spinner = (Spinner) dialog.findViewById(R.id.select_niveau);
            List<String> listeNiveaux = new ArrayList<>();
            for(int i = 0; i < this.niveaux.size(); i++) {
                listeNiveaux.add(this.niveaux.get(i).getLibelle());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, listeNiveaux);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) view).setTextColor(Color.WHITE);
                    niveauCible = niveaux.get(position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            final CalendarView calendarView = (CalendarView) dialog.findViewById(R.id.calendarView);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                }
            });

            TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.time_picker);
            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                }
            });

            Button acceptButton = (Button) dialog.findViewById(R.id.btn_yes);
            Button declineButton = (Button) dialog.findViewById(R.id.btn_no);

            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addEvenement();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    }

    @Override
    public void onMapLongClick(final LatLng latLng) {

        longitude = String.valueOf(latLng.longitude);
        latitude = String.valueOf(latLng.latitude);

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_dialbox_add_terrain);

        TextView adresseTerrain = (TextView) dialog.findViewById(R.id.text_adresse_terrain);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            StringBuilder adreeseBuilder = new StringBuilder();
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                if(addresses.get(0).getAddressLine(0) != null) {
                    adreeseBuilder.append(addresses.get(0).getAddressLine(0) + " ");
                }
                if(addresses.get(0).getAddressLine(1) != null) {
                    adreeseBuilder.append(addresses.get(0).getAddressLine(1) + " ");
                }
                if(addresses.get(0).getAddressLine(2) != null) {
                    adreeseBuilder.append(addresses.get(0).getAddressLine(2) + " ");
                }
                adresseTerrain.setText(adreeseBuilder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        EditText editNomTerrain = (EditText) dialog.findViewById(R.id.edit_nom_terrain);
        editNomTerrain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nomTerrain = s.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final ImageView imageTerrainPublic = (ImageView) dialog.findViewById(R.id.image_terrain_public);
        imageTerrainPublic.setImageResource(R.drawable.image_terrain_public);

        SwitchCompat switchTerrainPublic = (SwitchCompat) dialog.findViewById(R.id.switch_terrain_public);
        switchTerrainPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    isTerrainPublic = true;
                    imageTerrainPublic.setImageResource(R.drawable.image_terrain_public);
                }
                else {
                    isTerrainPublic = false;
                    imageTerrainPublic.setImageResource(R.drawable.image_terrain_prive);
                }
            }
        });

        Spinner spinner = (Spinner) dialog.findViewById(R.id.select_sport);
        List<String> listeSport = new ArrayList<>();
        for(int i = 0; i < this.niveaux.size(); i++) {
            listeSport.add(this.sports.get(i).getLibelle());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listeSport);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.WHITE);
                sportSelected = sports.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button acceptButton = (Button) dialog.findViewById(R.id.btn_yes);
        Button declineButton = (Button) dialog.findViewById(R.id.btn_no);

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTerrain(latLng);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void filtreMarkers(Sport sportSelected) {
        // TODO a implémenter
        Set<Marker> keys = this.mMarkersHashMap.keySet();
        Iterator<Marker> iterator = keys.iterator();
        while(iterator.hasNext()){
            Marker x = iterator.next();
            if(this.mMarkersHashMap.get(x) != null)
                System.out.println("key is " + x.toString());
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        public MarkerInfoWindowAdapter()
        {
        }

        @Override
        public View getInfoWindow(Marker marker)
        {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v = null;
            if( !marker.getTitle().equals("Ma position") ) {

                    Terrain terrain = mMarkersHashMap.get(marker);
                    v = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker_add_evenement, null);

                    ImageView markerIcon = (ImageView) v.findViewById(R.id.marker_image);

                    TextView textViewNomTerrain = (TextView) v.findViewById(R.id.text_nom_terrain);
                    TextView textViewEstPublic = (TextView) v.findViewById(R.id.text_terrain_est_public);
                    TextView textViewEstOccupe = (TextView) v.findViewById(R.id.text_terrain_est_occupe);
                    textViewNomTerrain.setText(terrain.getNom());
                    textViewEstPublic.setText(terrain.getIsPublic() ? PUBLIC : PRIVE);
                    textViewEstOccupe.setText(terrain.getIsOccupe() ? OCCUPE : LIBRE);

                    markerIcon.setImageResource(manageMarkerIcon(terrain));
            }
            return v;
        }
    }

     private int manageMarkerIcon(Terrain terrain) {
        int drawable = 0;
        switch(terrain.getSport().getId()) {
            case 1:
                drawable = R.drawable.map_marker_foot;
                break;
            case 2:
                drawable = R.drawable.map_marker_rugby;
                break;
            case 3:
                drawable = R.drawable.map_marker_basket;
                break;
            case 4:
                drawable = R.drawable.map_marker_handball;
                break;
            case 5:
                drawable = R.drawable.map_marker_golf;
                break;
            case 6:
                drawable = R.drawable.map_marker_baseball;
                break;
            case 7:
                drawable = R.drawable.map_marker_tennis;
                break;
            case 8:
                drawable = R.drawable.map_marker_volleyball;
                break;
        }
        return drawable;
    }
}