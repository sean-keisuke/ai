package com.reuben.dtanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author reuben
 */
public class dtanner {

    public static void main(String[] args)
    {
        parseFile(Paths.get(args[0]));
    }

    private static void parseFile(Path paths)
    {
        int numBags = 0;
        int maxBagSpace = 0;
        List<Item> items = new LinkedList<>();
        try (BufferedReader reader = Files.newBufferedReader(paths, Charset.defaultCharset()))
        {
            String line = null;
            int lineCount = 0;
            while ((line = reader.readLine()) != null)
            {
                switch (lineCount)
                {
                    case 0:
                        try
                        {
                            numBags = Integer.parseInt(line.trim());
                        }
                        catch (NumberFormatException e)
                        {
                            System.err.println("File provided does not have correct form, please provide a new file");
                            System.exit(1);
                        }
                        break;
                    case 1:
                        try
                        {
                            maxBagSpace = Integer.parseInt(line.trim());
                        }
                        catch (NumberFormatException e)
                        {
                            System.err.println("File provided does not have correct form, please provide a new file");
                            System.exit(1);
                        }
                        break;
                    default:
                        items.add(parseItem(line.trim()));
                        break;
                }
                ++lineCount;
            }
        }
        catch (IOException e)
        {
            System.err.println("There was something wrong with the file provided, please provide a file with valid data.");
            System.exit(1);
        }
    }

    private static Item parseItem(String line)
    {
        String name = "";
        int weight = 0;
        char posneg = '\0';
        List<String> items = new LinkedList<>();

        StringTokenizer tok = new StringTokenizer(line, " ");
        String token = "";
        int tokCount = 0;
        while (tok.hasMoreTokens())
        {
            switch (tokCount)
            {
                case 0:
                    name = tok.nextToken();
                    break;
                case 1:
                    try
                    {
                        weight = Integer.parseInt(tok.nextToken());
                    }
                    catch (NumberFormatException e)
                    {
                        System.err.println("This item was not properly formed, please provide a file with valid items");
                        System.exit(1);
                    }
                    break;
                case 2:
                    posneg = tok.nextToken().charAt(0);
                    break;
                default:
                    items.add(tok.nextToken());
                    break;
            }
            ++tokCount;
        }
        return new Item(name, weight, posneg, items);
    }

    private static class ItemException extends Exception {

        public ItemException(String message)
        {
            super(message);
        }
    }
}
