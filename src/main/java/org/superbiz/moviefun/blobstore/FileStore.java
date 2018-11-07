package org.superbiz.moviefun.blobstore;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.superbiz.moviefun.albums.AlbumsController;

import java.io.*;
import java.util.Optional;

import static java.lang.String.format;


@Component
public class FileStore implements BlobStore {


    @Override
    public void put(Blob blob) throws IOException {

        saveUploadToFile(blob.inputStream, getCoverFile(blob.name));
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {

        File file = new File(name);
        Tika tika = new Tika();


        if (!file.exists()) {
            return Optional.empty();
        }

        return Optional.of(new Blob(
                name,
                new FileInputStream(file),
                tika.detect(file)
        ));

    }

    @Override
    public void deleteAll() {

    }


    private void saveUploadToFile(InputStream uploadedFile, File targetFile) throws IOException {
        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            byte[] buffer = new byte[uploadedFile.available()];
            uploadedFile.read(buffer);
            outputStream.write(buffer);
        }
    }

    private File getCoverFile(String name) {
        String coverFileName = format("covers/%s", name);
        return new File(coverFileName);
    }

}
