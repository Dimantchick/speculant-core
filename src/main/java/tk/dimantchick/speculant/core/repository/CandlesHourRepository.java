package tk.dimantchick.speculant.core.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import tk.dimantchick.speculant.core.domain.candles.CandleWithEmaHA;
import tk.dimantchick.speculant.core.domain.instrument.Instrument;

import java.time.OffsetDateTime;

public interface CandlesHourRepository extends CrudRepository<CandleWithEmaHA, Long> {

    Slice<CandleWithEmaHA> findByInstrument(Instrument instrument, Pageable pageable);
    void deleteByTimeLessThan(OffsetDateTime time);
}
