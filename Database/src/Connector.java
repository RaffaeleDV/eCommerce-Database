

import java.sql.*;
import java.util.Scanner;


class Connector {
	
	public static Scanner scanner = new Scanner(System.in);
	
    private String url = "jdbc:mysql://localhost:3306/progetto";
    static Connection con;
    static Statement st = null;
	private PreparedStatement ps = null; 
	static ResultSet rs = null;
	static int risultato = 0;
	private String[] values = new String[10];
	
	/**
	 * Stampa le richieste di input e per ciascuno di essi li acquisisce.
	 * @param richieste di input
	 * @param dimension numero di input richiesti
	 * @return input richiesti
	 */
	public static String[] acquisisciValori(String[] richieste){
	  String dati[] = new String[richieste.length];
	  
	  for(int i = 0; i < richieste.length; i++){
		  System.out.println("Fornisci " + richieste[i]);
		  dati[i] = scanner.nextLine();
	  }
	  return dati;
	}
	
	/**
	 * Esegue una query.
	 * @param sql procedura da eseguire
	 * @throws SQLException 
	 */
	public static void executeQuery(String sql) {
		try{
			//System.out.println(sql);
			Statement st = con.createStatement();
			rs = st.executeQuery(sql);
		}catch(SQLException e){
			System.err.println("Errore in ExecuteQuery (" + sql+")");
			
		}
		
	}
	
	/**
	 * Esegue un update.
	 * @param Update procedura da eseguire
	 * @throws SQLException 
	 */
	public static void executeUpdate(String sql) {
		try{
			Statement st = con.createStatement();
			risultato = st.executeUpdate(sql);
		}catch(SQLException e){
			System.err.println("Errore in ExecuteUpdate (" + sql+")");
		}
	}
	
	/**
	 * Inserisce i valori all'interno della tabella.
	 * @param tabella in cui inserire i valori
	 * @param values valori da inserire
	 * @throws SQLException 
	 */
	static void INSERT_INTO(String table, String[] values, String[] types){
		
		String data;
		
		if(types[0].equals("i"))
			data = " VALUES(" + values[0];
		else
			data = " VALUES('" + values[0] + "'";
		for(int i = 1; i < values.length; i++){
			if(types[i].equals("i"))
				data = data.concat(", " + values[i]);
			else
				data = data.concat(", '" + values[i] + "'");
		}
		data = data.concat(");");
  		String sql = "INSERT INTO " + table + data;
  		System.out.println(sql);
  		executeUpdate(sql);
	}
	
	/**
	 * Cancella le tuple il cui attributo è uguale a value.
	 * @param table tabella dove eliminare la tupla
	 * @param attribute attributo di confronto
	 * @param value valore da confrontare con attribute
	 * @throws SQLException 
	 */
   public static void DELETE_FROM(String table, String attribute, String value){;
   System.out.println("DELETE FROM " + table + " WHERE " + attribute + " = '" + value + "';");		
		   executeUpdate("DELETE FROM " + table + " WHERE " + attribute + " = '" + value + "';");
	}
	
   
   public static float calcola_affidabilita(String cod_fis){
	   String sql_numero_consegne_fis = "select count(*) "
	   		+ "from (((prodotto_fisico as p join fattura as f on p.codice_prod = f.prodotto_fattura) "
	   		+ "join ordine on ordine.codice_ordine = f.ordine_fattura  ) "
	   		+ "join consegna on ordine.codice_ordine= consegna.ordine_consegna) "
	   		+ "where p.venditore_prodotto = "+cod_fis+" and consegna.fase=1;";
	   executeQuery(sql_numero_consegne_fis);
	   int cs = 0, cf = 0, os= 0, of = 0;
	   float c= 0, o = 0, com = 0; 
	   try {
		   if(rs.next()) cf = rs.getInt(1);
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	   String sql_numero_consegne_soft = "select count(*) "
	   		+ "from ((software as s join ordine as o on s.ordine_software= o.codice_ordine) "
	   		+ "join consegna as c on o.codice_ordine = c.ordine_consegna) "
	   		+ "where s.venditore_software = "+ cod_fis+ " and (c.fase=2 or c.fase=1);";    
	   executeQuery(sql_numero_consegne_soft);
	   try{
		   if(rs.next())  cs = rs.getInt(1);
	   }catch(SQLException e){
		   e.printStackTrace();
	   }
	   
	   String sql_numero_ordini_fis = "select count(*) "
		   		+ "from ((prodotto_fisico as p join fattura as f on p.codice_prod = f.prodotto_fattura) "
		   		+ "join ordine on ordine.codice_ordine = f.ordine_fattura  ) "
		   		+ "where p.venditore_prodotto = "+cod_fis+";";
	   executeQuery(sql_numero_ordini_fis);
	   try{
		   if(rs.next()) of = rs.getInt(1);
	   }catch(SQLException e){
		   e.printStackTrace();
	   }
	   
	   String sql_numero_ordini_soft = "select count(*) "
		   		+ "from software as s join ordine as o on s.ordine_software= o.codice_ordine "
		   		+ "where s.venditore_software ="+cod_fis+";";
	   executeQuery(sql_numero_ordini_soft);
	   try{
		   if(rs.next()) os = rs.getInt(1);
	   }catch(SQLException e){
		   e.printStackTrace();
	   }
	   o = of + os;
	   float percentuale = 0;
	   if(o == 0) {
		   o = 1;
		   percentuale = 0;  
	   }
	   else {percentuale =  ((cs + cf)/(o))*100;}
	   
	   String sql_numero_commenti = "select avg(voto) as voto "
		   		+ "from commento as c "
		   		+ "where c.commentato = "+cod_fis +";";
	   executeQuery(sql_numero_commenti);
	   try{
		   if(rs.next()) com = rs.getFloat("voto");
	   }catch(SQLException e){
		   e.printStackTrace();
	   }
	   return (com+percentuale)/2;
   }
   
   public static void main(String args[]) throws Exception {
	   try{
		   Class.forName("com.mysql.jdbc.Driver").newInstance();

           String url = "jdbc:mysql://localhost:3306/progetto";
           con = DriverManager.getConnection(url,"root","raffaele");
              
		   System.out.println("Connessione OK \n");
		   
		   
		   
		   System.out.println("Benvenuto nel database.\n");
	   }catch(Exception e){
		   System.err.println("Errore Connessione");
		   
	   }
		  while(true){
			  
			  System.out.println("Elenco Operazioni: ");
			  System.out.println("Operazione 1: Visualizzazione delle consegne effettuate (fase 2) entro la scadenza");
			  System.out.println("Operazione 2: Visualizzazione degli ordini effettuati dall'utente");
			  System.out.println("Operazione 3: Visualizzazione dei dati del prodotto");
			  System.out.println("Operazione 4: Visualizzazione dei dati dell'utente e dell'affidabilita'");
			  System.out.println("Operazione 5: Aggiunta nuovo utente");
			  System.out.println("Operazione 6: Aggiunta di un nuovo prodotto fisico");
			  System.out.println("Operazione 7: Aggiunta di un nuovo prodotto software");
			  System.out.println("Operazione 8: Aggiunta di un nuovo ordine");
			  System.out.println("Operazione 9: Aggiunta di una consegna");
			  System.out.println("Operazione 10: Aggiunta di un nuovo commento");
			  System.out.println("Operazione 11: Aggiunta di un ente spedizioniere");
			  System.out.println("Operazione 12: Rimozione di un prodotto fisico");
			  System.out.println("Operazione 13: Rimozione di un prodotto software");
			  System.out.println("Operazione 14: Rimozione di un ordine");
			  System.out.println("Operazione 15: Aggiornamento dei dati di un utente");
			  System.out.println("Operazione 0: Uscita dal database ");
			
			  
			  
			  String operazione = scanner.nextLine();
			  String[] richieste;
			  String[] values;
			  String[] types;
			  String sql = new String();
			  
			  switch(operazione){
														/*Visualizzazione delle consegne effettuate dall'utente*/
			  case "1":								
				  	sql = "SELECT c.ordine_consegna, c.data_consegna, c.tipo_consegna FROM consegna AS c JOIN ordine AS o on c.ordine_consegna = o.codice_ordine WHERE c.data_consegna between o.data_ordine and o.data_scad_ordine AND fase = 2;";
				  	executeQuery(sql);
				  	System.out.println("OrdineConsegna   DataConsegna   TipoConsegna");
				  	int i = 0;
				  	while(rs.next()){
				  		System.out.println(rs.getString("ordine_consegna") + "\t" + rs.getString("data_consegna") + "\t" + rs.getString("tipo_consegna"));
				  	i++;
				  	}if(i == 0) System.out.println("Tupla vuota!");
				  		break;
				  										/*Visualizzazione degli ordini effettuati dall'utente*/
			  case "2":								
				    System.out.println("Inserire codice fiscale dell'utente");
					String cf = scanner.nextLine();	
					System.out.println("CodiceOrdine   DataOrdine  DataScadOrdine, TipoSpedizione");
					sql = "select distinct codice_ordine, data_ordine, data_scad_ordine, tipo_spedizione from ordine, fattura, software where (codice_ordine = ordine_fattura and compratore_fattura =" + cf + ") or"
							+ " (codice_ordine = ordine_software and acquirente_software =" + cf + ");";
					executeQuery(sql);
					while(rs.next())
						System.out.println(rs.getString("codice_ordine") + "\t" + rs.getString("data_ordine") + "\t" + rs.getString("data_scad_ordine")+ "\t" + rs.getString("tipo_spedizione"));
					break;
														/*Visualizzazione dei dati del prodotto*/
			  case "3": 								
				    System.out.println("Inserire codice prodotto");
					String name = scanner.nextLine();
					System.out.println("Codice  Prezzo  Venditore  Ordine");
					sql = "SELECT * from software where software.codice_software = " + name;
					executeQuery(sql);
					while(rs.next())
						System.out.println(rs.getString("codice_software")+ "\t" + rs.getString("prezzo_software") + "\t" + rs.getString("venditore_software") + "\t" + rs.getString("ordine_software"));
					sql = "SELECT * "
							+ "from prodotto_fisico "
							+ "where prodotto_fisico.codice_prod =" + name;
					executeQuery(sql);
					while(rs.next())
						System.out.println(rs.getString("codice_prod")+ "\t"+ rs.getString("prezzo_prod") + "\t\t" + rs.getString("venditore_prodotto"));					
					break;
			  		
														/*Visualizzazione dei dati dell'utente e dell'affidabilità*/
			  case "4":
				  	System.out.println("Inserisci CF:");
				  	String cf_utente = scanner.nextLine();
				  	sql = "select * from utente where utente.cf ="+ cf_utente + ";";
				  	executeQuery(sql);

				  	rs.next();
				  	System.out.println(rs.getString("cf") + "\t"
				  	+ rs.getString("nome") + "\t" + rs.getString("cognome") + "\t" 
				  	+ rs.getString("CAP")+ "\t" + rs.getString("indirizzo_spedizione") 
				  	+ "\t" + rs.getString("dataNascita")+ "\t" + rs.getString("pin")+ "\t"  
				  	+ rs.getString("data_scadenza_carta")+ "\t" + rs.getString("cvv") + "\t"
				  	+ calcola_affidabilita(rs.getString("cf")));
				  	break;
				  										/*Aggiunta nuovo utente*/
			  case "5": 								
				  	richieste = new String[]{"C.F", "Nome", "Cognome", "CAP", "Indirizzo", "Data di Nascita(YYYY-MM-DD)", "PIN", "Scadenza Carta(YYYY-NN-DD)", "CVV(3 Cifre)"};
			  		types = new String[]{"s", "s", "s", "", "s", "s", "s", "s", "s"};
				  	INSERT_INTO("utente", acquisisciValori(richieste), types);  		
			  		break;
			  											/*Aggiunta di un nuovo prodotto fisico*/
			  case "6": 								
				  	richieste = new String[]{"Codice Prodotto", "Prezzo Prodotto", "Descrizione Prodotto", "C.F del venditore"};
				  	types = new String[]{"s", "", "s", "s"};
				  	INSERT_INTO("prodotto_fisico", acquisisciValori(richieste), types);			 
					break;
														/*Aggiunta di un nuovo prodotto software*/
			  case "7": 								
					richieste = new String[]{"Codice Prodotto", "Prezzo Prodotto", "C.F dell'acquirente", "C.F del venditore", "Codice Ordine"};
					types = new String[]{"s", "", "s", "s", "s"};
					INSERT_INTO("software", acquisisciValori(richieste), types);
					break;
														/*Aggiunta di un nuovo ordine*/
			  case "8":   								
				    richieste = new String[] {"Codice ordine", "Data dell'Ordine", "Data Scadenza", "Tipo Spedizione"}; 
				    types = new String[]{"s", "s", "s", "s"};
				    INSERT_INTO("ordine", acquisisciValori(richieste), types);
				  	break;
														/*Aggiunta di una consegna*/
			  case "9":								
				  	richieste = new String[] {"Ordine Consegna", "fase", "Data Consegna", "Tipo Consegna", "Codice Consegna", "Codice Corriere"}; 
				    types = new String[]{"s", "s", "s", "s", "s", "s"};
				    INSERT_INTO("consegna", acquisisciValori(richieste), types);
				  	break;
														/*Aggiunta di un nuovo commento*/
			  case "10": 								
				  	richieste = new String[] {"Descrizione", "voto(0-100)", "Data Commento", "Codice Fiscale Commentatore", "Codice Fiscale Commentato"}; 
				    types = new String[]{"s", "", "s", "s", "s"};
				    INSERT_INTO("commento", acquisisciValori(richieste), types);
				  	break;
														/*Aggiunta di un ente spedizioniere*/
			  case "11": 									
				  	richieste = new String[] {"Nome Ente", "Partita IVA", "Indirizzo Ente", "Telefono"}; 
				    types = new String[]{"s", "s", "s", "s"};
				    INSERT_INTO("corriere", acquisisciValori(richieste), types);
				  	break;	  
			  
					  									/*Rimozione di un prodotto fisico*/
			  case "12": 								
				    System.out.println("Inserire codice prodotto");
					DELETE_FROM("prodotto_fisico", "prodotto_fisico.codice_prod", scanner.nextLine());
					break;
														/*Rimozione di un prodotto software*/
			  case "13": 								
				    System.out.println("Inserire codice prodotto");
					DELETE_FROM("software", "software.codice_software", scanner.nextLine());
					break;
				  										/*Cancellazione di un ordine*/
			  case "14": 								
				    System.out.println("Inserire codice ordine");
					DELETE_FROM("ordine", "codice_ordine", scanner.nextLine());
					break;
														/*Aggiornamento dei dati di un utente*/
			  case "15":									
					richieste = new String[]{"C.F", "Nome", "Cognome", "CAP", "Indirizzo", "Data di Nascita(YYYY-MM-DD)", "PIN", "CVV", "Scadenza Carta(YYYY-NN-DD)"};		  		
					values = acquisisciValori(richieste);
					sql = "UPDATE utente SET nome=\""+values[1]+"\",cognome=\""+values[2]+
					"\",CAP=\""+values[3]+"\",indirizzo_spedizione=\""+values[4]+"\",dataNascita=\""+values[5]+
					"\",pin=\""+values[6]+"\",CVV=\""+values[7]+"\",data_scadenza_carta=\""+values[8]+"\" WHERE cf = \""+
					values[0]+"\";";
					
					
					System.out.println("UPDATE utente SET nome=\""+values[1]+"\",cognome=\""+values[2]+
					"\",CAP=\""+values[3]+"\",indirizzo_spedizione=\""+values[4]+"\",data_nascita=\""+values[5]+
					"\",pin=\""+values[6]+"\",CVV=\""+values[7]+"\",scadenza_carta=\""+values[8]+"\" WHERE cf = \""+
					values[0]+"\";");
					executeQuery(sql);
					break;
														/*Uscita dal database*/
			  case "0": 
				    System.out.println("Uscita dal database"); 
					scanner.close(); 
					con.close(); 
					System.exit(0);
			  default: System.out.println("Input errato. Riprovare");
			  }

			  System.out.println("Eseguire un'altra operazione?(y)");
			  if(scanner.nextLine().equals("y")) continue;
			  else{
				  System.out.println("Uscita dal database"); 
					scanner.close(); 
					con.close(); 
					System.exit(0);
			  }
			  
			  
		  }
      }//end main
      
      
      
} // end Connessione
