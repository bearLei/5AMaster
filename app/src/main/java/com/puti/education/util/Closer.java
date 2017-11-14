/**  

 * Copyright © 2015Formax. All rights reserved.

 *

 * @Title: Closer.java

 * @Prject: FormaxMobile

 * @Package: formax.utils

 * @Description: 简化流关闭操作，不用每处地方都写个try...catch

 * @author: jieshao  

 * @date: 2015-3-24 下午6:01:17

 * @version: V1.0  

 */
package com.puti.education.util;

import android.database.Cursor;

import java.io.Closeable;

/**
 * @author jieshao
 *
 *
 */
public class Closer {

	public static void close(Closeable stream ){
		if (stream == null) {
			return ;
		}
		try {
			stream.close();
		} catch (Exception e) {

        }
		
	}

	public static void close(Cursor stream ){
		if (stream == null) {
			return ;
		}
		try {
			stream.close();
		} catch (Exception e) {

		}

	}

}
