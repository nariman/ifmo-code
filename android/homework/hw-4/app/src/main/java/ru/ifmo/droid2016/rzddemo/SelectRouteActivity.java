package ru.ifmo.droid2016.rzddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.ifmo.droid2016.rzddemo.cache.DataSchemeVersion;

/**
 * Created by dmitry.trunin on 09.11.2016.
 */
public class SelectRouteActivity extends AppCompatActivity implements View.OnClickListener {

    private static int selectedVersion = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route);

        final EditText departureEditText = (EditText) findViewById(R.id.station_departure);
        final EditText arrivalEditText = (EditText) findViewById(R.id.station_arrival);

        departureEditText.setText(Constants.DEMO_FROM_STATION_NAME);
        arrivalEditText.setText(Constants.DEMO_TO_STATION_NAME);

        final Button btnVersion1 = (Button) findViewById(R.id.btn_timetable_1);
        final Button btnVersion2 = (Button) findViewById(R.id.btn_timetable_2);

        btnVersion1.setOnClickListener(this);
        btnVersion2.setOnClickListener(this);
        btnVersion1.setTag(R.id.tag_version, DataSchemeVersion.V1);
        btnVersion2.setTag(R.id.tag_version, DataSchemeVersion.V2);
    }

    @Override
    public void onClick(View view) {
        final Object tagVersion = view.getTag(R.id.tag_version);
        if (tagVersion instanceof Integer) {
            final @DataSchemeVersion int version = (Integer) tagVersion;

            if (selectedVersion == -1 || selectedVersion == version) {
                selectedVersion = version;
                final Intent timetable = TimetableActivity.createIntent(this,
                        Constants.DEMO_FROM_STATION_ID, Constants.DEMO_FROM_STATION_NAME,
                        Constants.DEMO_TO_STATION_ID, Constants.DEMO_TO_STATION_NAME,
                        version);
                startActivity(timetable);

            } else {
                Toast.makeText(this, R.string.version_warn, Toast.LENGTH_LONG).show();
            }
        }
    }
}
