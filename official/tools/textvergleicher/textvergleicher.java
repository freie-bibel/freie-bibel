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
 * @file $/textvergleicher.java
 * @brief The main module that delegates to the specific processors.
 * @author Stephan Kreutzer
 * @since 2011-09-03
 */



import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;



public class textvergleicher
{
    public static void main(String args[])
    {
        System.out.print("textvergleicher  Copyright (C) 2011-2013  Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the file COPYING for details.\n\n");

        if (args.length != 1)
        {
            System.out.print("Usage:\n" +
                             "\ttextvergleicher config-file\n\n");

            return;
        }

        ConfigProcessor configuration = new ConfigProcessor(args[0]);

        if (configuration.run() != 0)
        {
            System.err.println("textvergleicher: Problem with configuration file " + args[0]);
            return;
        }

        ArrayList<String> inFiles = configuration.GetInFiles();
        ListIterator<String> iter = inFiles.listIterator();
        String outDirectory = configuration.GetOutDirectory();
        Map<String, String> bookMapping = configuration.GetBookMapping();

        while (iter.hasNext())
        {
            String bibleFile = iter.next();
            BibleProcessor bible = new BibleProcessor(bibleFile, outDirectory, bookMapping);

            if (bible.run() != 0)
            {
                System.err.println("textvergleicher: Problem with bible file " + bibleFile);
                return;
            }
        }

        System.out.print("\n\n\n");

        String annotationDirectory = configuration.GetAnnotationDirectory();

        HtmlProcessor html = new HtmlProcessor(annotationDirectory, outDirectory);

        if (html.run() != 0)
        {
            System.err.println("textvergleicher: Problem with HTML in out directory " + outDirectory);
            return;
        }
    }
}