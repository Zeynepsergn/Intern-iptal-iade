package tr.gov.gib.iade.service;

import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.iade.entity.MukellefKullanici;
import tr.gov.gib.iade.entity.Odeme;
import tr.gov.gib.iade.entity.OdemeDetay;
import tr.gov.gib.iade.object.request.IadeIslemRequest;
import tr.gov.gib.iade.object.response.IadeIslemResponse;

import java.math.BigDecimal;
import java.util.List;

public interface IadeIslemService {
    void processIade(Long iadeTalepId);

    GibResponse<List<IadeIslemResponse>> iadeSorgula(IadeIslemRequest request);

    GibResponse<List<IadeIslemResponse>> tumIadeSorgula(IadeIslemRequest request);
    // Yeni metodlar
    BigDecimal getOdenenBorcMiktariByOdemeId(Integer odemeId);

    MukellefKullanici getMukellefByTckn(String tckn);

    List<Odeme> getOdemeByMukellefId(Integer mukellefId);

    List<OdemeDetay> getOdemeDetayByOdemeList(List<Odeme> odemeler);


}
