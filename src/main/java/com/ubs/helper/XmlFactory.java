package com.ubs.helper;

import com.ubs.Model.sdat.ValidatedMeteredData;
import com.ubs.Model.sdat.ValidatedMeteredData_12;
import com.ubs.Model.sdat.ValidatedMeteredData_13;
import com.ubs.Model.sdat.ValidatedMeteredData_14;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XmlFactory {
    public static ValidatedMeteredData convertToValidatedMeteredData_12_14(String Path) throws JAXBException {
        File xmlFile = new File(Path);
        try {
            JAXBContext context = JAXBContext.newInstance(ValidatedMeteredData_12.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ValidatedMeteredData_12 convertedXml = (ValidatedMeteredData_12) unmarshaller.unmarshal(xmlFile);
            return convertedXml;
        } catch (UnmarshalException e) {
            try {
                JAXBContext context = JAXBContext.newInstance(ValidatedMeteredData_14.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                ValidatedMeteredData_14 convertedXml = (ValidatedMeteredData_14) unmarshaller.unmarshal(xmlFile);
                return convertedXml;
            }catch (UnmarshalException e1){
                JAXBContext context = JAXBContext.newInstance(ValidatedMeteredData_13.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                ValidatedMeteredData_13 convertedXml = (ValidatedMeteredData_13) unmarshaller.unmarshal(xmlFile);
                return convertedXml;
            }
        }
    }

}
