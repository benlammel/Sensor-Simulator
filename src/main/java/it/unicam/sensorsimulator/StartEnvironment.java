package it.unicam.sensorsimulator;

import it.unicam.sensorsimulator.persistence.FileTools;
import it.unicam.sensorsimulator.persistence.Folder;
import it.unicam.sensorsimulator.simulationcontroller.SimulationController;
import it.unicam.sensorsimulator.ui.ApplicationFrame;
import it.unicam.sensorsimulator.ui.ressources.SimulationResourcesAndProperties;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class StartEnvironment extends Application {

	private ApplicationFrame applicationFrame;
	private SimulationController simulationController;
	private SimulationResourcesAndProperties ressources;
	private Stage stage;

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {
		FileTools.verifyFolderStructure(Folder.values());
		this.stage = stage;
		ressources = new SimulationResourcesAndProperties();
		simulationController = new SimulationController(this);
		applicationFrame = new ApplicationFrame(this);

		Scene scene = new Scene(applicationFrame,
				ressources.getApplicationWidth(),
				ressources.getApplicationHeight());

		scene.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(
					ObservableValue<? extends Number> observableValue,
					Number oldSceneWidth, Number newSceneWidth) {
				ressources.setApplicationWidth((Double) newSceneWidth);
			}
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(
					ObservableValue<? extends Number> observableValue,
					Number oldSceneHeight, Number newSceneHeight) {
				ressources.setApplicationHeight((Double) newSceneHeight);
			}
		});

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				applicationFrame.closeSubStages();
			}
		});

		stage.setTitle(ressources.getApplicationHeader());
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		System.out.println("Closing");
		ressources.saveToPropertiesFile();
		simulationController.stop();
	}

	public SimulationController getSimulationController() {
		return simulationController;
	}

	public SimulationResourcesAndProperties getRessourcesAndProperties() {
		return ressources;
	}

	public Stage getMainStage() {
		return stage;
	}

	public ApplicationFrame getApplicationFrame() {
		return applicationFrame;
	}
}