package it.unicam.sensorsimulator.plugin.example.reporting.report;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import it.unicam.sensorsimulator.interfaces.ReportInterface;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ExampleReport implements ReportInterface {
	
	@XmlElement(name = "run", type=ExampleRunReport.class)
	private ArrayList<ExampleRunReport> runList;

	public ExampleReport(){
		runList = new ArrayList<ExampleRunReport>();
	}
	
	public ArrayList<ExampleRunReport> getRuns() {
		return runList;
	}

	public void setRuns(ArrayList<ExampleRunReport> run) {
		this.runList = run;
	}

	public void addRun(ExampleRunReport runReport) {
		runList.add(runReport);
	}

}
