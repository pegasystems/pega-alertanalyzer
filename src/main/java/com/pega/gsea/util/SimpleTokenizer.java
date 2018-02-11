package com.pega.gsea.util;

import java.util.ArrayList;

/**
 * A class that tokenizes a string on a char delimiter.  This is meant to
 * be a substitute for the java.util.StringTokenizer, which has the property
 * of grouping consecutive delimiters together as if they were a single delimiter.
 * <p>This class treats consecutive delimiter chars as if they have an empty token
 * ("") between them.
 * <p>Important differences between this class and StringTokenizer:
 * <ul>
 * <li>This class does not support the return of delim chars.
 * <li>The delimiter is a single char, not a string of delim chars.
 * <li>The number of tokens returned will always be one more than the number of delim
 * chars present.
 * <li>This class does not recognize "comments"; the string is split on any instance
 * of the delimiter char, regardless of whether or not it is wrapped in quotes.
 * </ul>
 * @author Pat Reaney, Nov 2001
 * @version 1.0
 */
public class SimpleTokenizer {

    private String string;
    private char delim;

    private int currPos = 0;
    private int maxPos;

    private String[] tokens;


    /**
     * Constructs a tokenizer for the specified string, splitting it on the
     * specified delim char.
     *
     * @param str   The string to be split
     * @param delim The char to split the string on.
     */
    public SimpleTokenizer(String str, char delim) {
        this.string = str;
        this.delim = delim;
        if (str != null) {
            maxPos = str.length();
            tokenize();
        }
    }

    /**
     * Main method for stand-alone testings.
     */
    public static void main(String[] args) {
        String str2 = "James\tIan\tKate\tBen\t\tHelen\tSimon";
        SimpleTokenizer stok2 = new SimpleTokenizer(str2, '	');

        System.out.println("Tokens in str2 = " + stok2.countTokens());
        while (stok2.hasMoreTokens()) {
            System.out.println("*" + stok2.nextToken() + "*");
        }
    }

    /**
     * Tests if there are more tokens available from this tokenizer's string.
     *
     * @return true if and only if there is at least one token in the string after the current position; false otherwise.
     */
    public boolean hasMoreTokens() {
        return (tokens != null && tokens.length > currPos);
    }

    /**
     * Returns the next token from this object's String.
     *
     * @return the next token from this object's String.
     */
    public String nextToken() {
        String str = tokens[currPos];
        currPos++;
        return str;
    }

    /**
     * Splits the string on the delim char. The tokens are placed into
     * a String array.
     */
    private void tokenize() {
        int curr = 0;
        int max = maxPos;
        int next = 0;
        ArrayList list = new ArrayList();
        String token = "";
        while (next >= 0) {
            next = string.indexOf(delim, curr);
            if (next == curr) { // first char was a delim
                token = "";
                curr = curr + 1;
            } else if (next > curr) {
                token = string.substring(curr, next);
                curr = next + 1;
            } else if (next < 0) { // delim not found
                token = string.substring(curr, max);
            }

            //System.out.println( "token was *" + token +"*" );
            list.add(token);
        }
        tokens = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            tokens[i] = (String) list.get(i);
        }
    }

    /**
     * Returns the number of tokens produced from this object's
     * string and delimiter.
     *
     * @return The number of tokens.
     */
    public int countTokens() {
        int size = 0;
        if (tokens != null) {
            size = tokens.length;
        }
        return size;
    }
}

