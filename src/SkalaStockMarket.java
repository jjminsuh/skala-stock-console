import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/* 변경사항
1. double 자료형 이용
    - Stock.java
2. 주식 구매, 주식 판매 진행 시 "뒤로가기" 옵션 추가 - 실제로 사용해보면서 불편함을 느낌
    - SkalaStockMarket.java
        - buyStock()
        - sellStock()
3. 주식 가격 실시간 업데이트
    - SkalaStockMarket.java
        - start()
    - StockRepository.java
        - changeRandomStockPrice()
4. 주식 구매 시 종목 추천
    - SkalaRepository.java
        - getMinStock()
5. admin 기능 추가 - 관리자로 로그인 하는 경우 주식 추가, 삭제 기능
    - SkalaStockMarket.java
        - start()
        - startAdminMode()
        - startPlayerMode()
        - addStock()
        - deleteStock()
    - StockRepository.java
        - addNewStock()
        - deleteStock()
*/

/* 보완할 점
1. admin 로그인을 위한 비밀번호 추가
2. 종목 추천 시 사용하는 알고리즘 - 종목이 매우 많아지는 경우 한계가 존재 / 현실에서는 이러한 방식으로 추천 불가
3. 타이머를 분리
4. admin 기능과 player 기능이 하나의 클래스에 합쳐져 있음 -> 분리할 수 있는 방법 고민
현재는 UI 관련 입출력을 SkalaStockMarket 클래스에서 처리한다는 관점에서 하나의 클래스로 만듦
*/


public class SkalaStockMarket {
    private PlayerRepository playerRepository = new PlayerRepository();
    private StockRepository stockRepository = new StockRepository();
    private Player player = null;

    public void start() {

        stockRepository.loadStockList();

        playerRepository.loadPlayerList();

        Scanner scanner = new Scanner(System.in);

        // 가격 변동 업데이트 위한 타이머
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                stockRepository.changeRandomStockPrice();
                stockRepository.saveStock();
            }
        };

        timer.scheduleAtFixedRate(task, 10000, 10000); 

        // 프로그램 시작
        System.out.print("플레이어 ID를 입력하세요: ");
        String playerId = scanner.nextLine();

        Boolean isAdmin = false;

        if(playerId.equals("admin")) { // 관리자인 경우
            isAdmin = true;
        } else {
            player = playerRepository.findPlayer(playerId);
            if (player == null) { // 새로운 플레이어
                player = new Player(playerId);

                System.out.print("초기 투자금을 입력하세요: ");
                int money = scanner.nextInt();
                player.setPlayerMoney(money);
                playerRepository.addPlayer(player);
            }
            displayPlayerStocks();
        }

        if(isAdmin) {
            startAdminMode(scanner);
        } else {
            startPlayerMode(scanner);
        }

        scanner.close();
        timer.cancel();
    }

    // 관리자 모드 실행
    private void startAdminMode(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== 스칼라 주식 프로그램 관리자 메뉴 ===");
            System.out.println("1. 주식 목록");
            System.out.println("2. 주식 추가");
            System.out.println("3. 주식 삭제");
            System.out.println("0. 프로그램 종료");

            System.out.print("선택: ");
            int code = scanner.nextInt();
            scanner.nextLine(); // 개행 버퍼 삭제

            switch (code) {
                case 1:
                    displayStockList();
                    break;
                case 2:
                    addStock(scanner);
                    break;
                case 3:
                    deleteStock(scanner);
                    break;
                case 0:
                    System.out.println("프로그램을 종료합니다...Bye");
                    running = false;
                    break;
                default:
                    System.out.println("올바른 번호를 선택하세요.");
            }
        }
    }

    // 주식 목록에서 주식 항목 추가
    private void addStock(Scanner scanner) {
        System.out.println("\n추가할 주식 이름을 입력하세요.:");
        String inputName = scanner.nextLine();


        System.out.println("\n추가할 주식 가격을 입력하세요.:");
        Double inputDouble = scanner.nextDouble();

        Stock newStock = new Stock(inputName, inputDouble);
        stockRepository.addNewStock(newStock);
        stockRepository.saveStock();
    }

    // 주식 목록에서 주식 항목 삭제
    private void deleteStock(Scanner scanner) {
        displayStockList();

        System.out.println("0. 뒤로가기");

        System.out.println("\n삭제할 주식 번호를 선택하세요:");
        System.out.print("선택: ");
        int index = scanner.nextInt() - 1;

        Stock selectedStock = stockRepository.findStock(index);
        if (selectedStock != null) {
           stockRepository.deleteStock(index);
           stockRepository.saveStock();
           System.out.println("삭제되었습니다.\n");
        } else if(index == -1){
            return;
        } else {
            System.out.println("ERROR: 잘못된 선택입니다.");
        }

    }

    // 플레이어 모드 실행
    private void startPlayerMode(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== 스칼라 주식 프로그램 메뉴 ===");
            System.out.println("1. 보유 주식 목록");
            System.out.println("2. 주식 구매");
            System.out.println("3. 주식 판매");
            System.out.println("0. 프로그램 종료");

            System.out.print("선택: ");
            int code = scanner.nextInt();

            switch (code) {
                case 1:
                    displayPlayerStocks();
                    break;
                case 2:
                    buyStock(scanner);
                    break;
                case 3:
                    sellStock(scanner);
                    break;
                case 0:
                    System.out.println("프로그램을 종료합니다...Bye");
                    running = false;
                    break;
                default:
                    System.out.println("올바른 번호를 선택하세요.");
            }
        }
       
    }

    private void displayPlayerStocks() {
        System.out.println("\n######### 플레이어 정보 #########");
        System.out.println("- 플레이어ID : " + player.getPlayerId());
        System.out.println("- 보유금액 : " + player.getPlayerMoney());
        System.out.println("- 보유 주식 목록");
        System.out.println(player.getPlayerStocksForMenu());
    }

    private void displayStockList() {
        System.out.println("\n=== 주식 목록 ===");
        System.out.println(stockRepository.getStockListForMenu());
    }

    // 주식 구매
    private void buyStock(Scanner scanner) {
        System.out.println("\n구매할 주식 번호를 선택하세요:");
        displayStockList();
        System.out.println("0. 뒤로가기");

        System.out.println("추천 주식은: " + stockRepository.getMinStock().getStockName() + "입니다.");

        System.out.print("선택: ");
        int index = scanner.nextInt() - 1;

        Stock selectedStock = stockRepository.findStock(index);
        if (selectedStock != null) {
            System.out.print("구매할 수량을 입력하세요: ");
            int quantity = scanner.nextInt();

            double totalCost = Double.parseDouble(
                                    String.format("%.2f", selectedStock.getStockPrice() * quantity));
            double playerMoney = player.getPlayerMoney();
            if (totalCost <= playerMoney) {
                player.setPlayerMoney(playerMoney - totalCost);
                player.addPlayerStock(new PlayerStock(selectedStock, quantity));
                System.out.println(quantity + "주를 구매했습니다! 남은 금액: " + player.getPlayerMoney());

                // 변경된 내용을 파일로 저장
                playerRepository.savePlayerList();
            } else {
                System.out.println("ERROR: 금액이 부족합니다.");
            }
        } else if(index == -1){
            return;
        } else {
            System.out.println("ERROR: 잘못된 선택입니다.");
        }
    }

    // 주식 판매
    private void sellStock(Scanner scanner) {
        System.out.println("\n판매할 주식 번호를 선택하세요:");
        displayPlayerStocks();
        System.out.println("0. 뒤로가기");

        System.out.print("선택: ");
        int index = scanner.nextInt() - 1;

        PlayerStock playerStock = player.findStock(index);
        if (playerStock != null) {
            System.out.print("판매할 수량을 입력하세요: ");
            int quantity = scanner.nextInt();

            // 어얼리 리턴
            if (quantity > playerStock.getStockQuantity()) {
                System.out.println("ERROR: 수량이 부족합니다.");
                return;
            }

            Stock baseStock = stockRepository.findStock(playerStock.getStockName());
            double playerMoney = player.getPlayerMoney() + baseStock.getStockPrice() * quantity;
            player.setPlayerMoney(playerMoney);

            playerStock.setStockQuantity(playerStock.getStockQuantity() - quantity);
            player.updatePlayerStock(playerStock);

            // 변경된 내용을 파일로 저장
            playerRepository.savePlayerList();

        } else if(index == -1){
            return;
        } else {
            System.out.println("ERROR: 잘못된 선택입니다.");
        }
    }
}
