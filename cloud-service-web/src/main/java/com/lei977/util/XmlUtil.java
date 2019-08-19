package com.drore.cloud.tdp.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.json.XML;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by zsj on 2016/12/24.
 */
public class XmlUtil {

    /**
     * 通过文件路径读取xml文件
     * 返回xml格式的字符串
     *
     * @param filepath
     * @return
     */
    public static String xmlToString(String filepath) {
        String documentStr = null;
        SAXReader reader = new SAXReader();
        Document document;
        try {
            document = reader.read(new File(filepath));
            documentStr = document.asXML();
            System.out.println("xml 字符串：");
            System.out.println(documentStr);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return documentStr;
    }


    /**
     * 传入Document对象
     * 获得xml格式的字符串
     *
     * @param document
     * @return
     */
    public static String getXmlString(Document document) {
        String xmlString = "";
        OutputFormat outputFormat = OutputFormat.createPrettyPrint(); //xml输出格式设置
        outputFormat.setEncoding("UTF-8");
        StringWriter stringWriter = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(stringWriter, outputFormat);
        try {
            xmlWriter.write(document);
            stringWriter.close();
            xmlWriter.close();
            xmlString = stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(xmlString);
        return xmlString;
    }


    /**
     * 海康lcd屏幕 叫号接口 xml数据封装
     *
     * @param obj {
     *            "materialNo":"50",
     *            "terminalNoList":["1","2","3"],
     *            "columnList":[
     *            [{"id":"11","value":"1a"}, {"id":"12","value":"1b"} ],
     *            [{"id":"21","value":"2a"}, {"id":"22","value":"2b"} ],
     *            [{"id":"31","value":"3a"}, {"id":"32","value":"3b"} ]
     *            ]
     *            }
     * @return
     */
    public static String getHkLcdXml(JSONObject obj) {
        Document document = DocumentHelper.createDocument();
        Element transData = document.addElement("TransData");
        transData.addAttribute("version", "2.0");
        Element materialNo = transData.addElement("materialNo");
        materialNo.setText(obj.getString("materialNo"));
        Element destType = transData.addElement("destType");
        destType.setText("byTerminal");

        Element terminalNoList = transData.addElement("TerminalNoList");
        JSONArray terminalNoArray = obj.getJSONArray("terminalNoList");
        for (int i = 0; i < terminalNoArray.size(); i++) {
            Element terminalNo = terminalNoList.addElement("terminalNo");
            terminalNo.setText(terminalNoArray.getString(i));
        }

        Element dataType = transData.addElement("dataType");
        dataType.setText("data");

        Element sendData = transData.addElement("SendData");
        Element refreshType = sendData.addElement("refreshType");
        refreshType.setText("all");

        Element itemDataList = sendData.addElement("ItemDataList");
        JSONArray columnList = obj.getJSONArray("columnList");
        for (int j = 0; j < columnList.size(); j++) {
            Element dataList = itemDataList.addElement("DataList");
            JSONArray rowList = columnList.getJSONArray(j);
            for (int k = 0; k < rowList.size(); k++) {
                JSONObject datainfo = rowList.getJSONObject(k);
                Element data = dataList.addElement("Data");
                Element id = data.addElement("id");
                Element value = data.addElement("value");
                id.setText(datainfo.getString("id"));
                value.setText(datainfo.getString("value"));
            }
        }
        return XmlUtil.getXmlString(document);
    }

    /**
     * 将xml字符串转换成json对象
     *
     * @param xmlString
     * @return
     */
    public static JSONObject xml2json(String xmlString) {
        JSONObject object;
        try {
            object = JSONObject.parseObject(String.valueOf(XML.toJSONObject(xmlString)));
        } catch (Exception e) {
            System.out.println("XML解析出错");
            object = new JSONObject();
        }
        return object;
    }

    public static void main(String[] args) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><EventNotify><event_log_id>B1A4330A-2C0A-4F44-9E3A-6431F28E3189</event_log_id><event_type>327681</event_type><status>3</status><start_time>2017-08-01 15:43:30</start_time><stop_time></stop_time><event_config_id>GJ_20170705_0005</event_config_id><event_name>${猢狲岩}_告警</event_name><event_level>1</event_level><object_type>502200</object_type><object_index_code>001262</object_index_code><object_name>猢狲岩_防区通道3</object_name><org_index>001197</org_index><org_name>sos报警柱</org_name><describe>????????????????</describe><ext_info>????????????????</ext_info><pic_data></pic_data></EventNotify>";
        System.out.println(" = " + xml2json(xml));

    }

}
