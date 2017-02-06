package com.training.onit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.training.onit.restapi.bean.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snowleopard on 9/11/2016.
 */
public class RouteAutoCompleteAdapter extends BaseAdapter implements Filterable {

    public class RouteViewHolder {
        TextView    tvRouteName;
        TextView    tvPrice;
        TextView    tvRouteInfo;
    }

    private static final int MAX_RESULTS = 10;
    private List<Route> resultList = new ArrayList<Route>();

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Route getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RouteViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_route_row, parent, false);

            holder = new RouteViewHolder();
            holder.tvRouteName = (TextView) convertView.findViewById(R.id.tvRouteName);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            holder.tvRouteInfo = (TextView) convertView.findViewById(R.id.tvRouteInfo);
            convertView.setTag(holder);
        }
        holder = (RouteViewHolder) convertView.getTag();
        Route route = getItem(position);
        holder.tvRouteName.setText(route.routeName);
        holder.tvPrice.setText(String.format("$%.2f/$%.2f", route.price, route.monthPrice));
        holder.tvRouteInfo.setText(route.startPos + " <-> " + route.endPos);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<Route> routes = findRoute(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = routes;
                    filterResults.count = routes.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<Route>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    private List<Route> findRoute(String keyword) {
        // GoogleBooksProtocol is a wrapper for the Google Books API
        keyword = keyword.toLowerCase();
        ArrayList<Route> ret = new ArrayList<Route>();
        for(int i = 0; i < AppGlobal.s_routes.length; i++) {
            Route route = AppGlobal.s_routes[i];
            if ( route.routeName.toLowerCase().indexOf(keyword) >= 0 ||
                    route.startPos.toLowerCase().indexOf(keyword) >= 0 ||
                    route.endPos.toLowerCase().indexOf(keyword) >=0 )
                ret.add(route);
        }
        return ret;
    }
}
