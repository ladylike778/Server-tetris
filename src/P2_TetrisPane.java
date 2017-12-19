//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.InetAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//public class P2_TetrisPane extends JPanel implements Runnable{
//    public P2_TetrisPane(){
//        byte buff[]=new byte[1024];
//        try {
//            ServerSocket svs = new ServerSocket(2525);
//            Socket s=svs.accept();
//            InputStream in=s.getInputStream();
//            int n=in.read(buff);
//            String str=new String(buff,0,n);
//            s.close();
//            in.close();
//            while(str!=null){
//                switch (str){
//                    case"@cmd-init":
//                        initmap();
//                        b
//                }
//
//            }
//
//
//        }catch (Exception e){
//
//        }
//
//    }
//    @Override
//    public void run() {
//
//
//    }
//
//
//}