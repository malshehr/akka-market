package messages;


import akka.actor.ActorRef;

public class AssignAuditMsg {

	private ActorRef audit;
	
	public AssignAuditMsg(ActorRef audit) {
		this.audit = audit;
	}
	
	public ActorRef getAudit() {
		return audit;
	}
}
