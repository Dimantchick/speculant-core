package tk.dimantchick.speculant.core.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import tk.dimantchick.speculant.core.domain.instrument.Instrument;
import tk.dimantchick.speculant.core.domain.instrument.InstrumentStatus;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository extends PagingAndSortingRepository<Instrument, Long> {
    Optional<Instrument> findById(Integer integer);

    Optional<Instrument> findByFigi(String figi);

    List<Instrument> findByStatusNot(InstrumentStatus status);

    List<Instrument> findAll(Sort sort);



}
