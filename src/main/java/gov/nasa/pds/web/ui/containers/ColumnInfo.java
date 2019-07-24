package gov.nasa.pds.web.ui.containers;

import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.ObjectStatement;

import java.io.Serializable;

public class ColumnInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Common to PDS3 and PDS4 labels
	private String name;
	private final String type;
	private String description;
	private Integer index;
	private Integer bytes;
	private final String format;
	private final String missingConstant;

	// PDS3 specific
	private Integer startByte;
	private final Integer numItems;
	private final Integer itemBytes;
	private final Integer itemOffset;
	private final ObjectStatement columnDef;
	private final Integer bits;
	private final Integer startBit;
	private final Integer itemBits;
	private Integer fieldNumber;

	// PDS4 specific
	private final String unit;
	private final Double scalingFactor;
	private final Double valueOffset;
	private Integer stopBit;
	
	/**
	 * Constructor used for PDS3 labels. All unused PDS4 fields are set to null
	 * 
	 * @param columnDef
	 */
	@SuppressWarnings("nls")
	public ColumnInfo(ObjectStatement columnDef) {
		
		// required
		final AttributeStatement nameAttrib = columnDef.getAttribute("NAME");
		this.name = nameAttrib.getValue().toString();

		// optional
		// Data type is treated the same for BIT_COLUMNS and COLUMNS since they
		// both share the same type
		final AttributeStatement bitTypeAttrib = columnDef
				.getAttribute("BIT_DATA_TYPE");
		final AttributeStatement typeAttrib = columnDef
				.getAttribute("DATA_TYPE");
		this.type = typeAttrib == null ? bitTypeAttrib.getValue().toString()
				: typeAttrib.getValue().toString();

		final AttributeStatement bytesAttrib = columnDef.getAttribute("BYTES");
		this.bytes = bytesAttrib == null ? null : Integer.valueOf(bytesAttrib
				.getValue().toString());

		final AttributeStatement descriptionAttrib = columnDef
				.getAttribute("DESCRIPTION");
		this.description = descriptionAttrib == null ? null : descriptionAttrib
				.getValue().toString();

		final AttributeStatement indexParam = columnDef
				.getAttribute("COLUMN_NUMBER");
		this.index = indexParam == null ? null : Integer.valueOf(indexParam
				.getValue().toString());

		final AttributeStatement formatAttrib = columnDef
				.getAttribute("FORMAT");
		this.format = formatAttrib == null ? null : formatAttrib.getValue()
				.toString();

		final AttributeStatement missingConstantAttrib = columnDef
				.getAttribute("MISSING_CONSTANT");
		this.missingConstant = missingConstantAttrib == null ? null
				: missingConstantAttrib.getValue().toString();

		this.columnDef = columnDef;

		final AttributeStatement bitsAttrib = columnDef.getAttribute("BITS");
		this.bits = bitsAttrib == null ? null : Integer.valueOf(bitsAttrib
				.getValue().toString());

		final AttributeStatement startBitAttrib = columnDef
				.getAttribute("START_BIT");
		this.startBit = startBitAttrib == null ? null : Integer
				.valueOf(startBitAttrib.getValue().toString());

		final AttributeStatement itemBitsAttrib = columnDef
				.getAttribute("ITEM_BITS");
		this.itemBits = itemBitsAttrib == null ? null : Integer
				.valueOf(itemBitsAttrib.getValue().toString());

		final AttributeStatement startByteAttrib = columnDef
				.getAttribute("START_BYTE");
		this.startByte = startByteAttrib == null ? null : Integer
				.valueOf(startByteAttrib.getValue().toString());

		final AttributeStatement numItemsAttrib = columnDef
				.getAttribute("ITEMS");
		this.numItems = numItemsAttrib == null ? null : Integer
				.valueOf(numItemsAttrib.getValue().toString());

		final AttributeStatement itemBytesAttrib = columnDef
				.getAttribute("ITEM_BYTES");
		this.itemBytes = itemBytesAttrib == null ? null : Integer
				.valueOf(itemBytesAttrib.getValue().toString());

		final AttributeStatement itemOffsetAttrib = columnDef
				.getAttribute("ITEM_OFFSET");
		this.itemOffset = itemOffsetAttrib == null ? null : Integer
				.valueOf(itemOffsetAttrib.getValue().toString());

		final AttributeStatement fieldNumberAttrib = columnDef
				.getAttribute("FIELD_NUMBER");
		this.fieldNumber = fieldNumberAttrib == null ? null : Integer
				.valueOf(fieldNumberAttrib.getValue().toString());

		// Unused PDS4 elements set to null
		this.unit = null;
		this.scalingFactor = null;
		this.valueOffset = null;
		this.stopBit = null;
	}
	
	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public String getDescription() {
		return this.description;
	}

	public Integer getIndex() {
		return this.index;
	}

	public Integer getBytes() {
		return this.bytes;
	}

	public Integer getStartByte() {
		return this.startByte;
	}

	public String getFormat() {
		return this.format;
	}

	public Integer getNumItems() {
		return this.numItems;
	}

	public Integer getItemBytes() {
		return this.itemBytes;
	}

	public Integer getItemOffset() {
		return this.itemOffset;
	}

	public String getMissingConstant() {
		return this.missingConstant;
	}

	public ObjectStatement getColumnDef() {
		return this.columnDef;
	}
	
	public Integer getBits() {
		return this.bits;
	}
	
	public Integer getStartBit() {
		return this.startBit;
	}
	
	public Integer getItemBits() {
		return this.itemBits;
	}
	
	public Integer getFieldNumber() {
        return this.fieldNumber;
    }
	
	public Integer getStopBit() {
		return this.stopBit;
	}
	
	public String getUnit() {
		return this.unit;
	}

	public Double getScalingFactor() {
		return this.scalingFactor;
	}

	public Double getValueOffset() {
		return this.valueOffset;
	}

	public void setName(String newName) {
		 this.name = newName;
	}
	    
	public void appendName(String nameToAppend) {
		this.name = this.name + nameToAppend;   
	}
	
	public void setStartByte(Integer newStartByte) {   
		this.startByte = newStartByte;
	}
	
	public void setBytes(Integer newBytes) {
		this.bytes = newBytes;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setIndex(Integer index) {
		this.index = index;
	}
	    
	public void setStopBit(int stopBit) {
		this.stopBit = stopBit;
	}
	
	public void setFieldNumber(Integer fieldNumber) {
		this.fieldNumber = fieldNumber;
	}
}
