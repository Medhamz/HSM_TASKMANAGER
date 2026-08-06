package com.hsm.taskmanager.service;

import com.hsm.taskmanager.entity.TestClass;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Exporte la liste des classes de test au format CSV et retourne les données en byte[].
     */
    public byte[] exportCsv(List<TestClass> tests) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(baos, true, StandardCharsets.UTF_8)) {
            // En-tête CSV
            writer.println("Name,Project,Type,Status,StartDate,EstimatedHours,CompletionDate,ActualHours");
            for (TestClass t : tests) {
                String line = String.join(",",
                        escapeCsv(t.getName()),
                        escapeCsv(t.getProject().getName()),
                        t.getType().name(),
                        t.getStatus().name(),
                        t.getStartDate().format(DATE_FORMATTER),
                        String.valueOf(t.getEstimatedHours()),
                        t.getCompletionDate() != null ? t.getCompletionDate().format(DATE_FORMATTER) : "",
                        t.getActualHours() != null ? String.valueOf(t.getActualHours()) : ""
                );
                writer.println(line);
            }
            writer.flush();
        }
        return baos.toByteArray();
    }

    /**
     * Exporte la liste des classes de test au format PDF.
     * À implémenter avec une bibliothèque comme iText ou OpenPDF.
     */
    public byte[] exportPdf(List<TestClass> tests) throws IOException {
        // Exemple simple : pour l'instant, on retourne un tableau vide ou on lève une exception
        // À remplacer par une vraie génération PDF
        throw new UnsupportedOperationException("PDF export not yet implemented. Use CSV instead.");
        // Pour une implémentation future, vous pouvez utiliser iText :
        // ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Document document = new Document();
        // PdfWriter.getInstance(document, baos);
        // document.open();
        // ... ajouter contenu ...
        // document.close();
        // return baos.toByteArray();
    }

    /**
     * Échappe une valeur pour le format CSV (gère les guillemets, virgules, sauts de ligne).
     */
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}