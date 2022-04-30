package lesson6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public static void main(String[] args) {
        final EchoClient client = new EchoClient();
        client.start();
    }

    private void start() {
        try {
            openConnection();
            final Scanner scanner = new Scanner(System.in);
            while (true) {
                final String msg = scanner.nextLine();
                out.writeUTF(msg);
                if ("/end".equalsIgnoreCase(msg)){
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();}

        }

    private void closeConnection() {
        if (socket != null){
            try{
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        if (in != null){
            try {
                in.close();
            } catch (IOException e){
                e.printStackTrace();
            }

        }
        if (out != null){
            try{
                out.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    private void openConnection() throws IOException {
        socket = new Socket("localhost", 8189);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        final String message = in.readUTF();
                        if ("/end".equalsIgnoreCase(message)){
                            break;
                        }
                        System.out.println("Сообщение от сервера " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }

        });
        thread.start();

    }

}
