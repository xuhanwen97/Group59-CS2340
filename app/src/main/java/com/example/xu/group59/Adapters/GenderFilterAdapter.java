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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xu on 3/8/18
 */

public class GenderFilterAdapter extends RecyclerView.Adapter<GenderFilterAdapter.GenderFilterViewHolder> {

    private Shelter.Gender[] genderList;
    private GenderFilterListener mListener;

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


    public interface GenderFilterListener {
        void onGenderFilterClicked(Shelter.Gender gender);
    }
}
