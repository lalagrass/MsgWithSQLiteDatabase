package com.lalagrass.sqlitesample2;

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
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        MsgDbHelper helper = new MsgDbHelper(getActivity());
        helper.InitDefaultDb();
        RefreshData();
        return rootView;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        /*
        Log.i(MainActivity.TAG, "onCreateContextMenu: ");
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        int position = info.position;
        Log.i(MainActivity.TAG, "onCreateContextMenu: " + position);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_context_overview, menu);
        */
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        /*
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        switch (item.getItemId()) {
            case R.id.action_delete:
                Log.i(MainActivity.TAG, "context menu: delete " + index);
                return true;
            default:
                break;
        }
        */
        return super.onContextItemSelected(item);
    }

    private void AddMsg() {
        AddMsgDialog dialog = new AddMsgDialog();
        dialog.setCallbacks(this);
        dialog.show(getChildFragmentManager(), "NoticeDialogFragment");
    }

    private void RefreshData() {
        MsgDbHelper helper = new MsgDbHelper(getActivity());
        List<MsgData> list = helper.listOverview();
        adapter.UpdateData(list);
    }

    private void DeleteData(String name) {
        MsgDbHelper helper = new MsgDbHelper(getActivity());
        helper.delete(name);
        List<MsgData> list = helper.listOverview();
        adapter.UpdateData(list);
    }

    @Override
    public void onDialogOk(MsgData data) {
        MsgDbHelper helper = new MsgDbHelper(getActivity());
        helper.insert(data);
        RefreshData();
    }

    @Override
    public void onClick(int position) {
        MsgData data = adapter.getItem(position);
        if (callback != null)
            callback.onNameSelected(data.Name);
    }
}
