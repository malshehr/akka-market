package main;

import actors.KafkaStreamer;
import actors.QuotesManager;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import messages.ProduceQuotesMsg;
import messages.StartStreamingMsg;

public class Driver {

	public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("Market");
        
        ActorRef consumerActor = system.actorOf(KafkaStreamer.props(), "kafkaconsumer"); 
        ActorRef producerActor = system.actorOf(QuotesManager.props(), "kafkaproducer"); 

        ActorRef[] receivers = new ActorRef[5];
        
        StartStreamingMsg st = new StartStreamingMsg("localhost:9092", "quickstart-events", "console-consumer-58236", receivers); // pass list of traders 
        ProduceQuotesMsg pq = new ProduceQuotesMsg("localhost:9092", "quickstart-events"); // testing

        
        consumerActor.tell(st, ActorRef.noSender()); // poll for messages indefinitely
        producerActor.tell(pq, ActorRef.noSender()); // send messages indefinitely
        
        // initialize traders and audit actor
    }

}
