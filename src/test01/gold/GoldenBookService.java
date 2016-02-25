package test01.gold;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class GoldenBookService implements Closeable {
    private static final Logger LOG = Logger.getLogger(GoldenBookService.class);

    private final Properties props = new Properties();
    private final Flickr flickr;

    public static GoldenBookService connect(Properties props)
            throws IOException {
        return new GoldenBookService(props);
    }

    private GoldenBookService(Properties props) {
        flickr = new Flickr(props.getProperty("apiKey"),
                props.getProperty("secret"), new REST());
        this.props.putAll(props);
        setupAuth();
    }

    @Override
    public void close() throws IOException {
    }

    public Iterable<Album> getAlbums() throws IOException {
        try {
            List<Album> result = new ArrayList<>();
            PhotosetsInterface pi = flickr.getPhotosetsInterface();
            String nsid = props.getProperty("nsid");
            int pageNumber = 1;
            while (true) {
                Photosets photosets = pi.getList(nsid, 500, pageNumber, null);
                Collection<Photoset> sets = photosets.getPhotosets();
                for (Photoset set: sets) {
                    result.add(toAlbum(set));
                }
                if (sets.size() < 500) {
                    break;
                }
                pageNumber++;
            }
            return result;
        } catch (FlickrException ex) {
            LOG.error("Error retrieving albums", ex);
            throw new IOException(ex.getMessage());
        }
    }

    public Album getAlbum(String id) throws IOException {
        try {
            PhotosetsInterface pi = flickr.getPhotosetsInterface();
            Photoset set = pi.getInfo(id);
            return set == null ? null : toAlbum(set);
        } catch (FlickrException ex) {
            LOG.error("Error retrieving album", ex);
            throw new IOException(ex.getMessage());
        }
    }

    public Album createAlbum(String title, String description,
            String priPhotoId) throws IOException {
        try {
            PhotosetsInterface pi = flickr.getPhotosetsInterface();
            return toAlbum(pi.create(title, description, priPhotoId));
        } catch (FlickrException ex) {
            LOG.error("Error deleting album", ex);
            throw new IOException(ex.getMessage());
        }
    }

    public void deleteAlbum(String id) throws IOException {
        try {
            PhotosetsInterface pi = flickr.getPhotosetsInterface();
            pi.delete(id);
        } catch (FlickrException ex) {
            LOG.error("Error retrieving albums", ex);
            throw new IOException(ex.getMessage());
        }
    }

    public Iterable<PhotoInfo> getAlbumPhotos(String id) throws IOException {
        try {
            List<PhotoInfo> result = new ArrayList<>();
            PhotosetsInterface pi = flickr.getPhotosetsInterface();
            int pageNumber = 1;
            while (true) {
                PhotoList<Photo> page = pi.getPhotos(id, 500, pageNumber);
                for (Photo photo: page) {
                    result.add(toPhotoInfo(photo));
                }
                if (page.size() < 500) {
                    break;
                }
                ++pageNumber;
            }
            return result;
        } catch (FlickrException ex) {
            LOG.error("Error retrieving albums", ex);
            throw new IOException(ex.getMessage());
        }
    }

    public PhotoInfo getPhoto(String id) throws IOException {
        try {
            PhotosInterface pi = flickr.getPhotosInterface();
            Photo photo = pi.getPhoto(id);
            return photo == null ? null : toPhotoInfo(photo);
        } catch (FlickrException ex) {
            LOG.error("Error retrieving albums", ex);
            throw new IOException(ex.getMessage());
        }
    }

    public void deletePhoto(String id) throws IOException {
        try {
            PhotosInterface pi = flickr.getPhotosInterface();
            pi.delete(id);
        } catch (FlickrException ex) {
            LOG.error("Error retrieving albums", ex);
            throw new IOException(ex.getMessage());
        }
    }

    public PhotoInfo uploadPhoto(File file, String title, String description)
            throws IOException {
        try {
            UploadMetaData metaData = new UploadMetaData();
            metaData.setTitle(title);
            metaData.setFilename(file.getName());
            metaData.setDescription(description);
            Uploader uploader = flickr.getUploader();
            String id =  uploader.upload(file, metaData);
            return new PhotoInfo()
                    .setId(id)
                    .setTitle(title)
                    .setDescription(description);
        } catch (FlickrException ex) {
            LOG.error("Error retrieving albums", ex);
            throw new IOException(ex.getMessage());
        }
    }

    private void setupAuth() {
        Auth auth = new Auth();
        auth.setToken(props.getProperty("token"));
        auth.setTokenSecret(props.getProperty("tokensecret"));
        auth.setPermission(Permission.DELETE);
        RequestContext rc = RequestContext.getRequestContext();
        rc.setAuth(auth);
    }

    private Album toAlbum(Photoset set) {
        return new Album()
                .setId(set.getId())
                .setTitle(set.getTitle())
                .setDescription(set.getDescription());
    }

    private PhotoInfo toPhotoInfo(Photo photo) {
        return new PhotoInfo()
                .setId(photo.getId())
                .setTitle(photo.getTitle())
                .setDescription(photo.getDescription());
    }
}
