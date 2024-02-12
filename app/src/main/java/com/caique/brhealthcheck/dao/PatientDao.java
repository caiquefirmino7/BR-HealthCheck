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


   @Update
   void updatePatient(Patient patient);


   @Delete
   void deletePatient(Patient patient);


   @Query("SELECT * FROM patients")
   List<Patient> getAllPatients();




}
