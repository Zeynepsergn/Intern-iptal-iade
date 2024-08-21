package tr.gov.gib.iade.service;

import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.iade.object.request.IadeTalepRequest;

public interface IadeTalepService {

    GibResponse iadeTalebiOnayla(IadeTalepRequest iadeTalepRequest);
}
