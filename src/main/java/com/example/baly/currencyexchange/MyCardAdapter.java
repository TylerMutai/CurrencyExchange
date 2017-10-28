package com.example.baly.currencyexchange;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * My Custom Adapter to handle the CardView Contents
 */

public class MyCardAdapter extends RecyclerView.Adapter {
    //The list object containing specific bitcoin and etc values for different currencies.
    private List<CardViewObjects> models = new ArrayList<>();
    private boolean viewHolder;
    private int layout;

    /**
     * @param models
     * A collection of CardViewObjects whose data is used in each view holder.
     *@param viewHolder
     * Stores a boolean which specifies which type of View Holder to use. True indicates the class ViewHolder
     * **/
    public MyCardAdapter(List<CardViewObjects> models, boolean viewHolder,int layout){
        //Check if the List Array does contain values
        if (models!=null){
            this.models.addAll(models);
        }
        this.viewHolder = viewHolder;
        this.layout=layout;
    }
    /**
     * @param parent
     * specifes the ViewGroup to which the new view will be added after it is bound to an adapter position.
     * **/

    //Called when this Adapter class is created.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false);
        if(viewHolder) {
            return new ViewHolder(view);
        } else {
            return new SimpleViewHolder(view);
        }

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(viewHolder) {
            ((ViewHolder) holder).bindData(models.get(position));
        } else {
            ((SimpleViewHolder) holder).bindData(models.get(position));
        }


    }

    @Override
    public int getItemCount() {
        return models.size();
    }

   @Override
    public int getItemViewType(int position) {
        return this.layout;
    }
}
