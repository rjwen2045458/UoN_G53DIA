package uk.ac.nott.cs.g53dia.agent.ScoreAgent;

import uk.ac.nott.cs.g53dia.library.*;


public class ReactiveLayer {
	
	private ScoreAgent agent = null;
	
	public ReactiveLayer(ScoreAgent agent) {
		this.agent = agent;
	}
	
	public Action Execute() {
		if(agent.getChargeLevel() < LitterAgent.MAX_CHARGE && agent.getCurrentCell() instanceof RechargePoint)
			return new RechargeAction();
		if((agent.getRecyclingLevel() > 0 && agent.getCurrentCell() instanceof RecyclingStation) 
			|| (agent.getWasteLevel() > 0 && agent.getCurrentCell() instanceof WasteStation)){
			
			return new DisposeAction();
		}
		if(((agent.getLitterLevel() <= 0 && (agent.getCurrentCell() instanceof RecyclingBin || agent.getCurrentCell() instanceof WasteBin))
			|| (0 < agent.getRecyclingLevel() && agent.getRecyclingLevel() < LitterAgent.MAX_LITTER && agent.getCurrentCell() instanceof RecyclingBin) 
			|| (0 < agent.getWasteLevel() && agent.getWasteLevel() < LitterAgent.MAX_LITTER && agent.getCurrentCell() instanceof WasteBin))
			&& ((LitterBin) agent.getCurrentCell()).getTask() != null) {
			agent.getAgentMemory().LoadLitter(agent.getCurrentCell(), agent);
			return new LoadAction(((LitterBin) agent.getCurrentCell()).getTask());
		}
		return null;
	}
}
