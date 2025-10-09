package gov.nasa.pds.tools.validate.content;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.ValidationTarget;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.mp4parser.IsoFile;
import org.mp4parser.boxes.iso14496.part12.MovieBox;
import org.mp4parser.boxes.iso14496.part12.TrackBox;

public class AudioVideo {
  final private ProblemListener listener;
  final private URL urlRef;
  final private ValidationTarget target;
  public AudioVideo (ProblemListener listener, ValidationTarget target, URL urlRef) {
    this.listener = listener;
    this.target = target;
    this.urlRef = urlRef;
  }
  public void checkMetadata (boolean audio, boolean video) {
    try {
      boolean a = false,v = false;
      IsoFile content = new IsoFile(FileUtils.toFile(this.urlRef).getPath());
      MovieBox movie = content.getMovieBox();
      if (movie == null) {
        this.listener.addProblem(new ValidationProblem(
            new ProblemDefinition(ExceptionType.ERROR, ProblemType.NOT_MP4_FILE,
                "Does not look like an MP4/M4A because no boxes found within: " + urlRef.toString()),
            target));
      } else {
        for (TrackBox track : movie.getBoxes(TrackBox.class)) {
          a |= "soun".equals(track.getMediaBox().getHandlerBox().getHandlerType());
          v |= "vide".equals(track.getMediaBox().getHandlerBox().getHandlerType());
        }
      }
      content.close();
      if (a != audio) {
        this.listener.addProblem(new ValidationProblem(
            new ProblemDefinition(ExceptionType.ERROR, ProblemType.NOT_MP4_FILE,
                "Does not look like an MP4/M4A because expected audio but found none: " + urlRef.toString()),
            target));
      }
      if (v != video) {
        this.listener.addProblem(new ValidationProblem(
            new ProblemDefinition(ExceptionType.ERROR, ProblemType.NOT_MP4_FILE,
                "Does not look like an MP4/M4A because expected video but found none: " + urlRef.toString()),
            target));
      }
    } catch (Exception e) {
      ProblemDefinition def =
          new ProblemDefinition(ExceptionType.ERROR, ProblemType.INTERNAL_ERROR,
              "Error occurred while processing MP4/M4A file content for "
                  + FilenameUtils.getName(urlRef.toString()) + ": " + e.getMessage());
      this.listener.addProblem(new ValidationProblem(def, this.target));
    }
  }

  public boolean checkWavHeader() {
    File file = FileUtils.toFile(this.urlRef);
    try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
      // Check RIFF chunk ID ("RIFF")
      byte[] riff = new byte[4];
      raf.readFully(riff);
      if (!new String(riff).equals("RIFF")) {
        this.listener.addProblem(new ValidationProblem(
            new ProblemDefinition(ExceptionType.ERROR, ProblemType.NOT_WAV_FILE,
                "Does not look like a WAV because did not find expected 'RIFF' in header: " + urlRef.toString()),
            target));
        return false;
      }
      // Skip file size
      byte[] sz = new byte[4];
      raf.readFully(sz);
      /* RIFF requires the chunk size to be 8 bytes less than the file size reported by the OS */
      int size = ByteBuffer.wrap(sz).order(ByteOrder.LITTLE_ENDIAN).getInt() + 8;
      if (size != file.length()) {
        this.listener.addProblem(new ValidationProblem(
            new ProblemDefinition(ExceptionType.WARNING, ProblemType.NON_WAV_FILE,
                String.format("Does not look like a valid WAV because the file length %d does not match the size in the header %d: %s", file.length(), size, urlRef.toString())),
            target)); 
      }
      // Check WAVE format ("WAVE")
      byte[] wave = new byte[4];
      raf.readFully(wave);
      if (!new String(wave).equals("WAVE")) {
        this.listener.addProblem(new ValidationProblem(
            new ProblemDefinition(ExceptionType.ERROR, ProblemType.NOT_WAV_FILE,
                "Does not look like a WAV because did not find expected 'WAVE' in header: " + urlRef.toString()),
            target));
        return false;
      }
      // Check 'fmt ' sub-chunk ("fmt ")
      byte[] fmt = new byte[4];
      raf.readFully(fmt);
      if (!new String(fmt).equals("fmt ")) {
        this.listener.addProblem(new ValidationProblem(
            new ProblemDefinition(ExceptionType.ERROR, ProblemType.NOT_WAV_FILE,
                "Does not look like a WAV because did not find expected 'fmt ' in header: " + urlRef.toString()),
            target));
        return false;
      }
    } catch (IOException e) {
      this.listener.addProblem(new ValidationProblem(
          new ProblemDefinition(ExceptionType.ERROR, ProblemType.NOT_WAV_FILE,
              "Does not look like a WAV because cannot read from it: " + urlRef.toString()),
          target));
      return false;
    }
    return true;
  }
}
