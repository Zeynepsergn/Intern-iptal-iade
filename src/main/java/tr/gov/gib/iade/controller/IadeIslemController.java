package tr.gov.gib.iade.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.reuest.GibRequest;
import tr.gov.gib.iade.entity.MukellefKullanici;
import tr.gov.gib.iade.entity.Odeme;
import tr.gov.gib.iade.entity.OdemeDetay;
import tr.gov.gib.iade.object.request.IadeIslemRequest;
import tr.gov.gib.iade.object.request.IadeTalepRequest;
import tr.gov.gib.iade.object.response.IadeIslemResponse;
import tr.gov.gib.iade.service.IadeIslemService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/iade")
public class IadeIslemController {

    private final IadeIslemService iadeIslemService;

    public IadeIslemController(IadeIslemService iadeIslemService) {
        this.iadeIslemService = iadeIslemService;
    }


    @PostMapping("/iadeEdilebilecekOdemeSorgula")
    public ResponseEntity<GibResponse> iadeEdilebilecekOdemeSorgula(@RequestBody GibRequest<IadeIslemRequest> request) {
        GibResponse response = new GibResponse();

        if (request == null || request.getData() == null) {
            response.setStatus(false);
            response.setMessage("Request veya veriler null.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        IadeIslemRequest requestData = request.getData();

        try {
            if (requestData.getOdemeId() != null) {
                // OdemeId ile ödemeyi sorgula
                BigDecimal odenenBorcMiktari = iadeIslemService.getOdenenBorcMiktariByOdemeId(requestData.getOdemeId());

                if (odenenBorcMiktari != null) {
                    response.setStatus(true);
                    response.setData(odenenBorcMiktari);
                } else {
                    response.setStatus(false);
                    response.setMessage("Ödeme bulunamadı.");
                }
            } else if (requestData.getTckn() != null) {
                // Tckn ile mükellefi ve ödemeyi sorgula
                MukellefKullanici mukellef = iadeIslemService.getMukellefByTckn(requestData.getTckn());

                if (mukellef != null) {
                    List<Odeme> odemeler = iadeIslemService.getOdemeByMukellefId(mukellef.getId());
                    List<OdemeDetay> odemeDetaylari = iadeIslemService.getOdemeDetayByOdemeList(odemeler);

                    response.setStatus(true);
                    response.setData(odemeDetaylari);
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

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @PostMapping("/iadeTalebiOlustur")
    public ResponseEntity<GibResponse> iadeTalebiOlustur(@RequestBody GibRequest<IadeTalepRequest> request) {
        // Tckn olmayacak sadece odemeId ile OdemeDetay tablosundan odenenBorcMiktari iade talebine islenir.
        // 0 ile ilk kayıt atılır.
        return null;
    }

    @PostMapping("/iadeTalebiOnayla")
    public ResponseEntity<GibResponse> iadeTalebiOnayla(@RequestBody GibRequest<IadeTalepRequest> request) {
        // IadeTalep tablosunda durumu 0 olan kaydın durumu 1 e cekilir.
        return null;
    }


    @PostMapping("/iadeSorgula")
    public ResponseEntity<GibResponse<List<IadeIslemResponse>>> iadeTalepSorgula(@RequestBody GibRequest<IadeIslemRequest> request) {
        System.out.println("Received request: " + request);
        GibResponse<List<IadeIslemResponse>> response = iadeIslemService.iadeSorgula(request.getData());
        // Eğer veri boşsa, yanıtın formatını kontrol et
        if (response.getData() == null) {
            response.setData(Collections.emptyList());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/tumIadeSorgula")
    public ResponseEntity<GibResponse<List<IadeIslemResponse>>> tumIadeTalepSorgula(@RequestBody IadeIslemRequest request) {
        GibResponse<List<IadeIslemResponse>> response = iadeIslemService.tumIadeSorgula(request);
        if (response.getData() == null) {
            response.setData(Collections.emptyList());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}