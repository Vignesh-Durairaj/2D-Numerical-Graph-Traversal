package com.vikhi.skiing.analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.vikhi.skiing.common.CommonException;
import com.vikhi.skiing.pojo.MapModel;
import com.vikhi.skiing.pojo.SkiPoint;

public class SkiingMapProcessor {
	
	static final Logger LOGGER = Logger.getLogger(SkiingMapProcessor.class);
	
	private MapModel mapModel;
	
	public void initMapModel(final String filePathStr) throws CommonException {
		mapModel = null;
		int[][] digitalMapArray = getDigitalMapArray (filePathStr);
		
		if (null != digitalMapArray && null != digitalMapArray[0] && digitalMapArray[0].length == 2) {
			
			mapModel = new MapModel();
			mapModel.setHeight(digitalMapArray[0][0]);
			mapModel.setWidth(digitalMapArray[0][1]);
			
			List<int[]> intList = new ArrayList<>(Arrays.asList(digitalMapArray));
			intList.remove(0);
			mapModel.setMapArray(intList.toArray(new int[][]{}));
			
		} else {
			LOGGER.error("Map data file is of invalid format and of invalid length and width");
			throw new CommonException("Map data file is of invalid format and of invalid length and width");
		}
	}

	public boolean validateMapModel() {
		boolean isValid = false;
		
		if (mapModel != null) {
			int height = mapModel.getHeight();
			int width = mapModel.getWidth();
			
			int[][] mapArray = mapModel.getMapArray();
			if (mapArray != null && mapArray.length == height) {
				isValid = Arrays.stream(mapArray).noneMatch(i -> i.length > width);
			}
		}
		
		LOGGER.info("The validity of the Map Model object created is : " + (isValid ? "Valid" : "In Valid"));
		return isValid;
	}
	
	public void printLongestSteepestSkiPath () {
		List<List<SkiPoint>> longAndSteepSkiPath = getSteepestSkiPath();
		if (null != longAndSteepSkiPath) {
			longAndSteepSkiPath
						.stream()
						.forEach(li -> {li.stream().forEach(LOGGER::info);
										LOGGER.info("Length Covered : " + li.size());
										LOGGER.info("Depth Covered : " + (li.get(0).getElevationValue() - li.get(li.size() - 1).getElevationValue()));});
		}
	}
	
	private int[][] getDigitalMapArray (final String filePathStr) throws CommonException {
		
		String mapPathStr = null;
		if (filePathStr == null || filePathStr.trim().equals("")) {
			mapPathStr = "src/map.txt";
		} else {
			mapPathStr = filePathStr;
		}
		
		int[][] digitalMapArray = null;
		Path mapPath = Paths.get(mapPathStr);
		
		try (Stream<String> mapStream = Files.lines(mapPath)) {
			 digitalMapArray = mapStream
							.filter(str -> str.trim().length() > 0)
							.map(i -> i.split(" "))
							.map(i -> Arrays
										.stream(i)
										.mapToInt(Integer::parseInt)
										.toArray())
							.toArray(int[][]::new);
		} catch (IOException e) {
			throw new CommonException ("Exception while reading the file.", e);
		} 
		
		return digitalMapArray;
	}
	
	public List<SkiPoint> getLongestPath (final SkiPoint currentSkiPoint) {
		List<SkiPoint> longestPath = new ArrayList<>();
		List<SkiPoint> currentTraversal = null;
		
		// Traversing in Northern direction
		if (currentSkiPoint.getVerticalPoint() > 0 
				&& currentSkiPoint.getElevationValue() > mapModel.getMapArray()[currentSkiPoint.getVerticalPoint() - 1][currentSkiPoint.getHorizontalPoint()]) {
			currentTraversal = getLongestPath (new SkiPoint(mapModel.getMapArray()[currentSkiPoint.getVerticalPoint() - 1][currentSkiPoint.getHorizontalPoint()], 
					currentSkiPoint.getHorizontalPoint(), currentSkiPoint.getVerticalPoint() - 1));
			
			if (isCurrentLongerOrDeeper(currentTraversal, longestPath)) {
				longestPath = currentTraversal;
			}
		}
		
		// Traversing in Eastern direction
		if (currentSkiPoint.getHorizontalPoint() < mapModel.getWidth() - 1 
				&& currentSkiPoint.getElevationValue() > mapModel.getMapArray()[currentSkiPoint.getVerticalPoint()][currentSkiPoint.getHorizontalPoint() + 1]) {
			currentTraversal = getLongestPath (new SkiPoint(mapModel.getMapArray()[currentSkiPoint.getVerticalPoint()][currentSkiPoint.getHorizontalPoint() + 1], 
					currentSkiPoint.getHorizontalPoint() + 1, currentSkiPoint.getVerticalPoint()));
			
			if (isCurrentLongerOrDeeper(currentTraversal, longestPath)) {
				longestPath = currentTraversal;
			}
		}
		
		// Traversing in Southern direction
		if (currentSkiPoint.getVerticalPoint() < mapModel.getHeight() - 1 
				&& currentSkiPoint.getElevationValue() > mapModel.getMapArray()[currentSkiPoint.getVerticalPoint() + 1][currentSkiPoint.getHorizontalPoint()]) {
			currentTraversal = getLongestPath (new SkiPoint(mapModel.getMapArray()[currentSkiPoint.getVerticalPoint() + 1][currentSkiPoint.getHorizontalPoint()], 
					currentSkiPoint.getHorizontalPoint(), currentSkiPoint.getVerticalPoint() + 1));
			
			if (isCurrentLongerOrDeeper(currentTraversal, longestPath)) {
				longestPath = currentTraversal;
			}
		}
		
		// Traversing in Western direction
		if (currentSkiPoint.getHorizontalPoint() > 0 
				&& currentSkiPoint.getElevationValue() > mapModel.getMapArray()[currentSkiPoint.getVerticalPoint()][currentSkiPoint.getHorizontalPoint() - 1]) {
			currentTraversal = getLongestPath (new SkiPoint(mapModel.getMapArray()[currentSkiPoint.getVerticalPoint()][currentSkiPoint.getHorizontalPoint() - 1], 
					currentSkiPoint.getHorizontalPoint() - 1, currentSkiPoint.getVerticalPoint()));
			
			if (isCurrentLongerOrDeeper(currentTraversal, longestPath)) {
				longestPath = currentTraversal;
			}
		}
		
		longestPath.add(currentSkiPoint);
		return longestPath;
	}
	
	private List<List<SkiPoint>> getAllLongestSkiPath() {
		
		List<List<SkiPoint>> allLongestSkiPaths = new ArrayList<>();
		for (int h = 0; h < mapModel.getHeight(); h ++) {
			for (int w = 0; w < mapModel.getWidth(); w ++) {
				SkiPoint currentSkiPoint = new SkiPoint(mapModel.getMapArray()[h][w], w, h);
				LOGGER.info("Getting all the longestSkiPath for Ski-Point : " + currentSkiPoint);
				List<SkiPoint> longestSkiPath = getLongestPath(currentSkiPoint);
				
				Collections.reverse(longestSkiPath);
				allLongestSkiPaths.add(longestSkiPath);
			}
		}
		
		return allLongestSkiPaths;
	}
	
	private List<List<SkiPoint>> getLongestSkiPaths () {
		List<List<SkiPoint>> longestSkiPaths = null;
		List<List<SkiPoint>> allTraversedPaths = getAllLongestSkiPath();
		
		int longestSkiDistance = allTraversedPaths
												.stream()
												.map(List::size)
												.max(Comparator.naturalOrder())
												.orElse(-1);
		
		longestSkiPaths = allTraversedPaths
										.stream()
										.filter(li -> li.size() == longestSkiDistance)
										.collect(Collectors.toList());
		
		return longestSkiPaths;
	}
	
	private List<List<SkiPoint>> getSteepestSkiPath () {
		List<List<SkiPoint>> steepestSkiPaths = null;
		List<List<SkiPoint>> longestSkiPaths = getLongestSkiPaths();
		
		if (null != longestSkiPaths) {
			int steepestDistance = longestSkiPaths
												.stream()
												.map(list -> list.get(0).getElevationValue() - list.get(list.size() - 1).getElevationValue())
												.max(Comparator.naturalOrder())
												.orElse(-1);
			
			steepestSkiPaths = longestSkiPaths
											.stream()
											.filter(list -> (list.get(0).getElevationValue() - list.get(list.size() - 1).getElevationValue()) == steepestDistance)
											.collect(Collectors.toList()); 
		}
		return steepestSkiPaths;
	}
	
	private boolean isCurrentLongerOrDeeper (final List<SkiPoint> currentTraversal, final List<SkiPoint> longestPath) {
		
		boolean isCurrentLongerOrDeeper = false;
		
		int currentTraversalSize = currentTraversal.size();
		int longestPathSize = longestPath.size();
		
		if (currentTraversalSize == longestPathSize) {
			int currentTraversalDepth = currentTraversal.get(currentTraversalSize - 1).getElevationValue() - 
					currentTraversal.get(0).getElevationValue();
			
			int longestTraversalDepth = longestPath.get(longestPathSize - 1).getElevationValue() - 
					longestPath.get(0).getElevationValue();
			
			if ((currentTraversalDepth == longestTraversalDepth && 
					currentTraversal.get(0).getElevationValue() < longestPath.get(0).getElevationValue()) || 
					currentTraversalDepth > longestTraversalDepth) {
				isCurrentLongerOrDeeper = true;
			} 
			
		} else if (currentTraversalSize > longestPathSize) {
			isCurrentLongerOrDeeper = true;
		}
		
		return isCurrentLongerOrDeeper;
	}

}
