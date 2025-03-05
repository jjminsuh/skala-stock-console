import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class StockRepository {
    public final static String STOCK_FILE_PATH = "data/stocks.txt";

    List<Stock> stockList = new ArrayList<>();

    public void loadStockList() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STOCK_FILE_PATH))){
            String line;
            while((line = reader.readLine()) != null) {
                Stock parsedStock = stockLineParse(line);

                if(parsedStock != null) {
                    stockList.add(parsedStock);
                }
            }
        } catch (IOException e) {
            stockList.add(new Stock("TechCorp", 100));
            stockList.add(new Stock("GreenEnergy", 80));
            stockList.add(new Stock("SkalaEdu", 120));

            saveStock();
        }
    }

    public void saveStock() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STOCK_FILE_PATH))){
            for(Stock stock: stockList) {
                writer.write(stock.getStockName() + "," + stock.getStockPrice());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("저장 중 오류가 발생했습니다.");
        }
    }

    // 새로운 stock 추가
    public void addNewStock(Stock newStock) {
        stockList.add(newStock);
    }

    // stock 삭제
    public void deleteStock(int index) {
        stockList.remove(index);
    }

    // stock 가격 random update
    public void changeRandomStockPrice () {
        for(Stock stock: stockList) {
            double percent = (Math.random() * 0.6) - 0.3;   // -0.3 ~ 0.3
            BigDecimal total = new BigDecimal(stock.getStockPrice() + (stock.getStockPrice() * percent)).setScale(2, RoundingMode.DOWN);
            stock.setStockPrice(Double.parseDouble(String.format("%.2f", total)));
        }
    }

    public String getStockListForMenu() {
        StringBuilder sb = new StringBuilder();

        for(int i = 1; i <= stockList.size(); i++) {
            sb.append(i);
            sb.append(". ");
            sb.append(stockList.get(i - 1).getStockName());
            sb.append(": ");
            sb.append(stockList.get(i - 1).getStockPrice());

            if(i != stockList.size()) {
                sb.append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

    public Stock findStock(int index) {
        if(index >= 0 && index < stockList.size()) {
            return stockList.get(index);
        }

        return null;
    }

    public Stock findStock(String name) {
        for(Stock stock: stockList) {
            if(stock.getStockName().equals(name)) {
                return stock;
            }
        }

        return null;
    }

    // 가격이 가장 싼 종목 찾기
    public Stock getMinStock() {
        double min = stockList.get(0).getStockPrice();
        Stock minStock = stockList.get(0);

        for(Stock stock: stockList) {
            if(Double.compare(stock.getStockPrice(), min) < 0) { // 비교연산 주의
                minStock = stock;
            }
        }

        return minStock;
    }

    private Stock stockLineParse(String line) {
        String[] input = line.split(",");
        if(input.length > 1) {
            return new Stock(input[0], Double.parseDouble(input[1]));
        } else {
            System.out.println("파일 분석이 불가합니다. " + line);
            return null;
        }
    }
}
