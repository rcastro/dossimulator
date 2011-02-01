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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.entity.EntityPropertyManager;
import br.upe.ecomp.doss.core.exception.InfraException;
import br.upe.ecomp.doss.measurement.Measurement;
import br.upe.ecomp.doss.problem.Problem;
import br.upe.ecomp.doss.stopCondition.StopCondition;

/**
 * Parses an {@link Algorithm} class to XML, saving all its settings.
 * 
 * @author Rodrigo Castro
 */
public final class AlgorithmXMLParser {

    private static final String DEFAULT_EXTENSION = ".xml";
    private static final String XML_VERSION = "1.0";
    private static final String XML_ENCODING = "UTF-8";

    private static final String CLASS = "class";
    private static final String PARAM = "param";

    private static final String MEASUREMENT = "measurement";
    private static final String MEASUREMENTS = "measurements";
    private static final String STOP_CONDITION = "stopCondition";
    private static final String STOP_CONDITIONS = "stopConditions";
    private static final String PROBLEM = "problem";
    private static final String ALGORITHM = "algorithm";

    private static final int INDENT_SIZE = 4;

    private AlgorithmXMLParser() {
    }

    /**
     * Generate the XML document based on the algorithm.
     * 
     * @param algorithm The algorithm that we want to save the configuration on a XML file.
     * @param filePath The path of the XML file.
     * @param fileName The name of the XML file.
     */
    public static void save(Algorithm algorithm, String filePath, String fileName) {
        Document xmlDoc;
        DocumentBuilder docBuilder;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactoryImpl.newInstance();
        try {
            docBuilder = dbFactory.newDocumentBuilder();
            xmlDoc = docBuilder.newDocument();
            xmlDoc.appendChild(xmlDoc.createElement("project"));

            addSimpleElememnt(xmlDoc, algorithm, ALGORITHM);
            addSimpleElememnt(xmlDoc, algorithm.getProblem(), PROBLEM);
            addComplexElement(xmlDoc, algorithm.getStopConditions(), STOP_CONDITIONS, STOP_CONDITION);
            addComplexElement(xmlDoc, algorithm.getMeasurements(), MEASUREMENTS, MEASUREMENT);

            saveFile(xmlDoc, filePath, fileName);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a XML document and returns the {@link Algorithm} configured based on the document.
     * 
     * @param filePath The path of the XML file.
     * @param fileName The name of the XML file.
     * @return The {@link Algorithm} configured based on the XML document.
     */
    public static Algorithm read(String filePath, String fileName) {
        DOMParser parser = new DOMParser();
        Document xmlDoc;
        if (!fileName.endsWith(DEFAULT_EXTENSION)) {
            fileName = fileName + DEFAULT_EXTENSION;
        }
        File xmlFile = new File(filePath, fileName);
        FileReader reader = null;
        Algorithm algorithm = null;
        try {
            reader = new FileReader(xmlFile);
            parser.parse(new InputSource(reader));
            xmlDoc = parser.getDocument();

            algorithm = readAlgorithmElement(xmlDoc.getDocumentElement());
            algorithm.setProblem(readProblemElement(xmlDoc.getDocumentElement()));
            algorithm.setStopConditions(readStopConditionsElement(xmlDoc.getDocumentElement()));
            algorithm.setMeasurements(readMeasurementsElement(xmlDoc.getDocumentElement()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return algorithm;
    }

    /**
     * Saves the generated XML to a file with the given name.
     * 
     * @param xmlDoc The {@link Document} that contains the XML generated.
     * @param filePath The path of the XML file.
     * @param fileName The name of the XML file.
     */
    private static void saveFile(Document xmlDoc, String filePath, String fileName) {
        OutputFormat format = new OutputFormat();
        format.setEncoding(XML_ENCODING);
        format.setVersion(XML_VERSION);
        format.setIndenting(true);
        format.setIndent(INDENT_SIZE);

        XMLSerializer serializer = new XMLSerializer();
        serializer.setOutputFormat(format);

        if (!fileName.endsWith(DEFAULT_EXTENSION)) {
            fileName = fileName + DEFAULT_EXTENSION;
        }

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(new File(filePath, fileName));
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

    private static void addSimpleElememnt(Document xmlDoc, Object entity, String elementName) {
        Element item = xmlDoc.createElement(elementName);
        item.setAttribute(CLASS, entity.getClass().getCanonicalName());

        addParameters(item, xmlDoc, entity);

        xmlDoc.getDocumentElement().appendChild(item);
    }

    private static void addComplexElement(Document xmlDoc, List<? extends Object> intitiesList, String elementName,
            String elementChieldName) {
        Element item = xmlDoc.createElement(elementName);

        Element itemChield;
        for (Object entitie : intitiesList) {
            itemChield = xmlDoc.createElement(elementChieldName);
            itemChield.setAttribute(CLASS, entitie.getClass().getCanonicalName());
            addParameters(itemChield, xmlDoc, entitie);
            item.appendChild(itemChield);
        }
        xmlDoc.getDocumentElement().appendChild(item);
    }

    private static void addParameters(Element item, Document xmlDoc, Object entity) {
        Element itemChield;
        for (String fieldName : EntityPropertyManager.getFieldsName(entity)) {
            if (EntityPropertyManager.getValue(entity, fieldName) != null) {
                itemChield = xmlDoc.createElement(PARAM);
                itemChield.setAttribute("name", fieldName);
                itemChield.setAttribute("value", EntityPropertyManager.getValueAsString(entity, fieldName));
                item.appendChild(itemChield);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static Algorithm readAlgorithmElement(Element rootElement) {
        Algorithm algorithm = null;

        validateAlgorithmElement(rootElement);

        Element algorithmNode = (Element) rootElement.getElementsByTagName(ALGORITHM).item(0);
        try {
            Class<? extends Algorithm> clazz = (Class<? extends Algorithm>) Class.forName(algorithmNode
                    .getAttribute(CLASS));
            algorithm = clazz.newInstance();

            if (algorithmNode.hasChildNodes()) {
                readParameters(algorithmNode, algorithm);
            }
        } catch (ClassNotFoundException e) {
            throw new InfraException("Could not find class " + algorithmNode.getAttribute(CLASS), e);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return algorithm;
    }

    @SuppressWarnings("unchecked")
    private static Problem readProblemElement(Element rootElement) {
        Problem problem = null;

        validateProblemElement(rootElement);

        Element problemNode = (Element) rootElement.getElementsByTagName(PROBLEM).item(0);
        try {
            Class<? extends Problem> clazz = (Class<? extends Problem>) Class.forName(problemNode.getAttribute(CLASS));
            problem = clazz.newInstance();

            if (problemNode.hasChildNodes()) {
                readParameters(problemNode, problem);
            }
        } catch (ClassNotFoundException e) {
            throw new InfraException("Could not fint class " + problemNode.getAttribute(CLASS), e);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return problem;
    }

    @SuppressWarnings("unchecked")
    private static List<StopCondition> readStopConditionsElement(Element rootElement) {
        List<StopCondition> stopConditions = new ArrayList<StopCondition>();
        StopCondition stopCondition = null;

        validateStopConditionsElement(rootElement);

        NodeList nodes = rootElement.getElementsByTagName(STOP_CONDITION);
        int nodesLength = nodes.getLength();
        Element stopConditionNode;
        for (int i = 0; i < nodesLength; i++) {
            stopConditionNode = (Element) nodes.item(i);
            try {
                Class<? extends StopCondition> clazz = (Class<? extends StopCondition>) Class.forName(stopConditionNode
                        .getAttribute(CLASS));
                stopCondition = clazz.newInstance();

                if (stopConditionNode.hasChildNodes()) {
                    readParameters(stopConditionNode, stopCondition);
                }

                stopConditions.add(stopCondition);
            } catch (ClassNotFoundException e) {
                throw new InfraException("Could not fint class " + stopConditionNode.getAttribute(CLASS), e);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return stopConditions;
    }

    @SuppressWarnings("unchecked")
    private static List<Measurement> readMeasurementsElement(Element rootElement) {
        List<Measurement> measurements = new ArrayList<Measurement>();
        Measurement measurement = null;

        validateMeasurementElement(rootElement);

        NodeList nodes = rootElement.getElementsByTagName(MEASUREMENT);
        int nodesLength = nodes.getLength();
        Element measurementNode;
        for (int i = 0; i < nodesLength; i++) {
            measurementNode = (Element) nodes.item(i);
            try {
                Class<? extends Measurement> clazz = (Class<? extends Measurement>) Class.forName(measurementNode
                        .getAttribute(CLASS));
                measurement = clazz.newInstance();

                if (measurementNode.hasChildNodes()) {
                    readParameters(measurementNode, measurement);
                }

                measurements.add(measurement);
            } catch (ClassNotFoundException e) {
                throw new InfraException("Could not fint class " + measurementNode.getAttribute(CLASS), e);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return measurements;
    }

    private static void readParameters(Element element, Object entity) {
        NodeList nodes = element.getElementsByTagName(PARAM);
        int nodesLength = nodes.getLength();

        Element chield;
        for (int i = 0; i < nodesLength; i++) {
            chield = (Element) nodes.item(i);
            EntityPropertyManager.setValue(entity, chield.getAttribute("name"), chield.getAttribute("value"));
        }
    }

    private static void validateAlgorithmElement(Element rootElement) {
        if (rootElement.getElementsByTagName(ALGORITHM) == null) {
            throw new InfraException("No algorithm specified.");
        }
    }

    private static void validateProblemElement(Element rootElement) {
        if (rootElement.getElementsByTagName(PROBLEM) == null) {
            throw new InfraException("No problem specified.");
        }
    }

    private static void validateStopConditionsElement(Element rootElement) {
        if (rootElement.getElementsByTagName(STOP_CONDITIONS) == null) {
            throw new InfraException("No stop condition specified.");
        }
    }

    private static void validateMeasurementElement(Element rootElement) {
        if (rootElement.getElementsByTagName(MEASUREMENTS) == null) {
            throw new InfraException("No measurement specified.");
        }
    }
}
