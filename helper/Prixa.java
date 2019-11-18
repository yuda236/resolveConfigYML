package com.example.prixa.helper;


import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.prixa.R;
import com.example.prixa.model.NodeTree;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Node;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


public class Prixa {

    public static String resolveConfig(InputStream yml, InputStream env) {
        String isiyml = readTextFile(yml);
        String[] split = isiyml.split("\r\n");
        String pembuka = "{\r\n";
        String penutup = "}";
        String isi = "";
        String result = "";

        String enviroment = readTextFile(env);
        String[] splitenv = enviroment.split("\r\n");
        Map<String, String> val = new HashMap<String, String>();

        for (int i = 0; i < splitenv.length; i++) {
            if (!(splitenv[i].trim().toString()).equals("")) {
                String[] splitenv2 = splitenv[i].split("=");
                if (splitenv2.length == 2) {
                    val.put(splitenv2[0].trim(), splitenv2[1].trim());
                }

            }
        }
        Map<Integer, Integer> parent = new HashMap<Integer, Integer>();
        Map<Integer, String> child = new HashMap<Integer, String>();
        ArrayList<Integer> rootIndex = new ArrayList<Integer>();
        ArrayList<NodeTree> tree = new ArrayList<>();

        //retrieve data;
        for (int i = 0; i < split.length; i++) {
            String[] split2 = split[i].split(":");
            int spasi = countSpace(split2[0]);
            int lengthsplit2 = split2.length;
            int parentval = 0;
            NodeTree node = new NodeTree();

            child.put(i, "");

            if (spasi == 0) {
                rootIndex.add(i);
                parent = new HashMap<Integer, Integer>();
                parent.put(spasi, i);
            } else {
                parent.put(spasi, i);
                int parentvalindex = spasi - 2;
                parentval = parent.get(parentvalindex);
                node.setParrent(parentval);
                addChild(child, i, parentval);

            }

            node.setId(i);
            node.setNamaNode(split2[0].toString().trim());
            node.setSpasi(spasi);
            if (split2.length == 2) {
                node.setIs_data(true);
                String key = split2[1].trim().replace("${$", "").replace("}", "");
                if(val.get(key) != null ) {
                    String valuekey = val.get(key);
                    boolean isdigitvalue = false;
                    try {
                        int num = Integer.parseInt(valuekey);
                        isdigitvalue = true;
                    } catch (NumberFormatException e) {
                        isdigitvalue= false;
                    }
                    if (!isdigitvalue) {
                        valuekey = "\"" + valuekey.replace("\"", "") + "\"";
                    }
                    node.setValue(valuekey);
                }
                else{
                    node.setValue("null");
                }



            } else {
                node.setIs_data(false);
            }

            tree.add(node);

        }

        //create json
        for (int i = 0; i < rootIndex.size(); i++) {
            int indextree = rootIndex.get(i);
            NodeTree node = tree.get(indextree);
            String temp = "";

            if (node.getIs_data() == true) {
                int spasiinsert = node.getSpasi() + 2;
                String str = str = addSpaceBegin("\"" + node.getNamaNode() + "\": " + node.getValue(), spasiinsert);
                temp = str;
            } else {
                int spasiinsert = node.getSpasi() + 2;
                String str = "\"" + node.getNamaNode() + "\": {\r\n";
                String temppembuka = addSpaceBegin(str, spasiinsert);
                String tempisi = rekursifchild(indextree, child, tree);
                String temppenutup = addSpaceBegin("}", spasiinsert);

                temp = temp + temppembuka + tempisi + temppenutup;

            }

            if (i != rootIndex.size() - 1) {
                temp = temp + "," + "\r\n";
            } else {
                temp = temp + "\r\n";
            }
            isi = isi + temp;

        }

        result = pembuka + isi + penutup;

        return result;
    }

    private static String rekursifchild(int parentindex, Map<Integer, String> child, ArrayList<NodeTree> tree) {
        String childtree = child.get(parentindex);
        String[] splitchild = childtree.split(",");
        String rekursifresult = "";

        if(splitchild.length == 0 || childtree.trim().equals("")){
            rekursifresult= "";
        }
        else {

            for (int i = 0; i < splitchild.length; i++) {
                int childindex = Integer.parseInt(splitchild[i].toString().trim());
                NodeTree nodechild =  tree.get(childindex);
                String temp = "";

                if (nodechild.getIs_data() == true) {
                    int spasiinsert = nodechild.getSpasi() + 2;
                    String str = addSpaceBegin("\"" + nodechild.getNamaNode() + "\": " + nodechild.getValue(),spasiinsert);
                    temp = str;
                } else {
                    int spasiinsert = nodechild.getSpasi() + 2;
                    String str = "\"" + nodechild.getNamaNode() + "\": {\r\n";
                    String temppembuka = addSpaceBegin(str, spasiinsert);
                    String tempisi = rekursifchild(childindex,child,tree);
                    String temppenutup = addSpaceBegin("}", spasiinsert);

                    temp = temp + temppembuka + tempisi + temppenutup;

                }

                if (i != splitchild.length - 1) {
                    temp = temp + "," + "\r\n";
                } else {
                    temp = temp + "\r\n";
                }
                rekursifresult = rekursifresult + temp;

            }
        }

        return rekursifresult;
    }

    private static void addChild(Map<Integer, String> child, int i, int parentval) {
        String value = child.get(parentval);
        if (value.trim().toString().equals("")) {
            child.put(parentval, String.valueOf(i));
        } else {
            value = value + "," + String.valueOf(i);
            child.put(parentval, value);
        }
    }

    public static int countSpace(String kata) {
        int jumlahTotalKata = kata.length();
        int jumlahTotalKata2 = kata.replace(" ", "").length();
        return jumlahTotalKata - jumlahTotalKata2;
    }

    public static String addSpaceBegin(String kata, int jumlahspasi) {
        String result = "";

        for (int i = 0; i < jumlahspasi; i++) {
            result = result + " ";
        }

        return result + kata;
    }

    public static String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }

}
