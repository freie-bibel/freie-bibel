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
 * @file $/hag2latex7.java
 * @brief The main module that delegates to the specific processors.
 * @author Stephan Kreutzer
 * @since 2013-03-27
 */



public class hag2latex7
{
    public static void main(String args[])
    {
        System.out.print("hag2latex7  Copyright (C) 2013  Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the file COPYING for details.\n\n");

        if (args.length != 3)
        {
            System.out.print("Usage:\n" +
                             "\thag2latex7 haggai-file-left haggai-file-right latex-file-out\n\n");

            return;
        }

        ParallelProcessor latex = new ParallelProcessor(args[0], args[1], args[2]);

        int result = latex.run();

        if (result != 0)
        {
            System.err.println("hag2latex7: Problem " + result + " with processing.");
            return;
        }
    }
}