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

/**
 * Class that contains the view adapter for the shelter list recycler. Contains attributes and
 * methods for the view adapter.
 */
public class ShelterListRecyclerViewAdapter extends RecyclerView.Adapter<ShelterListRecyclerViewAdapter.ShelterListViewHolder> {

    private ShelterListRecyclerViewListener mListener;
    private List<Shelter> shelterList;

    /**
     * Creates a new instance of the Shelter List Recycler View Adapter. Sets the attributes for the
     * listener and the shelter list.
     * @param listener tracks changes for the view adapter.
     * @param shelterList the list of shelters in the shelter list recycler
     * @return a new adapter for the Shelter List Recycler View
     */
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

    /**
     * A class for the shelter list view holder. Holds the shelter list view.
     */
    public class ShelterListViewHolder extends RecyclerView.ViewHolder {

        private final TextView shelterNameTextView;
        private Shelter data;

        /**
         * Constructor for the shelter list view holder. Calls to the super class constructor.
         * @param itemView a view of an item
         */
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

        /**
         * Set the text to be displayed in the list of the specific shelter that is being search for
         * @param shelter shelter that is being searched for
         */
        public void bindData(Shelter shelter) {
            if (shelter.getShelterName() != null) {
                data = shelter;
                shelterNameTextView.setText(shelter.getShelterName());
            } else {
                shelterNameTextView.setText(R.string.not_applicable);
            }
        }

    }

    /**
     * A Shelter List Recycler View Listener for the above adapter. Tracks when a shelter is clicked
     * on.
     */
    public interface ShelterListRecyclerViewListener {
        /**
         * Checks if the shelter has been clicked on.
         * @param shelter the shelter being checked
         */
        void onShelterClicked(Shelter shelter);
    }
}
