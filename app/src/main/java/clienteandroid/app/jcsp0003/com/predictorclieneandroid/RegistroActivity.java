package clienteandroid.app.jcsp0003.com.predictorclieneandroid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class RegistroActivity extends AppCompatActivity {

    private static Socket socket;
    private static PrintStream output;
    private Context context = this;

    private EditText nombreText;
    private EditText apellidosText;
    private EditText correoText;
    private EditText repetirCorreoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombreText=((EditText) findViewById(R.id.nombreField));
        apellidosText=((EditText) findViewById(R.id.apellidosField));
        correoText=((EditText) findViewById(R.id.email));
        repetirCorreoText=((EditText) findViewById(R.id.emailRepetir));

        Button iniciarSesion = (Button) findViewById(R.id.button2);
        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button registrarse = (Button) findViewById(R.id.button1);
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nombreText.getText().toString().length()>0 && apellidosText.getText().toString().length()>0 && correoText.getText().toString().length()>0 && repetirCorreoText.getText().toString().length()>0){
                    if(correoText.getText().toString().equals(repetirCorreoText.getText().toString())){
                        String msn=9+nombreText.getText().toString()+"#"+apellidosText.getText().toString()+"#"+correoText.getText();
                        loginTask myATaskYW = new loginTask(v);
                        myATaskYW.execute(msn);
                    }else{
                        Toast.makeText(context, "Los correos no coinciden", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Rellena todos los campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    class loginTask extends AsyncTask<String,Void,String>{

        View v;

        public loginTask(View vw) {
            v=vw;
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
            Log.i(value, "");
            if(value.equals("-1")){
                Toast.makeText(context, "Ya existe un usuario con ese correo", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Regisro correcto, revise su correo", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
