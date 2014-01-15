package donnees;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente l'ensemble des données (colonnes)
 * @author Julien
 *
 */
public class MechanicData {
	private List<DataColumn> columns;

	/**
	 * constructeur de base
	 */
	public MechanicData(){
		columns = new ArrayList<DataColumn>();
	}
	
	public List<DataColumn> getColumns() {
		return columns;
	}
	
	/**
	 * raccourci pour renvoyer directement le nombre de colonne
	 * @return
	 */
	public int size(){
		return columns.size();
	}
	
	/**
	 * raccourci pour ajouter une colonne directement
	 * @param dc
	 */
	public void add(DataColumn dc){
		columns.add(dc);
	}
	
	/**
	 * raccourci pour renvoyer une colonne
	 * @param indice
	 * @return
	 */
	public DataColumn get(int indice){
		return columns.get(indice);
	}
	
	/**
	 * récupère une colonne quand on a son nom
	 * @param name
	 * @return
	 */
	public DataColumn getByName(String name){
		for(int i=0;i<columns.size();i++){
			if(columns.get(i).getName().equalsIgnoreCase(name)){
				return columns.get(i);
			}
		}
		
		return null;
	}
	
	/**
	 * Supprimer avant l'indice sur toutes les colonnes
	 * @param indice
	 */
	public void deleteBefore(int indice){
		for(int i=0; i<columns.size();i++){
			columns.get(i).deleteBefore(indice);
		}
	}
	
	/**
	 * Supprimer après l'indice sur toutes les colonnes
	 * @param indice
	 */
	public void deleteAfter(int indice){
		for(int i=0; i<columns.size();i++){
			columns.get(i).deleteAfter(indice);
		}
	}
	
	/**
	 * Supprimer entre les deux indices sur toutes les colonnes
	 * @param debIndice
	 * @param endIndice
	 */
	public void deleteBetween(int debIndice, int endIndice){
		for(int i=0; i<columns.size();i++){
			columns.get(i).deleteBetween(debIndice, endIndice);
		}
	}
}
