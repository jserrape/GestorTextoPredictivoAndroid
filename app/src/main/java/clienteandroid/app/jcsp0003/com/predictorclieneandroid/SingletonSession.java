package clienteandroid.app.jcsp0003.com.predictorclieneandroid;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by jcsp0003 on 23/02/2018.
 */

public class SingletonSession {

    private static SingletonSession instance;

    private Socket socket;
    private PrintStream output;

    private static final int SERVERPORT = 4444;
    private static final String ADDRESS = "192.168.0.104";

    //no outer class can initialize this class's object
    private SingletonSession() {

    }

    public static SingletonSession Instance(){
        if (instance == null)
        {
            instance = new SingletonSession();
        }
        return instance;
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintStream getOutput() {
        return output;
    }

    public void inicializar() {
        try {
            socket = new Socket(InetAddress.getByName(ADDRESS), SERVERPORT);
            output = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
