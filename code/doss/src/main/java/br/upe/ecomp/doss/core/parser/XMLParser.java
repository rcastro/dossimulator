/**
 * Copyright (C) 2010
 * Swarm Intelligence Team (SIT)
 * Department of Computer and Systems
 * University of Pernambuco
 * Brazil
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package br.upe.ecomp.doss.core.parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.Configurable;

/**
 * Parses an {@link Algorithm} class to XML, saving all its settings.
 * 
 * @author Rodrigo Castro
 */
public final class XMLParser {

    private static final int INDENT_SIZE = 4;
    private static final String XML_VERSION = "1.0";
    private static final String XML_ENCODING = "UTF-8";

    private XMLParser() {
    }

    /**
     * Generate the XML document based on the algorithm.
     * 
     * @param algorithm The algorithm that we want to save the configuration on a XML file.
     * @param filePath The path of the XML file.
     * @param fileName The name of the XML file.
     */
    public static void generateXMLDocument(Algorithm algorithm, String filePath, String fileName) {
        Document xmlDoc;
        DocumentBuilder docBuilder;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactoryImpl.newInstance();
        try {
            docBuilder = dbFactory.newDocumentBuilder();
            xmlDoc = docBuilder.newDocument();
            xmlDoc.appendChild(xmlDoc.createElement("project"));

            addSimpleElememnt(xmlDoc, algorithm, "algorithm");
            addSimpleElememnt(xmlDoc, algorithm.getProblem(), "problem");
            addComplexElement(xmlDoc, algorithm.getStopConditions(), "stopConditions", "stopCondition");
            addComplexElement(xmlDoc, algorithm.getMeasurements(), "measurements", "measurement");

            saveFile(xmlDoc, filePath, fileName);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the generated XML to a file with the given name.
     * 
     * @param xmlDoc The {@link Document} that contains the XML generated.
     * @param filePath The path of the XML file.
     * @param fileName The name of the XML file.
     */
    public static void saveFile(Document xmlDoc, String filePath, String fileName) {
        OutputFormat format = new OutputFormat();
        format.setEncoding(XML_ENCODING);
        format.setVersion(XML_VERSION);
        format.setIndenting(true);
        format.setIndent(INDENT_SIZE);

        XMLSerializer serializer = new XMLSerializer();
        serializer.setOutputFormat(format);

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(new File(filePath, fileName + ".xml"));
            serializer.setOutputCharStream(fileWriter);
            serializer.serialize(xmlDoc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void addSimpleElememnt(Document xmlDoc, Configurable configurable, String elementName) {
        Element item = xmlDoc.createElement(elementName);
        item.setAttribute("class", configurable.getClass().getCanonicalName());

        addParameters(item, xmlDoc, configurable);

        xmlDoc.getDocumentElement().appendChild(item);
    }

    private static void addComplexElement(Document xmlDoc, List<? extends Configurable> configurableList,
            String elementName, String elementChieldName) {
        Element item = xmlDoc.createElement(elementName);

        Element itemChield;
        for (Configurable configurable : configurableList) {
            itemChield = xmlDoc.createElement(elementChieldName);
            itemChield.setAttribute("class", configurable.getClass().getCanonicalName());
            addParameters(itemChield, xmlDoc, configurable);
            item.appendChild(itemChield);
        }

        xmlDoc.getDocumentElement().appendChild(item);
    }

    private static void addParameters(Element item, Document xmlDoc, Configurable configurable) {
        Element itemChield;
        for (String parameterName : configurable.getParametersMap().keySet()) {
            if (configurable.getParameterByName(parameterName) != null) {
                itemChield = xmlDoc.createElement("param");
                itemChield.setAttribute("name", parameterName);
                itemChield.setAttribute("value", configurable.getParameterByName(parameterName).toString());
                item.appendChild(itemChield);
            }
        }
    }
}
