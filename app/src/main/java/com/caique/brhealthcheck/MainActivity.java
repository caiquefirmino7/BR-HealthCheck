package com.caique.brhealthcheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.caique.brhealthcheck.adapter.PatientAdapter;
import com.caique.brhealthcheck.dao.PatientDao;
import com.caique.brhealthcheck.databinding.ActivityMainBinding;
import com.caique.brhealthcheck.db.PatientDatabase;
import com.caique.brhealthcheck.model.Patient;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PatientDao patientDao;
    private MutableLiveData<List<Patient>> patientsList = new MutableLiveData<>();
    private PatientAdapter patientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigateRegisterScreen = new Intent(MainActivity.this, RegisterPatient.class);
                startActivity(navigateRegisterScreen);

                getPatients(FilterType.TODOS);// Carrega a lista de pacientes ao criar a atividade
                setupRecyclerView(); // Configura o RecyclerView
            }
        });

        getPatients(FilterType.TODOS);
        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPatients(FilterType.TODOS); // Atualiza a lista de pacientes ao retomar a atividade
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu_all) {
            getPatients(FilterType.TODOS);
            return true;
        } else if (item.getItemId() == R.id.action_menu_interned) {
            getPatients(FilterType.INTERNADOS);
            return true;
        } else if (item.getItemId() == R.id.action_menu_quarantined) {
            getPatients(FilterType.QUARENTENA);
            return true;
        } else if (item.getItemId() == R.id.action_menu_released) {
            getPatients(FilterType.LIBERADOS);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }




    // Método para obter a lista de pacientes do banco de dados
    private void getPatients(FilterType filterType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                patientDao = PatientDatabase.getInstance(MainActivity.this).patientDao();
                List<Patient> patients;

                switch (filterType) {
                    case TODOS:
                        patients = patientDao.getAllPatients();
                        break;
                    case INTERNADOS:
                        patients = patientDao.getInternedPatients();
                        break;
                    case QUARENTENA:
                        patients = patientDao.getQuarantinedPatients();
                        break;
                    case LIBERADOS:
                        patients = patientDao.getReleasedPatients();
                        break;
                    default:
                        patients = patientDao.getAllPatients();
                        break;
                }

                patientsList.postValue(patients); // Notifica os observadores sobre a mudança na lista de pacientes
            }
        }).start();
    }


    private void insertPatient(Patient patient) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                patientDao.insertPatient(patient); // Insere o paciente no banco de dados
                getPatients(FilterType.TODOS); // Atualiza a lista de pacientes e notifica os observadores
            }
        }).start();
    }


    private void setupRecyclerView() {
        patientsList.observe(this, new androidx.lifecycle.Observer<List<Patient>>() {
            @Override
            public void onChanged(List<Patient> patients) {
                binding.rvMain.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                binding.rvMain.setHasFixedSize(true);
                patientAdapter = new PatientAdapter(MainActivity.this, patients);
                binding.rvMain.setAdapter(patientAdapter);
                patientAdapter.notifyDataSetChanged();
            }
        });
    }
}
