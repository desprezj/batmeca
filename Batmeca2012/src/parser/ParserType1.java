package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import donnees.DataColumn;
import donnees.MechanicData;


/**
 * 
 * @author Julien Desprez
 * @Classe parser, charge, lit parse le fichier et contient ensuite toutes les informations nécessaires
 */
public class ParserType1 {

	private java.io.File dataFile;
	private static Logger logger = Logger.getLogger("test");  
	private static final int NB_MAX_LINE = 50000;
	private String headerInfo;
	private List<String> colomnHeader = new ArrayList<String>();
	private int nbColonne;
	
	private MechanicData mD = new MechanicData();
	
	/**
	 * création du parseur avec initialisation
	 * 
	 */
	public ParserType1() {
		headerInfo="";
		nbColonne = 0;
	}
	
	public String getHeaderInfo() {
		return headerInfo;
	}
	
	
	public MechanicData getmD() {
		return mD;
	}

	/**
	 * 
	 * @return renvoie le nombre de colonne
	 */
	public int getNbColonne() {
		return nbColonne;
	}

	/**
	 * Charge le fichier dans le parser
	 * @param filename le nom du fichier à charger
	 */
	public final void loadFile(String filename) {
		this.dataFile = new File(filename);
	}
	
	/**
	 * Extrait les informations du header dans le fichier brute
	 * @throws FileNotFoundException
	 */
	public final void extractHeaderInformation() throws FileNotFoundException {

		Scanner scanner = new Scanner(new FileReader(dataFile));
		scanner.useDelimiter("\t");
		scanner.nextLine();
		scanner.nextLine();
		scanner.nextLine();
		String colomn = scanner.nextLine();

		String result[] = colomn.split("\t");
		for(int i = 0;i<result.length;i++){
			this.colomnHeader.add(result[i]);
		}

		scanner.close();
	}



	/**
	 * 
	 * @return renvoie la liste des header (sec, Kn ,etc..)
	 */
	public List<String> getColomnHeader() {
		return colomnHeader;
	}
	
	/**
	 * Parser tous le fichier
	 * @throws FileNotFoundException
	 */
	public final void processLineByLine() throws FileNotFoundException {

		Scanner scanner = new Scanner(new FileReader(dataFile));
		try {
			int i = 0;
			boolean test = false;
			while ( !test) { 
				i = 0;
				while ( (scanner.hasNextLine()) && (i < ParserType1.NB_MAX_LINE)) { 
					String line = scanner.nextLine();
					line.split("\\n");
					String[] tabLine = line.split("\\t");
					if(isFloat(tabLine[0].replace(",",".").trim())){
						if(this.nbColonne == 0){
							this.nbColonne = tabLine.length;
							for(int z=0;z<nbColonne;z++){
								DataColumn dc = new DataColumn();
								mD.add(dc);
							}
						}
						Float f;
						for(int j=0;j<tabLine.length;j++){
							f = Float.parseFloat(tabLine[j].replace(",",".").trim());
							mD.get(j).add(f);
						}
					}
					else{
						
						if(headerInfo==null){
							headerInfo = line.replace("\t", "$$");
						}
						else{
							headerInfo = headerInfo + "@@" + line.replace("\t", "$$");
						}
					}	
					i++;
				}
				if(!scanner.hasNextLine()){
					test = true;
				}
				
			}
		} finally {
			scanner.close();
			logger.info("Parsage fini");
		}
		
	}
	
	/**
	 * test si une string est un float
	 * @param input
	 * @return
	 */
	private boolean isFloat( String input )  
	{  
	   try  
	   {  
	      Float.parseFloat( input );  
	      return true;  
	   } 
	   catch( Exception e)  
	   {  
	      return false;  
	   } 
	}  
	
}