package com.m1miageprojet.appclient;

import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Client 
{
	private final String FINISH = "" + (char) 4;
	private final String hote;
	private final int port;
	private final String id;

	public Client(String hote, int port, String mon_id) 
	{
		this.hote = hote;
		this.port = port;
		this.id = mon_id;
	}

	public void execute() 
	{
		Socket la_connection = null;
		try 
		{
			la_connection = new Socket(this.hote, this.port);
			BufferedReader bufferClient = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter ma_sortie = new PrintWriter(la_connection.getOutputStream());
			System.out.println(" Connexion " + this.hote + " sur " + this.port + " établie !");
			String inputLine = "Hello je suis :" + this.id;
			ma_sortie.println(inputLine);
			System.out.println(inputLine);
			ma_sortie.flush();
			boolean co = true;
			while(co && (inputLine = bufferClient.readLine()) != null)
			{
				ma_sortie.println(id + " : " + inputLine);
				System.out.println(id + " : " + inputLine);
				ma_sortie.flush();
				if(inputLine.equals("FINISH"))
				{
					co = false;
				}
			}
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				if (la_connection != null) 
				{
					la_connection.close();
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) 
	{
		if (args.length != 3) 
		{
			System.err.println("utilisation: java " + Client.class.getCanonicalName() + " hote port identifiant");
			System.exit(1);
		}

		try 
		{
			new Client(args[0], Integer.parseInt(args[1]), args[2]).execute();
		} 
		catch (NumberFormatException e) 
		{
			System.out.println("Format du port incorrect :  format exception for " + e.getMessage());
			System.exit(-1);
		}
	}

}
