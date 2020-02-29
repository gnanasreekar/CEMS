package com.rgs.cems.Justclasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rgs.cems.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
private List<Model> listData;
Context context;

public void setlist(List<Model> listData) {
        this.listData = listData;
        }

    public MyAdapter(Context context) {
        this.context = context;
    }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lidt_data,parent,false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model ld=listData.get(position);
        final String Name = ld.getName();
        final String Date = ld.getDate();
        final String Block = ld.getBlock();
        final String Phase = ld.getPhase();
        final String Report = ld.getReport();
        final String Urg = ld.getUrg();

        holder.txtname.setText(Name);
    holder.txttime.setText(Date);
    if (Urg.equals("1")){
        holder.relativeLayout.setBackgroundResource(R.drawable.shape_rounded_red);
    }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
}

@Override
public int getItemCount() {
        return listData.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder{
    TextView txttime,txtname,tv_urg;
    RelativeLayout relativeLayout;
    CardView cardView;
    LinearLayout linearLayout;
    public ViewHolder(View itemView) {
        super(itemView);
        linearLayout = itemView.findViewById(R.id.linearlayout_events);
        txttime=(TextView)itemView.findViewById(R.id.time_rec);
        txtname=(TextView)itemView.findViewById(R.id.name_rec);
        tv_urg=(TextView)itemView.findViewById(R.id.urgent_rec);
        relativeLayout = itemView.findViewById(R.id.rec_revv);
        cardView = itemView.findViewById(R.id.card_view);


    }
}
}