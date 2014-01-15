import java.util.ArrayList;
import java.util.List;

import donnees.DataColumn;
import donnees.MechanicData;
/**
 * Classe qui regroupe les traitements utilisés
 * @author julien
 *
 */
public class Traitement {
	
	/**
	 * Calcul la tangente d'une courbe en fonction de deux points
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return l'equation de la droite du type ax +b
	 */
	public float[] calculTangente(List<Float> x1,List<Float> y1, List<Float> x2,List<Float> y2){
		//moyenne sur quelques points pour réduire l'imprécision
		float averageX1 =0f;
		for(int i=0;i<x1.size();i++){
			averageX1 = averageX1 + x1.get(i);
		}
		averageX1 = averageX1/(x1.size());
		
		//moyenne sur le point X2
		float averageX2 =0f;
		for(int i=0;i<x2.size();i++){
			averageX2 = averageX2 + x2.get(i);
		}
		averageX2 = averageX2/(x2.size());
		
		//moyenne sur le point Y1
		float averageY1 =0f;
		for(int i=0;i<y1.size();i++){
			averageY1 = averageY1 + y1.get(i);
		}
		averageY1 = averageY1/(y1.size());
		
		//moyenne sur le point Y2
		float averageY2 =0f;
		for(int i=0;i<y2.size();i++){
			averageY2 = averageY2 + y2.get(i);
		}
		averageY2 = averageY2/(y2.size());
		//calcul du coefficient directeur a
		float a = (averageY2 - averageY1)/ (averageX2 - averageX1);
		//calcul de l'ordonnée à l'origine
		float b = averageY1 - a * averageX1;
		//on prépare la droite
		float[] equation = {a,b};
		
		return equation;
	}
	
	/**
	 * prépare les données pour le calcul de tangente en fournissant 10 points autour de l'indice des curseurs
	 * @param data
	 * @param indice
	 * @return les sous ensembles de données
	 */
	public ArrayList<Float> preparedTangenteData(List<Float> data, int indice){
		ArrayList<Float> subSet = new ArrayList<Float>();
		for(int i=-5;i<6;i++){
			subSet.add(data.get(indice+i));
		}
		
		return subSet;
	}
	
	/**
	 * Retrouve le déplacement et la Force dans la config pour avoir les numéros de colonnes
	 * @param configuration
	 * @return
	 */
	public int[] findDispAndForce(String configuration){
		int[] xy ={-1,-1};
		
		String[] confData = configuration.split("#");
		int nbCol = Integer.parseInt(confData[1]);
		for(int i=2;i<2+nbCol;i++){
			if(confData[i].equalsIgnoreCase("c_ext")){
				xy[0]= i-2; // deplacement sur les X
			}
			if(confData[i].equalsIgnoreCase("c_load")){
				xy[1]= i-2; // force sur les Y
			}
		}
		
		return xy;
	}
	
	
	/**
	 * Calcul une surface circulaire
	 * @param diametre
	 * @return
	 */
	public float calculSurface(float diametre){
		float surface =0;	
		surface = (float) (Math.PI * (diametre * diametre) /4);
		return surface;	
	}
	
	/**
	 * Calcul une surface rectangulaire
	 * @param longueur
	 * @param largeur
	 * @return
	 */
	public float calculSurface(float longueur, float largeur){
		float surface =0;
		surface = longueur * largeur;
		return surface;
	}
	
	/**
	 * Extract data de la configuration
	 * @param configuration
	 * @return
	 */
	public float[] extractData(String configuration){
		String[] confData = configuration.split("#");
		for(int i=0;i<confData.length;i++){
			if(confData[i].contains("circulaire")){
				float[] data = {-1,-1,-1};
				data[0] = Float.parseFloat(confData[i+1]);// l0
				data[1] = Float.parseFloat(confData[i+2]);// l0ext
				data[2] = Float.parseFloat(confData[i+3]);// d0
				return data;
			}
			if(confData[i].contains("rectangulaire")){
				float[] data = {-1,-1,-1,-1};
				data[0] = Float.parseFloat(confData[i+1]); //l0
				data[1] = Float.parseFloat(confData[i+2]); //l0ext
				data[2] = Float.parseFloat(confData[i+3]); //t
				data[3] = Float.parseFloat(confData[i+4]); //s
				return data;
			}
		}
		
		return null;
	}
	
	/**
	 * Calcul et ajoute la contrainte et deformation rationnel aux données
	 * @param md
	 */
	public void caculRationalFromConventional(MechanicData md){
		DataColumn contrainteRav = new DataColumn("MPa", "Rat. Eng. stress");
		DataColumn deformationRav = new DataColumn("%", "Rat. Deform.");
		
		for(int i=0; i< md.getByName("Eng. stress").size();i++){
			contrainteRav.add(md.getByName("Eng. stress").get(i) *(1 + md.getByName("Deformation").get(i)));
			
			deformationRav.add((float) (Math.log(1 + md.getByName("Deformation").get(i))));
		}
		
		md.add(contrainteRav);
		md.add(deformationRav);
	}
}
