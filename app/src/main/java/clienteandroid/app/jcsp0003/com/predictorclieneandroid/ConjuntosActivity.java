package clienteandroid.app.jcsp0003.com.predictorclieneandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConjuntosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView lst;

    String[] conjuntos={"conj1","conj2","conj3","conj4","conj5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conjuntos);

        lst=findViewById(R.id.listvw);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,conjuntos);
        lst.setAdapter(arrayAdapter);
        lst.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TextView tv=(TextView)view;
        //Toast.makeText(this,"You click on "+tv.getText()+i,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent (view.getContext(), Interfaz_escrituraActivity.class);
        startActivityForResult(intent, 0);
    }
}
