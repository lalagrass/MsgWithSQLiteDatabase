package com.lalagrass.sqlitesample2;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 9/8/2015.
 */
public class MsgOverviewAdapter extends RecyclerView.Adapter<MsgOverviewAdapter.ViewHolder> {

    public interface iAdapterCallback {
        void onClick(int position);
    }
    private List<MsgData> list = new ArrayList<>();
    private static iAdapterCallback callback;

    public void setCallback(iAdapterCallback callback0) {
        callback = callback0;
    }

    private void onClickItem(int position) {
        if (callback != null)
            callback.onClick(position);
    }
    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tName;
        private final TextView tMsg;
        private final TextView tDate;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(MainActivity.TAG, "Element " + getAdapterPosition() + " clicked.");
                    onClickItem(getAdapterPosition());
                }
            });
            tName = (TextView) v.findViewById(R.id.tName);
            tMsg = (TextView) v.findViewById(R.id.tMsg);
            tDate = (TextView) v.findViewById(R.id.tDate);
        }

        public TextView getTextName() {
            return tName;
        }

        public TextView getTextMsg() {
            return tMsg;
        }

        public TextView getTextDate() {
            return tDate;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     */
    public MsgOverviewAdapter() {
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_msgoverview_item, viewGroup, false);
        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        //Log.d(MainActivity.TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        MsgData data = list.get(position);
        viewHolder.getTextName().setText(data.Name);
        viewHolder.getTextMsg().setText(data.Msg);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(data.Date);
        viewHolder.getTextDate().setText(currentDateandTime);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void UpdateData(List<MsgData> l) {
        list.clear();
        list.addAll(l);
        notifyDataSetChanged();
    }

    public MsgData getItem(int position) {
        return list.get(position);
    }
}
