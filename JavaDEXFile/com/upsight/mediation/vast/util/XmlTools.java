package com.upsight.mediation.vast.util;

import com.google.android.gms.location.places.Place;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlTools {
    private static String TAG;

    static {
        TAG = "XmlTools";
    }

    public static String getElementValue(Node node) {
        NodeList childNodes = node.getChildNodes();
        String str = null;
        int i = 0;
        while (i < childNodes.getLength()) {
            String trim = ((CharacterData) childNodes.item(i)).getData().trim();
            if (trim.length() != 0) {
                return trim;
            }
            i++;
            str = trim;
        }
        return str;
    }

    public static void logXmlDocument(Document document) {
        try {
            Transformer newTransformer = TransformerFactory.newInstance().newTransformer();
            newTransformer.setOutputProperty("omit-xml-declaration", "no");
            newTransformer.setOutputProperty("method", "xml");
            newTransformer.setOutputProperty("indent", "yes");
            newTransformer.setOutputProperty("encoding", "UTF-8");
            newTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            newTransformer.transform(new DOMSource(document), new StreamResult(new StringWriter()));
        } catch (Exception e) {
        }
    }

    public static String stringFromStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[Place.TYPE_SUBLOCALITY_LEVEL_2];
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    public static Document stringToDocument(String str) {
        try {
            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(str));
            return newDocumentBuilder.parse(inputSource);
        } catch (Exception e) {
            return null;
        }
    }

    public static String xmlDocumentToString(Document document) {
        try {
            Transformer newTransformer = TransformerFactory.newInstance().newTransformer();
            newTransformer.setOutputProperty("omit-xml-declaration", "no");
            newTransformer.setOutputProperty("method", "xml");
            newTransformer.setOutputProperty("indent", "yes");
            newTransformer.setOutputProperty("encoding", "UTF-8");
            newTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            Writer stringWriter = new StringWriter();
            newTransformer.transform(new DOMSource(document), new StreamResult(stringWriter));
            return stringWriter.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String xmlDocumentToString(Node node) {
        try {
            Transformer newTransformer = TransformerFactory.newInstance().newTransformer();
            newTransformer.setOutputProperty("omit-xml-declaration", "yes");
            newTransformer.setOutputProperty("method", "xml");
            newTransformer.setOutputProperty("indent", "yes");
            newTransformer.setOutputProperty("encoding", "UTF-8");
            newTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            Writer stringWriter = new StringWriter();
            newTransformer.transform(new DOMSource(node), new StreamResult(stringWriter));
            return stringWriter.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
