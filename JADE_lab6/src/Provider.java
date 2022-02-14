import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.*;

public class Provider extends Agent {
    private String product;

    protected void setup (){
        Object[] args = getArguments();
        if (args != null && args.length >0){
            product = (String) args[0];
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            ServiceDescription sd = new ServiceDescription();
            sd.setType(product);
            sd.setName("JADE-product-provider");
            dfd.addServices(sd);
            try {
                DFService.register(this, dfd);
            }
            catch (FIPAException fe) {
                fe.printStackTrace();
            }

            addBehaviour(new CyclicBehaviour() {
                @Override
                public void action() {
                    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
                    ACLMessage msg = myAgent.receive(mt);
                    if (msg != null) {
                        ACLMessage reply = msg.createReply();
                        if (msg.getContent().equals("request")){
                            reply.setContent(product);
                            reply.setPerformative(ACLMessage.INFORM);
                        }
                        else{
                            reply.setPerformative(ACLMessage.FAILURE);
                        }
                        myAgent.send(reply);
                    }
                }
            });
        }
        else{
            System.out.println("Не указан наименование продукта поставки");
            doDelete();
        }

    }
}
