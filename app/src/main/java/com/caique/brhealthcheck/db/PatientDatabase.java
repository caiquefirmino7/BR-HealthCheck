package com.caique.brhealthcheck.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.caique.brhealthcheck.dao.PatientDao;
import com.caique.brhealthcheck.model.Patient;

@Database(entities = {Patient.class}, version = 1, exportSchema = false)
public  abstract class PatientDatabase extends RoomDatabase {

    // Método abstrato que retorna o DAO (Data Access Object) para interagir com o banco de dados.
    public abstract PatientDao patientDao();


//    essa variável instance é comumente usada para armazenar a instância do banco
//    de dados que será compartilhada em tod o o aplicativo. O uso de
//    volatile garante que, quando a instância do banco de dados é
//atualizada em uma thread, outras threads possam ver a atualização
//    corretamente e não usem um valor desatualizado.
    private static volatile PatientDatabase instance;


//    Esse método getInstance(Context context) é um padrão de projeto
//    chamado "Singleton" que garante que apenas uma instância da classe
//    PatientDatabase seja criada durante a execução do programa.
    public static PatientDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (PatientDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            PatientDatabase.class,
                            "DB_PATIENTS"
                    ).build();
                }
            }
        }
        return instance;
    }





}
