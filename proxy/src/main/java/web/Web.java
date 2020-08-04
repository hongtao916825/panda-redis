package web;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Web {

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(6061);

        while(true) {
            Socket socket = serverSocket.accept();
            try {
                boolean flag = true;
                InputStream inputStream = null;
                do {
                        int len = Integer.MAX_VALUE;
                        byte[] bytes = new byte[1024];
                        StringBuilder sb = new StringBuilder();
                        inputStream = socket.getInputStream();
                        while (len > 1024 && (len = inputStream.read(bytes)) != -1) {
                            //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                            sb.append(new String(bytes, 0, len,"UTF-8"));
                        }
                        System.out.println("get message from client: " + sb);
                        socket.getOutputStream().write("+OK".getBytes());
                        break;

//                        String input = socketInput.readLine();      //获取客户端发送给服务端的消息
//                        if (input.contains("*")) {
//                            System.out.println(input);
//                            index = 0;
//                            length = Integer.valueOf(input.replace("*", ""));
//                        } else {
//                            if (input.contains("$")) {
//                                index++;
//                            } else {
//                                System.out.println(input);
//                                if (index >= length) {
//                                    printStream.println("+OK");             //回复客户端   也就是给客户端的输入流输入消息
//                                }
//                            }
//                        }
//                    if ("12345".equals(input)) {
//                        flag = false;
//                        printStream.println("+OK\r\n");             //回复客户端   也就是给客户端的输入流输入消息
//                    } else {
//                        System.out.println(input);
//                    }
                } while (flag);
                socket.shutdownOutput();
                socket.shutdownInput();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
            System.out.println("客户端已退出，IP:" + socket.getInetAddress() + ",port:" + socket.getPort());
        }

    }

}
