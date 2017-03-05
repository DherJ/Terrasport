package com.terrasport.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.terrasport.R;
import com.terrasport.fragment.TerrainFragment;
import com.terrasport.model.Sport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jdher on 03/03/2017.
 */

public class MapSearchView extends LinearLayout  {

    private Button searchButton;

    private boolean isVisible;
    private List<Sport> sports;
    private Sport sportSelected;

    private Context mContext;
    private TerrainFragment terrainFragment;

    public MapSearchView(Context context) {
        super(context);
        this.mContext = context;
    }

    public MapSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setOrientation(LinearLayout.HORIZONTAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

    }

    public void draw() {
        if(this.sports != null) {
            Spinner spinner = new Spinner(this.mContext);
            List<String> listeSports = new ArrayList<>();
            listeSports.add("Aucun");
            for (int i = 0; i < this.sports.size(); i++) {
                listeSports.add(this.sports.get(i).getLibelle());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, listeSports);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) view).setTextColor(Color.BLACK);
                    if(position == 0) {
                        sportSelected = null;
                        terrainFragment.searchButton.setBackgroundResource(R.drawable.ic_action_search);
                    } else {
                        sportSelected = sports.get(position - 1);
                        switch(sportSelected.getId()) {
                            case 1:
                                terrainFragment.searchButton.setBackgroundResource(R.drawable.map_marker_foot);
                                break;
                            case 2:
                                terrainFragment.searchButton.setBackgroundResource(R.drawable.map_marker_rugby);
                                break;
                            case 3:
                                terrainFragment.searchButton.setBackgroundResource(R.drawable.map_marker_basket);
                                break;
                            case 4:
                                terrainFragment.searchButton.setBackgroundResource(R.drawable.map_marker_handball);
                                break;
                            case 5:
                                terrainFragment.searchButton.setBackgroundResource(R.drawable.map_marker_golf);
                                break;
                            case 6:
                                terrainFragment.searchButton.setBackgroundResource(R.drawable.map_marker_baseball);
                                break;
                            case 7:
                                terrainFragment.searchButton.setBackgroundResource(R.drawable.map_marker_tennis);
                                break;
                            case 8:
                                terrainFragment.searchButton.setBackgroundResource(R.drawable.map_marker_volleyball);
                                break;
                        }
                        setIsVisible(!isVisible);
                        setVisibility(View.GONE);
                        terrainFragment.filtreMarkers(sportSelected);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            addView(spinner);
        }
    }

    public void setVisible(boolean visible) {
        if(visible == true) {
            // Translation de la gauche vers la droite
            TranslateAnimation animate = new TranslateAnimation(0,this.getWidth(),0,0);
            animate.setDuration(500);
            animate.setFillAfter(true);
            this.startAnimation(animate);
            this.setVisibility(View.GONE);
        } else {
            TranslateAnimation animate = new TranslateAnimation(this.getWidth(),0,0,0);
            animate.setDuration(500);
            animate.setFillAfter(true);
            this.startAnimation(animate);
            this.setVisibility(View.VISIBLE);
        }
        this.setIsVisible(!this.isVisible);
    }

    public void clickOnItem(Button button) {
        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public void setSearchButton(Button searchButton) {
        this.searchButton = searchButton;
    }

    public List<Sport> getSports() {
        return sports;
    }

    public void setSports(List<Sport> sports) {
        this.sports = sports;
    }

    public void setIsVisible(boolean value) {
        this.isVisible = value;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public TerrainFragment getTerrainFragment() {
        return terrainFragment;
    }

    public void setTerrainFragment(TerrainFragment terrainFragment) {
        this.terrainFragment = terrainFragment;
    }
}
