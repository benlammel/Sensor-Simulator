package it.unicam.sensorsimulator.plugin.heed.reporting.report;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import it.unicam.sensorsimulator.interfaces.ReportInterface;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class HeedReport implements ReportInterface {
	
	
	@XmlElementWrapper(name = "runlist")
	@XmlElement(name = "run", type=HeedRunResults.class)
	private ArrayList<HeedRunResults> runs;
	
	public HeedReport() {
		runs = new ArrayList<HeedRunResults>();
	}

	public ArrayList<HeedRunResults> getRunList() {
		return runs;
	}
	
	public void setRunList(ArrayList<HeedRunResults> runlist) {
		this.runs = runlist;
	}

	public void addRun(HeedRunResults runResults) {
		runs.add(runResults);
	}

}
