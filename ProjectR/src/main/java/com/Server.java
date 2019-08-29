package com;
import jdk.internal.util.xml.impl.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {
    public static final Logger logger=  LoggerFactory.getLogger(Server.class);
    public void run(int port){
        try(ServerSocket serverSocket=new ServerSocket(port)){

            while(true){
                try(Socket socket=serverSocket.accept()){
                    logger.info("{}已连接",socket.getInetAddress().getHostName());
                    InputStream in=socket.getInputStream();
                    OutputStream out=socket.getOutputStream();
                    Command command = null;
                    while(true) {
                        command = Protocol.readCommand(in);
                        command.run(out);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new Server().run(6379);
    }
}
