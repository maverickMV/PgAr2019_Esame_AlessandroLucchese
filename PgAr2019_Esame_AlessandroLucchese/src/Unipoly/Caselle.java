package Unipoly;

import java.util.ArrayList;

import it.unibs.fp.mylib.NumeriCasuali;

public class Caselle 
{
	private String tipo;		// tipo di casella
	private String nome;		// nome casella
	private int id;				// id casella
	private String nomePr;		// nome Proprietario (in caso di proprieta)

	private int valore; // valore da aggiungere/sottrarre al conto giocatore
	
	private int costoCasa;	// costo per costruire la casa
	private int costoAlb;	// costo per costruire albergo
	
	private int guadBase;	// guadagno / costo (per chi non è proprietario) in caso di assenza di case e alberghi
	private int guadCasa;		// variabile che identifica i guadagni dalle case		
	private int guadAlbergo;		// variabile che identifica i guadagni dagli alberghi
	
	private int numCase = 0;			// per gestire il numero di case
	private int numAlb = 0;				// per gestire il numero di alberghi
	
	ArrayList <String> messaggi = new ArrayList <String>();
	
	public Caselle (int _id, String _nome, String _tipo)
	{
		tipo = _tipo;
		nome = _nome;
		id = _id;
		
		guadBase = NumeriCasuali.estraiIntero(3000, 8000);
		guadCasa = NumeriCasuali.estraiIntero(4000, 10000);
		guadAlbergo = NumeriCasuali.estraiIntero(7000, 20000);
	}
	
	public ArrayList <String> getMessaggi ()
	{
		return messaggi;
	}
	
	public String getNome ()
	{
		return nome;
	}
	
	public String getTipo ()
	{
		return tipo;
	}
	
	public int getId ()
	{
		return id;
	}
	
	public int getValore ()
	{
		return valore;
	}
	
	public void setValore (int _valore)
	{
		valore = _valore;
	}
	
	public void setCostoCasa (int _valore)
	{
		costoCasa = _valore;
	}
	
	public void setCostoAlb(int _valore)
	{
		costoAlb = _valore;
	}
	
	public void setGuadBase (int _valore)
	{
		guadBase = _valore;
	}
	
	public void setGuadCasa (int _valore)
	{
		guadCasa = _valore;
	}
	
	public void setGuadAlb (int _valore)
	{
		guadAlbergo = _valore;
	}
	
	public String getNomePr ()
	{
		return nomePr;
	}
	
	public void setNomePr (String valore)
	{
		nomePr = valore;
	}
	
	public int getGuadBase ()
	{
		return guadBase;
	}
	
	public int getGuadCasa ()
	{
		return guadCasa;
	}
	public int getGuadAlb ()
	{
		return guadAlbergo;
	}
	
	public int getCostoCasa ()
	{
		return costoCasa;
	}
	/**
	 * 
	 * @return ritorno valore costo costruzione albergo
	 */
	public int getCostoAlb ()
	{
		return costoAlb;
	}
	/**
	 * 
	 * @return valore che identifica il costo/guadagno per il passaggio su questa casella
	 */
	public int passaggioGio ()
	{
		if (numCase != 0)
			return guadCasa;
		else if (numAlb != 0)
			return guadAlbergo;
		else
			return guadBase;
	}
	/**
	 * 
	 * @return boolean per identificare se la casella è identificabile o meno
	 */
	public boolean isEdificabile ()
	{
		if (numCase == 0 && numAlb == 0)
			return true;
		else
			return false;
	}
	
	public void costrCasa ()
	{
		numCase++;
	}
	
	public void costrAlb ()
	{
		numAlb++;
	}
}
