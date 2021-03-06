/* Copyright (C) 2013  Stephan Kreutzer
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
 * @file $/ParallelProcessor.java
 * @brief Processor to generate LaTeX parallel bible output from Haggai XML.
 * @author Stephan Kreutzer
 * @since 2013-03-27
 */



import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import javax.xml.stream.XMLInputFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;



class ParallelProcessor
{
    public ParallelProcessor(String haggaiFileLhs, String haggaiFileRhs, String latexFileOut)
    {
        this.haggaiFileLhs = new File(haggaiFileLhs);
        this.haggaiFileRhs = new File(haggaiFileRhs);
        this.latexFileOut = latexFileOut;
    }

    public int run()
    {
        if (this.haggaiFileLhs.exists() != true)
        {
            return -1;
        }

        if (this.haggaiFileLhs.isFile() != true)
        {
            return -2;
        }

        if (this.haggaiFileLhs.canRead() != true)
        {
            return -3;
        }

        if (this.haggaiFileRhs.exists() != true)
        {
            return -4;
        }

        if (this.haggaiFileRhs.isFile() != true)
        {
            return -5;
        }

        if (this.haggaiFileRhs.canRead() != true)
        {
            return -6;
        }


        try
        {
            BufferedWriter out = new BufferedWriter(
                                 new OutputStreamWriter(
                                 new FileOutputStream(latexFileOut), "UTF8"));

            out.write("\\documentclass[a4paper]{article}\n");
            out.write("% This file was generated by hag2latex7 (http://www.freie-bibel.de).\n");
            out.write("\n");
            out.write("\\usepackage[utf8]{inputenc}\n");
            out.write("\\usepackage[left=1.5cm,right=1.5cm,top=1.5cm,bottom=1.5cm,includeheadfoot]{geometry}\n");
            out.write("\\usepackage{ngerman}\n");
            out.write("\\usepackage{lettrine}\n");
            out.write("\\usepackage[SeparatedFootnotes]{parallel}\n");
            out.write("\\usepackage{fancyhdr}\n");
            out.write("\n");
            out.write("\\setlength{\\parskip}{0pt}\n");
            out.write("\n");
            out.write("\\pagestyle{fancy}\n");
            out.write("\\fancyhead{}\n");
            out.write("\\fancyhead[L]{\\leftmark}\n");
            out.write("\\fancyhead[R]{\\rightmark}\n");
            out.write("\\setlength{\\headsep}{4.6pt}\n");
            out.write("\\renewcommand{\\headrulewidth}{0.5pt}\n");
            out.write("\n");
            out.write("\\begin{document}\n");


            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream inLhs = new FileInputStream(haggaiFileLhs);
            InputStream inRhs = new FileInputStream(haggaiFileRhs);
            XMLEventReader eventReaderLhs = inputFactory.createXMLEventReader(inLhs, "UTF8");
            XMLEventReader eventReaderRhs = inputFactory.createXMLEventReader(inRhs, "UTF8");

            XMLEvent event = null;
            String bookNameLhs = "";
            String bookNameRhs = "";
            int bookNumberLhs = 0;
            int chapterNumberLhs = 0;
            int verseNumberLhs = 0;
            int bookNumberRhs = 0;
            int chapterNumberRhs = 0;
            int verseNumberRhs = 0;
            boolean newBook = false;
            boolean newChapter = false;

            while (eventReaderLhs.hasNext() == true)
            {
                event = eventReaderLhs.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equalsIgnoreCase("biblebook") == true)
                    {
                        Attribute bnumber = event.asStartElement().getAttributeByName(new QName("bnumber"));
                        bookNumberLhs = Integer.parseInt(bnumber.getValue());
                        Attribute bname = event.asStartElement().getAttributeByName(new QName("bname"));
                        bookNameLhs = bname.getValue();
                        newBook = true;
                    }
                    else if (tagName.equalsIgnoreCase("chapter") == true)
                    {
                        Attribute cnumber = event.asStartElement().getAttributeByName(new QName("cnumber"));
                        chapterNumberLhs = Integer.parseInt(cnumber.getValue());
                        newChapter = true;
                    }
                    else if (tagName.equalsIgnoreCase("verse") == true)
                    {
                        Attribute vnumber = event.asStartElement().getAttributeByName(new QName("vnumber"));
                        verseNumberLhs = Integer.parseInt(vnumber.getValue());
                    }

                    if (bookNumberLhs > 0 &&
                        chapterNumberLhs > 0 &&
                        verseNumberLhs > 0)
                    {
                        break;
                    }
                }
            }

            while (eventReaderRhs.hasNext() == true)
            {
                event = eventReaderRhs.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equalsIgnoreCase("biblebook") == true)
                    {
                        Attribute bnumber = event.asStartElement().getAttributeByName(new QName("bnumber"));
                        bookNumberRhs = Integer.parseInt(bnumber.getValue());
                        Attribute bname = event.asStartElement().getAttributeByName(new QName("bname"));
                        bookNameRhs = bname.getValue();
                        newBook = true;
                    }
                    else if (tagName.equalsIgnoreCase("chapter") == true)
                    {
                        Attribute cnumber = event.asStartElement().getAttributeByName(new QName("cnumber"));
                        chapterNumberRhs = Integer.parseInt(cnumber.getValue());
                        newChapter = true;
                    }
                    else if (tagName.equalsIgnoreCase("verse") == true)
                    {
                        Attribute vnumber = event.asStartElement().getAttributeByName(new QName("vnumber"));
                        verseNumberRhs = Integer.parseInt(vnumber.getValue());
                    }

                    if (bookNumberRhs > 0 &&
                        chapterNumberRhs > 0 &&
                        verseNumberRhs > 0)
                    {
                        break;
                    }
                }
            }


            if (bookNumberLhs == 0 ||
                chapterNumberLhs == 0 ||
                verseNumberLhs == 0 ||
                bookNumberRhs == 0 ||
                chapterNumberRhs == 0 ||
                verseNumberRhs == 0)
            {
                return 1;
            }

            System.out.print("Processing ");

            boolean done = false;

            while (done != true)
            {
                if (bookNumberLhs < bookNumberRhs)
                {
                    chapterNumberLhs = 0;
                    verseNumberLhs = 0;

                    while (eventReaderLhs.hasNext() == true)
                    {
                        event = eventReaderLhs.nextEvent();

                        if (event.isStartElement() == true)
                        {
                            String tagName = event.asStartElement().getName().getLocalPart();

                            if (tagName.equalsIgnoreCase("biblebook") == true)
                            {
                                Attribute bnumber = event.asStartElement().getAttributeByName(new QName("bnumber"));
                                bookNumberLhs = Integer.parseInt(bnumber.getValue());
                                Attribute bname = event.asStartElement().getAttributeByName(new QName("bname"));
                                bookNameLhs = bname.getValue();
                                newBook = true;
                                break;
                            }
                        }
                    }

                    if (eventReaderLhs.hasNext() != true)
                    {
                        System.out.print("end.\n");
                        break;
                    }
                }
                else if (bookNumberLhs > bookNumberRhs)
                {
                    chapterNumberRhs = 0;
                    verseNumberRhs = 0;

                    while (eventReaderRhs.hasNext() == true)
                    {
                        event = eventReaderRhs.nextEvent();

                        if (event.isStartElement() == true)
                        {
                            String tagName = event.asStartElement().getName().getLocalPart();

                            if (tagName.equalsIgnoreCase("biblebook") == true)
                            {
                                Attribute bnumber = event.asStartElement().getAttributeByName(new QName("bnumber"));
                                bookNumberRhs = Integer.parseInt(bnumber.getValue());
                                Attribute bname = event.asStartElement().getAttributeByName(new QName("bname"));
                                bookNameRhs = bname.getValue();
                                newBook = true;
                                break;
                            }
                        }
                    }

                    if (eventReaderRhs.hasNext() != true)
                    {
                        System.out.print("end.\n");
                        break;
                    }
                }
                else if (bookNumberLhs == bookNumberRhs)
                {
                    if (chapterNumberLhs < chapterNumberRhs || chapterNumberLhs == 0)
                    {
                        while (eventReaderLhs.hasNext() == true)
                        {
                            event = eventReaderLhs.nextEvent();

                            if (event.isStartElement() == true)
                            {
                                String tagName = event.asStartElement().getName().getLocalPart();

                                if (tagName.equalsIgnoreCase("biblebook") == true)
                                {
                                    Attribute bnumber = event.asStartElement().getAttributeByName(new QName("bnumber"));
                                    bookNumberLhs = Integer.parseInt(bnumber.getValue());
                                    Attribute bname = event.asStartElement().getAttributeByName(new QName("bname"));
                                    bookNameLhs = bname.getValue();
                                    chapterNumberLhs = 0;
                                    verseNumberLhs = 0;
                                    bookNumberRhs = 0;
                                    chapterNumberRhs = 0;
                                    verseNumberRhs = 0;
                                    newBook = true;
                                    break;
                                }
                                if (tagName.equalsIgnoreCase("chapter") == true)
                                {
                                    Attribute cnumber = event.asStartElement().getAttributeByName(new QName("cnumber"));
                                    chapterNumberLhs = Integer.parseInt(cnumber.getValue());
                                    verseNumberLhs = 0;
                                    newChapter = true;
                                    break;
                                }
                            }
                        }

                        if (eventReaderLhs.hasNext() != true)
                        {
                            System.out.print("end.\n");
                            break;
                        }
                    }
                    else if (chapterNumberLhs > chapterNumberRhs)
                    {
                        while (eventReaderRhs.hasNext() == true)
                        {
                            event = eventReaderRhs.nextEvent();

                            if (event.isStartElement() == true)
                            {
                                String tagName = event.asStartElement().getName().getLocalPart();

                                if (tagName.equalsIgnoreCase("biblebook") == true)
                                {
                                    Attribute bnumber = event.asStartElement().getAttributeByName(new QName("bnumber"));
                                    bookNumberRhs = Integer.parseInt(bnumber.getValue());
                                    Attribute bname = event.asStartElement().getAttributeByName(new QName("bname"));
                                    bookNameRhs = bname.getValue();
                                    chapterNumberRhs = 0;
                                    verseNumberRhs = 0;
                                    bookNumberLhs = 0;
                                    chapterNumberLhs = 0;
                                    verseNumberLhs = 0;
                                    newBook = true;
                                    break;
                                }
                                if (tagName.equalsIgnoreCase("chapter") == true)
                                {
                                    Attribute cnumber = event.asStartElement().getAttributeByName(new QName("cnumber"));
                                    chapterNumberRhs = Integer.parseInt(cnumber.getValue());
                                    verseNumberRhs = 0;
                                    newChapter = true;
                                    break;
                                }
                            }
                        }

                        if (eventReaderRhs.hasNext() != true)
                        {
                            System.out.print("end.\n");
                            break;
                        }
                    }
                    else if (chapterNumberLhs == chapterNumberRhs)
                    {
                        if (verseNumberLhs < verseNumberRhs || verseNumberLhs == 0)
                        {
                            while (eventReaderLhs.hasNext() == true)
                            {
                                event = eventReaderLhs.nextEvent();

                                if (event.isStartElement() == true)
                                {
                                    String tagName = event.asStartElement().getName().getLocalPart();

                                    if (tagName.equalsIgnoreCase("biblebook") == true)
                                    {
                                        Attribute bnumber = event.asStartElement().getAttributeByName(new QName("bnumber"));
                                        bookNumberLhs = Integer.parseInt(bnumber.getValue());
                                        Attribute bname = event.asStartElement().getAttributeByName(new QName("bname"));
                                        bookNameLhs = bname.getValue();
                                        chapterNumberLhs = 0;
                                        verseNumberLhs = 0;
                                        bookNumberRhs = 0;
                                        chapterNumberRhs = 0;
                                        verseNumberRhs = 0;
                                        newBook = true;
                                        break;
                                    }
                                    if (tagName.equalsIgnoreCase("chapter") == true)
                                    {
                                        Attribute cnumber = event.asStartElement().getAttributeByName(new QName("cnumber"));
                                        chapterNumberLhs = Integer.parseInt(cnumber.getValue());
                                        verseNumberLhs = 0;
                                        chapterNumberRhs = 0;
                                        verseNumberRhs = 0;
                                        newChapter = true;
                                        break;
                                    }
                                    if (tagName.equalsIgnoreCase("verse") == true)
                                    {
                                        Attribute vnumber = event.asStartElement().getAttributeByName(new QName("vnumber"));
                                        verseNumberLhs = Integer.parseInt(vnumber.getValue());
                                        break;
                                    }
                                }
                            }

                            if (eventReaderLhs.hasNext() != true)
                            {
                                System.out.print("end.\n");
                                break;
                            }
                        }
                        else if (verseNumberLhs > verseNumberRhs)
                        {
                            while (eventReaderRhs.hasNext() == true)
                            {
                                event = eventReaderRhs.nextEvent();

                                if (event.isStartElement() == true)
                                {
                                    String tagName = event.asStartElement().getName().getLocalPart();

                                    if (tagName.equalsIgnoreCase("biblebook") == true)
                                    {
                                        Attribute bnumber = event.asStartElement().getAttributeByName(new QName("bnumber"));
                                        bookNumberRhs = Integer.parseInt(bnumber.getValue());
                                        Attribute bname = event.asStartElement().getAttributeByName(new QName("bname"));
                                        bookNameRhs = bname.getValue();
                                        chapterNumberRhs = 0;
                                        verseNumberRhs = 0;
                                        bookNumberLhs = 0;
                                        chapterNumberLhs = 0;
                                        verseNumberLhs = 0;
                                        newBook = true;
                                        break;
                                    }
                                    if (tagName.equalsIgnoreCase("chapter") == true)
                                    {
                                        Attribute cnumber = event.asStartElement().getAttributeByName(new QName("cnumber"));
                                        chapterNumberRhs = Integer.parseInt(cnumber.getValue());
                                        verseNumberRhs = 0;
                                        chapterNumberLhs = 0;
                                        verseNumberLhs = 0;
                                        newChapter = true;
                                        break;
                                    }
                                    if (tagName.equalsIgnoreCase("verse") == true)
                                    {
                                        Attribute vnumber = event.asStartElement().getAttributeByName(new QName("vnumber"));
                                        verseNumberRhs = Integer.parseInt(vnumber.getValue());
                                        break;
                                    }
                                }
                            }

                            if (eventReaderRhs.hasNext() != true)
                            {
                                System.out.print("end.\n");
                                break;
                            }
                        }
                        else if (verseNumberLhs == verseNumberRhs)
                        {
                            System.out.print(bookNumberLhs + ":" + chapterNumberLhs + ":" + verseNumberLhs + ", ");

                            if (newBook == true)
                            {
                                out.write("\\vspace{1cm}\n");
                                out.write("\\begin{Parallel}{}{}\n");
                                out.write("\\ParallelLText{\\centerline{\\textsc{\\large " + bookNameLhs + "}}}\\ParallelRText{\\centerline{\\textsc{\\large " + bookNameRhs + "}}}\n");
                                out.write("\\end{Parallel}\n");
                                out.write("\\vspace{0.25cm}\n");

                                newBook = false;
                            }

                            if (newChapter == true)
                            {
                                out.write("\\vspace{0.5cm}\n");
                                out.write("\\begin{Parallel}{}{}\n");
                                out.write("\\ParallelLText{Kapitel " + chapterNumberLhs + "}\\ParallelRText{Kapitel " + chapterNumberRhs + "}\n");
                                out.write("\\end{Parallel}\n");
                                out.write("\\vspace{0.5cm}\n");
                                out.write("\\begin{Parallel}[v]{}{}\n");
                                out.write("\\markboth{" + bookNameLhs + " " + chapterNumberLhs + "}{" + bookNameRhs + " " + chapterNumberRhs + "}");

                                newChapter = false;
                            }

                            out.write("\\ParallelLText{" + verseNumberLhs + "\\hspace{0.3em}\\textperiodcentered\\hspace{0.3em}");

                            String textBuffer = this.processText(eventReaderLhs, event);
                            out.write(textBuffer);

                            out.write("}\\ParallelRText{" + verseNumberRhs + "\\hspace{0.3em}\\textperiodcentered\\hspace{0.3em}");

                            textBuffer = this.processText(eventReaderRhs, event);

                            out.write(textBuffer);
                            out.write("}\\ParallelPar\n");

                            while (eventReaderLhs.hasNext() == true)
                            {
                                event = eventReaderLhs.nextEvent();

                                if (event.isStartElement() == true)
                                {
                                    String tagName = event.asStartElement().getName().getLocalPart();

                                    if (tagName.equalsIgnoreCase("biblebook") == true)
                                    {
                                        Attribute bnumber = event.asStartElement().getAttributeByName(new QName("bnumber"));
                                        bookNumberLhs = Integer.parseInt(bnumber.getValue());
                                        Attribute bname = event.asStartElement().getAttributeByName(new QName("bname"));
                                        bookNameLhs = bname.getValue();
                                        chapterNumberLhs = 0;
                                        verseNumberLhs = 0;
                                        bookNumberRhs = 0;
                                        chapterNumberRhs = 0;
                                        verseNumberRhs = 0;
                                        newBook = true;
                                        newChapter = true;

                                        out.write("\\end{Parallel}\n");

                                        break;
                                    }
                                    else if (tagName.equalsIgnoreCase("chapter") == true)
                                    {
                                        Attribute cnumber = event.asStartElement().getAttributeByName(new QName("cnumber"));
                                        chapterNumberLhs = Integer.parseInt(cnumber.getValue());
                                        verseNumberLhs = 0;
                                        chapterNumberRhs = 0;
                                        verseNumberRhs = 0;
                                        newChapter = true;

                                        out.write("\\end{Parallel}\n");

                                        break;
                                    }
                                    else if (tagName.equalsIgnoreCase("verse") == true)
                                    {
                                        Attribute vnumber = event.asStartElement().getAttributeByName(new QName("vnumber"));
                                        verseNumberLhs = Integer.parseInt(vnumber.getValue());
                                        verseNumberRhs = 0;
                                        break;
                                    }
                                }
                                else if (event.isEndDocument() == true)
                                {
                                    System.out.print("end.\n");
                                    done = true;
                                }
                            }
                        }
                    }
                }
            }

            out.write("\\end{Parallel}\n");
            out.write("\\pagebreak{}\n");
            out.write("\\end{document}\n");

            out.close();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            return -7;
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
            return -8;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return -9;
        }
        catch (XMLStreamException ex)
        {
            ex.printStackTrace();
            return 2;
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
                if (event.asStartElement().getName().getLocalPart().equalsIgnoreCase("style") == true)
                {
                    boolean handled = false;
                    Attribute fs = event.asStartElement().getAttributeByName(new QName("fs"));

                    if (fs != null)
                    {
                        String fixedStyle = fs.getValue();

                        if (fixedStyle.equals("emphasis") == true)
                        {
                            textBuffer += "\\textbf{" + this.processText(eventReader, event) + "}";
                            handled = true;
                        }
                        else if (fixedStyle.equals("super") == true)
                        {
                            textBuffer += "$\\langle$" + this.processText(eventReader, event) + "$\\rangle$";
                            handled = true;
                        }
                        else if (fixedStyle.equals("italic") == true)
                        {
                            textBuffer += "\\textit{" + this.processText(eventReader, event) + "}";
                            handled = true;
                        }
                    }

                    if (handled != true)
                    {
                        textBuffer += this.processText(eventReader, event);
                    }
                }
                else if (event.asStartElement().getName().getLocalPart().equalsIgnoreCase("note") == true)
                {
                    String footnote = this.processText(eventReader, event);

                    //textBuffer += "\\footnote{" + footnote + "}";
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

    private File haggaiFileLhs;
    private File haggaiFileRhs;
    private String latexFileOut;
}
