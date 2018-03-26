package clienteandroid.app.jcsp0003.com.predictorclieneandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by jcsp0003 on 22/02/2018.
 */
public class LoginActivity extends AppCompatActivity {

    private Button buttonLogin;
    private Button buttonRegistro;
    private EditText correoText;
    private EditText passwordText;
    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Runnable connect = new connectSocket();
        new Thread(connect).start();

        buttonLogin = (Button) findViewById(R.id.button1);
        buttonRegistro = ((Button) findViewById(R.id.button2));

        correoText = ((EditText) findViewById(R.id.email));
        passwordText = ((EditText) findViewById(R.id.password));


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correoText.getText().toString().length()>0 && passwordText.getText().toString().length()>0){
                    loginTask tareaLogin = new loginTask(v);
                    String aux;
                    MessageDigest md = null;
                    try {
                        md = MessageDigest.getInstance("SHA-1");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    byte[] textBytes = new byte[0];
                    try {
                        textBytes = passwordText.getText().toString().getBytes("iso-8859-1");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    md.update(textBytes, 0, textBytes.length);
                    byte[] sha1hash = md.digest();
                    StringBuilder buf = new StringBuilder();
                    for (byte b : sha1hash) {
                        int halfbyte = (b >>> 4) & 0x0F;
                        int two_halfs = 0;
                        do {
                            buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                            halfbyte = b & 0x0F;
                        } while (two_halfs++ < 1);
                    }
                    tareaLogin.execute("8"+correoText.getText().toString()+"#"+buf.toString());
                }else{
                    Toast.makeText(context, "Escriba correo y contraseña ", Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), RegistroActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    class connectSocket implements Runnable {
        @Override
        public void run() {
            try {
                SingletonSocket.Instance().inicializar();
            } catch (Exception e) {
                Log.e("TCP", "C: Error", e);
            }
        }
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
            if(value.equals("-1")){
                Toast.makeText(context, "Correo o contraseña no válidos", Toast.LENGTH_LONG).show();
            }else{
                Intent intent = new Intent (v.getContext(), ConjuntosActivity.class);
                startActivityForResult(intent, 0);
                finish();
            }
        }
    }
}

