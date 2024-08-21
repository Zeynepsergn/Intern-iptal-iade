package tr.gov.gib.iade.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.iade.entity.IadeTalep;
import tr.gov.gib.iade.entity.OdemeDetay;
import tr.gov.gib.iade.object.request.IadeTalepRequest;
import tr.gov.gib.iade.repository.IadeTalepRepository;
import tr.gov.gib.iade.repository.OdemeDetayRepository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service("IadeTalepOlusturmaService")
public class IadeTalepOlusturmaService {
    @Autowired
    private OdemeDetayRepository odemeDetayRepository;

    @Autowired
    private IadeTalepRepository iadeTalepRepository;

    public GibResponse iadeTalebiOlustur(IadeTalepRequest requestData) {
        GibResponse response = new GibResponse();

        Integer odemeId = requestData.getId();
        String iadeAciklama = requestData.getIadeAciklama();

        // Ödeme detayını OdemeDetay tablosundan bul
        OdemeDetay odemeDetay = odemeDetayRepository.findByOdemeId(odemeId);

        if (odemeDetay == null) {
            // Eğer ödeme detayı bulunamazsa NOT_FOUND yanıtı dön
            response.setMessage("Ödeme detayı bulunamadı.");
            return response;
        }

        // İade talebini oluştur
        IadeTalep iadeTalep = new IadeTalep();
        iadeTalep.setOdemeId(odemeDetay.getOdemeId());
        iadeTalep.setIadeTalepDurum((short) 0); // Varsayılan değer: ilk kayıt için 0
        iadeTalep.setIadeAciklama(iadeAciklama);
        iadeTalep.setOptime(OffsetDateTime.now());
        iadeTalep.setMukellefKullaniciId(odemeDetay.getMukellefKullaniciId());
        iadeTalep.setIadeTalepTur(requestData.getIadeTalepTur()); // Talep türünü isteğe göre ayarla

        // Ödeme detayından alınan borç miktarını iade talebine işleyin (eğer gerekliyse)
        BigDecimal odenenBorcMiktari = odemeDetay.getOdenenBorcMiktari();
        // iadeTalep.setBorcMiktari(odenenBorcMiktari); // Eğer gerekli ise borç miktarını set et

        iadeTalepRepository.save(iadeTalep);

        response.setMessage("İade talebi başarıyla oluşturuldu.");
        return response;
    }

}
