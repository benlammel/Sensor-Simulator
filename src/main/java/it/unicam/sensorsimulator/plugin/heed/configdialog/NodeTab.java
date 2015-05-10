package it.unicam.sensorsimulator.plugin.heed.configdialog;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.plugin.heed.agents.AgentConfiguration;
import it.unicam.sensorsimulator.plugin.heed.agents.AgentTypes;
import it.unicam.sensorsimulator.plugin.heed.simulation.SimulationRunFile;

import java.util.ArrayList;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.NumberStringConverter;

public class NodeTab extends Tab {
	
	private HeedPluginConfigDialog heedPluginConfigDialog;
	private TableView table;
	private TableColumn nodeID, nodeType, eMax, eResidential, pMin;
	
	private ObservableList<AgentConfiguration> agentData;
	private double pMinStandardValue = 0.0001;
	private double eResidentialStandardValue = 4000;
	private double eMaxStandardValue = 5000;
	
	public NodeTab(HeedPluginConfigDialog heedPluginConfigDialog) {
		this.heedPluginConfigDialog = heedPluginConfigDialog;
		this.setText("Node Config");
		this.setClosable(false);
		
		table = new TableView();
		table.setEditable(true);
		table.getColumns().addAll(generateColums());
		
		setContent(table);
	}

	@SuppressWarnings("unchecked")
	private ArrayList<TableColumn> generateColums() {
		ArrayList<TableColumn> columns = new ArrayList<TableColumn>();
		
		nodeID = new TableColumn<AgentConfiguration,Integer>("Node ID");
		nodeID.setEditable(false);
		nodeID.setCellValueFactory(
                new PropertyValueFactory<AgentConfiguration,Integer>("agentID"));
		columns.add(nodeID);
		
		nodeType = new TableColumn("Type");
		nodeType.setCellFactory(ComboBoxTableCell.<AgentConfiguration, AgentTypes>forTableColumn(AgentTypes.values()));
		nodeType.setCellValueFactory(new Callback<CellDataFeatures<AgentConfiguration, AgentTypes>, ObservableValue<AgentTypes>>() {
		     public ObservableValue<AgentTypes> call(CellDataFeatures<AgentConfiguration, AgentTypes> p) {
		         return new SimpleObjectProperty(p.getValue().getAgentType());
		     }
		  });
		nodeType.setOnEditCommit(
			    new EventHandler<CellEditEvent<AgentConfiguration, AgentTypes>>() {
			        @Override
			        public void handle(CellEditEvent<AgentConfiguration, AgentTypes> t) {
			            ((AgentConfiguration) t.getTableView().getItems().get(
			                t.getTablePosition().getRow())
			                ).setAgentType(t.getNewValue());
			        }
			    }
			);
		
		columns.add(nodeType);
		
		eMax = new TableColumn<AgentConfiguration,Double>("E Max");
		eMax.setCellFactory(TextFieldTableCell.<AgentConfiguration, Number>forTableColumn(new NumberStringConverter()));
		eMax.setEditable(true);
		eMax.setCellValueFactory(new Callback<CellDataFeatures<AgentConfiguration, Number>, ObservableValue<Number>>() {
		     public ObservableValue<Number> call(CellDataFeatures<AgentConfiguration, Number> p) {
		         return new SimpleDoubleProperty(p.getValue().geteMax());
		     }
		  });
		eMax.setOnEditCommit(
			    new EventHandler<CellEditEvent<AgentConfiguration, Number>>() {
			        @Override
			        public void handle(CellEditEvent<AgentConfiguration, Number> t) {
			            ((AgentConfiguration) t.getTableView().getItems().get(
			                t.getTablePosition().getRow())
			                ).seteMax(t.getNewValue().doubleValue());
			        }
			    }
			);
		columns.add(eMax);
		
		eResidential = new TableColumn<AgentConfiguration,Double>("E residential");
		eResidential.setCellFactory(TextFieldTableCell.<AgentConfiguration, Number>forTableColumn(new NumberStringConverter()));
		eResidential.setEditable(true);
		eResidential.setCellValueFactory(new Callback<CellDataFeatures<AgentConfiguration, Number>, ObservableValue<Number>>() {
		     public ObservableValue<Number> call(CellDataFeatures<AgentConfiguration, Number> p) {
		         return new SimpleDoubleProperty(p.getValue().geteResidential());
		     }
		  });
		eResidential.setOnEditCommit(
			    new EventHandler<CellEditEvent<AgentConfiguration, Number>>() {
			        @Override
			        public void handle(CellEditEvent<AgentConfiguration, Number> t) {
			            ((AgentConfiguration) t.getTableView().getItems().get(
			                t.getTablePosition().getRow())
			                ).seteResidential(t.getNewValue().doubleValue());
			        }
			    }
			);
		columns.add(eResidential);
		
		pMin = new TableColumn<AgentConfiguration,Double>("P min");
		pMin.setCellFactory(TextFieldTableCell.<AgentConfiguration, Number>forTableColumn(new NumberStringConverter()));
		pMin.setEditable(true);
		pMin.setCellValueFactory(new Callback<CellDataFeatures<AgentConfiguration, Number>, ObservableValue<Number>>() {
		     public ObservableValue<Number> call(CellDataFeatures<AgentConfiguration, Number> p) {
		         return new SimpleDoubleProperty(p.getValue().getpMin());
		     }
		  });
		pMin.setOnEditCommit(
			    new EventHandler<CellEditEvent<AgentConfiguration, Number>>() {
			        @Override
			        public void handle(CellEditEvent<AgentConfiguration, Number> t) {
			            ((AgentConfiguration) t.getTableView().getItems().get(
			                t.getTablePosition().getRow())
			                ).setpMin(t.getNewValue().doubleValue());
			        }
			    }
			);
		columns.add(pMin);

		return columns;
	}

	public void updateRunFile(SimulationRunInterface simulationRunFile) {
		SimulationRunFile runfile = (SimulationRunFile) simulationRunFile;
		
		ArrayList<AgentConfiguration> list = setAndReturnStandardValues(runfile.getAgentList());
		agentData = (ObservableList<AgentConfiguration>) FXCollections.observableArrayList(list);
		table.setItems(agentData);
	}

	private ArrayList<AgentConfiguration> setAndReturnStandardValues(
			ArrayList<AgentConfiguration> arrayList) {
				
		for(AgentConfiguration agent : arrayList){
			setStandardValues(agent);
		}
		return arrayList;
	}

	private void setStandardValues(AgentConfiguration agent) {
		if(agent.getAgentType()==null){
			agent.setAgentType(AgentTypes.TEMPERATURE);
		}
		if(agent.getpMin()==0.0){
			agent.setpMin(pMinStandardValue);
		}
		if(agent.geteResidential()==0.0){
			agent.seteResidential(eResidentialStandardValue);
		}
		if(agent.geteMax()==0.0){
			agent.seteMax(eMaxStandardValue);
		}
	}

	public ObservableList<AgentConfiguration> getAgentData() {
		return agentData;
	}

	public void refreshContent() {
		if(heedPluginConfigDialog.getCurrentRunFile().getAgentList().isEmpty()){
			ArrayList<AgentConfiguration> list = new ArrayList<AgentConfiguration>();
			for(GeneralAgentInterface agent : heedPluginConfigDialog.getAgentListFromPanel()){
				list.add(new AgentConfiguration(agent.getAgentID(), agent.getAgentRadius(), agent.getLocationX(), agent.getLocationY()));
			}
			list = setAndReturnStandardValues(list);
			agentData = (ObservableList<AgentConfiguration>) FXCollections.observableArrayList(list);
			table.setItems(agentData);
		}else{
			ArrayList<Integer> panel = new ArrayList<Integer>();
			for(GeneralAgentInterface agent : heedPluginConfigDialog.getAgentListFromPanel()){
				panel.add(agent.getAgentID());
			}
			
			ArrayList<Integer> simFile = new ArrayList<Integer>();
			for(GeneralAgentInterface agent : agentData){
				simFile.add(agent.getAgentID());
			}
			
			for(int panelAgent : panel){
				if(!simFile.contains(panelAgent)){
					for(GeneralAgentInterface agent : heedPluginConfigDialog.getAgentListFromPanel()){
						if(agent.getAgentID()==panelAgent){
							AgentConfiguration newAgent = new AgentConfiguration(agent.getAgentID(), agent.getAgentRadius(), agent.getLocationX(), agent.getLocationY());
							setStandardValues(newAgent);
							agentData.add(newAgent);
						}
					}
				}
			}
		}
	}
}
