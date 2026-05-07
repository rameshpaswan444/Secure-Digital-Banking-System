package com.banking.Service;

import com.banking.Entity.Transaction;
import com.banking.Exception.ResourceNotFoundException;
import com.banking.Repository.TransactionRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final TransactionRepository transactionRepository;

    public ByteArrayInputStream generateReceipt(Long transactionId) {

        Transaction tx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        Document document = new Document();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);

            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);

            Paragraph title = new Paragraph(
                    "BANK TRANSACTION RECEIPT",
                    font
            );

            title.setAlignment(Element.ALIGN_CENTER);

            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Transaction ID: " + tx.getId()));
            document.add(new Paragraph("Type: " + tx.getType()));
            document.add(new Paragraph("Amount: " + tx.getAmount()));
            document.add(new Paragraph("Account Number: " + tx.getAccountNumber()));
            document.add(new Paragraph("Description: " + tx.getDescription()));
            document.add(new Paragraph("Date: " + tx.getTimestamp()));

            document.close();

        } catch (Exception e) {
            throw new ResourceNotFoundException("PDF generation failed");
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
