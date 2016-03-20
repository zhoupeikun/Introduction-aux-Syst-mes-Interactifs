package fr.ilda.e11;

/**
 * Created by aprouzeau on 04/03/2016.
 */

import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.debug.StateMachineVisualization;
import fr.lri.swingstates.events.VirtualEvent;
import fr.lri.swingstates.sm.JStateMachine;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.jtransitions.EnterOnJTag;
import fr.lri.swingstates.sm.jtransitions.LeaveOnJTag;
import fr.lri.swingstates.sm.transitions.Drag;
import fr.lri.swingstates.sm.transitions.Event;
import fr.lri.swingstates.sm.transitions.Press;
import fr.lri.swingstates.sm.transitions.Release;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

// --------------------------------------------------------------------------------------

class CrossingTrace extends CStateMachine {

    private Point2D pInit = new Point2D.Double(0, 0);

    private CPolyLine line;

    private Canvas c;

    State waiting, onPress;

    CrossingTrace(Canvas ca) {
        this.c = ca;

        waiting = new State() {
            Transition press = new Press(BUTTON1, ">> onPress"){
                public void action(){
                    fireEvent(new VirtualEvent("pressEvent"));
                    pInit.setLocation(getPoint());
                    line = c.newPolyLine(getPoint());
                    line.setDrawable(true);
                    line.setFilled(false);
                }
            };
        };

        onPress = new State(){
            Transition drag = new Drag(BUTTON1){
                public void action(){
                    line.lineTo(getPoint());
                }
            };
            Transition release = new Release(BUTTON1, ">> waiting"){
                public void action(){
                    fireEvent(new VirtualEvent("releaseEvent"));
                    line.setDrawable(false);
                }
            };
        };

    }

} ;

class CrossingInteraction extends JStateMachine{
    State waiting, ext, inte;

    CrossingInteraction(JFrame frame){
        waiting = new State(){
            Transition press = new Event("pressEvent", ">> ext"){
            };
        };

        ext = new State(){
            Transition enter = new EnterOnJTag(AbstractButton.class.getName(), ">> inte"){
                public void action() {
                }
            };
            Transition release = new Event("releaseEvent", ">> waiting"){
            };
        };

        inte = new State(){
            Transition leave = new LeaveOnJTag(AbstractButton.class.getName(), ">> ext"){
                public void action() {
                    ((AbstractButton) getComponent()).doClick();
                }
            };
            Transition release = new Event("releaseEvent", ">> waiting"){
            };
        };

    }
}

// --------------------------------------------------------------------------------------

public class CrossingDemo extends JFrame  {



    public CrossingDemo() {
        super("CrossingDemo") ;

        Container pane = getContentPane() ;

        pane.setSize(new Dimension(400,300)) ;

        //Création du canvas sur lequel on dessine la trace

        Canvas canvas = new Canvas(getContentPane().getWidth(), getContentPane().getHeight()) ;
        CrossingTrace ct = new CrossingTrace(canvas) ;
        ct.attachTo(canvas);
        canvas.setVisible(true);
        canvas.setOpaque(false);


        showStateMachine(ct);

        //Création de l'interface graphique

        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        JButton button2 = new JButton("Button 2");
        Dimension dimButton = new Dimension(60, 10);
        button2.setMinimumSize(dimButton);
        button2.setPreferredSize(dimButton);
        pane.add(Box.createRigidArea(new Dimension(0, 30)));
        pane.add(new JButton("Button2"), BorderLayout.CENTER);
        pane.add(new JCheckBox("Choix 1"));
        pane.add(new JCheckBox("Choix 2"));
        pane.add(new JCheckBox("Choix 3"));
        pane.add(Box.createRigidArea(new Dimension(0, 30)));


        //On définit le canvas comme le glassPane de la fenêtre
        this.setGlassPane(canvas);
        //Par défaut le glassPane n'est pas visible, il faut donc lui préciser de l'être
        this.getGlassPane().setVisible(true);


        //On définit et on attache la JStateMachine pour l'interactiond e crossing elle même
        CrossingInteraction ci = new CrossingInteraction(this);
        ci.attachTo(this);

        showJStateMachine(ci);

        //On ajoute ci comme StateMachineListener de ct, pour pouvoir faire passer des évenements de ct à ci
        ct.addStateMachineListener(ci);


        pack() ;
        setVisible(true) ;
    }

    public static void showStateMachine(CStateMachine sm) {
        JFrame viz = new JFrame();
        viz.getContentPane().add(new StateMachineVisualization(sm));
        viz.pack();
        viz.setVisible(true);
    }

    public static void showJStateMachine(JStateMachine sm) {
        JFrame viz = new JFrame();
        viz.getContentPane().add(new StateMachineVisualization(sm));
        viz.pack();
        viz.setVisible(true);
    }

}