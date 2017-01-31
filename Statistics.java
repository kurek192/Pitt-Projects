//STATISTICS CLASS
//various calculations for statistical values

public class Statistics {

    double[] data;
    int size;

    public Statistics(double[] data) {
        this.data = data;
        size = data.length;
    }

    double mean() {
        double sum = 0.0;
        for (double a : data) {
            sum += a;
        }
        return sum / size;
    }

    double variance() {
        double mean = mean();
        double temp = 0;
        for (double a : data) {
            temp += (a - mean) * (a - mean);
        }
        return temp / size;
    }

    double standardDeviation() {
        return Math.sqrt(variance());
    }

}
