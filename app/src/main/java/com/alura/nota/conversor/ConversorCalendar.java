package com.alura.nota.conversor;

import androidx.room.TypeConverter;

import java.util.Calendar;

public class ConversorCalendar {

    private long momentoCadastro;

    //converter para um tipo SQLITE aceita para gravar banco de dados
    @TypeConverter
    public Long paraLong(Calendar valor) {


        if (valor != null) {

            momentoCadastro = valor.getTimeInMillis();
        }
        return momentoCadastro;

    }

    @TypeConverter
    public Calendar paraCalendar(Long valor) {

        Calendar momentoCadastro = Calendar.getInstance();

        if (valor != null) {

            momentoCadastro.setTimeInMillis(valor);
        }
        return momentoCadastro;

    }
}
