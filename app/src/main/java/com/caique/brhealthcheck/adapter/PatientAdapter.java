package com.caique.brhealthcheck.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caique.brhealthcheck.R;
import com.caique.brhealthcheck.databinding.PatientItemBinding;
import com.caique.brhealthcheck.model.Patient;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {
    private final List<Patient> patientList;

    public PatientAdapter(List<Patient> patientList) {
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PatientItemBinding binding = PatientItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PatientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patientList.get(position);
        holder.binding.textViewName.setText(patient.getName());
        holder.binding.textViewAge.setText("Idade: " + patient.getAge());
        holder.binding.textViewBodyTemperature.setText("Temperatura Corporal: " + patient.getBodyTemperature());
        holder.binding.textViewCoughDays.setText("Períodos com tosse: " + patient.getCoughPeriodDays());
        holder.binding.textViewHeadacheDays.setText("Períodos com dor de cabeça: " + patient.getHeadachePeriodDays());
        holder.binding.btEdit.setImageResource(R.drawable.icon_edit);
        holder.binding.btDelete.setImageResource(R.drawable.icon_delete);
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

        public void bind(Patient patient) {

            binding.textViewName.setText(patient.getName());
            binding.textViewAge.setText(patient.getAge());
            binding.textViewBodyTemperature.setText( patient.getBodyTemperature());
            binding.textViewHeadacheDays.setText(patient.getHeadachePeriodDays());
            binding.textViewCoughDays.setText(patient.getCoughPeriodDays());
            binding.btEdit.setImageResource(R.drawable.icon_edit);
            binding.btDelete.setImageResource(R.drawable.icon_delete);

        }
    }
}
