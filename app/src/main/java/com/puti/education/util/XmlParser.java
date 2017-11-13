package com.puti.education.util;

import android.util.Log;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/8/31 0031.
 */

public class XmlParser {
    public static Map<String, String> parseXml(String file) {
        Map<String, String> map = null;

        try {
            SAXReader reader = new SAXReader();
            Document document = DocumentHelper.parseText(file);
            Element root = document.getRootElement();

            List<Element> elementList = root.elements();
            map = new HashMap<String, String>();
            for (Element e : elementList) {
                Log.d("", "name: " + e.getName() + " text: " + e.getText());
                map.put(e.getName(), e.getText());
            }
        }catch(Exception e){
            Log.d("", "parse error" + e.getMessage());
        }

        return map;
    }
}
