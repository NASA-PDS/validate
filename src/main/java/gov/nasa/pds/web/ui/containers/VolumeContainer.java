package gov.nasa.pds.web.ui.containers;

import gov.nasa.arc.pds.tools.util.LocaleUtils;
import gov.nasa.pds.web.ui.constants.DataSetConstants;
import gov.nasa.pds.web.ui.constants.DataSetConstants.DataType;
import gov.nasa.pds.web.ui.constants.DataSetConstants.Status;

import java.util.Date;
import java.util.UUID;

public class VolumeContainer implements BaseContainerInterface {

	private final DataType type;

	private String typeDisplay;

	private final String createdBy;

	private final Status status;

	private String statusDisplay;

	private final LocaleUtils localeUtils;

	private final UUID uuid;

	private final Date lastModified;

	private String lastModifiedDisplay;

	private final String name;

	private final String setId;

	private final String volId;

	private final String volumePath;

	public VolumeContainer(LocaleUtils localeUtils, final String name,
			final String volId, final String setId, final String relPath,
			Status status, String createdBy, DataType type, UUID uuid) {
		this.localeUtils = localeUtils;
		this.status = status;
		this.createdBy = createdBy;
		this.type = type;
		this.uuid = uuid;
		this.lastModified = new Date();
		this.name = name;
		this.volId = volId;
		this.setId = setId;
		this.volumePath = relPath;
	}

	public String getSetId() {
		return this.setId;
	}

	public String getVolumePath() {
		return this.volumePath;
	}

	public String getStatusDisplay() {
		if (this.statusDisplay == null) {
			this.statusDisplay = this.localeUtils.getText(this.status.getKey());
		}
		return this.statusDisplay;
	}

	public String getTypeDisplay() {
		if (this.typeDisplay == null) {
			this.typeDisplay = this.localeUtils.getText(this.type.getKey());
		}
		return this.typeDisplay;
	}

	public DataType getType() {
		return this.type;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public DataSetConstants.Status getStatus() {
		return this.status;
	}

	public String getUuid() {
		return this.uuid.toString();
	}

	public Date getLastModified() {
		return this.lastModified;
	}

	public String getLastModifiedDisplay() {
		if (this.lastModifiedDisplay == null) {
			// TODO: hook this up to struts' date display tools
			this.lastModifiedDisplay = this.lastModified.toString();
		}
		return this.lastModifiedDisplay;
	}

	public String getName() {
		return this.name;
	}

	public String getVolId() {
		return this.volId;
	}

}
