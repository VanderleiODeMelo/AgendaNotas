package com.alura.nota.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.alura.nota.conversor.ConversorCalendar;
import com.alura.nota.dao.NotaDao;
import com.alura.nota.model.Nota;

@Database(entities = {Nota.class}, version = 2, exportSchema = false)
@TypeConverters(ConversorCalendar.class)
public abstract class NotaDataBase extends RoomDatabase {

    public static final String NOME_DO_BANCO_DE_DADOS = "nota.db";
    private static NotaDataBase getInstance;

    public abstract NotaDao notaDao();

    public static NotaDataBase getInstance(Context context) {

        if (getInstance == null) {

            getInstance = Room.databaseBuilder(context, NotaDataBase.class, NOME_DO_BANCO_DE_DADOS)
//                    .allowMainThreadQueries()
                    .addMigrations(new Migration(1,2) {
                        @Override
                        public void migrate(@NonNull SupportSQLiteDatabase database) {

                            database.execSQL("ALTER TABLE Nota ADD COLUMN `momentoCadastro` INTEGER");

                        }
                    })
                    .build();
        }


        return getInstance;

    }


}
