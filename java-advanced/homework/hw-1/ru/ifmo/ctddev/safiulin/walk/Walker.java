/*
 * Nariman Safiulin (woofilee)
 * File: Walker.java
 * Created on: Feb 12, 2017
 */

package ru.ifmo.ctddev.safiulin.walk;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import java.nio.file.*;

/**
 * Simple file system walker.
 */
public class Walker {
    /**
     * Default FNV hash
     */
    public static final int DEFAULT_HASH = 0x811c9dc5;

    /**
     * Hash for the invalid files
     */
    public static final int ERROR_HASH = 0x00000000;

    /**
     * Prime number for the FNV hash
     */
    public static final int FNV_PRIME = 0x01000193;

    /**
     * Buffer size for the hash proccess, in bytes
     */
    private static final int BUFFER_SIZE = 8192;

    private Walker() {}

    /**
     * Calculates FNV hash for each file, listed in the input file, and
     * stores it in the output file.
     * All directories will be recursively walked.
     * 
     * @param is
     *        input file path
     * @param os
     *        output file path
     */
    public static void walk(Path is, Path os) {
        /**
         * Input file
         */
        BufferedReader input;

        /**
         * Output file
         */
        BufferedWriter output;

        try {
            input = Files.newBufferedReader(is);
        } catch (IOException | SecurityException e) {
            System.err.println("Can't open the input file. Please, check the file path " + 
                               "correctness and availability.");
            return;
        }

        try {
            output = Files.newBufferedWriter(os);
        } catch (IOException | SecurityException e) {
            System.err.println("Can't create the output file. Please, check the file path " + 
                               "correctness and availability.");
            return;
        }


        try {
            Iterator<String> lines = input.lines().iterator();

            try {
                while (lines.hasNext()) {
                    String line = lines.next();
                    Iterator<Path> paths;

                    try {
                        paths = Files.walk(Paths.get(line))
                                    .filter(Files::isRegularFile)
                                    .iterator();
                    } catch (IOException | SecurityException e) {
                        System.err.println(String.format("Can't open a path for hashing: %s", 
                                                         line));

                        output.write(String.format("%08x %s\n", ERROR_HASH, line));
                        continue;
                    }

                    while (paths.hasNext()) {
                        Path path = paths.next();
                        int hash = DEFAULT_HASH;

                        try {
                            BufferedInputStream in =
                                new BufferedInputStream(Files.newInputStream(path));

                            byte[] bytes = new byte[BUFFER_SIZE];
                            int length;

                            while ((length = in.read(bytes)) >= 0) {
                                for (int i = 0; i < length; i++) {
                                    hash *= FNV_PRIME;
                                    hash ^= bytes[i] & 0xFF;
                                }
                            }
                        } catch (IOException e) {
                            System.err.println(String.format("Can't read a file for hashing: %s", 
                                                             path.toString()));
                            hash = ERROR_HASH;
                        }

                        output.write(String.format("%08x %s\n", hash, path.toString()));
                    }
                }
            } catch (UncheckedIOException e) {
                System.err.println("Error during reading the input file...");
                return;
            }
        } catch (IOException e) {
            System.err.println("Can't write to the output file...");
            return;
        }


        try {
            output.close();
            input.close();
        } catch (IOException e) {
            // Stupid Java closing logic
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            Stream.of(
                "Please, provide an required arguments:",
                " - input filename  - file with the list of filenames/directories for hash " + 
                                                                                "calculating",
                " - output filename - file, where results of hash calculating will be stored",
                "",
                "Format:",
                ">> java Walk <input filename> <output filename>",
                "",
                "For example:",
                ">> java Walk ./tests/input.txt ./tests/output.txt"
            ).forEach(System.out::println);

            return;
        }

        try {
            Walker.walk(Paths.get(args[0]), Paths.get(args[1]));
        } catch (InvalidPathException e) {
            System.err.println("Can't recognise a paths in input and/or ouput file paths.");
        }
    }
}
