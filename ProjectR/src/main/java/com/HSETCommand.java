package com;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;


//HSET key index value    设置列表中某个位置的值
public class HSETCommand implements Command {

    public List<Object> args;

    @Override
    public void setArgs(List<Object> args) {
        this.args=args;
    }

    @Override
    public void run(OutputStream os) throws IOException {

        if(args.size()<3){
            try {
                Protocol.writeError(os,"参数少于4个");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
          String key= new String((byte[])args.get(0));
          String index= new String((byte[])args.get(1));
          String value=new String((byte[])args.get(2));
        Database database=Database.getObject();
          Map<String,String> map=database.getMap(key);
          boolean isCun=map.containsKey(index);
          map.put(index,value);
          if(isCun){
              Protocol.writeInteger(os,0);//如果已经存在index，则只是把数据替换，不改变数量

          }else{
              Protocol.writeInteger(os,1);//如不存在index，则把数据插入，数量改变为1

          }
    }
}
