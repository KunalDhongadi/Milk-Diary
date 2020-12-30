package com.kunaldhongadi.milkydiary;

import android.os.Parcel;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Month extends ExpandableGroup<Days> {


    public Month(String title, List<Days> items) {
        super(title, items);
    }


}
