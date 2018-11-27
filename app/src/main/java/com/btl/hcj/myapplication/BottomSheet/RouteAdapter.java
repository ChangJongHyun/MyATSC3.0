package com.btl.hcj.myapplication.BottomSheet;

import android.annotation.SuppressLint;
import android.provider.FontsContract;
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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RouteAdapter extends RecyclerView.Adapter<RouteHolder> {

    private List<RouteVO> mItems;
    private ItemListener mListener;
    public static List<RouteHolder> mRouteHolder = new LinkedList<>();

    public RouteAdapter(List items, ItemListener listener) {
        mItems = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public RouteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_bottom_sheet_item, parent, false);
        return new RouteHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteHolder holder, int position) {

        RouteVO data = mItems.get(position);

        StringBuilder sb = new StringBuilder();
        sb.append(data.getStringDestiation());
        sb.append("  ");
        sb.append(data.getDistance());
        sb.append("  ");
        sb.append(data.getStringDuration());
        sb.append(" 소요");

        holder.infoView.setText(sb.toString());
        holder.setData(data);
        mRouteHolder.add(holder);
    }

    @Override
    public void onViewRecycled(@NonNull RouteHolder holder) {
        super.onViewRecycled(holder);
        mRouteHolder.remove(holder);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface ItemListener {
        void onItemClick(RouteVO route, RouteHolder holder);
    }
}
