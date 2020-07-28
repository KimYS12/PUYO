package game_p;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import ddong.ClientNetWork;
import ddong.DDongData;
import ddong.DDongInter;
import jdbc_p.GameRoomDAO;
import jdbc_p.GameRoomDTO;
import jdbc_p.LobbyDAO;
import lobby_p.Lobby_Main;

public class WaitRoomFrame extends JFrame implements DDongInter, WindowListener {

   final int width = 806;
   final int height = 679 + 50;

   boolean meChk, youChk;

   public ClientNetWork cn;

   String imgSrc;

   JToggleButton ready;
   WaitRoomPanel meP, youP;
   JPanel state;
   JLabel meLb, youLb;

   public Integer roomNum;
   public String id;
   String enenmy;

   DDongData data;

   ExecutorService threadPool;

   public WaitRoomFrame(String id, Integer roomNum) {
      // TODO Auto-generated constructor stub

      init(id, roomNum);

      setSize(width, height); // 覗傾績 紫戚綜
      setLocationRelativeTo(null); // 覗傾績 獣拙獣 乞艦斗 掻肖拭 窒径
      setResizable(false); // 覗傾績 紫戚綜 繕箭 拝 呪 蒸製
      getContentPane().setLayout(null); // 傾戚2焼数
      setTitle("践軒践軒"); // 展戚堂
      setIconImage(new ImageIcon("./img/logo.png").getImage()); // 展戚堂郊 稽壱 竺舛
      getContentPane().setBackground(Color.white);
      this.imgSrc = "./img/background.png";

      meP = new WaitRoomPanel(imgSrc);
      meP.setBounds(0, 0, Puyo.PUYOSIZE * 6, Puyo.PUYOSIZE * 13);
      add(meP);

      this.meLb = new JLabel("ME");
      meLb.setBounds(0, Puyo.PUYOSIZE * 13, Puyo.PUYOSIZE * 6, Puyo.PUYOSIZE);
      meLb.setHorizontalAlignment(JLabel.CENTER);
      meLb.setText(id);
      add(meLb);

      state = new JPanel();
      state.setLayout(null);
      state.setBounds(Puyo.PUYOSIZE * 6, 0, Puyo.PUYOSIZE * 4, Puyo.PUYOSIZE * 13);
      state.setBackground(Color.white);
      add(state);

      ready = new JToggleButton("層搾");
      JButton exit = new JButton("蟹亜奄");

      // 獄動 娃維 50;
      // 獄動 恥 掩戚 130;
      // 害精 掩戚 520;
      // 520 - 130 / 2
      int btnSizeW = 100;
      int btnSizeH = 40;
      int x = (state.getSize().width - btnSizeW) / 2;
      // System.out.println(x);
      int gap = 50;
      int y = (state.getSize().height - (btnSizeH * 2)) / 2;
      // System.out.println(y);

      // ready.setBackground(null);
      ready.setBorderPainted(false);
      // ready.setBorder(null);

      exit.setBorderPainted(false);

      ready.setBounds(x, y, btnSizeW, btnSizeH);
      exit.setBounds(x, y + gap, btnSizeW, btnSizeH);

      ready.setEnabled(false);

      ready.addActionListener(new ReadyBtn());
      exit.addActionListener(new ExitBtn());

      state.add(ready);
      state.add(exit);

      youP = new WaitRoomPanel(imgSrc);
      youP.setBounds(Puyo.PUYOSIZE * 10, 0, Puyo.PUYOSIZE * 6, Puyo.PUYOSIZE * 13);
      youP.setBackground(Color.green);
      add(youP);

      this.youLb = new JLabel("陥献政煽税 羨紗企奄");
      youLb.setBounds(Puyo.PUYOSIZE * 10, Puyo.PUYOSIZE * 13, Puyo.PUYOSIZE * 6, Puyo.PUYOSIZE);
      youLb.setHorizontalAlignment(JLabel.CENTER);
      add(youLb);

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      // setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      setVisible(true);

      addWindowListener(this);

   }

   void init(String id, Integer roomNum) {
      this.id = id;
      this.roomNum = roomNum;
      this.meChk = false;
      this.threadPool = Executors.newCachedThreadPool();

      ddongDataInit();
   }

   void ddongDataInit() {
      this.data = new DDongData(); // 極 丞縦 実特
      data.type = "惟績";

   }

   @Override
   public void reciver(DDongData dd) {
      // TODO Auto-generated method stub

      System.out.println(this.enenmy);

      if (dd.type.equals("惟績")) {

         searchUser();

         if (this.enenmy != null && dd.src.equals(enenmy)) { // 旋戚 赤生檎辞 旋税 舛左研 閤聖凶
            this.youChk = dd.chk;

         }
         match();

      }

   }

   void searchUser() { // 搭重戚 尽聖凶 旋 達奄

      GameRoomDTO users = new GameRoomDAO().detailroom(WaitRoomFrame.this.roomNum); // 号腰硲稽 巨搾拭 羨悦

      String user1 = users.getUser1();
      String user2 = users.getUser2();

      String[] userArr = { user1, user2 };

      for (String user : userArr) {

         if (user != null) {
            if (!id.equals(user)) {
               this.enenmy = user;
               youLb.setText(enenmy);
               this.ready.setEnabled(true);
            }
         } else { // null 戚 廃腰 戚虞亀 赤陥檎
            this.enenmy = null;
            youLb.setText("陥献政煽税 羨紗企奄");
            this.ready.setEnabled(false);

         }

      }

   }

   void match() { // 層搾 獄動聖 喚袈聖凶 搭重聖 左鎧壱 搭重戚 尽聖凶 旋税 雌殿 溌昔

      if (this.meChk && youChk) {

         this.dispose();

         PuyoFrame game = new PuyoFrame(roomNum, id, enenmy);
         game.cn = cn;
         cn.ddInter = game;

         try {
            Thread.sleep(33);
         } catch (Exception e) {
            // TODO: handle exception
         }

         data.type = "惟績掻";
         data.dst = enenmy;
         cn.send(data);
         System.out.println(enenmy + "拭格耕陥 ささささささささ");
         System.out.println(data.dst + "dst陥 ddddddddddddd");

      }

   }

   void waitRoomDb() {

      // 巨搾拭辞 号拭辞 蟹娃 政煽 肢薦
      GameRoomDTO users = new GameRoomDAO().detailroom(WaitRoomFrame.this.roomNum);

      String user1 = users.getUser1();
      String user2 = users.getUser2();

      String[] userArr = { user1, user2 };

      for (int i = 0; i < userArr.length; i++) {

         if (id.equals(userArr[i])) {
            new GameRoomDAO().modifyUser5(new String[] { "user1", "user2" }[i], userArr[i]);
            break;
         }

      }
      // 号 巨搾 穣汽戚闘 魁

      // 号 鉢檎 曽戟
      WaitRoomFrame.this.dispose();
      WaitRoomFrame.this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

   }

   void signal() {
      DDongData data = new DDongData();
      data.type = "稽搾";
      data.data = null;
      cn.send(data);
   }

   class ReadyBtn implements ActionListener { // 層搾 獄動

      @Override
      public void actionPerformed(ActionEvent e) {
         // TODO Auto-generated method stub

         if (!WaitRoomFrame.this.meChk) {
            WaitRoomFrame.this.meChk = true;
            data.chk = true;
            cn.send(data);
         } else {
            WaitRoomFrame.this.meChk = false;
            data.chk = false;
            cn.send(data);
         }

      }

   }

   class ExitBtn implements ActionListener { // 蟹亜奄 獄動 send 研 背辞 獄動 醗失鉢人 搾醗失鉢研 蟹寛 操醤走

      @Override
      public void actionPerformed(ActionEvent e) {
         // TODO Auto-generated method stub
         new LobbyDAO().insert(id);
         waitRoomDb();
         new Lobby_Main(cn);
         signal();
      }

   }

   @Override
   public void windowOpened(WindowEvent e) {
      // TODO Auto-generated method stub

   }

   @Override
   public void windowClosing(WindowEvent e) { // 悪薦 曽戟獣
      // TODO Auto-generated method stub
      waitRoomDb();
      signal();
   }

   @Override
   public void windowClosed(WindowEvent e) {
      // TODO Auto-generated method stub

   }

   @Override
   public void windowIconified(WindowEvent e) {
      // TODO Auto-generated method stub

   }

   @Override
   public void windowDeiconified(WindowEvent e) {
      // TODO Auto-generated method stub

   }

   @Override
   public void windowActivated(WindowEvent e) {
      // TODO Auto-generated method stub

   }

   @Override
   public void windowDeactivated(WindowEvent e) {
      // TODO Auto-generated method stub

   }

}