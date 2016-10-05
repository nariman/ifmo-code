package ru.ifmo.android_2016.calc;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public final class CalculatorActivity extends Activity {

    private static final int[] NUMBERS_RES_ID = new int[]{
            R.id.d1,
            R.id.d2,
            R.id.d3,
            R.id.d4,
            R.id.d5,
            R.id.d6,
            R.id.d7,
            R.id.d8,
            R.id.d9,
            R.id.d0
    };

    private static final int[] ACTIONS_RES_ID = new int[]{
            R.id.div,
            R.id.mul,
            R.id.sub,
            R.id.add,
            R.id.eqv,
            R.id.clear
    };

    private TextView resultTextView;

    private StringBuilder leftOperand;
    private StringBuilder rightOperand;
    private int operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        resultTextView = (TextView) findViewById(R.id.result);
        resultTextView.setMovementMethod(new ScrollingMovementMethod());
        clearView();

        if (savedInstanceState != null) {
            leftOperand = new StringBuilder(savedInstanceState.getString("leftOperand"));
            rightOperand = new StringBuilder(savedInstanceState.getString("rightOperand"));
            operator = savedInstanceState.getInt("operator");
            updateView();
        }

        View.OnClickListener numbersListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                char sym = ((Button) v).getText().charAt(0);
                StringBuilder operand = operator == 0 ? leftOperand : rightOperand;

                if (operand.toString().equals("0")) {
                    if (v.getId() != R.id.d0) {
                        operand.setCharAt(0, sym);
                    }
                } else {
                    operand.append(sym);
                }

                updateView();
            }
        };

        View.OnClickListener actionsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.clear) {
                    clearView();
                } else {
                    if (operator != 0 && rightOperand.length() != 0) {
                        calculate();
                    }

                    if (v.getId() != R.id.eqv) {
                        operator = v.getId();
                    }

                    updateView();
                }
            }
        };

        for (int id : NUMBERS_RES_ID) {
            findViewById(id).setOnClickListener(numbersListener);
        }

        for (int id : ACTIONS_RES_ID) {
            findViewById(id).setOnClickListener(actionsListener);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("leftOperand", leftOperand.toString());
        outState.putString("rightOperand", rightOperand.toString());
        outState.putInt("operator", operator);
    }

    private void clearView() {
        leftOperand = new StringBuilder("0");
        rightOperand = new StringBuilder();
        operator = 0;
        updateView();
    }

    private void updateView() {
        if (operator != 0) {
            int actionStringResId = 0;

            switch (operator) {
                case R.id.add:
                    actionStringResId = R.string.action_add;
                    break;
                case R.id.sub:
                    actionStringResId = R.string.action_sub;
                    break;
                case R.id.mul:
                    actionStringResId = R.string.action_mul;
                    break;
                case R.id.div:
                    actionStringResId = R.string.action_div;
                    break;
            }

            resultTextView.setText(
                    leftOperand.toString() + getString(actionStringResId) + rightOperand.toString()
            );
        } else {
            resultTextView.setText(leftOperand.toString());
        }
    }

    private void notifyView(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void calculate() {
        Long left;
        Long right;
        Long result = 0L;

        try {
            left = Long.parseLong(leftOperand.toString());
            right = Long.parseLong(rightOperand.toString());
        } catch (NumberFormatException e) {
            notifyView(getString(R.string.overflow));
            clearView();
            return;
        }

        try {
            switch (operator) {
                case R.id.add:
                    if (right > 0 ? left > Long.MAX_VALUE - right
                            : left < Long.MIN_VALUE - right) {
                        throw new ArithmeticException(getString(R.string.overflow));
                    }
                case R.id.sub:
                    if (right > 0 ? left < Long.MIN_VALUE + right
                            : left > Long.MAX_VALUE + right) {
                        throw new ArithmeticException(getString(R.string.overflow));
                    }
                    break;
                case R.id.mul:
                    if (right > 0 ? left > Long.MAX_VALUE / right || left < Long.MIN_VALUE / right
                            : (right < -1 ? left > Long.MIN_VALUE / right || left < Long.MAX_VALUE / right
                            : right == -1 && left == Long.MIN_VALUE)) {
                        throw new ArithmeticException(getString(R.string.overflow));
                    }
                case R.id.div:
                    if ((left == Long.MIN_VALUE) && (right == -1)) {
                        throw new ArithmeticException(getString(R.string.overflow));
                    } else if (right == 0) {
                        throw new ArithmeticException(getString(R.string.zero_division));
                    }
            }
        } catch (ArithmeticException e) {
            notifyView(e.getMessage());
            clearView();
            return;
        }

        switch (operator) {
            case R.id.add:
                result = left + right;
                break;
            case R.id.sub:
                result = left - right;
                break;
            case R.id.mul:
                result = left * right;
                break;
            case R.id.div:
                result = left / right;
                break;
        }

        leftOperand = new StringBuilder(result.toString());
        rightOperand = new StringBuilder();
        operator = 0;
    }
}

