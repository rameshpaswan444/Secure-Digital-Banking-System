package com.banking.Controller;

import com.banking.Service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final PdfService pdfService;

    @GetMapping("/receipt/{transactionId}")
    public ResponseEntity<InputStreamResource> downloadReceipt(
            @PathVariable Long transactionId
    ) {

        InputStreamResource file =
                new InputStreamResource(
                        pdfService.generateReceipt(transactionId)
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=receipt.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }
}
