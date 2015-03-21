/* Copyright (C) 2011-2013  Stephan Kreutzer
 *
 * This file is part of Freie Bibel.
 *
 * Freie Bibel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * Freie Bibel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License 3 for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Freie Bibel. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/ConfigProcessor.java
 * @brief Processor to read the configuration file.
 * @author Stephan Kreutzer
 * @since 2011-09-05
 */



import java.io.File;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;



class ConfigProcessor
{
    public ConfigProcessor(String configFilePath)
    {
        this.configFile = new File(configFilePath);
        this.inFiles = new ArrayList<String>();
        this.bookMapping = new HashMap<String, String>();
    }

    public int run()
    {
        if (this.configFile.exists() != true)
        {
            return -1;
        }

        if (this.configFile.isFile() != true)
        {
            return -2;
        }

        if (this.configFile.canRead() != true)
        {
            return -3;
        }


        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(configFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            while (eventReader.hasNext() == true)
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equalsIgnoreCase("inFile") == true)
                    {
                        event = eventReader.nextEvent();
                        this.inFiles.add(event.asCharacters().getData());
                    }
                    else if (tagName.equalsIgnoreCase("annotationDirectory") == true)
                    {
                        event = eventReader.nextEvent();
                        this.annotationDirectory = event.asCharacters().getData();
                    }
                    else if (tagName.equalsIgnoreCase("outDirectory") == true)
                    {
                        event = eventReader.nextEvent();
                        this.outDirectory = event.asCharacters().getData();
                    }
                    else if (tagName.equalsIgnoreCase("mapping") == true)
                    {
                        Attribute bnumber = event.asStartElement().getAttributeByName(new QName("bnumber"));
                        String book = bnumber.getValue();

                        event = eventReader.nextEvent();
                        this.bookMapping.put(book, event.asCharacters().getData());
                    }
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            return -4;
        }
        catch (XMLStreamException ex)
        {
            ex.printStackTrace();
            return 1;
        }

        return 0;
    }

    public ArrayList<String> GetInFiles()
    {
        return this.inFiles;
    }

    public String GetAnnotationDirectory()
    {
        return this.annotationDirectory;
    }

    public String GetOutDirectory()
    {
        return this.outDirectory;
    }

    public Map<String, String> GetBookMapping()
    {
        return this.bookMapping;
    }

    private File configFile;
    private ArrayList<String> inFiles;
    private String annotationDirectory;
    private String outDirectory;
    private Map<String, String> bookMapping;
}