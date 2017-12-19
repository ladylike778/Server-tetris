import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class Tetris_Frame extends JFrame {
    Container cp;
    int FrameW=1800,FrameH=900;
    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    public Tetris_Frame (){
        init();
    }
    private void init (){
        this.setTitle("Tetris Battle_v0.001");
        this.setBounds(dim.width/2-FrameW/2,dim.height/2-FrameH/2,FrameW,FrameH);
//        this.setBounds(0,0,FrameW,FrameH);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        cp=this.getContentPane();
        cp.setLayout(null);
        cp.setBackground(new Color(25, 85, 15));
        TetrisPane tp = new TetrisPane();/* TetrisPane是用來畫遊戲畫面的,包括方塊and動作   */
        P2 tp2 = new P2();
        tp.setBounds(100,80,700,700);
        tp2.setBounds(1000,80,700,700);
        tp.setPreferredSize(new Dimension(700,700));
        cp.add(tp);addKeyListener(tp);
        cp.add(tp2);
        Thread thread1 = new Thread(tp);
        thread1.start();
        Thread  thr2=new Thread(tp2);

        thr2.start();


    }
}
class TetrisPane extends JPanel implements KeyListener ,Runnable{
    /*  宣告背景陣列  */
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
    public TetrisPane() {
        this.setLayout(null);
//        this.setBackground(Color.BLACK);
        this.setBackground(new Color(63, 61, 64));
        /*   指定圖片檔位置   */
        try{
            shadowBk =ImageIO.read(getClass().getResource("Tetris_image/shadow.png"));
            backimage1 = ImageIO.read(getClass().getResource("Tetris_image/bg1.png"));
            backimage2 = ImageIO.read(getClass().getResource("Tetris_image/bg2.png"));
//            color[0]=ImageIO.read(getClass().getResource("Tetris_image/shadow.png"));
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
        /*  不明原因,toolkit的圖片讀取方式不能用*/
//        backimage1=Toolkit.getDefaultToolkit().getImage("Tetris_image/bg1.png");
//        backimage2=Toolkit.getDefaultToolkit().getImage("Tetris_image/bg2.png");
        /*  初始化背景陣列  */
        initmap();
        newBlock();
        holdblock=-1;
        nextblock=(int)(Math.random()*7);
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
                    g.drawImage(shadowBk,(i%4+x)*33+3+175,(i/4+fall_downShadow())*33+3,null);
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
//        /*  測試是否能正常顯示掉落方塊*/
//        g.drawImage(color[0],(0%4+x)*33+3+175,(0/4+y)*33+3,null);
//        g.drawImage(color[0],(1%4+x)*33+3+175,(1/4+y)*33+3,null);
//        g.drawImage(color[0],(2%4+x)*33+3+175,(2/4+y)*33+3,null);
//        g.drawImage(color[0],(3%4+x)*33+3+175,(3/4+y)*33+3,null);
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
    public void setBlock(int x,int y,int blockType,int turnState){
        flag=true;
        for (int i=0;i<16;i++){
            if(shapes[blockType][turnState][i]==1){
                map[x+i%4][y+i/4]=blockType+1;
            }
        }
    }
    public void newBlock(){
        blockPause=0;
        flag=false;
        blockType = nextblock;
        changedblock = 1;
        nextblock = (int)(Math.random()*7);
        turnState=0;
        x=4;y=0;
        if(gameOver(x,y)==1){
            initmap();
        }
        repaint();
    }
    public int down_Shift(){
        int down=0;
        if(blow(x,y+1,blockType,turnState)==1){
            y++;
//            System.out.println(y);
            down=1;
        }repaint();
        if (blow(x,y+1,blockType,turnState)==0&&blockPause<1){
            blockPause++;
        }else if(blow(x,y+1,blockType,turnState)==0&&blockPause>0){
            setBlock(x,y,blockType,turnState);
            newBlock();
            deLine();
            down=0;
        }
        return down;
    }
    public void fall_down(){
        while (blow(x,y+1,blockType,turnState)==1){
            y++;
        }repaint();
        if (blow(x,y+1,blockType,turnState)==0){
            setBlock(x,y,blockType,turnState);
            newBlock();
            deLine();
        }
    }
    public int fall_downShadow(){
        z=y;
        while (blow(x,z+1,blockType,turnState)==1){
            z++;
        }repaint();
        if (blow(x,z+1,blockType,turnState)==0){
        }return z;
    }
    public int r_Shift(){
        int right=0;
        if (blow(x+1,y,blockType,turnState)==1){
            x++;
//            System.out.println(x);
            right=1;
        }repaint();
        return right;
    }
    public void l_Shift(){
        if (blow(x-1,y,blockType,turnState)==1){
            x--;
//            System.out.println(x);
        }repaint();
    }

    public int gameOver(int x,int y){
        if(blow(x,y,blockType,turnState)==0){
            return 1;
        }return 0;
    }
    public void roTate(){
        int tmpState = turnState;
        tmpState = (turnState+1)%4;
        if(blow(x,y,blockType,tmpState)==1){
            turnState=tmpState;
        }else{
            if(x==8&&blockType==0){
                x--;
                x--;
                turnState=tmpState;
            }else if(x>5){
                --x;
                turnState=tmpState;
            } else if(x<3){
                x++;
                turnState=tmpState;
            }else if(x>6){
                x-=2;
                turnState=tmpState;
            }else if(y==17&&blockType==0){
                y-=1;
                turnState=tmpState;
            }else if(y==18&&blockType==0){
                y-=2;
                turnState=tmpState;
            }else if(y==19&&blockType==0){
                y-=2;
                turnState=tmpState;
            }
        }
        repaint();
    }
    void deLine(){
        int row=19,access1=0;
        for(int i=19;i>=0;i--){
            int count=0;
            for(int j=0;j<10;j++){
                if(map[j][i]!=0){
                    count++;
                }
            }
            if(count==10){
                access1=1;
                for(int j=0;j<10;j++){
                    map[j][i]=0;
                }
            }
            else{
                for(int j = 0;j<10;j++){
                    map[j][row]=map[j][i];
                }
                row--;
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_DOWN:
                down_Shift();
                break;
            case KeyEvent.VK_LEFT:
                l_Shift();
                break;
            case KeyEvent.VK_RIGHT:
                r_Shift();
                break;
            case KeyEvent.VK_UP:
                roTate();
                break;
            case KeyEvent.VK_SHIFT:
                if(holdblock>=0&&changedblock==1){
                    int temp;
                    temp=holdblock;
                    holdblock=blockType;
                    blockType=temp;
                    x=4;y=0;
                    changedblock=0;
                }else if(changedblock==1){
                    holdblock=blockType;
                    newBlock();
                }
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_SPACE:
                fall_down();
                break;
        }
    }

    @Override
    public void run() {
        Timer t1 = new Timer(800, new TimerListener());
        t1.start();
    }

    class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            down_Shift();
            repaint();

        }
    }
    void Sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.out.println("Unexcepted interrupt");
            System.exit(0);
        }
    }



}
