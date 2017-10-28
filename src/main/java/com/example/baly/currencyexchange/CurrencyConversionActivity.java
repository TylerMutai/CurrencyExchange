package com.example.baly.currencyexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by baly on 10/23/17.
 */

public class CurrencyConversionActivity extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.currency_conversion_layout);
        TextView textView = (TextView) findViewById(R.id.conversion_screen_country_text_view);
            Intent intent = getIntent();
            String countryName = intent.getStringExtra("countryName");

            //Bitcoin and Etherium coin values returned from the main activity's screen, to be used for conversion.
            final Double btcValue = Double.parseDouble(intent.getStringExtra("conversionValueBtc"));
            final Double etcValue = Double.parseDouble(intent.getStringExtra("conversionValueEtc"));

            textView.setText(countryName);
            textView = (TextView) findViewById(R.id.bitcoin_conversion_text_view);
            textView.setText(getString(R.string.conversion_goes_here, countryName));
            textView = (TextView) findViewById(R.id.etc_conversion_text_view);
            textView.setText(getString(R.string.conversion_goes_here, countryName));
        Button button = (Button) findViewById(R.id.convert_currency_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editTextBtc = (EditText) findViewById(R.id.btc_value_edit_text);
                    EditText editTextEtc = (EditText) findViewById(R.id.etc_value_edit_text);
                    TextView textViewBtc = (TextView) findViewById(R.id.bitcoin_conversion_text_view);
                    TextView textViewEtc = (TextView) findViewById(R.id.etc_conversion_text_view);
                    if (editTextBtc.getText().toString().equals("") || editTextEtc.getText().toString().equals("")) {
                        textViewBtc.setText("0");
                        textViewEtc.setText("0");
                    } else {
                        Double userBtcValue = Double.valueOf(editTextBtc.getText().toString());
                        Double userEtcValue = Double.valueOf(editTextEtc.getText().toString());
                        ArrayList<Double> result = new ArrayList<>();
                        result.addAll(calculateCurrencyValues(btcValue, userBtcValue, etcValue, userEtcValue));
                        textViewBtc.setText(String.format(Locale.US, "%.2f", result.get(0)));
                        textViewEtc.setText(String.format(Locale.US, "%.2f", result.get(1)));
                    }
                }
            });
        }

    //Returns an ArrayList of type Double containing the computed values of bitcoins and etherium coins
    private ArrayList<Double> calculateCurrencyValues(Double btcValue, Double userBtcValue, Double etcValue, Double userEtcValue){
        Double btcResult = parseDouble(btcValue * userBtcValue);
        Double etcResult = parseDouble(etcValue * userEtcValue);
        ArrayList<Double> result = new ArrayList<>();
        result.add(btcResult);
        result.add(etcResult);
        return result;
    }
    //This used to round off any double values to two decimal places for easier readability.
    private Double parseDouble(Double parseValue){
        NumberFormat format = new DecimalFormat("#0.00");

        return Double.valueOf(format.format(parseValue));
    }
    @Override
    protected void onStop(){
        super.onStop();
        finish();
    }
}
