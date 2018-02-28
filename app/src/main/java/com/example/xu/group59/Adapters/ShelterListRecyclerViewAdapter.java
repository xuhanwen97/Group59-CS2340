package com.example.xu.group59.Adapters;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xu.group59.R;
import com.example.xu.group59.models.Shelter;

import java.util.List;

public class ShelterListRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<Shelter> shelterList;
    private ShelterListRecyclerViewListener mListener;

    public ShelterListRecyclerViewAdapter(List<Shelter> shelterList, ShelterListRecyclerViewListener listener) {
        if (shelterList != null && listener != null) {
            this.shelterList =  shelterList;
            this.mListener = listener;
        }
    }

    @Override
    public ShelterListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ShelterListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ShelterListViewHolder) holder).bindData(shelterList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.shelter_list_item_view;
    }

    @Override
    public int getItemCount() {
        return shelterList.size();
    }

    public class ShelterListViewHolder extends RecyclerView.ViewHolder {

        private TextView shelterNameTextView;

        public ShelterListViewHolder(View itemView) {
            super(itemView);
            shelterNameTextView = (TextView) itemView.findViewById(R.id.shelter_name_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onShelterClicked(shelterList.get(getAdapterPosition()));
                }
            });

        }

        public void bindData(Shelter shelter) {
            if (shelter.getShelterName() != null) {
                shelterNameTextView.setText(shelter.getShelterName());
            } else {
                shelterNameTextView.setText(R.string.not_applicable);
            }
        }

    }

    public interface ShelterListRecyclerViewListener {
        void onShelterClicked(Shelter shelter);
    }
}
