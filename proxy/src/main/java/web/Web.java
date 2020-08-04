package web;

import api.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Web {

    private static ExecutorService executor
            = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(6061);

        while(true) {
            Socket socket = serverSocket.accept();
            try {
                InputStream inputStream = null;
                do {
                    byte[] bytes = new byte[1024];
                    StringBuilder sb = new StringBuilder();
                    inputStream = socket.getInputStream();
                    inputStream.read(bytes);
                    Client jedis = new Client("47.97.215.217",6061);
                    Future<String> future = executor.submit(() -> {
                        return jedis.send(bytes);
                    });
                    String replay = future.get();
                    //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                    sb.append(new String(bytes));
                    System.out.println("get message from client: " + sb);
                    socket.getOutputStream().write(replay.getBytes());
                    socket.getOutputStream().flush();
                } while (!socket.isConnected());
                socket.shutdownOutput();
                socket.shutdownInput();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("客户端已退出，IP:" + socket.getInetAddress() + ",port:" + socket.getPort());
        }

    }

}
