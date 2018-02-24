package clienteandroid.app.jcsp0003.com.predictorclieneandroid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

public class Interfaz_escrituraActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText edittext;
    ListView lst;
    private Context context = this;

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

            @Override
            public void afterTextChanged(Editable s) {
                prediccionTask tareaPrediccion = new prediccionTask(aux);
                int n=edittext.getText().toString().length()-1;
                char aux= edittext.getText().toString().charAt(n);

                String[] parts = edittext.getText().toString().split(" ");
                int cont=0;
                String auxT="";
                for(int i=parts.length-1;i>=0;i--){
                    if(cont < 5){
                        auxT=parts[i]+" "+auxT;
                    }
                    ++cont;
                }
                Toast toast = Toast.makeText(getApplicationContext(),auxT, Toast.LENGTH_SHORT);
                toast.show();
                if(' ' == aux){
                    tareaPrediccion.execute("71"+auxT);
                }else{
                    tareaPrediccion.execute("70"+auxT);
                }
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

    class prediccionTask extends AsyncTask<String,Void,String> {

        Interfaz_escrituraActivity interf;

        public prediccionTask(Interfaz_escrituraActivity interf) {
            this.interf=interf;
        }

        @Override
        protected String doInBackground(String... values){
            try {
                String request = values[0];
                SingletonSession.Instance().getOutput().println(request);

                InputStream stream = SingletonSession.Instance().getSocket().getInputStream();
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

        @Override
        protected void onPostExecute(String value){
            if(value.length()>2) {
                value = value.substring(1, value.length() - 1);
                String[] parts = value.split(", ");

                lst = findViewById(R.id.listvw);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(interf, android.R.layout.simple_list_item_1, parts);
                lst.setAdapter(arrayAdapter);
                lst.setOnItemClickListener(interf);

                Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
