package com.caique.brhealthcheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
            }
        });

        getPatients();
        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPatients();
    }

    private void getPatients() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                patientDao = PatientDatabase.getInstance(MainActivity.this).patientDao();
                List<Patient> patients = patientDao.getAllPatients();
                patientsList.postValue(patients);
            }
        }).start();
    }

    private void setupRecyclerView() {
        patientsList.observe(this, new androidx.lifecycle.Observer<List<Patient>>() {
            @Override
            public void onChanged(List<Patient> patients) {
                binding.rvMain.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                binding.rvMain.setHasFixedSize(true);
                patientAdapter = new PatientAdapter(patients);
                binding.rvMain.setAdapter(patientAdapter);
                patientAdapter.notifyDataSetChanged();
            }
        });
    }
}
