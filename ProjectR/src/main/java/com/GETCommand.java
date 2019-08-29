package com;

import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.List;


//get key  获取key对应的值
public class GETCommand implements  Command{
    private List<Object> args;
    @Override
    public void setArgs(List<Object> args) {

        this.args=args;
    }
    @Override
    public void run(OutputStream os) throws IOException {
        if(args.size()<1){
            try {
                Protocol.writeError(os,"参数少于2个");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        String key= new String((byte[])args.get(0));
        Database database=Database.getObject();
        String str=database.getString(key);
        if(str==null){
            Protocol.writeNull(os);
        }else{
            Protocol.writeBulkString(os,str);
        }


    }
}
