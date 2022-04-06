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
        setTitle("ע�����");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(550, 250, 450, 300);
        contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //���ر���ͼƬ
                g.drawImage(new ImageIcon("images\\ע�ᱳ��.jpg").getImage(), 0,0, getWidth(), getHeight(), null);
            }
        };
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        //�û��������
        textField = new JTextField();
        textField.setBounds(160, 76, 150, 21);
        textField.setOpaque(false);//�ı�������Ϊ͸��
        contentPane.add(textField);
        textField.setColumns(10);

        //���������
        passwordField = new JPasswordField();
        passwordField.setEchoChar('*');
        passwordField.setOpaque(false);//�ı�������Ϊ͸��
        passwordField.setBounds(160, 121, 150, 21);
        contentPane.add(passwordField);

        //����ȷ�Ͽ�
        passwordField_1 = new JPasswordField();
        passwordField.setEchoChar('*');
        passwordField_1.setOpaque(false);//�ı�������Ϊ͸��
        passwordField_1.setBounds(160, 166, 150, 21);
        contentPane.add(passwordField_1);

        //���ذ�ť
        final JButton btnNewButton = new JButton("����");
        btnNewButton.setBounds(120, 218, 72, 25);
        contentPane.add(btnNewButton);

        //ע�ᰴť
        final JButton btnNewButton_1 = new JButton();
        btnNewButton_1.setText("ע��");
        btnNewButton_1.setBounds(240, 218, 78, 25);
        btnNewButton_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//��������ڸ�������ʾ״̬�仯
        getRootPane().setDefaultButton(btnNewButton_1);//���ûس���Ӧ
        contentPane.add(btnNewButton_1);

        //��ʾ��Ϣ
        lblNewLabel = new JLabel();
        lblNewLabel.setBounds(180, 190, 117, 20);
        lblNewLabel.setForeground(Color.red);
        contentPane.add(lblNewLabel);

        label = new JLabel("�û�����");
        label.setBounds(100, 78, 72, 18);
        contentPane.add(label);

        label_1 = new JLabel("�������룺");
        label_1.setBounds(100, 119, 86, 18);
        contentPane.add(label_1);

        label_2 = new JLabel("ȷ�����룺");
        label_2.setBounds(100, 169, 86, 18);
        contentPane.add(label_2);

        lblNewLabel_1 = new JLabel("��ӭע��");
        lblNewLabel_1.setFont(new Font("����", Font.BOLD, 30));
        lblNewLabel_1.setBounds(150, 13, 266, 38);
        contentPane.add(lblNewLabel_1);

        //���ذ�ť����
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnNewButton.setEnabled(false);
                //���ص�½����
                MyLogin frame = new MyLogin();
                frame.setVisible(true);//��ʾ��¼����
                setVisible(false);//����ע�����
            }
        });

        //ע�ᰴť����
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Properties userPro = new Properties();
                File file = new File("Users.properties");
                MyUtil.loadPro(userPro, file);

                //��ȡ�û��������Ϣ
                String u_name = textField.getText();
                String u_pwd = new String(passwordField.getPassword());
                String u_pwd_ag = new String(passwordField_1.getPassword());

                // �ж��û����Ƿ�����ͨ�û����Ѵ���
                if (u_name.length() != 0) {

                    if (userPro.containsKey(u_name)) {
                        lblNewLabel.setText("�û����Ѵ���!");
                    } else {
                        writePassword(userPro, file, u_name, u_pwd, u_pwd_ag);
                    }
                } else {
                    lblNewLabel.setText("�û�������Ϊ�գ�");
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
                        //���ص�½����
                        MyLogin frame = new MyLogin();
                        frame.setVisible(true);
                        setVisible(false);
                    } else {
                        lblNewLabel.setText("����Ϊ�գ�");
                    }
                } else {
                    lblNewLabel.setText("���벻һ�£�");
                }
            }
        });
    }
}
