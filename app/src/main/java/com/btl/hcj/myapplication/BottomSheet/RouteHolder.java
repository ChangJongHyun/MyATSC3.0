package com.btl.hcj.myapplication.BottomSheet;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.btl.hcj.myapplication.R;

public class RouteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView infoView;
    RouteVO route;
    RouteAdapter.ItemListener listener;

    RouteHolder(View root, RouteAdapter.ItemListener listener) {
        super(root);
        infoView = root.findViewById(R.id.route_info);
        this.listener = listener;
        root.setOnClickListener(this);
    }

    void setData(RouteVO route) {
        this.route = route;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(route, this);
        }
    }
}
