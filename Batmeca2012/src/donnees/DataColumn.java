package donnees;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant la manipulation d'une colonne de données
 * @author Julien
 *
 */
public class DataColumn {
	private List<Float> data;
	private String unit;
	private String name;
	
	/**
	 * Constructeur de base
	 */
	public DataColumn(){
		data = new ArrayList<Float>();
		unit ="";
		name="";
	}
	
	/**
	 * Constructeur complexe
	 * @param unit
	 * @param name
	 */
	public DataColumn(String unit, String name){
		data = new ArrayList<Float>();
		this.unit = unit;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Float> getData() {
		return data;
	}

	public void setData(ArrayList<Float> data) {
		this.data = data;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * raccourci pour renvoyer directement la taille de l'array
	 * @return
	 */
	public int size(){
		return data.size();
	}
	
	/**
	 * raccourci pour renvoyer directement la valeur à un indice
	 * @param index
	 * @return
	 */
	public float get(int index){
		return data.get(index);
	}
	
	/**
	 * raccourci pour affecter directement une valeur à un index
	 * @param index
	 * @param value
	 */
	public void set(int index, float value){
		data.set(index, value);
	}
	
	/**
	 * raccourci pour ajouter un float dans data
	 * @param e
	 */
	public void add(float e){
		data.add(e);
	}
	
	/**
	 * On supprime toutes les données avant un indice
	 * @param indice
	 */
	public void deleteBefore(int indice){
		for(int i=indice;i>=0;i--){
				data.remove(i);
		}
	}
	
	/**
	 * On supprimer toutes les données après un indice
	 * @param indice
	 */
	public void deleteAfter(int indice){
		for(int i=data.size()-1;i>=indice;i--){
				data.remove(i);//tous les indices se décalent à chaque fois
		}
	}
	
	/**
	 * Supprime toutes les données entre deux indices
	 * @param debIndice
	 * @param endIndice
	 */
	public void deleteBetween(int debIndice, int endIndice){
		for(int i=debIndice;i<endIndice;i++){
			data.remove(debIndice);//tous les indices se décalent à chaque fois
		}
	}
	
	
	/**
	 * Calcul le max de la colonne et renvoie son index
	 * @param data
	 * @return
	 */
	public int calculIndiceMax(){
		final int twenty = 20;
		int indice = 0;
		float maxValue =0;
		float compare =0;
		
		for(int i=twenty;i<data.size()-twenty;i++){
			//On compare des moyennes sur 11 points pour avoir un résultat indépendant des variations locales
			compare = data.get(i-5)+data.get(i-4)+data.get(i-3)+data.get(i-2)+data.get(i-1)+data.get(i);
			compare = (compare + data.get(i+1)+data.get(i+2)+data.get(i+3)+data.get(i+4)+data.get(i+5))/11;
			
			if(compare> maxValue){
				maxValue = compare;
				indice =i;
			}
		}	
		return indice;
	}
}
