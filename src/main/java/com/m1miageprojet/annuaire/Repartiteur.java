package com.m1miageprojet.annuaire;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Repartiteur 
{
	private ServerSocket serveur;
	ArrayList<Thread> threadsClients;
	
	public Repartiteur(int port) throws IOException
	{
		serveur = new ServerSocket(port);
		threadsClients = new ArrayList<Thread>();
	}
	
	public Socket attrendreClient() throws IOException
	{
		return serveur.accept();
	}
	
	public ArrayList<Thread> getThreadsClients()
	{
		return threadsClients;
	}

	public static void main(String[] args) 
	{
		try
		{
			int portNumber = Integer.parseInt(args[0]);
			Repartiteur servertest = new Repartiteur(portNumber);
			System.out.println("Serveur �coute "+ portNumber + "...");
			int cpt = 0;
			while(true)
			{
				Socket clientSocket = servertest.attrendreClient();
				servertest.getThreadsClients().add(new Thread(new ServiceClient(clientSocket)));
				servertest.getThreadsClients().get(cpt).start();
				cpt++;
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.out.println("Serveur en arr�t !");
		}
		catch(NumberFormatException nbe)
		{
			nbe.printStackTrace();
			System.out.println("Serveur en arr�t !");
		}
	}

}
