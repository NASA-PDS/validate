package gov.nasa.pds.tools.validate.content;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.ValidationTarget;
import java.net.URL;
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
      IsoFile content = new IsoFile(this.urlRef.getPath());
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
}
