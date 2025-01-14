package tr.gov.gib.iade.service;

import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.request.GibRequest;
import tr.gov.gib.iade.entity.MukellefKullanici;
import tr.gov.gib.iade.entity.Odeme;
import tr.gov.gib.iade.entity.OdemeDetay;
import tr.gov.gib.iade.object.request.IadeIslemRequest;

import java.math.BigDecimal;
import java.util.List;

public interface IadeEdilebileceklerService {

    GibResponse getMukellefByTcknn(GibRequest<IadeIslemRequest> request);

    OdemeDetay getOdenenBorcMiktariByOdemeId(Integer odemeId);

    List<OdemeDetay> getOdemeDetayByTckn(String tckn);

    List<Odeme> getOdemeByMukellefId(Integer mukellefId);

    List<OdemeDetay> getOdemeDetayByOdemeList(List<Odeme> odemeler);

    GibResponse getMukellefByTckn(GibRequest<IadeIslemRequest> iadeIslemRequest);



}
