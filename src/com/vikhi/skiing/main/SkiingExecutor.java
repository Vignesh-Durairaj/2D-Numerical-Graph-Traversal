package com.vikhi.skiing.main;

import com.vikhi.skiing.analyzer.SkiingMapProcessor;
import com.vikhi.skiing.common.CommonException;

public class SkiingExecutor {

	public static void main(String[] args) throws CommonException {
		String filePathStr = null;
		if (null != args && args.length > 0 && null != args[0] && args[0].trim().length() > 0) {
			filePathStr = args[0].trim();
		}
		
		SkiingMapProcessor skiingMap = new SkiingMapProcessor();
		skiingMap.initMapModel(filePathStr);
		
		if (skiingMap.validateMapModel()) {
			skiingMap.printLongestSteepestSkiPath();
		}
	}
}
