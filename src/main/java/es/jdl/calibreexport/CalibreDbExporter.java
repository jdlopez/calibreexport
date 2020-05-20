package es.jdl.calibreexport;

import com.google.gson.Gson;
import es.jdl.calibreexport.domain.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CalibreDbExporter {

    public static final String USAGE_HELP = "Usage: java -jar calibreexport.jar -db:[PATH] -out:[PATH]";
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
        }
        if (dbPath == null)
            throw new RuntimeException(USAGE_HELP);
        if (outputPath == null)
            outputPath = "./out";
        System.out.println(new CalibreDbExporter(dbPath, outputPath).getJson());
    }

    private String getJson() throws SQLException, ParseException {
        Gson gson = new Gson();
        ArrayList<Book> books = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            ResultSet rs = conn.createStatement().executeQuery("select * from books order by author_sort");
            while (rs.next()) {
                Book book = buildBook(rs);
                books.add(book);
            }
            System.out.println(gson.toJson(books));
        } finally {
            if (conn != null)
                conn.close();
        }

        return gson.toJson(books);
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
