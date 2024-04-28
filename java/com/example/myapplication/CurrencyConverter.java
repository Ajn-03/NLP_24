package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.WindowDecorActionBar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrencyConverter extends AppCompatActivity {
    private CurrencyService currencyService;
    Button convertButton; EditText amountEditText;
    public Spinner fromCurrencySpinner,toCurrencySpinner;;
    private TextView convertedAmountTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);
        currencyService = RetrofitClient.getClient().create(CurrencyService.class);
        convertButton=(Button)findViewById(R.id.convert_button);
        amountEditText=(EditText) findViewById(R.id.amount_edit_text);
        fromCurrencySpinner=(Spinner)findViewById(R.id.from_currency_spinner);
        toCurrencySpinner=(Spinner)findViewById(R.id.to_currency_spinner);
        convertedAmountTextView = findViewById(R.id.converted_amount_text_view);
        CurrencyTask task = new CurrencyTask();
        task.execute();
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input and selected currencies
                String amountString = amountEditText.getText().toString();
                String fromCurrency = (String) fromCurrencySpinner.getSelectedItem();
                String toCurrency = (String) toCurrencySpinner.getSelectedItem();
                //String baseCurrency="EUR";
                // Make API call using Retrofit
                Call<ExchangeRateResponse> call = currencyService.getExchangeRate("4c7d83bce0b64811bade4625e9d2024e", fromCurrency);

                //currencyService.getExchangeRate(fromCurrency)
                        call.enqueue(new Callback<ExchangeRateResponse>() {
                            @Override
                            public void onResponse(Call<ExchangeRateResponse> call, Response<ExchangeRateResponse> response) {
                                if (response.isSuccessful()) {
                                    ExchangeRateResponse exchangeRateResponse = response.body();
                                    double conversionRate = exchangeRateResponse.getRates().get(toCurrency);
                                    double amount = Double.parseDouble(amountString);
                                    double convertedAmount = amount * conversionRate;

                                    // Update UI with converted amount
                                    convertedAmountTextView.setText("Converted Amount: " + convertedAmount + " " + toCurrency);
                                } else {
                                    // Handle API call failure
                                    Toast.makeText(CurrencyConverter.this, "Error fetching exchange rates", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ExchangeRateResponse> call, Throwable t) {
                                // Handle network errors
                                Toast.makeText(CurrencyConverter.this, "Network error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
    private class CurrencyTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://openexchangerates.org/api/currencies.json?prettyprint=false&show_alternative=false&show_inactive=false")
                    .get()
                    .addHeader("accept", "application/json")
                    .build();

            try (okhttp3.Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
                Iterator<String> keys = jsonObject.keys();
                List<String> currencyCodes = new ArrayList<>();
                while (keys.hasNext()) {
                    String key = keys.next();
                    currencyCodes.add(key);
                }
                return currencyCodes;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> currencyCodes) {
            if (currencyCodes != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CurrencyConverter.this, android.R.layout.simple_spinner_item, currencyCodes);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                fromCurrencySpinner.setAdapter(adapter);
                toCurrencySpinner.setAdapter(adapter);
            }
        }
    }
}