package com.pccw.util.printing;

import java.awt.print.Paper;

public class BasePapermm extends Paper {

	private static final int TOP_BOTTOM_MARGIN = 3;
	private static final int LEFT_RIGHT_MARGIN = 3;
	
	public BasePapermm(double pWidth, double pHeight) {
		super();
		this.setSize(JavaPrintingHelper.mmToPrintingDimension(pWidth), 
						JavaPrintingHelper.mmToPrintingDimension(pHeight));
		this.setImageableArea(JavaPrintingHelper.mmToPrintingDimension(LEFT_RIGHT_MARGIN), 
				JavaPrintingHelper.mmToPrintingDimension(TOP_BOTTOM_MARGIN), 
				this.getWidth() - (2 * JavaPrintingHelper.mmToPrintingDimension(LEFT_RIGHT_MARGIN)), 
				this.getHeight() - (2 * JavaPrintingHelper.mmToPrintingDimension(TOP_BOTTOM_MARGIN)));
	}
}