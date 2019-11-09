package socketio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Http 1.1 сервер на SocketServer
 *
 * @version 1.0
 * @autor Айрат Загидуллин
 */
public class Server {

    /**
     * Старт Http сервера
     */
    public void startServer() {
        File file = new File(".");
        File[] files = file.listFiles();
        try (ServerSocket serverSocket = new ServerSocket(8842)) {
            System.out.println("Server started!");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");

                try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                     BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                    if (input.readLine().contains("GET")) {
                        System.out.println("readline");
                        output.write("HTTP/1.1 200 OK");
                        output.write("Content-Type: text/html; charset=utf-8");
                        output.newLine();
                        for (File f : files) {
                            if (f.isDirectory()) {
                                output.newLine();
                                output.write("<p>directory - " + f.getName() + "</p>");
                            } else {
                                output.write("<p>file - " + f.getName() + "</p>");
                            }
                        }
                    } else {
                        output.write("HTTP/1.1 404 Not Found");
                        output.write("Content-Type: text/html; charset=utf-8");
                        output.newLine();
                        output.write("<p>Поступил неправильный запрос!</p>");
                    }
                    output.flush();
                    System.out.println("Client disconnected!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();
    }
}