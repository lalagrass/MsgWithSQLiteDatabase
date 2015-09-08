package com.lalagrass.sqlitesample2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MsgDetailFragment extends Fragment {

    public static MsgDetailFragment msgDetailFragment;
    private static String targetName;
    private Toolbar toolBar;
    private RecyclerView recyclerView;
    private MsgDetailAdapter adapter;

    public static MsgDetailFragment getFragment(String name) {
        targetName = name;
        if (msgDetailFragment == null)
            msgDetailFragment = new MsgDetailFragment();
        return msgDetailFragment;
    }

    public MsgDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleView);
        toolBar.setTitle(R.string.name_detail);
        toolBar.setNavigationIcon(R.drawable.ic_action_back);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        adapter = new MsgDetailAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        RefreshData();
        return rootView;
    }


    public void RefreshData() {
        MsgDbHelper helper = new MsgDbHelper(getActivity());
        List<MsgData> list = helper.listDetail(targetName);
        adapter.UpdateData(list);
    }
}
