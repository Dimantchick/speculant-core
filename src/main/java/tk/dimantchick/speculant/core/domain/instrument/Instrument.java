package tk.dimantchick.speculant.core.domain.instrument;

import ru.tinkoff.invest.openapi.model.rest.Currency;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "INSTRUMENTS")
public class Instrument {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String figi;
    private String ticker;
    private Currency currency;
    private InstrumentStatus status;

    public Instrument() {
    }

    public Instrument(int id, String figi, String ticker, Currency currency, InstrumentStatus status) {
        this.id = id;
        this.figi = figi;
        this.ticker = ticker;
        this.currency = currency;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFigi() {
        return figi;
    }

    public void setFigi(String figi) {
        this.figi = figi;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public InstrumentStatus getStatus() {
        return status;
    }

    public void setStatus(InstrumentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "id=" + id +
                ", figi='" + figi + '\'' +
                ", ticker='" + ticker + '\'' +
                ", currency=" + currency +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instrument that = (Instrument) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
