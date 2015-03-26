/* Copyright (C) 2015  Stephan Kreutzer
 *
 * This file is part of Freie Bibel.
 *
 * Freie Bibel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * Freie Bibel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with Freie Bibel. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/official/tools/offene_bibel/generate_snapshot1.java
 * @brief Generate a new snapshot of the Offene Bibel.
 * @details Calls the Offene Bibel converter, which will retrieve the Wiki
 *     text from the website and convert it to OSIS, from which the Free Scriptures
 *     tools will attempt to generate HTML, EPUB2 and PDF from it.
 * @author Stephan Kreutzer
 * @since 2015-03-17
 */



import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class generate_snapshot1
{
    public static void main(String args[])
    {
        System.out.print("generate_snapshot1 workflow  Copyright (C) 2015  Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public\n" +
                         "License 3 or any later version for details. Also, see the source code\n" +
                         "repository https://github.com/freie-bibel/official/\n" +
                         "and the project website http://www.freie-bibel.de.\n\n");

        String programPath = generate_snapshot1.class.getProtectionDomain().getCodeSource().getLocation().getFile();

        File cacheDirectory = new File(programPath + "free-offene-bibel-converter" + File.separator + "install" + File.separator + "tmp" + File.separator + "pageCache");

        // This will trigger that the Wiki text gets retrieved from the Offene Bibel
        // website.
        generate_snapshot1.DeleteFileRecursively(cacheDirectory);
        
        // As parent directories must exist for File.mkdir(), this is also a check
        // if those parent directories are in place as expected.
        if (cacheDirectory.mkdir() != true)
        {
            System.out.println("generate_snapshot1 workflow: Couldn't create directory '" + cacheDirectory.getAbsolutePath() + "'.");
            System.exit(-1);
        }

        // --lineGroupUnmilestoned is necessary for valid OSIS (see http://www.crosswire.org/wiki/OSIS_211_CR#milestoned_.3Clg.3E).
        ProcessBuilder builder = new ProcessBuilder("java", "-cp", programPath + "free-offene-bibel-converter" + File.separator + "install" + File.separator + "lib" + File.separator + "Parser-0.0.1-SNAPSHOT.jar", "offeneBibel.osisExporter.Exporter", "--continueOnError", "--skipGenerateWeb", "--lineGroupUnmilestoned");
        builder.directory(new File(programPath));
        builder.redirectErrorStream(true);

        try
        {
            Process process = builder.start();
            Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");

            while (scanner.hasNext() == true)
            {
                String line = scanner.next();
                System.out.println(line);
            }

            scanner.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        File lesefassungFile = new File(programPath + "free-offene-bibel-converter" + File.separator + "install" + File.separator + "results" + File.separator + "offeneBibelLesefassungModule.osis");

        if (lesefassungFile.exists() != true)
        {
            System.out.print("generate_snapshot1 workflow: OSIS file '" + lesefassungFile.getAbsolutePath() + "' doesn't exist, but should by now.\n");
            System.exit(-1);
        }

        if (lesefassungFile.isFile() != true)
        {
            System.out.print("generate_snapshot1 workflow: Path '" + lesefassungFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (lesefassungFile.canRead() != true)
        {
            System.out.print("generate_snapshot1 workflow: OSIS file '" + lesefassungFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        File studienfassungFile = new File(programPath + "free-offene-bibel-converter" + File.separator + "install" + File.separator + "results" + File.separator + "offeneBibelStudienfassungModule.osis");

        if (studienfassungFile.exists() != true)
        {
            System.out.print("generate_snapshot1 workflow: OSIS file '" + studienfassungFile.getAbsolutePath() + "' doesn't exist, but should by now.\n");
            System.exit(-1);
        }

        if (studienfassungFile.isFile() != true)
        {
            System.out.print("generate_snapshot1 workflow: Path '" + studienfassungFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (studienfassungFile.canRead() != true)
        {
            System.out.print("generate_snapshot1 workflow: OSIS file '" + studienfassungFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }
        
        File offeneBibelDirectory = new File(programPath + ".." + File.separator + ".." + File.separator + "bibel" + File.separator + "offene_bibel");

        if (offeneBibelDirectory.exists() != true)
        {
            System.out.print("generate_snapshot1 workflow: 'Offene Bibel' directory '" + offeneBibelDirectory.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (offeneBibelDirectory.isDirectory() != true)
        {
            System.out.print("generate_snapshot1 workflow: 'Offene Bibel' path '" + offeneBibelDirectory.getAbsolutePath() + "' isn't a directory.\n");
            System.exit(-1);
        }

        if (offeneBibelDirectory.canRead() != true)
        {
            System.out.print("generate_snapshot1 workflow: 'Offene Bibel' directory '" + offeneBibelDirectory.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }
        
        String now = "20150317T111613Z";

        {
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
            dateFormat.setTimeZone(timeZone);
            now = dateFormat.format(new Date());
        }
        
        File snapshotDirectory = new File(offeneBibelDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now);
        
        if (snapshotDirectory.exists() != true)
        {
            if (snapshotDirectory.mkdir() != true)
            {
                System.out.print("generate_snapshot1 workflow: Couldn't create result directory '" + snapshotDirectory.getAbsolutePath() + "'.");
                System.exit(-1);
            }
        }
        else
        {
            System.out.println("generate_snapshot1 workflow: Result path '" + snapshotDirectory.getAbsolutePath() + "' already exists, but shouldn't.");
            System.exit(-1);
        }

        CopyFileBinary(lesefassungFile, new File(snapshotDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now + "_lesefassung_osis.xml"));
        CopyFileBinary(studienfassungFile, new File(snapshotDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now + "_studienfassung_osis.xml"));


        File inofficialDirectory = new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "inofficial");
        
        if (inofficialDirectory.exists() != true)
        {
            System.out.print("generate_snapshot1 workflow: 'inofficial' directory '" + inofficialDirectory.getAbsolutePath() + "' doesn't exist, no additional output formats will be generated.\n");
            System.exit(-1);
        }

        if (inofficialDirectory.isDirectory() != true)
        {
            System.out.print("generate_snapshot1 workflow: 'inofficial' path '" + inofficialDirectory.getAbsolutePath() + "' isn't a directory, no additional output formats will be generated.\n");
            System.exit(-1);
        }

        if (inofficialDirectory.canRead() != true)
        {
            System.out.print("generate_snapshot1 workflow: 'inofficial' directory '" + inofficialDirectory.getAbsolutePath() + "' isn't readable, no additional output formats will be generated.\n");
            System.exit(-1);
        }
        
        inofficialDirectory = new File(inofficialDirectory.getAbsolutePath() + File.separator + "custom");
        
        if (inofficialDirectory.exists() != true)
        {
            if (inofficialDirectory.mkdir() != true)
            {
                System.out.println("generate_snapshot1 workflow: Couldn't create directory '" + inofficialDirectory.getAbsolutePath() + "'.");
                System.exit(-1);
            }
        }
        
        inofficialDirectory = new File(inofficialDirectory.getAbsolutePath() + File.separator + "offene_bibel" + File.separator + "offene_bibel_" + now);

        if (inofficialDirectory.exists() != true)
        {
            if (inofficialDirectory.mkdirs() != true)
            {
                System.out.print("generate_snapshot1 workflow: Couldn't create 'Offene Bibel' inofficial directory '" + inofficialDirectory.getAbsolutePath() + "'.");
                System.exit(-1);
            }
        }
        else
        {
            System.out.println("generate_snapshot1 workflow: 'Offene Bibel' inofficial path '" + inofficialDirectory.getAbsolutePath() + "' already exists, but shouldn't.");
            System.exit(-1);
        }

        builder = new ProcessBuilder("java",
                                     "osis2html1",
                                     snapshotDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now + "_lesefassung_osis.xml",
                                     programPath + ".." + File.separator + "free-scriptures" + File.separator + "tools" + File.separator + "haggai2html" + File.separator + "haggai2html1.xsl",
                                     inofficialDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now + "_lesefassung.html");
        builder.directory(new File(programPath + ".." + File.separator + "free-scriptures" + File.separator + "tools" + File.separator + "workflows" + File.separator + "osis2html1"));
        builder.redirectErrorStream(true);

        try
        {
            Process process = builder.start();
            Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");

            while (scanner.hasNext() == true)
            {
                String line = scanner.next();
                System.out.println(line);
            }

            scanner.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }

        builder = new ProcessBuilder("java",
                                     "osis2html1",
                                     snapshotDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now + "_studienfassung_osis.xml",
                                     programPath + ".." + File.separator + "free-scriptures" + File.separator + "tools" + File.separator + "haggai2html" + File.separator + "haggai2html1.xsl",
                                     inofficialDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now + "_studienfassung.html");
        builder.directory(new File(programPath + ".." + File.separator + "free-scriptures" + File.separator + "tools" + File.separator + "workflows" + File.separator + "osis2html1"));
        builder.redirectErrorStream(true);

        try
        {
            Process process = builder.start();
            Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");

            while (scanner.hasNext() == true)
            {
                String line = scanner.next();
                System.out.println(line);
            }

            scanner.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        builder = new ProcessBuilder("java",
                                     "osis2epub1",
                                     snapshotDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now + "_lesefassung_osis.xml",
                                     "html2epub1",
                                     programPath + ".." + File.separator + "free-scriptures" + File.separator + "tools" + File.separator + "haggai2epub" + File.separator + "haggai2epub1",
                                     inofficialDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now + "_lesefassung_epub2.epub");
        builder.directory(new File(programPath + ".." + File.separator + "free-scriptures" + File.separator + "tools" + File.separator + "workflows" + File.separator + "osis2epub1"));
        builder.redirectErrorStream(true);

        try
        {
            Process process = builder.start();
            Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");

            while (scanner.hasNext() == true)
            {
                String line = scanner.next();
                System.out.println(line);
            }

            scanner.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }

        builder = new ProcessBuilder("java",
                                     "osis2epub1",
                                     snapshotDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now + "_studienfassung_osis.xml",
                                     "html2epub1",
                                     programPath + ".." + File.separator + "free-scriptures" + File.separator + "tools" + File.separator + "haggai2epub" + File.separator + "haggai2epub1",
                                     inofficialDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now + "_studienfassung_epub2.epub");
        builder.directory(new File(programPath + ".." + File.separator + "free-scriptures" + File.separator + "tools" + File.separator + "workflows" + File.separator + "osis2epub1"));
        builder.redirectErrorStream(true);

        try
        {
            Process process = builder.start();
            Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");

            while (scanner.hasNext() == true)
            {
                String line = scanner.next();
                System.out.println(line);
            }

            scanner.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }


        builder = new ProcessBuilder("java",
                                     "osis2pdf1",
                                     snapshotDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now + "_lesefassung_osis.xml",
                                     "xelatex",
                                     programPath + ".." + File.separator + "free-scriptures" + File.separator + "tools" + File.separator + "haggai2latex" + File.separator + "haggai2xelatex1_variant1.xsl",
                                     programPath + "replacement_dictionary_lesefassung.xml",
                                     inofficialDirectory.getAbsolutePath());
        builder.directory(new File(programPath + ".." + File.separator + "free-scriptures" + File.separator + "tools" + File.separator + "workflows" + File.separator + "osis2pdf1"));
        builder.redirectErrorStream(true);

        try
        {
            Process process = builder.start();
            Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");

            while (scanner.hasNext() == true)
            {
                String line = scanner.next();
                System.out.println(line);
            }

            scanner.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }

        CopyFileBinary(new File(inofficialDirectory.getAbsolutePath() + File.separator + "result.tex"), new File(inofficialDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now + "_lesefassung_a4.tex"));
        CopyFileBinary(new File(inofficialDirectory.getAbsolutePath() + File.separator + "result.pdf"), new File(inofficialDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now + "_lesefassung_a4.pdf"));
        generate_snapshot1.DeleteFileRecursively(new File(inofficialDirectory.getAbsolutePath() + File.separator + "result.tex"));
        generate_snapshot1.DeleteFileRecursively(new File(inofficialDirectory.getAbsolutePath() + File.separator + "result.pdf"));

        builder = new ProcessBuilder("java",
                                     "osis2pdf1",
                                     snapshotDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now + "_studienfassung_osis.xml",
                                     "xelatex",
                                     programPath + ".." + File.separator + "free-scriptures" + File.separator + "tools" + File.separator + "haggai2latex" + File.separator + "haggai2xelatex1_variant1.xsl",
                                     programPath + "replacement_dictionary_studienfassung.xml",
                                     inofficialDirectory.getAbsolutePath());
        builder.directory(new File(programPath + ".." + File.separator + "free-scriptures" + File.separator + "tools" + File.separator + "workflows" + File.separator + "osis2pdf1"));
        builder.redirectErrorStream(true);

        try
        {
            Process process = builder.start();
            Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");

            while (scanner.hasNext() == true)
            {
                String line = scanner.next();
                System.out.println(line);
            }

            scanner.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        CopyFileBinary(new File(inofficialDirectory.getAbsolutePath() + File.separator + "result.tex"), new File(inofficialDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now + "_studienfassung_a4.tex"));
        CopyFileBinary(new File(inofficialDirectory.getAbsolutePath() + File.separator + "result.pdf"), new File(inofficialDirectory.getAbsolutePath() + File.separator + "offene_bibel_" + now + "_studienfassung_a4.pdf"));
        generate_snapshot1.DeleteFileRecursively(new File(inofficialDirectory.getAbsolutePath() + File.separator + "result.tex"));
        generate_snapshot1.DeleteFileRecursively(new File(inofficialDirectory.getAbsolutePath() + File.separator + "result.pdf"));
    }

    public static int DeleteFileRecursively(File file)
    {
        if (file.exists() != true)
        {
            System.out.println("generate_snapshot1 workflow: Nothing to delete at path '" + file.getAbsolutePath() + "'.");
            return 0;
        }
    
        if (file.isDirectory() == true)
        {
            for (File child : file.listFiles())
            {
                if (generate_snapshot1.DeleteFileRecursively(child) != 0)
                {
                    return -1;
                }
            }
        }
        
        if (file.delete() != true)
        {
            System.out.println("generate_snapshot1 workflow: Can't delete '" + file.getAbsolutePath() + "'.");
            return -1;
        }

        return 0;
    }

    public static int CopyFileBinary(File from, File to)
    {
        if (from.exists() != true)
        {
            System.out.println("generate_snapshot1 workflow: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' doesn't exist.");
            return -1;
        }
        
        if (from.isFile() != true)
        {
            System.out.println("generate_snapshot1 workflow: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't a file.");
            return -2;
        }
        
        if (from.canRead() != true)
        {
            System.out.println("generate_snapshot1 workflow: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't readable.");
            return -3;
        }
    
    
        byte[] buffer = new byte[1024];

        try
        {
            to.createNewFile();

            FileInputStream reader = new FileInputStream(from);
            FileOutputStream writer = new FileOutputStream(to);
            
            int bytesRead = reader.read(buffer, 0, buffer.length);
            
            while (bytesRead > 0)
            {
                writer.write(buffer, 0, bytesRead);
                bytesRead = reader.read(buffer, 0, buffer.length);
            }
            
            writer.close();
            reader.close();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
    
        return 0;
    }
}
