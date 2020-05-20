package es.jdl.calibreexport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.jdl.calibreexport.domain.Book;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalibreDbExporter {

    public static final String USAGE_HELP = "Usage: java -jar calibreexport.jar -db:[PATH] -out:[PATH]";
    private static boolean covers = false;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 2009-12-31 23:00:00+00:00 2017-05-21 18:01:20.440000+00:00

    private String outputPath = null;
    private String dbPath = null;

    public CalibreDbExporter(String dbPath, String outputPath) {
        this.dbPath = dbPath;
        this.outputPath = outputPath;
    }

    public static void main(String[] args) throws Exception {
        String outputPath = null;
        String dbPath = null;
        if (args == null)
            throw new RuntimeException(USAGE_HELP);
        // parse params:
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-out:"))
                outputPath = args[i].substring("-out:".length());
            if (args[i].startsWith("-db:"))
                dbPath = args[i].substring("-db:".length());
            if (args[i].startsWith("-covers:"))
                covers = convertBool(args[i].substring("-covers:".length()));
        }
        if (dbPath == null)
            throw new RuntimeException(USAGE_HELP);
        if (outputPath == null)
            outputPath = "./out";
        File fOut = new File(outputPath);
        if (!(fOut.exists() && fOut.isDirectory()))
            throw new RuntimeException(fOut + " doesn't exists or it is not a directory");

        List<Book> lst = new CalibreDbExporter(dbPath, outputPath).getBooksFromDb();
        int count = 0;
        if (covers) {
            File base = new File(dbPath).getParentFile();
            File outImagesDir = new File(fOut, "covers");
            outImagesDir.mkdir();
            for (Book b: lst) {
                if (b.getHasCover() != null && b.getHasCover().booleanValue()) {
                    File fCover = new File(new File(base, b.getPath()), "cover.jpg");
                    File imgOut = new File(outImagesDir, b.getUuid() + ".jpg");
                    //scaleImage(fCover, imgOut);
                    Files.copy(fCover.toPath(), imgOut.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    b.setCoverRelPath("covers/" + b.getUuid() + ".jpg");
                    count++;
                }
            } // end for
        }
        System.out.println("Read " + lst.size() + " books from database. Copied " + count + " images");
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").setPrettyPrinting().create();
        FileWriter fw = new FileWriter(new File(fOut, "books.json"));
        gson.toJson(lst, fw);
        fw.close();
    }

    private static void scaleImage(File fIn, File fOut) throws IOException {
        int maxSize = 50;
        BufferedImage coverImage = ImageIO.read(fIn);
        int newHeight = Math.min(coverImage.getHeight(), maxSize);
        int newWidth = (int) Math.round(coverImage.getWidth() * ((double)newHeight / (double)coverImage.getHeight()));
        System.out.println("Scaling: " + coverImage.getHeight() + "x" + coverImage.getHeight() + " -> " + newHeight + "x" + newWidth);
        Image newImage = coverImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        BufferedImage bImgOut = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        ImageIO.write(bImgOut, "jpg", fOut);

    }

    private static boolean convertBool(String boolValue) {
        return  ("true".equalsIgnoreCase(boolValue) || "yes".equalsIgnoreCase(boolValue));
    }

    private List<Book> getBooksFromDb() throws SQLException, ParseException {
        ArrayList<Book> books = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            ResultSet rs = conn.createStatement().executeQuery("select * from books order by author_sort");
            while (rs.next()) {
                Book book = buildBook(rs);
                books.add(book);
            }
        } finally {
            if (conn != null)
                conn.close();
        }

        return books;
    }

    private Book buildBook(ResultSet rs) throws SQLException, ParseException {
        Book b = new Book();
        b.setId(rs.getInt("id"));
        b.setTitle(rs.getString("title"));
        b.setSort(rs.getString("sort"));
        b.setTimestamp(parseDate(rs.getString("timestamp")));
        b.setPubdate(parseDate(rs.getString("pubdate")));
        b.setSeriesIndex(rs.getBigDecimal("series_index"));
        b.setAuthorSort(rs.getString("author_sort"));
        b.setIsbn(rs.getString("isbn"));
        b.setIccn(rs.getString("lccn"));
        b.setPath(rs.getString("path"));
        b.setFlags(rs.getBigDecimal("flags"));
        b.setUuid(rs.getString("uuid"));
        b.setHasCover(rs.getInt("has_cover") == 1);
        b.setLastModified(parseDate(rs.getString("last_modified")));
        return b;
    }

    private Date parseDate(String dateStr) throws ParseException {
        return df.parse(dateStr);
    }

}
