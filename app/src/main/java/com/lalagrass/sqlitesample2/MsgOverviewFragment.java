package com.lalagrass.sqlitesample2;

import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
public class MsgOverviewFragment extends Fragment implements AddMsgDialog.dialogCallbacks, MsgOverviewAdapter.iAdapterCallback, AppBarLayout.OnOffsetChangedListener {

    public interface iCallback {
        void onNameSelected(String name);
    }

    private AppBarLayout appBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private iCallback callback;
    private Handler handler = new Handler();
    private Toolbar toolBar;
    private RecyclerView recyclerView;
    private MsgOverviewAdapter adapter;

    public MsgOverviewFragment() {
    }

    public void setCallback(iCallback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        appBar = (AppBarLayout) rootView.findViewById(R.id.app_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshData();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
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
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                DeleteData(adapter.getSelected());
                return true;
            case R.id.action_details:
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

    @Override
    public void onPause() {
        appBar.removeOnOffsetChangedListener(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        appBar.addOnOffsetChangedListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        swipeRefreshLayout.setEnabled(i == 0);
    }
}
