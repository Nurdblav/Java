
import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.lang.reflect.Array;
import java.util.*;

public class Manager extends Agent{
    private Product[] dataBase;
    private AID[] supplierAgents;

    protected void setup (){
//      0-хлебобулочные изделия, 1-бакалея, 2-крупы, 3-мясо, 4-молочные изделия
        dataBase = new Product[]{new Product("хлебобулочные изделия",30),
                                 new Product("бакалея",30),
                                 new Product("крупы",30),
                                 new Product("мясо",30),
                                 new Product("молочные изделия",30)};

        addBehaviour(new TickerBehaviour(this,1000) {
            @Override
            protected void onTick() {
                Random random = new Random();
                int quantity = random.nextInt(5) + 1;
                for (int i = 0; i<quantity; i++){
                    int k = random.nextInt(5);
                    dataBase[k].setQuantity(dataBase[k].getQuantity() - (random.nextInt(10)+1));
                    if (dataBase[k].getQuantity()<0)
                        dataBase[k].setQuantity(0);
                }
                HashMap lack = new HashMap();
                for (Product i:dataBase){
                    String check = i.getName() + " - " +i.getQuantity();
                    System.out.println (check);
                    if (i.getQuantity()<=10){
                        lack.putIfAbsent(i.getName(),i.getQuantity());
                    }
                }
                System.out.println ("");
                if (lack.size()>=3){
                    System.out.println("Trying to request the products");
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("product-request");
                    template.addServices(sd);
                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        System.out.println("Found the following supplier agents:");
                        supplierAgents = new AID[result.length];
                        for (int i = 0; i < result.length; ++i) {
                            supplierAgents[i] = result[i].getName();
                            System.out.println(supplierAgents[i].getName());
                        }
                    }
                    catch (FIPAException fe) {
                        fe.printStackTrace();
                    }
                    if (supplierAgents.length>0){
                    myAgent.addBehaviour(new ProdRequest(lack));
                    }
                    else{
                        System.out.println ("Нет свободных снабженцев");
                    }
                }
            }
        });
    }

    private class ProdRequest extends Behaviour{
        private int step = 0;
        private HashMap lack;
        private MessageTemplate mt;
        private String[] temp_reply;

        private ProdRequest (HashMap lack){
            this.lack = lack;
        }

        @Override
        public void action() {
            switch (step){
                case 0:
                // Send the cfp to all sellers
                ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
                    req.addReceiver(supplierAgents[0]);
                String reqContent = "";
                Set<String> keys = lack.keySet();
                for (String i : keys){
                    reqContent += ',' + i;
                }
                reqContent = reqContent.substring(1);
                req.setContent(reqContent);
                req.setConversationId("product-request");
                req.setReplyWith("req"+System.currentTimeMillis()); // Unique value
                myAgent.send(req);
                // Prepare the template to get shipment
                step = 1;
                break;
                case 1:
                ACLMessage reply = myAgent.receive();
                if (reply != null) {
                    // Reply received
                    if (reply.getPerformative() == ACLMessage.INFORM) {
                        // This is an shipment
                        temp_reply = reply.getContent().trim().split(",");
                        System.out.println ("Поставка получена:");
                        for (String i: temp_reply){
                            for (int j = 0; j<dataBase.length; j++){
                                if (dataBase[j].getName().equals(i)){
                                    dataBase[j].setQuantity(dataBase[j].getQuantity()+100);
                                    break;
                                }
                            }
                        }
                    }
                    else{
                        System.out.println ("Ошибка поставки:");
                    }
                    for (Product i:dataBase){
                        String check = i.getName() + " - " +i.getQuantity();
                        System.out.println (check);
                    }
                    step = 2;
                }
                break;
            }
        }

        @Override
        public boolean done() {
            return  (step == 4);
        }
    }


}
