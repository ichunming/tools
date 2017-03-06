package com.ichunming.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	String reg = "@(\\S)+\\s";
        String content = "@nfisnd dfjaisjdi@dfid fasdifj";
        Matcher m = Pattern.compile(reg).matcher(content);
		while(m.find()) {
			System.out.println(m.start());
			System.out.println(m.end());
		}
    }
}
