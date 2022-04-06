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
                    // ������½����
                    MyLogin frame = new MyLogin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MyLogin() {
        setTitle("��½����");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(550, 250, 450, 300);
        contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;

            //��¼��������
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon("images/��¼����.jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        //�û��������
        textField = new JTextField();
        textField.setBounds(160, 107, 150, 24);
        textField.setOpaque(false);//�˷��������ÿؼ��Ƿ�͸���ġ�true��ʾ��͸����false��ʾ͸�����ı�������Ϊ͸��
        contentPane.add(textField);
        textField.setColumns(10);

        //���������
        passwordField = new JPasswordField();
        passwordField.setEchoChar('*');//������ʾ�ַ�
        passwordField.setOpaque(false);//�ı�����Ϊ͸��
        passwordField.setBounds(160, 144, 150, 24);
        contentPane.add(passwordField);

        //��¼��ť
        final JButton btnNewButton = new JButton();
        btnNewButton.setText("��¼");
        btnNewButton.setToolTipText("");
        btnNewButton.setBounds(120, 202, 72, 25);
        btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//��������ڸ�������ʾ״̬�仯
        getRootPane().setDefaultButton(btnNewButton);//���ûس���Ӧ
        contentPane.add(btnNewButton);

        //ע�ᰴť
        final JButton btnNewButton_1 = new JButton();
        btnNewButton_1.setText("ע��");
        btnNewButton_1.setBounds(240, 202, 72, 25);
        btnNewButton_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//��������ڸ�������ʾ״̬�仯
        contentPane.add(btnNewButton_1);

        // ��ʾ��Ϣ
        final JLabel lblNewLabel = new JLabel();
        lblNewLabel.setBounds(168, 175, 151, 21);
        lblNewLabel.setForeground(Color.red);
        getContentPane().add(lblNewLabel);

        //�û�����ǩ
        JLabel lblNewLabel_1 = new JLabel("�û�����");
        lblNewLabel_1.setBounds(110, 110, 72, 18);
        contentPane.add(lblNewLabel_1);

        //�����ǩ
        JLabel label = new JLabel("���룺");
        label.setBounds(120, 147, 72, 18);
        contentPane.add(label);

        //�����ǩ
        JLabel lblNChatroom = new JLabel("��������");
        lblNChatroom.setFont(new java.awt.Font("����", java.awt.Font.BOLD, 25));
        lblNChatroom.setBounds(150, 31, 197, 45);
        contentPane.add(lblNChatroom);

        // ������½��ť
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Properties userPro = new Properties();
                File file = new File("Users.properties");
                MyUtil.loadPro(userPro, file);
                String u_name = textField.getText();//��ȡ�û�������û���
                if (file.length() != 0) {

                    if (userPro.containsKey(u_name)) {
                        String u_pwd = new String(passwordField.getPassword());//��ȡ�û����������
                        if (u_pwd.equals(userPro.getProperty(u_name))) {

                            try {
                                Socket client = new Socket("localhost", 8520);

                                btnNewButton.setEnabled(false);
                                MyChatroom frame = new MyChatroom(u_name, client);
                                frame.setVisible(true);// ��ʾ�������
                                setVisible(false);// ���ص���½����

                            } catch (UnknownHostException e1) {
                                errorTip("�����쳣�����µ�½����");
                            } catch (IOException e1) {
                                errorTip("�����쳣");
                            }

                        } else {
                            lblNewLabel.setText("���������������");
                            textField.setText("");
                            passwordField.setText("");
                            textField.requestFocus();
                        }
                    } else {
                        lblNewLabel.setText("�������ǳƲ����ڣ�");
                        textField.setText("");
                        passwordField.setText("");
                        textField.requestFocus();
                    }
                } else {
                    lblNewLabel.setText("�������ǳƲ����ڣ�");
                    textField.setText("");
                    passwordField.setText("");
                    textField.requestFocus();
                }
            }
        });

        //ע�ᰴť����
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnNewButton_1.setEnabled(false);
                MyRegister frame = new MyRegister();
                frame.setVisible(true);// ��ʾע�����
                setVisible(false);// ���ص���½����
            }
        });
    }

    protected void errorTip(String str) {
        //��ʾ������Ϣ�������Ϣ��
        JOptionPane.showMessageDialog(contentPane, str, "Error Message", JOptionPane.ERROR_MESSAGE);
        textField.setText("");
        passwordField.setText("");
        textField.requestFocus();
    }
}
