/*******************************************************************************
 * [y] hybris Platform
 *  
 *   Copyright (c) 2000-2013 hybris AG
 *   All rights reserved.
 *  
 *   This software is the confidential and proprietary information of hybris
 *   ("Confidential Information"). You shall not disclose such Confidential
 *   Information and shall use it only in accordance with the terms of the
 *   license agreement you entered into with hybris.
 ******************************************************************************/
package com.hybris.mobile.utility;

import android.text.TextPaint;
import android.text.style.URLSpan;

public class URLSpanNoUnderline extends URLSpan {
	public URLSpanNoUnderline(String p_Url) {
		super(p_Url);
	}

	public void updateDrawState(TextPaint p_DrawState) {
		super.updateDrawState(p_DrawState);
		p_DrawState.setUnderlineText(false);
	}
}
