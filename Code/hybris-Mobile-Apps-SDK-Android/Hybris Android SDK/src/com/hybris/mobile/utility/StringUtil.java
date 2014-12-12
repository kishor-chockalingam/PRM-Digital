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

import android.text.Spannable;
import android.text.style.URLSpan;

public final class StringUtil {

	public static void removeUnderlines(Spannable p_Text) {
		URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);

		for(URLSpan span:spans) {
			int start = p_Text.getSpanStart(span);
			int end = p_Text.getSpanEnd(span);
			p_Text.removeSpan(span);
			span = new URLSpanNoUnderline(span.getURL());
			p_Text.setSpan(span, start, end, 0);
		}

	}

	public static String replaceIfNull(String toReplace, String replacement){
		if (toReplace == null || "".equals(toReplace)) {
			return replacement;
		}
		return toReplace;
	}
}
