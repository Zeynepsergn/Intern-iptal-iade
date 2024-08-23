package tr.gov.gib.iade.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tr.gov.gib.gibcore.exception.GibException;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.util.ServiceMessage;
import tr.gov.gib.iade.entity.*;
import tr.gov.gib.iade.repository.IadeTalepRepository;
import tr.gov.gib.iade.object.request.IadeIslemRequest;
import tr.gov.gib.iade.object.response.IadeIslemResponse;
import tr.gov.gib.iade.service.IadeSorgulamaService;

import java.util.ArrayList;
import java.util.List;

@Service("IadeSorgulamaService")
public class IadeSorgulamaServiceImpl implements IadeSorgulamaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IadeSorgulamaServiceImpl.class);

    private final IadeTalepRepository iadeTalepRepository;


    public IadeSorgulamaServiceImpl(IadeTalepRepository iadeTalepRepository){
        this.iadeTalepRepository = iadeTalepRepository;
    }


    @Override
    public GibResponse<List<IadeIslemResponse>> iadeSorgula(IadeIslemRequest request) throws GibException {
        try {
            List<IadeTalep> iadeTalepleri = iadeTalepRepository.findIadeTalepsByTcknAndOdemeId(request.getTckn(), request.getId());
            List<IadeIslemResponse> responses = new ArrayList<>();
            iadeResponseOlustur(iadeTalepleri, responses);

            return GibResponse.<List<IadeIslemResponse>>builder()
                    .service(ServiceMessage.OK)
                    .data(responses)
                    .build();
        } catch (Exception e) {
            throw new GibException(ServiceMessage.NO_OK, e.getMessage());
        }
    }

    @Override
    public GibResponse<List<IadeIslemResponse>> tumIadeSorgula(IadeIslemRequest request) throws GibException {
        try {
            List<IadeTalep> iadeTalepleri = iadeTalepRepository.findIadeTalepsByTckn(request.getTckn());
            List<IadeIslemResponse> responses = new ArrayList<>();
            iadeResponseOlustur(iadeTalepleri, responses);

            return GibResponse.<List<IadeIslemResponse>>builder()
                    .service(tr.gov.gib.gibcore.util.ServiceMessage.OK)
                    .data(responses)
                    .build();
        } catch (Exception e) {
            throw new GibException(ServiceMessage.NO_OK, e.getMessage());
        }
    }


    private void iadeResponseOlustur(List<IadeTalep> iadeTalepleri, List<IadeIslemResponse> responses) {
        for (IadeTalep iadeTalep : iadeTalepleri) {
            Character iadeTalepTurChar = iadeTalep.getIadeTalepTur().toString().charAt(0);
            String iadeTalepTurDscString = iadeTalep.getIadeTalepTur().toString();
            Character iadeDurumChar = iadeTalep.getIadeTalepDurum().toString().charAt(0);

            IadeIslemResponse response = IadeIslemResponse.builder()
                    .iadeId(iadeTalep.getId())
                    .odemeId(iadeTalep.getOdemeId().getId())
                    .tckn(iadeTalep.getMukellefKullaniciId().getTckn())
                    .mukellefAdi(iadeTalep.getMukellefKullaniciId().getMukellefAd())
                    .mukellefSoyadi(iadeTalep.getMukellefKullaniciId().getMukellefSoyad())
//                  .iadeTutar(iadeTalep.getIadeEmanet().getIadeTutar())
                    .iadeTalepTur(iadeTalepTurChar)
                    .iadeTalepTurDsc(iadeTalepTurDscString)
                    .iadeDurum(iadeDurumChar)
                    .iadeDurumDsc("Durum Açıklaması")
                    .iadeZamani(iadeTalep.getOptime().toLocalDateTime())
                    .iadeAciklama("Açıklama")
                    .build();

            responses.add(response);
        }
    }
}
