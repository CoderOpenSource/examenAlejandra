package com.example.myapplication.rutina.business.repository;

import android.content.Context;

import com.example.myapplication.rutina.data.models.Ejercicio;
import com.example.myapplication.rutina.data.models.RutinaEjercicio;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class GeneradorPDFRutina {

    public static String crearPDF(int idRutina, Context context) throws FileNotFoundException {
        // 1. Crear directorio donde se almacenará el PDF
        File directorio = new File(context.getExternalFilesDir(null), "RutinasPDFs");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // 2. Nombre del archivo PDF
        String nombreArchivo = "Rutina_" + idRutina + ".pdf";
        File archivoPDF = new File(directorio, nombreArchivo);

        // 3. Crear documento PDF
        PdfWriter writer = new PdfWriter(archivoPDF.getPath());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 4. Añadir el título
        document.add(new Paragraph("RUTINA PARA EL DÍA: " + idRutina)
                .setFontSize(18)
                .setBold()
                .setFontColor(ColorConstants.RED)
                .setTextAlignment(TextAlignment.CENTER));

        // 5. Crear tabla con columnas para ejercicios
        Table tablaEjercicios = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1})).useAllAvailableWidth();

        // 6. Obtener ejercicios de la rutina
        RutinaEjercicioRepository rutinaEjercicioRepository = new RutinaEjercicioRepository(context);
        EjercicioRepository ejercicioRepository = new EjercicioRepository(context);  // Asegúrate de tener un repositorio para los ejercicios
        List<RutinaEjercicio> ejerciciosRutina = rutinaEjercicioRepository.obtenerRutinaEjerciciosPorRutinaId(idRutina);

        if (ejerciciosRutina == null || ejerciciosRutina.isEmpty()) {
            document.add(new Paragraph("No se encontraron ejercicios para esta rutina."));
        } else {
            for (RutinaEjercicio ejercicioRutina : ejerciciosRutina) {
                // Obtener el objeto Ejercicio completo usando el id del ejercicio
                Ejercicio ejercicio = ejercicioRepository.obtenerEjercicioPorId(ejercicioRutina.getIdEjercicio());

                // Agregar datos del ejercicio al PDF
                tablaEjercicios.addCell(new Cell().add(new Paragraph("Ejercicio:").setBold()));
                tablaEjercicios.addCell(new Cell().add(new Paragraph(ejercicio.getNombre())));
                tablaEjercicios.addCell(new Cell().add(new Paragraph("Posición:").setBold()));
                tablaEjercicios.addCell(new Cell().add(new Paragraph(ejercicio.getPosicion())));
                tablaEjercicios.addCell(new Cell().add(new Paragraph("Ejecución:").setBold()));
                tablaEjercicios.addCell(new Cell().add(new Paragraph(ejercicio.getEjecucion())));
                tablaEjercicios.addCell(new Cell().add(new Paragraph("Equipo:").setBold()));
                tablaEjercicios.addCell(new Cell().add(new Paragraph(ejercicio.getEquipo())));

                // Agregar series, repeticiones y descanso
                tablaEjercicios.addCell(new Cell().add(new Paragraph("Series").setBold()));
                tablaEjercicios.addCell(new Cell().add(new Paragraph(String.valueOf(ejercicioRutina.getSeries()))));
                tablaEjercicios.addCell(new Cell().add(new Paragraph("Repeticiones").setBold()));
                tablaEjercicios.addCell(new Cell().add(new Paragraph(String.valueOf(ejercicioRutina.getRepeticiones()))));
                tablaEjercicios.addCell(new Cell().add(new Paragraph("Descanso (seg)").setBold()));
                tablaEjercicios.addCell(new Cell().add(new Paragraph(String.valueOf(ejercicioRutina.getDescanso()))));

                // Cargar imagen del ejercicio desde la URL
                if (ejercicio.getMultimedia() != null && !ejercicio.getMultimedia().isEmpty()) {
                    File imgFile = new File(ejercicio.getMultimedia()); // Asegúrate de que multimedia sea una URL local válida
                    if (imgFile.exists()) {
                        try {
                            ImageData imageData = ImageDataFactory.create(imgFile.getAbsolutePath());
                            Image image = new Image(imageData).scaleToFit(200, 200);
                            tablaEjercicios.addCell(new Cell().add(new Paragraph("Imagen del ejercicio:").setFontSize(12).setBold())
                                    .setBackgroundColor(ColorConstants.BLACK).setFontColor(ColorConstants.WHITE));
                            tablaEjercicios.addCell(new Cell().add(image)
                                    .setBackgroundColor(ColorConstants.DARK_GRAY).setFontColor(ColorConstants.WHITE));
                        } catch (Exception e) {
                            tablaEjercicios.addCell(new Cell(1, 2).add(new Paragraph("Error al cargar la imagen.")));
                        }
                    } else {
                        tablaEjercicios.addCell(new Cell(1, 2).add(new Paragraph("Imagen no encontrada.")));
                    }
                }
            }
        }

        // 7. Añadir tabla al documento
        document.add(tablaEjercicios);

        // 8. Cerrar el documento
        document.close();

        // 9. Retornar la ruta del PDF
        return archivoPDF.getPath();
    }

}
