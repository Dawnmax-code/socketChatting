package chat.client;

import chat.bean.ClientBean;
import chat.util.MyUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Properties;

public class MyLogin extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;
    public static HashMap<String, ClientBean> onlines;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // 启动登陆界面
                    MyLogin frame = new MyLogin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MyLogin() {
        setTitle("登陆界面");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(550, 250, 450, 300);
        contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;

            //登录背景铺设
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon("images/登录背景.jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        //用户名输入框
        textField = new JTextField();
        textField.setBounds(160, 107, 150, 24);
        textField.setOpaque(false);//此方法是设置控件是否透明的。true表示不透明，false表示透明。文本区域设为透明
        contentPane.add(textField);
        textField.setColumns(10);

        //密码输入框
        passwordField = new JPasswordField();
        passwordField.setEchoChar('*');//设置显示字符
        passwordField.setOpaque(false);//文本区域为透明
        passwordField.setBounds(160, 144, 150, 24);
        contentPane.add(passwordField);

        //登录按钮
        final JButton btnNewButton = new JButton();
        btnNewButton.setText("登录");
        btnNewButton.setToolTipText("");
        btnNewButton.setBounds(120, 202, 72, 25);
        btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//设置鼠标在该区域显示状态变化
        getRootPane().setDefaultButton(btnNewButton);//设置回车响应
        contentPane.add(btnNewButton);

        //注册按钮
        final JButton btnNewButton_1 = new JButton();
        btnNewButton_1.setText("注册");
        btnNewButton_1.setBounds(240, 202, 72, 25);
        btnNewButton_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//设置鼠标在该区域显示状态变化
        contentPane.add(btnNewButton_1);

        // 提示信息
        final JLabel lblNewLabel = new JLabel();
        lblNewLabel.setBounds(168, 175, 151, 21);
        lblNewLabel.setForeground(Color.red);
        getContentPane().add(lblNewLabel);

        //用户名标签
        JLabel lblNewLabel_1 = new JLabel("用户名：");
        lblNewLabel_1.setBounds(110, 110, 72, 18);
        contentPane.add(lblNewLabel_1);

        //密码标签
        JLabel label = new JLabel("密码：");
        label.setBounds(120, 147, 72, 18);
        contentPane.add(label);

        //标题标签
        JLabel lblNChatroom = new JLabel("网络聊天");
        lblNChatroom.setFont(new java.awt.Font("宋体", java.awt.Font.BOLD, 25));
        lblNChatroom.setBounds(150, 31, 197, 45);
        contentPane.add(lblNChatroom);

        // 监听登陆按钮
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Properties userPro = new Properties();
                File file = new File("Users.properties");
                MyUtil.loadPro(userPro, file);
                String u_name = textField.getText();//获取用户输入的用户名
                if (file.length() != 0) {

                    if (userPro.containsKey(u_name)) {
                        String u_pwd = new String(passwordField.getPassword());//获取用户输入的密码
                        if (u_pwd.equals(userPro.getProperty(u_name))) {

                            try {
                                Socket client = new Socket("localhost", 8520);

                                btnNewButton.setEnabled(false);
                                MyChatroom frame = new MyChatroom(u_name, client);
                                frame.setVisible(true);// 显示聊天界面
                                setVisible(false);// 隐藏掉登陆界面

                            } catch (UnknownHostException e1) {
                                errorTip("出现异常，重新登陆试试");
                            } catch (IOException e1) {
                                errorTip("其他异常");
                            }

                        } else {
                            lblNewLabel.setText("您输入的密码有误！");
                            textField.setText("");
                            passwordField.setText("");
                            textField.requestFocus();
                        }
                    } else {
                        lblNewLabel.setText("您输入昵称不存在！");
                        textField.setText("");
                        passwordField.setText("");
                        textField.requestFocus();
                    }
                } else {
                    lblNewLabel.setText("您输入昵称不存在！");
                    textField.setText("");
                    passwordField.setText("");
                    textField.requestFocus();
                }
            }
        });

        //注册按钮监听
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnNewButton_1.setEnabled(false);
                MyRegister frame = new MyRegister();
                frame.setVisible(true);// 显示注册界面
                setVisible(false);// 隐藏掉登陆界面
            }
        });
    }

    protected void errorTip(String str) {
        //显示错误信息，清空信息栏
        JOptionPane.showMessageDialog(contentPane, str, "Error Message", JOptionPane.ERROR_MESSAGE);
        textField.setText("");
        passwordField.setText("");
        textField.requestFocus();
    }
}
