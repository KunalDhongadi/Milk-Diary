package com.kunaldhongadi.milkydiary;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class DaysViewHolder extends ChildViewHolder {

    private TextView dateTextView, dayTextView;
    private ImageView imageView;

    public DaysViewHolder(View itemView) {
        super(itemView);
        this.dateTextView = itemView.findViewById(R.id.list_date_view);
        this.dayTextView = itemView.findViewById(R.id.list_day_view);
        this.imageView = itemView.findViewById(R.id.status_view);
    }

    public void bind(Days days) {
        dateTextView.setText(days.date);
        dayTextView.setText(days.day);
        imageView.setImageResource(days.imageId);
    }
}
