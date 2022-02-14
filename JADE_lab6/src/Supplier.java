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

public class Supplier extends Agent{
    private String[] productList;
    private String reqResponse = "";
    private AID manager;

    protected void setup (){
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("product-request");
        sd.setName("JADE-product-supply");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new ShipmentReg());
    }


    private class ShipmentReg extends CyclicBehaviour{
        private int step = 0;
        private AID[] providerAgents;
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        ACLMessage msg;
        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
        private int replyCount = 0;

        @Override
        public void action() {
            switch (step) {
                case 0:
                    msg = myAgent.receive(mt);
                    if (msg != null && msg.getPerformative() == ACLMessage.REQUEST) {
                        manager = msg.getSender();
                        // CFP Message received. Process it
                        productList = msg.getContent().split(",");
                        for (String i : productList) {
                            System.out.println("Пытаюсь найти поставщика " + i);
                            DFAgentDescription template = new DFAgentDescription();
                            ServiceDescription sd = new ServiceDescription();
                            sd.setType(i);
                            template.addServices(sd);
                            try {
                                DFAgentDescription[] result = DFService.search(myAgent, template);
                                System.out.println("Были найдены следующие поставщики:");
                                providerAgents = new AID[result.length];
                                for (int j = 0; j < result.length; ++j) {
                                    providerAgents[j] = result[j].getName();
                                    System.out.println(providerAgents[j].getName());
                                }
                            } catch (FIPAException fe) {
                                fe.printStackTrace();
                            }
                            if (providerAgents.length > 0) {
                                cfp.addReceiver(providerAgents[0]);
                            }
                        }
                        cfp.setContent("request");
                        cfp.setConversationId("product-provider");
                        cfp.setReplyWith("cfp" + System.currentTimeMillis());
                        myAgent.send(cfp);

                        mt = MessageTemplate.and(MessageTemplate.MatchConversationId("product-provider"),
                                MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));


                        step = 1;
                    }
                    else {
                        block();
                    }
                    break;
                case 1:
                    ACLMessage reply = myAgent.receive(mt);
                    if (reply != null) {
                        if (reply.getPerformative() == ACLMessage.INFORM) {
                            System.out.println(reply.getContent() + " удачно приобретён у поставщика");
                            reqResponse += ',' + reply.getContent();
                        } else {
                            System.out.println("У поставщика нет в наличии нужного продукта");
                        }
                        replyCount++;
                        if (replyCount == productList.length){
                            step = 2;
                        }
                    }
                    break;
                case 2:
                    addBehaviour(new OneShotBehaviour() {
                        @Override
                        public void action() {
                            reqResponse = reqResponse.substring(1);
                            ACLMessage info = new ACLMessage(ACLMessage.INFORM);
                            info.addReceiver(manager);
                            if (!reqResponse.equals("")) {
                                info.setPerformative(ACLMessage.INFORM);
                                info.setContent(reqResponse);
                                System.out.println("Удачная поставка");
                            } else {
                                info.setPerformative(ACLMessage.FAILURE);
                                System.out.println("Облом");
                            }
                            myAgent.send(info);
                            reqResponse = "";
                        }
                    });
                    step = 0;
                    break;
            }
        }
    }


}
