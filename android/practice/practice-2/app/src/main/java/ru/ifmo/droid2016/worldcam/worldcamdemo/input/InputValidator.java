package ru.ifmo.droid2016.worldcam.worldcamdemo.input;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by dmitry.trunin on 03.10.2016.
 */
public abstract class InputValidator implements TextWatcher {

    @NonNull
    private final EditText inputEditText;

    @NonNull
    private final TextView errorText;

    private boolean hasValidated = false;

    protected InputValidator(@NonNull EditText inputEditText, @NonNull TextView errorText) {
        this.inputEditText = inputEditText;
        this.errorText = errorText;
        inputEditText.addTextChangedListener(this);
    }

    protected abstract boolean isValid(String value);

    /**
     * Checks validity of the input field, and display error text if it it's invalid.
     *
     * @return true if input is valid, false otherwise
     */
    public boolean validate() {
        final boolean isValid = isValid(inputEditText.getText().toString());
        if (!isValid) {
            errorText.setVisibility(View.VISIBLE);
        }
        hasValidated = true;
        return isValid;
    }

    @Override
    public void afterTextChanged(Editable s) {
        final String value = s.toString();
        if (TextUtils.isEmpty(value) || isValid(value)) {
            errorText.setVisibility(View.INVISIBLE);
        } else if (hasValidated) {
            errorText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}
