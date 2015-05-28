package it.unicam.sensorsimulator.simulationcontroller.xml;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import it.unicam.sensorsimulator.interfaces.ReportInterface;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.logging.LogFileHandler;

public class SerializationTools {

	public static void saveToXML(String pathAndFileName,
			SimulationRunInterface simulationRunFile) {
		saveToXML(new File(pathAndFileName), simulationRunFile);
	}

	public static void saveToXML(File file,
			SimulationRunInterface simulationRunFile) {
		LogFileHandler log = LogFileHandler.getInstance();
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(simulationRunFile.getClass());
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(simulationRunFile, file);
			jaxbMarshaller.marshal(simulationRunFile, System.out);
			log.trace(SerializationTools.class,
					"File saved to: " + file.getAbsolutePath());
		} catch (JAXBException e) {
			log.catching(e);
		}
	}

	public static SimulationRunInterface loadXMLRunFile(File file,
			Class<?> parseClass) throws JAXBException {
		LogFileHandler log = LogFileHandler.getInstance();
		JAXBContext jaxbContext = JAXBContext.newInstance(parseClass);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		SimulationRunInterface simulationRun = (SimulationRunInterface) jaxbUnmarshaller
				.unmarshal(file);

		log.trace(SerializationTools.class,
				"opened file: " + file.getAbsolutePath());
		return simulationRun;
	}

	public static ReportInterface loadXMLReportFile(File file, Class<?> parseClass) throws JAXBException {
		LogFileHandler log = LogFileHandler.getInstance();
		JAXBContext jaxbContext = JAXBContext.newInstance(parseClass);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		ReportInterface reportFile = (ReportInterface) jaxbUnmarshaller.unmarshal(file);
		log.trace(SerializationTools.class,	"opened file: " + file.getAbsolutePath());
		return reportFile;
	}
}
