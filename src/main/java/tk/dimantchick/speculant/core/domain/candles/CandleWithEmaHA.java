package tk.dimantchick.speculant.core.domain.candles;

import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.invest.openapi.model.rest.Candle;
import ru.tinkoff.invest.openapi.model.rest.CandleResolution;
import tk.dimantchick.speculant.core.domain.instrument.Instrument;
import tk.dimantchick.speculant.core.repository.InstrumentRepository;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

@Entity
@Table(name = "CANDLES_HOUR")
public class CandleWithEmaHA {
//    public static final ZoneId ZONE = ZoneId.of("Z");
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Autowired
    @Transient
    private InstrumentRepository instrumentRepository;

    @ManyToOne
    @JoinColumn(name="instrument_id")
    public Instrument instrument;
    public OffsetDateTime time;
    public BigDecimal openPrice;
    public BigDecimal closePrice;
    public BigDecimal lowestPrice;
    public BigDecimal highestPrice;
    public BigDecimal HAopenPrice;
    public BigDecimal HAclosePrice;
    public BigDecimal HAlowestPrice;
    public BigDecimal HAhighestPrice;
    public int volume;
    public BigDecimal ema5;
    public BigDecimal ema10;
    public BigDecimal ema20;
    public BigDecimal ema50;
    public BigDecimal ema100;
    public BigDecimal ema200;
    public CandleResolution resolution;

    public static final BigDecimal ema5A = new BigDecimal(2D/(5D + 1D));
    public static final BigDecimal ema10A = new BigDecimal(2D/(10D + 1D));
    public static final BigDecimal ema20A = new BigDecimal(2D/(20D + 1D));
    public static final BigDecimal ema50A = new BigDecimal(2D/(50D + 1D));
    public static final BigDecimal ema100A = new BigDecimal(2D/(100D + 1D));
    public static final BigDecimal ema200A = new BigDecimal(2D/(200D + 1D));


    public CandleWithEmaHA(Candle candle, Instrument instrument) {
        this.time = candle.getTime();
        this.openPrice = candle.getO();
        this.closePrice = candle.getC();
        this.lowestPrice = candle.getL();
        this.highestPrice = candle.getH();
        this.instrument = instrument;
        this.volume = candle.getV();
        ema5 = closePrice;
        ema10 = closePrice;
        ema20 = closePrice;
        ema50 = closePrice;
        ema100 = closePrice;
        ema200 = closePrice;
        HAopenPrice = openPrice;
        HAclosePrice = closePrice;
        HAlowestPrice = lowestPrice;
        HAhighestPrice = highestPrice;
        this.resolution = candle.getInterval();
    }

    public CandleWithEmaHA(Candle candle, CandleWithEmaHA last, Instrument instrument) {
        this.time = candle.getTime();
        this.openPrice = candle.getO();
        this.closePrice = candle.getC();
        this.lowestPrice = candle.getL();
        this.highestPrice = candle.getH();
        this.instrument = instrument;
        this.volume = candle.getV();
        ema5 = closePrice.multiply(ema5A).add(BigDecimal.ONE.subtract(ema5A).multiply(last.ema5)).setScale(4, RoundingMode.HALF_UP);
        ema10 = closePrice.multiply(ema10A).add(BigDecimal.ONE.subtract(ema10A).multiply(last.ema10)).setScale(4, RoundingMode.HALF_UP);
        ema20 = closePrice.multiply(ema20A).add(BigDecimal.ONE.subtract(ema20A).multiply(last.ema20)).setScale(4, RoundingMode.HALF_UP);
        ema50 = closePrice.multiply(ema50A).add(BigDecimal.ONE.subtract(ema50A).multiply(last.ema50)).setScale(4, RoundingMode.HALF_UP);
        ema100 = closePrice.multiply(ema100A).add(BigDecimal.ONE.subtract(ema100A).multiply(last.ema100)).setScale(4, RoundingMode.HALF_UP);
        ema200 = closePrice.multiply(ema200A).add(BigDecimal.ONE.subtract(ema200A).multiply(last.ema200)).setScale(4, RoundingMode.HALF_UP);
        HAopenPrice = (last.HAopenPrice.add(last.HAclosePrice)).divide(new BigDecimal(2)).setScale(10, RoundingMode.HALF_UP);
        HAclosePrice = (openPrice.add(closePrice).add(lowestPrice).add(highestPrice)).divide(new BigDecimal(4));
        HAlowestPrice = lowestPrice.min(HAopenPrice).min(HAclosePrice);
        HAhighestPrice = highestPrice.max(HAopenPrice).max(HAclosePrice);
        this.resolution = candle.getInterval();
    }

    @Override
    public String toString() {
        return "tk.dimantchick.speculant.candles.CandleWithHA{" +
                "instrument=" + instrument +
                ", time=" + time +
                ", resolution=" + resolution +
                ", openPrice=" + openPrice +
                ", closePrice=" + closePrice +
                ", lowestPrice=" + lowestPrice +
                ", highestPrice=" + highestPrice +
                ", HAopenPrice=" + HAopenPrice +
                ", HAclosePrice=" + HAclosePrice +
                ", HAlowestPrice=" + HAlowestPrice +
                ", HAhighestPrice=" + HAhighestPrice +
                ", volume=" + volume +
                ", ema5=" + ema5 +
                ", ema10=" + ema10 +
                ", ema20=" + ema20 +
                ", ema50=" + ema50 +
                ", ema100=" + ema100 +
                ", ema200=" + ema200 +
                '}';
    }

    public CandleWithEmaHA() {
    }

    public boolean isHAGreen() {

        return HAopenPrice.compareTo(HAclosePrice) < 0;
    }
    public boolean isHARed() {
        return HAopenPrice.compareTo(HAclosePrice) > 0;
    }
    public double HAoc() {
        return Math.abs(HAopenPrice.doubleValue() - HAclosePrice.doubleValue());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
