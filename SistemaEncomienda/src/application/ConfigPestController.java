package application;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import data.CiudadDestino;
import data.DetalleVariableConfiguracion;
import data.Ipostel;
import data.PesoTarifa;
import data.PrecioTarifa;
import data.Tarifa;
import data.TipoEmbalaje;
import data.Unidad;
import data.VariableConfiguracion;
import eu.schudt.javafx.controls.calendar.DatePicker;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class ConfigPestController {
	
	@FXML	TabPane tabPane;	
	@FXML	Tab tbUnidad;	
	@FXML	Tab tbTarifa;	
	@FXML	Tab tbPesp;	
	@FXML	Tab tbVariable;
		
//	Pestaña Unidad
	@FXML	TextField tfuUnidad;	
	@FXML	TextField tfuFactor;
	@FXML	Label luAlerta;
//	Fin
	
//	Pestaña Tarifa
	@FXML	TextField tftTarifa;
	@FXML	Label ltAlerta;
//	Fin
	
//	Pestaña Peso
	Query queryResultUnidad, queryResultTabla;
	@FXML	TableView<PesoTarifa> tvpTablaPT = new TableView<PesoTarifa>();
	@FXML	TableColumn<PesoTarifa,String> tcpRangoPesoPT;
	@FXML	TableColumn<PesoTarifa,String> tcpUnidadPT;
	@FXML	ChoiceBox cbpUnidad;
	@FXML 	Label lpAlerta;
	@FXML   TextField tfpDesde;	
	@FXML   TextField tfpHasta;
	@FXML	Button bpMas;
	@FXML	Button bpEditar;		
	@FXML	Button bpMenos;
	ObservableList<PesoTarifa> itemsPesoTarifa = FXCollections.observableArrayList();
	ObservableList<Unidad> UnidadList = FXCollections.observableArrayList();
	ObservableList<String> opcionUnidad = FXCollections.observableArrayList();	
	private boolean bandUnidad = false,bandDesde = false, bandHasta = false;
	int idEditar=0,posEditar=0;
//	Fin
	
//	Pestaña Variable
	@FXML	TextField tfvVariable;
	@FXML	Label lvAlerta;
//	Fin	
	
//	Pestaña Ciudad Destino
	@FXML	TextField tfcdCiudadDestino;
	@FXML	Label lcdAlerta;
//	Fin
	
//	Pestaña Tipo Embalaje
	@FXML	TextField tfteTipoEmbalaje;
	@FXML	Label lteAlerta;
//	Fin
	
//	Pestaña Asociar Tarifa/Peso
	@FXML 	TableView tvatpTabla;
	@FXML 	TableColumn tcatpTarifa;
	@FXML 	TableColumn tcatpPeso;
	@FXML 	TableColumn tcatpUnidad;
	@FXML 	TableColumn tcatpMonto;
	@FXML	ChoiceBox cbatpTarifa;
	@FXML	ChoiceBox cbatpPeso;
	@FXML   Button batpMas;	
	@FXML   Button batpMenos;
	@FXML   Button batpEditar;
	@FXML 	TextField tfatpPrecio;
	@FXML 	Label latpMsj;
	@FXML	Label latpCodigo;
	
	private ObservableList<Tarifa> TarifaList = FXCollections.observableArrayList();
	private ObservableList<String> opcionTarifa = FXCollections.observableArrayList();
	private Map<String,Integer> MapTarifa = new LinkedHashMap<String, Integer>();
	Query queryResultTarifa;	
	private ObservableList<PesoTarifa> PesoList = FXCollections.observableArrayList();
	private ObservableList<String> opcionPeso;	
	Query queryResultPeso;
	Query queryResultPesoPrecio;	
	Query queryUnicoPeso;
	private Map<String,Integer> MapPeso = new LinkedHashMap<String, Integer>();
	
	private ObservableList<PrecioTarifa> PrecioPesoList = FXCollections.observableArrayList();
	private boolean bandTarifa = false, bandPeso = false, bandPrecio = false, bandNuevo = false, bandEditar = false;
	private boolean bandSiExiste = false;
	private String tarifaSeleccionada = null;
	private ObservableList<PrecioTarifa> itemsPrecioTarifa = FXCollections.observableArrayList();	
	Query queryResultPrecioTarifa;
	int posedit=0;
			
//	Fin
	
//	Pestaña Ipostel	
	@FXML	ChoiceBox cbiUnidad;
	@FXML 	Label liAlerta;
	@FXML   TextField tfiDesde;	
	@FXML   TextField tfiHasta;
	@FXML   TextField tfiMonto;
	@FXML	Button biMas;
	@FXML	Button biEditar;		
	@FXML	Button biMenos;
	@FXML	TableView<Ipostel> tviTablaIpostel = new TableView<Ipostel>();	
	@FXML	TableColumn<Ipostel,String> tciRangoPesoI;
	@FXML	TableColumn<Ipostel,String> tciValorI;	
	@FXML	TableColumn<Ipostel,String> tciUnidadI;
	ObservableList<Ipostel> itemsIpostel = FXCollections.observableArrayList();
//	Fin	
	
//	Pestaña Detalle Variable
	
	@FXML	GridPane gpdvFecha;
	DatePicker dpFechaVigencia;
	@FXML	Button bdvAceptar;
	@FXML	Button bdvCancelar;
	@FXML	ChoiceBox cbdvTipoVariable;
	@FXML	ChoiceBox cbdvTipoValor;
	@FXML 	TextField tfdvValorVariable;		
	@FXML	Label ldvAlerta;	
	Query queryResultTipoVariable;	
	private ObservableList<VariableConfiguracion> TipoVariableList = FXCollections.observableArrayList();
	private ObservableList<String> opcionTipoVariable = FXCollections.observableArrayList();	
	boolean bandValorVariable = false, bandFechaVigencia = false, bandTipoVariable = false, bandTipoDato = false, bandPuntoDecimal = false;		
	int posTipoVariable=0;	char [] v;
//	Fin
	
	@FXML
	private void initialize(){
		System.out.println("controlador de pestañas "+tabPane.getTabs().get(3).getText() );
		tfuFactor.addEventHandler(javafx.scene.input.KeyEvent.KEY_TYPED, libreria.decimalValidacion(8));
		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>(){
	       	@Override
			public void changed(ObservableValue<? extends Tab> arg0, Tab arg1, Tab arg2) {
//				System.out.println("arg0 "+arg0.getValue().getText());//Tab seleccionada	System.out.println("arg1 "+arg1.getText());//Tab anterior
				System.out.println("arg2 "+arg2.getText());//Tab seleccionada
				
				if (arg2.getText().equals("Unidad")){					
					contenidoUnidad();					
				}else if (arg2.getText().equals("Tarifa")){				
					contenidoTarifa();				
				}else if (arg2.getText().equals("Peso")){					
					contenidoPeso();
				}else if (arg2.getText().equals("Variable")){					
					contenidoVariable();						
				}else if (arg2.getText().equals("Cuidad Destino")){					
					contenidoCiudadDestino();
				}else if (arg2.getText().equals("Tipo Embalaje")){					
					contenidoTipoEmbalaje();				
				}else if (arg2.getText().equals("Asociar Tarifa/Peso")){					
					contenidoAsociarTarifaPeso();
				}else if (arg2.getText().equals("Ipostel")){
					contenidoIpostel();
				}else if (arg2.getText().equals("Detalle Variable")){
					contenidoDetalleVariable();
				}
	       	}
		});		
	}
	
//	Pestaña Unidad
	
	public void contenidoUnidad(){
		System.out.println("estoy en unidad");	
		tfuUnidad.setText("");tfuFactor.setText("");
		tfuUnidad.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
		    	luAlerta.setVisible(false);
		    }
		});
		tfuFactor.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
		    	luAlerta.setVisible(false);
		    }
		});		
	}
	@FXML
	public void actionUnidadAceptar(){
		System.out.println("boton aceptar unidad");
		if (tfuUnidad.getText().compareTo("") != 0 && tfuFactor.getText().compareTo("") != 0 ){
			Session sesion = openSesion();		
			Unidad objUnidad = new Unidad();
			objUnidad.setDescripcion(tfuUnidad.getText());
			objUnidad.setFactor(Double.parseDouble(tfuFactor.getText()));
			sesion.save(objUnidad);
			closeSesion(sesion);
			tfuFactor.setText("");tfuUnidad.setText("");
			luAlerta.setVisible(true);luAlerta.setText("Información ingresada exitosamente");
		}else{
			luAlerta.setVisible(true);luAlerta.setText("Debe ingresar la informaciÃ³n solicitada");
		}
	}
//	Fin Pestaña Unidad
	
//	Pestaña Tarifa
	
	public void contenidoTarifa(){
		System.out.println("estoy en tarifa");		
		tftTarifa.setText("");
		tftTarifa.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
		    	ltAlerta.setVisible(false);
		    }
		});
	}	
	@FXML
	public void actionTarifaAceptar(){
		System.out.println("boton aceptar tarifa");
		if (tftTarifa.getText().compareTo("") != 0){
			Session sesion = openSesion();		
			Tarifa objTarifa = new Tarifa();
			objTarifa.setDescripcion(tftTarifa.getText());
			sesion.save(objTarifa);
			closeSesion(sesion);
			tftTarifa.setText("");
			ltAlerta.setVisible(true);ltAlerta.setText("Información ingresada exitosamente");
		}else{
			ltAlerta.setVisible(true);ltAlerta.setText("Debe ingresar la informaciÃ³n solicitada");
		}
	}
//	Fin Pestaña Tarifa	
	
//	Pestaña Variable
	public void contenidoVariable(){
		tfvVariable.setText("");
		tfvVariable.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
		    	lvAlerta.setVisible(false);
		    }
		});	
	}
	@FXML
	public void actionVariableAceptar(){
		System.out.println("boton aceptar variable");
		if (tfvVariable.getText().compareTo("") != 0){
			Session sesion = openSesion();			
			Query queryVariableConfiguracion = sesion.createQuery("from VariableConfiguracion where nombre = :cadena");
			queryVariableConfiguracion.setParameter("cadena", tfvVariable.getText());
			queryVariableConfiguracion.setMaxResults(1);
			VariableConfiguracion objvc = (VariableConfiguracion) queryVariableConfiguracion.uniqueResult();
			
			if (objvc == null){
				System.out.println("no encontro algo con " + tfvVariable.getText());
				VariableConfiguracion objVariable = new VariableConfiguracion();
				objVariable.setNombre(tfvVariable.getText());
				sesion.save(objVariable);
				closeSesion(sesion);
				tfvVariable.setText("");
				lvAlerta.setVisible(true);lvAlerta.setText("Información ingresada exitosamente");
				//irme a la pestaña de detalle variable
			}else{
				closeSesion(sesion);
				lvAlerta.setText(tfvVariable.getText()+" ya se encuentra registrado");
				lvAlerta.setVisible(true);
				System.out.println("si encontro algo con " + objvc.getNombre());		
			}
			
//			Session sesion = openSesion();		
//			Tarifa objTarifa = new Tarifa();
//			objTarifa.setDescripcion(tftTarifa.getText());
//			sesion.save(objTarifa);
//			closeSesion(sesion);
//			tftTarifa.setText("");
//			lvAlerta.setVisible(true);lvAlerta.setText("Información ingresada exitosamente");
		}else{
			lvAlerta.setVisible(true);lvAlerta.setText("Debe ingresar la informaciÃ³n solicitada");
		}
	}
//	Fin Pestaña Variable
	
//	Pestaña Ciudad Destino
	
	public void contenidoCiudadDestino(){
		tfcdCiudadDestino.setText("");
		tfcdCiudadDestino.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
		    	lcdAlerta.setVisible(false);
		    }
		});	
	}
	@FXML
	public void actionCiudadDestinoAceptar(){
		System.out.println("boton aceptar ciudad destino");
		
		if (tfcdCiudadDestino.getText().compareTo("") != 0){
			Session sesion = openSesion();		
			CiudadDestino objCiudadDestino = new CiudadDestino();
			objCiudadDestino.setDescripcion(tfcdCiudadDestino.getText());
			sesion.save(objCiudadDestino);
			closeSesion(sesion);
			tfcdCiudadDestino.setText("");
			lcdAlerta.setVisible(true);lcdAlerta.setText("Información ingresada exitosamente");
		}else{
			lcdAlerta.setVisible(true);lcdAlerta.setText("Debe ingresar la información solicitada");
		}
		tfcdCiudadDestino.setText("");
	}
//	Fin Pestaña Ciudad Destino
	
//	Pestaña Tipo Embalaje
	public void contenidoTipoEmbalaje(){
		tfteTipoEmbalaje.setText("");
		tfteTipoEmbalaje.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
		    	lteAlerta.setVisible(false);
		    }
		});
	}	
	@FXML
	public void actionTipoEmbalajeAceptar(){
		System.out.println("boton aceptar tipo embalaje");
		if (tfteTipoEmbalaje.getText().compareTo("") != 0){
			Session sesion = openSesion();		
			TipoEmbalaje objTipoEmbalaje = new TipoEmbalaje();
			objTipoEmbalaje.setNombre(tfteTipoEmbalaje.getText());
			sesion.save(objTipoEmbalaje);
			closeSesion(sesion);
			tfcdCiudadDestino.setText("");
			lteAlerta.setVisible(true);lteAlerta.setText("Información ingresada exitosamente");
		}else{
			lteAlerta.setVisible(true);lteAlerta.setText("Debe ingresar la información solicitada");
		}
	}
//	Fin Pestaña Tipo Embalaje
	
//	Pestaña Asociar Tarifa/Peso	
	public void contenidoAsociarTarifaPeso(){
		System.out.println("estoy en asociar tarifa/peso");		
		tfatpPrecio.setText("");
		itemsPrecioTarifa.clear();
		tvatpTabla.setItems(itemsPrecioTarifa);
		tcatpTarifa.setCellValueFactory(new PropertyValueFactory<PrecioTarifa,String>("tarifa"));
		tcatpPeso.setCellValueFactory(new PropertyValueFactory<PrecioTarifa,String>("PesoTarifa"));
		
		tcatpUnidad.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PrecioTarifa, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<PrecioTarifa, String> arg0) {
				return new SimpleStringProperty(""+arg0.getValue().getPesoTarifa().getUndMedida().getDescripcion());
			}			
		});	
		
		tcatpMonto.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PrecioTarifa,String>, ObservableValue<String>>(){
		     @Override
		     public ObservableValue<String> call(CellDataFeatures<PrecioTarifa,String> arg0) {
		      // TODO Auto-generated method stub
		      DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		 	  simbolos.setDecimalSeparator('.');
		      DecimalFormat xx = new DecimalFormat("##.00",simbolos);
		      return new SimpleStringProperty(""+xx.format(arg0.getValue().getMonto()) );
		     }
	});        
		
		tvatpTabla.setColumnResizePolicy(tvatpTabla.UNCONSTRAINED_RESIZE_POLICY);	
		    
		try{		
			//------------- CARGA DE TARIFA				
			queryResultTarifa=null; TarifaList=null; opcionTarifa.clear();
			cbatpTarifa.setItems(opcionTarifa);
			Session sesion1 = openSesion();	
			queryResultTarifa = sesion1.createQuery("from Tarifa");	
			TarifaList = FXCollections.observableArrayList(queryResultTarifa.list());
			MapTarifa = new LinkedHashMap<String, Integer>();
			opcionTarifa = FXCollections.observableArrayList();
			for (int r=0;r<TarifaList.size();r++){
				MapTarifa.put(TarifaList.get(r).getDescripcion(),TarifaList.get(r).getCodigo());
				opcionTarifa.add(TarifaList.get(r).getDescripcion());
			}
			
			cbatpTarifa.setItems(opcionTarifa);
			closeSesion(sesion1);

		}catch(HibernateException e){
			e.printStackTrace();
		}
		
		cbatpTarifa.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){			
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				
				if (!opcionTarifa.isEmpty()){
					System.out.println(" holahola " + opcionTarifa.get(arg2.intValue()));				
					tarifaSeleccionada = opcionTarifa.get(arg2.intValue());
				}
				System.out.println(" - - - - - - --- - - - - - - - - - - - - - - - - - - ");
				System.out.println("tarifa seleccionada:   "+tarifaSeleccionada+ " desde el hlm: " + MapTarifa.get(tarifaSeleccionada));
				latpMsj.setVisible(false);				batpMas.setDisable(false);
				tfatpPrecio.setDisable(false);				cbatpPeso.setDisable(false);
				cargaPeso();	bandTarifa=true;		tfatpPrecio.setText("");
				
				if ((bandTarifa) && (arg2.intValue() != -1)){
					try{							
						itemsPrecioTarifa.clear();
						Session sesion1 = openSesion();
						queryResultPrecioTarifa = sesion1.createQuery("from PrecioTarifa where codTarifa = :cd");
						queryResultPrecioTarifa.setInteger("cd", TarifaList.get(arg2.intValue()).getCodigo() );						
						itemsPrecioTarifa = FXCollections.observableArrayList(queryResultPrecioTarifa.list());
						
						if (itemsPrecioTarifa.isEmpty())
							System.out.println("es null");
						
						tvatpTabla.setItems(itemsPrecioTarifa);
						closeSesion(sesion1);
					}catch(HibernateException e){
						e.printStackTrace();
					}
				}
		}});
		
		tfatpPrecio.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
		    	bandPrecio = true;
		    	if (tfatpPrecio.getText().compareTo("")==0)
		    		bandPrecio = false;
		    	cbatpTarifa.setDisable(true); cbatpTarifa.setOpacity(1);
		    	latpMsj.setVisible(false);		
		    }
		});
		
		tvatpTabla.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PrecioTarifa>(){
			@Override
			public void changed(ObservableValue<? extends PrecioTarifa> arg0, PrecioTarifa arg1, PrecioTarifa arg2) {
				
				if (!itemsPrecioTarifa.isEmpty()){															
					latpCodigo.setText(String.valueOf(arg2.getCodigo()));
					for (int y=0;y<itemsPrecioTarifa.size();y++){
						if (latpCodigo.getText().compareTo(String.valueOf(itemsPrecioTarifa.get(y).getCodigo())) == 0){
							posedit=y;
						}
					}
					// cargar el combobox de peso segun el registro seleccionado para actualizar															
					opcionPeso=null;    opcionPeso=FXCollections.observableArrayList();	
					opcionPeso.add(arg2.getPesoTarifa().toString());
					cbatpPeso.setItems(opcionPeso);
					cbatpPeso.setDisable(true); cbatpPeso.setOpacity(1);	
					cbatpPeso.getSelectionModel().select(arg2.getPesoTarifa().toString());
					
					cbatpTarifa.setDisable(true); cbatpTarifa.setOpacity(1);
					System.out.println("Precio del registro: "+ arg2.getMonto());
					tfatpPrecio.setVisible(true);
					tfatpPrecio.setText(String.valueOf(arg2.getMonto()));
					
					batpMas.setVisible(false);
					batpEditar.setDisable(false);
					batpMenos.setDisable(false);
				}			
			}
		});		
		
		tfatpPrecio.addEventHandler(javafx.scene.input.KeyEvent.KEY_TYPED, libreria.decimalValidacion(10));		

	}
	
	private void cargaPeso(){
		System.out.println("Carga peso");
		System.out.println(MapTarifa.get(tarifaSeleccionada));
		bandSiExiste=false;	cbatpPeso.setDisable(false);
		try{			
			Session sesion3 = openSesion();	
			queryResultPesoPrecio = sesion3.createQuery("from PrecioTarifa where codTarifa = :cod");
			queryResultPesoPrecio.setInteger("cod",MapTarifa.get(tarifaSeleccionada));
			PrecioPesoList = FXCollections.observableArrayList(queryResultPesoPrecio.list());
								
			queryResultPeso = sesion3.createQuery("from PesoTarifa");
			PesoList = FXCollections.observableArrayList(queryResultPeso.list());
			opcionPeso=null;    opcionPeso=FXCollections.observableArrayList();	
			MapPeso = null;		MapPeso = new LinkedHashMap<String, Integer>();
			
			if ((PrecioPesoList.size()>0) && (PrecioPesoList.size()< PesoList.size())){
				for (int a=0;a<PesoList.size();a++){
					for (int b=0;b<PrecioPesoList.size();b++){
						if (PesoList.get(a).getCodigo() == PrecioPesoList.get(b).getPesoTarifa().getCodigo()){
							bandSiExiste = true;
							break;								
						}
					}
					if (!bandSiExiste){
						opcionPeso.add(PesoList.get(a).toString());
						MapPeso.put(PesoList.get(a).toString(),PesoList.get(a).getCodigo());
					}												
					bandSiExiste = false;
				}							
			}else if (PrecioPesoList.size() == 0){
				batpMenos.setDisable(true);
				batpEditar.setDisable(true);				
				latpMsj.setVisible(true);
				latpMsj.setText("No existen pesos asociados a la tarifa seleccionada");
				for (int a=0;a<PesoList.size();a++){
					opcionPeso.add(PesoList.get(a).toString());
					MapPeso.put(PesoList.get(a).toString(),PesoList.get(a).getCodigo());
				}
				cbatpTarifa.setDisable(false); cbatpTarifa.setOpacity(1);
			}else if (PrecioPesoList.size() == PesoList.size()){
				latpMsj.setVisible(true);
				latpMsj.setText("Todos los pesos existentes ya han sido asociados");
				batpMas.setDisable(true);
				tfatpPrecio.setDisable(true);
				cbatpPeso.setDisable(true);
			}
			System.out.println("map: "+MapPeso);	
			cbatpPeso.setItems(opcionPeso);
			closeSesion(sesion3);
		}catch(HibernateException e){
			e.printStackTrace();
		}			
	}
	
	@FXML
	private void actionatpBotonMas(){
		System.out.println("Mas  "+tfatpPrecio.getText());	
		bandNuevo = true;
		batpEditar.setDisable(false);
		batpMenos.setDisable(false);
		cbatpPeso.setDisable(false); cbatpPeso.setOpacity(1);	
		
		PrecioTarifa objpf = new PrecioTarifa();
		PesoTarifa objPeso;
		
		try{
			itemsPrecioTarifa.clear();
			Session sesion1 = openSesion();	
			queryResultPrecioTarifa = sesion1.createQuery("from PrecioTarifa where codTarifa = :ct and codPesoTarifa = :cpt");
			System.out.println("tarifa seleccionada:  "+ TarifaList.get(cbatpTarifa.getSelectionModel().getSelectedIndex()).getCodigo()  );
			System.out.println("peso seleccionado codigo:  "+  MapPeso.get(cbatpPeso.getSelectionModel().getSelectedItem().toString()));
			System.out.println("peso seleccionado:  "+  cbatpPeso.getSelectionModel().getSelectedItem().toString() );
			
	   	    queryResultPrecioTarifa.setInteger("ct", TarifaList.get(cbatpTarifa.getSelectionModel().getSelectedIndex()).getCodigo());
			queryResultPrecioTarifa.setInteger("cpt", MapPeso.get(cbatpPeso.getSelectionModel().getSelectedItem().toString()));				
			itemsPrecioTarifa = FXCollections.observableArrayList(queryResultPrecioTarifa.list());
			
			System.out.println("cuando no hay nada:  "+itemsPrecioTarifa.isEmpty() + " / size: " +itemsPrecioTarifa.size());
			
			if (itemsPrecioTarifa.isEmpty()){				
				//Consulta para traer de tabla peso, el objeto segun el codigo que esta en Map de Peso
				if (!MapPeso.isEmpty()){
					queryUnicoPeso = sesion1.createQuery("from PesoTarifa where codigo = :codp");
					queryUnicoPeso.setInteger("codp", MapPeso.get(cbatpPeso.getSelectionModel().getSelectedItem().toString()));
					queryUnicoPeso.setMaxResults(1);
				}
				objPeso = new PesoTarifa();
				objPeso = (PesoTarifa) queryUnicoPeso.uniqueResult();
								
				if (!TarifaList.isEmpty())
					objpf.setTarifa(TarifaList.get(cbatpTarifa.getSelectionModel().getSelectedIndex()));
				
				objpf.setPesoTarifa(objPeso);
				objpf.setMonto(Double.parseDouble(tfatpPrecio.getText()));
				sesion1.save(objpf);
				latpMsj.setVisible(true);
				latpMsj.setText("Registro guardado exitosamente");
				
				queryResultPrecioTarifa = sesion1.createQuery("from PrecioTarifa where codTarifa = :ct");
				queryResultPrecioTarifa.setInteger("ct", TarifaList.get(cbatpTarifa.getSelectionModel().getSelectedIndex()).getCodigo());
				itemsPrecioTarifa = FXCollections.observableArrayList(queryResultPrecioTarifa.list());
				closeSesion(sesion1);
				System.out.println("tamaño para la tabla " + itemsPrecioTarifa.size());
				tvatpTabla.setItems(itemsPrecioTarifa);
				
			}else if (!itemsPrecioTarifa.isEmpty()){
				latpMsj.setVisible(true);
				latpMsj.setText("Relación tarifa y precios existentes");
			}	
		}catch(HibernateException e){
			e.printStackTrace();
		}			
		cargaPeso();
		
		tfatpPrecio.setText("");
		cbatpTarifa.setDisable(false); cbatpTarifa.setOpacity(1);	
	}
	
	@FXML
	private void actionatpBotonMenos(){
		System.out.println("menos " + latpCodigo.getText() + "   " + cbatpTarifa.getSelectionModel().getSelectedIndex());
		PrecioTarifa objpf;
		
		try{
			Session sesion1 = openSesion();
			itemsPrecioTarifa.clear();							
			queryResultPrecioTarifa = sesion1.createQuery("from PrecioTarifa where codigo = :codt");
			queryResultPrecioTarifa.setInteger("codt", Integer.parseInt(latpCodigo.getText()) );
			queryResultPrecioTarifa.setMaxResults(1);
			objpf = new PrecioTarifa();
			objpf = (PrecioTarifa) queryResultPrecioTarifa.uniqueResult();
			sesion1.delete(objpf);	

			latpMsj.setText("Registro eliminado exitosamente");
			latpMsj.setVisible(true);
			System.out.println("listo!: "+ TarifaList.get(cbatpTarifa.getSelectionModel().getSelectedIndex()).getCodigo() );
			queryResultPrecioTarifa = sesion1.createQuery("from PrecioTarifa where codTarifa = :ct");
			queryResultPrecioTarifa.setInteger("ct", TarifaList.get(cbatpTarifa.getSelectionModel().getSelectedIndex()).getCodigo() );
			itemsPrecioTarifa = FXCollections.observableArrayList(queryResultPrecioTarifa.list());					
			tvatpTabla.setItems(itemsPrecioTarifa);
			closeSesion(sesion1);
			tfatpPrecio.setText("");			
			
		}catch(HibernateException e){
			e.printStackTrace();
		}		
		batpMas.setVisible(true);
		cbatpTarifa.setDisable(false);
		cargaPeso();
	}
	
	@FXML
	private void actionatpBotonEditar(){
		bandEditar = true;
		PrecioTarifa objpf;
			
		try{
			Session sesion1 = openSesion();
			objpf = (PrecioTarifa) sesion1.get(PrecioTarifa.class, Integer.parseInt(latpCodigo.getText()));
			objpf.setMonto(Double.parseDouble(tfatpPrecio.getText()));
			sesion1.update(objpf);		
			closeSesion(sesion1);
			latpMsj.setText("Registro actualizado exitosamente");
			latpMsj.setVisible(true);	
			
			itemsPrecioTarifa.set(posedit, objpf);			
			tvatpTabla.setItems(itemsPrecioTarifa);			
		}catch(HibernateException e){
			e.printStackTrace();
		}	
		tfatpPrecio.setText("");
		cbatpTarifa.setDisable(false); cbatpTarifa.setOpacity(1);
		batpMas.setDisable(false); batpMas.setVisible(true); batpMas.setOpacity(1);
		
		cargaPeso();
		cbatpPeso.setDisable(false); cbatpPeso.setOpacity(1);
	}
//	Fin
	
//	Pestaña Peso
	
	public void contenidoPeso(){
		Session sesion1 = openSesion();		
		tfpDesde.setText(""); tfpHasta.setText("");
		queryResultUnidad = null; UnidadList = null; opcionUnidad.clear();
		cbpUnidad.setItems(opcionUnidad);
		queryResultUnidad = sesion1.createQuery("from Unidad");
		UnidadList = FXCollections.observableArrayList(queryResultUnidad.list());
		for (int r=0;r<UnidadList.size();r++){
			opcionUnidad.add(UnidadList.get(r).getDescripcion());
		}
		
		cbpUnidad.setItems(opcionUnidad);
		cbpUnidad.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){			
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				
				if (!opcionUnidad.isEmpty())
					System.out.println(opcionUnidad.get(arg2.intValue()));
				
				bandUnidad=true;
				lpAlerta.setVisible(false);
		}});
		
		tfpDesde.lengthProperty().addListener(new ChangeListener<Number>(){
		     @Override
		     public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {	           
		    	 bandDesde = true;     lpAlerta.setVisible(false);
		    	 System.out.println("idEditar desde tfdesde: "+idEditar);
		}});
		
		tfpHasta.lengthProperty().addListener(new ChangeListener<Number>(){
		     @Override
		     public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {	           
		    	 bandHasta = true;
			     lpAlerta.setVisible(false);
			     System.out.println("idEditar desde tfhasta: "+idEditar);	
		}});		
		
		
		tcpRangoPesoPT.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PesoTarifa, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<PesoTarifa, String> arg0) {
				return new SimpleStringProperty(""+arg0.getValue().getDesde()+"-"+arg0.getValue().getHasta());
			}			
		});
		
		tcpUnidadPT.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PesoTarifa, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<PesoTarifa, String> arg0) {
				return new SimpleStringProperty(""+arg0.getValue().getUndMedida().getDescripcion());
			}			
		});
		
		tvpTablaPT.setColumnResizePolicy(tvpTablaPT.UNCONSTRAINED_RESIZE_POLICY);	
		tvpTablaPT.setVisible(true);
		queryResultTabla = sesion1.createQuery("from PesoTarifa");
		itemsPesoTarifa = FXCollections.observableArrayList(queryResultTabla.list()); 
		tvpTablaPT.setItems(itemsPesoTarifa);
		tvpTablaPT.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PesoTarifa>(){
			@Override
			public void changed(ObservableValue<? extends PesoTarifa> arg0, PesoTarifa arg1,PesoTarifa arg2) {
					bpMas.setDisable(true);	
					bpEditar.setDisable(false);
					if (arg2 != null){
						System.out.println("Opción de tabla seleccionada arg2: "+arg2.getDesde()+" "+arg2.getHasta()+" "+arg2.getUndMedida().getDescripcion());
						tfpDesde.setText(String.valueOf(arg2.getDesde()));
						tfpHasta.setText(String.valueOf(arg2.getHasta()));
						idEditar = arg2.getCodigo();
						
						for (int d=0;d<UnidadList.size();d++){
							if (UnidadList.get(d).getCodigo() == arg2.getUndMedida().getCodigo()){
								cbpUnidad.getSelectionModel().select(d);
								break;
							}
						}	
						
						for (int d=0;d<itemsPesoTarifa.size();d++){
							if (itemsPesoTarifa.get(d).getCodigo() == arg2.getCodigo())
								posEditar=d;
						}
					}
			}			
		});	
		closeSesion(sesion1);
		tfpDesde.addEventHandler(javafx.scene.input.KeyEvent.KEY_TYPED, libreria.decimalValidacion(10));
		tfpHasta.addEventHandler(javafx.scene.input.KeyEvent.KEY_TYPED, libreria.decimalValidacion(10));	
	}
	
	@FXML
	private void actionpBotonMas(){
		if (!tfpDesde.getText().equals("") && !tfpHasta.getText().equals("") 
				&& cbpUnidad.getSelectionModel().getSelectedIndex()!=-1){				
			Session sesion1 = openSesion();	
			Query queryResult = sesion1.createQuery("from PesoTarifa");
			
			//if (validacionRango(queryResult)){
				System.out.println("inserto!!!!!!!! peso tarifa");
				PesoTarifa obj = new PesoTarifa();
				obj.setDesde(Double.parseDouble(tfpDesde.getText()));
				obj.setHasta(Double.parseDouble(tfpHasta.getText()));
				obj.setUndMedida(UnidadList.get(cbpUnidad.getSelectionModel().getSelectedIndex()));
				sesion1.save(obj);
				
				queryResultTabla = sesion1.createQuery("from PesoTarifa");
				itemsPesoTarifa = FXCollections.observableArrayList(queryResultTabla.list());
				tvpTablaPT.setItems(itemsPesoTarifa);
				tfpDesde.setText("");			tfpHasta.setText("");	
			//}
			closeSesion(sesion1);
		}
	}
	
	@FXML
	private void actionpBotonMenos(){
		System.out.println("Quiere eliminar: " + idEditar);		
		try{
			Query queryResultEliminar;	
			
			PesoTarifa objpt;
			Session sesion1 = openSesion();
			queryResultEliminar = sesion1.createQuery("from PesoTarifa where Codigo = :codBor");
			queryResultEliminar.setInteger("codBor",idEditar);
			queryResultEliminar.setMaxResults(1);
			objpt = (PesoTarifa) queryResultEliminar.uniqueResult();
			
			sesion1.delete(objpt);
			queryResultTabla = sesion1.createQuery("from PesoTarifa");
			itemsPesoTarifa = FXCollections.observableArrayList(queryResultTabla.list());				
			tvpTablaPT.setItems(itemsPesoTarifa);
			tfpDesde.setText("");			tfpHasta.setText("");	
			bpMas.setDisable(false);			bpEditar.setDisable(false);
			closeSesion(sesion1);				
			
			lpAlerta.setText("Eliminación exitosa");
			lpAlerta.setVisible(true);
			
		}catch(ConstraintViolationException ee){
			lpAlerta.setText("El rango no se puede eliminar, se encuentra relacionado con factura");
			lpAlerta.setLayoutX(10);
			lpAlerta.setVisible(true);
		}
	}
	
	@FXML
	private void actionpBotonEditar(){
		Session sesion1 = openSesion();
		Query queryResult = sesion1.createQuery("from PesoTarifa");		

		PesoTarifa objpt;
		objpt = (PesoTarifa) sesion1.get(PesoTarifa.class, idEditar);
		objpt.setDesde(Double.parseDouble(tfpDesde.getText()));
		objpt.setHasta(Double.parseDouble(tfpHasta.getText()));
		if (cbpUnidad.getSelectionModel().getSelectedIndex() != -1)
			objpt.setUndMedida(UnidadList.get(cbpUnidad.getSelectionModel().getSelectedIndex()));
		sesion1.update(objpt);
		
		itemsPesoTarifa.set(posEditar,objpt);
		tvpTablaPT.setItems(itemsPesoTarifa);
		
		lpAlerta.setText("Registro actualizado exitosamente");
		lpAlerta.setVisible(true);		idEditar=0;				
		bpEditar.setDisable(true);		bpMas.setDisable(false);
		tfpDesde.setText("");			tfpHasta.setText("");

		closeSesion(sesion1);
	}
// 	Fin Peso	

//	Pestaña Ipostel
	public void contenidoIpostel(){
		queryResultUnidad = null; UnidadList = null;  opcionUnidad.clear();
		cbiUnidad.setItems(opcionUnidad);
		tfiDesde.setText("");tfiHasta.setText("");tfiMonto.setText("");
		
		Session sesion1 = openSesion();
		queryResultUnidad = sesion1.createQuery("from Unidad where factor <= 1");
		
		UnidadList = FXCollections.observableArrayList(queryResultUnidad.list());
		for (int r=0;r<UnidadList.size();r++){
			opcionUnidad.add(UnidadList.get(r).getDescripcion());
		}
		
		cbiUnidad.setItems(opcionUnidad);
		cbiUnidad.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){			
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				
				if (!opcionUnidad.isEmpty())
					System.out.println(opcionUnidad.get(arg2.intValue()));
				
				bandUnidad=true;
				liAlerta.setVisible(false);
		}});
		
		tfiDesde.lengthProperty().addListener(new ChangeListener<Number>(){
		     @Override
		     public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {	           
		    	 bandDesde = true;     liAlerta.setVisible(false);
		    	 System.out.println("idEditar desde tfdesde: "+idEditar);
		}});
		
		tfiHasta.lengthProperty().addListener(new ChangeListener<Number>(){
		     @Override
		     public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {	           
		    	 bandHasta = true;
			     liAlerta.setVisible(false);
			     System.out.println("idEditar desde tfhasta: "+idEditar);	
		}});
		
		tfiDesde.addEventHandler(javafx.scene.input.KeyEvent.KEY_TYPED, libreria.decimalValidacion(10));
		tfiHasta.addEventHandler(javafx.scene.input.KeyEvent.KEY_TYPED, libreria.decimalValidacion(10));
		
		queryResultTabla = sesion1.createQuery("from Ipostel");
		itemsIpostel = FXCollections.observableArrayList(queryResultTabla.list()); 
		tviTablaIpostel.setItems(itemsIpostel);
		closeSesion(sesion1);
		tviTablaIpostel.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ipostel>(){
			@Override
			public void changed(ObservableValue<? extends Ipostel> arg0, Ipostel arg1,Ipostel arg2) {
					biMas.setDisable(true);
					biEditar.setDisable(false);
					if (arg2 != null){
						System.out.println("Opción de tabla seleccionada arg2: "+arg2.getDesde()+" "+arg2.getHasta()+" "+arg2.getValor()+" "+arg2.getUndMedida().getDescripcion());
						tfiDesde.setText(String.valueOf(arg2.getDesde()));
						tfiHasta.setText(String.valueOf(arg2.getHasta()));
						tfiMonto.setText(String.valueOf(arg2.getValor()));
						idEditar = arg2.getCodigo();
						
						for (int d=0;d<UnidadList.size();d++){
							if (UnidadList.get(d).getCodigo() == arg2.getUndMedida().getCodigo()){
								cbiUnidad.getSelectionModel().select(d);
								break;
							}
						}
						
						for (int d=0;d<itemsIpostel.size();d++){
							if (itemsIpostel.get(d).getCodigo() == arg2.getCodigo())
								posEditar=d;
						}
					}
			}			
		});		
		
		tciRangoPesoI.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ipostel, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<Ipostel, String> arg0) {
				return new SimpleStringProperty(""+arg0.getValue().getDesde()+"-"+arg0.getValue().getHasta());
			}			
		});
	
		tciValorI.setCellValueFactory(new PropertyValueFactory<Ipostel,String>("Valor"));		
		
		tciUnidadI.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ipostel, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<Ipostel, String> arg0) {
				return new SimpleStringProperty(""+arg0.getValue().getUndMedida().getDescripcion());
			}			
		});
	
		tviTablaIpostel.setColumnResizePolicy(tviTablaIpostel.UNCONSTRAINED_RESIZE_POLICY);		
	}	
	@FXML
	private void actioniBotonMenos(){
		try{
			Query queryResultEliminar;
			
			Ipostel obji;
			Session sesion1 = openSesion();
			queryResultEliminar = sesion1.createQuery("from Ipostel where Codigo = :codBor");
			queryResultEliminar.setInteger("codBor",idEditar);
			queryResultEliminar.setMaxResults(1);
			obji = (Ipostel) queryResultEliminar.uniqueResult();
			sesion1.delete(obji);
			queryResultTabla = sesion1.createQuery("from Ipostel");
			itemsIpostel = FXCollections.observableArrayList(queryResultTabla.list()); 
			tviTablaIpostel.setItems(itemsIpostel);
			tfiDesde.setText("");tfiHasta.setText("");tfiMonto.setText("");
			biMas.setDisable(false);
			biEditar.setDisable(false);
			closeSesion(sesion1);
			liAlerta.setText("Eliminación exitosa");
			liAlerta.setVisible(true);
			
		}catch(ConstraintViolationException ee){
			liAlerta.setText("El rango no se puede eliminar, se encuentra relacionado con factura");
			liAlerta.setLayoutX(10);
			liAlerta.setVisible(true);
		}
			
	}
	@FXML
	private void actioniBotonMas(){
		if (!tfiDesde.getText().equals("") && !tfiHasta.getText().equals("") && !tfiMonto.getText().equals("") 
				&& cbiUnidad.getSelectionModel().getSelectedIndex()!=-1){				
			Session sesion1 = openSesion();					
			Query queryResult = sesion1.createQuery("from Ipostel");
					
			System.out.println("inserto!!!! Ipostel");
			Ipostel obj = new Ipostel();
			obj.setDesde(Double.parseDouble(tfiDesde.getText()));
			obj.setHasta(Double.parseDouble(tfiHasta.getText()));
			obj.setValor(Double.parseDouble(tfiMonto.getText()));
			obj.setUndMedida(UnidadList.get(cbiUnidad.getSelectionModel().getSelectedIndex()));
			sesion1.save(obj);
			
			queryResultTabla = sesion1.createQuery("from Ipostel");
			itemsIpostel = FXCollections.observableArrayList(queryResultTabla.list()); 
			tviTablaIpostel.setItems(itemsIpostel);
			tfiDesde.setText("");tfiHasta.setText("");tfiMonto.setText("");
			
			closeSesion(sesion1);
		}else{
			liAlerta.setText("Debe ingresar toda la información");
			liAlerta.setVisible(true);
		}
	}	
	@FXML
	private void actioniBotonEditar(){
		Session sesion1 = openSesion();
		Query queryResult = sesion1.createQuery("from Ipostel");
		
		Ipostel obji = (Ipostel) sesion1.get(Ipostel.class, idEditar);	
		obji.setDesde(Double.parseDouble(tfiDesde.getText()));
		obji.setHasta(Double.parseDouble(tfiHasta.getText()));
		obji.setValor(Double.parseDouble(tfiMonto.getText()));
		if (cbiUnidad.getSelectionModel().getSelectedIndex() != -1)
			obji.setUndMedida(UnidadList.get(cbiUnidad.getSelectionModel().getSelectedIndex()));
		sesion1.update(obji);
		
		itemsIpostel.set(posEditar, obji);
		tviTablaIpostel.setItems(itemsIpostel);
		
		liAlerta.setText("Registro actualizado exitosamente");
		liAlerta.setVisible(true);
		idEditar=0;				
		biEditar.setDisable(true);
		biMas.setDisable(false);
		tfiDesde.setText("");tfiHasta.setText("");tfiMonto.setText("");
		
		closeSesion(sesion1);
	}
//	Fin Pestaña Ipostel
	
//	Pestaña Detalle Variable
	
	public void contenidoDetalleVariable(){
		bdvAceptar.setDisable(false);
		tfdvValorVariable.setText("");
		
		dpFechaVigencia = new DatePicker();		
		dpFechaVigencia.setDateFormat( new SimpleDateFormat("dd-MM-yyyy"));
		dpFechaVigencia.getCalendarView().setShowTodayButton(false);
		dpFechaVigencia.getCalendarView().setShowWeeks(false);
		dpFechaVigencia.getStylesheets().add(getClass().getResource("DatePicker.css").toExternalForm());		
		System.out.println("fecha seleccionada:  "+dpFechaVigencia.getSelectedDate());
		gpdvFecha.add(dpFechaVigencia,0,0);
				
		
//		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
//		simbolos.setDecimalSeparator('.');
//		final DecimalFormat decimalFormat = new DecimalFormat("##.00",simbolos);
		
		try{			
			Session sesion1 = openSesion();
			queryResultTipoVariable = null;						
			TipoVariableList = null;	opcionTipoVariable.clear();
			cbdvTipoVariable.setItems(opcionTipoVariable);
			queryResultTipoVariable = sesion1.createQuery("from VariableConfiguracion");
			TipoVariableList = FXCollections.observableArrayList(queryResultTipoVariable.list());
			
			for (int r=0;r<TipoVariableList.size();r++)
				opcionTipoVariable.add(TipoVariableList.get(r).getNombre());
			
			cbdvTipoVariable.setItems(opcionTipoVariable);
			cbdvTipoVariable.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){			
				@Override
				public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
					ldvAlerta.setVisible(false);
					if (!opcionTipoVariable.isEmpty())
						System.out.println(opcionTipoVariable.get(arg2.intValue()));				
			}});
			closeSesion(sesion1);
		}catch(HibernateException e){	e.printStackTrace();	}
		
		tfdvValorVariable.setOnAction(new EventHandler(){
			@Override
			public void handle(Event arg0) {
				tfdvValorVariable.addEventHandler(javafx.scene.input.KeyEvent.KEY_TYPED, libreria.porcentajeValidacion());
//				http://code.makery.ch/blog/javafx-8-event-handling-examples/				
			}});
		
		tfdvValorVariable.textProperty().addListener(new ChangeListener<String>(){
			 @Override
			    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {				 
				 	
				 ldvAlerta.setVisible(false);
				 if (tfdvValorVariable.getText().equals("")){
						 bandValorVariable=false;
						 bdvAceptar.setDisable(true);	
						 bandPuntoDecimal=false;
						 v = null;
				 }else{
					 bandValorVariable=true;			 
						
					 if (bandValorVariable && bandFechaVigencia && bandTipoVariable && bandTipoDato)
						bdvAceptar.setDisable(false);
				}												 
		}});	
		
		cbdvTipoVariable.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){			
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {		
				bandTipoVariable = true;	
				posTipoVariable=arg2.intValue();
				if (bandValorVariable && bandFechaVigencia && bandTipoVariable && bandTipoDato){
					 System.out.println("activo en cbtv");					
					 bdvAceptar.setDisable(false);
          		}
				if (bandFechaVigencia && bandTipoVariable && bandTipoDato){				
					 tfdvValorVariable.setDisable(false);
					 tfdvValorVariable.setOpacity(1);
       		    }	
				System.out.println("* * * "+bandFechaVigencia + " * * * "+bandTipoVariable+" * * * "+ bandTipoDato);
		}});
		
		cbdvTipoValor.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){			
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {		
				ldvAlerta.setVisible(false);
				bandTipoDato = true;
				bandPuntoDecimal=false;
				tfdvValorVariable.setText("");
				if (arg2.intValue() == 0){
					System.out.println("Monto: ");
//					asignarTipoMonto();
				}else if (arg2.intValue() == 1){
					System.out.println("Porcentaje: ");
//					asignarTipoPorcentaje();
				}
				 
				if (bandValorVariable && bandFechaVigencia && bandTipoVariable && bandTipoDato){
					 System.out.println("activo en cb td");
					 bdvAceptar.setDisable(false);
         		 }
				if (bandFechaVigencia && bandTipoVariable && bandTipoDato){				
					 tfdvValorVariable.setDisable(false);
 					 tfdvValorVariable.setOpacity(1);
        		 }
				System.out.println("* * * "+bandFechaVigencia + " * * * "+bandTipoVariable+" * * * "+ bandTipoDato);
		}});
				
		if (bandTipoDato)
			if (cbdvTipoValor.getSelectionModel().getSelectedItem().toString().equals("Monto")){
				asignarTipoMonto();
				System.out.println("undi monto");	
			}else if (cbdvTipoValor.getSelectionModel().getSelectedItem().toString().equals("Porcentaje")){
				asignarTipoPorcentaje();
				System.out.println("undi porcentaje");
			}
	
//		gpdvFecha.setOnMouseClicked(new EventHandler<MouseEvent>() {
//		    @Override
//		    public void handle(MouseEvent mouseEvent) {
//		        EventTarget target = mouseEvent.getTarget();
//		        lastY = mouseEvent.getSceneY();
//		    	System.out.println("imprimi en click de fecha a a a a a");
//		    }
//		});
		
//		gpdvFecha.getChildren().get(0).setOnDragEntered(new EventHandler<DragEvent>(){
//            @Override
//            public void handle(DragEvent t) {
//                myGridPane.getChildren().get(ci).setStyle("-fx-background-color:yellow;");   
//            }                
//        });
	}	
	@FXML
	private void botondvActionAceptar(){
		ldvAlerta.setVisible(false);	
		Calendar c = Calendar.getInstance();
		c.setTime(this.dpFechaVigencia.getSelectedDate());
		System.out.println("la fechaaaaaaaaaaaaaaaa   "+c.getTime());
//		objde guardarfactura.setfecha(c.getTime());
		
		try{
			Session sesion2 = openSesion();
			DetalleVariableConfiguracion obj = new DetalleVariableConfiguracion();
			obj.setTipoValor(cbdvTipoValor.getSelectionModel().getSelectedItem().toString());
			
			if (cbdvTipoValor.getSelectionModel().getSelectedIndex() == 0){
				obj.setValor(Double.parseDouble(tfdvValorVariable.getText()));
			}else if (cbdvTipoValor.getSelectionModel().getSelectedIndex() == 1){
				if (Double.parseDouble(tfdvValorVariable.getText())>0 && Double.parseDouble(tfdvValorVariable.getText())<=100)
					obj.setValor(Double.parseDouble(tfdvValorVariable.getText()));				
			}			
			
			obj.setObjVariable(TipoVariableList.get(posTipoVariable));
				
			sesion2.save(obj);			
			closeSesion(sesion2);
		}catch(HibernateException e){
			e.printStackTrace();
		}			
		tfdvValorVariable.setText("");
	}	
	private void asignarTipoPorcentaje(){
		tfdvValorVariable.addEventHandler(javafx.scene.input.KeyEvent.KEY_TYPED, libreria.porcentajeValidacion());
	}
	private void asignarTipoMonto(){
		tfdvValorVariable.addEventHandler(javafx.scene.input.KeyEvent.KEY_TYPED, libreria.telefonoValidacion(10));
	}	
//	Fin
	
	private Session openSesion(){		
		Session sesion = Main.sesionFactory.getCurrentSession();
		sesion.beginTransaction();		return sesion;
	}	
	private void closeSesion(Session sesion){		
		sesion.getTransaction().commit();
	}
}

//gpdvFecha.setOnAction(new EventHandler(){
//@Override
//public void handle(Event arg0) {
//	lAlerta.setVisible(false);
//	System.out.println("texto en fechaaaa  "+dpFechaVigencia.getValue());
//	
//	if (dpFechaVigencia.getValue()!=null)
//		bandFechaVigencia=true;
//	else
//		bandFechaVigencia=false;
//	
//	if (bandFechaVigencia && bandTipoVariable && bandTipoDato){				
//		 tfValorVariable.setDisable(false);
//		 tfValorVariable.setOpacity(1);
//	}else{
//		 tfValorVariable.setDisable(true);
//		 tfValorVariable.setOpacity(0.5);
//	}	
//	System.out.println("* * * "+bandFechaVigencia + " * * * "+bandTipoVariable+" * * * "+ bandTipoDato);
//}
//});
