package com.btl.hcj.myapplication.BottomSheet;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.btl.hcj.myapplication.R;
import com.btl.hcj.myapplication.data.Direction.Route;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteHolder> {

    private List<Route> mItems;
    private ItemListener mListener;

    public RouteAdapter(List items, ItemListener listener) {
        mItems = items;
        mListener = listener;
    }

    @Override
    public RouteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_bottom_sheet_item, parent, false);
        return new RouteHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteHolder holder, int position) {
        Route data = mItems.get(position);
        String s = data.getTotalDuration() + "";
        holder.nameView.setText(s);
        holder.route = data;
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class RouteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameView;
        TextView durationView;
        ImageView imageView;
        Route route;

        RouteHolder(View root) {
            super(root);
            nameView = root.findViewById(R.id.name);
            durationView = root.findViewById(R.id.duration);
            imageView = root.findViewById(R.id.image_icon);
            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(route);
            }
        }
    }
    public interface ItemListener {
        void onItemClick(Route route);
    }
}
