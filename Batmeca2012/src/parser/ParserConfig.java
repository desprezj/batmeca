package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import donnees.MechanicData;


/**
 * Classe permettant le parsing des fichier de configuration .par
 * @author Julien
 *
 */
public class ParserConfig {
	private java.io.File dataFile;
	
	private String typeEssai;
	private String inputFile;
	private String outputFile;
	private String[] columnHeader = {"","","","",""};
	private Float l0 = null;  //longueur à vide de l'éprouvette
	private Float l0ext = null; // longueur à vide de l'extenso
	private Float d0 = null; //diametre de l'eprouvette si circulaire
	private Float s0 = null; // largeur de l'éprouvette si rectangulaire
	private Float t0 = null; // longueur de l'éprouvette si rectangulaire
	private int nbCol =0; //nombre de colonne de données
	
	/**
	 * Constructeur du ParserConfi
	 */
	public ParserConfig(){
		dataFile = null;
		typeEssai =null;
		inputFile =null;
	}
	
	/**
	 * 
	 * @return le type de l'essai
	 */
	public String getTypeEssai() {
		return typeEssai;
	}
	/**
	 * 
	 * @return le fichier d'entré
	 */
	public String getInputFile() {
		return inputFile;
	}
	/**
	 * 
	 * @return le fichier de sortie resultat
	 */
	public String getOutputFile() {
		return outputFile;
	}
	/**
	 * 
	 * @return la list des colonnes
	 */
	public String[] getColumnHeader() {
		return columnHeader;
	}
	/**
	 * 
	 * @return la longueur de l'éprouvette
	 */
	public Float getL0() {
		return l0;
	}
	/**
	 * 
	 * @return la longueur de l'extenso
	 */
	public Float getL0ext() {
		return l0ext;
	}
	/**
	 * 
	 * @return le diamètre de l'éprouvette
	 */
	public Float getD0() {
		return d0;
	}
	
	public Float getS0() {
		return s0;
	}

	public Float getT0() {
		return t0;
	}

	/**
	 * Charge le fichier sur le parseur
	 * @param filename
	 */
	public final void loadFile(String filename) {
		this.dataFile = new File(filename);
	}
	
	/**
	 * Va parcourir le fichier et le parse pour en récupérer toutes les données
	 * @throws FileNotFoundException 
	 */
	public final void processLinebyLine() throws FileNotFoundException{
		Scanner scanner = new Scanner(new FileReader(dataFile));
		while (scanner.hasNextLine()){
			String line = scanner.nextLine();
			
			if(line.contains("type")){
				String[] temp = line.split("\t");
				typeEssai = temp[1].toLowerCase();
			}
			else {
				if(line.contains("L0ext")){
					String[] temp = line.split("\t");
					l0ext = Float.parseFloat(temp[1]);
				}
				else{
					if(line.contains("d0")){
						String[] temp = line.split("\t");
						d0 = Float.parseFloat(temp[1]);
					}
					else{
						if(line.contains("L0")){
							String[] temp = line.split("\t");
							l0 = Float.parseFloat(temp[1]);
						}
						else{
							if(line.contains("c_")){
								String[] temp = line.split("\t");
								columnHeader[Integer.parseInt(temp[1])-1] = temp[0];
								nbCol++;
							}
							else{
								if(line.contains("s0")){
									String[] temp = line.split("\t");
									s0 = Float.parseFloat(temp[1]);
								}
								else{
									if(line.contains("t0")){
										String[] temp = line.split("\t");
										t0 = Float.parseFloat(temp[1]);
									}
								}
							}
						}
					}
				}
			}

		}
		
		scanner.close();
	}

	public int getNbCol() {
		return nbCol;
	}

	public void setNbCol(int nbCol) {
		this.nbCol = nbCol;
	}

	@Override
	/**
	 * forme du string en fonction de si la section est circulaire ou non
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(d0 != null){// Pour une section circulaire
			sb.append(typeEssai).append("#").append(nbCol).append("#");
			for(int i=0;i<nbCol;i++){
				sb.append(columnHeader[i]).append("#");
			}
			sb.append("circulaire#"+l0).append("#").append(l0ext).append("#").append(d0);
			return sb.toString();
		}
		else{ // pour une section rectangulaire
			sb.append(typeEssai).append("#").append(nbCol).append("#");
			for(int i=0;i<nbCol;i++){
				sb.append(columnHeader[i]).append("#");
			}
			sb.append("rectangulaire#"+l0).append("#").append(l0ext).append("#").append(s0).append("#").append(t0);
			return sb.toString();
		}
	}
	
	/**
	 * On complète les header des colonnes du gestionnaire de données
	 * @param md
	 */
	public void completeHeader(MechanicData md){
		for(int i=0;i<nbCol;i++){
			switch(columnHeader[i]){
				case "c_time": md.get(i).setName("time");
								md.get(i).setUnit("sec");
								break;
				case "c_ll": md.get(i).setName("load line");
								md.get(i).setUnit("mm");
								break;
				case "c_load": md.get(i).setName("load");
								md.get(i).setUnit("kN");
								break;
				case "c_ext": md.get(i).setName("axial ext.");
								md.get(i).setUnit("mm");
								break;
				case "c_rad": md.get(i).setName("radial ext.");
								md.get(i).setUnit("mm");
								break;
			}
		}
	}

	
}
