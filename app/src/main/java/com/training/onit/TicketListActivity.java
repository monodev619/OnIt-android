package com.training.onit;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.training.onit.restapi.bean.Route;
import com.training.onit.restapi.bean.Ticket;
import com.training.onit.restapi.entity.RespMyTickets;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketListActivity extends BaseActivity {

    SwipeRefreshLayout  swipeRefreshLayout;
    RecyclerView        recyclerViewTicket;
    MyTicketAdapter     m_adapter;

    List<Ticket> m_tickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);

        recyclerViewTicket = (RecyclerView) findViewById(R.id.recyeclerViewTicket);
        recyclerViewTicket.setLayoutManager(new LinearLayoutManager(mContext));
        m_adapter = new MyTicketAdapter();
        recyclerViewTicket.setAdapter(m_adapter);

        int nItemMargin = getResources().getDimensionPixelSize(R.dimen.ticket_list_margin);
        recyclerViewTicket.addItemDecoration(new VerticalSpaceItemDecoration(nItemMargin));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMyTickets();
            }
        });

        loadMyTickets();
    }

    private void loadMyTickets() {

        TypedValue typed_value = new TypedValue();
        getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, typed_value, true);
        swipeRefreshLayout.setProgressViewOffset(false, 0, getResources().getDimensionPixelSize(typed_value.resourceId));
        swipeRefreshLayout.setRefreshing(true);

        Call<RespMyTickets> call = AppGlobal.apiService.getMyTickets(AppGlobal.authToken);
        call.enqueue(new Callback<RespMyTickets>() {
            @Override
            public void onResponse(Call<RespMyTickets> call, Response<RespMyTickets> response) {
                int statusCode = response.code();
                RespMyTickets myTickets = response.body();
                if ( myTickets != null && !myTickets.hasError ) {
                    m_tickets = myTickets.tickets;
                    for ( int i = 0; i < m_tickets.size(); i++){
                        Ticket ticket = m_tickets.get(i);
                        for ( int j = 0; j < AppGlobal.s_routes.length; j++){
                            Route route = AppGlobal.s_routes[j];
                            if ( ticket.route_name.equals(route.routeName) ) {
                                ticket.org_stop = route.startPos;
                                ticket.dst_stop = route.endPos;
                            }
                        }
                    }
                    m_adapter.notifyDataSetChanged();
                }
                else {
                    showError("Failed to get my tickets");
                }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RespMyTickets> call, Throwable t) {
                showError("Exception to get my tickets");

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMyTickets();
    }

    View.OnClickListener m_onClickItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int itemPosition = recyclerViewTicket.getChildLayoutPosition(v);
            Ticket ticket = m_tickets.get(itemPosition);

            Intent intent = new Intent(mContext, UseTicketActivity.class);
            intent.putExtra("ticket", ticket);
            startActivity(intent);
        }
    };

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }

    public class MyTicketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_OBJECT_VIEW) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_ticket_row, parent, false);
                itemView.setOnClickListener(m_onClickItemListener);
                return new MyTicketViewHolder(itemView);
            }
            else {
                TextView emptyView = new TextView(parent.getContext());
                emptyView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                emptyView.setText("Sorry, you have no ticket to use.");
                emptyView.setGravity(Gravity.CENTER);
                emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                return new EmptyViewHolder(emptyView);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            OwnTicket ticket = AppGlobal.s_ownTickets.get(position);
//            holder.tvRouteName.setText(ticket.route.routeName);
//            holder.tvStartPos.setText(ticket.route.startPos);
//            holder.tvEndPos.setText(ticket.route.endPos);
//            if ( ticket.nCount < 0 )
//                holder.tvTicketCount.setText("∞");
//            else
//                holder.tvTicketCount.setText(ticket.nCount + "");

            if ( holder instanceof MyTicketViewHolder ) {
                MyTicketViewHolder holder1 = (MyTicketViewHolder)holder;
                Ticket ticket = m_tickets.get(position);
                holder1.tvRouteName.setText(ticket.route_name);
                holder1.tvStartPos.setText(ticket.org_stop);
                holder1.tvEndPos.setText(ticket.dst_stop);
                if (ticket.isPeriod())
                    holder1.tvTicketCount.setText("∞");
                else
                    holder1.tvTicketCount.setText(ticket.quantity + "");
            }
        }

        private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
        private static final int VIEW_TYPE_OBJECT_VIEW = 1;
        @Override
        public int getItemCount() {
            return m_tickets == null || m_tickets.size() == 0 ? 1 : m_tickets.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (m_tickets == null || m_tickets.size() == 0) {
                return VIEW_TYPE_EMPTY_LIST_PLACEHOLDER;
            } else {
                return VIEW_TYPE_OBJECT_VIEW;
            }
        }

        public class EmptyViewHolder extends RecyclerView.ViewHolder {
            public EmptyViewHolder(View itemView) {
                super(itemView);
            }
        }

        public class MyTicketViewHolder extends  RecyclerView.ViewHolder {

            TextView tvRouteName;
            TextView tvStartPos;
            TextView tvEndPos;
            TextView tvTicketCount;

            public MyTicketViewHolder(View itemView) {
                super(itemView);

                tvRouteName = (TextView) itemView.findViewById(R.id.tvRouteName);
                tvStartPos = (TextView) itemView.findViewById(R.id.tvStartPos);
                tvEndPos = (TextView) itemView.findViewById(R.id.tvEndPos);
                tvTicketCount = (TextView) itemView.findViewById(R.id.tvTicketCount);
            }
        }
    }
}
