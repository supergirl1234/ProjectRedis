package com;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
//LRANGE key start end  返回列表中某个范围的值
public class LRANGECommand  implements  Command{

    private List<Object> args;

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
       int start= Integer.parseInt(new String((byte[])args.get(1)));
       int end= Integer.parseInt(new String((byte[])args.get(2)));
        Database database=Database.getObject();
        List<String> list=database.getList(key);
      /*  if(list.size()==0){
            try {
                Protocol.writeNull(os);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {*/
            //List<String> result = new ArrayList<>();//存放结果的
            if (end<0) {

                end = list.size() + end;
            }
            /*for (int i = start; i < end; i++) {
                String str = list.get(i);
                result.add(str);
            }*/

            List<String> result = list.subList(start, end + 1);
            try {
                Protocol.writeArray(os, result);
            } catch (Exception e) {
                e.printStackTrace();
            }

        //}

    }
}
