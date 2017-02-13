/*
 * Nariman Safiulin (woofilee)
 * File: Walk.java
 * Created on: Feb 12, 2017
 */

package ru.ifmo.ctddev.safiulin.walk;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.file.*;

/**
 * Simple file system walker.
 * 
 * Calculates FNV hash for each file, listed in the input file, And
 * stores it in the output file.
 * All directories will be recursively walked.
 */
public class Walker {
    /**
     * Default FNV hash
     */
    int DEFAULT_HASH = 0x811c9dc5;

    /**
     * Hash for the invalid files
     */
    int ERROR_HASH = 0x00000000;

    /**
     * Prime number for the FNV hash
     */
    int FNV_PRIME = 0x01000193;

    /**
     * Buffer size for the hash proccess, in bytes
     */
    int BUFFER_SIZE = 8192;

    /**
     * Input file
     */
    BufferedReader input;

    /**
     * Output file
     */
    BufferedWriter output;

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            Stream.of(
                "Please, provide an required arguments:",
                " - input file  - file path with the list of filenames/directories for hash calculating",
                " - output file - file path, where result of hash calculating will be stored, for each file in the input file",
                "",
                "Format:",
                ">> java Walk <input file> <output file>",
                "",
                "For example:",
                ">> java Walk ./tests/input.txt ./tests/output.txt"
            ).forEach(System.out::println);

            return;
        }

        new Walker(Paths.get(args[0]), Paths.get(args[1]));
    }

    public Walker(Path input, Path output) {        
        try {
            this.input = Files.newBufferedReader(input);
        } catch (IOException e) {
            System.err.println(
                "Can't open the input file. Please, check the file path correctness.");
            return;
        } catch (SecurityException e) {
            System.err.println("Can't open the input file. Please, check the file availability.");
            return;
        }

        try {
            this.output = Files.newBufferedWriter(output);
        } catch (IOException e) {
            System.err.println(
                "Can't create the output file. Please, check the file path correctness.");
            return;
        } catch (SecurityException e) {
            System.err.println(
                "Can't create the output file. Please, check the path availability.");
            return;
        }

        
        try {
            Iterator<String> lines = this.input.lines().iterator();

            while (lines.hasNext()) {
                String line = lines.next();
                Iterator<Path> paths;

                try {
                    paths = Files.walk(Paths.get(line))
                                 .filter(Files::isRegularFile)
                                 .iterator();
                } catch (IOException | SecurityException e) {
                    System.err.println(String.format("Can't open a path for hashing: %s", line));

                    this.output.write(String.format("%08x %s\n", ERROR_HASH, line));
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
                        System.err.println(
                            String.format("Can't read a file for hashing: %s", path.toString()));
                        hash = ERROR_HASH;
                    }

                    this.output.write(String.format("%08x %s\n", hash, path.toString()));
                }
            }
        } catch (IOException e) {
            System.err.println("Can't write to output file...");
            return;
        }

        try {
            this.output.close();
            this.input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
