package tr.gov.gib.iade.service;

import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.iade.object.request.IadeTalepRequest;

public interface IadeTalepOlusturmaService {

    // İade talebi oluşturma metodu
    GibResponse iadeTalebiOlustur(IadeTalepRequest requestData);
}
