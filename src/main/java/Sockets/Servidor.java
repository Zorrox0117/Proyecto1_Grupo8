package Sockets;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



import BoardGeneration.ListGeneration;
import DoubleLinked.DoubleNode;
import DoubleLinked.DoublyLinkedList;
import GUI.VentanaPrincipal;

public class Servidor implements Runnable {

    ServerSocket servidor = null;
    Socket socket = null;

    DataOutputStream output;
    DataInputStream input;

    //PORT de nuestro servidor
    int PORT = 5000;
    public static String Nombrejugador1 = VentanaPrincipal.Nombre;
    public static String Nombrejugador2;


    public void run() {
        //Creamos el socket del servidor
        try {
            this.servidor = new ServerSocket(PORT);
            System.out.println("Servidor iniciado");

            //genero la lista para el tablero
            ListGeneration list = new ListGeneration();
            DoublyLinkedList board = list.random();
            DoubleNode node = new DoubleNode(null, "Reto", "+", 50, 50, 0, null);
            //Siempre estara escuchando peticiones
            while (true) {
                //Espero a que un cliente se conecte
                socket = servidor.accept();
                System.out.println("Cliente conectado");
                //Genero los canales de  entrada y salida
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());

                //Leo el mensaje que me envia
                Nombrejugador2 = input.readUTF();
                System.out.println(Nombrejugador2);

                //Le envio un mensaje
                output.writeUTF(Nombrejugador1);

                System.out.println();
                output.writeUTF(Json.generateString(Json.toJson(board), true));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
