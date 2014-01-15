package essai;
/**
 * 
 * @author Julien Desprez
 * Classe décrivant un Essai
 */
public class EssaiModel {
	private int id;
	private String type;
	private String idAttribute;
	private String idMateriau;
	private boolean visible;
	
	/**
	 * Constructeur de la classe ESSAI
	 */
	public EssaiModel(){
		type="";
		idAttribute = "";
		idMateriau = "";
		visible = true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIdAttribute() {
		return idAttribute;
	}

	public void setIdAttribute(String idAttribute) {
		this.idAttribute = idAttribute;
	}

	public String getIdMateriau() {
		return idMateriau;
	}

	public void setIdMateriau(String idMateriau) {
		this.idMateriau = idMateriau;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	
}
