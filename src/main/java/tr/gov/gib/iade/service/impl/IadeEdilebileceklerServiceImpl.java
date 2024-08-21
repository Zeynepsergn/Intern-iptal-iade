package tr.gov.gib.iade.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.reuest.GibRequest;
import tr.gov.gib.iade.entity.MukellefKullanici;
import tr.gov.gib.iade.entity.Odeme;
import tr.gov.gib.iade.entity.OdemeDetay;
import tr.gov.gib.iade.object.request.IadeIslemRequest;
import tr.gov.gib.iade.repository.OdemeDetayRepository;
import tr.gov.gib.iade.service.IadeEdilebileceklerService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("IadeEdilebileceklerService")
public class IadeEdilebileceklerServiceImpl implements IadeEdilebileceklerService {


    private static final Logger LOGGER = LoggerFactory.getLogger(IadeEdilebileceklerServiceImpl.class);

    private final OdemeDetayRepository odemeDetayRepository;


    @Autowired
    public IadeEdilebileceklerServiceImpl(OdemeDetayRepository odemeDetayRepository) {
        this.odemeDetayRepository = odemeDetayRepository;
    }

    @Override
    public GibResponse getMukellefByTcknn(GibRequest<IadeIslemRequest> request) {
        GibResponse response = new GibResponse();

        if (request == null || request.getData() == null) {
            response.setStatus(false);
            response.setMessage("Request veya veriler null.");
            return response;
        }

        IadeIslemRequest requestData = request.getData();

        try {
            if (requestData.getId() != null) {
                // OdemeId ile ödemeyi sorgula
                BigDecimal odenenBorcMiktari = getOdenenBorcMiktariByOdemeId(requestData.getId());

                if (odenenBorcMiktari != null) {
                    response.setStatus(true);
                    response.setData(odenenBorcMiktari);
                    response.setMessage("Başarılı"); // Başarı durumunda mesaj ayarla
                } else {
                    response.setStatus(false);
                    response.setMessage("Ödeme bulunamadı.");
                }
            } else if (requestData.getTckn() != null && !requestData.getTckn().isEmpty()) {
                // Tckn ile mükellefi ve ödemeyi sorgula
                MukellefKullanici mukellef = getMukellefByTckn(requestData.getTckn());

                if (mukellef != null) {
                    List<Odeme> odemeler = getOdemeByMukellefId(mukellef.getId());
                    List<OdemeDetay> odemeDetaylari = getOdemeDetayByOdemeList(odemeler);

                    response.setStatus(true);
                    response.setData(odemeDetaylari);
                    response.setMessage("Başarılı"); // Başarı durumunda mesaj ayarla
                } else {
                    response.setStatus(false);
                    response.setMessage("Mükellef bulunamadı.");
                }
            } else {
                response.setStatus(false);
                response.setMessage("Geçersiz parametreler.");
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Bir hata oluştu: " + e.getMessage());
        }

        return response;
    }


    @Override
    public BigDecimal getOdenenBorcMiktariByOdemeId(Integer odemeId) {
        List<OdemeDetay> odemeDetayList = odemeDetayRepository.findByOdemeIdIn(Collections.singletonList(odemeId));

        if (odemeDetayList.isEmpty()) {
            LOGGER.warn("OdemeDetay bulunamadı, ID: {}", odemeId);
            return null;
        }

        OdemeDetay odemeDetay = odemeDetayList.get(0);
        Odeme odeme = odemeDetay.getOdemeId();

        return odeme.getMukellefBorcId().getKalanBorc();
    }

    @Override
    public MukellefKullanici getMukellefByTckn(String tckn) {
        // Gerekli sorgulama işlemleri burada yapılacak
        return getMukellefByTckn(tckn);
    }

    @Override
    public List<Odeme> getOdemeByMukellefId(Integer mukellefId) {
        // Gerekli sorgulama işlemleri burada yapılacak
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

    @Override
    public GibResponse getMukellefByTckn(IadeIslemRequest iadeIslemRequest) {
        GibRequest<IadeIslemRequest> request = new GibRequest<>();
        request.setData(iadeIslemRequest);
        return getMukellefByTcknn(request);
    }



}
