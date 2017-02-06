//William Kurek
//Project 1

public class Pair<T1, T2> {

    private T1 First;
    private T2 Second;

    public Pair(T1 aFirst, T2 aSecond) {
        First = aFirst;
        Second = aSecond;
    }
    

    /**
     * Gets the first element of this pair.
     *
     * @return the first element of this pair.
     */
    public T1 fst() {
        return First;
    }

    /**
     * Gets the second element of this pair.
     *
     * @return the second element of this pair.
     */
    public T2 snd() {
        
        return Second;
    }

    /**
     * Sets the first element to aFirst.
     *
     * @param aFirst the new first element
     */
    public void setFst(T1 aFirst) {
        First = aFirst;

    }

    /**
     * Sets the second element to aSecond.
     *
     * @param aSecond the new second element
     */
    public void setSnd(T2 aSecond) {
        Second = aSecond;
    }

    /**
     * Checks whether two pairs are equal. Note that the pair (a,b) is equal to
     * the pair (x,y) if and only if a is equal to x and b is equal to y.
     *
     * @return true if this pair is equal to aPair. Otherwise return false.
     */
    public boolean equals(Object otherObject) {
        if (otherObject == null) {
            return false;
        }

        if (getClass() != otherObject.getClass()) {
            return false;
        }

        if (otherObject instanceof Pair) {

            Pair<?, ?> pair1 = (Pair<?, ?>) otherObject;
            if (pair1.First.equals(this.First) && pair1.Second.equals(this.Second)) {
                return (true);
            }
        }
        return (false);

    }

    /**
     * Generates a string representing this pair. Note that the String
     * representing the pair (x,y) is "(x,y)". There is no whitespace unless x
     * or y or both contain whitespace themselves.
     *
     * @return a string representing this pair.
     */
    public String toString() {
        return "(" + this.fst() + "," + this.snd() + ")";
    }
}
