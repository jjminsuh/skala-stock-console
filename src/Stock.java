public class Stock {
    String stockName;
    double stockPrice;

    public Stock() {

    }

    public Stock(String stockName, double stockPrice) {
        this.stockName = stockName;
        this.stockPrice = stockPrice;
    }

    public String getStockName() {
        return stockName;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(stockName);
        result.append(":");
        result.append(stockPrice);

        return result.toString();
    }
}
