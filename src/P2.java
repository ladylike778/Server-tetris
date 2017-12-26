import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class P2 extends JPanel implements Runnable{
    public int map[][] = new int[10][20];
    /*  宣告方塊圖片  */
    private Image backimage1;
    private Image backimage2;
    private Image shadowBk;
    /*  宣告方塊數據*/
    private int blockPause;
    private int blockType;
    private int turnState;
    private int x,y,z;
    private int holdblock,nextblock,changedblock;
    /*  flag判斷方塊是否已放置*/
    private boolean flag = false;
    private int currentblock;
    /*  新增方塊圖片檔*/
    private Image [] color = new Image[8];
    private final int shapes[][][]=new int[][][]{
            // I
            { { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 },
                    { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 } },
            // s
            { { 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 } },
            // z
            { { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 } },
            // j
            { { 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
                    { 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
            // o
            { { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
            // l
            { { 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
            // t
            { { 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 }
            }
    };
    public P2(){
        rec();
        this.setLayout(null);
        this.setBackground(new Color(63, 61, 64));
        /*   指定圖片檔位置   */
        try{
            shadowBk = ImageIO.read(getClass().getResource("Tetris_image/shadow.png"));
            backimage1 = ImageIO.read(getClass().getResource("Tetris_image/bg1.png"));
            backimage2 = ImageIO.read(getClass().getResource("Tetris_image/bg2.png"));
            color[0]=ImageIO.read(getClass().getResource("Tetris_image/lightBlue3.png"));
            color[1]=ImageIO.read(getClass().getResource("Tetris_image/red.png"));
            color[2]=ImageIO.read(getClass().getResource("Tetris_image/green1.png"));
            color[3]=ImageIO.read(getClass().getResource("Tetris_image/blue1.png"));
            color[4]=ImageIO.read(getClass().getResource("Tetris_image/yellow.png"));
            color[5]=ImageIO.read(getClass().getResource("Tetris_image/orange1.png"));
            color[6]=ImageIO.read(getClass().getResource("Tetris_image/purple.png"));
            color[7]=ImageIO.read(getClass().getResource("Tetris_image/gray.png"));
        }catch (IOException io){
            io.printStackTrace();
        }
        /*  初始化背景陣列  */
        holdblock=-1;
        /*  宣告Timer  */
    }
    /*  背景陣列全部設成0*/
    private void initmap() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                map[i][j] = 0;
            }
        }
    }
    /*  swing用來畫圖的方法,awt中不用重寫方法,但是swing需要重寫*/
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                /*  奇數就用背景圖2,偶數就用背景圖1*/
                if (map[i][j] == 0) {
                    if ((i + j) % 2 == 0) {
                        /*  背景圖1,位置x:,位置y:*/
                        g.drawImage(backimage1, i*30+3*(i+1)+175, j*30+3*(j+1), null);
//                        g.drawImage(backimage1,  j*32+190, i*32, null);
//                        System.out.println(++x);
//                        System.out.println(i*30+3*(i+1)+150);
//                        System.out.println(j*30+3*(j+1));
                    } else {
                        g.drawImage(backimage2, i*30+3*(i+1)+175, j*30+3*(j+1), null);
//                        g.drawImage(backimage2,  j*32+190, i*32, null);
//                        System.out.println(++y);
                    }
                }else{
                    g.drawImage(color[map[i][j]-1],i*30+3*(i+1)+175,j*30+3*(j+1),null);
                }
            }
        }//for loop end
        if(!flag){
            for (int i=0;i<16;i++){
                if(shapes[blockType][turnState][i]==1){
                    g.drawImage(color[blockType], (i%4+x)*33+3+175, (i/4+y)*33+3, null);
                }
            }
        }
        if(holdblock>=0){
            for(int i=0;i<16;i++){
                if(shapes[holdblock][0][i]==1){
                    g.drawImage(color[holdblock],(i%4)*33+3+20, (i/4)*33+3+80, null);
                }
            }
        }
        for(int i=0;i<16;i++){
            if(shapes[nextblock][0][i]==1){
                g.drawImage(color[nextblock],(i%4)*33+3+550, (i/4)*33+80, null);
            }
        }
    }




    public void newBlock(int a){
        blockPause=0;
        flag=false;
        blockType =nextblock;
        nextblock=a;
        changedblock = 1;
        turnState=0;
        x=4;y=0;
        repaint();
    }
    public void down_Shift(){
        y++;
        repaint();


    }
    public int blow(int x ,int y, int blockType,int turnState){
        for(int i=0;i<16;i++){
            if(shapes[blockType][turnState][i]==1){
                if (x+i%4>=10||y+i/4>=20||x+i%4<0||y+i<0){
                    return 0;
                }
                if (map[x+i%4][y+i/4]!=0){
                    return 0;
                }
            }
        }
        return 1;
    }
    public void fall_down(){
        while (blow(x,y+1,blockType,turnState)==1){
            y++;
        }repaint();
        if (blow(x,y+1,blockType,turnState)==0){

            newBlock(nextblock);

        }
    }

    @Override
    public void run() {
        int i=0;
        while(i==0){
        rec();

        }


    }
    public void rec(){
        byte buff[]=new byte[1024];
        try {
            ServerSocket svs = new ServerSocket(2525);
            Socket s=svs.accept();
            InputStream in=s.getInputStream();
            int n=in.read(buff);
            String str=new String(buff,0,n);
            while(str!=null){
                switch (str){
                    case"@cmd-init":
                        initmap();
                        System.out.print("okk7\t" );
                        str=null;
                        System.out.print("sss");
                        break;
                    case"next0":
                        System.out.print("0\t");
                        newBlock(0);
                        str=null;System.out.print(str+"\t");
                        break;
                    case"next1":
                        System.out.print("1\t");
                        newBlock(1);
                        str=null;System.out.print(str+"\t");
                        break;
                    case"next2":
                        System.out.print("2\t");
                        newBlock(2);
                        str=null;System.out.print(str+"\t");
                        break;
                    case"next3":
                        System.out.print("3\t");
                        newBlock(3);
                        str=null;System.out.print(str+"\t");
                        break;
                    case"next4":
                        System.out.print("4\t");
                        newBlock(4);
                        str=null;System.out.print(str+"\t");
                        break;
                    case"next5":
                        System.out.print("5\t");
                        newBlock(5);
                        str=null;System.out.print(str+"\t");
                        break;
                    case"next6":
                        System.out.print("6\t");
                        newBlock(6);
                        str=null;
                        System.out.print(str+"\t");
                        break;
                    case"downshift();":
                        down_Shift();
                        str=null;
                        break;
                    case "y++":
                        y++;
                        repaint();
                        str=null;
                        break;
                }

            }
        }catch (Exception e){

        }
    }
}