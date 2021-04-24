package tk.dimantchick.speculant.core.domain.instrument;

import org.springframework.stereotype.Component;
import tk.dimantchick.speculant.core.repository.InstrumentRepository;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class ActiveInstruments {

    private CopyOnWriteArraySet<Instrument> instruments = new CopyOnWriteArraySet<>();

    private InstrumentRepository instrumentRepository;

    public ActiveInstruments(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
        List<Instrument> instrumentList = instrumentRepository.findByStatusNot(InstrumentStatus.DISABLED);
        instruments.addAll(instrumentList);
    }

    public CopyOnWriteArraySet<Instrument> getInstruments() {
        return instruments;
    }

    public void updateInstruments() {
        List<Instrument> instrumentList = instrumentRepository.findByStatusNot(InstrumentStatus.DISABLED);
        Iterator<Instrument> it = instruments.iterator();
        while (it.hasNext()){
            Instrument active = it.next();
            if (instrumentList.contains(active)) {
                InstrumentStatus newStatus = instrumentList.get(instrumentList.indexOf(active)).getStatus();
                active.setStatus(newStatus);
            }
            else {
                it.remove();
            }
        }
    }


}
