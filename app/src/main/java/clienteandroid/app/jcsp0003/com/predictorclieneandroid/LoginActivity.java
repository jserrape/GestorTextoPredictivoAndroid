package clienteandroid.app.jcsp0003.com.predictorclieneandroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private Button buttonLogin;
    private Button buttonRegistro;
    private EditText correoText;
    private EditText passwordText;
    private Context context = this;

    private static final int SERVERPORT = 4444;
    private static final String ADDRESS = "192.168.0.102";
    private static Socket socket;
    private static PrintStream output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = (Button) findViewById(R.id.button1);
        buttonRegistro = ((Button) findViewById(R.id.button2));

        correoText = ((EditText) findViewById(R.id.email));
        passwordText = ((EditText) findViewById(R.id.password));

        try {
            socket = new Socket(InetAddress.getByName(ADDRESS), SERVERPORT);
            output = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correoText.getText().toString().length()>0 && passwordText.getText().toString().length()>0){
                    Log.i("Enviando", "Enviado");
                    MyATaskCliente myATaskYW = new MyATaskCliente(v);
                    myATaskYW.execute("8"+correoText.getText().toString()+"#"+passwordText.getText().toString());
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


    class MyATaskCliente extends AsyncTask<String,Void,String>{

        View v;

        public MyATaskCliente(View vw) {
            v=vw;
        }

        @Override
        protected String doInBackground(String... values){
            try {
                String request = values[0];
                output.println(request);

                InputStream stream = socket.getInputStream();
                byte[] lenBytes = new byte[256];
                stream.read(lenBytes,0,256);
                String received = new String(lenBytes,"UTF-8").trim();

                return received;
            }catch (UnknownHostException ex) {
                return ex.getMessage();
            } catch (IOException ex) {
                Log.e("E/TCP Client", "" + ex.getMessage());
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
            }
        }
    }
}

