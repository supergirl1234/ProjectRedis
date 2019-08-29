package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

//LPUSH key string  将某个值加入到一个key列表头部
public class LPUSHCommand  implements  Command{
    private static final Logger logger = LoggerFactory.getLogger(LPUSHCommand.class);
    /*chen 1*/
    private List<Object> args;
    @Override
    public void setArgs(List<Object> args) {
        this.args=args;
    }
    @Override
    public void run(OutputStream os) throws IOException {

        if(args.size()<2){
            Protocol.writeError(os,"参数少于2");
           return;
        }
        String key= new String((byte[])args.get(0));//chen
        String value= new String((byte[])args.get(1));//1
        Database database=Database.getObject();
        List<String> list= database.getList(key);/*获取数据库中的list*/
        list.add(0,value);//头插
       logger.debug("插入后共{}个数据",list.size());
        Protocol.writeInteger(os,list.size());


    }
}
