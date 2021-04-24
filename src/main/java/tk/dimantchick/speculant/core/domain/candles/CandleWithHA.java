package tk.dimantchick.speculant.core.domain.candles;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.invest.openapi.model.rest.Candle;
import ru.tinkoff.invest.openapi.model.rest.CandleResolution;
import tk.dimantchick.speculant.core.domain.instrument.Instrument;
import tk.dimantchick.speculant.core.repository.InstrumentRepository;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "CANDLES_5MIN")
public class CandleWithHA {
    public static final ZoneId ZONE = ZoneId.of("Z");
    @Autowired
    @Transient
    private InstrumentRepository instrumentRepository;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="instrument_id")
    public Instrument instrument;
//    @JsonProperty("time")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
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
    public CandleResolution resolution;

    public CandleWithHA(Candle candle, Instrument instrument) {
        this.time = candle.getTime();
        this.openPrice = candle.getO();
        this.closePrice = candle.getC();
        this.lowestPrice = candle.getL();
        this.highestPrice = candle.getH();
        this.instrument = instrument;
        this.volume = candle.getV();
        HAopenPrice = openPrice;
        HAclosePrice = closePrice;
        HAlowestPrice = lowestPrice;
        HAhighestPrice = highestPrice;
        this.resolution = candle.getInterval();
    }

    public CandleWithHA(Candle candle, CandleWithHA last, Instrument instrument) {
        this.time = candle.getTime();
        this.openPrice = candle.getO();
        this.closePrice = candle.getC();
        this.lowestPrice = candle.getL();
        this.highestPrice = candle.getH();
        this.instrument = instrument;
        this.volume = candle.getV();
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
                '}';
    }

    public CandleWithHA() {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
