package com.example.todolistapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NuevaTareaActivity extends AppCompatActivity {

    private EditText etTitulo, etDescripcion;

    private CheckBox cbCompletada;

    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarea);

        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);

        cbCompletada = findViewById(R.id.cbCompletada);

        btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(view -> {

            registrarTarea();

        });

    }

    private void registrarTarea() {

        String titulo =
                etTitulo.getText().toString();

        String descripcion =
                etDescripcion.getText().toString();

        String estado;

        if (cbCompletada.isChecked()) {
            estado = "Completada";
        } else {
            estado = "Pendiente";
        }

        if (!titulo.isEmpty() &&
                !descripcion.isEmpty()) {

            AdminSQLiteOpenHelper admin =
                    new AdminSQLiteOpenHelper(
                            this,
                            "tareas.db",
                            null,
                            1
                    );

            SQLiteDatabase bd =
                    admin.getWritableDatabase();

            ContentValues registro =
                    new ContentValues();

            registro.put("titulo", titulo);
            registro.put("descripcion", descripcion);
            registro.put("estado", estado);

            bd.insert("tareas",
                    null,
                    registro);

            bd.close();

            Toast.makeText(this,
                    "Tarea guardada",
                    Toast.LENGTH_SHORT).show();

            finish();

        } else {

            Toast.makeText(this,
                    "Completa todos los campos",
                    Toast.LENGTH_SHORT).show();

        }

    }

}