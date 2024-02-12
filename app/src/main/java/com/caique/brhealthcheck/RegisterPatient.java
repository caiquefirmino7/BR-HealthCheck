package com.caique.brhealthcheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.caique.brhealthcheck.dao.PatientDao;
import com.caique.brhealthcheck.databinding.ActivityRegisterPatientBinding;
import com.caique.brhealthcheck.db.PatientDatabase;
import com.caique.brhealthcheck.model.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterPatient extends AppCompatActivity {

    private ActivityRegisterPatientBinding binding;
    PatientDao patientDao;
    private List<Patient> listPatient = new ArrayList<>();
    ExecutorService executor = Executors.newFixedThreadPool(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterPatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();
        binding.toolbarRegister.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigateMainScreen = new Intent(RegisterPatient.this, MainActivity.class);
                startActivity(navigateMainScreen);
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

                register(name, age, bodyTemp, coughDays, headacheDays);

                Toast.makeText(getApplicationContext(), "Sucesso ao cadastrar usuário!", Toast.LENGTH_SHORT).show();
                finish();

            }

            private void register(String name, String age, String bodyTemp, String coughDays, String headacheDays){
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Criar uma nova instância de Patient com os dados fornecidos
                            Patient newPatient = new Patient(name, age, bodyTemp, coughDays, headacheDays);

                            // Obter a instância do PatientDatabase
                            PatientDatabase patientDatabase = PatientDatabase.getInstance(getApplicationContext());

                            // Obter o DAO do PatientDatabase
                            PatientDao patientDao = patientDatabase.patientDao();

                            // Inserir o paciente no banco de dados usando o DAO
                            patientDao.insertPatient(newPatient);

                            // Encerrar o ExecutorService após a conclusão das operações
                            executor.shutdown();
                        } catch (Exception e) {
                            // Tratar exceções, se ocorrerem durante a inserção do paciente
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }
}