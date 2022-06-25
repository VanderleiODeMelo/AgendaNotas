package com.alura.nota.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtil {

    public static String formatadorData(Calendar momentoCadastro) {

        String formato = "dd/MM/yyyy";
        Date momentoCadastroDate = momentoCadastro.getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dataFormatada = new SimpleDateFormat(formato);
        return dataFormatada.format(momentoCadastroDate);

    }

}
