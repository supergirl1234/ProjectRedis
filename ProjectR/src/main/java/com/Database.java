package com;

import java.util.*;

public class Database {

    /*这是一个饿汉单例模式*/
  private static Database object=new Database();
  public static Database getObject(){
      return object;
    }

    /*redis的五种数据类型*/
    private   Map<String,String> string;
    private   Map<String,List<String>> list;
    private  Map<String,Map<String,String>>  hash;
    private  Map<String,Set<String>> set;
    private  Map<String, LinkedHashSet<String>> zset;

    private  Database(){
        string=new HashMap<>() ;
        list=new HashMap<>();
        hash=new HashMap<>();
        set=new HashMap<>();
        zset=new HashMap<>();
    }



//    string
   public  Map<String, String> getString() {
        return string;
    }
    public  String getString(String key){

        String str=string.get(key);
        return  str;
    }



    /*list*/
    /*key：chen*/
    public  List<String> getList(String key){
        List<String>  li=list.get(key);
        if(li==null) {
            li = new ArrayList<>();

                list.put(key, li);

        }

        return  li;
    }


    /*hash*/
    public  Map<String,String> getMap(String key){
        Map<String,String> map=hash.get(key);
        if(map==null){
            map=new HashMap<>();
            hash.put(key,map);
        }
        return  map;
    }
}
