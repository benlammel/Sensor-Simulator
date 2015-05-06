package it.unicam.sensorsimulator.interfaces;

public interface GeneralAgentInterface {
	
	public int getAgentID();
	public void setAgentID(int id);
	
	public double getAgentRadius();
	public void setAgentRadius(double radius);

	public double getLocationX();
	public void setLocationX(double locationX);

	public double getLocationY();
	public void setLocationY(double locationY);
	
//	public Object[] getAgentArguments();
//	public void setAgentArguments(Object[] args);
//	
//	public Class getAgentClass();
//	public void setAgentClass(Class cls);

}
