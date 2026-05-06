package com.example.todolistapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditarTareaActivity extends AppCompatActivity {

    private EditText etTitulo, etDescripcion;

    private CheckBox cbCompletada;

    private Button btnEditar, btnEliminar;

    private int idTarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tarea);

        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);

        cbCompletada = findViewById(R.id.cbCompletada);

        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

        idTarea =
                getIntent().getIntExtra("id", 0);

        cargarDatos();

        btnEditar.setOnClickListener(view -> {

            editarTarea();

        });

        btnEliminar.setOnClickListener(view -> {

            eliminarTarea();

        });

    }

    private void cargarDatos() {

        AdminSQLiteOpenHelper admin =
                new AdminSQLiteOpenHelper(
                        this,
                        "tareas.db",
                        null,
                        1
                );

        SQLiteDatabase bd =
                admin.getReadableDatabase();

        Cursor fila = bd.rawQuery(
                "SELECT titulo, descripcion, estado " +
                        "FROM tareas WHERE id=" + idTarea,
                null
        );

        if (fila.moveToFirst()) {

            etTitulo.setText(fila.getString(0));
            etDescripcion.setText(fila.getString(1));

            cbCompletada.setChecked(
                    fila.getString(2)
                            .equals("Completada")
            );

        }

        fila.close();
        bd.close();

    }

    private void editarTarea() {

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

        bd.update(
                "tareas",
                registro,
                "id=" + idTarea,
                null
        );

        bd.close();

        Toast.makeText(this,
                "Tarea actualizada",
                Toast.LENGTH_SHORT).show();

        finish();

    }

    private void eliminarTarea() {

        AdminSQLiteOpenHelper admin =
                new AdminSQLiteOpenHelper(
                        this,
                        "tareas.db",
                        null,
                        1
                );

        SQLiteDatabase bd =
                admin.getWritableDatabase();

        bd.delete(
                "tareas",
                "id=" + idTarea,
                null
        );

        bd.close();

        Toast.makeText(this,
                "Tarea eliminada",
                Toast.LENGTH_SHORT).show();

        finish();

    }

}