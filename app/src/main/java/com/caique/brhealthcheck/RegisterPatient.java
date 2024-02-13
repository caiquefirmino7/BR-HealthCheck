package com.caique.brhealthcheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.caique.brhealthcheck.dao.PatientDao;
import com.caique.brhealthcheck.databinding.ActivityRegisterPatientBinding;
import com.caique.brhealthcheck.db.PatientDatabase;
import com.caique.brhealthcheck.model.Patient;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterPatient extends AppCompatActivity {

    private ActivityRegisterPatientBinding binding;
    private PatientDao patientDao;
    private ExecutorService executor = Executors.newFixedThreadPool(1);

    private Boolean checkItaly;
    private Boolean checkIndonesia;
    private Boolean checkPortugal;
    private Boolean checkUsa;
    private Boolean checkChina;
    private Boolean checkAnyone;

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

                checkItaly = binding.checkBoxItaly.isChecked();
                checkIndonesia = binding.checkBoxIndonesia.isChecked();
                checkPortugal = binding.checkBoxPortugal.isChecked();
                checkUsa = binding.checkBoxUSA.isChecked();
                checkChina = binding.checkBoxChina.isChecked();
                checkAnyone = binding.checkboxNot.isChecked();

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
                    PatientDatabase patientDatabase = PatientDatabase.getInstance(getApplicationContext());
                    patientDao = patientDatabase.patientDao();

                    Patient existingPatient = patientDao.getPatientByName(name);

                    if (existingPatient != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterPatient.this, "Paciente já existente!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    String calculatedStatus = "Liberado";

                    if ((checkItaly || checkChina || checkIndonesia || checkPortugal || checkUsa) &&
                            Float.parseFloat(bodyTemp) > 37 && Integer.parseInt(coughDays) > 5 && Integer.parseInt(headacheDays) > 5) {
                        calculatedStatus = "Internado";
                    }
                    else if ((checkItaly || checkChina || checkIndonesia || checkPortugal || checkUsa) &&
                            (((Integer.parseInt(age) > 60 || Integer.parseInt(age) < 10) &&
                                    (Float.parseFloat(bodyTemp) > 37 || Integer.parseInt(headacheDays) > 3 || Integer.parseInt(coughDays) > 5)) ||
                                    (Integer.parseInt(age) >= 10 && Integer.parseInt(age) <= 60 &&
                                            Float.parseFloat(bodyTemp) > 37 && Integer.parseInt(headacheDays) > 5 && Integer.parseInt(coughDays) > 5))) {
                        calculatedStatus = "Quarentena";
                    }

                    Patient newPatient = new Patient(name, age, bodyTemp, coughDays, headacheDays, calculatedStatus);

                    patientDao.insertPatient(newPatient);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterPatient.this, "Sucesso ao cadastrar usuário!", Toast.LENGTH_SHORT).show();
                            // Iniciar a atividade principal após o cadastro bem-sucedido
                            Intent intent = new Intent(RegisterPatient.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Finalizar a atividade de cadastro após iniciar a atividade principal
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
