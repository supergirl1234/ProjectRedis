package com;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/*协议解析，协议编解码，将字节流转化为对象*/

public class Protocol {

    /*读取客户端输入的数据*/
    public static Command readCommand(InputStream in) throws Exception {
        Object object = read(in);
        if (!(object instanceof List)) {
            throw new Exception("命令必须是Array类型");

        }
        /*lpush chen 1*/
        List<Object> list = (List<Object>) object;
        if (list.size()<1) {
            throw new Exception("命令元素个数必须大于1");
        }
        Object require = list.remove(0);/*require=lpush,remove之后，list=chen 1*/
        if (!(require instanceof byte[])) {

            throw new Exception("错误的命令类型");
        }
        byte[] array = (byte[]) require;
        String commandName = new String(array);
        String className = String.format("com.%sCommand", commandName.toUpperCase());
        Class<?> cls = Class.forName(className);
        if (!Command.class.isAssignableFrom(cls)) {
            throw new Exception("错误的命令");

        }
        /*LPUSHCommand*/
        Command command = (Command) cls.newInstance();
        command.setArgs(list);/*此时的1ist=chen 1*/
        return command;
    }

    /*对输入的数据判断一下是什么类型*/
    public static Object read(InputStream in) throws Exception {

        int o = in.read();

        if (o == -1) {
            throw new RuntimeException("不应该读到数据结尾");
        }
        switch (o) {
            /*reids内部也会对输入的数据做解析*/
            // Simple String
            case '+':
                return readSimpleString(in);
            //Error
            case '-':
                throw new Exception(readError(in));
                //Integer
            case ':':
                return readInteger(in);
            //BulkString
            case '$':
                return readBulkString(in);
            //Array
            case '*':
                return readArray(in);
            default:
                throw new RuntimeException("输入数据格式不正确");
        }
    }

    /*String*/
    public static String readSimpleString(InputStream in) throws IOException {

        return readString(in);

    }

    /*Exeception*/
    public static String readError(InputStream in) throws Exception {

        return readString(in);
    }

    /*long*/
    public static long readInteger(InputStream in) throws Exception {
        return readInt(in);
    }

    /*byte[]*/
    public static byte[] readBulkString(InputStream in) throws Exception {


        int length = (int) readInt(in);
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int a = in.read();
            bytes[i] = (byte) a;

        }

        /*还有剩余的\r\n*/
        in.read();
        in.read();
        return bytes;
    }

    /*list*/
    public static List readArray(InputStream in) throws Exception {
        int length = (int) readInt(in);
        List<Object> list = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            Object object= read(in);
            list.add(object);
        }
        return list;
    }


    public static String readString(InputStream in) throws IOException {

        StringBuilder sb = new StringBuilder();
        boolean needMoreRead = true;
        int a = -1;
        while (true) {
            if (needMoreRead) {
                a = in.read();
                if (a == -1) {
                    throw new Error("不应该读到数据结尾");
                }
            } else {
                needMoreRead = true;
            }
            if (a == '\r') {

                int b = in.read();
                if (b == -1) {

                    throw new Error("不应该读到数据结尾");

                }
                if (b == '\n') {
                    break;

                }
                if (b == '\r') {
                    sb.append((char) a);
                    a = b;
                    needMoreRead = false;
                } else {
                    sb.append((char) a);
                    sb.append((char) b);
                }

            } else {

                sb.append((char) a);

            }


        }
        return sb.toString();

    }

    public static long readInt(InputStream in) throws Exception {

        StringBuilder sb = new StringBuilder();
        boolean isFu = false;

        int a = in.read();
        if (a == -1) {
            throw new RuntimeException("不应该读到数据末尾");
        }

        if (a == '-') {//负数
            isFu = true;
        } else {
            sb.append((char) a);
        }

        while (true) {
            a = in.read();
            if (a == '\r') {
                int b = in.read();
                if (b == '\n') {
                    break;
                }
                if (b == -1) {
                    throw new RuntimeException("不应该读到数据末尾");
                }
                sb.append((char) a);
                sb.append((char) b);

            } else {

                sb.append((char) a);
            }

        }

        long num = Long.parseLong(sb.toString());
        if (isFu) {
            num = -num;
        }
        return num;

    }


    /*写*/
    public static void writeArray(OutputStream os, List<?> list) throws Exception {
        try {
            os.write('*');
            os.write(String.valueOf((list.size())).getBytes());
            os.write("\r\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Object item : list) {

            if (item instanceof String) {
                Protocol.writeBulkString(os, (String)item);

            } else if (item instanceof Integer) {
                Protocol.writeInteger(os, item);

            } else if (item instanceof Long) {
                Protocol.writeInteger(os,item);

            } else {
                throw new Exception("错误的类型");
            }
        }

    }

    public static void writeInteger(OutputStream os, Object value) {

        try {
            os.write(':');
            os.write(String.valueOf(value).getBytes());
            os.write("\r\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeBulkString(OutputStream os,String str) {

        try {
            os.write('$');
            os.write(String.valueOf(str.length()).getBytes());//长度
            os.write("\r\n".getBytes());
            os.write(str.getBytes());//内容
            os.write("\r\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeNull(OutputStream os) {
        try {
            os.write('$');
            os.write('-');
            os.write('1');
            os.write("\r\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeError(OutputStream os,String message) {
        try {
            os.write('-');
            os.write(message.getBytes());
            os.write("\r\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
