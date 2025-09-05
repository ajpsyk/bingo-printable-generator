package org.bingo.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

import org.bingo.config.AssetPaths;
import org.bingo.config.DocumentConfig;
import org.bingo.config.PageConfig;
import org.bingo.model.*;

public class DocumentBuilder {

    public static void buildOnePerPageBingoCards (
            DocumentConfig docConfig,
            PageConfig pageConfig
    ) throws FileNotFoundException {

        PdfDocument doc;
        Path outputPath = docConfig.getOutputPath();
        try {
            doc = new PdfDocument(new PdfWriter(outputPath.toString()));
        } catch (IOException e) {
            throw new FileNotFoundException("Output path is invalid or not writable: " + outputPath);
        }

        PageLayout pl = getPageDimensions(docConfig);
        AssetPaths assets = docConfig.getAssets();

        int cardAmount = pageConfig.getCopies();

        Frame frame = ObjectBuilder.getFrame(doc, pl, assets.getFramePath());
        Header header =  ObjectBuilder.getHeader(doc, pl, assets.getHeaderPath(), pageConfig);
        Grid grid = ObjectBuilder.getGrid(doc, pageConfig, pl, header.getHeight(), false);

        PdfFormXObject freeSpace = ObjectBuilder.loadImage(assets.getFreeSpacePath(), doc);

        Map<String, GridContent> bingoSquares = ObjectBuilder.loadImageDirectory(
                assets.getIconsPath(), doc, grid, createFont(assets.getFontPath()), docConfig.getFontColor()
        );
        List<List<String>> permutations = Shuffler.getUniquePermutations(
                new ArrayList<>(bingoSquares.keySet()), cardAmount, docConfig.getSeed()
        );

        IntStream.range(0, cardAmount).forEach(i -> {
            PdfPage page = doc.addNewPage(docConfig.getPageSize());
            PdfCanvas canvas = new PdfCanvas(page);

            addToDocument(canvas, frame.getObject(), frame.getTransform());
            addToDocument(canvas, header.getObject(), header.getTransform());
            addToDocument(canvas, grid.getObject(), grid.getTransform());

            List<String> permutation = permutations.get(i);
            ObjectBuilder.addImagesAndLabelsToGrid(
                    pageConfig, grid, permutation, bingoSquares, freeSpace, canvas
            );
        });


        doc.close();
    }

    public static void buildTwoPerPageBingoCards (
            DocumentConfig docConfig, PageConfig pageConfig
    ) throws IOException {
        PdfDocument doc = new PdfDocument(new PdfWriter(docConfig.getOutputPath().toString()));
        PageLayout pl = getPageDimensions(docConfig);
        float xOffset = pl.getMarginLeft() + pl.getMarginRight() + pl.getPrintSafeWidth();
        AssetPaths assets = docConfig.getAssets();

        int cardAmount = pageConfig.getCopies();

        Frame frame = ObjectBuilder.getFrame(doc, pl, assets.getFramePath());
        Frame offsetFrame = frame.toBuilder()
                .transform(frame.getTransform().toBuilder()
                        .positionX(frame.getTransform().getPositionX() + xOffset)
                        .build())
                .build();;

        Header header =  ObjectBuilder.getHeader(doc, pl,  assets.getHeaderPath(), pageConfig);
        Header offsetHeader = header.toBuilder()
                .transform(header.getTransform().toBuilder()
                        .positionX(header.getTransform().getPositionX() + xOffset)
                        .build())
                .build();

        PdfFormXObject freeSpace = ObjectBuilder.loadImage(assets.getFreeSpacePath(), doc);

        Grid grid = ObjectBuilder.getGrid(doc, pageConfig, pl, header.getHeight(), false);
        Grid offsetGrid = grid.toBuilder()
                .transform(grid.getTransform().toBuilder()
                        .positionX(grid.getTransform().getPositionX() + xOffset)
                        .build())
                .build();

        Map<String, GridContent> bingoSquares = ObjectBuilder.loadImageDirectory(
                assets.getIconsPath(), doc, grid, createFont(assets.getFontPath()), docConfig.getFontColor()
        );
        List<List<String>> permutations = Shuffler.getUniquePermutations(
                new ArrayList<>(bingoSquares.keySet()), cardAmount, docConfig.getSeed()
        );
        IntStream.range(0, cardAmount / 2).forEach(i -> {
            PdfPage page = doc.addNewPage(PageSize.LETTER.rotate());
            PdfCanvas canvas = new PdfCanvas(page);

            addToDocument(canvas, frame.getObject(), frame.getTransform());
            addToDocument(canvas, offsetFrame.getObject(), offsetFrame.getTransform());

            addToDocument(canvas, header.getObject(), header.getTransform());
            addToDocument(canvas, offsetHeader.getObject(), offsetHeader.getTransform());

            addToDocument(canvas, grid.getObject(), grid.getTransform());
            addToDocument(canvas, offsetGrid.getObject(), offsetGrid.getTransform());


            List<String> leftPermutation = permutations.get(i * 2);
            ObjectBuilder.addImagesAndLabelsToGrid(
                    pageConfig, grid, leftPermutation, bingoSquares, freeSpace, canvas
            );

            List<String> rightPermutation = permutations.get(i * 2 + 1);
            ObjectBuilder.addImagesAndLabelsToGrid(
                    pageConfig, offsetGrid, rightPermutation, bingoSquares, freeSpace, canvas
            );

        });

        doc.close();
    }

    public static void buildInstructionsTokensCallingCards (
            DocumentConfig docConfig,
            PageConfig tokenConfig,
            PageConfig ccConfig,
            PageConfig multiCallingCardsConfig,
            PageConfig multiCallingLastCardsConfig
    ) throws IOException {

        AssetPaths assets = docConfig.getAssets();
        PdfDocument doc = new PdfDocument(
                new PdfReader(assets.getInstructionsPath().toString()),
                new PdfWriter(docConfig.getOutputPath().toString())
        );
        PageLayout pl = getPageDimensions(docConfig);

        // Tokens
        PdfPage page = doc.addNewPage(docConfig.getPageSize());
        PdfCanvas canvas = new PdfCanvas(page);

        Header scissors = ObjectBuilder.getHeader(doc, pl, assets.getScissorsIconPath(), tokenConfig);
        Grid grid = ObjectBuilder.getGrid(doc, tokenConfig, pl, scissors.getHeight(), true);
        PdfFormXObject token = ObjectBuilder.loadImage(assets.getTokenPath(), doc);

        addToDocument(canvas, scissors.getObject(), scissors.getTransform());
        addToDocument(canvas, grid.getObject(), grid.getTransform());
        ObjectBuilder.addTokensToGrid(tokenConfig, grid, token, canvas);


        // Calling Cards Single
        PdfPage page2 = doc.addNewPage(docConfig.getPageSize());
        PdfCanvas canvas2 = new PdfCanvas(page2);

        Header callingCardsHeader = ObjectBuilder.getHeader(doc, pl, assets.getCallingCardsHeaderPath(), ccConfig);
        Grid ccGrid = ObjectBuilder.getGrid(
                doc, ccConfig, pl, callingCardsHeader.getHeight(), false
        );
        Map<String, GridContent> ccSquares = ObjectBuilder.loadImageDirectory(
                assets.getIconsPath(), doc, ccGrid, createFont(assets.getFontPath()), docConfig.getFontColor()
        );
        List<String> labelsInOrder = new ArrayList<>(ccSquares.keySet());
        labelsInOrder.sort(Comparator.naturalOrder());

        addToDocument(canvas2, callingCardsHeader.getObject(), callingCardsHeader.getTransform());
        addToDocument(canvas2, ccGrid.getObject(), ccGrid.getTransform());
        ObjectBuilder.addImagesAndLabelsToGrid(ccConfig, ccGrid, labelsInOrder, ccSquares, null, canvas2);

        // Calling Cards Multi
        Grid multiGrid = ObjectBuilder.getGrid(
                doc, multiCallingCardsConfig, pl, scissors.getHeight(), true
        );
        Map<String, GridContent> multiSquares = ObjectBuilder.loadImageDirectory(
                assets.getIconsPath(), doc, multiGrid, createFont(assets.getFontPath()), docConfig.getFontColor()
        );
        for (int i = 0; i < 7; i++) {
            PdfPage page3 = doc.addNewPage(docConfig.getPageSize());
            PdfCanvas canvas3 = new PdfCanvas(page3);

            addToDocument(canvas3, scissors.getObject(), scissors.getTransform());
            addToDocument(canvas3, multiGrid.getObject(), multiGrid.getTransform());
            ObjectBuilder.addImagesAndLabelsToGrid(
                    multiCallingCardsConfig,
                    multiGrid,
                    labelsInOrder.subList(i * 4, i * 4 + 4),
                    multiSquares,
                    null,
                    canvas3
            );
        }

        PdfPage page4 = doc.addNewPage(docConfig.getPageSize());
        PdfCanvas canvas4 = new PdfCanvas(page4);
        Grid multiGridLastPage =  ObjectBuilder.getGrid(
                doc, multiCallingLastCardsConfig, pl, scissors.getHeight(), true
        );

        addToDocument(canvas4, scissors.getObject(), scissors.getTransform());
        addToDocument(canvas4, multiGridLastPage.getObject(), multiGridLastPage.getTransform());
        ObjectBuilder.addImagesAndLabelsToGrid(
                multiCallingLastCardsConfig,
                multiGridLastPage,
                labelsInOrder.subList(28, 30),
                multiSquares,
                null,
                canvas4
        );

        doc.close();
    }

    public static PageLayout getPageDimensions(
            DocumentConfig docConfig
    ) {
        float pageWidth = docConfig.getPageSize().getWidth();
        float pageHeight = docConfig.getPageSize().getHeight();

        float marginTop = Conversions.inchesToPoints(docConfig.getMarginTopInches());
        float marginRight = Conversions.inchesToPoints(docConfig.getMarginRightInches());
        float marginBottom = Conversions.inchesToPoints(docConfig.getMarginBottomInches());
        float marginLeft = Conversions.inchesToPoints(docConfig.getMarginLeftInches());

        float printSafeWidth = pageWidth - marginRight - marginLeft;
        if (pageWidth > pageHeight) { printSafeWidth = (pageWidth - 2 * marginRight - 2 * marginLeft) / 2f; } // landscape
        float printSafeHeight = pageHeight - marginTop - marginBottom;

        return PageLayout.builder()
                .width(pageWidth)
                .height(pageHeight)
                .printSafeWidth(printSafeWidth)
                .printSafeHeight(printSafeHeight)
                .marginTop(marginTop)
                .marginRight(marginRight)
                .marginBottom(marginBottom)
                .marginLeft(marginLeft)
                .build();
    }


    public static void addToDocument(
            PdfCanvas canvas, PdfFormXObject object, Transform transform
    ) {
        canvas.addXObjectWithTransformationMatrix(
                object,
                transform.getScaleX(),
                transform.getSkewY(), transform.getSkewX(),
                transform.getScaleY(),
                transform.getPositionX(), transform.getPositionY()
        );
    }

    public static PdfFont createFont(Path fontPath) throws FileNotFoundException {
        try {
            return PdfFontFactory.createFont(
                    fontPath.toString(), PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED
            );
        } catch (IOException e) {
            throw new FileNotFoundException("Failed to load font: " + fontPath);
        }
    }
}
