package com.example.baly.currencyexchange;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ArrayList<CardViewObjects> cardViewObjectsList = new ArrayList<>();
    private Button button;
    private ProgressBar progressBar;
    private boolean clicked = true;
    private RecyclerView recyclerView;
    private int mainCount = 5;
    private boolean userFile = false;
    private String urlPattern = "min-api.cryptocompare.com/data/price?fsym=ETC&tsyms=BTC,";
    private ArrayList<HashMap<String, String>> builder = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            setUpViews(cardViewObjectsList, R.id.card_holder_recyler_view, false, R.layout.card_layout_holder);
        } else {
            setContentView(R.layout.activity_main);

            /**
             * @param clicked
             * Indicates a click action. true represents clicked. false represents not clicked.
             * **/

            //Used to animate various touch events.
            button = (Button) findViewById(R.id.retry_button);
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
            generateList();
            LinearLayout mLayout = (LinearLayout) findViewById(R.id.help);
            final LinearLayout mLayouts = (LinearLayout) findViewById(R.id.main_layout);
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) findViewById(R.id.help_text_view);
                    if (clicked) {
                        button.setVisibility(View.GONE);
                        mLayouts.setBackgroundColor(Color.GRAY);
                        textView.setVisibility(View.VISIBLE);
                        if(recyclerView==null){

                        }else {
                            recyclerView.setVisibility(View.GONE);
                        }
                        clicked = false;
                    } else {
                        textView.setVisibility(View.GONE);
                        button.setVisibility(View.VISIBLE);
                        mLayouts.setBackgroundColor(Color.WHITE);
                        clicked = true;
                        if(recyclerView==null){

                        }else {
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                    }
                }
            });
            FloatingActionButton mButton = (FloatingActionButton) findViewById(R.id.float_action_button);
            mButton.bringToFront();
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) findViewById(R.id.help_text_view);
                    textView.setVisibility(View.GONE);
                    mLayouts.setBackgroundColor(Color.WHITE);
                    clicked = true;
                    if (button.getVisibility() == View.VISIBLE) {
                        Toast.makeText(getApplicationContext(), "Please connect to the internet by pressing the " +
                                "retry button.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //Create an array list of countries that will be selected from a list.
                    List<CardViewObjects> myList = new ArrayList<>();
                    SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(getString(R.string.country_list_preference_file), Context.MODE_PRIVATE);
                    Map<String, ?> sharedPrefKeys = sharedPref.getAll();
                    for (Map.Entry<String, ?> entry : sharedPrefKeys.entrySet()) {
                        myList.add(new CardViewObjects(entry.getKey(), entry.getValue().toString()));
                    }

                    //finally populate the recycle View with the Country's list and display the list.
                    setContentView(R.layout.select_country_activity);
                    setUpViews(myList, R.id.list_item_view, true, R.layout.list_view_items);
                    RecyclerView rv = (RecyclerView) findViewById(R.id.list_item_view);
                    Toast.makeText(MainActivity.this, "Click on a city to select it.", Toast.LENGTH_LONG).show();
                    rv.setVisibility(View.VISIBLE);
                    rv.bringToFront();
                }
            });

        }
    }

    public void onListViewClick(View v) {
        TextView countryId = (TextView) v.findViewById(R.id.country_id_text_view);
        TextView countryName = (TextView) v.findViewById(R.id.country_name_text_view);
        String id = countryId.getText().toString();
        String country = countryName.getText().toString();
        Context context = MainActivity.this;
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.user_list_preference_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (!sharedPref.contains("PREF")) {
            editor.putString("PREF", "PREF");
        }
        editor.putString(id, country);
        editor.apply();
        Toast.makeText(getApplicationContext(), "Added successfully", Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    //saves the current state of the card view objects so that the user doesn't have to fetch a fresh list everytime
    //the user returns to the main activity from the conversion screen.
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("listArray", cardViewObjectsList);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onCardViewClick(View v) {
        TextView textView = (TextView) v.findViewById(R.id.country_name_text_view);
        String countryName = textView.getText().toString();
        textView = (TextView) v.findViewById(R.id.other_currency_bitcoin_text_view);
        String btcValue = textView.getText().toString();
        textView = (TextView) v.findViewById(R.id.other_currency_etc_text_view);
        String etcValue = textView.getText().toString();

        Intent intent = new Intent(MainActivity.this, CurrencyConversionActivity.class);
        intent.putExtra("countryName", countryName);
        intent.putExtra("conversionValueBtc", btcValue);
        intent.putExtra("conversionValueEtc", etcValue);
        startActivity(intent);
    }

    private void generateList() {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        Context context = MainActivity.this;

        //Get the preference file that the user defined for specific card views.
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.user_list_preference_file), Context.MODE_PRIVATE);

        //Check of the key "PREF" exists. if it doesn't, then the preference file doesn't exist and hence the default preference file is used.
        if (sharedPref.contains("PREF")) {
            userFile = true;
        } else {
            sharedPref = context.getSharedPreferences(getString(R.string.country_list_preference_file), Context.MODE_PRIVATE);

            //Check if the default preference file exists. if it Doesn't, a new one is created.
            if (!sharedPref.contains("USD")) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("KES", "Kenya");
                editor.putString("NGN", "Nigeria");
                editor.putString("USD", "United States");
                editor.putString("GHC", "Ghana");
                editor.putString("TZS", "Tanzania");
                editor.putString("UGX", "Uganda");
                editor.putString("EUR", "Europe");
                editor.putString("XOF", "Senegal");
                editor.putString("ZAR", "South Africa");
                editor.putString("XAF", "Gabon");
                editor.putString("AUD", "Australia");
                editor.putString("XOF", "Mali");
                editor.putString("KWD", "Kuwait");
                editor.putString("RWF", "Rwanda");
                editor.putString("LSL", "Lesotho");
                editor.putString("ALL", "Algeria");
                editor.putString("MXN", "Mexico");
                editor.putString("NAD", "Namibia");
                editor.putString("EGP", "Egypt");
                editor.putString("ARS", "Argentina");
                editor.apply();
            }
        }

        /**
         * Convert the map returned from the
         * @param sharedPref object into an array list of type Hash Map.
         * Check if
         * @param builder is populated, Hence avoiding duplication of data.
         * **/

       if (builder.size()==0){
            Map<String, ?> sharedPrefKeys = sharedPref.getAll();
            HashMap<String, String> values = new HashMap<>();
            for (Map.Entry<String, ?> entry : sharedPrefKeys.entrySet()) {
                urlPattern = urlPattern + entry.getKey() + ",";
                values.put(entry.getKey(), entry.getValue().toString());
                builder.add(values);
            }
        }

        /**
         * @param mainCount
         * stores a value of 5 or the size of the user defined preference file array
         * for the card view objects being populated on the screen.
         * **/
        if (userFile) {
            mainCount = builder.size();
        }
        BackgroundTask bg = new BackgroundTask(new AsyncResponse() {

            @Override
            public void processFinish(String message, String result) {
                progressBar.setVisibility(View.GONE);
                if (result.equals("")) {

                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            generateList();
                            button.setVisibility(View.GONE);
                        }
                    });
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else {
                    String bitcoin = "";
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject = new JSONObject(result);
                        bitcoin = jsonObject.getString("BTC");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Create only card view entries for the default view, either according to the defined user's preferences
                    //Or the default preference file.
                    String currencyValue = "";
                    for (int count = 0; count < mainCount; count++) {
                        HashMap<String, String> hash = builder.get(count);
                        Object[] keySetArray = hash.keySet().toArray();
                        String key = keySetArray[count].toString();
                        //Ensure that the Key 'PREF' isn't diplayed
                        if (key.equals("PREF")) {
                            count++;
                            if (count >= mainCount) {
                                break;
                            } else {
                                key = keySetArray[count].toString();
                            }
                        }
                        Float btcInt = Float.parseFloat(bitcoin);
                        try {
                            currencyValue = jsonObject.getString(key);
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "Request will complete with some errors. " +
                                    "The country isn't available", Toast.LENGTH_LONG).show();
                        }
                        if (currencyValue.equals("")) {
                            currencyValue = "0";
                        }
                        Float currencyValueEtc = Float.parseFloat(currencyValue);
                        Float currencyValueBtc = getBitcoinValue(btcInt, Float.parseFloat(currencyValue));
                        cardViewObjectsList.add(new CardViewObjects(currencyValueBtc, currencyValueEtc, hash.get(key)));
                    }
                    setUpViews(cardViewObjectsList, R.id.card_holder_recyler_view, false, R.layout.card_layout_holder);
                }
            }
        }, urlPattern);
        bg.execute();
    }

    private void setUpViews(List<CardViewObjects> cardViewObjects, int rv, boolean viewType, int layout) {
        List<CardViewObjects> cardViewObjectsLists = new ArrayList<>();
        if (cardViewObjects != null) {
            cardViewObjectsLists.addAll(cardViewObjects);
        }
        //Specify a Recycle View adapter
        MyCardAdapter recylerviewAdapter = new MyCardAdapter(cardViewObjectsLists, viewType, layout);

        //The Recycle view itself
        recyclerView = (RecyclerView) findViewById(rv);

        //specify that the recycler view's layout size will not change. Improves performance.
        recyclerView.setHasFixedSize(true);

        //specify the layout manager for the Recycle View
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setAdapter(recylerviewAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private Float getBitcoinValue(Float btc, Float val) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        Float btcValue = val / btc;
        btcValue = Float.parseFloat(formatter.format(btcValue));
        return btcValue;
    }

}
