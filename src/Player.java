import java.util.ArrayList;

public class Player {
    private String playerId;
    private double playerMoney;
    private ArrayList<PlayerStock> playerStocks = new ArrayList<>();

    public Player() {

    }

    public Player(String id) {
        this.playerId = id;
        this.playerMoney = 10000;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public double getPlayerMoney() {
        return playerMoney;
    }

    public void setPlayerMoney(double playerMoney) {
        this.playerMoney = playerMoney;
    }

    public ArrayList<PlayerStock> getPlayerStocks() {
        return playerStocks;
    }

    public void setPlayerStocks(ArrayList<PlayerStock> playerStocks) {
        this.playerStocks = playerStocks;
    }

    public void addPlayerStock(PlayerStock stock) {
        boolean stockExists = false;

        for(PlayerStock existingStock: playerStocks) {
            if(existingStock.getStockName().equals(stock.getStockName())) {
                existingStock.setStockPrice(stock.getStockPrice());
                existingStock.setStockQuantity(existingStock.getStockQuantity() + stock.getStockQuantity());
                stockExists = true;
                break;
            }
        }

        if(!stockExists) {
            playerStocks.add(stock);
        }
    }

    public void updatePlayerStock(PlayerStock stock) {
        for(PlayerStock existingStock: playerStocks) {
            if(existingStock.getStockName().equals(stock.getStockName())) {
                existingStock.setStockPrice(stock.getStockPrice());
                existingStock.setStockQuantity(existingStock.getStockQuantity());
                if(existingStock.getStockQuantity() == 0) {
                    playerStocks.remove(existingStock);
                }
                break;
            }
        }
    }

    public PlayerStock findStock(int index) {
        if(index >= 0 && index < playerStocks.size()) {
            return playerStocks.get(index);
        }

        return null;
    }

    public String getPlayerStocksForFile() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < playerStocks.size(); i++) {
            if(i > 0) {
                sb.append("|");
            }

            sb.append(playerStocks.get(i));
        }

        return sb.toString();
    }

    public String getPlayerStocksForMenu() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < playerStocks.size(); i++) {
            sb.append(i + 1);
            sb.append(". ");
            sb.append(playerStocks.get(i).toString());

            if(i != playerStocks.size() - 1) {
                sb.append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
