package com.example.todolistapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etBuscar;

    private RecyclerView rvTareas;

    private FloatingActionButton fabNuevaTarea;

    private List<Tarea> listaTareas;

    private TareaAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etBuscar = findViewById(R.id.etBuscar);

        rvTareas = findViewById(R.id.rvTareas);

        fabNuevaTarea = findViewById(R.id.fabNuevaTarea);

        rvTareas.setLayoutManager(
                new LinearLayoutManager(this));

        cargarListaTareas("");

        fabNuevaTarea.setOnClickListener(view -> {

            Intent intent =
                    new Intent(MainActivity.this,
                            NuevaTareaActivity.class);

            startActivity(intent);

        });

        etBuscar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {

                cargarListaTareas(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        cargarListaTareas("");
    }

    private void cargarListaTareas(String textoBusqueda) {

        listaTareas = new ArrayList<>();

        AdminSQLiteOpenHelper admin =
                new AdminSQLiteOpenHelper(
                        this,
                        "tareas.db",
                        null,
                        1
                );

        SQLiteDatabase bd =
                admin.getReadableDatabase();

        Cursor fila;

        if (textoBusqueda.isEmpty()) {

            fila = bd.rawQuery(
                    "SELECT id, titulo, descripcion, estado FROM tareas",
                    null
            );

        } else {

            fila = bd.rawQuery(
                    "SELECT id, titulo, descripcion, estado " +
                            "FROM tareas " +
                            "WHERE titulo LIKE '%" + textoBusqueda + "%'",
                    null
            );

        }

        while (fila.moveToNext()) {

            listaTareas.add(
                    new Tarea(
                            fila.getInt(0),
                            fila.getString(1),
                            fila.getString(2),
                            fila.getString(3)
                    )
            );

        }

        fila.close();
        bd.close();

        adaptador =
                new TareaAdapter(listaTareas, this);

        rvTareas.setAdapter(adaptador);

    }

}