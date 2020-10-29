package com.zhangqtest.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 *
 * 功能描述:
 * @Function : 用来提供一种比较简洁的参数校验的方式.
 * @param:
 * @return:
 * @auther: zhangq
 * @date: 2020-10-29 22:26
 */
public class CheckEmptyUtil {

	/**
	 * 判断一个参数是否为空.判断的是否根据不同的类型.会有不同的判断标准.
	 * 
	 * @param object
	 *            待校验的参数
	 * @return 首先如果object是为null,不进行类型判断,直接返回true;<br>
	 *         如果object不为空,则针对不同的class,分别进行判断<br>
	 *         String : trim()后length为0的是否返回true<br>
	 *         List : 返回list.isEmpty()<br>
	 *         Map : 返回map.isEmpty()<br>
	 *         Set : 返回set.isEmpty()<br>
	 *         Array : 如果length为0,返回true<br>
	 *         CharSequence : length为0或者toString后trim()为空,则返回true<br>
	 */
	public static boolean isEmpty(Object object) {
		if (object == null) {
			return true;
		}
		if (object instanceof String) {
			String str = (String) object;
			if (str.trim().length() == 0) {
				return true;
			}
		}
		if (object instanceof CharSequence) {
			CharSequence cs = (CharSequence) object;
			if (cs.length() == 0) {
				return true;
			}
			if (cs.toString().trim().length() == 0) {
				return true;
			}
		}
		if (object instanceof List<?>) {
			List<?> list = (List<?>) object;
			return list.isEmpty();
		}
		if (object instanceof Map<?, ?>) {
			Map<?, ?> map = (Map<?, ?>) object;
			return map.isEmpty();
		}
		if (object instanceof Set<?>) {
			Set<?> set = (Set<?>) object;
			return set.isEmpty();
		}
		if (object.getClass().isArray()) {
			Object[] array = (Object[]) object;
			if (array.length == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断对象是否为空,其实现为isEmpty取反.
	 * 
	 * @param 待校验的参数.
	 * @return 对象为空则返false,不为空则返回true;
	 */
	public static boolean isNotEmpty(Object object) {
		return !isEmpty(object);
	}

	/**
	 * 如果传入的参数中任意 一个为空,则返回true.
	 * 
	 * @param objects
	 *            待校验对象数组
	 * @return 判断结果
	 */
	public static boolean isOrEmpty(Object... objects) {
		if (objects == null || objects.length == 0) {
			return true;
		}
		for (Object object : objects) {
			if (isEmpty(object)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 如果传入的参数都为空,返回true,否则返回false.
	 * 
	 * @param objects
	 *            待判断的数组
	 * @return 判断结果
	 */
	public static boolean isAndEmpty(Object... objects) {
		if (objects == null || objects.length == 0) {
			return true;
		}
		for (Object object : objects) {
			if (!isEmpty(object)) {
				return false;
			}
		}
		return true;
	}
}
