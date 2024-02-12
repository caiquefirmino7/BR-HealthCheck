package com.caique.brhealthcheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
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

                Boolean checkItaly = binding.checkBoxItaly.isChecked();
                Boolean checkIndonesia = binding.checkBoxIndonesia.isChecked();
                Boolean checkPortugal = binding.checkBoxPortugal.isChecked();
                Boolean checkUsa = binding.checkBoxUSA.isChecked();
                Boolean checkChina = binding.checkBoxChina.isChecked();
                Boolean checkAnyone = binding.checkboxNot.isChecked();

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

                if (!checkItaly && !checkIndonesia && !checkPortugal && !checkUsa && !checkChina && !checkAnyone) {
                    Toast.makeText(getApplicationContext(), "Selecione pelo menos um CheckBox!", Toast.LENGTH_SHORT).show();
                    return;
                }

                finish();
                register(name, age, bodyTemp, coughDays, headacheDays);


            }
        });

        binding.checkboxNot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.checkBoxItaly.setEnabled(!isChecked);
                binding.checkBoxIndonesia.setEnabled(!isChecked);
                binding.checkBoxPortugal.setEnabled(!isChecked);
                binding.checkBoxUSA.setEnabled(!isChecked);
                binding.checkBoxChina.setEnabled(!isChecked);

                if (isChecked) {
                    binding.checkBoxItaly.setChecked(false);
                    binding.checkBoxIndonesia.setChecked(false);
                    binding.checkBoxPortugal.setChecked(false);
                    binding.checkBoxUSA.setChecked(false);
                    binding.checkBoxChina.setChecked(false);
                }
            }
        });
    }

    private void register(String name, String age, String bodyTemp, String coughDays, String headacheDays) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    // Inicializar o PatientDao
                    PatientDatabase patientDatabase = PatientDatabase.getInstance(getApplicationContext());
                    patientDao = patientDatabase.patientDao();



                    // Verificar se o paciente já existe no banco de dados
                    Patient existingPatient = patientDao.getPatientByName(name);

                    if (existingPatient != null) {
                        // Se o paciente já existe, exibir uma mensagem e retornar
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterPatient.this, "Paciente já existente!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    // Criar uma nova instância de Patient com os dados fornecidos
                    Patient newPatient = new Patient(name, age, bodyTemp, coughDays, headacheDays);


                    // Inserir o paciente no banco de dados usando o DAO
                    patientDao.insertPatient(newPatient);


                    // Exibir a mensagem de sucesso apenas se o paciente não existir
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterPatient.this, "Sucesso ao cadastrar usuário!", Toast.LENGTH_SHORT).show();

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Encerrar o ExecutorService após a conclusão das operações
                    if (executor != null) {
                        executor.shutdown();
                    }
                }
            }
        });


    }



}
