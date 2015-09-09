package com.lalagrass.sqlitesample2;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MsgOverviewFragment extends Fragment implements AddMsgDialog.dialogCallbacks, MsgOverviewAdapter.iAdapterCallback {

    public interface iCallback {
        void onNameSelected(String name);
    }

    private iCallback callback;
    private Handler handler = new Handler();
    private Toolbar toolBar;
    private RecyclerView recyclerView;
    private MsgOverviewAdapter adapter;
    private int selected;

    public MsgOverviewFragment() {
    }

    public void setCallback(iCallback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleView);
        toolBar.inflateMenu(R.menu.menu_main);
        toolBar.setTitle(R.string.action_addmsg);
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_addmsg:
                        AddMsg();
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        adapter = new MsgOverviewAdapter();
        adapter.setCallback(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        registerForContextMenu(recyclerView);
        MsgDbHelper helper = new MsgDbHelper(getActivity());
        helper.InitDefaultDb();
        RefreshData();
        return rootView;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Log.i(MainActivity.TAG, "onCreateContextMenu");
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_context_overview, menu);
/*
        super.onCreateContextMenu(menu, v, menuInfo);
        menuInfo.
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        int position = info.position;
        Log.i(MainActivity.TAG, "onCreateContextMenu: " + position);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_context_overview, menu);
        */
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.i(MainActivity.TAG, "onContextItemSelected");
        switch (item.getItemId()) {
            case R.id.action_delete:
                Log.i(MainActivity.TAG, "context menu: delete ");
                DeleteData(adapter.getSelected());
                return true;
            case R.id.action_details:
                Log.i(MainActivity.TAG, "context menu: details ");
                onClick(adapter.getSelected());
                return true;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void AddMsg() {
        AddMsgDialog dialog = new AddMsgDialog();
        dialog.setCallbacks(this);
        dialog.show(getChildFragmentManager(), "NoticeDialogFragment");
    }

    private void RefreshData() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                MsgDbHelper helper = new MsgDbHelper(getActivity());
                List<MsgData> list = helper.listOverview();
                adapter.UpdateData(list);
            }
        });
    }

    private void RefreshDataAdd() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                MsgDbHelper helper = new MsgDbHelper(getActivity());
                List<MsgData> list = helper.listOverview();
                adapter.UpdateDataAdd(list);
            }
        });
    }

    private void RefreshDataDelete(final int position) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                MsgDbHelper helper = new MsgDbHelper(getActivity());
                List<MsgData> list = helper.listOverview();
                adapter.UpdateDataDelete(list, position);
            }
        });
    }

    private void DeleteData(int position) {
        MsgData data = adapter.getItem(position);
        if (data != null) {
            String name = data.Name;
            if (name != null) {
                MsgDbHelper helper = new MsgDbHelper(getActivity());
                helper.delete(name);
                List<MsgData> list = helper.listOverview();
                adapter.UpdateData(list);
            }
        }
        RefreshDataDelete(position);
    }

    @Override
    public void onDialogOk(MsgData data) {
        MsgDbHelper helper = new MsgDbHelper(getActivity());
        helper.insert(data);
        RefreshDataAdd();
    }

    @Override
    public void onClick(int position) {
        MsgData data = adapter.getItem(position);
        if (callback != null)
            callback.onNameSelected(data.Name);
    }
}
