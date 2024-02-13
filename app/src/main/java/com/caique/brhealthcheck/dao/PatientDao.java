package com.caique.brhealthcheck.dao;


import androidx.room.Dao;

import androidx.room.Insert;
import androidx.room.Query;


import com.caique.brhealthcheck.FilterType;
import com.caique.brhealthcheck.model.Patient;

import java.util.List;

@Dao
public interface PatientDao {

    @Insert
    void insertPatient(Patient patient);


    @Query("DELETE FROM patients WHERE uid = :id")
    void deletePatient(int id);


    @Query("SELECT * FROM patients")
    List<Patient> getAllPatients();

    @Query("SELECT * FROM patients WHERE name = :name LIMIT 1")
    Patient getPatientByName(String name);

    @Query("SELECT * FROM patients WHERE status = :filtro")
    List<Patient> getPatientsByFilter(FilterType filtro);

    @Query("SELECT * FROM patients WHERE status = 'Internado'")
    List<Patient> getInternedPatients();

    @Query("SELECT * FROM patients WHERE status = 'Quarentena'")
    List<Patient> getQuarantinedPatients();

    @Query("SELECT * FROM patients WHERE status = 'Liberado'")
    List<Patient> getReleasedPatients();


}





