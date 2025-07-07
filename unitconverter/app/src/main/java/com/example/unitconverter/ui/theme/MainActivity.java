package com.example.unitconverter.ui.theme; // Make sure this matches your package name

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.unitconverter.R;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText inputValueEditText;
    private Spinner fromUnitSpinner;
    private Spinner toUnitSpinner;
    private Button convertButton;
    private TextView resultTextView;
    private TextView errorTextView;

    private enum LengthUnit {
        METRE, MILLIMETRE, MILE, FOOT;

        public static LengthUnit fromString(String value) {
            for (LengthUnit unit : values()) {
                if (unit.name().equalsIgnoreCase(value)) {
                    return unit;
                }
            }
            return null;
        }
    }

    private final Map<LengthUnit, Double> conversionFactors = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conversionFactors.put(LengthUnit.METRE, 1.0);
        conversionFactors.put(LengthUnit.MILLIMETRE, 0.001);
        conversionFactors.put(LengthUnit.MILE, 1609.34);
        conversionFactors.put(LengthUnit.FOOT, 0.3048);

        inputValueEditText = findViewById(R.id.inputValueEditText);
        fromUnitSpinner = findViewById(R.id.fromUnitSpinner);
        toUnitSpinner = findViewById(R.id.toUnitSpinner);
        convertButton = findViewById(R.id.convertButton);
        resultTextView = findViewById(R.id.resultTextView);
        errorTextView = findViewById(R.id.errorTextView);

        // Populate Spinners
        String[] units = getResources().getStringArray(R.array.length_units);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromUnitSpinner.setAdapter(adapter);
        toUnitSpinner.setAdapter(adapter);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performConversion();
            }
        });
    }

    private void performConversion() {
        String inputValueString = inputValueEditText.getText().toString();
        String fromUnitString = fromUnitSpinner.getSelectedItem().toString();
        String toUnitString = toUnitSpinner.getSelectedItem().toString();

        errorTextView.setText("");
        resultTextView.setText(getString(R.string.result_placeholder)); // Reset result

        if (inputValueString.isEmpty()) {
            errorTextView.setText(getString(R.string.error_invalid_input));
            return;
        }

        double inputValue;
        try {
            inputValue = Double.parseDouble(inputValueString);
        } catch (NumberFormatException e) {
            errorTextView.setText(getString(R.string.error_invalid_input));
            return;
        }

        LengthUnit fromUnit = LengthUnit.fromString(fromUnitString);
        LengthUnit toUnit = LengthUnit.fromString(toUnitString);

        if (fromUnit == null || toUnit == null) {
            errorTextView.setText("Internal error: Invalid unit selected.");
            return;
        }

        if (fromUnit == toUnit) {
            errorTextView.setText(getString(R.string.error_same_unit));
            return;
        }

        double valueInMetres = inputValue * conversionFactors.get(fromUnit);
        double convertedValue = valueInMetres / conversionFactors.get(toUnit);

        String toUnitDisplayName = toUnit.name().substring(0, 1).toUpperCase() + toUnit.name().substring(1).toLowerCase();
        resultTextView.setText(String.format(java.util.Locale.US, "%.2f %s", convertedValue, toUnitDisplayName));
    }
}
