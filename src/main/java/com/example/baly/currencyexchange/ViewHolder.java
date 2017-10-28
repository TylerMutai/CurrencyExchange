package com.example.baly.currencyexchange;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * This View Holder is used to handle the recycle view that populates a single TextView
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    private TextView countryName;
    private TextView countryId;

    /**
     * @param view the parent View containing the various child views(TextViews)
     * **/
    public ViewHolder(View view){
        super(view);
        countryName = (TextView) view.findViewById(R.id.country_name_text_view);
        countryId = (TextView) view.findViewById(R.id.country_id_text_view);
    }



    public void bindData(CardViewObjects viewObjects){
        //set the values of the TextViews accordingly.
        countryName.setText(viewObjects.getCountryName());
        countryId.setText(viewObjects.getCountryId());
    }
}

