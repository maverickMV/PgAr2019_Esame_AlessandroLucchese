package Unipoly;

import it.unibs.fp.mylib.InputDati;

public class GiocoMain 
{
	private static final String INSERISCI_NUMERO_GIOCATORI_MIN_2_MAX_4 = "Inserisci numero giocatori (MIN:2 MAX:4): ";

	public static void main(String[] args) 
	{
		boolean controllo = false;
		int numGiocatori;
		do // ciclo per controllare che il giocatore inserisca un valore valido
		{
			controllo = false; // variabile per la gestione del ciclo
			numGiocatori = InputDati.leggiIntero(INSERISCI_NUMERO_GIOCATORI_MIN_2_MAX_4);
			if (numGiocatori < 2 || numGiocatori > 4)		// controllo che il numero di giocatori inserito sia giusto
			{
				System.out.println("Errore valore inserito non valido");
				controllo = true;	// se il valore inserito è sbagliato il ciclo rincomincia
			}
			
		} while (controllo);
		
		Partita partita = new Partita (numGiocatori);		// inizializzazione partita
	}
}
