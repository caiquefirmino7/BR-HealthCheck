package com.caique.brhealthcheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    private final ExecutorService executor = Executors.newFixedThreadPool(1);

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
        binding.toolbarRegister.setNavigationOnClickListener(v -> {
            Intent navigateMainScreen = new Intent(RegisterPatient.this, MainActivity.class);
            startActivity(navigateMainScreen);
        });

        binding.fabRegister.setOnClickListener(v -> {
            String name = Objects.requireNonNull(binding.editName.getText()).toString();
            String age = Objects.requireNonNull(binding.editAge.getText()).toString();
            String bodyTemp = Objects.requireNonNull(binding.editBodyTemp.getText()).toString();
            String coughDays = Objects.requireNonNull(binding.editCoughDay.getText()).toString();
            String headacheDays = Objects.requireNonNull(binding.editheadacheDays.getText()).toString();
            String weeks = Objects.requireNonNull(binding.editWeeks.getText()).toString();

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

            if (weeks.isEmpty() && (checkItaly || checkIndonesia || checkPortugal || checkUsa || checkChina)) {
                Toast.makeText(getApplicationContext(), "Por favor, informe as semanas de visita !", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!checkItaly && !checkIndonesia && !checkPortugal && !checkUsa && !checkChina && !checkAnyone) {
                Toast.makeText(getApplicationContext(), "Selecione um CheckBox!", Toast.LENGTH_SHORT).show();
                return;
            }

            register(name, age, bodyTemp, coughDays, headacheDays, weeks);
        });

        binding.checkBoxItaly.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.checkBoxIndonesia.setChecked(false);
                binding.checkBoxPortugal.setChecked(false);
                binding.checkBoxUSA.setChecked(false);
                binding.checkBoxChina.setChecked(false);
                binding.checkboxNot.setChecked(false);
            }
        });

        binding.checkBoxIndonesia.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.checkBoxItaly.setChecked(false);
                binding.checkBoxPortugal.setChecked(false);
                binding.checkBoxUSA.setChecked(false);
                binding.checkBoxChina.setChecked(false);
                binding.checkboxNot.setChecked(false);
            }
        });

        binding.checkBoxPortugal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.checkBoxItaly.setChecked(false);
                binding.checkBoxIndonesia.setChecked(false);
                binding.checkBoxUSA.setChecked(false);
                binding.checkBoxChina.setChecked(false);
                binding.checkboxNot.setChecked(false);
            }
        });

        binding.checkBoxUSA.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.checkBoxItaly.setChecked(false);
                binding.checkBoxIndonesia.setChecked(false);
                binding.checkBoxPortugal.setChecked(false);
                binding.checkBoxChina.setChecked(false);
                binding.checkboxNot.setChecked(false);
            }
        });

        binding.checkBoxChina.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.checkBoxItaly.setChecked(false);
                binding.checkBoxIndonesia.setChecked(false);
                binding.checkBoxPortugal.setChecked(false);
                binding.checkBoxUSA.setChecked(false);
                binding.checkboxNot.setChecked(false);
            }
        });

        binding.checkboxNot.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.checkBoxItaly.setChecked(false);
                binding.checkBoxIndonesia.setChecked(false);
                binding.checkBoxPortugal.setChecked(false);
                binding.checkBoxUSA.setChecked(false);
                binding.checkBoxChina.setChecked(false);
            }
        });

        binding.checkboxNot.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Desabilita o campo de texto das semanas e limpar o texto
                binding.editWeeks.setEnabled(false);
                binding.editWeeks.setText("");

                binding.checkBoxItaly.setChecked(false);
                binding.checkBoxIndonesia.setChecked(false);
                binding.checkBoxPortugal.setChecked(false);
                binding.checkBoxUSA.setChecked(false);
                binding.checkBoxChina.setChecked(false);
            } else {
                // Habilita o campo de texto das semanas
                binding.editWeeks.setEnabled(true);
            }
        });

    }

//    Método register para realizar o registro do paciente no banco de dados.
//    É usado um ExecutorService para executar a operação de registro em uma thread separada, evitando bloqueios na UI.
    private void register(String name, String age, String bodyTemp, String coughDays,
                          String headacheDays, String weeks) {
        executor.submit(() -> {
            try {
                PatientDatabase patientDatabase = PatientDatabase.getInstance(getApplicationContext());
                patientDao = patientDatabase.patientDao();

                String nameInput = name.trim();
                if (nameInput.isEmpty()) {
                    return;
                }

                // Normaliza o nome removendo espaços extras antes e depois do nome
                String normalizedName = nameInput.trim().replaceAll("\\s{2,}", " ");

                Patient existingPatient = patientDao.getPatientByName(normalizedName);

                if (existingPatient != null) {
                    runOnUiThread(() -> Toast.makeText(RegisterPatient.this,
                            "Paciente já existente!", Toast.LENGTH_SHORT).show());
                    return;
                }

                String calculatedStatus = "Liberado";

                if ((checkItaly || checkChina || checkIndonesia || checkPortugal || checkUsa) &&
                        Float.parseFloat(bodyTemp) > 37 && Integer.parseInt(coughDays) > 5 &&
                        Integer.parseInt(headacheDays) > 5 && Integer.parseInt(weeks) <= 6) {
                    calculatedStatus = "Internado";
                } else if ((checkItaly || checkChina || checkIndonesia || checkPortugal || checkUsa) &&
                        ((((Integer.parseInt(age) > 60 || Integer.parseInt(age) < 10) &&
                                (Float.parseFloat(bodyTemp) > 37 || Integer.parseInt(headacheDays) > 3 ||
                                        Integer.parseInt(coughDays) > 5)) ||
                                (Integer.parseInt(age) >= 10 && Integer.parseInt(age) <= 60 &&
                                        Float.parseFloat(bodyTemp) > 37 && Integer.parseInt(headacheDays) > 5 &&
                                        Integer.parseInt(coughDays) > 5))) && Integer.parseInt(weeks) <= 6) {
                    calculatedStatus = "Quarentena";
                }

                Patient newPatient = new Patient(normalizedName, age, bodyTemp, coughDays, headacheDays,
                        calculatedStatus);

                patientDao.insertPatient(newPatient);

                runOnUiThread(() -> {
                    Toast.makeText(RegisterPatient.this, "Sucesso ao cadastrar usuário!",
                            Toast.LENGTH_SHORT).show();
                    // Inicia a atividade principal após o cadastro bem-sucedido
                    Intent intent = new Intent(RegisterPatient.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Finaliza a atividade de cadastro após iniciar a atividade principal
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

<<<<<<< HEAD
}
=======
}
>>>>>>> 4b6195605c1ea58b1de42bafefdf41aad9575789
