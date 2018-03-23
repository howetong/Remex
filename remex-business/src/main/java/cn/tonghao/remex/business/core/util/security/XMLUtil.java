package cn.tonghao.remex.business.core.util.security;

import cn.tonghao.remex.business.core.log.RemexLogger;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class XMLUtil {
    private static final Logger LOG = RemexLogger.getLogger(XMLUtil.class);

    /**
     * 没有多层节点的xml字符串转map
     *
     * @param xmlStr
     * @return
     */
    public static Map<String, String> xml2Map(String xmlStr) {
        try {
            if (StringUtils.isEmpty(xmlStr)) {
                return null;
            }
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            StringReader xmlReader = new StringReader(xmlStr);
            InputSource xmlSource = new InputSource(xmlReader);
            Document document = builder.parse(xmlSource);
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            Map<String, String> nodeMap = new HashMap<String, String>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE && nodeList.item(i).hasChildNodes()) {
                    String key = nodeList.item(i).getNodeName();
                    String value = nodeList.item(i).getFirstChild().getNodeValue();
                    nodeMap.put(key, value);
                }
            }
            return nodeMap;
        } catch (Exception e) {
            LOG.error("xml转map失败:", e);
            return null;
        }
    }

    public static String replaceVariableForXml(Map<String, String> variables, String xml) {
        int len = xml.length();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = xml.charAt(i);
            if (c == '$') {
                i++;
                StringBuilder key = new StringBuilder();
                for (char temp = xml.charAt(i); temp != '}'; temp = xml
                        .charAt(i)) {
                    if (temp != '{')
                        key.append(temp);
                    i++;
                }
                String variable = null;
                try {
                    if (variables.containsKey(key.toString())) {
                        variable = String.valueOf(variables.get(key.toString()));
                    }
                } catch (Exception e) {
                    LOG.info("type cast exception for {}", key.toString());
                    LOG.error("Exception", e);
                    variable = null;
                }
                if (null == variable || "null".equalsIgnoreCase(variable))
                    builder.append("");
                else
                    builder.append(variable);
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }


    public static <T> String replaceVariableForXml(T t, String xml) throws RuntimeException, IllegalAccessException {
        int len = xml.length();
        StringBuilder builder = new StringBuilder();

        Field[] fields = t.getClass().getDeclaredFields();

        if (fields == null || fields.length == 0) {
            throw new RuntimeException("no find fields of object " + t.getClass().getSimpleName());
        }

//        List<String> variables = new ArrayList<String>();
        Map<String, String> variables = new HashMap<String, String>();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(t);
            if (value != null) {
                variables.put(field.getName().toUpperCase(), value.toString());
            }
        }

        for (int i = 0; i < len; i++) {
            char c = xml.charAt(i);
            if (c == '$') {
                i++;
                StringBuilder key = new StringBuilder();
                for (char temp = xml.charAt(i); temp != '}'; temp = xml
                        .charAt(i)) {
                    if (temp != '{')
                        key.append(temp);
                    i++;
                }
                String variable = null;
                try {

                    if (variables.containsKey(key.toString().toUpperCase())) {
                        variable = variables.get(key.toString().toUpperCase());
                    }
                } catch (Exception e) {
                    LOG.info("type cast exception for {}", key.toString());
                    LOG.error("Exception", e);
                    variable = null;
                }
                if (null == variable || "null".equalsIgnoreCase(variable))
                    builder.append("");
                else
                    builder.append(variable);
            } else {
                builder.append(c);
            }
        }

//        Document document = null;
//        try {
//            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(builder.toString());
//        } catch (ParserConfigurationException e) {
//            LOG.error("ParserConfigurationException", e);
//        } catch (SAXException e) {
//            LOG.error("SAXException", e);
//        } catch (IOException e) {
//            LOG.error("IOException", e);
//        }
//
//        if (document == null) {
//            return null;
//        }
//
//        return document.toString();

        return builder.toString().replace("\r\n", "");
    }

    public static <T> void xml2Object(String xmlStr, T obj) {
        try {
            if (StringUtils.isEmpty(xmlStr)) {
                return;
            }

            if (obj == null) {
                return;
            }

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            StringReader xmlReader = new StringReader(xmlStr);
            InputSource xmlSource = new InputSource(xmlReader);
            Document document = builder.parse(xmlSource);
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            Field[] fields = obj.getClass().getDeclaredFields();

            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE && nodeList.item(i).hasChildNodes()) {
                    String key = nodeList.item(i).getNodeName();
                    String value = nodeList.item(i).getFirstChild().getNodeValue();
                    for (Field field : fields) {
                        if (field == null) {
                            continue;
                        }
                        field.setAccessible(true);
                        if (field.getName().toUpperCase().equals(key)) {

                            if (field.getType().getSimpleName().equals("BigDecimal")) {
                                field.set(obj, new BigDecimal(value));
                            } else if (field.getType().getSimpleName().equals("String")) {
                                field.set(obj, value);
                            }

                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("xml转map失败:", e);
        }
    }


}
