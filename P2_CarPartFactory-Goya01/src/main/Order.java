
/*
* This class represents an order that a customer has placed. It contains the following information:
* id: The unique id of the order
* customerName: The name of the customer who placed the order
* requestedParts: A map of the parts that the customer has requested. The key is the part id and the value is the quantity of that part
* fulfilled: A boolean value indicating whether the order has been fulfilled or not
* Implemented getters and setters for all the attributes.
*/
package main;

import interfaces.Map;

public class Order {
    private int id;
    private String customerName;
    private Map<Integer, Integer> requestedParts;
    private boolean fulfilled;

    public Order() {

    }

    public Order(int id, String customerName, Map<Integer, Integer> requestedParts, boolean fulfilled) {
        this.id = id;
        this.customerName = customerName;
        this.requestedParts = requestedParts;
        this.fulfilled = fulfilled;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public boolean isFulfilled() {
        return fulfilled;
    }
    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }
    public Map<Integer, Integer> getRequestedParts() {
        return requestedParts;
    }
    public void setRequestedParts(Map<Integer, Integer> requestedParts) {
        this.requestedParts = requestedParts;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        if (customerName == null) {
            throw new IllegalArgumentException("Customer name cannot be null");
        }
        this.customerName = customerName;
    }

    /**
    * Returns the order's information in the following format: {id} {customer name} {number of parts requested} {isFulfilled}
    * @return (String) The order's information
    */

    @Override
    public String toString() {
        return String.format("%d %s %d %s", this.getId(), this.getCustomerName(), this.getRequestedParts().size(), (this.isFulfilled())? "FULFILLED": "PENDING");
    }
}
