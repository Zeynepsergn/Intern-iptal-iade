package tr.gov.gib.iade.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.request.GibRequest;
import tr.gov.gib.iade.object.request.IadeIslemRequest;
import tr.gov.gib.iade.object.request.IadeTalepRequest;
import tr.gov.gib.iade.object.response.IadeIslemResponse;
import tr.gov.gib.iade.service.IadeEdilebileceklerService;
import tr.gov.gib.iade.service.IadeSorgulamaService;
import tr.gov.gib.iade.service.IadeTalepService;
import tr.gov.gib.iade.service.impl.IadeTalepOlusturmaServiceImpl;


import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/iade")
public class IadeIslemController {


    private final IadeSorgulamaService iadeSorgulamaService;
    private final IadeTalepService iadeTalepService;
    private final IadeEdilebileceklerService iadeEdilebileceklerService;
    private final IadeTalepOlusturmaServiceImpl iadeTalepOlusturmaServiceImpl;

    public IadeIslemController(IadeSorgulamaService iadeSorgulamaService, IadeTalepService iadeTalepService, IadeEdilebileceklerService iadeEdilebileceklerService, IadeTalepOlusturmaServiceImpl iadeTalepOlusturmaServiceImpl) {
        this.iadeSorgulamaService = iadeSorgulamaService;
        this.iadeTalepService = iadeTalepService;
        this.iadeEdilebileceklerService = iadeEdilebileceklerService;
        this.iadeTalepOlusturmaServiceImpl = iadeTalepOlusturmaServiceImpl;
    }


    @PostMapping("/iadeEdilebilecekOdemeSorgula")
    public ResponseEntity<GibResponse> iadeEdilebilecekOdemeSorgula(@RequestBody GibRequest<IadeIslemRequest> request) {
        GibResponse response = iadeEdilebileceklerService.getMukellefByTckn(request);
        return new ResponseEntity<>(response, response.getStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/iadeTalebiOlustur")
    public ResponseEntity<GibResponse> iadeTalebiOlustur (@RequestBody GibRequest<IadeTalepRequest> request) {
        IadeTalepRequest requestData = request.getData();
        GibResponse response = iadeTalepOlusturmaServiceImpl.iadeTalebiOlustur(requestData);

        if ("Ödeme detayı bulunamadı.".equals(response.getMessage())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }


    @PostMapping("/iadeTalebiOnayla")
    public ResponseEntity<GibResponse> iadeTalebiOnayla(@RequestBody GibRequest<IadeTalepRequest> request) {
        IadeTalepRequest iadeTalepRequest = request.getData();
        GibResponse response = iadeTalepService.iadeTalebiOnayla(iadeTalepRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/iadeSorgula")
    public ResponseEntity<GibResponse<List<IadeIslemResponse>>> iadeTalepSorgula(@RequestBody GibRequest<IadeIslemRequest> request) {
        System.out.println("Received request: " + request);
        GibResponse<List<IadeIslemResponse>> response = iadeSorgulamaService.iadeSorgula(request.getData());
        // Eğer veri boşsa, yanıtın formatını kontrol et
        if (response.getData() == null) {
            response.setData(Collections.emptyList());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/tumIadeSorgula")
    public ResponseEntity<GibResponse<List<IadeIslemResponse>>> tumIadeTalepSorgula(@RequestBody IadeIslemRequest request) {
        GibResponse<List<IadeIslemResponse>> response = iadeSorgulamaService.tumIadeSorgula(request);
        if (response.getData() == null) {
            response.setData(Collections.emptyList());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}