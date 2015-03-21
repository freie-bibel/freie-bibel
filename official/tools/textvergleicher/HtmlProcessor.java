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
 * @file $/HtmlProcessor.java
 * @brief Processor to complete the output HTML files.
 * @author Stephan Kreutzer
 * @since 2011-09-09
 */



import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;



class HtmlProcessor
{
    public HtmlProcessor(String annotationDirectoryPath, String outDirectoryPath)
    {
        this.annotationDirectory = new File(annotationDirectoryPath);
        this.outDirectory = new File(outDirectoryPath);
    }

    public int run()
    {
        if (this.annotationDirectory.exists() != true)
        {
            return -1;
        }

        if (this.annotationDirectory.isDirectory() != true)
        {
            return -2;
        }

        if (this.annotationDirectory.canRead() != true)
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


        String htmlFiles[];
        htmlFiles = this.outDirectory.list();

        for (int i = 0; i < htmlFiles.length; i++)
        {
            if (htmlFiles[i].endsWith(".html") != true)
            {
                continue;
            }

            System.out.print(htmlFiles[i] + " ");

            try
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                                                       new FileInputStream(this.outDirectory + "/" +
                                                                           htmlFiles[i]), "UTF8"));

                StringBuilder inContent = new StringBuilder();
                String input = "";

                while ((input = in.readLine()) != null)
                {
                    inContent.append(input + "\n");
                }

                in.close();


                BufferedWriter out = new BufferedWriter(
                                     new OutputStreamWriter(
                                     new FileOutputStream(this.outDirectory + "/" +
                                                          htmlFiles[i]), "UTF8"));

                out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                out.write("<!DOCTYPE html\n");
                out.write("    PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n");
                out.write("    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n\n\n");
                out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n\n\n");
                out.write("  <head>\n\n\n");
                out.write("      <title>Textvergleichung</title>\n\n");
                out.write("      <meta name=\"description\" content=\"Textvergleichung.\" />\n");
                out.write("      <meta name=\"generator\" content=\"textvergleicher (http://www.freie-bibel.de)\" />\n");
                out.write("      <meta name=\"keywords\" content=\"Textvergleichung\" />\n");
                out.write("      <meta http-equiv=\"expires\" content=\"1296000\" />\n");
                out.write("      <meta http-equiv=\"content-type\" content=\"application/xhtml+xml; charset=UTF-8\" />\n\n\n");
                out.write("  </head>\n\n\n");
                out.write("  <body>\n\n\n");
                out.write("      <div>\n");
                out.write("        <h1>Textvergleichung</h1>\n");
                out.write("        <div>\n");
                out.write("          Zur <a href=\"index.html\">\u00DCbersicht</a>.\n");
                out.write("        </div>\n");
                out.write("        <table border=\"1\">\n");
                out.write(inContent.toString());
                out.write("        </table>\n");

                if (new File(this.annotationDirectory + "/" + htmlFiles[i]).exists() == true)
                {
                    out.write("        <p>\n");

                    BufferedReader inAnnotations = new BufferedReader(new InputStreamReader(
                                                                      new FileInputStream(this.annotationDirectory + "/" +
                                                                                          htmlFiles[i]), "UTF8"));

                    String annotation = "";

                    while ((annotation = inAnnotations.readLine()) != null)
                    {
                        out.write(annotation + "\n");
                    }

                    inAnnotations.close();

                    out.write("        </p>\n");
                }

                out.write("        <div>\n");
                out.write("          Zur <a href=\"index.html\">\u00DCbersicht</a>.\n");
                out.write("        </div>\n");
                out.write("      </div>\n\n\n");
                out.write("  </body>\n\n\n");
                out.write("</html>\n");

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
        }

        return 0;
    }

    private File annotationDirectory;
    private File outDirectory;
}