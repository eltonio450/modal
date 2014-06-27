package Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import Stockage.Paquet;
import Utilitaires.Global;
import Utilitaires.Message;
import Utilitaires.Utilitaires;

/**
 * 
 * @author antoine
 * @param f : paquet dont on cherche les frères pour reconstruire le paquet manquant.
 * @param num : le numéro du paquet manquant
 */
public class taskRetablirPaquets implements Runnable {

	Paquet frere;
	int numeroMort;
	SocketChannel[] clientSocket = new SocketChannel[Global.NOMBRESOUSPAQUETS];
	ByteBuffer[] b = new ByteBuffer[Global.NOMBRESOUSPAQUETS];
	//ByteBuffer temp;
	Paquet reconstruit;
	int newByte = 0;



	public taskRetablirPaquets (Paquet f, int num) {
		frere = f;
		numeroMort = num;
		for(int i = 0; i<Global.NOMBRESOUSPAQUETS;i++)
			b[i] = ByteBuffer.allocate((int) (Global.PAQUET_SIZE+3));

		Paquet reconstruit = new Paquet(frere.idMachine-frere.idInterne+numeroMort,frere.owner);

	}

	@Override
	public void run() {

		//Etape 1: se connecter sur les autres paquets et récupérer le buffer correspond.

		for(int i =0;i<Global.NOMBRESOUSPAQUETS;i++){
			if(i!=numeroMort &&i!=frere.idInterne){
				try {
					InetSocketAddress local = new InetSocketAddress(0); 
					clientSocket[i].bind(local); 
					InetSocketAddress remote = new InetSocketAddress(frere.otherHosts.get(i).ipAdresse, frere.otherHosts.get(i).port); 
					clientSocket[i].connect(remote);
					clientSocket[i].write(Utilitaires.stringToBuffer(Message.DEMANDE_PAQUET));

					//Etape 2 : attendre que le monsieur réponde qu'il veut bien nous envoyer le paquet
					//Il faut utiliser la super fonction de simon.


					//Etape 3 : Envoyer le numero du paquet
					clientSocket[i].write(Utilitaires.stringToBuffer(frere.otherHosts.get(i).toString()+"-"+(frere.idMachine-frere.idInterne+i)));



					//Etage 4 : recevoir le paquet dans le buffer
					b[i].clear();
					while(b[i].position()!=Global.PAQUET_SIZE)
						clientSocket[i].read(b[i]);
					b[i].flip();


					//Etape 5 : remercier
					//nan en fait on s'en fout
					//clientSocket[i].write(Utilitaires.stringToBuffer(Message.OK));

				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		for(int j = 0;j<Global.PAQUET_SIZE;j++)
		{

			//temp est le buffer temporaire qui contient le byte qui va être écrit réellement.
			b[reconstruit.idInterne].clear();

			if(numeroMort>=Global.NOMBRESOUSPAQUETSSIGNIFICATIFS)
			{
				for(int i=0;i<Global.NOMBRESOUSPAQUETSSIGNIFICATIFS;i++)
					newByte += (int) b[i].get(j);
				newByte %= 256;
				b[numeroMort].put((byte)newByte);
			}
			else
			{
				for(int i=0;i<Global.NOMBRESOUSPAQUETSSIGNIFICATIFS;i++)
				{
					if(i!=numeroMort)
						newByte += (int) b[i].get(j);


				}
				newByte =(b[Global.NOMBRESOUSPAQUETSSIGNIFICATIFS-1].get(j) - newByte)%256;
				b[numeroMort].put((byte)newByte);
			}

			b[reconstruit.idInterne].flip();

		}
		try {
			reconstruit.fichier.write(b[numeroMort]);
			reconstruit.remettrePositionZero();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}





	
}
