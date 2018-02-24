package clienteandroid.app.jcsp0003.com.predictorclieneandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

/**
 * Created by jcsp0003 on 23/02/2018.
 */
public class Interfaz_escrituraActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText edittext;
    ListView lst;
    private Context context = this;

    private Button btnShowPMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz_escritura);


        final Interfaz_escrituraActivity aux=this;

        edittext = (EditText) findViewById(R.id.editText);
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            /**
             * Solicito una prediccion en funcion de si la semilla está complea o no
             */
            @Override
            public void afterTextChanged(Editable s) {
                prediccionTask tareaPrediccion = new prediccionTask(aux);
                int n=edittext.getText().toString().length()-1;
                if(n>0) {
                    char aux = edittext.getText().toString().charAt(n);

                    String[] parts = edittext.getText().toString().split(" ");
                    int cont = 0;
                    String auxT = "";
                    for (int i = parts.length - 1; i >= 0; i--) {
                        if (cont < 5) {
                            auxT = parts[i] + " " + auxT;
                        }
                        ++cont;
                    }
                    if (' ' == aux) {
                        tareaPrediccion.execute("71" + auxT);
                    } else {
                        tareaPrediccion.execute("70" + auxT);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Selecciono un item de la lista y lo añado al texto principal
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView tv=(TextView)view;
        String text=edittext.getText().toString()+tv.getText();
        edittext.setText(text);
        edittext.setSelection(edittext.length());
    }

    /**
     * Tarea que realiza y recibe la predicción y crea la lista
     */
    class prediccionTask extends AsyncTask<String,Void,String> {

        Interfaz_escrituraActivity interf;

        public prediccionTask(Interfaz_escrituraActivity interf) {
            this.interf=interf;
        }

        /**
         * Función que solicita la prediccion
         */
        @Override
        protected String doInBackground(String... values){
            try {
                String request = values[0];
                SingletonSocket.Instance().getOutput().println(request);

                InputStream stream = SingletonSocket.Instance().getSocket().getInputStream();
                byte[] lenBytes = new byte[256];
                stream.read(lenBytes,0,256);
                String received = new String(lenBytes,"UTF-8").trim();

                return received;
            }catch (UnknownHostException ex) {
                return ex.getMessage();
            } catch (IOException ex) {
                return ex.getMessage();
            }
        }

        /**
         * Función que rellena la lista con las predicciones
         */
        @Override
        protected void onPostExecute(String value){
            if(value.length()>2) {
                value = value.substring(1, value.length() - 1);
                value=value.replaceAll(","," ,")+" ";
                String[] parts = value.split(", ");

                lst = findViewById(R.id.listvw);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(interf, android.R.layout.simple_list_item_1, parts);
                lst.setAdapter(arrayAdapter);
                lst.setOnItemClickListener(interf);
            }
        }
    }
}
