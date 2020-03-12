package com.rgs.cems.Justclasses;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rgs.cems.Dataretrive.Report;
import com.rgs.cems.R;

import java.util.Date;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Model> listData;
    Context context;
    SharedPreferences sharedPreferences;


    public void setlist(List<Model> listData) {
        this.listData = listData;
    }

    public MyAdapter(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("sp", 0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lidt_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final Model ld = listData.get(position);
        final String Name = ld.getName();
        final String Date = ld.getDate();
        final String Block = ld.getBlock();
        final String Phase = ld.getPhase();
        final String Report = ld.getReport();
        final String Urg = ld.getUrg();
        final String Key = ld.getKey();
        final String Status = ld.getStatus();
        final String Reopened = ld.getReopned();


        holder.txtname.setText(Name);
        holder.txttime.setText(Date);
        if (Urg.equals("1")) {
            holder.relativeLayout.setBackgroundResource(R.drawable.shape_rounded_red);
            holder.tv_urg.setText("Urgent");
        }

        if (Status.equals("0")){
            holder.relativeLayout.setBackgroundResource(R.drawable.shape_rounded_green);
            holder.tv_urg.setText("Solved");
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
                TextView solved = dialog.findViewById(R.id.solvedon_tv);
                name.setText(Name);
                rep.setText(Report);
                date.setText(Date);
                block.setText(Block);
                phase.setText(Phase);
                solved.setText(Reopened);

                if (Status.equals("0")){
                    bt_solved.setText("Reopen?");
                    imptv.setText("Solved!");
                    implogo.setImageResource(R.drawable.ic_check_black_24dp);
                }

                if (Urg.equals("1")) {
                    imptv.setText(" Urgent!");
                    implogo.setImageResource(R.drawable.ic_error_black_24dp);
                }

                bt_solved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Report/" + Key);
                        Date d = new Date();
                        final CharSequence s = DateFormat.format("MMMM d, yyyy HH:mm:ss", d.getTime());
                        if (Status.equals("1")){

                            databaseReference.child("Status").setValue(0);
                            databaseReference.child("Urg").setValue(0);
                            databaseReference.child("Date_solved").setValue("Solved on: " + s + "\nby " + sharedPreferences.getString("name", "Not aval"));
                        } else if (Status.equals("0")) {
                            databaseReference.child("Status").setValue(1);
                            databaseReference.child("Urg").setValue(1);
                            databaseReference.child("Date_solved").setValue("Reopened on: " + s + "\nby " + sharedPreferences.getString("name", "Not aval"));

                        }

                        if(context instanceof Report){
                            ((Report)context).getreports();
                        }

                        dialog.dismiss();
                        Toast.makeText(context, "Post Submitted", Toast.LENGTH_SHORT).show();
                    }
                });

                ((ImageButton) dialog.findViewById(R.id.bt_del)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Report");
                        databaseReference.child(Key).removeValue();

                        if(context instanceof Report){
                            ((Report)context).getreports();
                        }
                        Toast.makeText(context, "Report will be deleted", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                ((ImageButton) dialog.findViewById(R.id.bt_edit)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                        dialog.setContentView(R.layout.dialog_add_review);
                        dialog.setCancelable(true);

                        final String[] datedia = {"Not selected"};

                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        Date d = new Date();
                        final CharSequence s = DateFormat.format("MMMM d, yyyy HH:mm:ss", d.getTime());
                        final EditText et_report = dialog.findViewById(R.id.addrep_report);
                        final EditText et_block = dialog.findViewById(R.id.edit_block);
                        final EditText et_phase = dialog.findViewById(R.id.edit_phase);
                        final TextView tv_name = dialog.findViewById(R.id.addrep_name);
                        final TextView tv_time = dialog.findViewById(R.id.addrep_time);
                        final TextView tv_date = dialog.findViewById(R.id.tv_date);
                        final Switch imp = dialog.findViewById(R.id.report_imp);



                        tv_time.setText(s);
                        tv_name.setText(Name);
                        et_report.setText(Report);
                        et_block.setText(Block);
                        et_phase.setText(Phase);
                        tv_date.setText(Date);

                        tv_date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final android.icu.util.Calendar calendar = android.icu.util.Calendar.getInstance();
                                android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(context, R.style.datepicker, new android.app.DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        calendar.set(java.util.Calendar.YEAR, year);
                                        calendar.set(java.util.Calendar.MONTH, monthOfYear);
                                        calendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
                                        datedia[0] = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(calendar.getTime());
                                        tv_date.setText(datedia[0]);
                                    }
                                }, calendar.get(java.util.Calendar.YEAR), calendar.get(java.util.Calendar.MONTH), calendar.get(java.util.Calendar.DAY_OF_MONTH));
                                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                    }
                                });
                                datePickerDialog.show();
                            }
                        });

                        if (Urg.equals("1")) {
                            imp.setChecked(true);
                        }


                        (dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        (dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Report/" + Key);

                                databaseReference.child("Report").setValue(et_report.getText().toString());
                                databaseReference.child("Name").setValue(Name);
                                databaseReference.child("Date").setValue(s);
                                databaseReference.child("Block").setValue(et_block.getText().toString());
                                databaseReference.child("Phase").setValue(et_phase.getText().toString());
                                databaseReference.child("Dateh").setValue(datedia[0]);
                                if (imp.isChecked()) {
                                    databaseReference.child("Urg").setValue(1);
                                } else {
                                    databaseReference.child("Urg").setValue(0);

                                }

                                if(context instanceof Report){
                                    ((Report)context).getreports();
                                }
                                Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                            }
                        });

                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show();
                            }
                        });

                        dialog.show();
                        dialog.getWindow().setAttributes(lp);
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