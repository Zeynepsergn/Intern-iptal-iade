package tr.gov.gib.iade.service;

import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.iade.entity.MukellefKullanici;
import tr.gov.gib.iade.entity.Odeme;
import tr.gov.gib.iade.entity.OdemeDetay;
import tr.gov.gib.iade.object.request.IadeIslemRequest;
import tr.gov.gib.iade.object.response.IadeIslemResponse;

import java.math.BigDecimal;
import java.util.List;

public interface IadeSorgulamaService {

    GibResponse<List<IadeIslemResponse>> iadeSorgula(IadeIslemRequest request);

    GibResponse<List<IadeIslemResponse>> tumIadeSorgula(IadeIslemRequest request);


}
