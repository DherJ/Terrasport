package com.terrasport.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.terrasport.R;
import com.terrasport.asyncTask.LoadTerrainAsyncTask;
import com.terrasport.model.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TerrainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TerrainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TerrainFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private OnFragmentInteractionListener mListener;

    private LoadTerrainAsyncTask mTask;
    private List<Terrain> terrains;

    private GoogleMap mGoogleMap;

    private LocationManager locationManager;
    private Location location;

    private HashMap<Marker, Terrain> mMarkersHashMap;

    public TerrainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TerrainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TerrainFragment newInstance(String param1, String param2) {
        TerrainFragment fragment = new TerrainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.terrains = new ArrayList<Terrain>();
        mTask = new LoadTerrainAsyncTask(this);
        mTask.execute();
        mMarkersHashMap = new HashMap<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_terrain, container, false);
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

    public void updateList(List<Terrain> result) {
        this.terrains = result;
    }

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId, boolean isPublic) {
        View customMarkerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker_public, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.marker_image);
        ImageView markerPublicImageView = (ImageView) customMarkerView.findViewById(R.id.public_image);
        if(Boolean.TRUE.equals(isPublic)) {
            markerPublicImageView.setImageResource(R.drawable.image_terrain_public);
        } else {
            markerPublicImageView.setImageResource(R.drawable.image_terrain_prive);
        }
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
                        drawable = R.drawable.map_marker_basket;
                        break;
                    case 3:
                        drawable = R.drawable.map_marker_rugby;
                        break;
                    case 4:
                        drawable = R.drawable.map_marker_handball;
                        break;
                }
                /*
                if(Boolean.TRUE.equals(this.terrains.get(i).getIsPublic())) {
                    bitmap = BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(drawable, this.terrains.get(i).getIsPublic()));
                } else {
                    bitmap = BitmapDescriptorFactory.fromResource(drawable);
                }
                */
                bitmap = BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(drawable, this.terrains.get(i).getIsPublic()));
                //bitmap = BitmapDescriptorFactory.fromResource(drawable);
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
            mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("Ma position").snippet("Je me trouve ici!")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_user)));

            mGoogleMap.setMyLocationEnabled(true);

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Zoom in the Google Map
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

        setTerrainsMarkers();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        /*
        BitmapDescriptor bitmap = null;
        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
        bitmap = BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.map_marker_basket));
        marker.setIcon(bitmap);
        */
        return false;
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


    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
    {
        public MarkerInfoWindowAdapter()
        {
        }

        @Override
        public View getInfoWindow(Marker marker)
        {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker)
        {
            View v  = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker_add_evenement, null);

            Terrain terrain = mMarkersHashMap.get(marker);

            ImageView markerIcon = (ImageView) v.findViewById(R.id.marker_image);

            markerIcon.setImageResource(manageMarkerIcon(terrain));

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
                drawable = R.drawable.map_marker_basket;
                break;
            case 3:
                drawable = R.drawable.map_marker_rugby;
                break;
            case 4:
                drawable = R.drawable.map_marker_handball;
                break;
        }
        return drawable;
    }
}