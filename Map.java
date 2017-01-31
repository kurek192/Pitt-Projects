//MAP CLASS
//Stores the map environment 
public class Map {

    private int first; //first member of pair
    private int second; //second member of pair
    private double restTime;
    private String value, restValue;
    private boolean active, rest;

    public Map(int first, int second, String value) {
        this.first = first;
        this.second = second;
        this.value = value;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void setValue(String value) {
        this.value = value;
        this.active = false;
    }

    public boolean getStatus() {
        return this.active;
    }

    public void setRest(boolean b, String restValue) {
        this.rest = b;
        this.restValue = restValue;
        if(!restValue.equals("null")){
        this.restTime = Math.floor(Math.pow(Double.parseDouble(restValue), 2) * 100);
        }else{
        this.restTime=0;    
        }
    }

    public boolean getRest() {
        return this.rest;
    }

    public String getRestValue() {
        return this.restValue;
    }

    public Double getRestTime() {
        return this.restTime;
    }

    public void decrementRest() {
        this.restTime--;
    }

    public void setActive() {
        this.active = true;
    }

    public void setInactive() {
        this.active = false;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public String getValue() {
        return value;
    }
    
    public Double getProbabilty(){
        return Double.parseDouble(this.value);
    }
    
}
