/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * CVS information:
 *  $RCSfile$
 *   $Source$
 *     $Date: 2010-07-15 19:49:13 +0200 (Thu, 15 Jul 2010) $
 *   $Author: jo_mormes $
 * $Revision: 6680 $
 */
package devplugin;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Logger;

import util.ui.Localizer;

/**
 * the date of a program. it holds year, month and day.
 *
 * @author Martin Oberhauser
 */
public class Date implements Comparable<Date>
{
  /**
   * the localizer for the date. It is not final because it must be reinitialized after setting the language
   * from the settings.
   */
  private static Localizer LOCALIZER = Localizer.getLocalizerFor(Date.class);

  /**
   * logger for this class.
   */
  private static final Logger LOGGER = Logger.getLogger(Date.class.getName());


  /**
   * a cache for dates. static vars are not serialized.
   */
  private static final HashMap<Integer, String> LONG_DATE_MAP = new HashMap<Integer, String>(28);

  /**
   * this limits the size of LONG_DATE_MAP to prevent a potential mem leak. the size was
   * chosen quite random so its subject to change.
   */
  private static final byte DATE_CACHE_SIZE = 50;



  /**
   * the year, ie Calendar.get(Calendar.YEAR).
   */
  private short mYear;

  /**
   * the month, ie Calendar.get(Calendar.MONTH) + 1.
   */
  private byte mMonth;

  /**
   * the day, ie Calendar.get(Calendar.DAY_OF_MONTH).
   */
  private byte mDay;



  /**
   * Constructs a new Date object, initialized with the current date.
   */
  public Date() {
    Calendar mCalendar = Calendar.getInstance();
    mYear = (short) mCalendar.get(Calendar.YEAR);
    mMonth = (byte) (mCalendar.get(Calendar.MONTH) + 1);
    mDay = (byte) mCalendar.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * constructs a new date initialized with the given arguments.
   *
   * @param year Calendar.get(Calendar.YEAR)
   * @param month Calendar.get(Calendar.MONTH) + 1
   * @param dayOfMonth Calendar.get(Calendar.DAY_OF_MONTH)
   */
  public Date(final short year, final byte month, final byte dayOfMonth) {
    mYear = year;
    mMonth = month;
    mDay = dayOfMonth;
  }

  /**
   * constructs a new date initialized with the given arguments.
   * This is a convenience method hiding the internal storage format.
   *
   * @param year Calendar.get(Calendar.YEAR)
   * @param month Calendar.get(Calendar.MONTH) + 1
   * @param dayOfMonth Calendar.get(Calendar.DAY_OF_MONTH)
   */
  public Date(final int year, final int month, final int dayOfMonth) {
    mYear = (short) year;
    mMonth = (byte) month;
    mDay = (byte) dayOfMonth;
  }

  /**
   * constructs a new date from a calendar object.
   *
   * @param cal the calendar to create a date from.
   */
  public Date(final Calendar cal) {
    mYear = (short) cal.get(Calendar.YEAR);
    mMonth = (byte) (cal.get(Calendar.MONTH) + 1);
    mDay = (byte) cal.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Constructs a new date from a date object, ie clones it.
   *
   * @param d the date to clone
   */
  public Date(final Date d) {
    mYear = d.mYear;
    mMonth = d.mMonth;
    mDay = d.mDay;
  }

  /**
   * Creates a new instance from a RandomAccessFile.
   *
   * IMPORTANT: This is needed for loading Date from the
   *            tv data files and cannot be replaced with serialisation.
   *
   * @param in the input to read from
   * @throws IOException if the stream could not be read
   * @throws ClassNotFoundException if the date could not be restored
   * @since 2.2
   * @deprecated since 3.0. use readData instead.
   */
  @Deprecated
  public Date(final DataInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    if (version == 1) { // currently, version==3 is used
      int date = in.readInt();
      long l = (long) date * 24 * 60 * 60 * 1000;
      java.util.Date d = new java.util.Date(l);
      Calendar mCalendar = Calendar.getInstance();
      mCalendar.setTime(d);
      mYear = (short) mCalendar.get(Calendar.YEAR);
      mMonth = (byte) (mCalendar.get(Calendar.MONTH) + 1);
      mDay = (byte) mCalendar.get(Calendar.DAY_OF_MONTH);
    }
    else if (version == 2)
    {
      mYear = (short) in.readInt();
      mMonth = (byte) in.readInt();
      mDay = (byte) in.readInt();
    }
    else
    {
      mYear = in.readShort();
      mMonth = in.readByte();
      mDay = in.readByte();
    }
  }

  /**
   * Creates a new instance from a stream.
   *
   * @param in the input to read from
   * @throws IOException if the stream could not be read
   * @throws ClassNotFoundException if the date could not be restored
   * @deprecated since 3.0, use readData instead
   */
  @Deprecated
  public Date(final ObjectInputStream in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    if (version == 1) { // currently, version==3 is used
      int date = in.readInt();
      long l = (long) date * 24 * 60 * 60 * 1000;
      java.util.Date d = new java.util.Date(l);
      Calendar mCalendar = Calendar.getInstance();
      mCalendar.setTime(d);
      mYear = (short) mCalendar.get(Calendar.YEAR);
      mMonth = (byte) (mCalendar.get(Calendar.MONTH) + 1);
      mDay = (byte) mCalendar.get(Calendar.DAY_OF_MONTH);
    }
    else if (version == 2)
    {
      mYear = (short) in.readInt();
      mMonth = (byte) in.readInt();
      mDay = (byte) in.readInt();
    }
    else
    {
      mYear = in.readShort();
      mMonth = in.readByte();
      mDay = in.readByte();
    }
  }






  /**
   * do not call this method repeatedly! always cache this value because this is
   * a very slow function!
   *
   * @return the current date
   */
  public static Date getCurrentDate() {
    return new Date();
  }

  /**
   * create a date from a value (see getValue).
   *
   * @param value the value to create a date from
   * @return the date
   */
  public static Date createDateFromValue(final long value) {
    short year = (short) (value / 10000L);
    byte month = (byte) (value % 10000L / 100L);
    byte day = (byte) (value % 100L);

    return new Date(year, month, day);
  }


  /**
   * creates a date from a formatted string of the form YYYY&lt;separator&gt;MM&lt;separator&gt;DD.
   *
   * @param date the string to parse.
   * @param separator the separator separating the year, month and day sections of the string
   * @return null if the parsing failed
   */
  public static Date createYYYYMMDD(final String date, final String separator) {
    if (date == null)
    {
      return null;
    }
    String[] s = date.split(separator);
    if (s.length != 3)
    {
      return null;
    }
    try {
      short year = Short.parseShort(s[0]);
      byte month = Byte.parseByte(s[1]);
      byte day = Byte.parseByte(s[2]);
      return new Date(year, month, day);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }


  /**
   * creates a date from a formatted string of the form DD&lt;separator&gt;MM&lt;separator&gt;YYYY.
   *
   * @param date the string to parse.
   * @param separator the separator separating the year, month and day sections of the string
   * @return null if the parsing failed
   */
  public static Date createDDMMYYYY(final String date, final String separator) {
    if (date == null)
    {
      return null;
    }
    String[] s = date.split(separator);
    if (s.length != 3)
    {
      return null;
    }
    try {
      short year = Short.parseShort(s[2]);
      byte month = Byte.parseByte(s[1]);
      byte day = Byte.parseByte(s[0]);
      return new Date(year, month, day);
    } catch(Exception e){
      e.printStackTrace();
    }
    return null;
  }


  /**
   * creates a date from a formatted string of the form YY&lt;separator&gt;MM&lt;separator&gt;DD.
   * this method assumes that the date is in the 21st century, ie 20XX.
   *
   * @param date the string to parse.
   * @param separator the separator separating the year, month and day sections of the string
   * @return null if the parsing failed
   */
  public static Date createYYMMDD(final String date, final String separator) {
    return createYYYYMMDD("20" + date, separator);
  }


  /**
   * @param date
   *          The date to check for days since.
   * @return The days since the given date.
   * @since 0.9.7.4 This method may not return the exactly number of days since
   *        the calculation is confounded by daylight savings time
   *        switchovers... Around midnight the result may not be correct.
   */
  public int getNumberOfDaysSince(final Date date) {
    Calendar cal1 = date.getCalendar();
    java.util.Date utilDate1 = cal1.getTime();
    long millis1 = utilDate1.getTime();

    Calendar cal2 = getCalendar();
    java.util.Date utilDate2 = cal2.getTime();
    long millis2 = utilDate2.getTime();

    int hours = (int) ((millis2 - millis1) / 1000L / 60L / 60L);

    if (hours % 24 > 1)
    {
      return (int) (hours / 24. + 0.99);
    }

    return (int) ((millis2 - millis1) / 1000L / 60L / 60L / 24L);

  }

  /**
   * @return the year (4 digits)
   */
  public int getYear() {
    return mYear;
  }

  /**
   * @return the month (2 digits, starting with 1 for january)
   */
  public int getMonth() {
    return mMonth;
  }

  /**
   * @return the day of month (2 digits, starting with 1)
   */
  public int getDayOfMonth() {
    return mDay;
  }

  /**
   * Returns the week number within the current year.
   * Should not be used since value depends on default locale
   * and thus changes from machine to machine. 
   * Since 3.0 better use getWeekOfYear(Locale locale) instead.
   * @return The week number.
   *
   * @since 2.5.1
   */
  public int getWeekOfYear() {
    Calendar cal = getCalendar();
    return cal.get(Calendar.WEEK_OF_YEAR);
  }
  
  /**
   * Returns the week number within the current year.
   * @param locale The locale to be used
   * @return The week number.
   *
   * @since 3.0
   */
  public int getWeekOfYear(Locale locale) {
    Calendar cal = getCalendar(locale);
    return cal.get(Calendar.WEEK_OF_YEAR);
  }


  /**
   * Writes this instance to a RandomAccessFile.
   *
   * IMPORTANT: This is needed for writing Date to the
   *            tv data files and cannot be replaced with serialisation.
   *
   * @param out the file to write to
   * @throws IOException if something went wrong
   *
   * @since 2.2
   * @deprecated since 3.0, use writeData instead
   */
  @Deprecated
  public void writeToDataFile(final RandomAccessFile out) throws IOException {
    out.writeInt(3); // version
    out.writeShort(mYear);
    out.writeByte(mMonth);
    out.writeByte(mDay);
  }

  /**
   * Writes this instance to a stream.
   *
   * @param out the stream to write to
   * @throws IOException if something went wrong
   * @deprecated since 3.0
   */
  @Deprecated
  public void writeData(final ObjectOutputStream out) throws IOException {
    out.writeInt(3); // version
    out.writeShort(mYear);
    out.writeByte(mMonth);
    out.writeByte(mDay);
  }

  /**
   * A hash code implementation that returns the same code for equal Dates.
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return mYear * 10000 + mMonth * 100 + mDay;
  }

  /**
   * {@inheritDoc}
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj) {

    if (obj instanceof Date) {
      Date d = (Date) obj;
      return d.getDayOfMonth() == getDayOfMonth() && d.getMonth() == getMonth() && d.getYear() == getYear();
    }
    return false;

  }

  /**
   * return the textual representation of this date with abbreviated day of week
   * and abbreviated month name.
   *
   * TODO the description does not fit the code. change code or change description?
   *
   * @return date string
   */
  public String getDateString() {
    return String.valueOf(getValue());
  }

  /**
   * @param longDay false for abbreviated day of week
   * @param longMonth false for abbreviated month name
   * @return date string
   */
  private String getFormattedString(final boolean longDay, final boolean longMonth) {
    Calendar cal = getCalendar();

    SimpleDateFormat day = new SimpleDateFormat(longDay ? "EEEEEE" : "E");
    SimpleDateFormat month = new SimpleDateFormat(longMonth ? "MMMMMM" : "MMM");

    int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

    java.util.Date javaDate = new java.util.Date(getCalendar().getTimeInMillis());

    return LOCALIZER.msg("datePattern", "{0}, {1} {2}", day.format(javaDate), month.format(javaDate), Integer
        .toString(dayOfMonth));
  }

  /**
   * return the textual representation of this date with full day of week
   * and full month name (neither abbreviated).
   *
   * @return date string
   */
  public String getLongDateString() {
    int hashCode = hashCode();
    String result = LONG_DATE_MAP.get(hashCode);
    if (result == null) {
      result = getFormattedString(true, true);
      //check cache size!
      if (LONG_DATE_MAP.size() < DATE_CACHE_SIZE)
      {
        LONG_DATE_MAP.put(hashCode, result);
      }
      else
      {
        LOGGER.warning("Date.DATE_CACHE_SIZE is to small (" + Date.DATE_CACHE_SIZE + ")!");
      }
    }
    return result;
  }

  /**
   * returns the textual representation of this date with abbreviated day of week, but full month.
   * @return date as string
   */
  public String getShortDayLongMonthString() {
    return getFormattedString(false, true);
  }

  /**
   * TODO is that correct? its like hashCode.
   * @return the value as in hashCode
   */
  public long getValue() {
    return mYear * 10000 + mMonth * 100 + mDay;
  }



  /**
   * @return the corresponding calendar for this date
   */
  public java.util.Calendar getCalendar() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.MONTH, mMonth - 1);
    cal.set(Calendar.YEAR, mYear);
    cal.set(Calendar.DAY_OF_MONTH, mDay);

    return cal;
  }
  
  /**
   * @param locale 
   * @return the corresponding calendar for this date
   * @since 3.0
   */
  public java.util.Calendar getCalendar(Locale locale) {
    Calendar cal = Calendar.getInstance(locale);
    cal.set(Calendar.MONTH, mMonth - 1);
    cal.set(Calendar.YEAR, mYear);
    cal.set(Calendar.DAY_OF_MONTH, mDay);

    return cal;
  }

  @Override
  public String toString() {
    return getFormattedString(false, false);
  }

  /**
   * @param days the days to add
   * @return a new created date object
   */
  public Date addDays(final int days) {
    Calendar cal = getCalendar();
    cal.add(Calendar.DAY_OF_MONTH, days);
    return new Date(cal);
  }

  /**
   * {@inheritDoc}
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(final Date otherDate) {
    if (this.mYear < otherDate.mYear) {
      return -1;
    }
    else if (this.mYear > otherDate.mYear) {
      return 1;
    }
    else if (this.mMonth < otherDate.mMonth) {
      return -1;
    }
    else if (this.mMonth > otherDate.mMonth) {
      return 1;
    }
    else if (this.mDay < otherDate.mDay) {
      return -1;
    }
    else if (this.mDay > otherDate.mDay) {
      return 1;
    }
    return 0;
  }

  /**
   * get the day of the week for this date.
   * @return day of week, as Calendar.MONDAY and so on
   * @since 2.6
   */
  public int getDayOfWeek() {
    Calendar cal = getCalendar();
    return cal.get(Calendar.DAY_OF_WEEK);
  }

  /**
   * is this a Monday?
   * @return <code>true</code>, if this is a Monday
   * @since 2.6
   */
  public boolean isFirstDayOfWeek() {
    return getDayOfWeek() == Calendar.MONDAY;
  }
  
  /**
   * is this the first day of the week?
   * @param locale The locale to be used 
   * @return <code>true</code>, if this is the first day of the week
   * @since 3.0
   */
  public boolean isFirstDayOfWeek(Locale locale) {
    Calendar cal = getCalendar(locale);
    return cal.get(Calendar.DAY_OF_WEEK) == cal.getFirstDayOfWeek();
  }





  /**
   * Writes this instance to a DataOutput.
   *
   * @param out the output to write to
   * @throws IOException if something went wrong
   */
  public void writeData(final DataOutput out) throws IOException {
    out.writeInt(3); // version
    out.writeShort(mYear);
    out.writeByte(mMonth);
    out.writeByte(mDay);
  }


  /**
   * reads an instance from a DataInput.
   *
   * @param in the input to read from
   * @return the new created instance
   * @throws IOException if somethin went wrong
   */
  public static Date readData(final DataInput in) throws IOException
  {
    int version = in.readInt();
    short year;
    byte month;
    byte day;
    if (version == 1) {
      // currently, version==3 is used
      int date = in.readInt();
      long l = (long) date * 24 * 60 * 60 * 1000;
      java.util.Date d = new java.util.Date(l);
      Calendar mCalendar = Calendar.getInstance();
      mCalendar.setTime(d);
      year = (short) mCalendar.get(Calendar.YEAR);
      month = (byte) (mCalendar.get(Calendar.MONTH) + 1);
      day = (byte) mCalendar.get(Calendar.DAY_OF_MONTH);
    }
    else if (version == 2)
    {
      year = (short) in.readInt();
      month = (byte) in.readInt();
      day = (byte) in.readInt();
    }
    else
    {
      year = in.readShort();
      month = in.readByte();
      day = in.readByte();
    }
    return new Date(year, month, day);
  }

  public static void resetLocalizer() {
    LOCALIZER = util.ui.Localizer.getLocalizerFor(Date.class);
  }
}
