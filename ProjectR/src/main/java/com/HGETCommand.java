package com;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;


//HGET key index  取出集合中某个位置的值
public class HGETCommand implements  Command {

      public List<Object> args;
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
          String key=new String((byte[])args.get(0));
          String index= new String((byte[])args.get(1));
        Database database=Database.getObject();
          Map<String,String> map=database.getMap(key);

           String value=map.get(index);
           if(value==null){
               Protocol.writeNull(os);
           }else{

               Protocol.writeBulkString(os,value);
           }

    }
}
