package com.caique.brhealthcheck.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

}





