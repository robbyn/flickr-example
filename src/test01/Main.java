package test01;

import java.io.File;
import test01.gold.GoldenBookService;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import test01.gold.Album;
import test01.gold.PhotoInfo;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            GoldenBookService gold = GoldenBookService.connect(
                    loadProps("/setup.properties"));
            PhotoInfo photo = gold.uploadPhoto(new File("/Users/maurice/perry.ch/hikes/_site/2015-12-20/preview/_DSF2480.jpg"), "Tannay", "La taverne");
            Album album = gold.createAlbum("Album Tannay", "Album Tannay description", photo.getId());
            String lastId = null;
            for (Album a: gold.getAlbums()) {
                printAlbum(a);
                System.out.println("---");
                lastId = a.getId();
            }
            if (lastId != null) {
                System.out.println("Photos:");
                for (PhotoInfo pi: gold.getAlbumPhotos(lastId)) {
                    printPhotoInfo(pi);
                    System.out.println("---");
                }
            }
//            Album newa = gold.createAlbum("Album title", "Album title", null);
//            if (newa != null) {
//                gold.deleteAlbum(newa.getId());
//            }
        } catch (IOException ex) {
            LOG.error("Error in main", ex);
        }
    }

    private static Properties loadProps(String name) throws IOException {
        Properties props = new Properties();
        InputStream in = Main.class.getResourceAsStream(name);
        try {
            props.load(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return props;
    }

    private static void printAlbum(Album a) {
        System.out.println("Album ID: " + a.getId());
        System.out.println("Album Title: " + a.getTitle());
        System.out.println("Album Description: " + a.getDescription());
    }

    private static void printPhotoInfo(PhotoInfo pi) {
        System.out.println("Photo ID: " + pi.getId());
        System.out.println("Photo Title: " + pi.getTitle());
        System.out.println("Photo Description: " + pi.getDescription());
    }
}
