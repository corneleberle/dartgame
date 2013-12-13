package com.namics.lab.dartgame.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.namics.lab.dartgame.service.LandscapeService;

@Service
public class LandscapeServiceImpl implements LandscapeService {

	private static final int PARTS = 5;

	private static final double MAX_HEIGHT = 0.6;

	public static final int LANDSCAPE_RESOLUTION = 1000;

	@Override
	public List<Double> getLandscape() {
		double[] landscapePoints = new double[PARTS + 1];

		// Start
		landscapePoints[0] = 0;

		// End
		landscapePoints[landscapePoints.length - 1] = 0;

		// Points between start and end
		Random random = new Random();
		for (int i = 1; i < landscapePoints.length - 1; i++) {
			landscapePoints[i] = random.nextFloat() * MAX_HEIGHT;
		}

		List<Double> landscape = new ArrayList<Double>();

		for (int i = 0; i < landscapePoints.length - 1; i++) {
			double from = landscapePoints[i];
			double to = landscapePoints[i + 1];
			landscape.addAll(getLandscapePart(from, to, LANDSCAPE_RESOLUTION / PARTS));
		}

		return landscape;
	}

	private List<Double> getLandscapePart(double from, double to, int numberOfPoints) {
		List<Double> points = new ArrayList<Double>();
		for (int x = 0; x < numberOfPoints; x++) {

			double a = (to - from) / numberOfPoints;
			double b = from;
			points.add(linearFunction(a, x, b));
		}
		return points;
	}

	private double linearFunction(double a, int x, double b) {
		return a * x + b;
	}
}
