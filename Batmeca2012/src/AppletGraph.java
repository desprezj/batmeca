
import java.applet.Applet;
import java.awt.BasicStroke;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.List;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import netscape.javascript.JSObject;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;

import tracage.TracerCourbe;
import donnees.DataColumn;
import donnees.MechanicData;


/**
 * Applet permettant l'affichage de la courbe
 * 
 * @author Julien
 * 
 */
public class AppletGraph extends Applet {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("test");
	private static String contrainteConv = "Eng. stress";
	private static String deformConv = "Deformation";
	
	private ChartPanel chartPanel; // panel sur lequel on affiche la courbe
	private JFreeChart jFreeChart; // la courbe manipulée (avec toutes les
									// series dedans)
	private XYSeriesCollection dataset; // la collection de series

	private CheckboxGroup columnX = new CheckboxGroup();
	private ArrayList<Checkbox> columnY = new ArrayList<Checkbox>();

	private Label xCursor1; // label donnant la position sur l'axe des X du curseur 1
	private Label xCursor2; // label donnant la position sur l'axe des X du curseur 2
	private Label yCursor1; // label donnant la position sur l'axe des y du curseur 1
	private Label yCursor2; // label donnant la position sur l'axe des y du curseur 2
	private int xPtCursor1; // donne l'indice en donnée du curseur1
	private int xPtCursor2; // donne l'indice en donnée du curseur2
	
	private String fileName; // nom du fichier manipulé
	private int length = 0; // nombre de point max
	private int nbCol = 0; // nombre de colonne de données (dans le fichier
							// brute)
	private int nbAddCol = 0;

	private MechanicData md; // données de l'essai mécanique
	private TracerCourbe demo;
	private Traitement treatment;
	
	
	private JPanel panel1 = new JPanel(); // panel pour les radiobouton de l'axe des X							
	private JPanel panel2 = new JPanel(); // panel pour les checkbox de l'axe des Y
										
	private ArrayList<String> mechanicValue = new ArrayList<String>();
	private XYSeries cursor1 = null; // premier curseur pour les infos
	private XYSeries cursor2 = null; // deuxième curseur pour les infos
	private boolean placement = false;
	
	//Labels représentant les valeurs remarquables
	private Label young; // module young
	private Label rmLabel; // label pour le Rm max
	private Label rpLabel; // label pour R0.02
	private Label aLabel; // label pour A
	
	private ArrayList<String> historic = new ArrayList<String>();
	private List listHisto = new List(20,false); // liste de l'historique
	private String configuration = null;
	private Container content =null;
	@Override
	/**
	 * Initialisation du panel et courbe Ajouter des boutons ou listener pour
	 * nouvelles fonctionnalités
	 */
	public void init() {
		
		// Reconstitution de toutes les données coté applet
		content = this;
		content.setLayout(null);
		// Lecture de toutes les données pour les initialiser
		setAllData();
		
		String xlabel = "X";
		String ylabel = "Y";
		Checkbox tempBox;
		if (md.size() != 0) {
			tempBox = new Checkbox(md.get(0).getName() + "("+md.get(0).getUnit()+")", columnX, true);
			// radio bouton pour l'axe des X
			panel1.add(tempBox);
		}

		for (int i = 1; i < nbCol; i++) {
			tempBox = new Checkbox(md.get(i).getName() + "(" + md.get(i).getUnit() + ")",columnX, false);
			panel1.add(tempBox);
		}
		panel1.setBorder(BorderFactory.createBevelBorder(1));
		content.add(panel1);
		panel1.setBounds(150,25,980,35);
		if (md.size() != 0) {
			// checkbox pour l'axe des Y (plusieurs courbes possibles)
			tempBox = new Checkbox(md.get(0).getName() + "("+ md.get(0).getUnit() +")", true);
			columnY.add(tempBox);
			panel2.add(columnY.get(0));
		}

		for (int i = 1; i < nbCol; i++) {//add checkbox pour représenter les colonnes en Y
			tempBox = new Checkbox(md.get(i).getName() + "(" +  md.get(i).getUnit()  + ")");
			columnY.add(tempBox);
			panel2.add(columnY.get(i));
		}

		panel2.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		content.add(panel2);
		panel2.setBounds(150,65,980,35);
		//ajout du bouton de tracer de courbe
		JButton b2 = new JButton("Draw");
		Action drawButtonListener = new Action();
		b2.addActionListener(drawButtonListener);
		content.add(b2);
		b2.setBounds(45,45,100,30);
		//tracer les courbes pour la premières fois
		demo = new TracerCourbe();

		dataset = demo.createDataset(xlabel + "/" + ylabel, md.get(0).getData(),md.get(0).getData());

		jFreeChart = demo.createChart(fileName, xlabel, ylabel, dataset);

		// Put the jFreeChart in a chartPanel
		chartPanel = new ChartPanel(jFreeChart);

		// on ajoute une possibilité de placer des curseurs en cliquant
		PlaceCursor pCursor = new PlaceCursor();
		chartPanel.addChartMouseListener(pCursor);
		

		// add the chartPanel to the container (getContentPane is inherited from
		// JApplet which AppletGraph extends).
		content.add(chartPanel);
		content.setSize(new Dimension(1000, 600));
		chartPanel.setBounds(20,150,800,500);			
		
		//label qui affiche la position des curseurs
		xCursor1 = new Label("cursor1 X:               ");
		xCursor2 = new Label("cursor2 X:               ");
		yCursor1 = new Label("cursor1 Y:               ");
		yCursor2 = new Label("cursor2 Y:               ");
		content.add(xCursor1);
		content.add(yCursor1);
		content.add(xCursor2);
		content.add(yCursor2);
		xCursor1.setBounds(830,175,120,20);
		yCursor1.setBounds(830,200,120,20);
		
		xCursor2.setBounds(830,240,120,20);
		yCursor2.setBounds(830,265,120,20);
		//on ajoute tous les boutons au panel principal
		setButtons();
		
	}

	/**
	 * Pour ajouter et initialiser tous les boutons du panel
	 */
	public void setButtons(){
		//bouton pour supprimer les données avant le curseur 1
		JButton b4 = new JButton("Delete before cursor 1");
		DeleteListenerCursor1 dLC1 = new DeleteListenerCursor1();
		b4.addActionListener(dLC1);
		content.add(b4);
		b4.setBounds(20,660,200,25);
		//bouton pour supprimer les données entre 1 et 2
		JButton b8 = new JButton("Delete between cursor 1 and 2");
		DeleteListenerBetweenCursor dLBC = new DeleteListenerBetweenCursor();
		b8.addActionListener(dLBC);		
		content.add(b8);
		b8.setBounds(225,660,220,25);
		
		//bouton pour supprimer les données après curseur2
		JButton b5 = new JButton("Delete after cursor 2");
		DeleteListenerCursor2 dLC2 = new DeleteListenerCursor2();
		b5.addActionListener(dLC2);
		content.add(b5);
		b5.setBounds(450,660,200,25);
		//historique
		content.add(listHisto);
		listHisto.setBounds(830,310,200,250);
		//bouton pour sauvegarder les données + historique
		JButton b6 = new JButton("Save");
		SaveAuto sa = new SaveAuto();
		b6.addActionListener(sa);
		content.add(b6);
		b6.setBounds(20,690,100,25);
		
		//bouton pour lancer le traitement spécifique
		JButton b7 = new JButton("Specific treatment");
		SpecifiqueTreatment st = new SpecifiqueTreatment();
		b7.addActionListener(st);
		content.add(b7);
		b7.setBounds(150,690,200,25);

	}
	
	/**
	 * 
	 * @author Julien Action du bouton Draw pour tracer les courbes
	 */
	private class Action implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			draw();
			
		}

	}

	/**
	 * Class qui permet la suppression de données après le curseur 2
	 * @author Julien
	 *
	 */
	private class DeleteListenerCursor2 implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(cursor2!=null){//suppression de toutes les données après le curseur2
				md.deleteAfter(xPtCursor2);
				length = xPtCursor2;
				draw();//on retrace pour actualiser
				//set up historic
				listHisto.add("DELETE from "+xPtCursor2);
				historic.add("DELETE from "+xPtCursor2);
			}	
		}
	}
	
	/**
	 * Class qui permet la suppression de données entre le curseur 2 et 1
	 * @author Julien
	 *
	 */
	private class DeleteListenerBetweenCursor implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(cursor1!=null && cursor2!=null){
				md.deleteBetween(xPtCursor1,xPtCursor2);
				length = length - (xPtCursor2-xPtCursor1);
				draw();//on retrace pour actualiser
				
				listHisto.add("length="+length);
				listHisto.add("test="+md.get(0).size());
				//set up historic
				listHisto.add("DELETE between "+xPtCursor1 + " AND "+xPtCursor2);
				historic.add("DELETE between "+xPtCursor1 + " AND "+xPtCursor2);
			}	
		}
	}
	
	
	/**
	 * Class qui permet la suppression de données avant le curseur 1
	 * @author Julien
	 *
	 */
	private class DeleteListenerCursor1 implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(cursor1!=null){//supression de toutes les données avant le curseur 1
				md.deleteBefore(xPtCursor1);
				length = length -xPtCursor1-1;
				draw();// on retrace pour actualiser

				//set up historic
				listHisto.add("DELETE before "+xPtCursor1);
				historic.add("DELETE before "+xPtCursor1);
			}	
		}
	}
	
	
	/**
	 * Class permettant de gérer les curseur, leur placement par click
	 * @author Julien
	 *
	 */
	private class PlaceCursor implements ChartMouseListener {

		@Override
		public void chartMouseClicked(ChartMouseEvent event) {
			//on récupère les positions de l'event
			int mouseX = event.getTrigger().getX();
			int mouseY = event.getTrigger().getY();
			logger.info("x = " + mouseX + ", y = " + mouseY);
			Point2D p = chartPanel.translateScreenToJava2D(new Point(mouseX,
					mouseY));
			XYPlot plot = (XYPlot) jFreeChart.getPlot();
			Rectangle2D plotArea = chartPanel.getScreenDataArea();
			//on convertit les positions en position sur le graph
			ValueAxis domainAxis = plot.getDomainAxis();
			RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();
			ValueAxis rangeAxis = plot.getRangeAxis();
			RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();
			// lowerBound and Upper bound donne la taille du carré visible
			// Pour extraire les sous valeurs il suffira de les prendre comme
			// limite
			// System.out.println("Chart: x = " + domainAxis.getLowerBound() +
			// ", y = " + rangeAxis.getLowerBound());

			double chartX = domainAxis.java2DToValue(p.getX(), plotArea,
					domainAxisEdge);
			double chartY = rangeAxis.java2DToValue(p.getY(), plotArea,
					rangeAxisEdge);
			
			//on crée deux données pour faire une droite pour le curseur
			ArrayList<Float> xTemp = new ArrayList<Float>();
			ArrayList<Float> yTemp = new ArrayList<Float>();

			xTemp.add((float) chartX);
			xTemp.add((float) chartX);
			yTemp.add(0.0f);
			yTemp.add((float) chartY);
						
			
			//Peut poser un bug si on place le même curseur exactement au même endroit
			if (!placement) {//on place le curseur 1 une fois sur 2
				if (cursor1 != null) {
					dataset.removeSeries(cursor1);
				}
				cursor1 = demo.createSerie("cursor 1", xTemp, yTemp);
				xCursor1.setText("cursor1 X: "+chartX);//mise a jour du label de position cursor
				yCursor1.setText("cursor1 Y: "+chartY);//mise a jour du label de position cursor
				dataset.addSeries(cursor1);
				placement = true;
				xPtCursor1 = valueToIndice((float)chartX);

			} else {
				if (cursor2 != null) {
					dataset.removeSeries(cursor2);
				}
				cursor2 = demo.createSerie("cursor 2", xTemp, yTemp);
				xCursor2.setText("cursor2 X: "+chartX);//mise a jour du label de position cursor
				yCursor2.setText("cursor2 Y: "+chartY);//mise a jour du label de position cursor
				dataset.addSeries(cursor2);
				placement = false;
				xPtCursor2 = valueToIndice((float)chartX);

			}
		}

		@Override
		public void chartMouseMoved(ChartMouseEvent event) {

		}

	}

	/**
	 * Sauvegarde automatique après chaque action dans une ficheir tmp ce
	 * fichier sera envoyé lors du click sur le bouton d'envoye
	 */
	private class SaveAuto implements ActionListener{

		@Override
		/**
		 * L'action faite pour envoyer les données au javascript qui renverra a la JSP
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			//historique avec @@ pour les retours à la ligne
			StringBuilder histo = new StringBuilder();
			histo.append("");
			for(int i=0;i<historic.size();i++){
				histo.append(historic.get(i));
				histo.append("@@");
			}
			//données avec $$ pour tabulation et @@ pour les retours à la ligne
			StringBuilder dataSet = new StringBuilder();
			dataSet.append("");
			for(int j=0;j<length;j++){
				for(int i=0;i<nbCol+nbAddCol;i++){
					dataSet.append(md.get(i).get(j));
					dataSet.append("$$");
				}
				dataSet.append("@@");
			}
			// création du header d'unité
			StringBuilder unitSet = new StringBuilder();
			unitSet.append("");
			for(int i=0;i<nbCol+nbAddCol;i++){
					unitSet.append(md.get(i).getName()+"("+md.get(i).getUnit()+")");
					unitSet.append("$$");
			}

			// création du string pour les valeurs représentative
			StringBuilder repValue = new StringBuilder();
			repValue.append("");
			if(mechanicValue.size()!=0){
				for(int i=0;i<mechanicValue.size();i++){
					repValue.append(mechanicValue.get(i));
					repValue.append("$$");
			}
			}
			
			//envoie d'une requete Javascript
			JSObject window = JSObject.getWindow((Applet) content); // On récupère la page
		    String[] params = {unitSet.toString(), histo.toString(), dataSet.toString(), getParameter("savePath"), fileName, repValue.toString()};
		    window.call("transfertFunc", params); // on appelle la fonction javascript avec les arguments
		}

		
	}

	/**
	 * Permet l'inialisation de toutes les données
	 * en lisant les blalise parameter de la page
	 */
	public void setAllData() {
		treatment = new Traitement();
		fileName = getParameter("fileName");
		configuration = getParameter("configuration");
		// nombre de ligne de données
		if (getParameter("length") != null) {
			length = Integer.parseInt(getParameter("length"));
		}
		// nombre de colonnes de données
		if (getParameter("nbColumn") != null) {
			nbCol = Integer.parseInt(getParameter("nbColumn"));
		}
		md = new MechanicData();
		DataColumn dc = new DataColumn();
		md.add(dc);
		for(int i=1;i<nbCol;i++){
			dc = new DataColumn();
			md.add(dc);
		}
		
	
		// remplissage du double array de données
		String xY;
		String[] temp;

		for (int i = 0; i < length; i++) {
			xY = getParameter("XY" + i);
			temp = xY.split("#");

			for (int j = 0; j < nbCol; j++) {
				md.get(j).add(Float.parseFloat(temp[j]));
			}
		}

		// Récupération des noms de colonnes et unité
		for (int i = 0; i < nbCol; i++) {
			md.get(i).setName(getParameter("col" + i));
			md.get(i).setUnit(getParameter("DetCol" + i));
		}

	}
	
	/**
	 * Convertit une valeur en l'indice de la matrice
	 * @param value
	 * @return
	 */
	public int valueToIndice(float value){
		//on récupère quelle colonne est utilisée pour l'axe des X
		String colX = columnX.getSelectedCheckbox().getLabel();
		int x = 0;
		// On récupère le radio bouton coché
		for (int i = 0; i < nbCol; i++) {
			if ((md.get(i).getName() + "(" + md.get(i).getUnit() + ")").equals(colX)) {
				x = i;
			}
		}
		
		for(int i=0;i<length;i++){
			if(value <= md.get(x).get(i)){
				return i;
			}
		}
		
		return length;
		
	}
	
	
	/**
	 * fonction de traçage des courbes en fonction des colonnes selectionnées
	 */
	public void draw(){
		// On récupère le radio bouton coché
		String colX = columnX.getSelectedCheckbox().getLabel();
		String[] colY = new String[20];
		int nbSerie = 0;
		// on récupère la checkbox cochée
		for (int i = 0; i < columnY.size(); i++) {
			if (columnY.get(i).getState()) {
				colY[nbSerie] = columnY.get(i).getLabel();
				nbSerie++;
			}
		}

		int x = 0;
		int[] y = new int[nbSerie];
		int nbSerie2 = 0;

		// On récupère le radio bouton coché
		for (int i = 0; i < nbCol; i++) {
			if ((md.get(i).getName() + "(" + md.get(i).getUnit() + ")").equals(colX)) {
					x = i;
			}
		}

		//on récupère les valeurs des colonnes cochés
		for (int i = 0; i < nbCol; i++) {
			for (int j = 0; j < nbSerie; j++) {
				if ((md.get(i).getName() + "(" + md.get(i).getUnit() + ")").equals(colY[j])) {
					y[nbSerie2] = i;
					nbSerie2++;
				}
			}
		}

		dataset.removeAllSeries();//on clean le graph
		nbAddCol = 0;
		for (int i = 0; i < nbSerie2; i++) {//on ajoute toutes les nouvelles courbes
			XYSeriesCollection newDataset = demo.createDataset(colX + "/"+ colY[i], md.get(x).getData(), md.get(y[i]).getData());
			
			dataset.addSeries(newDataset.getSeries(0));
		}
		
		refreshAxisLabel(md.get(x).getUnit(), md.get(y[0]).getUnit());
		
	}
	
	/**
	 * Permet de grossir les traits des courbes
	 */
	public void grossirTrait(){
		XYPlot plot = jFreeChart.getXYPlot();
		 int seriesCount = plot.getSeriesCount();
		 if(seriesCount>0){
			 for (int i = 1; i < seriesCount; i++) {
				 plot.getRenderer().setSeriesStroke(i, new BasicStroke(2));
			 }
		 }
	}
	
	/**
	 * classe permettant de dispatcher sur les différents traitements spécifiques
	 * @author julien
	 *
	 */
	private class SpecifiqueTreatment implements ActionListener {
		@Override
		/**
		 * action réalisée pour le dispatching lors du clique sur le bouton
		 */
		public void actionPerformed(ActionEvent e) {
			//On récupère la configuration du fichier .par
			content.remove((JButton)e.getSource());
			String[] confData = configuration.split("#");
			// essai en traction
			if(confData[0].equalsIgnoreCase("st") || confData[0].equalsIgnoreCase("ae")){ 
				//1ère étape: on récupère les données de force et déplacement
				int[] xy=treatment.findDispAndForce(configuration);
							
				//2ème étape : on divise retrace avec F/S0 et ll/ L0
				float[] dataConfig = treatment.extractData(configuration);
				float S0 =0;
				if(dataConfig.length ==3){
					S0 = treatment.calculSurface(dataConfig[2]);
				}
				//on crée 2 colonnes pour la contrainte et deformation conventionnel
				DataColumn contrainte =new DataColumn("MPa",contrainteConv);
				DataColumn deformation =new DataColumn("mm/mm",deformConv);
				
				for(int i=0;i<length;i++){ // on calcul les valeurs en tous points
					contrainte.add(1000 * md.get(xy[1]).get(i)/S0);
					deformation.add(md.get(xy[0]).get(i)/dataConfig[1]);
				}

				md.add(contrainte);
				md.add(deformation);
				//on ajoute les checkbox pour pouvoir tracer les courbes
				addBoxToContent(contrainteConv,deformConv);
				treatment.caculRationalFromConventional(md);
				addBoxToContent("Rat. Eng. stress", "Rat. Deform.");
				
				draw();
				
				//Ajout du bouton pour tracer la tangente
				JButton b10 = new JButton("Calcul representative values");
				TangenteCreator tc = new TangenteCreator();
				b10.addActionListener(tc);
				content.add(b10);
				b10.setBounds(830,575,210,25);
			}
			content.validate();
		}
	}
	
	/**
	 * Permet le tracer de la tangente et max de la contrainte conv et deformation conv
	 * @author Julien
	 *
	 */
	private class TangenteCreator implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			ArrayList<Float> x1 = treatment.preparedTangenteData(md.getByName(deformConv).getData(), xPtCursor1);
			ArrayList<Float> y1 = treatment.preparedTangenteData(md.getByName(contrainteConv).getData(), xPtCursor1);
			ArrayList<Float> x2 = treatment.preparedTangenteData(md.getByName(deformConv).getData(), xPtCursor2);
			ArrayList<Float> y2 = treatment.preparedTangenteData(md.getByName(contrainteConv).getData(), xPtCursor2);
			//on calcul la tangente de la zone plastique
			float[] tangente = treatment.calculTangente(x1, y1, x2, y2);
			
			//3ème étape: recherche du maximum
			int maxI =	md.getByName(contrainteConv).calculIndiceMax();
			//tracer du max
			ArrayList<Float> maxTemp = new ArrayList<Float>();
			ArrayList<Float> mayTemp = new ArrayList<Float>();
			maxTemp.add(md.getByName(deformConv).get(maxI));
			maxTemp.add(md.getByName(deformConv).get(maxI));
			mayTemp.add(0.0f);
			mayTemp.add(md.getByName(contrainteConv).get(maxI));
			//tracer du maximum
			XYSeries max = demo.createSerie("MAX", maxTemp, mayTemp);
			dataset.addSeries(max);
			
			//On trace la tangente en créant ses données
			ArrayList<Float> xTemp = new ArrayList<Float>();
			ArrayList<Float> yTemp = new ArrayList<Float>();
			for(int i=0;i<md.getByName(deformConv).size();i++){
				float temp = tangente[0] * md.getByName(deformConv).get(i) + tangente[1];
				if(temp >= 0 && temp <= md.getByName(contrainteConv).get(maxI)){
					xTemp.add(md.getByName(deformConv).get(i));
					yTemp.add(tangente[0] * md.getByName(deformConv).get(i) + tangente[1]);
				}
			}
			//listHisto.add("y="+ tangente[0] +"* x + "+tangente[1]);
			//on ajoute la tangente au tracé
			XYSeries tan = demo.createSerie("tangente", xTemp, yTemp);
			dataset.addSeries(tan);
			
			//calcul de Rp 0.02
			float bRp = - tangente[0] * 0.02f;
			int indiceRp = 0;
			for(int i=0; i< md.getByName(deformConv).size();i++){
				if(md.getByName(contrainteConv).get(i) <= (tangente[0]* md.getByName(deformConv).get(i) + bRp)){
					indiceRp = i;
					break;
				}
			}
			//on trace la droite passant par Rp 0.02
			xTemp = new ArrayList<Float>();
			yTemp = new ArrayList<Float>();
			for(int i=0;i<md.getByName(deformConv).size();i++){
				float temp = tangente[0] * md.getByName(deformConv).get(i) + bRp;
				if(temp>=0 && temp<= md.getByName(contrainteConv).get(maxI)){
					xTemp.add(md.getByName(deformConv).get(i));
					yTemp.add(tangente[0] * md.getByName(deformConv).get(i) + bRp);
				}
			}
			//on ajoute la tangente au tracé
			XYSeries tan2 = demo.createSerie("tangente Rp0.02", xTemp, yTemp);
			dataset.addSeries(tan2);

			
			//calcul de A : allongement à la rupture
			float bA = md.getByName(contrainteConv).get(md.getByName(contrainteConv).size()-1) - tangente[0] * md.getByName(deformConv).get(md.getByName(deformConv).size()-1);
			int indiceA =0;
			for(int i=0; i<md.getByName(deformConv).size();i++){
				if(tangente[0] * md.getByName(deformConv).get(i) + bA >=0){
					indiceA= i;
					break;
				}
			}
			
			xTemp = new ArrayList<Float>();
			yTemp = new ArrayList<Float>();
			for(int i=0;i<md.getByName(deformConv).size();i++){
				float temp = tangente[0] * md.getByName(deformConv).get(i) + bA;
				if(temp>=0 && temp<= md.getByName(contrainteConv).get(maxI)){
					xTemp.add(md.getByName(deformConv).get(i));
					yTemp.add(tangente[0] * md.getByName(deformConv).get(i) + bA);
				}
			}
			//on ajoute la tangente au tracé
			XYSeries tan3 = demo.createSerie("tangente A", xTemp, yTemp);
			dataset.addSeries(tan3);

			// ajout du formulaire de données représentatives
			young = new Label("E = "+ tangente[0] +" MPa");
			content.add(young);
			young.setBounds(750,660,150,25);
			mechanicValue.add("E = "+ tangente[0] +" MPa");

			rmLabel = new Label("Rm = "+ md.getByName(contrainteConv).get(maxI) +" MPa");
			content.add(rmLabel);
			rmLabel.setBounds(750,690,150,25);
			mechanicValue.add("Rm = "+ md.getByName(contrainteConv).get(maxI) +" MPa");
			
			rpLabel = new Label("Rp0.02 = "+ md.getByName(contrainteConv).get(indiceRp) +" MPa");
			content.add(rpLabel);
			rpLabel.setBounds(750,720,150,25);
			mechanicValue.add("Rp0.02 = "+ md.getByName(contrainteConv).get(indiceRp) +" MPa");
			
			aLabel = new Label("A = "+ md.getByName(deformConv).get(indiceA));
			content.add(aLabel);
			aLabel.setBounds(750,750,150,25);
			mechanicValue.add("A = "+ md.getByName(deformConv).get(indiceA));
			
			content.validate();
			grossirTrait();
		}
		
	}
	
	/**
	 * Permet le rafraichissement des noms d'axes
	 */
	public void refreshAxisLabel(String X, String Y){
		jFreeChart = demo.createChart(fileName, X, Y, dataset);
		content.remove(chartPanel);
		chartPanel = new ChartPanel(jFreeChart);
		PlaceCursor pCursor = new PlaceCursor();
		chartPanel.addChartMouseListener(pCursor);
		chartPanel.validate();
		content.add(chartPanel);
		chartPanel.setBounds(20,150,800,500);	
		content.validate();
	}
	
	/**
	 * Permet l'ajout des checkbox pour les contraintes et deformations
	 * @param colName1
	 * @param colName2
	 */
	public void addBoxToContent(String colName1, String colName2){
		Checkbox tempBox = new Checkbox(md.getByName(colName1).getName() + "(" + md.getByName(colName1).getUnit() + ")",columnX, false);
		panel1.add(tempBox);
		nbCol ++;
		tempBox = new Checkbox(md.getByName(colName2).getName() + "(" + md.getByName(colName2).getUnit() + ")",columnX, false);
		panel1.add(tempBox);
		nbCol ++;
		panel1.validate();
		//De même sur le deuxième panel, on ajoute une checkbox
		tempBox = new Checkbox(md.getByName(colName1).getName() + "(" +  md.getByName(colName1).getUnit()  + ")");
		columnY.add(tempBox);
		panel2.add(tempBox);
		tempBox = new Checkbox(md.getByName(colName2).getName() + "(" +  md.getByName(colName2).getUnit()  + ")");
		columnY.add(tempBox);
		panel2.add(tempBox);
		panel2.validate();
	}
}
