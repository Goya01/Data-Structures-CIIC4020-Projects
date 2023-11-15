
/*
* This class represents a machine that produces car parts. It has the following attributes:
* id: The unique id of the machine
* part: The part that the machine produces
* period: The period of the machine
* weightError: The weight error of the machine
* chanceOfDefective: The chance of defective parts that the machine produces
* timer: A queue that holds the time values of the machine
* conveyorBelt: A queue that holds the parts that the machine has produced
* totalPartsProduced: The total number of parts that the machine has produced
* Implemented getters and setters for all the attributes.
*/
package main;

import java.util.LinkedList;

import data_structures.ListQueue;
import interfaces.Queue;

public class PartMachine {
    private int id;
    private CarPart part;
    private int period;
    private double weightError;
    private int chanceOfDefective;
    private Queue<Integer> timer;
    private Queue<CarPart> conveyorBelt;
    private int totalPartsProduced;

    public PartMachine() {
        
    }
    
    /**
    * Constructor for PartMachine. It initializes the timer and conveyor belt queues.
    * @param id (int) The unique id of the machine
    * @param p1  (CarPart) The part that the machine produces
    * @param period  (int) The period of the machine
    * @param weightError (double) The weight error of the machine
    * @param chanceOfDefective (int) The chance of defective parts that the machine produces
    */

    public PartMachine(int id, CarPart p1, int period, double weightError, int chanceOfDefective) {
        this.id = id;
        this.part = p1;
        this.period = period;
        this.weightError = weightError;
        this.chanceOfDefective = chanceOfDefective;
        
        timer = new ListQueue<Integer>();
        for (int i = period - 1; i >= 0; i--) {
            timer.enqueue(i);
        }
        
        conveyorBelt = new ListQueue<CarPart>();
        for (int i = 0; i < 10; i++) {
            conveyorBelt.enqueue(null);
        }
        
        totalPartsProduced = 0;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Queue<Integer> getTimer() {
        return timer;
    }
    public void setTimer(Queue<Integer> timer) {
        this.timer = timer;
    }
    public CarPart getPart() {
        return part;
    }
    public void setPart(CarPart part1) {
        this.part = part1;
    }
    public Queue<CarPart> getConveyorBelt() {
        return conveyorBelt;
    }
    public void setConveyorBelt(Queue<CarPart> conveyorBelt) {
        this.conveyorBelt = conveyorBelt;
    }
    public int getTotalPartsProduced() {
        return totalPartsProduced;
    }
    public void setTotalPartsProduced(int count) {
        this.totalPartsProduced = count;
    }
    public double getPartWeightError() {
        return weightError;
    }
    public void setPartWeightError(double partWeightError) {
        this.weightError = partWeightError;
    }
    public int getChanceOfDefective() {
        return chanceOfDefective;
    }
    public void setChanceOfDefective(int chanceOfDefective) {
        this.chanceOfDefective = chanceOfDefective;
    }

    /**
    * Resets the conveyor belt by removing all the parts and adding null values to it.
    */

    public void resetConveyorBelt() {
        for (int i = 0; i < 10; i++) {
            conveyorBelt.dequeue();
            conveyorBelt.enqueue(null);
        }
    }

    /**
    * Returns the time value at the front of the timer queue and enqueues it at the back of the queue.
    * @return (int) The time value at the front of the timer queue
    */

    public int tickTimer() {
        int time = timer.dequeue();
        timer.enqueue(time);
        return time;
    }

    /**
    * Produces a car part and adds it to the conveyor belt.
    * @return (CarPart) The car part that was produced
    */

    public CarPart produceCarPart() {
        int timeValue = tickTimer();
        
        if (timeValue == 0) {
            double minWeight = part.getWeight() - weightError;
            double maxWeight = part.getWeight() + weightError;
            double randomWeight = minWeight + Math.random() * (maxWeight - minWeight);
    
            int chanceOfDefective = this.chanceOfDefective;
            boolean isDefective = (totalPartsProduced % chanceOfDefective) == 0;
            CarPart newCarPart = new CarPart(part.getId(), part.getName(), randomWeight, isDefective);
            conveyorBelt.enqueue(newCarPart);
            totalPartsProduced++;
            return conveyorBelt.dequeue();
        } else {
            conveyorBelt.enqueue(null);
            conveyorBelt.dequeue();
            return null;
        }
    }

    /**
    * Returns the remaining parts on the conveyor belt.
    * @return (LinkedList<CarPart>) The remaining parts on the conveyor belt
    */

    public LinkedList<CarPart> getRemainingConveyorBelt() {
        LinkedList<CarPart> remaining = new LinkedList<CarPart>();
        for (int i = 0; i < conveyorBelt.size(); i++) {
            CarPart part = conveyorBelt.dequeue();
            if (part != null) {
                remaining.add(part);
            }
            conveyorBelt.enqueue(part);
        }
        return remaining;
    }

    /**
    * Returns string representation of a Part Machine in the following format:
    * Machine {id} Produced: {part name} {total parts produced}
    * @return (String) The string representation of a Part Machine
    */

    @Override
    public String toString() {
        return "Machine " + this.getId() + " Produced: " + this.getPart().getName() + " " + this.getTotalPartsProduced();
    }

    /**
    * Prints the content of the conveyor belt. 
    * The machine is shown as |Machine {id}|.
    * If the is a part it is presented as |P| and an empty space as _.
    */

    public void printConveyorBelt() {
        // String we will print
        String str = "";
        // Iterate through the conveyor belt
        for(int i = 0; i < this.getConveyorBelt().size(); i++){
            // When the current position is empty
            if (this.getConveyorBelt().front() == null) {
                str = "_" + str;
            }
            // When there is a CarPart
            else {
                str = "|P|" + str;
            }
            // Rotate the values
            this.getConveyorBelt().enqueue(this.getConveyorBelt().dequeue());
        }
        System.out.println("|Machine " + this.getId() + "|" + str);
    }
}
