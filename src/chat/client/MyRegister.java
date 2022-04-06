package chat.client;

import chat.util.MyUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class MyRegister extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;
    private JPasswordField passwordField_1;
    private JLabel lblNewLabel;
    private JLabel label;
    private JLabel label_1;
    private JLabel label_2;
    private JLabel lblNewLabel_1;

    public MyRegister() {
        setTitle("注册界面");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(550, 250, 450, 300);
        contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //加载背景图片
                g.drawImage(new ImageIcon("images\\注册背景.jpg").getImage(), 0,0, getWidth(), getHeight(), null);
            }
        };
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        //用户名输入框
        textField = new JTextField();
        textField.setBounds(160, 76, 150, 21);
        textField.setOpaque(false);//文本区域设为透明
        contentPane.add(textField);
        textField.setColumns(10);

        //密码输入框
        passwordField = new JPasswordField();
        passwordField.setEchoChar('*');
        passwordField.setOpaque(false);//文本区域设为透明
        passwordField.setBounds(160, 121, 150, 21);
        contentPane.add(passwordField);

        //密码确认框
        passwordField_1 = new JPasswordField();
        passwordField.setEchoChar('*');
        passwordField_1.setOpaque(false);//文本区域设为透明
        passwordField_1.setBounds(160, 166, 150, 21);
        contentPane.add(passwordField_1);

        //返回按钮
        final JButton btnNewButton = new JButton("返回");
        btnNewButton.setBounds(120, 218, 72, 25);
        contentPane.add(btnNewButton);

        //注册按钮
        final JButton btnNewButton_1 = new JButton();
        btnNewButton_1.setText("注册");
        btnNewButton_1.setBounds(240, 218, 78, 25);
        btnNewButton_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//设置鼠标在该区域显示状态变化
        getRootPane().setDefaultButton(btnNewButton_1);//设置回车响应
        contentPane.add(btnNewButton_1);

        //提示信息
        lblNewLabel = new JLabel();
        lblNewLabel.setBounds(180, 190, 117, 20);
        lblNewLabel.setForeground(Color.red);
        contentPane.add(lblNewLabel);

        label = new JLabel("用户名：");
        label.setBounds(100, 78, 72, 18);
        contentPane.add(label);

        label_1 = new JLabel("输入密码：");
        label_1.setBounds(100, 119, 86, 18);
        contentPane.add(label_1);

        label_2 = new JLabel("确认密码：");
        label_2.setBounds(100, 169, 86, 18);
        contentPane.add(label_2);

        lblNewLabel_1 = new JLabel("欢迎注册");
        lblNewLabel_1.setFont(new Font("宋体", Font.BOLD, 30));
        lblNewLabel_1.setBounds(150, 13, 266, 38);
        contentPane.add(lblNewLabel_1);

        //返回按钮监听
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnNewButton.setEnabled(false);
                //返回登陆界面
                MyLogin frame = new MyLogin();
                frame.setVisible(true);//显示登录界面
                setVisible(false);//隐藏注册界面
            }
        });

        //注册按钮监听
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Properties userPro = new Properties();
                File file = new File("Users.properties");
                MyUtil.loadPro(userPro, file);

                //获取用户输入的信息
                String u_name = textField.getText();
                String u_pwd = new String(passwordField.getPassword());
                String u_pwd_ag = new String(passwordField_1.getPassword());

                // 判断用户名是否在普通用户中已存在
                if (u_name.length() != 0) {

                    if (userPro.containsKey(u_name)) {
                        lblNewLabel.setText("用户名已存在!");
                    } else {
                        writePassword(userPro, file, u_name, u_pwd, u_pwd_ag);
                    }
                } else {
                    lblNewLabel.setText("用户名不能为空！");
                }
            }

            private void writePassword(Properties userPro, File file, String u_name, String u_pwd, String u_pwd_ag) {
                if (u_pwd.equals(u_pwd_ag)) {
                    if (u_pwd.length() != 0) {
                        userPro.setProperty(u_name, u_pwd_ag);
                        try {
                            userPro.store(new FileOutputStream(file), "Username && Password");
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        btnNewButton_1.setEnabled(false);
                        //返回登陆界面
                        MyLogin frame = new MyLogin();
                        frame.setVisible(true);
                        setVisible(false);
                    } else {
                        lblNewLabel.setText("密码为空！");
                    }
                } else {
                    lblNewLabel.setText("密码不一致！");
                }
            }
        });
    }
}
