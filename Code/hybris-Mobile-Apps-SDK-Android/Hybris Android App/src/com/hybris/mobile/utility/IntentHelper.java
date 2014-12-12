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

import java.util.Hashtable;

public class IntentHelper {
	private static IntentHelper _instance;
    private Hashtable<String, Object> _hash;

    private IntentHelper() {
        _hash = new Hashtable<String, Object>();
    }

    private static IntentHelper getInstance() {
        if(_instance==null) {
            _instance = new IntentHelper();
        }
        return _instance;
    }

    public static void addObjectForKey(Object object, String key) {
        getInstance()._hash.put(key, object);
    }

    public static Object getObjectForKey(String key) {
        IntentHelper helper = getInstance();
        Object data = helper._hash.get(key);
        helper._hash.remove(key);
        helper = null;
        return data;
    }
}
