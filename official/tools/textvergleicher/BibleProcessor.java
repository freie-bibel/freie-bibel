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
 * @file $/BibleProcessor.java
 * @brief Processor to process the Haggai XML bible files.
 * @author Stephan Kreutzer
 * @since 2011-09-06
 */



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;



class BibleProcessor
{
    public BibleProcessor(String bibleFilePath, String outDirectoryPath, Map<String, String> bookMapping)
    {
        this.bibleFile = new File(bibleFilePath);
        this.outDirectory = new File(outDirectoryPath);
        this.bookMapping = bookMapping;
        this.footnoteStack = new ArrayList<String>();
    }

    public int run()
    {
        if (this.bibleFile.exists() != true)
        {
            return -1;
        }

        if (this.bibleFile.isFile() != true)
        {
            return -2;
        }

        if (this.bibleFile.canRead() != true)
        {
            return -3;
        }

        if (this.outDirectory.exists() != true)
        {
            return -4;
        }

        if (this.outDirectory.isDirectory() != true)
        {
            return -5;
        }

        if (this.outDirectory.canWrite() != true)
        {
            return -6;
        }


        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(bibleFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in, "UTF8");

            XMLEvent event = null;
            String bibleName = "";
            String bookNumber = "";
            String chapterNumber = "";
            String versNumber = "";
            String textBuffer = "";

            while (eventReader.hasNext() == true)
            {
                event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equalsIgnoreCase("title") == true)
                    {
                        event = eventReader.nextEvent();
                        bibleName = event.asCharacters().getData();
                    }
                    else if (tagName.equalsIgnoreCase("biblebook") == true)
                    {
                        Attribute bnumber = event.asStartElement().getAttributeByName(new QName("bnumber"));
                        bookNumber = bnumber.getValue();

                        System.out.print("\n\n" + bibleFile + ", #" + bookNumber + ":");
                    }
                    else if (tagName.equalsIgnoreCase("chapter") == true)
                    {
                        Attribute cnumber = event.asStartElement().getAttributeByName(new QName("cnumber"));
                        chapterNumber = cnumber.getValue();

                        System.out.print("\n" + chapterNumber);
                    }
                    else if (tagName.equalsIgnoreCase("verse") == true)
                    {
                        Attribute vnumber = event.asStartElement().getAttributeByName(new QName("vnumber"));
                        versNumber = vnumber.getValue();

                        textBuffer = this.processText(eventReader, event);

                        try
                        {
                            boolean outExists = new File(this.outDirectory + "/" +
                                                         bookNumber + "_" +
                                                         chapterNumber + "_" +
                                                         versNumber + ".html").exists();

                            BufferedWriter out = new BufferedWriter(
                                                 new OutputStreamWriter(
                                                 new FileOutputStream(this.outDirectory + "/" +
                                                                      bookNumber + "_" +
                                                                      chapterNumber + "_" +
                                                                      versNumber + ".html",
                                                                      true), "UTF8"));

                            if (outExists != true)
                            {
                                out.write("          <tr>\n");
                                out.write("            <th>\u00DCbersetzung</th>\n");
                                out.write("            <th>" + bookMapping.get(bookNumber) + " " + chapterNumber + "," + versNumber + "</th>\n");
                                out.write("            <th>Anmerkungen</th>\n");
                                out.write("          </tr>\n");
                            }

                            out.write("          <tr>\n");
                            out.write("            <td style=\"white-space:nowrap;\">" + bibleName + "</td>\n");
                            out.write("            <td style=\"white-space:nowrap;\">" + textBuffer + "</td>\n");
                            out.write("            <td style=\"white-space:nowrap;\">");

                            ListIterator<String> iter = footnoteStack.listIterator();
                            int i = 1;

                            while (iter.hasNext())
                            {
                                out.write("<sup>" + i + "</sup> " + iter.next() + " ");
                                i++;
                            }

                            footnoteStack.clear();

                            out.write("</td>\n");
                            out.write("          </tr>\n");

                            out.close();
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }

                        textBuffer = "";

                        System.out.print(".");
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

    public String processText(XMLEventReader eventReader, XMLEvent event) throws XMLStreamException
    {
        String textBuffer = "";
        String tagName = event.asStartElement().getName().getLocalPart();

        while (eventReader.hasNext() == true)
        {
            event = eventReader.nextEvent();

            if (event.isStartElement() == true)
            {
                if (event.asStartElement().getName().getLocalPart().equalsIgnoreCase("note") == true)
                {
                    String footnote = this.processText(eventReader, event);
                    int footnoteNumber = footnoteStack.indexOf(footnote);

                    if (footnoteNumber == -1)
                    {
                        footnoteStack.add(footnote);
                        textBuffer += "<sup>" + footnoteStack.size() + "</sup>";
                    }
                    else
                    {
                        textBuffer += "<sup>" + (footnoteNumber + 1) + "</sup>";
                    }
                }
                else if (event.asStartElement().getName().getLocalPart().equalsIgnoreCase("style") == true)
                {
                    boolean handled = false;
                    Attribute fs = event.asStartElement().getAttributeByName(new QName("fs"));

                    if (fs != null)
                    {
                        String fixedStyle = fs.getValue();

                        if (fixedStyle.equals("emphasis") == true)
                        {
                            textBuffer += "<b>" + this.processText(eventReader, event) + "</b>";
                            handled = true;
                        }
                        else if (fixedStyle.equals("super") == true)
                        {
                            textBuffer += "\u27E8" + this.processText(eventReader, event) + "\u27E9";
                            handled = true;
                        }
                        else if (fixedStyle.equals("italic") == true)
                        {
                            textBuffer += "<i>" + this.processText(eventReader, event) + "</i>";
                            handled = true;
                        }
                    }

                    if (handled != true)
                    {
                        textBuffer += this.processText(eventReader, event);
                    }
                }
                else
                {
                    textBuffer += this.processText(eventReader, event);
                }
            }
            else if (event.isCharacters() == true)
            {
                if (tagName.equalsIgnoreCase("verse") == true)
                {
                    textBuffer += event.asCharacters().getData();
                }
                else if (tagName.equalsIgnoreCase("style") == true)
                {
                    textBuffer += event.asCharacters().getData();
                }
                else if (tagName.equalsIgnoreCase("note") == true)
                {
                    textBuffer += event.asCharacters().getData();
                }
            }
            else if (event.isEndElement() == true)
            {
                break;
            }
        }

        return textBuffer;
    }

    private File bibleFile;
    private File outDirectory;
    private Map<String, String> bookMapping;
    private ArrayList<String> footnoteStack;
}
