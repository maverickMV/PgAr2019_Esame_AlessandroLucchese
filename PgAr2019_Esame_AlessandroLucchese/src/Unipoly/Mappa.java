package Unipoly;

import java.io.FileInputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public class Mappa 
{
	XMLInputFactory xmlif = null;
	XMLStreamReader xmlr = null;
	
	ArrayList <Caselle> stazioni = new ArrayList <Caselle> ();	// array per gestire le stazioni
	ArrayList <Caselle> proprietaDisp = new ArrayList <Caselle> (); // array per gestire le proprieta che si possono comprare
	
	private String nome;
	private int numCaselle;
	private int casellaAtt = 0; // variabile usata per gestione creazione caselle
	
	private ArrayList <Caselle> caselle = new ArrayList <Caselle>();	// array per gestione caselle
	
	public Mappa (String nomeFile)
	{
		try
		{
			xmlif = XMLInputFactory.newInstance();
			xmlr = xmlif.createXMLStreamReader(nomeFile, new FileInputStream (nomeFile));
		}
		catch (Exception e)
		{
			System.out.println("Errore nell'inizializzare la lettura dati mappa");
		}
		
		leggiMappa();
	}
	
	public void leggiMappa ()
	{
		try
		{
			int idCella = 0;
			String nomeCella = "";
			String tipoCella = "";
			while (xmlr.hasNext())
			{
				idCella = 0;
				nomeCella = "";
				tipoCella = "";
				switch (xmlr.getEventType())
				{
					case XMLStreamConstants.START_ELEMENT:
						switch (xmlr.getLocalName())
						{
							case "map":
								for (int i = 0; i < xmlr.getAttributeCount(); i++)
								{
									if (xmlr.getAttributeLocalName(i).equals("size"))
										numCaselle = Integer.valueOf(xmlr.getAttributeValue(i));
									else if (xmlr.getAttributeLocalName(i).equals("title"))
										nome = xmlr.getAttributeValue(i);
									
								} // chiusura FOR attributi per MAP
								break;
								
							case "cell":
								
								for (int i = 0; i < xmlr.getAttributeCount(); i++)
								{
									switch (xmlr.getAttributeLocalName(i))
									{
										case "id":
											idCella = Integer.valueOf(xmlr.getAttributeValue(i));
											break;
										case "name":
											nomeCella = xmlr.getAttributeValue(i);
											break;
										case "type":
											tipoCella = xmlr.getAttributeValue(i);
											break;
									}	// chiusura SWITCH per ATTRIBUTI di CELL
									
								}	// chiusura FOR per ATTRIBUTI di CELL
								
								caselle.add(new Caselle (idCella, nomeCella, tipoCella));
								casellaAtt++;
								
								if (tipoCella.equals("proprieta"))
								{
									proprietaDisp.add(new Caselle (idCella, nomeCella, tipoCella)); // aggiungo una nuova proprietà fra quelle disponibili all'acquisto
									
									do
									{
										xmlr.next();
										if (xmlr.isStartElement())
										{
											if (xmlr.getLocalName().equals("costs"))										
											{
												for (int i = 0; i < xmlr.getAttributeCount(); i++)
												{								
													switch (xmlr.getAttributeLocalName(i))
													{
														case "base":
															caselle.get(casellaAtt-1).setValore(Integer.valueOf(xmlr.getAttributeValue(i)));
															break;
														case "house":
															caselle.get(casellaAtt-1).setCostoCasa(Integer.valueOf(xmlr.getAttributeValue(i)));
															break;
														case "hotel":
															caselle.get(casellaAtt-1).setCostoAlb(Integer.valueOf(xmlr.getAttributeValue(i)));
															break;
													}
												}
											}
										
											else if (xmlr.getLocalName().equals("earnings"))
											{
												for (int i = 0; i < xmlr.getAttributeCount(); i++)
												{
													switch (xmlr.getAttributeLocalName(i))
													{
														case "base":
															caselle.get(casellaAtt-1).setGuadBase(Integer.valueOf(xmlr.getAttributeValue(i)));
															break;
														case "house":
															caselle.get(casellaAtt-1).setGuadCasa(Integer.valueOf(xmlr.getAttributeValue(i)));
															break;
														case "hotel":
															caselle.get(casellaAtt-1).setGuadAlb(Integer.valueOf(xmlr.getAttributeValue(i)));
															break;
													}
												}
											}
										}
										
									} while (!xmlr.isEndElement()); // chiusura ciclo per PROPRIETA
										
								}	// chiusura condizione PROPRIETA
								
								else if (tipoCella.equals("imprevisto") || tipoCella.equals("probabilita"))
								{
									do
									{
										if (xmlr.isStartElement())
										{
											if (xmlr.getLocalName().equals("amount"))
											{
												for (int i = 0; i < xmlr.getAttributeCount(); i++)
												{
													if (xmlr.getAttributeLocalName(i).equals("value"))
													{
														caselle.get(casellaAtt-1).setValore(Integer.valueOf(xmlr.getAttributeValue(i)));
													}
												}
												
											}
											
											else if (xmlr.getLocalName().equals("line"))
											{
												for (int i = 0; i < xmlr.getAttributeCount(); i++)
												{
													if (xmlr.getAttributeLocalName(i).equals("value"))
													{
														caselle.get(casellaAtt-1).getMessaggi().add(xmlr.getAttributeValue(i));
													}
												}
											}
										}
										xmlr.next();
									} while (!xmlr.isEndElement());
								} // chiusura caso IMPREVISTO o PROBABILITA
								
								
								else if (tipoCella.equals("stazione"))
								{
									stazioni.add(new Caselle (idCella, nomeCella, tipoCella));
								}
								
								break;
						} // chiusura SWITCH tag diversi
						break;
						
				} // chiusura SWITCH tipo di eventi
				
				xmlr.next();
				
			} // chiusura WHILE LETTORE
		}
		catch (Exception e)
		{
			System.out.println("Errore nella lettura dati");
		}
	}
	
	public int getNumCaselle ()
	{
		return numCaselle;
	}
	
	public ArrayList <Caselle> getCaselle ()
	{
		return caselle;
	}
	
	public String getNome ()
	{
		return nome;
	}
	
	public ArrayList <Caselle> getStazioni ()
	{
		return stazioni;
	}
	
	public ArrayList <Caselle> getPrDisponibili ()
	{
		return proprietaDisp;
	}
}
