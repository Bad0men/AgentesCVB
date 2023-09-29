package agents;

import jade.core.Agent;
import behaviours.RequestPerformer;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import gui.GuiComprador;

/**
 * 
 * @author Peregrino López Pedro Emmanuel
 *                      &
 *          Mora Olivera Camila Guadalupe
 */

public class AgenteComprador extends Agent {
  private String bookTitle;
  private AID[] sellerAgents;
  private int ticker_timer = 10000;
  private AgenteComprador this_agent = this;
  private GuiComprador gui;
  
  protected void setup() {
    System.out.println("Agente comprador " + getAID().getName() + " esta listo");
    gui = new GuiComprador(this);
    gui.showGui();
  }
  
  protected void takeDown() {
    gui.dispose();
    System.out.println("Agente comprador " + getAID().getName() + " Terminando");
  }
  
  public AID[] getSellerAgents() {
    return sellerAgents;
  }
  
  public String getBookTitle() {
    return bookTitle;
  }
  
  public void comprar(String titulo){
      bookTitle = titulo;
      System.out.println("Libro: " + bookTitle);
      
      addBehaviour(new TickerBehaviour(this, ticker_timer) {
        protected void onTick() {
          System.out.println("Tratando de comprar " + bookTitle);
          
          DFAgentDescription template = new DFAgentDescription();
          ServiceDescription sd = new ServiceDescription();
          sd.setType("Venta de libros");
          template.addServices(sd);
          
          try {
            DFAgentDescription[] result = DFService.search(myAgent, template);
            System.out.println("Los siguientes agentes son vendedores:");
            sellerAgents = new AID[result.length];
            for(int i = 0; i < result.length; i++) {
              sellerAgents[i] = result[i].getName();
              System.out.println(sellerAgents[i].getName());
            }
            
          }catch(FIPAException fe) {
            fe.printStackTrace();
          }
          
          myAgent.addBehaviour(new RequestPerformer(this_agent));
        }
      });
  }
}
