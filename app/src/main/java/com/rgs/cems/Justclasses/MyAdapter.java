package com.rgs.cems.Justclasses;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rgs.cems.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lidt_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model ld = listData.get(position);
        final String Name = ld.getName();
        final String Date = ld.getDate();
        final String Block = ld.getBlock();
        final String Phase = ld.getPhase();
        final String Report = ld.getReport();
        final String Urg = ld.getUrg();
        final String Key = ld.getKey();


        holder.txtname.setText(Name);
        holder.txttime.setText(Date);
        if (Urg.equals("1")) {
            holder.relativeLayout.setBackgroundResource(R.drawable.shape_rounded_red);
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_view_report);
                dialog.setCancelable(true);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;

                final AppCompatButton bt_solved = (AppCompatButton) dialog.findViewById(R.id.bt_solved);
                TextView imptv = dialog.findViewById(R.id.viewrep_imptv);
                ImageView implogo = dialog.findViewById(R.id.viewrep_implogo);
                TextView name = dialog.findViewById(R.id.repview_name);
                TextView rep = dialog.findViewById(R.id.tvreport_virerep);
                TextView date = dialog.findViewById(R.id.tvtime_virerep);
                TextView block = dialog.findViewById(R.id.tvblock_virerep);
                TextView phase = dialog.findViewById(R.id.tvphase_virerep);
                name.setText(Name);
                rep.setText(Report);
                date.setText(Date);
                block.setText(Block);
                phase.setText(Phase);

                if (Urg.equals("1")) {
                    imptv.setText(" Urgent!");
                    implogo.setImageResource(R.drawable.ic_error_black_24dp);
                }

                bt_solved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(context, "Post Submitted", Toast.LENGTH_SHORT).show();
                    }
                });

                ((ImageButton) dialog.findViewById(R.id.bt_del)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Report");
                        databaseReference.child(Key).removeValue();
                        Toast.makeText(context, "Report will be deleted", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setAttributes(lp);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txttime, txtname, tv_urg;
        RelativeLayout relativeLayout;
        CardView cardView;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearlayout_events);
            txttime = (TextView) itemView.findViewById(R.id.time_rec);
            txtname = (TextView) itemView.findViewById(R.id.name_rec);
            tv_urg = (TextView) itemView.findViewById(R.id.urgent_rec);
            relativeLayout = itemView.findViewById(R.id.rec_revv);
            cardView = itemView.findViewById(R.id.card_view);


        }
    }
}