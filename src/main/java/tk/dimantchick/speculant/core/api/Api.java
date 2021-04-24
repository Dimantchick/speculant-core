package tk.dimantchick.speculant.core.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class Api {

    private OpenApi openApi;
    private long lastAction;
    private static final int TIMEOUT = 500;
    private static final String BROCKER_ACCOUNT = null;

    @Autowired
    public Api(OpenApi openApi) {
        this.openApi = openApi;
        this.lastAction = System.currentTimeMillis();
    }

    private synchronized void sleep() {
        long wait = TIMEOUT - (System.currentTimeMillis() - lastAction);
        if (wait > 0) {
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
            }
        }
        lastAction = System.currentTimeMillis();
    }

    public List<Order> getOrders() {
        sleep();
        List<Order> orders = openApi.getOrdersContext().getOrders(BROCKER_ACCOUNT).join();
        return orders;
    }

    public PlacedLimitOrder placeLimitOrder(String figi, int lots, BigDecimal price, OperationType type) {
        sleep();
        LimitOrderRequest limitOrder = new LimitOrderRequest();
        limitOrder.setLots(lots);
        limitOrder.setPrice(price);
        limitOrder.setOperation(type);
        return openApi.getOrdersContext().placeLimitOrder(figi, limitOrder, BROCKER_ACCOUNT).join();
    }

    public PlacedMarketOrder placeMarketOrder(String figi, int lots, OperationType type) {
        sleep();
        MarketOrderRequest marketOrder = new MarketOrderRequest();
        marketOrder.setLots(lots);
        marketOrder.setOperation(type);
        return openApi.getOrdersContext().placeMarketOrder(figi, marketOrder, BROCKER_ACCOUNT).join();
    }

    public void cancelOrder(String orderId) {
        sleep();
        openApi.getOrdersContext().cancelOrder(orderId, BROCKER_ACCOUNT);
    }

    public Operations getOperations(String figi, OffsetDateTime start) {
        sleep();
        return openApi.getOperationsContext().getOperations(start, OffsetDateTime.now(), figi, BROCKER_ACCOUNT).join();
    }

    public Portfolio getPortfolio() {
        sleep();
        return openApi.getPortfolioContext().getPortfolio(BROCKER_ACCOUNT).join();
    }

    public List<CurrencyPosition> getCurrencies() {
        sleep();
        return openApi.getPortfolioContext().getPortfolioCurrencies(BROCKER_ACCOUNT).join().getCurrencies();
    }
    public CurrencyPosition getCurrency(Currency currency) {
        sleep();
        Currencies currencies = openApi.getPortfolioContext().getPortfolioCurrencies(BROCKER_ACCOUNT).join();
        List<CurrencyPosition> currencyPositions = currencies.getCurrencies();
        for (CurrencyPosition cp : currencyPositions) {
            if (cp.getCurrency().equals(currency)) {
                return cp;
            }
        }
        CurrencyPosition cp = new CurrencyPosition();
        cp.setCurrency(currency);
        cp.setBalance(BigDecimal.ZERO);
        cp.setBlocked(BigDecimal.ZERO);
        return cp;
    }

    public List<MarketInstrument> getStocks() {
        sleep();
        return openApi.getMarketContext().getMarketStocks().join().getInstruments();
    }

    public List<Candle> getHistoricalCandles(String figi, OffsetDateTime start, OffsetDateTime end, CandleResolution cr) {
        sleep();
        Optional<Candles> candles = openApi.getMarketContext().getMarketCandles(figi, start, end, cr).join();
        List<Candle> get = candles.get().getCandles();
        return get;
    }

    public MarketInstrument getInstrumentByFigi(String figi) {
        sleep();
        SearchMarketInstrument searchMarketInstrument = openApi.getMarketContext().searchMarketInstrumentByFigi(figi).join().get();
        return getInstrumentByTicker(searchMarketInstrument.getTicker());
    }

    public MarketInstrument getInstrumentByTicker(String ticker) {
        sleep();
        MarketInstrumentList marketInstrumentList = openApi.getMarketContext().searchMarketInstrumentsByTicker(ticker).join();
        final var instrumentOpt = marketInstrumentList.getInstruments().stream().findFirst();
        final MarketInstrument instrument;
        if (instrumentOpt.isEmpty()) {
            instrument = null;
        } else {
            instrument = instrumentOpt.get();
        }
        return instrument;
    }

    /*public LinkedList<CandleWithHA> grabCandles(String figi, OffsetDateTime start, OffsetDateTime end, CandleResolution resolution) {
        Optional<Candles> candles1 = openApi.getMarketContext().getMarketCandles(figi, start, end, resolution).join();
        List<Candle> get = candles1.get().getCandles();
        LinkedList<CandleWithHA> candles = new LinkedList<>();
        for (Candle c : get
        ) {
            CandleWithHA candleWithHA;
            if (candles.size() == 0) {
                candleWithHA = new CandleWithHA(c);
            } else {
                candleWithHA = new CandleWithHA(c, candles.getLast());
            }
            candles.add(candleWithHA);
        }
        return candles;
    }*/



}
