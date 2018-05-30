package com.olivercatling.hasamishogi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.olivercatling.hasamishogi.Database.HasamiShogiDAO;
import com.olivercatling.hasamishogi.Database.Settings;
import com.olivercatling.hasamishogi.Game.Const;
import com.olivercatling.hasamishogi.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityGameSettings extends AppCompatActivity {
    private HasamiShogiDAO dao;

    private Settings settings;

    private ToggleButton gameMode;
    private ToggleButton gameWinMode;
    private Spinner gameWinAmount;

    private ArrayAdapter<Integer> spinnerAdapter;

    private static final Integer[] HASAMI_SHOGI_AMOUNT = {1 , 2, 3, 4, 5, 6, 7, 8, 9};
    private static final Integer[] DAI_HASAMI_SHOGI_AMOUNT = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
    private ArrayList<Integer> spinnerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int reference = intent.getIntExtra(Const.SETTINGS_KEY_REF, 0);

        dao = new HasamiShogiDAO(this);
        dao.open();
        settings = dao.getSettingsFromRef(reference);
        spinnerData = new ArrayList<>();
    }

    @Override
    public void onResume(){
        super.onResume();
        initGameMode();
        initGameWinMode();
        initGameWinAmount();
    }

    private void initGameMode(){
        this.gameMode = (ToggleButton) findViewById(R.id.toggle_no_pieces);
        gameMode.setChecked(settings.isDaiHasamiShogi());
        gameMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerData.clear();
                if(gameMode.isChecked()){
                    settings.setDaiHasamiShogi(true);
                    gameWinMode.setEnabled(true);
                    spinnerData.addAll(Arrays.asList(DAI_HASAMI_SHOGI_AMOUNT));

                } else {
                    settings.setDaiHasamiShogi(false);
                    settings.setDaiHasamiShogiFiveWinRule(false); // Reset Back To Off
                    gameWinMode.setChecked(false);
                    gameWinMode.setEnabled(false);
                    spinnerData.addAll(Arrays.asList(HASAMI_SHOGI_AMOUNT));
                }
                spinnerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initGameWinMode(){
        this.gameWinMode = (ToggleButton) findViewById(R.id.toggle_win_mode);
        gameWinMode.setChecked(settings.isDaiHasamiShogiFiveWinRule());
        if(settings.isDaiHasamiShogi()) gameWinMode.setEnabled(true);
        else gameWinMode.setEnabled(false);
        gameWinMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameWinMode.isChecked()) settings.setDaiHasamiShogiFiveWinRule(true);
                else settings.setDaiHasamiShogiFiveWinRule(false);
            }
        });
    }

    private void initGameWinAmount(){
        this.gameWinAmount = (Spinner) findViewById(R.id.spinner_no_win);
        if(settings.isDaiHasamiShogi())spinnerData.addAll(Arrays.asList(DAI_HASAMI_SHOGI_AMOUNT));
        else spinnerData.addAll(Arrays.asList(HASAMI_SHOGI_AMOUNT));
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerData);
        gameWinAmount.setAdapter(spinnerAdapter);
        gameWinAmount.setSelection(settings.getWinNumber() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menu_item_save){
            settings.setWinNumber(Integer.valueOf(gameWinAmount.getSelectedItem().toString()));
            dao.updateSettings(settings);
        }
        Intent intent = new Intent(getApplicationContext(), ActivityGameControl.class);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }
}
