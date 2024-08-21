package tr.gov.gib.iade.service.impl;

import org.springframework.stereotype.Service;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.iade.entity.IadeTalep;
import tr.gov.gib.iade.entity.OdemeDetay;
import tr.gov.gib.iade.object.request.IadeTalepRequest;
import tr.gov.gib.iade.repository.IadeTalepRepository;
import tr.gov.gib.iade.repository.OdemeDetayRepository;
import tr.gov.gib.iade.service.IadeTalepOlusturmaService;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class IadeTalepOlusturmaServiceImpl implements IadeTalepOlusturmaService {

    private final OdemeDetayRepository odemeDetayRepository;
    private final IadeTalepRepository iadeTalepRepository;

    public IadeTalepOlusturmaServiceImpl(OdemeDetayRepository odemeDetayRepository, IadeTalepRepository iadeTalepRepository) {
        this.odemeDetayRepository = odemeDetayRepository;
        this.iadeTalepRepository = iadeTalepRepository;
    }

    @Override
    public GibResponse iadeTalebiOlustur(IadeTalepRequest requestData) {
        GibResponse response = new GibResponse();

        if (requestData == null || requestData.getId() == null) {
            response.setMessage("Geçersiz talep verisi.");
            return response;
        }

        Integer odemeId = requestData.getId();
        String iadeAciklama = requestData.getIadeAciklama();

        // Ödeme detayını OdemeDetay tablosundan bul
        OdemeDetay odemeDetay = odemeDetayRepository.findByOdemeId(odemeId);

        if (odemeDetay == null) {
            response.setMessage("Ödeme detayı bulunamadı.");
            return response;
        }

        // İade talebini oluştur
        IadeTalep iadeTalep = new IadeTalep();
        iadeTalep.setOdemeId(odemeDetay.getOdemeId());
        iadeTalep.setIadeTalepDurum((short) 0); // Varsayılan değer
        iadeTalep.setIadeAciklama(iadeAciklama);
        iadeTalep.setOptime(OffsetDateTime.now());
        iadeTalep.setMukellefKullaniciId(odemeDetay.getMukellefKullaniciId());
        iadeTalep.setIadeTalepTur(requestData.getIadeTalepTur()); // Talep türünü ayarla
        iadeTalep.setMukellefIban(requestData.getMukellefIban());

        BigDecimal odenenBorcMiktari = odemeDetay.getOdenenBorcMiktari();
        if (odenenBorcMiktari != null) {
            // iadeTalep.setBorcMiktari(odenenBorcMiktari); // Eğer gerekliyse set et
        }

        iadeTalepRepository.save(iadeTalep);

        response.setMessage("İade talebi başarıyla oluşturuldu.");
        return response;
    }
}
