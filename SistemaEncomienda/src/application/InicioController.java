package application;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;

import data.Cliente;
import data.Factura;
import data.PesoTarifa;
import data.Tarifa;
import data.TipoEmbalaje;
import data.Unidad;
import data.VariableConfiguracion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage; 

public class InicioController {
	
	@FXML
	private MenuBar mbMenu;
	
	@FXML
	private Label lAlertaMsj;
	
	Tooltip ttEncomienda = new Tooltip();
	Tooltip ttCliente = new Tooltip();
	
	VentanaCliente ventCliente;	
	VentanaReporte1 ventReporteUno;
	VentanaConfigPest ventConfigPest;
	
	Query queryResultTipoEmbalaje, queryResultTarifa, queryResultUnidad, queryResultVarConf, queryResultPeso;
	private ObservableList<TipoEmbalaje> TipoEmbalajeList = FXCollections.observableArrayList();  //Carga datos de Tabla TipoEmbalaje 
	private ObservableList<Tarifa> TarifaList = FXCollections.observableArrayList();
	
	TipoEmbalaje objTipoEmbalaje;
	Tarifa objTarifa;
	Unidad objUnidad;
	VariableConfiguracion objVarConf;
	PesoTarifa objPeso;
	
	private boolean banderainicio = false;
	
	PruebaVentanas objPV;
	
	@FXML
	private void initialize(){
		ttEncomienda.setText("Facturar encomienda");
		ttCliente.setText("Gestionar clientes");
		try{						
			Session sesion1 = openSesion();
			queryResultTipoEmbalaje = sesion1.createQuery("from TipoEmbalaje");
			TipoEmbalajeList = FXCollections.observableArrayList(queryResultTipoEmbalaje.list());	
			closeSesion(sesion1);
		}catch(SQLGrammarException sq){
			System.out.println("No existen tablas desde INICIO CONTROLLER");
			banderainicio = true;
			Main.initdata(true);
			lAlertaMsj.setText("BASE DATOS GENERADA");
			lAlertaMsj.setVisible(true);
		}		
	}
	
	@FXML
	public void menuEncomienda() throws Exception{
		System.out.println("menu encomienda "+banderainicio);
		lAlertaMsj.setVisible(false);
		if (!banderainicio){			
			try{				
				Session sesion1 = openSesion();
				System.out.println("inicio - - - - - - - - - - - - - - - - - - - -  + + 3");
				queryResultTipoEmbalaje = sesion1.createQuery("from TipoEmbalaje");	
				queryResultTipoEmbalaje.setMaxResults(1);
				objTipoEmbalaje = new TipoEmbalaje();
				objTipoEmbalaje = (TipoEmbalaje) queryResultTipoEmbalaje.uniqueResult();					
				System.out.println("inicio - - - - - - - - - - - - - - - - - - - -  + + 4");
				closeSesion(sesion1);			
			}catch(HibernateException e){
				e.printStackTrace();
			}
			
			if (objTipoEmbalaje!=null){
				objPV = null;
				if (objPV == null){
					objPV = new PruebaVentanas();
					objPV.start(new Stage());
				}
			}else{
				lAlertaMsj.setText("DEBE INGRESAR INFORMACIÓN BÁSICA EN MÓDULO CONFIGURACIÓN");
				lAlertaMsj.setVisible(true);
			}
		}else{
			lAlertaMsj.setText("REGISTRAR INFORMACIÓN EN MÓDULO CONFIGURACIÓN");
			lAlertaMsj.setVisible(true);
		}			
	}
	
	@FXML
	public void menuCliente() throws Exception{
		System.out.println("menu cliente "+banderainicio);
		lAlertaMsj.setVisible(false);
		ventCliente = null;
		if (ventCliente == null){
			ventCliente = new VentanaCliente();
			ventCliente.start(new Stage());
		}
	}	
						
	@FXML
	public void menuConfiguracion() throws Exception{
		System.out.println("menu pestañas ! ! ! ! ! ! ! ! ! !");
		 ventConfigPest = null;
		 
		 if (ventConfigPest == null){
			 ventConfigPest = new VentanaConfigPest();
			 ventConfigPest.start(new Stage());
		 }
	}
	
	@FXML
	public void menuReporte1() throws Exception{
		System.out.println("acción reporte 1");
		
		if (ventReporteUno == null){
			ventReporteUno = new VentanaReporte1();
			ventReporteUno.start(new Stage());
		}
			
	}
	
	@FXML
	public void menuSalir() throws Exception{
		System.out.println("acción salir");
		lAlertaMsj.setVisible(false);
		System.exit(1);
	}
	
	private Session openSesion(){		
		Session sesion = Main.sesionFactory.getCurrentSession();
		sesion.beginTransaction();		
		return sesion;
	}
	
	private void closeSesion(Session sesion){		
		sesion.getTransaction().commit();
	}
}
