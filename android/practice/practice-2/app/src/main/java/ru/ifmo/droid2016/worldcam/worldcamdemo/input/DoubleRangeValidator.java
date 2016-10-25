package ru.ifmo.droid2016.worldcam.worldcamdemo.input;

import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by dmitry.trunin on 03.10.2016.
 */
public class DoubleRangeValidator extends InputValidator {

    private final double minValue;
    private final double maxValue;

    public DoubleRangeValidator(@NonNull EditText inputEditText,
                                @NonNull TextView errorText,
                                double minValue,
                                double maxValue) {
        super(inputEditText, errorText);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    protected boolean isValid(String value) {
        try {
            final double d = Double.parseDouble(value);
            return d >= minValue && d <= maxValue;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
