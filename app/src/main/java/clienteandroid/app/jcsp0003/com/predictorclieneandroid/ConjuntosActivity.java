package clienteandroid.app.jcsp0003.com.predictorclieneandroid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

/**
 * Created by jcsp0003 on 23/02/2018.
 */
public class ConjuntosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView lst;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conjuntos);

        pedirListaHiloTask tareaLogin = new pedirListaHiloTask(this);
        tareaLogin.execute("1");
    }


    class pedirListaHiloTask extends AsyncTask<String,Void,String>{

        ConjuntosActivity conj;

        public pedirListaHiloTask(ConjuntosActivity conjuntosActivity) {
            conj=conjuntosActivity;
        }

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

        @Override
        protected void onPostExecute(String value){
            Log.i(value, "");
            value=value.substring(1,value.length()-1);
            value=value.replaceAll("#"," ");
            String[] parts = value.split(", ");

            lst=findViewById(R.id.listvw);
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(conj,android.R.layout.simple_list_item_1,parts);
            lst.setAdapter(arrayAdapter);
            lst.setOnItemClickListener(conj);

            //Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView tv=(TextView)view;

        String[] parts = tv.getText().toString().split(" ");
        String aux="";
        for(int z=0;z<parts.length-1;z++){
            aux+=parts[z];
        }

        //Toast.makeText(context, "-"+aux+"-", Toast.LENGTH_SHORT).show();
        cargarConjuntoTask tareaLogin = new cargarConjuntoTask();
        tareaLogin.execute("5"+aux);
        Intent intent = new Intent (view.getContext(), Interfaz_escrituraActivity.class);
        startActivityForResult(intent, 0);
        finish();
    }

    class cargarConjuntoTask extends AsyncTask<String,Void,String>{

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

        @Override
        protected void onPostExecute(String value){

        }
    }
}
