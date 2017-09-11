package cn.jk.kaoyandanci.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import cn.jk.kaoyandanci.ui.activity.LearnWordActivity;

/**
 * Created by Administrator on 2017/6/26.
 */

public class ButterKnifeBindViewConstructor {

    static List<String> fieldNames = new ArrayList<>();

    static HashMap<String, String> viewMap = new HashMap<>();

    static {
        viewMap.put("", "");
    }

    public static void main(String[] args) throws Exception {

        String xmlPath = "K:\\android\\KaoYanDanci\\app\\src\\main\\res\\layout\\activity_learn_word.xml";
        Class classToInsert = LearnWordActivity.class;
        showBindCode(classToInsert, xmlPath);
    }

    public static void showBindCode(Class classToInsert, String xmlPath) throws Exception {


        for (Field field : classToInsert.getDeclaredFields()) {
            fieldNames.add(field.getName());
        }
        final File f = new File(classToInsert.getProtectionDomain().getCodeSource().getLocation().getPath());
        System.out.print(f.getAbsolutePath());

        File xmlFile = new File(xmlPath);

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document document = docBuilder.parse(xmlFile);
        doSomething(document.getDocumentElement());
    }

    public static void doSomething(Node node) {
        Node idNode = node.getAttributes().getNamedItem("android:id");
        if (idNode != null) {
            String id = idNode.getNodeValue().substring(5);
            if (!fieldNames.contains(id)) {
                String[] names = node.getNodeName().split("\\.");
                String nodeName = names[names.length - 1];

                System.out.print("@BindView(R.id." + id + ")\n" + nodeName + " " + id + ";\n");
            }
        }
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                //calls this method for all the children which is Element
                doSomething(currentNode);
            }
        }
    }
}
