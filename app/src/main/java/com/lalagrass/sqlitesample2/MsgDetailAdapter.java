package com.lalagrass.sqlitesample2;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 9/8/2015.
 */
public class MsgDetailAdapter extends RecyclerView.Adapter<MsgDetailAdapter.ViewHolder> {

    private List<MsgData> list = new ArrayList<>();

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tName;
        private final TextView tMsg;
        private final TextView tDate;

        public ViewHolder(View v) {
            super(v);
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

        public void setLeft(boolean left) {
            if (left) {
                LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) tName.getLayoutParams();
                if (p == null) {
                    p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    tName.setLayoutParams(p);
                }
                p.gravity = Gravity.LEFT;

                p = (LinearLayout.LayoutParams) tMsg.getLayoutParams();
                if (p == null) {
                    p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    tMsg.setLayoutParams(p);
                }
                p.gravity = Gravity.LEFT;

                p = (LinearLayout.LayoutParams) tDate.getLayoutParams();
                if (p == null) {
                    p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    tDate.setLayoutParams(p);
                }
                p.gravity = Gravity.LEFT;
            } else {

                LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) tName.getLayoutParams();
                if (p == null) {
                    p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    tName.setLayoutParams(p);
                }
                p.gravity = Gravity.RIGHT;

                p = (LinearLayout.LayoutParams) tMsg.getLayoutParams();
                if (p == null) {
                    p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    tMsg.setLayoutParams(p);
                }
                p.gravity = Gravity.RIGHT;

                p = (LinearLayout.LayoutParams) tDate.getLayoutParams();
                if (p == null) {
                    p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    tDate.setLayoutParams(p);
                }
                p.gravity = Gravity.RIGHT;
            }
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     */
    public MsgDetailAdapter() {
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
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        MsgData data = list.get(position);
        viewHolder.getTextMsg().setText(data.Msg);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(data.Date);
        viewHolder.getTextDate().setText(currentDateandTime);
        if (data.IsSender == 0) {
            viewHolder.getTextName().setText(R.string.string_i);
            viewHolder.setLeft(false);
        } else {
            viewHolder.getTextName().setText(data.Name);
            viewHolder.setLeft(true);
        }
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
}
