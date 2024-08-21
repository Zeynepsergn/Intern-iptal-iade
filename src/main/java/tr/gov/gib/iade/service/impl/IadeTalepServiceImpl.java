package tr.gov.gib.iade.service.impl;

import org.springframework.stereotype.Service;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.util.ServiceMessage;
import tr.gov.gib.iade.entity.IadeTalep;
import tr.gov.gib.iade.object.request.IadeTalepRequest;
import tr.gov.gib.iade.object.response.IadeTalepResponse;
import tr.gov.gib.iade.repository.IadeTalepRepository;
import tr.gov.gib.iade.service.IadeTalepService;

@Service("IadeTalepService")
public class IadeTalepServiceImpl implements IadeTalepService {

    private final IadeTalepRepository iadeTalepRepository;

    public IadeTalepServiceImpl(IadeTalepRepository iadeTalepRepository) {
        this.iadeTalepRepository = iadeTalepRepository;
    }

    @Override
    public GibResponse iadeTalebiOnayla(IadeTalepRequest iadeTalepRequest) {
        IadeTalep iadeTalep = iadeTalepRepository.findIadeTalepById(iadeTalepRequest.getId());
        iadeTalep.setIadeTalepDurum(iadeTalepRequest.getIadeTalepDurum());
        iadeTalepRepository.save(iadeTalep);

        return GibResponse.builder()
                .data(
                        IadeTalepResponse.builder()
                                .talepId(iadeTalepRequest.getId())
                                .talepSonucu("Talep onaylandı.")
                                .build())
                .service(ServiceMessage.OK)
                .build();

    }
}
