package com.developers.sd.drawline;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class StatusFragment extends Fragment {

    private RecyclerView recyclerView;
    private LineAdapter lineAdapter;
    private List<Line> lineList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        lineAdapter = new LineAdapter(lineList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(lineAdapter);
        for (int i = 0; i<=20; i++) {
            Line line = new Line("Line "+i, 0);
            lineList.add(line);
        }
        lineAdapter.notifyDataSetChanged();

        return view;
    }

    public void prepareList() {
        for (int i = 0; i<=20; i++) {
            Line line = new Line("Line "+i, 0);
            lineList.add(line);
        }
        //lineAdapter.notifyDataSetChanged();
    }

    public class LineAdapter extends RecyclerView.Adapter<LineAdapter.MyViewHolder> {

        private List<Line> lineList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView title;
            public ToggleButton status;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                status = (ToggleButton) view.findViewById(R.id.status);
            }
        }


        public LineAdapter(List<Line> lineList) {
            this.lineList = lineList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Line line = lineList.get(position);
            holder.title.setText(line.getTitle());
            if (line.getStatus() == 0) {
                holder.status.setChecked(true);
            } else {
                holder.status.setChecked(false);
            }
            holder.status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        line.setStatus(0);
                    } else {
                        line.setStatus(1);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return lineList.size();
        }
    }

}
