public class PlayerStock extends Stock {
    private int stockQuantity;

    public PlayerStock(Stock stock, int quantity) {
        this.stockName = stock.getStockName();
        this.stockPrice = stock.getStockPrice();
        this.stockQuantity = quantity;
    }

    public PlayerStock(String name, String price, String quantity) {
        this.stockName = name;
        this.stockPrice = Double.parseDouble(price);
        this.stockQuantity = Integer.parseInt(quantity);
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(super.toString());
        result.append(":");
        result.append(stockQuantity);
        return result.toString();
    }
}
