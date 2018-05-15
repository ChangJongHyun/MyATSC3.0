package com.btl.hcj.myapplication;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class XMLparser {
    private XmlPullParser parser = null;

    public static final int KXML_PARSER = 0;
    public static final int EXPATPULL_PARSER = 1;

    // Constructor
    public XMLparser(int parserType) {

        switch (parserType) {
            case KXML_PARSER:
                try {
                    XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                    parser = parserFactory.newPullParser() ;
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case EXPATPULL_PARSER:
                try {
                    parser = Xml.newPullParser();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

    }

    public void readXML(File xmlFile) {
        // xml 파서에 입력스트림 지정
        try {
            FileInputStream fis = new FileInputStream(xmlFile);
            if (parser != null) {
                parser.setInput(fis, null);
            }
        } catch (FileNotFoundException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }


    public void parse() {
        // TODO PARSE
    }


}
