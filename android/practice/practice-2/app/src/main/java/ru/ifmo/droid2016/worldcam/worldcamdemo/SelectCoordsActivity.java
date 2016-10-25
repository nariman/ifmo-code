package ru.ifmo.droid2016.worldcam.worldcamdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.ifmo.droid2016.worldcam.worldcamdemo.input.DoubleRangeValidator;
import ru.ifmo.droid2016.worldcam.worldcamdemo.input.InputValidator;

/**
 * Первый экран приложения с полями ввода широты и долготы и кнопкой "Вперед" по клику на
 * которую происходит переход на {@link NearbyWebcamsActivity} со списком вебкамер в выбранных
 * координатах.
 */
public class SelectCoordsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText latEditText;
    private EditText lngEditText;
    private Button enterButton;
    private InputValidator latValidator;
    private InputValidator lngValidator;

    private static final String KEY_HAS_VALIDATED = "has_validated";

    /**
     * Этот флаг нужен только для того, чтобы после восстановления состояния валидировать поля
     * снова в том случае, если они уже были валидированы до поворота. Потому что до первого
     * нажатия на кнопку "вперед" мы не показываем ошибку (пользователь еще может продолжать
     * вводить правильное значение).
     */
    private boolean hasValidated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_coords_activity);

        latEditText = (EditText) findViewById(R.id.lat_text);
        lngEditText = (EditText) findViewById(R.id.lng_text);
        enterButton = (Button) findViewById(R.id.btn_enter);

        final TextView latErrorText = (TextView) findViewById(R.id.lat_error);
        final TextView lngErrorText = (TextView) findViewById(R.id.lng_error);

        latValidator = new DoubleRangeValidator(latEditText, latErrorText, -90.0, 90.0);
        lngValidator = new DoubleRangeValidator(lngEditText, lngErrorText, -180.0, 180.0);

        enterButton.setOnClickListener(this);

        // If already has validated, validate again so that error text appears
        hasValidated = savedInstanceState != null
                && savedInstanceState.getBoolean(KEY_HAS_VALIDATED, false);
        if (hasValidated) {
            latValidator.validate();
            lngValidator.validate();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == enterButton) {
            final boolean isLatValid = latValidator.validate();
            final boolean isLngValid = lngValidator.validate();
            hasValidated = true;

            // Игнорируем ошибку широты для того, чтобы продемонстрировать обработку ошибок
            if (/*isLatValid && */isLngValid) {
                final double lat = getDouble(latEditText, 60.0);
                final double lng = getDouble(lngEditText, 30.0);
                onCoordsSelected(lat, lng);
            }
        }
    }

    void onCoordsSelected(double latitude, double longitude) {
        final Intent intent = new Intent(getApplicationContext(), NearbyWebcamsActivity.class);
        intent.putExtra(NearbyWebcamsActivity.EXTRA_LATITUDE, latitude);
        intent.putExtra(NearbyWebcamsActivity.EXTRA_LONGITUDE, longitude);
        startActivity(intent);
    }

    private double getDouble(EditText editText, double defaultValue) {
        try {
            final String strValue = editText.getText().toString();
            final double value = Double.parseDouble(strValue);
            return value;

        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_HAS_VALIDATED, hasValidated);
    }
}
