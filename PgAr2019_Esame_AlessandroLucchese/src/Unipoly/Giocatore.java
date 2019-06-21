package Unipoly;

import java.util.ArrayList;

public class Giocatore 
{
	private String nome;		// nome giocatore
	private boolean isSuoTurno = false;		// variabile per gestire il passaggio turni
	private int conto = 50000;		// conto giocatore
	private int posizione = 0; 		// indica la posizione sulla mappa del giocatore
	
	private final int VALORE_VITTORIA = 1000000;
	
	private boolean haVinto = false;		// per la gestione della vittoria
	private boolean haPerso = false;		// per la gestione della sconfitta
	
	ArrayList <Integer> proprietaPoss = new ArrayList <Integer>();
	
	private boolean haTicket = false;		// gestione ticket per priogione 
	private boolean isImprigionato = false;		//gestione imprigionamento
	
	private int numTiriDoppi = 0;		// numero di volte che un giocatore esegue un tiro doppio
	/**
	 * 
	 * @param _nome costruttore per creare la classe Giocatore
	 */
	public Giocatore (String _nome)
	{
		nome = _nome;
	}
	/**
	 * 
	 * @param valore per la variazione della posizione del giocatore sulla mappa di gioco
	 */
	public void aggiornaPosizione (int valore)	// utlizzato con il metodo lancia dadi
	{
		posizione += valore;
	}
	/**
	 * 
	 * @param valore per settare la posizione del giocatore
	 */
	public void setPosizione (int valore)	// utilizzato per le stazioni che fanno spostamenti rapidi
	{
		posizione = valore;
	}
	/**
	 * 
	 * @param valore per la variazione si positiva che negativa del conto
	 */
	public void aggiornaConto (int valore)
	{
		conto += valore;
		if (conto > VALORE_VITTORIA)
			haVinto = true;
		else if (conto <= 0)
			haPerso = true;
	}
	/**
	 * 
	 * @param valore per settare valore di haTicket
	 */
	public void setHaTicket (boolean valore)
	{
		haTicket = valore;
	}
	/**
	 * 
	 * @return nome per ottenere il nome del giocatore
	 */
	public String getNome ()
	{
		return nome;
	}
	
	public int getConto ()
	{
		return conto;
	}
	/**
	 * 
	 * @return posizione per ottenere la posizione che ha il giocatore all'interno della mappa di gioco
	 */
	public int getPosizione ()
	{
		return posizione;
	}
	/**
	 * 
	 * @return haVinto per gestione vittoria giocatori
	 */
	public boolean getHaVinto ()
	{
		return haVinto;
	}
	
	/**
	 * 
	 * @return haPerso per gestione sconfitta giocatori
	 */
	public boolean getHaPerso ()
	{
		return haPerso;
	}
	
	/**
	 * 
	 * @param valore valore da dare alla variabile numTiriDoppi
	 */
	public void setTiriDoppi (int valore)			// PER PRIGIONE      DA FARE!!!!!!
	{
		numTiriDoppi = valore;
		if (numTiriDoppi == 3)
		{
			isImprigionato = true;
			//posizione = posizioneCella;
			numTiriDoppi = 0;
			// messaggio per comunicare l'impriogionamento
		}
	}
	
	public int getTiriDoppi ()
	{
		return numTiriDoppi;
	}
}
