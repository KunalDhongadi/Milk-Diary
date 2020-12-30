package com.kunaldhongadi.milkydiary;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class MonthViewHolder extends GroupViewHolder {
    private TextView textView;
    private ImageView dropdownArrow;

    public MonthViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.heading);
        dropdownArrow = itemView.findViewById(R.id.dropdown_arrow);
    }

    public void bind(Month month) {
        textView.setText(month.getTitle());
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation animation = new RotateAnimation(
                360, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(300);
        animation.setFillAfter(true);
        dropdownArrow.setAnimation(animation);
    }

    private void animateCollapse() {
        RotateAnimation animation = new RotateAnimation(
                180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(300);
        animation.setFillAfter(true);
        dropdownArrow.setAnimation(animation);
    }
}
