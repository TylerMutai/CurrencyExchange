package com.example.baly.currencyexchange;


import android.os.Parcel;
import android.os.Parcelable;


/**
 * This class specifies the various data items the CardView layout will hold.
 */

public class CardViewObjects implements Parcelable {
    private float btcValue;
    private float etcValue;
    private String countryName;
    private String countryId;

    public CardViewObjects(float btcValue, float etcValue, String countryName) {
        this.btcValue = btcValue;
        this.etcValue = etcValue;
        this.countryName = countryName;

    }

    public String getCountryId() {
        return countryId;
    }

    public CardViewObjects(String countryId, String countryName){
        this.countryName = countryName;
        this.countryId = countryId;

    }

    //Used with onSavedInstanceState, to save the instance of this particular class.
    public CardViewObjects(Parcel p) {
        this.btcValue = p.readFloat();
        this.etcValue = p.readFloat();
        this.countryName = p.readString();
    }

    //getter methods
    public float getBtcValue() {
        return btcValue;
    }

    public float getEtcValue() {
        return etcValue;
    }

    public String getCountryName() {
        return countryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.btcValue);
        dest.writeFloat(this.etcValue);
        dest.writeString(this.countryName);
    }

    public static final Parcelable.Creator<CardViewObjects> CREATOR = new Parcelable.Creator<CardViewObjects>() {
        public CardViewObjects createFromParcel(Parcel in) {
            return new CardViewObjects(in);
        }

        public CardViewObjects[] newArray(int size) {
            return new CardViewObjects[size];
        }
    };
}

