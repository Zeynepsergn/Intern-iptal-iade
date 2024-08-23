package tr.gov.gib.iade.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.util.ServiceMessage;
import tr.gov.gib.iade.entity.IadeTalep;
import tr.gov.gib.iade.entity.Odeme;
import tr.gov.gib.iade.object.request.IadeTalepRequest;
import tr.gov.gib.iade.object.response.IadeTalepResponse;
import tr.gov.gib.iade.repository.IadeTalepRepository;
import tr.gov.gib.iade.service.IadeTalepService;

import java.time.Duration;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Service("IadeTalepService")
@Transactional
public class IadeTalepServiceImpl implements IadeTalepService {

    private final IadeTalepRepository iadeTalepRepository;

    public IadeTalepServiceImpl(IadeTalepRepository iadeTalepRepository) {
        this.iadeTalepRepository = iadeTalepRepository;
    }

    @Override
    public GibResponse iadeTalebiOnayla(IadeTalepRequest iadeTalepRequest) {
        IadeTalep iadeTalep = iadeTalepRepository.findIadeTalepById(iadeTalepRequest.getId());
        if (iadeTalep == null)
            return GibResponse.builder().status(false).message("İade talebi bulunamadı!").build();

        if(!iadeTalep.getIadeTalepDurum().equals((short) 0)) {
            return GibResponse.builder().status(false).message("Kayıt iade edilebilir durumda değil").build();
        }

        Odeme odeme = iadeTalep.getOdemeId();
        if (odeme == null) {
            return GibResponse.builder().status(false).message("Ödeme bilgisi bulunamadı!").build();
        }

        OffsetDateTime odemeOptime = odeme.getOptime();
        OffsetDateTime talepOptime = iadeTalep.getOptime();
        boolean iadeIcinUygun = false;

        long gunFarki = Duration.between(odemeOptime, talepOptime).toDays();

        // iadeTalepTur ve odemeTur kontrolleri
        String iadeTalepTur = iadeTalep.getIadeTalepTur().toString();
        String odemeTur = odeme.getVergiId().toString();

        switch (iadeTalepTur) {
            case "0": // Dilekçe ile sadece nakit ödemeler için
                if (odemeTur.equals("2")) { // Nakit
                    iadeIcinUygun = true;
                }
                break;
            case "1": // İnternet ile sanal ve fiziksel ödemeler için
                if (odemeTur.equals("0") && gunFarki <= 2) { // SanalPos
                    iadeIcinUygun = true;
                } else if (odemeTur.equals("1") && gunFarki == 0 && talepOptime.toLocalTime().isBefore(LocalTime.of(17, 0))) { // FizikselPos
                    iadeIcinUygun = true;
                }
                break;
            case "2": // Sistem hatası durumunda yapılacak ödemeler için
                iadeIcinUygun = true; // Tüm ödeme türlerinde geçerli
                break;
            default:
                return GibResponse.builder().status(false).message("Bilinmeyen iade talep türü.").build();
        }

        if (!iadeIcinUygun) {
            return GibResponse.builder().status(false).message("İade süresi dolmuş veya uygun değil.").build();
        }

        iadeTalep.setIadeTalepDurum((short) 1);
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
