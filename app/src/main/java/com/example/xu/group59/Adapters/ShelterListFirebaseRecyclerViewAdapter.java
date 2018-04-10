package com.example.xu.group59.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xu.group59.R;
import com.example.xu.group59.models.Shelter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

/**
 * A class that is the shelter list fragment that uses firebase into an adapter
 */
public final class ShelterListFirebaseRecyclerViewAdapter extends FirebaseRecyclerAdapter<Shelter, ShelterListFirebaseRecyclerViewAdapter.ShelterListViewHolder> {

    private ShelterListRecyclerViewAdapter.ShelterListRecyclerViewListener mListener;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options possible configuration options for the RecycleView.Adapter
     */
    private ShelterListFirebaseRecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<Shelter> options) {
        super(options);
    }

    /**
     * A method that makes a new Instance of the shelter list firebase recycler view adapter
     * @param listener a listener of the shelter list recycler view adapter
     * @param options a firebase recycler options<>  options
     * @return a shetler list recycler view adapter
     */
    public static ShelterListFirebaseRecyclerViewAdapter newInstance(ShelterListRecyclerViewAdapter.ShelterListRecyclerViewListener listener, FirebaseRecyclerOptions<Shelter> options) {
        ShelterListFirebaseRecyclerViewAdapter adapter = new ShelterListFirebaseRecyclerViewAdapter(options);
        adapter.mListener = listener;

        return adapter;
    }

    @Override
    public ShelterListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ShelterListViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.shelter_list_item_view;
    }

    @Override
    protected void onBindViewHolder(@NonNull ShelterListViewHolder holder, int position, @NonNull Shelter model) {
        holder.bindData(model);
    }

    public class ShelterListViewHolder extends RecyclerView.ViewHolder {

        private final TextView shelterNameTextView;
        private Shelter data;

        /**
         * a constructor that creates the shelter list view holder
         * @param itemView a view of the item view
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
         * @param shelter shelter that you are trying to find
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
}
