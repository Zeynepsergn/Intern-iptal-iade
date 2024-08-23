package tr.gov.gib.iade.service.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.gov.gib.gibcore.exception.GibException;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.request.GibRequest;
import tr.gov.gib.gibcore.util.ServiceMessage;
import tr.gov.gib.iade.entity.MukellefBorc;
import tr.gov.gib.iade.entity.MukellefKullanici;
import tr.gov.gib.iade.entity.Odeme;
import tr.gov.gib.iade.entity.OdemeDetay;
import tr.gov.gib.iade.object.request.IadeIslemRequest;
import tr.gov.gib.iade.object.response.IadeSorgulaResponse;
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

        if (request == null || request.getData() == null) {
            return GibResponse.builder().status(false).message("Request boş olamaz!").build();
        }

        IadeIslemRequest requestData = request.getData();

        try {

            if (requestData.getId() != null) {
                OdemeDetay odemeDetay = getOdenenBorcMiktariByOdemeId(requestData.getId());
                if(odemeDetay == null)
                    return GibResponse.builder().status(false).message("Ödeme bulunamadı.").build();

                Odeme odeme = odemeDetay.getOdemeId();
                MukellefBorc borc = odeme.getMukellefBorcId();
                MukellefKullanici mukellef = odemeDetay.getMukellefKullaniciId();

                IadeSorgulaResponse sorgulaRes = IadeSorgulaResponse
                        .builder()
                        .borcId(borc.getId())
                        .odemeId(odeme.getId())
                        .ad(mukellef.getMukellefAd())
                        .soyad(mukellef.getMukellefSoyad())
                        .tckn(mukellef.getTckn())
                        .unvan(mukellef.getMukellefUnvan())
                        .oid(odemeDetay.getOid())
                        .tutar(odemeDetay.getOdenenBorcMiktari())
                        .build();

                return GibResponse
                        .builder()
                        .service(ServiceMessage.OK)
                        .data(Collections.singletonList(sorgulaRes))
                        .build();

            }
            else if (!ObjectUtils.isEmpty(requestData.getTckn())) {
                // Tckn ile mükellefi ve ödemeyi sorgula
                List<OdemeDetay> odemeDetays = getOdemeDetayByTckn(requestData.getTckn());
                if(odemeDetays.isEmpty())
                    return GibResponse.builder().status(false).message("Ödeme bulunamadı.").build();

                List<IadeSorgulaResponse> sorgulaResList = new ArrayList<>();
                for(OdemeDetay odemeDetay : odemeDetays){
                    Odeme odeme = odemeDetay.getOdemeId();
                    MukellefBorc borc = odemeDetay.getOdemeId().getMukellefBorcId();
                    MukellefKullanici mukellef = odemeDetay.getMukellefKullaniciId();

                    IadeSorgulaResponse sorgulaRes = IadeSorgulaResponse
                            .builder()
                            .borcId(borc.getId())
                            .odemeId(odeme.getId())
                            .ad(mukellef.getMukellefAd())
                            .soyad(mukellef.getMukellefSoyad())
                            .tckn(mukellef.getTckn())
                            .unvan(mukellef.getMukellefUnvan())
                            .oid(odemeDetay.getOid())
                            .tutar(odemeDetay.getOdenenBorcMiktari())
                            .build();

                    sorgulaResList.add(sorgulaRes);
                }

                return GibResponse
                        .builder()
                        .service(ServiceMessage.OK)
                        .data(sorgulaResList)
                        .build();
            }
            else {
                return GibResponse.builder().status(false).message("Geçersiz parametreler.").build();
            }
        } catch (Exception e) {
            throw new GibException(ServiceMessage.FAIL, "Odeme bilgileri getirilirken hata oluştu.");
        }
    }


    @Override
    public OdemeDetay getOdenenBorcMiktariByOdemeId(Integer odemeId) {
        OdemeDetay odemeDetay = odemeDetayRepository.findByOdemeId(odemeId);

        if (odemeDetay == null) {
            LOGGER.warn("OdemeDetay bulunamadı, ID: {}", odemeId);
            return null;
        }
        return odemeDetay;
    }

    @Override
    public List<OdemeDetay> getOdemeDetayByTckn(String tckn) {
        // Gerekli sorgulama işlemleri burada yapılacak
        //return getMukellefByTckn(tckn);
        return odemeDetayRepository.findOdemeDetaysByTckn(tckn);
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
    public GibResponse getMukellefByTckn(GibRequest<IadeIslemRequest> iadeIslemRequest) {
        return getMukellefByTcknn(iadeIslemRequest);
    }



}
