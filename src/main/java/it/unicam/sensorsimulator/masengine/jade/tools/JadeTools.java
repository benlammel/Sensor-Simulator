package it.unicam.sensorsimulator.masengine.jade.tools;
/*
import jade.core.Agent;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;

public class JadeTools {

	public static ACLMessage prepareMessageForBroadcast(Agent agent, ACLMessage message) {
		AMSAgentDescription[] agents = null;
		try {
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults(new Long(-1));
			agents = AMSService.search(agent, new AMSAgentDescription(), c);
		} catch (Exception e) {
		}
		
		for (AMSAgentDescription agentID : agents) {
			if(!agentID.getName().equals(agent.getAID())){
				message.addReceiver(agentID.getName());
			}
		}
		return message;
	}

	public static boolean amIAlone(Agent agent) {
		int i = 0;
		
		AMSAgentDescription[] agents = null;
		try {
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults(new Long(-1));
			agents = AMSService.search(agent, new AMSAgentDescription(), c);
			
			String agentName = agent.getName();
			
			for(AMSAgentDescription agentID : agents){
				String agentIDName = agentID.getName().getName();
				if(agentIDName.contains("ID") && !agentIDName.equals(agentName)){
					i++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(i>0){
			return false;
		}else{
			return true;
		}
	}
}
*/