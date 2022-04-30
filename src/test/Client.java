package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8189;

    public static void main(String[] args) throws IOException {

        Socket socket = null;
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());


            Thread threadReader = new Thread(() -> {
                try {
                    while (true) {
                        outputStream.writeUTF(scanner.nextLine());
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            threadReader.setDaemon(true);
            threadReader.start();

            while (true) {
                String str = inputStream.readUTF();
                if (str.equals("/end")) {
                    outputStream.writeUTF("/end");
                    break;
                } else {
                    System.out.println("Сервер: " + str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                socket.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
