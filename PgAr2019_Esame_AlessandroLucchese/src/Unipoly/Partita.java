package Unipoly;

import java.util.ArrayList;

import it.unibs.fp.mylib.InputDati;
import it.unibs.fp.mylib.NumeriCasuali;

public class Partita 
{
	boolean controllo = true; // variabile di controllo continuazione gioco
	private int numGiocatori;
	private int turnoGiocatore = 0;
	
	ArrayList <Mappa> mappe = new ArrayList <Mappa>();		// array contenente le diverse configurazioni delle mappe
	private int mappaSelezionata = -1;
	
	private boolean sconfitta = false;
	
	ArrayList <Giocatore> giocatori = new ArrayList <Giocatore> (); // array per gestione multiplayer
	
	private String[] classifica;
	private String[] classificaSconf;
	
	private int numSconfitti = 0;
	
	
	
	int numTiriDoppi = 0;		// variabile per gestione tiri doppi (per prigione)
	
	/**
	 * 
	 * @param _numGiocatori numero di giocatori che partecipano
	 */
	
	public Partita (int _numGiocatori)		// DA AGGIUNGERE LA SELEZIONE MAPPA
	{
		creaMappe();
		
		numGiocatori = _numGiocatori;
		classifica = new String [numGiocatori];
		classificaSconf = new String [numGiocatori];
		for (int i = 0; i < numGiocatori; i++)
		{
			String nome = InputDati.leggiStringa("Inserire nome giocatore: ");
			giocatori.add(new Giocatore (nome)); 
		}
		
		do		// INIZIO GIOCO
		{
			int lancio = tiraDadi(turnoGiocatore);
			String nomeGioAtt = giocatori.get(turnoGiocatore).getNome();
			System.out.println(nomeGioAtt+" ha fatto "+lancio);
			giocatori.get(turnoGiocatore).aggiornaPosizione(lancio);
			
			int posGiocatore = giocatori.get(turnoGiocatore).getPosizione();
			
			if (posGiocatore >= mappe.get(mappaSelezionata).getNumCaselle())		// per la gestione della fine del giro
			{
				giocatori.get(turnoGiocatore).setPosizione(posGiocatore - mappe.get(mappaSelezionata).getNumCaselle());
				posGiocatore = giocatori.get(turnoGiocatore).getPosizione();
			}
			System.out.println(nomeGioAtt+ " è in posizione "+ posGiocatore);
			
			switch (mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).getTipo())
			{
				case "probabilita":		// caso casella PROBABILITA
					System.out.println(nomeGioAtt+ " è finito su una casella probabilità");
					for (int i = 0; i < mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).getMessaggi().size(); i++)
					{	
						System.out.println(mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).getMessaggi().get(i)+"\n");
					}
					int valorePr = mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).getValore();
					System.out.println(nomeGioAtt+ " guadagna "+ valorePr+ " euro");
					giocatori.get(turnoGiocatore).aggiornaConto(valorePr);
					System.out.println(nomeGioAtt + " ora possiede "+giocatori.get(turnoGiocatore).getConto()+ " euro");
					break;
					
				case "imprevisto":		// caso casella IMPREVISTO
					System.out.println(nomeGioAtt+ " è finito su una casella imprevisto");
					for (int i = 0; i < mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).getMessaggi().size(); i++)
					{	
						System.out.println(mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).getMessaggi().get(i)+"\n");
					}
					int valoreImp = mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).getValore() * -1;
					System.out.println(nomeGioAtt+ " perde "+ valoreImp+ " euro");
					giocatori.get(turnoGiocatore).aggiornaConto(valoreImp);
					System.out.println(nomeGioAtt + " ora possiede "+giocatori.get(turnoGiocatore).getConto()+ " euro");
					break;
					
				case "proprieta":		// caso casella PROPRIETA
					System.out.println("Sei finito su "+ mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).getNome());
					
					boolean condAcq = false;
					String nomePr = mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).getNome();
					boolean sulMercato = false;
					int indicePro = 0;
					
					for (int i = 0; i < mappe.get(mappaSelezionata).getPrDisponibili().size(); i++)
					{
						if (mappe.get(mappaSelezionata).getPrDisponibili().get(i).getNome().equals(nomePr))
						{
							sulMercato = true;
							indicePro = i;
						}
					}
					if (sulMercato)
					{
						int costo = mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).getValore();
						System.out.println("Questa proprieta è in vendita");
						System.out.println("Costo: "+ costo);
						do
						{
							condAcq = false;
							System.out.print("Vuoi Comprare questa propietà");
							int scelta = InputDati.leggiIntero("(SI = 0, NO = 1)");
							
							if (scelta > 1 || scelta < 0)
							{
								System.out.println("Errore valore inserito non valido");
								condAcq = true;
							}
							if (scelta == 0)
							{
								System.out.println("Hai comprato la proprieta");
								giocatori.get(turnoGiocatore).aggiornaConto(costo*-1);
								mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).setNomePr(nomeGioAtt);
								mappe.get(mappaSelezionata).getPrDisponibili().remove(indicePro);
								System.out.println("Nuovo conto: "+giocatori.get(turnoGiocatore).getConto()+" euro");
							}

						} while (condAcq);
						
					}
					else		// se la proprieta non e in vendita
					{
						int variazione = mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).passaggioGio();
						
						if (mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).getNomePr().equals(nomeGioAtt))
						{
							System.out.println("Questo proprietà è tua!");
							System.out.println("Guadagni " + variazione+ " euro");
							
							if (mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).isEdificabile() == true)
							{
								boolean condCostr = false;
								
								do
								{
									condCostr = false;
									System.out.println("Vuoi costruire?");
									int sceltaCostr = InputDati.leggiIntero("(CASA = 0, ALBERGO = 1, NIENTE = 2): ");
									
									if (sceltaCostr < 0 || sceltaCostr > 2)
									{
										System.out.println("Errore valore inserito non valido");
										condCostr = true;
									}
									if (sceltaCostr == 0)
									{
										int costoC = mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).getCostoCasa()*-1;
										System.out.println("Casa costruita");
										giocatori.get(turnoGiocatore).aggiornaConto(costoC);
										mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).costrCasa();
										System.out.println("Conto: "+ giocatori.get(turnoGiocatore).getConto()+ " euro");
									}
									else if (sceltaCostr == 1)
									{
										int costoAlb = mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).getCostoAlb()*-1;
										System.out.println("Albergo costruita");
										giocatori.get(turnoGiocatore).aggiornaConto(costoAlb);
										mappe.get(mappaSelezionata).getCaselle().get(posGiocatore).costrAlb();
										System.out.println("Conto: "+ giocatori.get(turnoGiocatore).getConto()+ " euro");
									}
								}	while (condCostr);
							}
							
						}
						else		// cosa fare se la proprieta non è del giocatore
						{	
							System.out.println("Questa proprietà non è tua!");
							System.out.println("Paghi "+variazione+ " euro al proprietario");
							variazione = variazione *-1;
							giocatori.get(turnoGiocatore).aggiornaConto(variazione);
						}
					}
					
					break;
				
				case "ticket":
					System.out.println(nomeGioAtt+ " è finito su una casella Ticket");
					giocatori.get(turnoGiocatore).setHaTicket(true);
					break;
				case "prigione":
					break;
				case "stazione":		// caso casella STAZIONE
					System.out.println(nomeGioAtt+ " è finito su una casella stazione");
					boolean controlloScStaz = false;
					boolean controlloScStaz2 = false;
					do
					{
						int scelta = InputDati.leggiIntero("Vuoi andare in un'altra stazione?"+"\n"+"(SI = 0, NO = 1): ");
						controlloScStaz = false;
						controlloScStaz2 = false;
						int sceltaDest;
						if (scelta > 1 || scelta < 0)
						{
							System.out.println("Errore valore inserito non corretto");
							controlloScStaz = true;
						}
						if (scelta == 0)
						{
							for (int i = 0; i < mappe.get(mappaSelezionata).getStazioni().size(); i++)
							{
								if (mappe.get(mappaSelezionata).getStazioni().get(i).getId() != posGiocatore)
								{
									System.out.print(i+"."+mappe.get(mappaSelezionata).getStazioni().get(i).getNome());
									System.out.println(" posizione: "+ mappe.get(mappaSelezionata).getStazioni().get(i).getId());
								}
							}
							
							do
							{
								sceltaDest = InputDati.leggiIntero("Inserisci verso quale stazione vuoi andare: ");
							
							
								if (sceltaDest < 0 || sceltaDest > mappe.get(mappaSelezionata).getStazioni().size()-1)
								{
									System.out.println("Errore stazione selezionata non esistente");
									controlloScStaz2 = true;
								}
								
							} while (controlloScStaz2);
							
							giocatori.get(turnoGiocatore).setPosizione(mappe.get(mappaSelezionata).getStazioni().get(sceltaDest).getId());
						}
					} while (controlloScStaz);
					
					break;
					
					default:
						System.out.println("Sei sulla casella iniziale");
					
			} // fine SWITCH
			
			
			// messaggi e calcoli di fine turno giocatore
			System.out.println("Turno di "+giocatori.get(turnoGiocatore).getNome()+" è finito");
			InputDati.leggiStringa("Cambio turno (premere un tasto per continuare)");
			System.out.println("\n\n");
			controlloStato ();
			
			// se il numero di giocatori è finito rincomincia dal primo
			turnoGiocatore++;
			if (turnoGiocatore >= numGiocatori)
				turnoGiocatore = 0;
			
		} while (controllo);
		
			for (int i = 0; i < giocatori.size(); i++)
			{
				int conto1 = giocatori.get(i).getConto();
				int contMagg = 0;
				for (int j = 0; j < giocatori.size(); j++)
				{					
					int conto2 = giocatori.get(j).getConto();
					if (conto2 > conto1)
						contMagg++;
					
				}
				classifica [contMagg] = giocatori.get(i).getNome();
			}
		
		
		for (int i = 0; i < classifica.length; i++)
		{
			System.out.println((i+1)+"° "+classifica[i]);
		}
	}
	
	/**
	 * Estrazione casuale per lancio dadi
	 * @return (lancio1+lancio2) valore spostamento casella
	 */
	
	private int tiraDadi (int numGiocatore)
	{
		int lancio1 = NumeriCasuali.estraiIntero(1, 6);
		int lancio2 = NumeriCasuali.estraiIntero(1, 6);
		
		if (lancio1 == lancio2)
		{
			giocatori.get(numGiocatore).setTiriDoppi(giocatori.get(numGiocatore).getTiriDoppi()+1);
		}
		return (lancio1+lancio2);
	}
	
	/**
	 * Verifica che interrompe il gioco se qualcuno ha vinto o se hanno perso tutti 
	 */
	
	private void controlloStato ()		// controllo stato che viene lanciato alla fine di ogni turno per controllare eventuali sconfitti
	{
		
		
		for (int i = 0; i < numGiocatori; i++)
		{
			
			if (giocatori.get(i).getHaVinto() == true)
				controllo = false;
			if (giocatori.get(i).getHaPerso() == true)
			{
				numSconfitti++;
			}
		}
		
		if (numSconfitti == numGiocatori || numSconfitti == (numGiocatori-1))
		{
			controllo = false;
			sconfitta = true;
			int contatore = 1;
			for (int i = 0; i < giocatori.size(); i++)
			{
				if (giocatori.get(i).getHaPerso() == false)
				{
					classificaSconf [0] = giocatori.get(i).getNome();
				}
				else
				{
					classificaSconf [contatore] = giocatori.get(i).getNome();
					contatore++;
				}
			}
			
			
		}
		
	}
	/**
	 * inizializzare mappe per il gioco che vengono successivamente lette dal parser
	 */
	private void creaMappe ()
	{
		String[] nomiFile = {"5) C1 + C2 (24).xml", "6) C1 + C2 (40).xml", "2) C1 (20).xml", "3) C1 (40).xml"};
		for (int i = 0; i < nomiFile.length; i++)
		{
			mappe.add(new Mappa(nomiFile[i]));
		}
		
		while (mappaSelezionata < 0)
		{
			String scelta = InputDati.leggiStringa("Qual è il nome della mappa che vuoi usare: ");
			for (int i = 0; i < mappe.size(); i++)
			{
				if (scelta.equals(mappe.get(i).getNome()))
				{
					mappaSelezionata = i;
				}
			}
			if (mappaSelezionata < 0)
			{
				System.out.println("Errore nome mappa inserito inesistente");
			}
		}		// chiusura WHILE ricerca mappa da usare
	}
	
}
