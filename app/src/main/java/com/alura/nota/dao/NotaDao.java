package com.alura.nota.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alura.nota.model.Nota;

import java.util.List;

@Dao
public interface NotaDao {


    @Insert
    void salvar(Nota nota);

    @Query("SELECT * FROM Nota")
    List<Nota> buscarTodasNotas();

    @Update
    void editar(Nota nota);

    @Delete
    void remover(Nota nota);
}
