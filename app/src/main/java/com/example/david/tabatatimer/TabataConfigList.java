package com.example.david.tabatatimer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.david.tabatatimer.data.Tabataconfig;
import com.example.david.tabatatimer.data.TabataconfigDAO;

import java.util.List;

public class TabataConfigList extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabata_config_list);

        final List<Tabataconfig> configurations = TabataconfigDAO.selectAll();

        listView = (ListView) findViewById(R.id.listView);

        ArrayAdapter<Tabataconfig> adapter = new ArrayAdapter<Tabataconfig>(this, R.layout.activity_tabata_config_list_row, configurations) {

            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                // Inflate LAYOUT
                if(convertView == null){
                    LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = (ViewGroup)inflater.inflate(R.layout.activity_tabata_config_list_row, null);
                }

                // VIEW
                TextView configLabel = (TextView) convertView.findViewById(R.id.TabataName);
                TextView work = (TextView) convertView.findViewById(R.id.work_list);
                TextView rest = (TextView) convertView.findViewById(R.id.rest_list);
                TextView prepare = (TextView) convertView.findViewById(R.id.prepare_list);
                TextView cycle = (TextView) convertView.findViewById(R.id.cycle_list);
                TextView tabata = (TextView) convertView.findViewById(R.id.tabatas_list);


                // Charger la vue avec les données
                Tabataconfig config = configurations.get(position);
                configLabel.setText(config.getName());
                work.setText("Work : "+Integer.toString(config.getWork()));
                rest.setText("Rest : "+Integer.toString(config.getRest()));
                prepare.setText("Prepare : "+Integer.toString(config.getPrepare()));
                cycle.setText("Cycle : "+Integer.toString(config.getCycles()));
                tabata.setText("Tabata : "+Integer.toString(config.getTabatas()));

                //
                return convertView;
            }
        };
        listView.setAdapter(adapter);
    }
}

