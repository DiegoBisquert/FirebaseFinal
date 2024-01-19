package diego.bisquert.firebasefinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import diego.bisquert.firebasefinal.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference myPersona;
    private ArrayList<Persona> listaPersonas;
    private DatabaseReference myListPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listaPersonas = new ArrayList<>();
        crearPersonitas();

        database = FirebaseDatabase.getInstance("https://fir-b-71560-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("frase");
        myPersona = database.getReference("persona");
        myListPersonas = database.getReference("listapersonas");


        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.setValue(binding.txtFrase.getText().toString());

                int edad = (int)(Math.random()*100);
                Persona p = new Persona(binding.txtFrase.getText().toString(),edad);
                myPersona.setValue(p);

                listaPersonas.add(p);
                myListPersonas.setValue(listaPersonas);
                binding.txtFrase.setText("");
            }
        });

        myListPersonas.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    GenericTypeIndicator<ArrayList<Persona>> gti = new GenericTypeIndicator<ArrayList<Persona>>() {};
                    ArrayList<Persona> aux = task.getResult().getValue(gti);
                    if (aux != null){
                        listaPersonas.addAll(aux);
                    }
                }
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(String.class) != null) {
                    String texto = snapshot.getValue(String.class);
                    binding.lbFrase.setText(texto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        myPersona.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(Persona.class) != null) {
                    Persona p = snapshot.getValue(Persona.class);
                    binding.lbFrase.setText(p.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        myListPersonas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<Persona>> gti = new GenericTypeIndicator<ArrayList<Persona>>() {};
                if(snapshot.getValue(gti) != null) {
                    ArrayList<Persona> lista = snapshot.getValue(gti);
                    binding.lbFrase.setText("Elementos de la lista: " + String.valueOf(lista.size()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearPersonitas() {
    }
}