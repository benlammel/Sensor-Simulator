package it.unicam.sensorsimulator.plugin.heedv2.reporting.report;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import it.unicam.sensorsimulator.interfaces.ReportInterface;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Heedv2Report implements ReportInterface {
	
	@XmlElementWrapper(name = "runlist")
	@XmlElement(name = "run", type=Heedv2RunReport.class)
	private ArrayList<Heedv2RunReport> runs;

	public Heedv2Report(){
		runs = new ArrayList<Heedv2RunReport>();
	}
	
//	@XmlElement
	public ArrayList<Heedv2RunReport> getRuns() {
		return runs;
	}

	public void setRuns(ArrayList<Heedv2RunReport> run) {
		this.runs = run;
	}

	public void addRun(Heedv2RunReport runReport) {
		runs.add(runReport);

	}

}
