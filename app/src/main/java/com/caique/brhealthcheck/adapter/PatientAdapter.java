package com.caique.brhealthcheck.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caique.brhealthcheck.R;
import com.caique.brhealthcheck.dao.PatientDao;
import com.caique.brhealthcheck.databinding.PatientItemBinding;
import com.caique.brhealthcheck.db.PatientDatabase;
import com.caique.brhealthcheck.model.Patient;

import java.text.MessageFormat;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {
    private final List<Patient> patientList;
    private final Context context;

    public PatientAdapter(Context context, List<Patient> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PatientItemBinding binding = PatientItemBinding.inflate(inflater, parent, false);
        return new PatientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patientList.get(position);
        holder.binding.textViewName.setText(patient.getName());
        holder.binding.textViewAge.setText(MessageFormat.format("Idade: {0}", patient.getAge()));
        holder.binding.textViewBodyTemperature.setText(MessageFormat.format("Temperatura Corporal: {0}", patient.getBodyTemperature()));
        holder.binding.textViewCoughDays.setText(MessageFormat.format("Períodos com tosse: {0}", patient.getCoughPeriodDays()));
        holder.binding.textViewHeadacheDays.setText(MessageFormat.format("Períodos com dor de cabeça: {0}", patient.getHeadachePeriodDays()));
        holder.binding.textViewStatus.setText(MessageFormat.format("Status: {0}", patient.getStatus()));
        holder.binding.btDelete.setImageResource(R.drawable.icon_delete);

        holder.binding.btDelete.setOnClickListener(v -> {

            int position1 = holder.getAdapterPosition();
            if (position1 != RecyclerView.NO_POSITION) {
                deletePatient(position1);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void deletePatient(int position) {

        Patient patient = patientList.get(position);
        PatientDao patientDao = PatientDatabase.getInstance(context).patientDao();

        new Thread(() -> {
            try {
                patientDao.deletePatient(patient.getUid());
                ((Activity) context).runOnUiThread(() -> {
                    patientList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Cadastro deletado com sucesso!", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {
        private final PatientItemBinding binding;

        public PatientViewHolder(@NonNull PatientItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
