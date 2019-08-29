package com;


import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {

        // InputStream in=System.in;

        String str = "+OK\r\n";
        //String str = "-ERROR Command\r\n";
        //String str = ":-100\r\n";

        InputStream in = new ByteInputStream(str.getBytes(), str.getBytes().length);
        Object object = Protocol.read(in);

        System.out.println(object);
    }

}
