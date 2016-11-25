package com.m1miageprojet.appserveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServiceClient implements Runnable
{
	private Socket client;
	
	public ServiceClient(Socket client) throws IOException
	{
		this.client = client;
	}
	
	
	public void lireClient(BufferedReader bufferClient) throws IOException
	{
		int cpt = 1;
		String inputLine = bufferClient.readLine();
        System.out.println(inputLine);
        while ((inputLine = bufferClient.readLine()) != null) 
        {
        	System.out.println(inputLine); 
        	if (inputLine.equals("FINISH"))
            {
            	System.out.println("Client déconnecté !");
            	return;
            }  
        }
        System.out.println("Client déconnecté !");
	}

	@Override
	public void run() 
	{
		try
		{
			System.out.println("Client connecté !");
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			lireClient(in);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.out.println("Serveur en arrêt !");
		}
	}

}
