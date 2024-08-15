package tr.gov.gib.iade.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tr.gov.gib.gibcore.exception.GibException;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.util.ServiceMessage;
import tr.gov.gib.iade.entity.*;
import tr.gov.gib.iade.repository.IadeEmanetRepository;
import tr.gov.gib.iade.repository.IadeTalepRepository;
import tr.gov.gib.iade.repository.OdemeDetayRepository;
import tr.gov.gib.iade.object.request.IadeIslemRequest;
import tr.gov.gib.iade.object.response.IadeIslemResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service("IadeIslemService")
public class IadeIslemServiceImpl implements IadeIslemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IadeIslemServiceImpl.class);

    private final IadeEmanetRepository iadeEmanetRepository;
    private final IadeTalepRepository iadeTalepRepository;
    private final OdemeDetayRepository odemeDetayRepository;

    public IadeIslemServiceImpl(IadeEmanetRepository iadeEmanetRepository,
                                IadeTalepRepository iadeTalepRepository,
                                OdemeDetayRepository odemeRepository) {
        this.iadeEmanetRepository = iadeEmanetRepository;
        this.iadeTalepRepository = iadeTalepRepository;
        this.odemeDetayRepository = odemeRepository;
    }

    @Override
    public void processIade(Long iadeTalepId) {
        int iadeTalepIdInt = iadeTalepId.intValue();
        Optional<IadeTalep> optionalIadeTalep = iadeTalepRepository.findById(iadeTalepIdInt);
        if (optionalIadeTalep.isEmpty()) {
            LOGGER.error("İade talebi bulunamadı, ID: {}", iadeTalepId);
            return;
        }

        IadeTalep iadeTalep = optionalIadeTalep.get();

        List<OdemeDetay> odemeDetayList = odemeDetayRepository.findByOdemeIdIn(Collections.singletonList(iadeTalep.getOdemeId().getId()));

        if (odemeDetayList.isEmpty()) {
            LOGGER.error("Ödeme bulunamadı, ID: {}", iadeTalep.getOdemeId().getId());
            return;
        }

        Odeme odeme = odemeDetayList.get(0).getOdemeId();
        if (odeme.getOdemeDurum() == 0) {

            VergiOdemeTur vergiOdemeTur = new VergiOdemeTur();
            vergiOdemeTur.setVergiOdemeTur(odeme.getVergiTurId());

            handleIade(iadeTalep, odeme, vergiOdemeTur); // handleIade metoduna ödeme türünü geçiyoruz
        } else {
            LOGGER.warn("Geçersiz ödeme durumu, ID: {}", odeme.getId());
        }
    }


    private void handleIade(IadeTalep iadeTalep, Odeme odeme, VergiOdemeTur vergiOdemeTur) {
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        switch (vergiOdemeTur.getVergiOdemeTur()) {
            case 0: // SanalPos
                handleSanalPosIade(iadeTalep, odeme, now);
                break;
            case 1: // FizikselPos
                handleFizikselPosIade(iadeTalep, odeme, now);
                break;
            case 2: // Nakit
                handleNakitIade(iadeTalep, odeme);
                break;
            default:
                LOGGER.warn("Geçersiz ödeme türü, ID: {}", odeme.getId());
        }
    }

    private void handleSanalPosIade(IadeTalep iadeTalep, Odeme odeme, LocalDateTime now) {
        LocalDateTime deadline = odeme.getOptime().plusDays(2).toLocalDateTime();
        if (now.isBefore(deadline)) {
            createIadeEmanet(iadeTalep, odeme);
        } else {
            LOGGER.warn("Sanal POS iade süresi geçmiş, ID: {}", odeme.getId());
        }
    }

    private void handleFizikselPosIade(IadeTalep iadeTalep, Odeme odeme, LocalDateTime now) {
        LocalDateTime deadline = odeme.getOptime().toLocalDate().atTime(17, 0);
        if (now.toLocalDate().equals(odeme.getOptime().toLocalDate()) && now.isBefore(deadline)) {
            createIadeEmanet(iadeTalep, odeme);
        } else {
            LOGGER.warn("Fiziksel POS iade süresi geçmiş, ID: {}", odeme.getId());
        }
    }

    private void handleNakitIade(IadeTalep iadeTalep, Odeme odeme) {
        if (iadeTalep.getIadeAciklama() != null && !iadeTalep.getIadeAciklama().isEmpty()) {
            createIadeEmanet(iadeTalep, odeme);
        } else {
            LOGGER.warn("Nakit ödemelerde dilekçe gereklidir, ID: {}", odeme.getId());
        }
    }

    private void createIadeEmanet(IadeTalep iadeTalep, Odeme odeme) {
        IadeEmanet iadeEmanet = new IadeEmanet();

        iadeEmanet.setOdemeId(odeme);
        iadeEmanet.setIadeTalepId(iadeTalep);
        iadeEmanet.setIadeTutar(iadeEmanet.getIadeTutar());
        iadeEmanet.setOptime(OffsetDateTime.now(ZoneId.systemDefault()));
        iadeEmanet.setIadeEmanetDurum((short) 0);

        iadeEmanetRepository.save(iadeEmanet);
        LOGGER.info("İade emanet oluşturuldu, ID: {}", iadeEmanet.getId());
    }

    @Override
    public GibResponse<List<IadeIslemResponse>> iadeSorgula(IadeIslemRequest request) throws GibException {
        try {
            List<IadeTalep> iadeTalepleri = iadeTalepRepository.findIadeTalepsByTcknAndOdemeId(request.getTckn(), request.getOdemeId());
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

    @Override
    public BigDecimal getOdenenBorcMiktariByOdemeId(Integer odemeId) {
        List<OdemeDetay> odemeDetayList = odemeDetayRepository.findByOdemeIdIn(Collections.singletonList(odemeId));

        if (odemeDetayList.isEmpty()) {
            // Hata yönetimi yapılabilir
            LOGGER.warn("OdemeDetay bulunamadı, ID: {}", odemeId);
            return null;
        }

        // İlk OdemeDetay nesnesini alıyoruz
        OdemeDetay odemeDetay = odemeDetayList.get(0);
        Odeme odeme = odemeDetay.getOdemeId();

        // Ödeme detayından borç miktarını alıyoruz
        return odeme.getMukellefBorcId().getKalanBorc();
    }



    @Override
    public MukellefKullanici getMukellefByTckn(String tckn) {
        return getMukellefByTckn(tckn);
    }


    @Override
    public List<Odeme> getOdemeByMukellefId(Integer mukellefId) {
        return getOdemeByMukellefId(mukellefId);
    }

    @Override
    public List<OdemeDetay> getOdemeDetayByOdemeList(List<Odeme> odemeler) {
        List<Integer> odemeIds = new ArrayList<>();
        for (Odeme odeme : odemeler) {
            odemeIds.add(odeme.getId());
        }
        return odemeDetayRepository.findByOdemeIdIn(odemeIds);
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

    @Scheduled(cron = "0 0 * * * ?")
    public void scheduledIadeUpdate() {
        LOGGER.info("Scheduled iade update başladı.");
        List<IadeTalep> iadeTalepleri = iadeTalepRepository.findAll();
        iadeTalepleri.forEach(iadeTalep -> processIade(Long.valueOf(iadeTalep.getId())));
        LOGGER.info("Scheduled iade update tamamlandı.");
    }
}