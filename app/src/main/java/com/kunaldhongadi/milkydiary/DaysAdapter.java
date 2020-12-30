package com.kunaldhongadi.milkydiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class DaysAdapter extends ExpandableRecyclerViewAdapter<MonthViewHolder, DaysViewHolder> {
    public DaysAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public MonthViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_list_heading,parent,false);
        return new MonthViewHolder(v);
    }

    @Override
    public DaysViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_list_view,parent,false);
        return new DaysViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(DaysViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Days days = (Days)group.getItems().get(childIndex);
        holder.bind(days);
    }

    @Override
    public void onBindGroupViewHolder(MonthViewHolder holder, int flatPosition, ExpandableGroup group) {
        final Month month = (Month)group;
        holder.bind(month);
    }
}
