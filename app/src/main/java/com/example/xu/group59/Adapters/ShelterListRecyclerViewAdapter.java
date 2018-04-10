package com.example.xu.group59.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xu.group59.R;
import com.example.xu.group59.models.Shelter;

import java.util.List;

public class ShelterListRecyclerViewAdapter extends RecyclerView.Adapter<ShelterListRecyclerViewAdapter.ShelterListViewHolder> {

    private ShelterListRecyclerViewListener mListener;
    private List<Shelter> shelterList;

    public static ShelterListRecyclerViewAdapter newInstance(ShelterListRecyclerViewListener listener, List<Shelter> shelterList) {
        ShelterListRecyclerViewAdapter adapter = new ShelterListRecyclerViewAdapter();
        adapter.mListener = listener;
        adapter.shelterList = shelterList;

        return adapter;
    }

    @Override
    public ShelterListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ShelterListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShelterListViewHolder holder, int position) {
        holder.bindData(shelterList.get(position));
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

        private final TextView shelterNameTextView;
        private Shelter data;

        public ShelterListViewHolder(View itemView) {
            super(itemView);
            shelterNameTextView = itemView.findViewById(R.id.shelter_name_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onShelterClicked(data);
                }
            });

        }

        public void bindData(Shelter shelter) {
            if (shelter.getShelterName() != null) {
                data = shelter;
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
