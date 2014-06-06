package RelationsPubliques;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

import Utilitaires.Utilitaires;

public class ClientPR extends Thread{
	private DatagramChannel channel;
	private ByteBuffer buffToSend;
	private String stringSent;
	private int remoteIndex;
	private ServerPR serveurPR;

	private ConcurrentLinkedQueue<Message> toSend;


	public ClientPR (ServerPR serveurPR) throws IOException{
		this.channel = DatagramChannel.open();
		this.channel.socket().bind(new InetSocketAddress(Global.CLIENTPRPORT));
		this.buffToSend = Utilitaires.stringToBuffer(Global.PREFIXE_BONJOUR);
		this.remoteIndex = 0;
		this.serveurPR = serveurPR;
	}

	public void run () {
		Message message;
		stringSent = Global.PREFIXE_BONJOUR;
		InetSocketAddress remote;

		while (true) {
			try {
				// Envoie coucou
				remote = getRemote();
				channel.send(buffToSend, remote);
				serveurPR.expectMessage(new ExpectedMessage(stringSent, new InetSocketAddress(remote.getHostName(), remote.getPort()-1), System.currentTimeMillis() + Global.TIMEOUT));
			
				buffToSend.flip();

				// Envoie ce qu'on lui a demandé d'envoyer
				for (int i=0; !toSend.isEmpty() && i<100; i++) {
					message = toSend.poll();
					if (message.expirationDate < System.currentTimeMillis())
						channel.send(Utilitaires.stringToBuffer(message.body), message.dest);
				}

				if (toSend.isEmpty()) {
					try {
						Thread.sleep(Global.SLEEPTIME);
					} catch (InterruptedException e) {}
				}

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("ClientPR Thread just crashed");
				System.exit(-1); 
			}
		}
	}

	public void sendMessage (Message message) {
		toSend.add(message);
		this.interrupt();
	}

	private InetSocketAddress getRemote () {
		return null;
	}
}
