package com.lin.proxymedia;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by linchen on 2018/3/16.
 * mail: linchen@sogou-inc.com
 */

public class HttpProxy {
    final private String LOCAL_IP_ADDRESS = "127.0.0.1";
    final private int HTTP_PORT = 80;
    private ServerSocket localServer = null;  //本地服务端
    private Socket localSocket = null;  //本地Socket
    private Socket remoteSocket = null; //服务器端Socket
    private String remoteIPAddress; //地址
    private ThreadStrategy mThreadStrategy = new SingleThreadStrategy();

    public HttpProxy(int localPort) {
        try {
            // --------建立代理中转服务器-----------//
            localServer = new ServerSocket(localPort, 1, InetAddress.getByName(LOCAL_IP_ADDRESS));
        } catch (Exception e) {

        }
    }

    public void startProx(String remteIp) {
        remoteIPAddress = remteIp;
        SocketAddress address = new InetSocketAddress(remoteIPAddress, HTTP_PORT);
        remoteSocket = new Socket();
        try {
            remoteSocket.connect(address);
            InputStream in_remoteSocket = remoteSocket.getInputStream();
            final OutputStream out_remoteSocket = remoteSocket.getOutputStream();
            mThreadStrategy.runThread(new Runnable() {
                @Override
                public void run() {
                    byte[] local_request = new byte[5120];
                    try {
                        localSocket = localServer.accept();
                        InputStream in_localSocket = localSocket.getInputStream();
                        String buffer = "";
                        while ((in_localSocket.read(local_request)) != -1) {
                            String str = new String(local_request);
                            buffer = buffer + str;
                            if (buffer.contains("GET")
                                    && buffer.contains("\r\n\r\n")) {
                                //---把request中的本地ip改为远程ip---//
                                buffer = buffer.replace(LOCAL_IP_ADDRESS,remoteIPAddress);
                                System.out.println("已经替换IP");
                                out_remoteSocket.write(buffer.getBytes());
                                out_remoteSocket.flush();
                                continue;
                            } else{
                                out_remoteSocket.write(buffer.getBytes());
                                out_remoteSocket.flush();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
