package com.caique.brhealthcheck;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
    private final MutableLiveData<List<Patient>> patientsList = new MutableLiveData<>();
    private PatientAdapter patientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab.setOnClickListener(v -> {
            Intent navigateRegisterScreen = new Intent(MainActivity.this, RegisterPatient.class);
            startActivity(navigateRegisterScreen);
        });

        getPatients(FilterType.ALL);
        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPatients(FilterType.ALL); // Atualiza a lista de pacientes ao retomar a atividade
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu_all) {
            getPatients(FilterType.ALL);
            return true;
        } else if (item.getItemId() == R.id.action_menu_interned) {
            getPatients(FilterType.ADMITTED);
            return true;
        } else if (item.getItemId() == R.id.action_menu_quarantined) {
            getPatients(FilterType.QUARANTINE);
            return true;
        } else if (item.getItemId() == R.id.action_menu_released) {
            getPatients(FilterType.RELEASED);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }




    // Método para obter a lista de pacientes do banco de dados
    private void getPatients(FilterType filterType) {
        new Thread(() -> {
            patientDao = PatientDatabase.getInstance(MainActivity.this).patientDao();
            List<Patient> patients;

            switch (filterType) {
                case ADMITTED:
                    patients = patientDao.getInternedPatients();
                    break;
                case QUARANTINE:
                    patients = patientDao.getQuarantinedPatients();
                    break;
                case RELEASED:
                    patients = patientDao.getReleasedPatients();
                    break;
                default:
                    patients = patientDao.getAllPatients();
                    break;
            }

            patientsList.postValue(patients); // Notifica os observadores sobre a mudança na lista de pacientes
        }).start();
    }


    @SuppressLint("NotifyDataSetChanged")
    private void setupRecyclerView() {
        patientsList.observe(this, patients -> {
            binding.rvMain.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            binding.rvMain.setHasFixedSize(true);
            patientAdapter = new PatientAdapter(MainActivity.this, patients);
            binding.rvMain.setAdapter(patientAdapter);
            patientAdapter.notifyDataSetChanged();
        });
    }
}
