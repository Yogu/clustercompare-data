/*
 * TV-Browser
 * Copyright (C) 04-2003 Martin Oberhauser (martin_oat@yahoo.de)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * CVS information:
 *  $RCSfile$
 *   $Source$
 *     $Date: 2010-06-28 19:33:48 +0200 (Mon, 28 Jun 2010) $
 *   $Author: bananeweizen $
 * $Revision: 6662 $
 */
package util.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tvbrowser.core.Settings;
import tvbrowser.core.plugin.PluginProxy;
import tvbrowser.core.plugin.PluginProxyManager;
import tvbrowser.core.plugin.PluginStateListener;
import tvbrowser.extras.common.InternalPluginProxyIf;
import tvbrowser.extras.common.InternalPluginProxyList;
import tvbrowser.extras.favoritesplugin.FavoritesPluginProxy;
import tvbrowser.extras.favoritesplugin.core.Favorite;
import tvbrowser.extras.favoritesplugin.dlgs.FavoriteTreeModel;
import util.io.IOUtilities;
import util.misc.StringPool;
import util.program.ProgramUtilities;
import util.settings.ProgramPanelSettings;
import devplugin.ContextMenuIf;
import devplugin.ImportanceValue;
import devplugin.Marker;
import devplugin.Plugin;
import devplugin.PluginAccess;
import devplugin.Program;
import devplugin.ProgramFieldType;
import devplugin.ProgramInfoHelper;

/**
 * A ProgramPanel is a JComponent representing a single program.
 *
 * @author Martin Oberhauser
 * @author Til Schneider, www.murfman.de
 */
public class ProgramPanel extends JComponent implements ChangeListener, PluginStateListener {

  private static final Logger mLog = java.util.logging.Logger
      .getLogger(TextAreaIcon.class.getName());

  private static final util.ui.Localizer mLocalizer = util.ui.Localizer
      .getLocalizerFor(ProgramPanel.class);

  private static final boolean USE_FULL_HEIGHT = true;

  private static final Composite NORMAL_COMPOSITE = AlphaComposite.SrcOver;
  private static final Composite PALE_COMPOSITE = AlphaComposite.getInstance(
      AlphaComposite.SRC_OVER, 0.5F);

  /** The title font. */
  private static Font mTitleFont;
  /** The time font. */
  private static Font mTimeFont;
  /** The normal font */
  private static Font mNormalFont;
  /**
   * the font size offset for the currently selected dynamic font size
   */
  private static int fontSizeOffset = 0;

  private static int columnWidthOffset = 0;

  /** The width of the left part (the time). */
  private static int WIDTH_LEFT = -1;
  /** The width of the right part (the title and short info). */
  private static int WIDTH_RIGHT = Settings.propColumnWidth.getInt() - WIDTH_LEFT;
  /** The total width. */
  private static int WIDTH_TOTAL = WIDTH_LEFT + WIDTH_RIGHT;

  /** Formatter for the Time-String */
  private static final TimeFormatter TIME_FORMATTER = new TimeFormatter();

  /**
   * horizontal distance between format icons
   */
  private static final int ICON_DISTANCE_X = 2;

  /**
   * vertical distance between format icons
   */
  private static final int ICON_DISTANCE_Y = 2;

  /** The height. */
  private int mHeight = 0;
  /**
   * The preferred height.
   * <p>
   * It's the height the panel has with a maximum of 3 information rows.
   */
  private int mPreferredHeight = 0;
  /** The start time as String. */
  private String mProgramTimeAsString;
  /** The icon used to render the title. */
  private TextAreaIcon mTitleIcon;
  /** The icon used to render the description. */
  private TextAreaIcon mDescriptionIcon;

  /** The icon used to paint the picture with */
  private PictureAreaIcon mPictureAreaIcon;

  /** The icons to show on the left side under the start time. */
  private Icon[] mIconArr;
  /** The program. */
  private Program mProgram;

  /** Color of the Text */
  private Color mTextColor = Settings.propProgramPanelForegroundColor.getColor();

  /** Panel under a Mouse ? */
  private boolean mMouseOver = false;

  private boolean mIsSelected = false;

  /**
   * this panel has been changed by third party, needs update before painting
   */
  private boolean mHasChanged = false;

  /** Orientation of progress bar */
  private int mAxis = ProgramPanelSettings.Y_AXIS;

  /** The vertical gap between the programs */
  private static int V_GAP = 5;

  private ProgramPanelSettings mSettings;

  private boolean mPaintExpiredProgramsPale = true;

  /**
   * rectangle of the format icon area, used for tooltip calculation
   */
  private Rectangle mInfoIconRect;

  /** The importance of this program */
  private byte mProgramImportance;

  /**
   * Creates a new instance of ProgramPanel.
   */
  public ProgramPanel() {
    this(new ProgramPanelSettings(Settings.propPictureType.getInt(), Settings.propPictureStartTime.getInt(), Settings.propPictureEndTime.getInt(), false, Settings.propIsPictureShowingDescription.getBoolean(), Settings.propPictureDuration.getInt(), Settings.propPicturePluginIds.getStringArray()));
  }

  /**
   * Creates a new instance of ProgramPanel.
   * @param settings The settings for this program panel.
   *
   * @since 2.2.2
   */
  public ProgramPanel(ProgramPanelSettings settings) {
    setToolTipText("");
    mSettings = settings;
    mAxis = settings.getAxis();

    if (mTitleFont == null) {
      updateFonts();
    }

    calculateWidth();

    mTitleIcon = new TextAreaIcon(null, mTitleFont, WIDTH_RIGHT - 5);
    mDescriptionIcon = new TextAreaIcon(null, mNormalFont, WIDTH_RIGHT - 5);
    mDescriptionIcon.setMaximumLineCount(3);

    setBackground(UIManager.getColor("programPanel.background"));
  }

  /**
   * Creates a new instance of ProgramPanel.
   *
   * @param prog
   *          The program to show in this panel.
   */
  public ProgramPanel(Program prog) {
    this();
    setProgram(prog);
  }

  /**
   * Creates a new instance of ProgramPanel.
   *
   * @param prog
   *          The program to show in this panel.
   * @param settings The settings for this program panel.
   *
   * @since 2.2.2
   */
  public ProgramPanel(Program prog, ProgramPanelSettings settings) {
    this(settings);
    setProgram(prog);
  }

  /**
   * Creates a new instance of ProgramPanel.
   *
   * @param prog
   *          The program to show in this panel.
   * @param axis
   *          Orientation of ProgressBar (X_AXIS/Y_AXIS)
   * @param settings The settings for this program panel.
   *
   * @since 2.2.2
   */
  public ProgramPanel(Program prog, int axis, ProgramPanelSettings settings) {
    this(settings);
    mAxis = axis;
    setProgram(prog);
  }

  private void calculateWidth() {
    if (WIDTH_LEFT == -1) {
      // distance between time and title shall match title font settings, but at
      // most n pixels
      int distance = getFontMetrics(mTitleFont).stringWidth("n");
      if (distance > 7) {
        distance = 7;
      }
      WIDTH_LEFT = getFontMetrics(mTimeFont).stringWidth(
          TIME_FORMATTER.formatTime(23, 59))
          + distance;
      WIDTH_RIGHT = Settings.propColumnWidth.getInt() + columnWidthOffset
          - WIDTH_LEFT;
      WIDTH_TOTAL = WIDTH_LEFT + WIDTH_RIGHT;
    }
  }

  /**
   * Reloads the font settings and sets a dynamic font size
   * @param newOffset increase or decrease compared to standard font size
   * @since 2.6
   */
  public static boolean updateFonts(int newOffset) {
    Font oldTitleFont = mTitleFont;
    Font oldTimeFont = mTimeFont;
    Font oldNormalFont = mNormalFont;
    boolean useDefaults = Settings.propUseDefaultFonts.getBoolean();
    if (useDefaults) {
      mTitleFont = Settings.propProgramTitleFont.getDefault();
      mTimeFont = Settings.propProgramTimeFont.getDefault();
      mNormalFont = Settings.propProgramInfoFont.getDefault();
    } else {
      mTitleFont = Settings.propProgramTitleFont.getFont();
      mTimeFont = Settings.propProgramTimeFont.getFont();
      mNormalFont = Settings.propProgramInfoFont.getFont();
    }
    if (newOffset == 0) {
      fontSizeOffset = 0;
    }
    else {
      fontSizeOffset += newOffset;
    }
    if (fontSizeOffset != 0) {
      mTitleFont = getDynamicFontSize(mTitleFont, fontSizeOffset);
      mTimeFont = getDynamicFontSize(mTimeFont, fontSizeOffset);
      mNormalFont = getDynamicFontSize(mNormalFont, fontSizeOffset);
    }
    // if the font didn't change, avoid updates
    if (mTitleFont.equals(oldTitleFont) && mTimeFont.equals(oldTimeFont) && mNormalFont.equals(oldNormalFont)) {
      return false;
    }
    // reset width of the left part, will be recalculated during first forceRepaint
    WIDTH_LEFT = -1;
    return true;
  }

private static Font getDynamicFontSize(Font font, int offset) {
  float size = font.getSize2D() + offset;
  if (size < 4) {
    size = 4;
  }
  return font.deriveFont(size);
}

  /**
   * Reloads the font settings and sets the dynamic font size
   * to standard font size again
   */
  public static void updateFonts() {
    updateFonts(0);
  }

  /**
   * Change the settings of this panel.
   *
   * @param settings The settings of this panel.
   */
  public void setProgramPanelSettings(ProgramPanelSettings settings) {
    mSettings = settings;
  }

  /**
   * Repaints the complete panel.
   */
  public void forceRepaint() {
    calculateWidth();
    mTitleIcon = new TextAreaIcon(null, mTitleFont, WIDTH_RIGHT - 5);
    mDescriptionIcon = new TextAreaIcon(null, mNormalFont, WIDTH_RIGHT - 5);
    mDescriptionIcon.setMaximumLineCount(Settings.propProgramPanelMaxLines
        .getInt());
    mProgram.validateMarking();
    Program p = mProgram;
    mProgram = null;
    setProgram(p);
  }

  /**
   * (Re)Loads the column width settings.
   */
  public static void updateColumnWidth() {
    updateColumnWidth(0);
  }

  public static int updateColumnWidth(int newOffset) {
    if (newOffset == 0) {
      columnWidthOffset = 0;
    }
    else {
      columnWidthOffset += (20 * newOffset);
    }
    int columnWidth = Settings.propColumnWidth.getInt() + columnWidthOffset;
    if (columnWidth < Settings.MIN_COLUMN_WIDTH) {
      columnWidthOffset = Settings.MIN_COLUMN_WIDTH - Settings.propColumnWidth.getInt();
    }
    if (columnWidth > Settings.MAX_COLUMN_WIDTH) {
      columnWidthOffset = Settings.MAX_COLUMN_WIDTH - Settings.propColumnWidth.getInt();
    }
    columnWidth = Settings.propColumnWidth.getInt() + columnWidthOffset;
    WIDTH_RIGHT = columnWidth - WIDTH_LEFT;
    WIDTH_TOTAL = WIDTH_LEFT + WIDTH_RIGHT;
    return columnWidth;
  }

  /**
   * Gets the preferred height.
   * <p>
   * It's the height the panel has with a maximum of 3 information rows.
   *
   * @return The preferred height.
   */
  public int getPreferredHeight() {
    return mPreferredHeight;
  }

  /**
   * Sets the height of this panel
   *
   * @param height
   */
  public void setHeight(int height) {
    if (mHeight != height) {
      setProgram(getProgram(), height);
      mHeight = height;
    }
  }

  public void setMaximumHeight() {
    setProgram(getProgram(), 10000);
  }

  /**
   * Gets the height.
   *
   * @return The height.
   */
  public int getHeight() {
    return mHeight;
  }

  /**
   * Sets the program this panel shows.
   *
   * @param program
   *          The program to show in this panel.
   */
  public void setProgram(Program program) {
    setProgram(program, -1);
  }

  /**
   * Sets the program this panel shows.
   *
   * @param program
   *          The program to show in this panel.
   * @param maxHeight
   *          The maximum height the program should have (in pixels).
   */
  public void setProgram(Program program, int maxHeight) {
    Program oldProgram = mProgram;
    mProgram = program;

    if (Settings.propProgramTableCutTitle.getBoolean()) {
      mTitleIcon.setMaximumLineCount(Settings.propProgramTableCutTitleLines
          .getInt());
    } else {
      mTitleIcon.setMaximumLineCount(-1);
    }
    mDescriptionIcon.setMaximumLineCount(-1);

    boolean programChanged = (oldProgram != program);
    if (programChanged) {
      // Get the start time, filter duplicate strings
      mProgramTimeAsString = StringPool.getString(program.getTimeString());

      // Set the new title
      mTitleIcon.setText(program.getTitle());

      if(mProgram.getProgramState() == Program.IS_VALID_STATE) {
        programHasChanged();
      }
    }

    boolean dontShow = true;

    if(mSettings.isShowingPictureForPlugins()) {
      String[] pluginIds = mSettings.getPluginIds();
      Marker[] markers = mProgram.getMarkerArr();

      if(markers != null && pluginIds != null) {
        for (Marker marker : markers) {
          for (String pluginId : pluginIds) {
            if(marker.getId().compareTo(pluginId) == 0) {
              dontShow = false;
              break;
            }
          }
        }
      }
    }

    // Create the picture area icon
    int length = program.getLength();
    if (!mSettings.isShowingOnlyDateAndTitle()
        && mProgram.hasFieldValue(ProgramFieldType.PICTURE_TYPE)
        && (
        mSettings.isShowingPictureEver() || !dontShow ||
        (mSettings.isShowingPictureInTimeRange() &&
         !ProgramUtilities.isNotInTimeRange(mSettings.getPictureTimeRangeStart(),mSettings.getPictureTimeRangeEnd(),program)) ||
         (mSettings
            .isShowingPictureForDuration() && mSettings.getDuration() <= length)
         )) {
      mPictureAreaIcon = new PictureAreaIcon(program,mNormalFont, WIDTH_RIGHT - 4, mSettings.isShowingPictureDescription(), true, false);
    } else {
      mPictureAreaIcon = new PictureAreaIcon();
    }

    // Calculate the maximum description lines
    int titleHeight = mTitleIcon.getIconHeight();
    int maxDescLines;
    if (Settings.propProgramPanelShortDurationActive.getBoolean()
        && length >= 0
        && length <= Settings.propProgramPanelShortDurationMinutes.getInt()) {
      maxDescLines = 0;
      mDescriptionIcon.setText("");
      mDescriptionIcon.setMaximumLineCount(0);
    } else {
      maxDescLines = Settings.propProgramPanelMaxLines.getInt();
    }
    int additionalHeight = Settings.propProgramPanelUsesExtraSpaceForMarkIcons.getBoolean() && program.getMarkerArr().length > 0 ? 16 : 0;

    if (maxHeight != -1) {
      maxDescLines = (maxHeight - titleHeight - mPictureAreaIcon.getIconHeight() - additionalHeight - V_GAP) / mNormalFont.getSize();
    }

    if (programChanged
        || (maxDescLines != mDescriptionIcon.getMaximumLineCount())) {
      int descHeight = 0;
      // (Re)set the description text
      if (!mSettings.isShowingOnlyDateAndTitle() && maxDescLines > 0) {
        mDescriptionIcon.setMaximumLineCount(maxDescLines);
        ProgramFieldType[] infoFieldArr = Settings.propProgramInfoFields
            .getProgramFieldTypeArray();
        Reader infoReader = new MultipleFieldReader(program, infoFieldArr);
        try {
          mDescriptionIcon.setText(infoReader);
        } catch (IOException exc) {
          mLog.log(Level.WARNING, "Reading program info failed for " + program,
              exc);
        }
        descHeight = mDescriptionIcon.getIconHeight();
      } else {
        descHeight = 0;
      }

      // Calculate the height
      mHeight = titleHeight + descHeight + mPictureAreaIcon.getIconHeight() + additionalHeight + V_GAP;
      setPreferredSize(new Dimension(WIDTH_TOTAL, mHeight));

      // Calculate the preferred height
      mPreferredHeight = titleHeight + (maxDescLines * mNormalFont.getSize()) + mPictureAreaIcon.getIconHeight() + additionalHeight + V_GAP;

      if (mHeight < mPreferredHeight) {
        mPreferredHeight = mHeight;
      }
    }

    if (isShowing()) {
      if (oldProgram != null) {
        oldProgram.removeChangeListener(this);
      }
      mProgram.addChangeListener(this);
      revalidate();
      repaint();
    }
  }

  /**
   * Gets the plugin icons for a program to show them on the left below the start time.
   *
   * @param program
   *          The program to get the icons for.
   * @return The icons for the program.
   */
  private Icon[] getPluginIcons(final Program program) {
    String[] iconPluginArr = Settings.propProgramTableIconPlugins
        .getStringArray();

    if (program.getProgramState() != Program.IS_VALID_STATE || (iconPluginArr == null) || (iconPluginArr.length == 0)) {
      return new Icon[0];
    } else {
      PluginProxyManager mng = PluginProxyManager.getInstance();
      ArrayList<Icon> iconList = new ArrayList<Icon>();
      int info = program.getInfo();
      int[] infoBits = ProgramInfoHelper.getInfoBits();
      Icon[] infoIcons = ProgramInfoHelper.getInfoIcons();
      for (int pluginIdx = 0; pluginIdx < iconPluginArr.length; pluginIdx++) {

        if (iconPluginArr[pluginIdx].equals(Settings.INFO_ID)) {
          // old style format (with all icons)
          if ((info != -1) && (info != 0)) {
            for (int i = 0; i < infoBits.length; i++) {
              if (ProgramInfoHelper.bitSet(info,
                  infoBits[i])
                  && (infoIcons[i] != null)) {
                iconList.add(infoIcons[i]);
              }
            }
          }
        } else if (iconPluginArr[pluginIdx].equals(Settings.PICTURE_ID)) {
          // picture icon
          if (mProgram.hasFieldValue(ProgramFieldType.PICTURE_TYPE)) {
            iconList.add(new ImageIcon("imgs/Info_HasPicture.png"));
          }
        } else if (iconPluginArr[pluginIdx].startsWith("FORMAT")) {
          // new style format (each icon separately)
          if ((info != -1) && (info != 0)) {
            int index = Integer.valueOf(iconPluginArr[pluginIdx].substring(7));
            if (ProgramInfoHelper.bitSet(info,
                infoBits[index])
                && (infoIcons[index] != null)) {
              iconList.add(infoIcons[index]);
            }
          }
        } else {
          // plugin icons
          PluginProxy plugin = mng.getPluginForId(iconPluginArr[pluginIdx]);

          // Check whether this entry still uses the old class name
          if (plugin == null) {
            String asId = "java." + iconPluginArr[pluginIdx];
            plugin = mng.getPluginForId(asId);

            if (plugin != null) {
              // It was the old class name, not an ID
              // -> Change the class name to an ID and save it
              iconPluginArr[pluginIdx] = asId;
              Settings.propProgramTableIconPlugins
                  .setStringArray(iconPluginArr);
            }
          }

          // Now add the icons
          if ((plugin != null) && plugin.isActivated()) {
            Icon[] iconArr = plugin.getProgramTableIcons(program);
            if (iconArr != null) {
              // Add the icons
              for (Icon icon : iconArr) {
                iconList.add(icon);
              }
            }
          }
        }
      }

      // Convert the list to an array and return it
      return iconList.toArray(new Icon[iconList.size()]);
    }
  }

  private byte getProgramImportance(final Program program) {
    if (program.getProgramState() == Program.IS_VALID_STATE &&
        Settings.propProgramPanelAllowTransparency.getBoolean() &&
        !mSettings.isIgnoringProgramImportance()) {
      int count = 0;
      int addValue = 0;

      PluginProxy[] plugins = PluginProxyManager.getInstance().getActivatedPlugins();

      for(PluginProxy plugin : plugins) {
        ImportanceValue value = plugin.getImportanceValueForProgram(program);

        if(value.getWeight() > 0 && value.getTotalImportance() >= Program.MIN_MARK_PRIORITY) {
          count += value.getWeight();
          addValue += value.getTotalImportance();
        }
      }

      if(count > 0) {
        return (byte)Math.max(addValue/count, Program.MIN_MARK_PRIORITY);
      }
    }

    return Program.MAX_PROGRAM_IMPORTANCE;
  }
  /**
   * Paints the component.
   *
   * @param g
   *          The graphics context to paint to.
   */
  public void paintComponent(Graphics g) {
    // lazy update of plugin icons and layout
    if (mHasChanged) {
      mIconArr = getPluginIcons(mProgram);
      mProgramImportance = getProgramImportance(mProgram);
      mHasChanged = false;
    }

    /* Prevent accidentally set program importance to take effect */
    if(mSettings.isIgnoringProgramImportance() || mProgram.getProgramState() == Program.WAS_DELETED_STATE) {
      mProgramImportance = Program.MAX_PROGRAM_IMPORTANCE;
    }

    /* This is for debugging of the marking problem after an data update */
    if(mProgram.getProgramState() == Program.WAS_DELETED_STATE) {
      setForeground(Color.red);
      mTextColor = Color.red;
    } else if(mProgram.getProgramState() == Program.WAS_UPDATED_STATE) {
      setForeground(Color.blue);
      mTextColor = Color.blue;
    }

    int width = getWidth();
    if (getTextIconWidth(width) != mDescriptionIcon.getIconWidth()) {
      recreateTextIcons(width);
    }

    int height = USE_FULL_HEIGHT ? getHeight() : mHeight;
    Graphics2D grp = (Graphics2D) g;

    // if program table is anti-aliased, then this program panel too
    if (Settings.propEnableAntialiasing.getBoolean()) {
      grp.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    // Draw the background if this program is on air
    if (mProgram.isOnAir()) {
      int minutesAfterMidnight = IOUtilities.getMinutesAfterMidnight();
      int progLength = mProgram.getLength();
      int startTime = mProgram.getStartTime();
      int elapsedMinutes = minutesAfterMidnight - startTime;
      if (elapsedMinutes < 0) {
        // The next day has begun -> we have to add 24 * 60 minutes
        // Example: Start time was 23:50 = 1430 minutes after midnight
        // now it is 0:03 = 3 minutes after midnight
        // elapsedMinutes = (24 * 60) + 3 - 1430 = 13 minutes
        elapsedMinutes += 24 * 60;
      }

      // elapsed minutes can not be larger than run time
      if (progLength > 0) {
        if (elapsedMinutes > progLength) {
          mLog.severe("paint program panel: elapsed minutes to large; sT=" + startTime + " mAM=" + minutesAfterMidnight + " pL=" + progLength + " eM=" + elapsedMinutes);
        }
        elapsedMinutes = Math.min(elapsedMinutes, progLength);
      }

      int borderWidth = Settings.propProgramTableOnAirProgramsShowingBorder
          .getBoolean() ? 1 : 0;
      if (mAxis == ProgramPanelSettings.X_AXIS) {
        // horizontal filling panel
        int progressX = 0;
        if (progLength > 0) {
          progressX = elapsedMinutes * width / progLength;
        }

        Color c = Settings.propProgramTableColorOnAirDark.getColor();
        grp.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(c.getAlpha()*mProgramImportance/10.)));

        int fillWidth = progressX - borderWidth;
        if (fillWidth > 0) {
          grp.fillRect(borderWidth, borderWidth, fillWidth, height - borderWidth);
        }
        c = Settings.propProgramTableColorOnAirLight.getColor();
        grp.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(c.getAlpha()*mProgramImportance/10.)));

        fillWidth = width - progressX - borderWidth * 2;
        if (fillWidth > 0) {
          grp.fillRect(progressX, borderWidth, fillWidth, height - borderWidth);
        }

      } else {
        // vertical filling panel
        int progressY = 0;
        if (progLength > 0) {
          progressY = elapsedMinutes * height / progLength;
        }

        Color c = Settings.propProgramTableColorOnAirDark.getColor();
        grp.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(c.getAlpha()*mProgramImportance/10.)));

        int fillHeight = progressY - borderWidth;

        if (fillHeight > height) {
          mLog.severe("paint program panel: fill height 1 to large");
          fillHeight = height;
        }

        if (fillHeight > 0) {
          grp.fillRect(borderWidth, borderWidth, width - borderWidth * 2, fillHeight);
        }

        c = Settings.propProgramTableColorOnAirLight.getColor();
        grp.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(c.getAlpha()*mProgramImportance/10.)));

        fillHeight = height - progressY - borderWidth;

        if (fillHeight > height) {
          mLog.severe("paint program panel: fill height 2 to large");
          fillHeight = height;
        }

        if (fillHeight > 0) {
          grp.fillRect(borderWidth, progressY, width - borderWidth * 2, fillHeight);
        }
      }
      if (Settings.propProgramTableOnAirProgramsShowingBorder.getBoolean()) {
        grp.draw3DRect(0, 0, width - 1, height - 1, true);
      }
    }

    // If there are plugins that have marked the program -> paint the background
    Marker[] markedByPluginArr = mProgram.getMarkerArr();
    if (markedByPluginArr.length != 0) {
      Color c = Plugin.getPluginManager().getTvBrowserSettings().getColorForMarkingPriority(mProgram.getMarkPriority());

      if(c == null) {
        c = Settings.propProgramPanelMarkedMinPriorityColor.getColor();
      }

      int alphaValue = (int)(c.getAlpha()*mProgramImportance/10.);

      if(mProgram.isExpired()) {
        grp.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(alphaValue*6/10.)));
      }
      else {
        grp.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), alphaValue));
      }

      if(mProgram.getMarkPriority() > Program.NO_MARK_PRIORITY) {
        if(Settings.propProgramPanelWithMarkingsShowingBoder.getBoolean()) {
          grp.fill3DRect(0, 0, width, height, true);
        }
        else {
          grp.fillRect(0, 0, width, height);
        }
      }
    }

    if (mMouseOver || mIsSelected) {
      Color test = Settings.propProgramTableMouseOverColor.getColor();
      if (mIsSelected) {
        test = Settings.propKeyboardSelectedColor.getColor();
      }
      grp.setColor(test);
      grp.fillRect(0, 0, width - 1, height - 1);

      Stroke str = grp.getStroke();
      Color col = grp.getColor();
      float[] dash = { 2.0f };
      int lineWidth = 1;
      BasicStroke dashed = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT,
          BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
      grp.setColor(Color.BLACK);
      grp.setStroke(dashed);

      grp.drawRect(lineWidth - 1, lineWidth - 1, width - lineWidth, height - lineWidth);

      grp.setStroke(str);
      grp.setColor(col);
    }

    // Draw all the text
    if (mPaintExpiredProgramsPale && mProgram.isExpired()) {
      Color c = new Color(Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), (int)(Color.gray.getAlpha()*mProgramImportance/10.));

      setForeground(c);
      grp.setColor(c);
    } else {
      Color c = new Color(mTextColor.getRed(), mTextColor.getGreen(), mTextColor.getBlue(), (int)(mTextColor.getAlpha()*mProgramImportance/10.));

      setForeground(c);
      grp.setColor(c);
    }
    grp.setFont(ProgramPanel.mTimeFont);
    grp.drawString(mProgramTimeAsString, 1, mTimeFont.getSize());

    mTitleIcon.paintIcon(this, grp, WIDTH_LEFT, 0);

    if (!mSettings.isShowingOnlyDateAndTitle()) {
      mPictureAreaIcon.paintIcon(this,grp, WIDTH_LEFT, mTitleIcon.getIconHeight());

      if (mHeight >= mPreferredHeight) {
        mDescriptionIcon.paintIcon(this, grp, WIDTH_LEFT, mTitleIcon
            .getIconHeight() + mPictureAreaIcon.getIconHeight());
      }

      // Paint the icons pale if the program is expired
      if (mPaintExpiredProgramsPale && mProgram.isExpired() && mProgramImportance == 10) {
        grp.setComposite(PALE_COMPOSITE);
      }
      else if (mProgramImportance != 10) {
        grp.setComposite(AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, mProgramImportance/10F));
      }
    }


    // paint the icons of the plugins that have marked the program (lower right corner)
    int x = width - 1;
    int y = mTitleIcon.getIconHeight() + mDescriptionIcon.getIconHeight()
        + mPictureAreaIcon.getIconHeight() + 18;
    y = Math.min(y, height - 1);
    for (Marker marker: markedByPluginArr) {
      Icon[] icons = marker.getMarkIcons(mProgram);
      if (icons != null) {
        for(int i = icons.length - 1; i >= 0 ; i--) {
          x -= icons[i].getIconWidth();

          icons[i].paintIcon(this, grp, x, y - icons[i].getIconHeight());
        }
      }
    }

    // Paint the icons on the left side
    if (mIconArr != null) {
      x = ICON_DISTANCE_X;
      y = mTimeFont.getSize() + 3;
      Point iconsTopLeft = new Point(x, y);

      // calculate height with double column layout
      int sumHeights = -ICON_DISTANCE_Y;
      int maxWidth = 0;
      int rowWidth = 0;
      for (int i = 0; i < mIconArr.length; i++) {
        sumHeights += mIconArr[i].getIconHeight() + ICON_DISTANCE_Y;
        if (i % 2 == 0) {
          rowWidth = mIconArr[i].getIconWidth();
        }
        else {
          rowWidth += mIconArr[i].getIconWidth();
        }
        if (rowWidth > maxWidth) {
          maxWidth = rowWidth;
        }
      }

      // single column of icons
      int colCount = 1;
      // layout icons in pairs
      if ((y + sumHeights >= mHeight) && (maxWidth + 3 * ICON_DISTANCE_X < WIDTH_LEFT)) {
        colCount = 2;
      }
      int iconHeight = 0;
      int currentX = x;
      for (int i = 0; i < mIconArr.length; i++) {
        Icon icon = mIconArr[i];
        boolean nextColumn = (colCount == 1) || (i % 2 == 0);
        if (nextColumn) {
          currentX = x;
          iconHeight = icon.getIconHeight();
        }
        else {
          iconHeight = Math.max(iconHeight, icon.getIconHeight());
        }
        if ((y + iconHeight) < mHeight) {
          icon.paintIcon(this, grp, currentX, y);
        }
        if (nextColumn) {
          currentX += icon.getIconWidth() + ICON_DISTANCE_X;
        }
        if (!nextColumn || (colCount == 1)) {
          y += iconHeight + ICON_DISTANCE_Y;
        }
      }
      // remember the size of this area for tooltip
      if (colCount == 1) {
        mInfoIconRect = new Rectangle(iconsTopLeft.x, iconsTopLeft.y, currentX
            - iconsTopLeft.x, y - iconsTopLeft.y);
      } else {
        mInfoIconRect = new Rectangle(iconsTopLeft.x, iconsTopLeft.y, maxWidth,
            y - iconsTopLeft.y + iconHeight);
      }
    }

    // Reset the old composite
    if (mPaintExpiredProgramsPale && mProgram.isExpired()) {
      grp.setComposite(NORMAL_COMPOSITE);
    }
  }

  /**
   * Called when the panel is added to a container.
   * <p>
   * registers the panel as ChangeListener at the program.
   */
  public void addNotify() {
    super.addNotify();
    mProgram.addChangeListener(this);
    PluginProxyManager.getInstance().addPluginStateListener(this);
  }

  /**
   * Called when the panel is added to a container.
   * <p>
   * removes the panel as ChangeListener from the program.
   */
  public void removeNotify() {
    super.removeNotify();
    mProgram.removeChangeListener(this);
    PluginProxyManager.getInstance().removePluginStateListener(this);
  }

  /**
   * Gets the program object of this ProgramPanel.
   *
   * @return the program object of this ProgramPanel.
   */
  public Program getProgram() {
    return mProgram;
  }

  /**
   * Adds a MouseListener that shows the plugin context menu when the user does
   * a right click on the program panel.
   *
   * @param caller
   *          The ContextMenuIf to exclude from the context menu. When
   *          <code>null</code> no ContextMenuIf is excluded.
   */
  public void addPluginContextMenuMouseListener(final ContextMenuIf caller) {
    addMouseListener(new MouseAdapter() {
      private Thread mLeftClickThread;
      private boolean mPerformingSingleClick = false;

      private Thread mMiddleSingleClickThread;
      private boolean mPerformingMiddleSingleClick = false;

      public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
          showPopup(e, caller);
        }
      }

      public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
          showPopup(e, caller);
        }
      }

      public void mouseClicked(final MouseEvent evt) {
        if (SwingUtilities.isLeftMouseButton(evt) && (evt.getClickCount() == 1) && evt.getModifiersEx() == 0) {
          mLeftClickThread = new Thread("Single click") {
            public void run() {
              try {
                mPerformingSingleClick = false;
                sleep(Plugin.SINGLE_CLICK_WAITING_TIME);
                mPerformingSingleClick = true;

                Plugin.getPluginManager().handleProgramSingleClick(mProgram, caller);
                mPerformingSingleClick = false;
              } catch (InterruptedException e) { // ignore
              }
            }
          };

          mLeftClickThread.setPriority(Thread.MIN_PRIORITY);
          mLeftClickThread.start();
        }
        else if (SwingUtilities.isLeftMouseButton(evt) && (evt.getClickCount() == 2) && evt.getModifiersEx() == 0) {
          if(!mPerformingSingleClick && mLeftClickThread != null && mLeftClickThread.isAlive()) {
            mLeftClickThread.interrupt();
          }

          if(!mPerformingSingleClick) {
            Plugin.getPluginManager().handleProgramDoubleClick(mProgram, caller);
          }
        }
        else if (SwingUtilities.isMiddleMouseButton(evt) && (evt.getClickCount() == 1)) {
          mMiddleSingleClickThread = new Thread("Single click") {
            public void run() {
              try {
                mPerformingMiddleSingleClick = false;
                sleep(Plugin.SINGLE_CLICK_WAITING_TIME);
                mPerformingMiddleSingleClick = true;

                Plugin.getPluginManager().handleProgramMiddleClick(mProgram, caller);
                mPerformingMiddleSingleClick = false;
              } catch (InterruptedException e) { // ignore
              }
            }
          };

          mMiddleSingleClickThread.setPriority(Thread.MIN_PRIORITY);
          mMiddleSingleClickThread.start();
        }
        else if (SwingUtilities.isMiddleMouseButton(evt) && (evt.getClickCount() == 2)) {
          if(!mPerformingMiddleSingleClick && mMiddleSingleClickThread != null && mMiddleSingleClickThread.isAlive()) {
            mMiddleSingleClickThread.interrupt();
          }

          if(!mPerformingMiddleSingleClick) {
            Plugin.getPluginManager().handleProgramMiddleDoubleClick(mProgram, caller);
          }
        }
      }
    });
  }

  /**
   * Shows the Popup
   *
   * @param evt
   *          Event for X/Y-Coordinates
   * @param caller
   *          ContextMenuIf that called this
   */
  private void showPopup(MouseEvent evt, ContextMenuIf caller) {
    if (SwingUtilities.isRightMouseButton(evt)) {
      JPopupMenu menu = PluginProxyManager.createPluginContextMenu(mProgram,
          caller);
      menu.show(evt.getComponent(), evt.getX() - 15, evt.getY() - 15);
    }
  }

  /**
   * Should be called, when the program has changed.
   * <p>
   * If you use this program panel directly (this is the case, when you have
   * added it into a JPanel), you don't have to call this method. This is done
   * automatically.
   * <p>
   * But if you use this panel just as renderer (e.g. for a list) you have to
   * register at the programs as ChangeListener and call this method when
   * {@link ChangeListener#stateChanged(javax.swing.event.ChangeEvent)} was
   * called.
   *
   * @see Program#addChangeListener(ChangeListener)
   */
  public void programHasChanged() {
    // we need to remember changed state so we can update the plugin icons
    // before painting
    mHasChanged = true;
  }

  // implements ChangeListener

  /**
   * Called when the state of the program has changed.
   * <p>
   * repaints the panel.
   *
   * @param evt
   *          The event describing the change.
   */
  public void stateChanged(ChangeEvent evt) {
    if (mProgram.equals(evt.getSource())) {
      programHasChanged();
      repaint();
    }
  }

  /**
   * Sets the Color of the Text
   *
   * @param col
   *          Color of the Text
   */
  public void setTextColor(Color col) {
    mTextColor = col;
  }

  /**
   * Returns the Color of the Text
   *
   * @return Color of the Text
   */
  public Color getTextColor() {
    return mTextColor;
  }

  /**
   * Paints the ProgramPanel
   *
   * @param mouse
   *          under a Mouse and needs highlight?
   * @param isSelected
   *          IsSelected program?
   * @param g
   *          Graphics-Object
   *
   */
  public void paint(boolean mouse, boolean isSelected, Graphics g) {
    mMouseOver = mouse;
    mIsSelected = isSelected;
    super.paint(g);
  }

  /**
   * @return The smallest height possible.
   */
  public int getMinimumHeight() {
    return mTitleIcon.getIconHeight() + mPictureAreaIcon.getIconHeight() + 3 + (Settings.propProgramPanelUsesExtraSpaceForMarkIcons.getBoolean() && mProgram.getMarkerArr().length > 0 ? 16 : 0);
  }

  /**
   * Sets if expired programs should be painted pale.
   * <p>
   * @param value <code>True</code> if expired programs should be painted pale,
   * <code>false</code> otherwise.
   * @since 2.7
   */
  public void setPaintExpiredProgramsPale(boolean value) {
    mPaintExpiredProgramsPale = value;
  }

  public void pluginActivated(PluginProxy plugin) {
    if (plugin.getProgramTableIcons(Plugin.getPluginManager().getExampleProgram()) != null) {
      updatePrograms();
    }
  }

  public void pluginDeactivated(PluginProxy plugin) {
    updatePrograms();
  }

  private void updatePrograms() {
    programHasChanged();
    repaint();
  }

  public void pluginLoaded(PluginProxy plugin) {
    // noop
  }

  public void pluginUnloaded(PluginProxy plugin) {
    // noop
  }

  /**
   * get the tooltip text for the local mouse coordinates x and y
   *
   * @param x
   * @param y
   * @return
   */
  public String getToolTipText(int x, int y) {
    // tooltip for info icon area (on the left)
    if (mInfoIconRect != null && mInfoIconRect.contains(x, y)) {
      return getProgramInfoTooltip();
    }

    // tooltip for all marker icons
    Marker[] markers = mProgram.getMarkerArr();
    if (markers != null && markers.length > 0
        && x >= getWidth() - markers.length * 16 - 2) {
      int markerY = mTitleIcon.getIconHeight()
          + mDescriptionIcon.getIconHeight() + mPictureAreaIcon.getIconHeight()
          + 2;
      markerY = Math.min(markerY, getHeight() - 16);
      if ((y >= markerY) && (y <= markerY + 16)) {
        return getMarkedByTooltip();
      }
    }

    // show full title if the title has been shortened
    if (mTitleIcon.isTextCut()) {
      return mProgram.getTitle();
    }

    return null;
  }

  private String getMarkedByTooltip() {
    StringBuilder buffer = new StringBuilder();
    Marker[] markers = mProgram.getMarkerArr();
    for (int i = markers.length - 1; i >= 0; i--) {
      Marker marker = markers[i];
      String text = "";
      PluginAccess plugin = Plugin.getPluginManager()
          .getActivatedPluginForId(marker.getId());
      if (plugin != null) {
        text = plugin.getInfo().getName();
      } else {
        InternalPluginProxyIf internalPlugin = InternalPluginProxyList
            .getInstance().getProxyForId(marker.getId());
        if (internalPlugin != null) {
          text = internalPlugin.getName();
          if (internalPlugin.equals(FavoritesPluginProxy.getInstance())) {
            // if this is a favorite, add the names of the favorite
            String favTitles = "";
            for (Favorite favorite : FavoriteTreeModel.getInstance()
                .getFavoritesContainingProgram(mProgram)) {
              if (favTitles.length() > 0) {
                favTitles += ", ";
              }
              favTitles += favorite.getName();
            }
            if (favTitles.length() > 0) {
              text += " (" + favTitles + ')';
            }
          }
        }
      }
      if (text.length() > 0) {
        buffer.append(text);
        buffer.append("<br/>");
      }
    }
    if (buffer.length() > 0) {
      buffer.insert(0, "<html><b>" + mLocalizer.msg("markedBy", "Marked by")
          + "</b><br/>");
      buffer.append("</html>");
      return buffer.toString();
    }
    return null;
  }

  private String  getProgramInfoTooltip() {
    StringBuilder buffer = new StringBuilder("");
    // program info icons
    int info = mProgram.getInfo();
    if (info > 0) {
      int[] infoBitArr = ProgramInfoHelper.getInfoBits();
      String[] infoIconFileName = ProgramInfoHelper.getInfoIconFilenames();
      String[] infoMsgArr = ProgramInfoHelper.getInfoIconMessages();

      for (int i = 0; i < infoBitArr.length; i++) {
        if (ProgramInfoHelper.bitSet(info, infoBitArr[i])) {
          if (infoIconFileName[i] != null) {
            buffer.append(
                "<tr><td valign=\"middle\" align=\"center\"><img src=\"file:imgs/")
                .append(infoIconFileName[i]).append("\"></td><td>&nbsp;")
                .append(infoMsgArr[i]).append("</td></tr>");
          }
        }
      }
    }
    // plugin icons
    for (PluginProxy proxy : PluginProxyManager.getInstance().getActivatedPlugins()) {
      Icon[] pluginIcons = proxy.getProgramTableIcons(mProgram);
      if (pluginIcons != null && pluginIcons.length > 0) {
        buffer.append(
            "<tr><td valign=\"middle\" align=\"center\">&nbsp;</td><td>&nbsp;")
            .append(proxy.getProgramTableIconText()).append("</td></tr>");
      }
    }
    // HTML header and footer
    if (buffer.length() > 0) {
      buffer.insert(0, "<html><table cellpadding=\"1\">");
      buffer.append("</table></html>");
      return buffer.toString();
    }
    return null;
  }

  /**
   * get the X offset of the title in pixels
   *
   * @return
   */
  public int getTitleX() {
    return WIDTH_LEFT;
  }

  @Override
  public String getToolTipText(MouseEvent event) {
    return getToolTipText(event.getX(), event.getY());
  }

  @Override
  public void setSize(Dimension d) {
    super.setSize(d);
    setWidth(d.width);
  }

  @Override
  public void setSize(int width, int height) {
    super.setSize(width, height);
    setWidth(width);
  }

  public void setWidth(int newWidth) {
    int oldWidth = getWidth();
    int textIconWidth = getTextIconWidth(newWidth);
    if (oldWidth != newWidth && newWidth > 0 || (textIconWidth != mDescriptionIcon.getIconWidth() && textIconWidth > 0)) {
      recreateTextIcons(newWidth);
    }
  }

  private int getTextIconWidth(final int fullWidth) {
    return fullWidth - WIDTH_LEFT - 5;
  }

  /**
   * recreate the textual icons if the width of the program panel has changed
   * @param newWidth
   */
  private void recreateTextIcons(int newWidth) {
    int textIconWidth = getTextIconWidth(newWidth);
    mTitleIcon = new TextAreaIcon(null, mTitleFont, textIconWidth);
    mDescriptionIcon = new TextAreaIcon(null, mNormalFont, textIconWidth);
    mDescriptionIcon.setMaximumLineCount(3);
    if (mProgram != null) {
      mProgram.validateMarking();
      Program p = mProgram;
      mProgram = null;
      setProgram(p);
    }
  }


}