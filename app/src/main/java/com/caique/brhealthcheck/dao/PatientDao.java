package com.caique.brhealthcheck.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.caique.brhealthcheck.model.Patient;

@Dao
public interface PatientDao {

   @Insert
   void insertPatient(Patient patient);
}
