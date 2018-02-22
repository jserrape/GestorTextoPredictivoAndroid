package clienteandroid.app.jcsp0003.com.predictorclieneandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Interfaz_escrituraActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText edittext;
    ListView lst;
    String[] conjuntos={"conj1","conj2","conj3","conj4","conj5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz_escritura);

        lst=findViewById(R.id.listvw);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,conjuntos);
        lst.setAdapter(arrayAdapter);
        lst.setOnItemClickListener(this);

        edittext = (EditText) findViewById(R.id.editText);
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Toast toast = Toast.makeText(getApplicationContext(), "Hello toast!", Toast.LENGTH_SHORT);
                //toast.show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast toast = Toast.makeText(getApplicationContext(), edittext.getText(), Toast.LENGTH_SHORT);
        toast.show();
        TextView tv=(TextView)view;
        String text=edittext.getText().toString()+tv.getText();
        edittext.setText(text);
    }
}
