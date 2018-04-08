package com.example.xu.group59.Adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xu.group59.R;
import com.example.xu.group59.models.Shelter;

/**
 * Created by xu on 3/8/18
 */

public class RestrictionsFilterAdapter extends RecyclerView.Adapter<RestrictionsFilterAdapter.RestrictionsFilterViewHolder> {

    private Shelter.Restrictions[] restrictionsList;
    private RestrictionsFilterListener mListener;

    public static RestrictionsFilterAdapter newInstance(Shelter.Restrictions[] restrictionsList, RestrictionsFilterListener listener) {

        Bundle args = new Bundle();

        RestrictionsFilterAdapter fragment = new RestrictionsFilterAdapter();

        if (restrictionsList == null) {
            restrictionsList = (Shelter.Restrictions[]) new Object[10];
        }

        if (listener != null) {
            fragment.mListener = listener;
        }

        fragment.restrictionsList = restrictionsList;

        return fragment;
    }


    @NonNull
    @Override
    public RestrictionsFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RestrictionsFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestrictionsFilterViewHolder holder, int position) {
        holder.bindData(restrictionsList[position]);
    }

    @Override
    public int getItemCount() {
        return restrictionsList.length;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.filter_list_item_view;
    }

    public class RestrictionsFilterViewHolder extends RecyclerView.ViewHolder {

        private final TextView filterNameTextView;
        private Shelter.Restrictions restriction;

        public RestrictionsFilterViewHolder(View itemView) {
            super(itemView);
            filterNameTextView = itemView.findViewById(R.id.filter_name_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRestrictionsFilterClicked(restriction);
                }
            });
        }

        public void bindData(Shelter.Restrictions restriction) {
            if ((restriction != null) && (restriction.getName() != null)) {
                this.restriction = restriction;
                filterNameTextView.setText(restriction.getName());
            } else {
                filterNameTextView.setText(R.string.not_applicable);
            }
        }

    }

    public interface RestrictionsFilterListener {
        void onRestrictionsFilterClicked(Shelter.Restrictions restriction);
    }
}
