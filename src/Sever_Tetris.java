import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
public class Sever_Tetris extends Thread {
    private InetAddress ipadrs;
    private ServerSocket servSocket;
    private Socket socket;
    private PrintStream outStream;
    private BufferedReader inputStream;
    private TetrisPane tp;
    public Sever_Tetris (TetrisPane tp1){
        tp=tp1;
        try{
            ipadrs = InetAddress.getLocalHost();
            servSocket = new ServerSocket(1723);
        }catch (UnknownHostException e){
            javax.swing.JOptionPane.showMessageDialog(null,"Error"+e.toString());
        }catch (IOException ioe){
            javax.swing.JOptionPane.showMessageDialog(null,"Error"+ioe.toString());
        }catch (Exception yee){
            javax.swing.JOptionPane.showMessageDialog(null,"Error"+yee.toString());
        }
    }
    public void run(){
        /*
         step 1的地方用來接訊息,要測試執行緒有沒有動作的話就把step1的地方註解掉
         然後把step2的地方解除註解
         */
        try {
            socket = servSocket.accept();// step 1
            outStream = new PrintStream(socket.getOutputStream());// tep 1
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));// step 1
            sendToclient("");// step 1
            String str="";
//            while (true){ //step2
//                System.out.println("hello it`s me");
//            }
            while (!(str=inputStream.readLine()).equals("")){// step 1
            }
        }catch (Exception e){
            javax.swing.JOptionPane.showMessageDialog(null,"Error"+e.toString());
        }
    }
    public void  sendToclient(String command){
        try {
            if(outStream != null){
                outStream.println(command);
            }else{

            }
        }catch (Exception e){
            javax.swing.JOptionPane.showMessageDialog(null,"Error"+e.toString());
        }
    }
}
