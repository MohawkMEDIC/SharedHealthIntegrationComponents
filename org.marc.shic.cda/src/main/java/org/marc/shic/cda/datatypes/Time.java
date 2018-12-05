/**
 * Copyright 2013 Mohawk College of Applied Arts and Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author Ryan Albert
 * @since 7-Feb-2014
 *
 */
package org.marc.shic.cda.datatypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.marc.everest.datatypes.ANY;
import org.marc.everest.datatypes.GTS;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.PQ;
import org.marc.everest.datatypes.SetOperator;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.DomainTimingEvent;
import org.marc.everest.datatypes.generic.EIVL;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.datatypes.generic.PIVL;
import org.marc.everest.datatypes.generic.QSD;
import org.marc.everest.datatypes.generic.QSI;
import org.marc.everest.datatypes.generic.QSP;
import org.marc.everest.datatypes.generic.QSU;
import org.marc.everest.datatypes.generic.SXCM;
import org.marc.everest.datatypes.interfaces.ISetComponent;

/**
 * Provides a level of abstraction for all everest objects that relate to time.
 *
 * @author Ryan Albert
 */
public final class Time {

	private LinkedHashSet<SXCM> timeSets = new LinkedHashSet<SXCM>();
	private NullFlavor nullSetter = NullFlavor.Unavailable;

	private final Logger LOGGER = Logger.getLogger(getClass());

	/**
	 * Initializes the time to the specified point in time.
	 *
	 * @param timeAt A Calendar object representing a single point in time.
	 */
	public Time(Calendar timeAt) {
		addPoint(timeAt, SetOperator.Hull);
	}

	/**
	 * Initializes the time to the specified interval of time.
	 *
	 * @param timeLow A Calendar object representing the lowest point of the
	 * time interval.
	 * @param timeHigh A Calendar object representing the highest point of
	 * the time interval
	 */
	public Time(Calendar timeLow, Calendar timeHigh) {
		addInterval(timeLow, timeHigh, SetOperator.Hull);
	}

	/**
	 * Initializes the time to wrap an existing time-capable object (TS, IVL
	 * (with TS values), or GTS)
	 *
	 * @param time The existing everest time object to wrap around.
	 */
	public Time(ANY time) {
		Class timeClass = time.getClass();
		if (timeClass == TS.class) {
			addPoint((TS) time, SetOperator.Hull);
		} else if (timeClass == IVL.class) {
			addInterval((IVL) time, SetOperator.Hull);
		} else if (timeClass == GTS.class) {
			//TODO: Handle importing GTS times.
			LOGGER.warn("GTS time importing functionality is not complete. No information.");
		} else {
			throw new ClassCastException("Unable to determine time type.");
		}
	}

	/**
	 * Initializes the time to the current time.
	 */
	public Time() {
		addPoint(Calendar.getInstance(), SetOperator.Hull);
	}

	/**
	 * Defines a number of timing units.
	 */
	public enum Unit {

		Millisecond("ms"),
		Second("s"),
		Minute("min"),
		Hour("hr"),
		Day("d"),
		Week("wk"),
		Month("mo"),
		Year("a"),
		Decade("de"),
		Century("ce"),
		Millenium("mi");

		private final String unit;

		private Unit(String unitStr) {
			unit = unitStr;
		}

		public String getUnit() {
			return unit;
		}
	}

	/**
	 * Defines all of the days in the week.
	 */
	public enum Day {

		Monday(TS.valueOf("20140317")),
		Tuesday(TS.valueOf("20140318")),
		Wednesday(TS.valueOf("20140319")),
		Thursday(TS.valueOf("20140320")),
		Friday(TS.valueOf("20140321")),
		Saturday(TS.valueOf("20140322")),
		Sunday(TS.valueOf("20140323"));

		private final TS dateOfDay;

		private Day(TS date) {
			dateOfDay = date;
		}

		public TS getDateOfDay() {
			return dateOfDay;
		}
	}

	/**
	 * Creates an everest time object that best represents the time
	 * information set.
	 *
	 * @param <T> The type of everest time object to create.
	 * @param timeType The class definition of the everest time object to
	 * create.
	 * @return An everest time object of the specified class type.
	 */
	public <T extends ANY> T getTime(Class<T> timeType) {
		T result = null;
		if (timeType == GTS.class) {
			result = (T) getGeneralTime();
		} else if (timeType == TS.class) {
			result = (T) getTimestamp();
		} else if (timeType == IVL.class) {
			result = (T) getInterval();
		} else {
			LOGGER.error("Unsupported time class provided.");
		}
		return result;
	}

	/**
	 * Generates a GTS (General Time Specification) object based on the
	 * collected time sets and operations performed on the time.
	 *
	 * @return An everest GTS object.
	 */
	public GTS getGeneralTime() {
		GTS result = new GTS();
		result.setNullFlavor(NullFlavor.Unavailable);

		Iterator<SXCM> timeSetsIterator = timeSets.iterator();
		if (timeSetsIterator.hasNext()) {
			SXCM currentSet = timeSetsIterator.next();
			ISetComponent<TS> currentSetComp = null;
			LinkedList<ISetComponent> currentOpList = new LinkedList();
			currentOpList.add(currentSet);
			SetOperator currentOp = currentSet.getOperator();
			while (timeSetsIterator.hasNext()) {
				currentSet = timeSetsIterator.next();
				if (currentOp != currentSet.getOperator() || !timeSetsIterator.hasNext()) {
					currentSetComp = applyOperation(currentOpList, currentOp);
					currentOpList.clear();
					currentOpList.add(currentSetComp);
					currentOpList.add(currentSet);
					currentOp = currentSet.getOperator();
				} else {
					currentOpList.add(currentSet);
				}
			}
			result.setNullFlavor((CS) null);
			result.setHull(currentSetComp);
		}

		return result;
	}

	/**
	 * Applies a set operation to a list of elements.
	 *
	 * @param elements A List of everest Time objects to conduct an
	 * operation on.
	 * @param op The SetOperator operation to perform.
	 * @return A ISetComponent that represents an everest time object.
	 */
	private ISetComponent applyOperation(List<ISetComponent> elements, SetOperator op) {
		ISetComponent result = null;

		switch (op) {
			case Hull:
				result = elements.get(0);
				break;
			case Inclusive:
				result = new QSU(elements);
				break;
			case Exclusive:
				result = elements.get(0);
				for (int i = 1; i < elements.size(); i++) {
					result = new QSD(result, elements.get(i));
				}
				break;
			case Intersect:
				result = new QSI(elements);
				break;
			case PeriodicHull:
				result = new QSP(elements.get(0), elements.get(1));
				break;
		}
		return result;
	}

	/**
	 * Gets the interval that covers this time. Ignores any events and
	 * periods.
	 *
	 * @return A time period of IVL
	 */
	public IVL<TS> getInterval() {
		IVL<TS> result = null;
		Iterator<SXCM> setIterator = timeSets.iterator();
		while (setIterator.hasNext()) {
			SXCM set = setIterator.next();
			if (set.getClass() == IVL.class) {
				result = (IVL) set;
				break;
			}
		}
		while (setIterator.hasNext()) {
			SXCM set = setIterator.next();
			if (set.getClass() == IVL.class) {
				IVL<TS> ivlSet = (IVL) set;
				if (result.getLow() == null || result.getLow().isNull()) {
					result.setLow(ivlSet.getLow());
				} else if (ivlSet.getLow() != null && !ivlSet.getLow().isNull() && ivlSet.getLow().compareTo(result.getLow()) < 0) {
					result.setLow(ivlSet.getLow());
				}

				if (result.getHigh() == null || result.getHigh().isNull()) {
					result.setHigh(ivlSet.getHigh());
				} else if (ivlSet.getHigh() != null && !ivlSet.getHigh().isNull() && ivlSet.getHigh().compareTo(result.getHigh()) < 0) {
					result.setHigh(ivlSet.getHigh());
				}
			}
		}
		if (result == null) {
			result = new IVL();
			result.setNullFlavor(nullSetter);
		}
		if (result.getLow() == null) {
			result.setLow(new TS());
			result.getLow().setNullFlavor(nullSetter);
		}
		if (result.getHigh() == null) {
			result.setHigh(new TS());
			result.getHigh().setNullFlavor(nullSetter);
		}
		result.setOperator(null);
		return result;
	}

	/**
	 * Gets an everest timestamp (TS) that reflects a single point in time.
	 * Will calculate the average of all intervals set within.
	 *
	 * @return A TS object representing a point in time.
	 */
	public TS getTimestamp() {
		TS result = new TS();
		BigInteger timeMillisTotal = BigInteger.ZERO;
		int resultCount = 0;
		result.setNullFlavor(nullSetter);

		//Iterate and add all averages of all intervals.
		for (SXCM set : timeSets) {
			//TODO: Add more time types to handle. This is fine for now.
			if (set.getClass() == IVL.class) {
				IVL<TS> ivlSet = (IVL) set;
				TS value = ivlSet.getValue();
				if (value != null && !value.isNull()) {
					timeMillisTotal = timeMillisTotal.add(BigInteger.valueOf(value.getDateValue().getTimeInMillis()));
					resultCount++;
					continue;
				}
				TS low = ivlSet.getLow();
				TS high = ivlSet.getHigh();
				BigInteger avgPeriod = BigInteger.ZERO;
				int divBy = 0;
				if (low != null && !low.isNull()) {
					avgPeriod = avgPeriod.add(BigInteger.valueOf(low.getDateValue().getTimeInMillis()));
					divBy++;
				}
				if (high != null && !high.isNull()) {
					avgPeriod = avgPeriod.add(BigInteger.valueOf(high.getDateValue().getTimeInMillis()));
					divBy++;
				}
				if (divBy != 0) {
					avgPeriod = avgPeriod.divide(BigInteger.valueOf(2));
					timeMillisTotal.add(avgPeriod);
					resultCount++;
				}
			}
		}

		if (resultCount > 0) {
			timeMillisTotal = timeMillisTotal.divide(BigInteger.valueOf(resultCount));

			//Set the result value and remove null.
			if (timeMillisTotal.longValue() != 0) {
				result.setDateValue(calendarFromDate(new Date(timeMillisTotal.longValue())));
				result.setNullFlavor((CS) null);
			}
		}
		return result;
	}

	/**
	 * Includes a periodic point of specified frequency to the time.
	 *
	 * @param point The point in time to base the frequency on
	 * @param freq The frequency value
	 * @param freqUnit The unit of the frequency.
	 */
	public void includePeriodicPoint(Calendar point, int freq, Unit freqUnit) {
		addPeriodic(new IVL<TS>(new TS(point)), freq, freqUnit, SetOperator.Inclusive);
	}

	/**
	 * Excludes a periodic point of specified frequency from the time.
	 *
	 * @param point The point in time to base the frequency on
	 * @param freq The frequency value
	 * @param freqUnit The unit of the frequency.
	 */
	public void excludePeriodicPoint(Calendar point, int freq, Unit freqUnit) {
		addPeriodic(new IVL<TS>(new TS(point)), freq, freqUnit, SetOperator.Exclusive);
	}

	/**
	 * Intersects a periodic point of specified frequency with the time.
	 *
	 * @param point The point in time to base the frequency on
	 * @param freq The frequency value
	 * @param freqUnit The unit of the frequency.
	 */
	public void intersectPeriodicPoint(Calendar point, int freq, Unit freqUnit) {
		addPeriodic(new IVL<TS>(new TS(point)), freq, freqUnit, SetOperator.Intersect);
	}

	/**
	 * Performs a periodic hull operation with the specified periodic point
	 * to the time.
	 *
	 * @param point The point in time to base the frequency on
	 * @param freq The frequency value
	 * @param freqUnit The unit of the frequency.
	 */
	public void periodicHullPeriodicPoint(Calendar point, int freq, Unit freqUnit) {
		addPeriodic(new IVL<TS>(new TS(point)), freq, freqUnit, SetOperator.PeriodicHull);
	}

	/**
	 * Includes a periodic interval of specified frequency to the time.
	 *
	 * @param lower The point in time of the lower portion of the interval
	 * @param upper The point in time of the upper portion of the interval
	 * @param freq The frequency in which to apply the interval
	 * @param freqUnit The unit of the frequency
	 */
	public void includePeriodicInterval(Calendar lower, Calendar upper, int freq, Unit freqUnit) {
		addPeriodic(new IVL<TS>(new TS(lower), new TS(upper)), freq, freqUnit, SetOperator.Inclusive);
	}

	/**
	 * Excludes a periodic interval of specified frequency from the time.
	 *
	 * @param lower The point in time of the lower portion of the interval
	 * @param upper The point in time of the upper portion of the interval
	 * @param freq The frequency in which to apply the interval
	 * @param freqUnit The unit of the frequency
	 */
	public void excludePeriodicInterval(Calendar lower, Calendar upper, int freq, Unit freqUnit) {
		addPeriodic(new IVL<TS>(new TS(lower), new TS(upper)), freq, freqUnit, SetOperator.Exclusive);
	}

	/**
	 * Intersects a periodic interval of specified frequency with the time.
	 *
	 * @param lower The point in time of the lower portion of the interval
	 * @param upper The point in time of the upper portion of the interval
	 * @param freq The frequency in which to apply the interval
	 * @param freqUnit The unit of the frequency
	 */
	public void intersectPeriodicInterval(Calendar lower, Calendar upper, int freq, Unit freqUnit) {
		addPeriodic(new IVL<TS>(new TS(lower), new TS(upper)), freq, freqUnit, SetOperator.Intersect);
	}

	/**
	 * Performs a periodic hull operation with the specified periodic
	 * interval to the time.
	 *
	 * @param lower The point in time of the lower portion of the interval
	 * @param upper The point in time of the upper portion of the interval
	 * @param freq The frequency in which to apply the interval
	 * @param freqUnit The unit of the frequency
	 */
	public void periodicHullPeriodicInterval(Calendar lower, Calendar upper, int freq, Unit freqUnit) {
		addPeriodic(new IVL<TS>(new TS(lower), new TS(upper)), freq, freqUnit, SetOperator.PeriodicHull);
	}

	/**
	 * Includes an event with a certain interval of offset of time to the
	 * time.
	 *
	 * @param event The type of timing event description
	 * @param offsetLow The offset of the lower portion of the interval to
	 * apply
	 * @param unitLow The unit of the lower offset
	 * @param offsetHigh The offset of the upper portion of the interval to
	 * apply
	 * @param unitHigh The unit of the upper offset
	 */
	public void includeEventInterval(DomainTimingEvent event, int offsetLow, Unit unitLow, int offsetHigh, Unit unitHigh) {
		addEvent(event, new IVL<PQ>(new PQ(new BigDecimal(offsetLow), unitLow.getUnit()), new PQ(new BigDecimal(offsetHigh), unitHigh.getUnit())), SetOperator.Inclusive);
	}

	/**
	 * Excludes an event with a certain interval of offset of time to the
	 * time.
	 *
	 * @param event The type of timing event description
	 * @param offsetLow The offset of the lower portion of the interval to
	 * apply
	 * @param unitLow The unit of the lower offset
	 * @param offsetHigh The offset of the upper portion of the interval to
	 * apply
	 * @param unitHigh The unit of the upper offset
	 */
	public void excludeEventInterval(DomainTimingEvent event, int offsetLow, Unit unitLow, int offsetHigh, Unit unitHigh) {
		addEvent(event, new IVL<PQ>(new PQ(new BigDecimal(offsetLow), unitLow.getUnit()), new PQ(new BigDecimal(offsetHigh), unitHigh.getUnit())), SetOperator.Exclusive);
	}

	/**
	 * Intersects an event with a certain interval of offset of time to the
	 * time.
	 *
	 * @param event The type of timing event description
	 * @param offsetLow The offset of the lower portion of the interval to
	 * apply
	 * @param unitLow The unit of the lower offset
	 * @param offsetHigh The offset of the upper portion of the interval to
	 * apply
	 * @param unitHigh The unit of the upper offset
	 */
	public void intersectEventInterval(DomainTimingEvent event, int offsetLow, Unit unitLow, int offsetHigh, Unit unitHigh) {
		addEvent(event, new IVL<PQ>(new PQ(new BigDecimal(offsetLow), unitLow.getUnit()), new PQ(new BigDecimal(offsetHigh), unitHigh.getUnit())), SetOperator.Intersect);
	}

	/**
	 * Performs a periodic hull operation on an event with a certain
	 * interval of offset of time to the time.
	 *
	 * @param event The type of timing event description
	 * @param offsetLow The offset of the lower portion of the interval to
	 * apply
	 * @param unitLow The unit of the lower offset
	 * @param offsetHigh The offset of the upper portion of the interval to
	 * apply
	 * @param unitHigh The unit of the upper offset
	 */
	public void periodicHullEventInterval(DomainTimingEvent event, int offsetLow, Unit unitLow, int offsetHigh, Unit unitHigh) {
		addEvent(event, new IVL<PQ>(new PQ(new BigDecimal(offsetLow), unitLow.getUnit()), new PQ(new BigDecimal(offsetHigh), unitHigh.getUnit())), SetOperator.PeriodicHull);
	}

	/**
	 * Includes an event with a certain offset of time to the time.
	 *
	 * @param event The type of timing event description
	 * @param offset The amount of offset to apply
	 * @param unit The units of the offset value
	 */
	public void includeEventPoint(DomainTimingEvent event, int offset, Unit unit) {
		addEvent(event, new IVL<PQ>(new PQ(new BigDecimal(offset), unit.getUnit())), SetOperator.Inclusive);
	}

	/**
	 * Includes an event with a certain offset of time to the time.
	 *
	 * @param event The type of timing event description
	 * @param offset The amount of offset to apply
	 * @param unit The units of the offset value
	 */
	public void excludeEventPoint(DomainTimingEvent event, int offset, Unit unit) {
		addEvent(event, new IVL<PQ>(new PQ(new BigDecimal(offset), unit.getUnit())), SetOperator.Exclusive);
	}

	/**
	 * Includes an event with a certain offset of time to the time.
	 *
	 * @param event The type of timing event description
	 * @param offset The amount of offset to apply
	 * @param unit The units of the offset value
	 */
	public void intersectEventPoint(DomainTimingEvent event, int offset, Unit unit) {
		addEvent(event, new IVL<PQ>(new PQ(new BigDecimal(offset), unit.getUnit())), SetOperator.Intersect);
	}

	/**
	 * Includes an event with a certain offset of time to the time.
	 *
	 * @param event The type of timing event description
	 * @param offset The amount of offset to apply
	 * @param unit The units of the offset value
	 */
	public void periodicHullEventPoint(DomainTimingEvent event, int offset, Unit unit) {
		addEvent(event, new IVL<PQ>(new PQ(new BigDecimal(offset), unit.getUnit())), SetOperator.PeriodicHull);
	}

	/**
	 * Includes the specified point of time into the time.
	 *
	 * @param point A Calendar object of the point in time
	 */
	public void includePoint(Calendar point) {
		addPoint(point, SetOperator.Inclusive);
	}

	/**
	 * Excludes the specified point of time from the time.
	 *
	 * @param point A Calendar object of the point in time
	 */
	public void excludePoint(Calendar point) {
		addPoint(point, SetOperator.Exclusive);
	}

	/**
	 * Intersects the time with the given point of time.
	 *
	 * @param point A Calendar object of the point in time
	 */
	public void intersectPoint(Calendar point) {
		addPoint(point, SetOperator.Intersect);
	}

	/**
	 * Performs a periodic hull operation on the time with the specified
	 * point of time.
	 *
	 * @param point A Calendar object of the point in time
	 */
	public void periodicHullPoint(Calendar point) {
		addPoint(point, SetOperator.PeriodicHull);
	}

	/**
	 * Includes the specified interval into the time (Union operation).
	 *
	 * @param lower A Calendar object of the lowest point in time of the
	 * interval
	 * @param upper A Calendar object of the highest point in time of the
	 * interval
	 */
	public void includeInterval(Calendar lower, Calendar upper) {
		addInterval(lower, upper, SetOperator.Inclusive);
	}

	/**
	 * Excludes the specified interval from the time (Exclusion operation)
	 *
	 * @param lower A Calendar object of the lowest point in time of the
	 * interval
	 * @param upper A Calendar object of the highest point in time of the
	 * interval
	 */
	public void excludeInterval(Calendar lower, Calendar upper) {
		addInterval(lower, upper, SetOperator.Exclusive);
	}

	/**
	 * Performs an intersection between the current time and the given
	 * interval.
	 *
	 * @param lower A Calendar object of the lowest point in time of the
	 * interval
	 * @param upper A Calendar object of the highest point in time of the
	 * interval
	 */
	public void intersectInterval(Calendar lower, Calendar upper) {
		addInterval(lower, upper, SetOperator.Intersect);
	}

	/**
	 * Performs a periodic hull operation on the time with the given
	 * interval.
	 *
	 * @param lower A Calendar object of the lowest point in time of the
	 * interval
	 * @param upper A Calendar object of the highest point in time of the
	 * interval
	 */
	public void periodicHullInterval(Calendar lower, Calendar upper) {
		addInterval(lower, upper, SetOperator.PeriodicHull);
	}

	/**
	 * Adds a periodic time interval to the time with the given time
	 * interval to repeat.
	 *
	 * @param timePhase A period of time
	 * @param freq The frequency to apply
	 * @param unitFreq The units of the frequency
	 * @param operation The type of operation to apply to the time.
	 */
	public void addPeriodic(IVL timePhase, int freq, Unit unitFreq, SetOperator operation) {
		PIVL<TS> period = new PIVL();
		period.setOperator(operation);
		if (timePhase == null) {
			period.setNullFlavor(nullSetter);
		} else {
			period.setPeriod(new PQ(new BigDecimal(freq), unitFreq.getUnit()));
			period.setPhase(timePhase);
		}
		timeSets.add(period);
	}

	/**
	 * Adds a periodic time to the time with the given point in time to
	 * repeat.
	 *
	 * @param timePoint A Calendar object representing the point in time
	 * @param freq The frequency to apply
	 * @param unitFreq The units of the frequency
	 * @param operation The type of operation to apply to the time.
	 */
	public void addPeriodic(Calendar timePoint, int freq, Unit unitFreq, SetOperator operation) {
		addPeriodic(new IVL<TS>(new TS(timePoint)), freq, unitFreq, operation);
	}

	/**
	 * Adds a described interval to the time.
	 *
	 * @param event The DomainTimingEvent description of the time.
	 * @param offset The offset to apply to the DomainTimingEvent being
	 * added.
	 * @param operation The type of operation to apply to the time.
	 */
	public void addEvent(DomainTimingEvent event, IVL offset, SetOperator operation) {
		EIVL eventInterval = new EIVL();
		eventInterval.setOperator(operation);
		if (event == null) {
			eventInterval.setNullFlavor(nullSetter);
			return;
		}
		eventInterval.setEvent(event);
		eventInterval.setOffset(offset);
		timeSets.add(eventInterval);
	}

	/**
	 * Creates an interval between two calendar values and applies an
	 * operation to it. If one of the lower or upper portions of the
	 * interval are null, then the value of the interval is set rather than
	 * the upper or lower portions of it.
	 *
	 *
	 * @param lower The lower portion of the interval.
	 * @param upper The upper portion of the interval.
	 * @param operator The type of operation to apply to the time.
	 */
	public void addInterval(Calendar lower, Calendar upper, SetOperator operator) {
		IVL<TS> result = new IVL<TS>();
		if (lower == null && upper != null) {
			result.setValue(createTimestamp(upper));
		} else if (upper == null && lower != null) {
			result.setValue(createTimestamp(lower));
		} else {
			return;
		}
		timeSets.add(result);
	}

	/**
	 * Adds an existing interval to the time with the specified operation
	 * applied.
	 *
	 * @param time An everest IVL object representing the time interval
	 * @param operator The type of operation to apply to the time.
	 */
	public void addInterval(IVL<TS> time, SetOperator operator) {
		addInterval(time.getLow().getDateValue(), time.getHigh().getDateValue(), operator);
	}

	/**
	 * Creates an interval at a point of time.
	 *
	 * @param point A Calendar object representing the point in time to add.
	 * @param operator The type of operation to apply to the time.
	 */
	public void addPoint(Calendar point, SetOperator operator) {
		IVL result = new IVL(createTimestamp(point));
		result.setOperator(operator);
		timeSets.add(result);
	}

	/**
	 * Adds a TS object to the time with the specified operation applied.
	 *
	 * @param point A TS object representing the point in time to add.
	 * @param operator The type of operation to apply to the time.
	 */
	public void addPoint(TS point, SetOperator operator) {
		addPoint(point.getDateValue(), operator);
	}

	/**
	 * Creates a timestamp from a calendar object. Checks for a null value
	 * and applies the set null flavor.
	 *
	 * @param value A Calendar object to create a TS from.
	 * @return A TS object
	 */
	private TS createTimestamp(Calendar value) {
		TS result = new TS();
		if (value == null) {
			result.setNullFlavor(nullSetter);
		} else {
			result.setDateValue(value);
		}
		return result;
	}

	/**
	 * Sets the null flavor to be set on any instances where a null value is
	 * given.
	 *
	 * @param nullType The NullFlavor to set
	 */
	public void setNullApplication(NullFlavor nullType) {
		if (nullType == null) {
			throw new UnsupportedOperationException(String.format("The %s may not be nullified.", NullFlavor.class.getSimpleName()));
		}
		nullSetter = nullType;
	}

	/**
	 * Generates a string representation of the time.
	 *
	 * @return A String representing the time.
	 */
	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		IVL timePeriod = getInterval();
		TS lowTime = (TS) timePeriod.getLow();
		Calendar lowTimeCalendar = lowTime.getDateValue();
		if (lowTimeCalendar != null) {
			return dateFormat.format(lowTimeCalendar.getTime());
		}
		return "";
	}

	/**
	 * Creates a time for the instant this function is invoked.
	 *
	 * @return A Time object representing the current system time.
	 */
	public static Time now() {
		return new Time(Calendar.getInstance());
	}

	/**
	 * Converts a Date object to a Calendar object.
	 *
	 * @param time A Date object
	 * @return A Calendar object.
	 */
	public static Calendar calendarFromDate(Date time) {
		Calendar result = null;
		if (time != null) {
			result = Calendar.getInstance();
			result.setTime(time);
		}
		return result;
	}
}
