package com;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;


//set key value
public class SETCommand implements Command {

    private List<Object> args;
    @Override
    public void setArgs(List<Object> args) {

        this.args=args;
    }

    @Override
    public void run(OutputStream os) throws IOException {
        if(args.size()<2){
            try {
                Protocol.writeError(os,"参数少于3个");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        String key= new String((byte[])args.get(0));
        String value= new String((byte[])args.get(1));
        Database database=Database.getObject();
        Map<String,String> string=database.getString();
        string.put(key,value);
       /* String str=Database.getString(key);
        str.
       str=new String(value);
        System.out.println(Database.getString(key));*/
        Protocol.writeInteger(os,1);

    }
}
