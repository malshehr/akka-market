package main;

import actors.Audit;
import actors.KafkaStreamer;
import actors.QuotesManager;
import actors.Trader;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import messages.AssignAuditMsg;
import messages.ProduceQuotesMsg;
import messages.StartAuditMsg;
import messages.StartStreamingMsg;

public class MarketSimulation {

	
	private ActorSystem simulation;
	
	public void runSimulation() {
		simulation = ActorSystem.create("Market");
        
        ActorRef consumerActor = simulation.actorOf(KafkaStreamer.props(), "kafkaconsumer"); 
        ActorRef producerActor = simulation.actorOf(QuotesManager.props(), "kafkaproducer"); 
        ActorRef auditActor = simulation.actorOf(Audit.props(), "audit");
        // initialize audit actor
        
        ActorRef[] receivers = new ActorRef[5];
        AssignAuditMsg assignAudit = new AssignAuditMsg(auditActor);
        for(int i = 0; i < receivers.length; i++) {
        	ActorRef trader = simulation.actorOf(Trader.props(), "Trader" + i);
        	trader.tell(assignAudit, ActorRef.noSender()); // inform the trader of its audit
        	receivers[i] = trader;
        }
        
        StartAuditMsg db = new StartAuditMsg("root", "whatever", "whatever"); // use your configuration
        StartStreamingMsg st = new StartStreamingMsg("localhost:9092", "market", "console-consumer-58236", receivers); // pass list of traders 
        ProduceQuotesMsg pq = new ProduceQuotesMsg("localhost:9092", "market"); // testing

        
        auditActor.tell(db, ActorRef.noSender()); // setup the database
        consumerActor.tell(st, ActorRef.noSender()); // poll for messages indefinitely
        producerActor.tell(pq, ActorRef.noSender()); // send messages indefinitely
	}
	
	
}
