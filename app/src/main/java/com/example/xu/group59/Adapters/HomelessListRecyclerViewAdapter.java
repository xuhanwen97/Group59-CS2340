package com.example.xu.group59.Adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.xu.group59.R;
import com.example.xu.group59.models.HomelessPerson;

import java.util.List;

/**
 * Created by xu on 4/23/18
 */

public class HomelessListRecyclerViewAdapter extends RecyclerView.Adapter<HomelessListRecyclerViewAdapter.HomelessListViewHolder>{

    private HomelessListRecyclerViewListener mListener;
    private List<HomelessPerson> homelessList;

    public static HomelessListRecyclerViewAdapter newInstance(HomelessListRecyclerViewListener listener, List<HomelessPerson> homelessList) {

        HomelessListRecyclerViewAdapter adapter = new HomelessListRecyclerViewAdapter();
        adapter.mListener = listener;
        adapter.homelessList = homelessList;

        return adapter;
    }

    @NonNull
    @Override
    public HomelessListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new HomelessListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomelessListViewHolder holder, int position) {
        holder.bindData(homelessList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.homeless_list_item_view;
    }

        @Override
    public int getItemCount() {
        return homelessList.size();
    }

    public class HomelessListViewHolder extends RecyclerView.ViewHolder {

        private final TextView homelessLoginTextView;
        private final Button homelessBanButton;
        private final Button homelessUnbanButton;
        private HomelessPerson data;

        /**
         * Constructor for the homeless list view holder. Calls to the super class constructor.
         * @param itemView a view of an item
         */
        public HomelessListViewHolder(View itemView) {
            super(itemView);
            homelessLoginTextView = itemView.findViewById(R.id.homeless_list_login_text_view);
            homelessBanButton = itemView.findViewById(R.id.homeless_list_ban_button);
            homelessUnbanButton = itemView.findViewById(R.id.homeless_list_unban_button);

            homelessBanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onHomelessBanClicked(data);
                }
            });

            homelessUnbanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onHomelessUnbanClicked(data);
                }
            });

        }

        /**
         * Set the text to be displayed in the list of the specific homeless that is being search for
         * @param homeless homeless that is being searched for
         */
        public void bindData(HomelessPerson homeless) {
            if (homeless.getUserLogin() != null) {
                data = homeless;

                if (homeless.getUserStatus().contains(HomelessPerson.UserStatus.Active)) {
                    homelessLoginTextView.setText(homeless.getUserLogin());
                    homelessLoginTextView.setTextColor(Color.GRAY);
                } else if (homeless.getUserStatus().contains(HomelessPerson.UserStatus.Blocked)) {
                    homelessLoginTextView.setText(homeless.getUserLogin() + " (banned)");
                    homelessLoginTextView.setTextColor(Color.RED);
                }

            } else {
                homelessLoginTextView.setText(R.string.not_applicable);
            }
        }
    }

    /**
     * A Homeless List Recycler View Listener for the above adapter. Tracks when a homeless is clicked
     * on.
     */
    public interface HomelessListRecyclerViewListener {
        void onHomelessBanClicked(HomelessPerson hp);
        void onHomelessUnbanClicked(HomelessPerson hp);
    }
    
}
