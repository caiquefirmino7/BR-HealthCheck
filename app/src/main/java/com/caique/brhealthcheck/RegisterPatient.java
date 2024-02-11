package com.caique.brhealthcheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.caique.brhealthcheck.databinding.ActivityRegisterPatientBinding;

import java.util.Objects;

public class RegisterPatient extends AppCompatActivity {

    private ActivityRegisterPatientBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterPatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();
        binding.toolbarRegister.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPatient.this, MainActivity.class);
                startActivity(intent);
            }
        });

        binding.fabRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = binding.editName.getText().toString();
                String age = binding.editAge.getText().toString();
                String bodyTemp = binding.editBodyTemp.getText().toString();
                String coughDays = binding.editCoughDay.getText().toString();
                String headacheDays = binding.editheadacheDays.getText().toString();

                if (name.trim().isEmpty()) {
                    binding.editName.setError("Informe o nome!");
                    return;
                }

                if (age.trim().isEmpty()) {
                    binding.editAge.setError("Informe a idade!");
                    return;
                }

                if (bodyTemp.trim().isEmpty()) {
                    binding.editBodyTemp.setError("Informe a temperatura corporal!");
                    return;
                }

                if (coughDays.trim().isEmpty()) {
                    binding.editCoughDay.setError("Informe os dias de tosse!");
                    return;
                }

                if (headacheDays.trim().isEmpty()) {
                    binding.editheadacheDays.setError("Informe os dias de dor de cabeça!");
                    return;
                }



            }
        });
    }
}