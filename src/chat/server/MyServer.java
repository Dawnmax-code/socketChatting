package chat.server;

import chat.bean.ClientBean;
import chat.bean.PacketBean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MyServer {

    private static ServerSocket ss;
    public static HashMap<String, ClientBean> onlines;

    static {
        try {
            //建立端口号
            ss = new ServerSocket(8520);
            onlines = new HashMap<String, ClientBean>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //线程内部类
    class ClientThread extends Thread {
        private Socket client;
        private PacketBean bean;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        public ClientThread(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                // 不停的从客户端接收信息
                while (true) {
                    // 读取从客户端接收到的PacketBean信息
                    ois = new ObjectInputStream(client.getInputStream());//获取输入流 反序列化
                    bean = (PacketBean) ois.readObject();//读取从客户端收到的packetbean信息

                    // 分析PacketBean中type是哪一种类型
                    switch (bean.getType()) {
                        case -1: { // 下线
                            // 创建服务器的PacketBean，并发送给客户端
                            PacketBean serverBean = new PacketBean();
                            serverBean.setType(-1);

                            try {
                                oos = new ObjectOutputStream(client.getOutputStream());
                                oos.writeObject(serverBean);
                                oos.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //移除下线用户
                            onlines.remove(bean.getName());

                            // 向剩下的在线用户发送有人离开的通知
                            PacketBean serverBean2 = new PacketBean();
                            serverBean2.setInfo(bean.getTimer() + "  " + bean.getName() + " " + " 下线了");//显示下线信息
                            serverBean2.setType(0);
                            HashSet<String> set = new HashSet<String>();
                            set.addAll(onlines.keySet());//刷新在线用户列表
                            serverBean2.setClients(set);

                            sendAll(serverBean2);
                            return;
                        }
                        // 上下线更新
                        case 0: { // 上线

                            // 记录上线客户的用户名和端口在ClientBean中
                            ClientBean cbean = new ClientBean();
                            cbean.setName(bean.getName());
                            cbean.setSocket(client);
                            // 添加在线用户
                            onlines.put(bean.getName(), cbean);
                            // 创建服务器的PacketBean，并发送给客户端
                            PacketBean serverBean = new PacketBean();
                            serverBean.setType(0);
                            serverBean.setInfo(bean.getTimer() + "  " + bean.getName() + "上线了");//设置在聊天面板上显示的上线提示
                            // 通知所有客户有人上线
                            HashSet<String> set = new HashSet<String>();
                            // 客户昵称
                            set.addAll(onlines.keySet());//刷新在线用户列表
                            serverBean.setClients(set);
                            sendAll(serverBean);
                            break;
                        }

                        case 1: { // 私聊

                            //创建服务器的PacketBean，并发送给客户端
                            PacketBean serverBean = new PacketBean();

                            serverBean.setType(1);
                            serverBean.setClients(bean.getClients());
                            serverBean.setInfo(bean.getInfo());
                            serverBean.setName(bean.getName());
                            serverBean.setTimer(bean.getTimer());
                            // 向选中的客户发送数据
                            sendMessage(serverBean);
                            break;
                        }

                        case 2: { // 请求接受文件

                            // 创建服务器的PacketBean，并发送给客户端
                            PacketBean serverBean = new PacketBean();
                            String info = bean.getTimer() + "  " + bean.getName() + "向你传送文件,是否需要接受";

                            serverBean.setType(2);
                            serverBean.setClients(bean.getClients()); // 这是发送的目的地
                            serverBean.setFileName(bean.getFileName()); // 文件名称
                            serverBean.setSize(bean.getSize()); // 文件大小
                            serverBean.setInfo(info);//提示信息内容
                            serverBean.setName(bean.getName()); // 来源
                            serverBean.setTimer(bean.getTimer());
                            // 向选中的客户发送数据
                            sendMessage(serverBean);

                            break;
                        }
                        case 3: { // 确定接收文件

                            PacketBean serverBean = new PacketBean();

                            serverBean.setType(3);
                            serverBean.setClients(bean.getClients()); // 文件来源
                            serverBean.setTo(bean.getTo()); // 文件目的地
                            serverBean.setFileName(bean.getFileName()); // 文件名称
                            serverBean.setIp(bean.getIp());
                            serverBean.setPort(bean.getPort());
                            serverBean.setName(bean.getName()); // 接收的客户名称
                            serverBean.setTimer(bean.getTimer());
                            // 通知文件来源的客户，对方确定接收文件
                            sendMessage(serverBean);
                            break;
                        }
                        case 4: { //取消接收文件

                            PacketBean serverBean = new PacketBean();

                            serverBean.setType(4);
                            serverBean.setClients(bean.getClients()); // 文件来源
                            serverBean.setTo(bean.getTo()); // 文件目的地
                            serverBean.setFileName(bean.getFileName());
                            serverBean.setInfo(bean.getInfo());
                            serverBean.setName(bean.getName());// 接收的客户名称
                            serverBean.setTimer(bean.getTimer());
                            sendMessage(serverBean);

                            break;
                        }
                        case 5: { // 群发

                            //创建服务器的PacketBean，并发送给客户端
                            PacketBean serverBean = new PacketBean();

                            serverBean.setType(5);
                            serverBean.setClients(bean.getClients());
                            serverBean.setInfo(bean.getInfo());
                            serverBean.setName(bean.getName());
                            serverBean.setTimer(bean.getTimer());
                            // 向选中的客户发送数据
                            sendAll(serverBean);

                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }

        // 向选中的用户发送数据
        private void sendMessage(PacketBean serverBean) {
            // 首先取得所有的values
            Set<String> cbs = onlines.keySet();
            Iterator<String> it = cbs.iterator();
            // 选中客户
            HashSet<String> clients = serverBean.getClients();
            while (it.hasNext()) {
                // 在线客户
                String client = it.next();
                // 选中的客户中若是在线的，就发送serverbean
                if (clients.contains(client)) {
                    Socket c = onlines.get(client).getSocket();
                    ObjectOutputStream oos;
                    try {
                        oos = new ObjectOutputStream(c.getOutputStream());
                        oos.writeObject(serverBean);
                        oos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        // 向所有的用户发送数据
        public void sendAll(PacketBean serverBean) {
            Collection<ClientBean> clients = onlines.values();
            Iterator<ClientBean> it = clients.iterator();
            ObjectOutputStream oos;
            while (it.hasNext()) {
                Socket c = it.next().getSocket();
                try {
                    //进行数据传输
                    oos = new ObjectOutputStream(c.getOutputStream());
                    oos.writeObject(serverBean);
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //关闭各种流
        private void close() {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void start() {
        try {
            //不停地接收来自客户端的请求
            while (true) {
                //建立连接，线程处理
                Socket client = ss.accept();
                new ClientThread(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //主方法
    public static void main(String[] args) {
        new MyServer().start();
    }

}
