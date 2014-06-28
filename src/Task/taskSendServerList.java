package Task;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

import Stockage.Donnees;
import Stockage.Machine;
import Utilitaires.Global;
import Utilitaires.*;

public class taskSendServerList implements Runnable {
	SocketChannel s;
	
	public taskSendServerList (SocketChannel s) {
		this.s = s;
	}
	
	public void run () {
		String message;
		LinkedList<Stockage.Machine> servers = Donnees.getAllServeurs();
		servers.add(Global.MYSELF);
		//Donnees.addHost();
		boolean continuer = true;
		
		
		while (continuer) {
			message = new String ();
			
			while (message.length() < Global.BUFFER_LENGTH/4 && !servers.isEmpty()) {
				Machine m = servers.pop();
				message += Message.BEGIN + " " + m.ipAdresse + " " + m.port + " ";
			}
			
			if (servers.isEmpty()) {
				message += Message.END_ENVOI;
				continuer = false;
			}
			else
				message += Message.NEXT_BUFFER;
			
			try {
				s.write(Utilitaires.stringToBuffer(message));
			} catch (IOException e) {
				return;
			}
		}
		
		try {
			s.close ();
		} catch (IOException e) {
			// Nobody cares !
		}
		Donnees.addHost(new Machine(s.socket().getInetAddress().toString(),s.socket().getPort()));	
		RelationsPubliques.BroadcastAll.broadcastTCP(Message.NEW_SERVER + " " + s.socket().getRemoteSocketAddress() + " " + s.socket().getPort() + " #", Donnees.getAllServeurs());
		Donnees.printServerList();
	}
}