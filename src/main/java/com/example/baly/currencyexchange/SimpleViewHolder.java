package com.example.baly.currencyexchange;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * This class acts as the View Holder to enable re using of various views.
 * It also binds data to views.
 */

public class SimpleViewHolder extends RecyclerView.ViewHolder {

    private TextView btcValue;
    private TextView etcValue;
    private TextView countryName;

    /**
     * @param view the parent View containing the various child views(TextViews)
     **/
    public SimpleViewHolder(View view) {
        super(view);
        btcValue = (TextView) view.findViewById(R.id.other_currency_bitcoin_text_view);
        etcValue = (TextView) view.findViewById(R.id.other_currency_etc_text_view);
        countryName = (TextView) view.findViewById(R.id.country_name_text_view);
    }


    public void bindData(CardViewObjects viewObjects) {
        //set the values of the TextViews accordingly.
        countryName.setText(viewObjects.getCountryName());
        btcValue.setText(String.valueOf(viewObjects.getBtcValue()));
        etcValue.setText(String.valueOf(viewObjects.getEtcValue()));
    }
}
