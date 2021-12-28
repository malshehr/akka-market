package main;

import actors.KafkaStreamer;
import actors.QuotesManager;
import actors.Trader;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import messages.ProduceQuotesMsg;
import messages.StartStreamingMsg;

public class Driver {

	public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("Market");
        
        ActorRef consumerActor = system.actorOf(KafkaStreamer.props(), "kafkaconsumer"); 
        ActorRef producerActor = system.actorOf(QuotesManager.props(), "kafkaproducer"); 
        // initialize audit actor

        
        ActorRef[] receivers = new ActorRef[5];
        for(int i = 0; i < 5; i++) {
        	ActorRef trader = system.actorOf(Trader.props(), "Trader" +i);
        	receivers[i] = trader;
        }
        
        StartStreamingMsg st = new StartStreamingMsg("localhost:9092", "quickstart-events", "console-consumer-58236", receivers); // pass list of traders 
        ProduceQuotesMsg pq = new ProduceQuotesMsg("localhost:9092", "quickstart-events"); // testing

        
        consumerActor.tell(st, ActorRef.noSender()); // poll for messages indefinitely
        producerActor.tell(pq, ActorRef.noSender()); // send messages indefinitely
                
    }

}
