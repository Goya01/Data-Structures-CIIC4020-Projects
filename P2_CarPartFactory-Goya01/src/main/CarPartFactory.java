
/*
* The CarPartFactory class represents a car part factory. It has the following attributes:
* machines: A list of machines that the factory has
* orders: A list of orders that the factory has
* partCatalog: A map of the parts that the factory has. The key is the part id and the value is the part
* inventory: A map of the parts that the factory has in its inventory. The key is the part id and the value is a list of parts
* productionBin: A stack that holds the parts that the factory has produced
* defectives: A map of the parts that the factory has produced that are defective. The key is the part id and the value is the number of defective parts
* Implemented getters and setters for all the attributes.
*/

package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import data_structures.ArrayList;
import data_structures.BasicHashFunction;
import data_structures.HashTableSC;
import data_structures.LinkedStack;

import interfaces.List;
import interfaces.Map;
import interfaces.Stack;

public class CarPartFactory {
    private List<PartMachine> machines;
    private List<Order> orders;
    private Map<Integer, CarPart> partCatalog;
    private Map<Integer, List<CarPart>> inventory;
    private Stack<CarPart> productionBin;
    private Map<Integer, Integer> defectives;

    public CarPartFactory() {
        
    }

    /**
    * This constructor takes in the path to the order file and the path to the parts file.
    * It then sets up the factory by creating the machines, the catalog, the inventory and the orders.
    * @param orderPath (String) The path to the orders.csv file
    * @param partsPath (String) The path to the parts.csv file
    * @throws IOException if the file is not found
    */

    public CarPartFactory(String orderPath, String partsPath) throws IOException {
        this.machines = new ArrayList<>();
        this.productionBin = new LinkedStack<CarPart>();
        this.partCatalog = new HashTableSC<Integer, CarPart>(2, new BasicHashFunction());
        this.inventory = new HashTableSC<Integer, List<CarPart>>(2, new BasicHashFunction());
        this.orders = new ArrayList<>();
        this.defectives = new HashTableSC<Integer, Integer>(2, new BasicHashFunction());

        setupMachines(partsPath);
        setupCatalog();
        setupOrders(orderPath);
        setupInventory();
    }
    public List<PartMachine> getMachines() {
        return machines;
    }
    public void setMachines(List<PartMachine> machines) {
        this.machines = machines;
    }
    public Stack<CarPart> getProductionBin() {
        return productionBin;
    }
    public void setProductionBin(Stack<CarPart> production) {
        this.productionBin = production;
    }
    public Map<Integer, CarPart> getPartCatalog() {
        return partCatalog;
    }
    public void setPartCatalog(Map<Integer, CarPart> partCatalog) {
        this.partCatalog = partCatalog;
    }
    public Map<Integer, List<CarPart>> getInventory() {
        return inventory;
    }
    public void setInventory(Map<Integer, List<CarPart>> inventory) {
        this.inventory = inventory;
    }
    public List<Order> getOrders() {
        return orders;
    }
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
    public Map<Integer, Integer> getDefectives() {
        return defectives;
    }
    public void setDefectives(Map<Integer, Integer> defectives) {
        this.defectives = defectives;
    }

    /**
    * This method sets up the orders by reading the orders.csv file and creating the orders.
    * It then adds the orders to the orders list.
    * @param path (String) The path to the orders.csv file
    * @throws IOException if the file is not found
    */

    public void setupOrders(String path) throws IOException {
        this.orders = new ArrayList<Order>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            boolean firstLine = false;
            while ((line = reader.readLine()) != null) {
                if (!firstLine) {
                    firstLine = true;
                    continue;
                }
                String[] orderInfo = line.split(",");
                Map<Integer, Integer> requestedParts = new HashTableSC<Integer, Integer>(2, new BasicHashFunction());
                String[] partsArray = orderInfo[2].split("-");
                for (String part : partsArray) {
                    String[] partDetails = part.replaceAll("[()]", "").trim().split(" ");
                    int partId = Integer.parseInt(partDetails[0]);
                    int quantity = Integer.parseInt(partDetails[1]);
                    requestedParts.put(partId, quantity);
                }
                Order order = new Order(Integer.parseInt(orderInfo[0]), orderInfo[1], requestedParts, false);
                orders.add(order);
            }
        }
    }

    /**
    * This method sets up the machines by reading the parts.csv file and creating the machines.
    * It then adds the machines to the machines list.
    * @param path (String) The path to the parts.csv file
    * @throws IOException if the file is not found
    */

    public void setupMachines(String path) throws IOException {
        this.machines = new ArrayList<PartMachine>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            boolean firstLine = false;
            while ((line = reader.readLine()) != null) {
                if (!firstLine) {
                    firstLine = true;
                    continue;
            }
            String[] parts = line.split(",");
            CarPart carPart = new CarPart(Integer.parseInt(parts[0]), parts[1], Double.parseDouble(parts[2]), false);
            PartMachine machine = new PartMachine(Integer.parseInt(parts[0]), carPart, Integer.parseInt(parts[4]), Double.parseDouble(parts[3]), Integer.parseInt(parts[5]));
            machines.add(machine);
        }
        reader.close();
    }

    /**
    * This method sets up the catalog by iterating through the machines list and adding the parts to the catalog.
    * It also sets up the defectives map by iterating through the machines list and adding the parts to the defectives map.
    */

    public void setupCatalog() {
        this.partCatalog = new HashTableSC<Integer, CarPart>(2, new BasicHashFunction());
        for (PartMachine machine : this.getMachines()) {
            partCatalog.put(machine.getPart().getId(), machine.getPart());
            defectives.put(machine.getPart().getId(), 0);
        }
    }

    /**
    * This method sets up the inventory by iterating through the machines list and adding the parts to the inventory.
    */

    public void setupInventory() {
        for (PartMachine machine : this.getMachines()) {
            inventory.put(machine.getPart().getId(), new ArrayList<CarPart>());
        }
    }

    /**
    * This method stores the parts in the inventory. It iterates through the production bin and adds the parts to the inventory.
    */

    public void storeInInventory() {
        while (!productionBin.isEmpty()) {
            CarPart part = productionBin.pop();
            int partId = part.getId();

            if (part.isDefective()) {
                int defecCount = defectives.get(partId);
                defectives.put(partId, defecCount + 1);
            } 
            else {
                List<CarPart> machineInv = inventory.get(partId);
                machineInv.add(part);
            }
        }
    }

    /**
    * This method runs the factory for the given number of days and minutes.
    * It iterates through the machines list and produces the parts.
    * It then stores the parts in the inventory and processes the orders.
    * @param days (int) The number of days to run the factory
    * @param minutes (int) The number of minutes to run the factory
    */

    public void runFactory(int days, int minutes) {
        for (int day = 1; day <= days; day++) {

            for (int minute = 1; minute <= minutes; minute++) {

                for (PartMachine machine : machines) {
                    CarPart producedPart = machine.produceCarPart();
                    
                    if (producedPart != null) {
                        productionBin.push(producedPart);
                    }
                }
            }
            for (PartMachine machine : machines) {
                LinkedList<CarPart> remaining = machine.getRemainingConveyorBelt();
                
                for (CarPart part : remaining) {
                    
                    if (part != null) {
                        productionBin.push(part);
                    }
                    machine.resetConveyorBelt();
                }
            }
            storeInInventory();
        }
        processOrders();
    }

    /**
    * This method processes the orders by iterating through the orders list and checking if the order can be fulfilled.
    * If the order can be fulfilled, it sets the order's fulfilled attribute to true and removes the parts from the inventory.
    * If the order cannot be fulfilled, it sets the order's fulfilled attribute to false.
    */

    public void processOrders() {
        for (Order order : orders) {
            boolean isOrderFulfilled = true;

            for (int key : order.getRequestedParts().getKeys()) {
                int requestedQuantity = order.getRequestedParts().get(key);
                List<CarPart> machineInv = inventory.get(key);

                if (requestedQuantity > machineInv.size()) {
                    isOrderFulfilled = false;
                    break;
                }
            }

            if (isOrderFulfilled) {
                order.setFulfilled(true);

                for (int key : order.getRequestedParts().getKeys()) {
                    int requestedQuantity = order.getRequestedParts().get(key);

                    for (int i = 0; i < requestedQuantity; i++) {
                        List<CarPart> machineInv = inventory.get(key);
                        machineInv.remove(0);
                        inventory.put(key, machineInv);
                    }
                }
            }
        }
    }


    /**
    * Generates a report indicating how many parts were produced per machine,
    * how many of those were defective and are still in inventory. Additionally, 
    * it also shows how many orders were successfully fulfilled. 
    */
    
    public void generateReport() {
        String report = "\t\t\tREPORT\n\n";
        report += "Parts Produced per Machine\n";
        for (PartMachine machine : this.getMachines()) {
            report += machine + "\t(" + 
            this.getDefectives().get(machine.getPart().getId()) +" defective)\t(" + 
            this.getInventory().get(machine.getPart().getId()).size() + " in inventory)\n";
        }

        report += "\nORDERS\n\n";
        for (Order transaction : this.getOrders()) {
            report += transaction + "\n";
        }
        System.out.println(report);
    }
}