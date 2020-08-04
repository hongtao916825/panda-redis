package connection;/* ━━━━━━如来保佑━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　┻　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━永无BUG━━━━━━
 * 图灵学院-悟空老师
 * 以往视频加小乔老师QQ：895900009
 * 悟空老师QQ：245553999
 */


import protocol.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/***
 * 传输层
 */
public class Connection {

   private Socket socket;
   private String host;
   private int port;
   private OutputStream outputStream;
   private InputStream inputStream;

   public Connection(String host, int port) {
      this.host = host;
      this.port = port;
   }

   /***
    * lianjie zhida  fasong shuju
    * @return

    */
   public Connection sendCommand(Protocol.Command command, byte[] ... args){
      connect();
      Protocol.sendCommand(outputStream,command,args);
      return  this;

   }

    public Connection sendCommand(byte[] ... args){
        connect();
        Protocol.sendCommand(outputStream,args);
        return this;
    }

 public void connect(){
    try {
       if (!isConnected()) {//IO复用
          socket = new Socket(host, port);
          inputStream = socket.getInputStream();
          outputStream = socket.getOutputStream();
       }
    } catch (IOException e) {
       e.printStackTrace();
    }


 }

   public boolean isConnected() {
      return socket != null && socket.isBound() && !socket.isClosed() && socket.isConnected()
              && !socket.isInputShutdown() && !socket.isOutputShutdown();
   }


   public String getStatusReply() {
    byte b[]=new byte[1024];
      try {
         socket.getInputStream().read(b);
      } catch (IOException e) {
         e.printStackTrace();
      }


      return new String(b);
   }
}
