
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Login extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    JButton b_subit = new JButton("  登录  ");
    JButton b_reset = new JButton("  退出  ");
    JLabel blank1 = new JLabel("                                                                                                                                                                                                                                                                     ");
    JLabel blank2 = new JLabel("                                                                                                                                                                                ");
    JLabel blank3 = new JLabel("      ");
    JLabel blank4 = new JLabel("        ");
    TextField loginname = new TextField ("2022111378",24);
    JPasswordField password = new JPasswordField("0123456789",17);
    UserJFrame userjframe = new UserJFrame();
    private Font font1;
    private Font font2;
    static Login loginframe = new Login();
    public Login() {
        super("Lab1-大模型编程");

        JLabel label_1 = new JLabel("用户名:");
        JLabel label_2 = new JLabel("密 码:");

        b_subit.addActionListener(this);
        b_reset.addActionListener(this);

        font1 = new Font("SansSerif", Font.BOLD, 20);
        font2 = new Font("SansSerif", Font.BOLD, 18);

        // 设置主框架属性
        this.setSize(400, 200);
        this.setLocation(600, 300);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(3, 1, 10, 10));  // 垂直三行

        // 第一行：用户名
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        row1.add(label_1);
        row1.add(loginname);

        // 第二行：密码
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        row2.add(label_2);
        row2.add(password);

        // 第三行：按钮
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        row3.add(b_subit);
        row3.add(b_reset);

        // 添加各行到主框架
        this.add(row1);
        this.add(row2);
        this.add(row3);

        // 设置字体
        label_1.setFont(font1);
        label_2.setFont(font1);
        loginname.setFont(font1);
        password.setFont(font1);
        b_subit.setFont(font2);
        b_reset.setFont(font2);

        this.addWindowListener(new WinClose());
    }

    @Override
    public void actionPerformed(ActionEvent e) { //单击按钮时触发执行
        if(e.getSource() == b_subit) {
            String pwd = new String(password.getPassword());
            if(loginname.getText().equals("2022111378")&&pwd.equals("0123456789")) {
                JLabel jlabel = new JLabel("<html><body>登录成功！<br><br></body></html>");
                jlabel.setForeground(Color.red);
                jlabel.setFont(new Font("宋体",Font.BOLD,22));
                JOptionPane.showMessageDialog(this, jlabel,"提示",JOptionPane.WARNING_MESSAGE);
                userjframe.setVisible(true);
                loginframe.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this,"用户名或密码错误\n");
                System.out.println(loginname.getText().equals("2022111378"));
            }
        }
        if(e.getSource() == b_reset){
            System.exit(0);
        }
    }
    public void run() {
        loginframe.setVisible(true);                   //显示框架
    }
}

class WinClose implements WindowListener {
    public void windowClosing(WindowEvent e) {    //单击窗口关闭按钮时触发并执行,实现WindowListener接口中的方法
        System.exit(0);                          //结束程序运行
    }

    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
}
