package com.firegodjr.ancientlanguage.output;

import com.firegodjr.ancientlanguage.Main;

public class ModOutput {
	
	public static void println(String out)
	{
		if(Main.verbose)
		{
			System.out.println(out);
		}
	}
	
	public static void print(String out)
	{
		if(Main.verbose)
		{
			System.out.print(out);
		}
	}
}
