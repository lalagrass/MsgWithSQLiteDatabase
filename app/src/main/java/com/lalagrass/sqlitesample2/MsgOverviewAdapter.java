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

    private iAdapterCallback callback;
    private List<MsgData> list = new ArrayList<>();
    private int selected;

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
                    if (callback != null)
                        callback.onClick(getAdapterPosition());
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setSelected(getAdapterPosition());
                    v.showContextMenu();
                    return true;
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

    public synchronized int getSelected() {
        int ret = selected;
        return ret;
    }

    private synchronized void setSelected(int p) {
        this.selected = p;
    }

    public MsgOverviewAdapter() {
    }

    public void setCallback(iAdapterCallback callback0) {
        this.callback = callback0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_msgoverview_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        MsgData data = list.get(position);
        viewHolder.getTextName().setText(data.Name);
        viewHolder.getTextMsg().setText(data.Msg);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(data.Date);
        viewHolder.getTextDate().setText(currentDateandTime);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void UpdateData(List<MsgData> l) {
        list.clear();
        list.addAll(l);
        notifyDataSetChanged();
    }

    public void UpdateDataAdd(List<MsgData> l) {
        list.clear();
        list.addAll(l);
        notifyItemInserted(0);
    }

    public void UpdateDataDelete(List<MsgData> l, int position) {
        list.clear();
        list.addAll(l);
        notifyItemRemoved(position);
    }

    public MsgData getItem(int position) {
        MsgData ret = null;
        if (position >= 0 && position < list.size()) {
            ret = list.get(position);
        }
        return ret;
    }
}
