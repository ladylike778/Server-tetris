import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class main {
    private static JButton sr=new JButton("server");
    private static JButton ct  =new JButton("client");
    private static int choose;
    public static void main(String [] args){
        Tetris_Frame tf=new Tetris_Frame();
        tf.setVisible(true);

    }

}
