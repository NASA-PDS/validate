package gov.nasa.pds.web.ui.containers;

import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.containers.VolumeContainerSimple;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.PointerStatement;
import gov.nasa.pds.tools.label.parser.DefaultLabelParser;
import gov.nasa.pds.tools.label.validate.Validator;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LabelFragmentContainer extends LabelContainer {

	@Override
	protected boolean initLabel(final VolumeContainerSimple volume,
			final Dictionary dictionary, final boolean forceParse) {
		this.resolver.setVolumeContext(volume);
		DefaultLabelParser parser = new DefaultLabelParser(this.resolver);
		try {
			if (this.labelFile != null) {
				this.labelObj = parser.parsePartial(this.labelFile, null);
			} else {
				this.labelObj = parser.parsePartial(this.labelUrl, null);
			}
			Validator validator = new Validator();
			validator.validate(this.labelObj, dictionary);

			// pass values through
			this.problems.addAll(this.labelObj.getProblems());

			// get pointers
			// get top level pointers
			final List<PointerStatement> foundPointers = this.labelObj
					.getPointers();
			if (foundPointers != null) {
				this.pointers.addAll(foundPointers);
			}
			// recursively get pointers hanging on objects
			final List<ObjectStatement> foundObjects = this.labelObj
					.getObjects();
			for (ObjectStatement object : foundObjects) {
				addPointers(object);
			}
			return true;
		} catch (LabelParserException e) {
			this.isValid = false;
			this.problems.add(e);
			return false;
		} catch (IOException e) {
			// this.errors.add(e);
			e.printStackTrace();
			return false;
		}
	}

	public LabelFragmentContainer(File labelFile, VolumeContainerSimple volume,
			Dictionary dictionary) {
		super(labelFile, volume, dictionary, false);
	}

}
