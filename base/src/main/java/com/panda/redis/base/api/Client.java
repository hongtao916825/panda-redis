package com.panda.redis.base.api;/* ━━━━━━如来保佑━━━━━━
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


import com.panda.redis.base.connection.Connection;
import com.panda.redis.base.constants.ProxyConstants;
import com.panda.redis.base.protocol.Protocol;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolAbstract;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;

/***
 * 就是给程序员提供API接口的
 * api操作层
 */
public class Client extends Jedis {

   private Connection connection;

   private String address;

   public Client(String host,int port) {
      super(host, port);
      connection=new Connection(host,port);
      this.address = host + ":" + port;
   }

   public Client(String address) {
      super(address.split(ProxyConstants.NODE_PREFIX)[0], Integer.valueOf(address.split(ProxyConstants.NODE_PREFIX)[1]));
      String host = address.split(ProxyConstants.NODE_PREFIX)[0];
      int port = Integer.parseInt(address.split(ProxyConstants.NODE_PREFIX)[1]);
      this.address = address;
      connection=new Connection(host,port);
   }

   public Client(String host, int port, int connectionTimeout, int soTimeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
      super(host, port, connectionTimeout, soTimeout,
              ssl, sslSocketFactory, sslParameters, hostnameVerifier);
   }

   /**
    * Set the string value as value of the key. The string can't be longer than 1073741824 bytes (1
    * GB).
    * <p>
    * Time complexity: O(1)
    * @param key
    * @param value
    * @return Status code reply
    */
   @Override
   public String set(final String key, String value) {
      connection.sendCommand(Protocol.Command.SET,SafeEncode.encode(key),SafeEncode.encode(value));//TODO 发送数据
      return  connection.getStatusReply();
   }

   public String send(final byte[] b) {
      connection.sendCommand(b);//TODO 发送数据
      return  connection.getStatusReply();
   }

   /**
    * Get the value of the specified key. If the key does not exist null is returned. If the value
    * stored at key is not a string an error is returned because GET can only handle string values.
    * <p>
    * Time complexity: O(1)
    * @param key
    * @return Bulk reply
    */
   @Override
   public String get(final String key) {
         connection.sendCommand(Protocol.Command.GET,SafeEncode.encode(key));
      return  connection.getStatusReply();
   }

   /**
    * Get the value of the specified key. If the key does not exist null is returned. If the value
    * stored at key is not a string an error is returned because GET can only handle string values.
    * <p>
    * Time complexity: O(1)
    * @return Bulk reply
    */

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }
}
