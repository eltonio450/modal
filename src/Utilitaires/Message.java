package Utilitaires;

public class Message {
	
    // TCP Connections
	  public static String HOST_CHANGED = "host-changed#" ;
		public static String EXCHANGE = "exchange#" ;
		public static String REPONSE_EXCHANGE = "ok-exchange#" ;
		public static String MONITOR = "monitor#";
		public static String END_ENVOI = "fin#" ;
		public static String NEXT_BUFFER = "NEXT#";
		public static String BEGIN = "BEGIN#";
		public static String GET_LIST = "GET_LIST#";
		public static String NEW_SERVER = "NEWBROHERE#";
		public static String ASK_FOR_LOCK = "ASK_FOR_LOCK#";
		public static int BUFFER_LENGTH = 1000 ;
		public static final String DEMANDE_ID = "id_pleas#";
		public static final String DEMANDE_PAQUET = "demande_paquet#";
		public static String ANNULE_ENVOI = "stop#" ;
		public static String DO_NOT_ACCEPT = "non#" ;
		public static String OK = "ok#" ;
		public static int TIME_TO_SLEEP_1 = 500 ; //TODO : make a reasonable choice
		
		//PR
		public static String PREFIXE_BONJOUR = "WESH#";
		public static String PREFIXE_REPONSE_BONJOUR = "YO#";
		public static String SELF_WAKE_UP = "DEBOUT#";
		public static String VERIFY_DEATH = "YOUDEADBRO?#";
		public static String NOT_DEAD = "NOPE#";
		public static String IS_DEAD = "ILESTMORT#";
}
