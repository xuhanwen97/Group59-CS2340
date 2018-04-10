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
 * Gender filter adapter for a recycler view
 */

public class GenderFilterAdapter extends RecyclerView.Adapter<GenderFilterAdapter.GenderFilterViewHolder> {

    private Shelter.Gender[] genderList;
    private GenderFilterListener mListener;

    /**
     * Static method to create new instance of the gender filter based on the passed in gender list
     * and listener
     *
     * @param genderList list of genders that each shelter excepts
     * @param listener listener to see when the gender filter should be applied
     * @return the new filter to be applied
     */
    public static GenderFilterAdapter newInstance(Shelter.Gender[] genderList, GenderFilterListener listener) {

        Bundle args = new Bundle();

        GenderFilterAdapter fragment = new GenderFilterAdapter();

        if (genderList == null) {
            genderList = (Shelter.Gender[]) new Object[10];
        }

        if (listener != null) {
            fragment.mListener = listener;
        }

        fragment.genderList = genderList;

        return fragment;
    }


    @NonNull
    @Override

    public GenderFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new GenderFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenderFilterViewHolder holder, int position) {
        holder.bindData(genderList[position]);
    }

    @Override
    public int getItemCount() {
        return genderList.length;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.filter_list_item_view;
    }

    public class GenderFilterViewHolder extends RecyclerView.ViewHolder {

        private final TextView filterNameTextView;
        private Shelter.Gender gender;

        /**
         * Class that calls the super class to viewHolder
         * @param itemView item view that hold the gender list
         */
        public GenderFilterViewHolder(View itemView) {
            super(itemView);
            filterNameTextView = itemView.findViewById(R.id.filter_name_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onGenderFilterClicked(gender);
                }
            });
        }

        /**
         * Sets the text to be in the list if the shelter accepts people of the that gender
         *
         * @param gender the gender of the current user that is to be used to filter the list of
         *               shelters
         */
        public void bindData(Shelter.Gender gender) {
            if ((gender != null) && (gender.getName() != null)) {
                this.gender = gender;
                filterNameTextView.setText(gender.getName());
            } else {
                filterNameTextView.setText(R.string.not_applicable);
            }
        }

    }

    /**
     * interface to to tell if the gender filter was clicked
     */
    public interface GenderFilterListener {
        /**
         *
         * @param gender gender that you are filtering by
         */
        void onGenderFilterClicked(Shelter.Gender gender);
    }
}
