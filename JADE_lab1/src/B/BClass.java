package B;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class BClass extends Agent {
    protected void setup() {
        System.out.println("Привет! агент "+getAID().getName()+" готов.");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println(" – " + myAgent.getLocalName() + " received: " + msg.getContent() );
                    //Вывод на экран локального имени агента и полученного сообщения
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative( ACLMessage.INFORM );
                    reply.setContent( "Pong");
//Содержимое сообщения
                    send(reply); //отправляем сообщения
                }
                block();
            }
        });
    }
}
