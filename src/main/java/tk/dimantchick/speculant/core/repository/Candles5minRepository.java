package tk.dimantchick.speculant.core.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import tk.dimantchick.speculant.core.domain.candles.CandleWithHA;
import tk.dimantchick.speculant.core.domain.instrument.Instrument;

import java.time.OffsetDateTime;

public interface Candles5minRepository extends CrudRepository<CandleWithHA, Long> {

    Slice<CandleWithHA> findByInstrument(Instrument instrument, Pageable pageable);

    //@Modifying
    //@Query("delete from candles_5min b where b.time<:time")
    //void deleteOld(@Param("time") OffsetDateTime time);
    @Transactional
    void deleteAllByTimeLessThan(OffsetDateTime time);

}
