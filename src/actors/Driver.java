package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import messages.StartStreamingMsg;

public class Driver {

	public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("Market");
        ActorRef kafkaActor = system.actorOf(KafkaStreamer.props(), "kafkaStreamer"); 
        
        StartStreamingMsg st = new StartStreamingMsg("localhost:9092", "quickstart-events", "console-consumer-58236"); // testing
        
        kafkaActor.tell(st, ActorRef.noSender()); // poll for messages indefinitely
        
        // initialize traders and kafka producer actor and audit actor
    }

}
